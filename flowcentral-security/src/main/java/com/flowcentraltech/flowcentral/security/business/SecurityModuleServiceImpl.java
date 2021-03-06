/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.flowcentraltech.flowcentral.security.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.business.NotificationRecipientProvider;
import com.flowcentraltech.flowcentral.common.business.RoleParticipatingUserProvider;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Dictionary;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.common.data.UserRoleInfo;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.data.NotificationChannelMessage;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleAttachmentConstants;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleEntityConstants;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleErrorConstants;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleNameConstants;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleSysParamConstants;
import com.flowcentraltech.flowcentral.security.constants.UserWorkflowStatus;
import com.flowcentraltech.flowcentral.security.entities.PasswordHistory;
import com.flowcentraltech.flowcentral.security.entities.PasswordHistoryQuery;
import com.flowcentraltech.flowcentral.security.entities.User;
import com.flowcentraltech.flowcentral.security.entities.UserGroupMemberQuery;
import com.flowcentraltech.flowcentral.security.entities.UserGroupRole;
import com.flowcentraltech.flowcentral.security.entities.UserGroupRoleQuery;
import com.flowcentraltech.flowcentral.security.entities.UserQuery;
import com.flowcentraltech.flowcentral.security.entities.UserRole;
import com.flowcentraltech.flowcentral.security.entities.UserRoleQuery;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.tcdng.unify.core.SessionContext;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.security.OneWayStringCryptograph;
import com.tcdng.unify.core.security.PasswordGenerator;
import com.tcdng.unify.core.system.UserSessionManager;
import com.tcdng.unify.core.util.ColorUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;

/**
 * Implementation of security module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(SecurityModuleNameConstants.SECURITY_MODULE_SERVICE)
public class SecurityModuleServiceImpl extends AbstractFlowCentralService
        implements SecurityModuleService, NotificationRecipientProvider, RoleParticipatingUserProvider {

    @Configurable
    private UserSessionManager userSessionManager;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    @Configurable
    private NotificationModuleService notificationModuleService;

    @Configurable("oneway-stringcryptograph")
    private OneWayStringCryptograph passwordCryptograph;

    public void setUserSessionManager(UserSessionManager userSessionManager) {
        this.userSessionManager = userSessionManager;
    }

    public void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
        this.fileAttachmentProvider = fileAttachmentProvider;
    }

    public void setNotificationModuleService(NotificationModuleService notificationModuleService) {
        this.notificationModuleService = notificationModuleService;
    }

    public void setPasswordCryptograph(OneWayStringCryptograph passwordCryptograph) {
        this.passwordCryptograph = passwordCryptograph;
    }

    @Override
    public List<User> findUsers(UserQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public User loginUser(String loginId, String password, Locale loginLocale) throws UnifyException {
        User user = environment().list(new UserQuery().loginId(loginId));
        if (user == null) {
            throw new UnifyException(SecurityModuleErrorConstants.INVALID_LOGIN_ID_PASSWORD);
        }

        boolean accountLockingEnabled = systemModuleService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.ENABLE_ACCOUNT_LOCKING);
        if (accountLockingEnabled && user.getLoginLocked()) {
            throw new UnifyException(SecurityModuleErrorConstants.USER_ACCOUNT_IS_LOCKED);
        }

        if (!UserWorkflowStatus.APPROVED.equals(user.getWorkflowStatus())) {
            throw new UnifyException(SecurityModuleErrorConstants.USER_ACCOUNT_NOT_APPROVED);
        }
        
        if (!RecordStatus.ACTIVE.equals(user.getStatus())) {
            throw new UnifyException(SecurityModuleErrorConstants.USER_ACCOUNT_NOT_ACTIVE);
        }

        password = passwordCryptograph.encrypt(password);
        if (user.getPassword() == null || !user.getPassword().equals(password)) {
            if (accountLockingEnabled && !user.isReserved()) { // No locking for reserved users
                updateLoginAttempts(user);
            }

            throw new UnifyException(SecurityModuleErrorConstants.INVALID_LOGIN_ID_PASSWORD);
        }

        Date now = environment().getNow();
        Update update = new Update().add("lastLoginDt", now).add("loginAttempts", Integer.valueOf(0));
        Date paswwordExpiryDt = user.getPasswordExpiryDt();
        if (paswwordExpiryDt != null && paswwordExpiryDt.before(now)) {
            update.add("changePassword", Boolean.TRUE);
            user.setChangePassword(Boolean.TRUE);
        }
        environment().updateAll(new UserQuery().id(user.getId()), update);

        // Set session locale
        SessionContext sessionCtx = getSessionContext();
        if (loginLocale != null) {
            sessionCtx.setLocale(loginLocale);
        } else if (StringUtils.isNotBlank(user.getBranchLanguageTag())) {
            sessionCtx.setLocale(Locale.forLanguageTag(user.getBranchLanguageTag()));
        }

        // Set session time zone
        if (StringUtils.isNotBlank(user.getBranchTimeZone())) {
            sessionCtx.setTimeZone(TimeZone.getTimeZone(user.getBranchTimeZone()));
        } else {
            sessionCtx.setTimeZone(getApplicationTimeZone());
        }

        sessionCtx.setUseDaylightSavings(sessionCtx.getTimeZone().inDaylightTime(now));

        // Login to session and set session attributes
        userSessionManager.login(createUserToken(user));
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.USERNAME, user.getFullName());
        String branchDesc = user.getBranchDesc();
        if (StringUtils.isBlank(branchDesc)) {
            branchDesc = getApplicationMessage("application.no.branch");
        }

        setSessionStickyAttribute(UnifyWebSessionAttributeConstants.MESSAGEBOX, null);
        setSessionStickyAttribute(UnifyWebSessionAttributeConstants.TASKMONITORINFO, null);
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.BRANCHDESC, branchDesc);
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.RESERVEDFLAG, user.isReserved());
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.SUPERVISORFLAG, user.getSupervisor());
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.USERROLEOPTIONS, null);
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.REPORTOPTIONS, null);
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.SHORTCUTDECK, null);
        return user;
    }

    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public void updateLoginAttempts(User user) throws UnifyException {
        int loginAttempts = user.getLoginAttempts() + 1;
        Update update = new Update();
        update.add("loginAttempts", Integer.valueOf(loginAttempts));
        if (systemModuleService.getSysParameterValue(int.class,
                SecurityModuleSysParamConstants.MAXIMUM_LOGIN_TRIES) <= loginAttempts) {
            update.add("loginLocked", Boolean.TRUE);
        }

        environment().updateAll(new UserQuery().id(user.getId()), update);
    }

    @Override
    public void logoutUser(boolean complete) throws UnifyException {
        userSessionManager.logout(complete);
    }

    @Override
    public void changeUserPassword(String oldPassword, String newPassword) throws UnifyException {
        oldPassword = passwordCryptograph.encrypt(oldPassword);
        User user = environment().find(new UserQuery().password(oldPassword).loginId(getUserToken().getUserLoginId()));
        if (user == null) {
            throw new UnifyException(SecurityModuleErrorConstants.INVALID_OLD_PASSWORD);
        }

        Long userId = user.getId();
        newPassword = passwordCryptograph.encrypt(newPassword);
        if (systemModuleService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.ENABLE_PASSWORD_HISTORY)) {
            PasswordHistoryQuery query = new PasswordHistoryQuery().userId(userId).password(newPassword);
            if (environment().countAll(query) > 0) {
                throw new UnifyException(SecurityModuleErrorConstants.NEW_PASSWORD_IS_STALE);
            }

            query.clear();
            query.userId(userId);
            query.orderById();
            List<Long> passwordHistoryIdList = environment().valueList(Long.class, "id", query);
            if (passwordHistoryIdList.size() >= systemModuleService.getSysParameterValue(int.class,
                    SecurityModuleSysParamConstants.PASSWORD_HISTORY_LENGTH)) {
                environment().delete(PasswordHistory.class, passwordHistoryIdList.get(0));
            }

            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setUserId(userId);
            passwordHistory.setPassword(oldPassword);
            environment().create(passwordHistory);
        }

        // Update user
        user.setPassword(newPassword);
        user.setPasswordExpiryDt(null);
        user.setChangePassword(Boolean.FALSE);
        environment().updateByIdVersion(user);
    }

    @Override
    public void resetUserPassword(Long userId) throws UnifyException {
        User user = environment().findLean(User.class, userId);
        String password = generatePasswordAndSendEmail(user);
        password = passwordCryptograph.encrypt(password);
        user.setPassword(password);
        user.setChangePassword(Boolean.TRUE);
        user.setLoginLocked(Boolean.FALSE);
        user.setLoginAttempts(Integer.valueOf(0));
        environment().updateLeanByIdVersion(user);
    }

    @Override
    public void unlockUser(Long userId) throws UnifyException {
        User user = environment().findLean(User.class, userId);
        user.setLoginLocked(Boolean.FALSE);
        user.setLoginAttempts(Integer.valueOf(0));
        environment().updateLeanByIdVersion(user);
    }

    @Override
    public List<UserRole> findUserRoles(UserRoleQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<UserRoleInfo> getAvailableUserRolesActiveNow(String userLoginId, String excludeRoleCode)
            throws UnifyException {
        return findConsolidatedUserRoles(userLoginId, excludeRoleCode, getNow());
    }

    @Override
    public List<UserRoleInfo> findConsolidatedUserRoles(String userLoginId, Date activeAt) throws UnifyException {
        return findConsolidatedUserRoles(userLoginId, null, activeAt);
    }

    @Override
    public Recipient getRecipientByLoginId(NotificationType type, String userLoginId) throws UnifyException {
        User user = environment().find(new UserQuery().loginId(userLoginId));
        switch (type) {
            case EMAIL:
                return new Recipient(userLoginId, user.getEmail());
            case SMS:
                return new Recipient(userLoginId, user.getMobileNo());
            case SYSTEM:
            default:
                return new Recipient(userLoginId, userLoginId);
        }
    }

    @Override
    public List<Recipient> getRecipientsByRole(NotificationType type, String roleCode) throws UnifyException {
        return getRecipientsByRole(type, Arrays.asList(roleCode));
    }

    @Override
    public List<Recipient> getRecipientsByRole(NotificationType type, Collection<String> roles) throws UnifyException {
        Set<Long> userIdSet = findIdsOfAllUsersWithRole(roles);
        if (!userIdSet.isEmpty()) {
            List<User> userList = environment().findAll(new UserQuery().idIn(userIdSet));
            List<Recipient> recipientList = new ArrayList<Recipient>();
            switch (type) {
                case EMAIL:
                    for (User user : userList) {
                        recipientList.add(new Recipient(user.getLoginId(), user.getEmail()));
                    }
                case SMS:
                    for (User user : userList) {
                        recipientList.add(new Recipient(user.getLoginId(), user.getMobileNo()));
                    }
                case SYSTEM:
                default:
                    for (User user : userList) {
                        recipientList.add(new Recipient(user.getLoginId(), user.getLoginId()));
                    }
            }

            return recipientList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<String> getParticipatingUsersByRole(String branchCode, String roleCode) throws UnifyException {
        return getParticipatingUsersByRole(branchCode, Arrays.asList(roleCode));
    }

    @Override
    public List<String> getParticipatingUsersByRole(String branchCode, Collection<String> roles) throws UnifyException {
        Set<Long> userIdSet = findIdsOfAllUsersWithRole(roles);
        if (!userIdSet.isEmpty()) {
            UserQuery userQuery = new UserQuery().idIn(userIdSet);
            if (branchCode != null) {
                userQuery.branchCode(branchCode);
            }

            return environment().valueList(String.class, "loginId", userQuery);
        }

        return Collections.emptyList();
    }

    @Override
    public User findUser(String userLoginId) throws UnifyException {
        return environment().list(new UserQuery().loginId(userLoginId));
    }

    @Override
    public byte[] findUserPhotograph(String userLoginId) throws UnifyException {
        Long userId = environment().value(Long.class, "id", new UserQuery().loginId(userLoginId));
        Attachment attachment = fileAttachmentProvider.retrieveFileAttachment("work",
                SecurityModuleEntityConstants.USER_ENTITY_NAME, userId, SecurityModuleAttachmentConstants.PHOTO);
        if (attachment != null) {
            return attachment.getData();
        }

        return null;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {
        installDefaultUsers(moduleInstall);
    }

    private void installDefaultUsers(final ModuleInstall moduleInstall) throws UnifyException {
        if (SecurityModuleNameConstants.SECURITY_MODULE_NAME.equals(moduleInstall.getModuleConfig().getName())) {
            logInfo("Installing default users ...");
            String email = systemModuleService.getSysParameterValue(String.class,
                    SystemModuleSysParamConstants.SYSTEM_EMAIL);
            if (environment().countAll(new UserQuery().id(DefaultApplicationConstants.SYSTEM_ENTITY_ID)) == 0) {
                User user = new User(DefaultApplicationConstants.SYSTEM_ENTITY_ID,
                        DefaultApplicationConstants.SYSTEM_FULLNAME, DefaultApplicationConstants.SYSTEM_LOGINID, email,
                        Boolean.FALSE);
                String password = generatePasswordAndSendEmail(user);
                user.setWorkflowStatus(UserWorkflowStatus.APPROVED);
                user.setPassword(passwordCryptograph.encrypt(password));
                environment().create(user);
            } else {
                environment().updateById(User.class, DefaultApplicationConstants.SYSTEM_ENTITY_ID,
                        new Update().add("email", email));
            }
        }
    }

    private Set<Long> findIdsOfAllUsersWithRole(final Collection<String> roleCodes) throws UnifyException {
        Set<Long> userIds = new HashSet<Long>();
        if (!DataUtils.isBlank(roleCodes)) {
            userIds.addAll(environment().valueSet(Long.class, "userId", new UserRoleQuery().roleCodeIn(roleCodes)));
        }

        Set<Long> userGroupIds = environment().valueSet(Long.class, "userGroupId",
                new UserGroupRoleQuery().roleCodeIn(roleCodes));
        if (!DataUtils.isBlank(userGroupIds)) {
            userIds.addAll(environment().valueSet(Long.class, "userId",
                    new UserGroupMemberQuery().userGroupIdIn(userGroupIds)));
        }

        return userIds;
    }

    private List<UserRoleInfo> findConsolidatedUserRoles(String userLoginId, String excludeRoleCode, Date activeAt)
            throws UnifyException {
        List<UserRoleInfo> userRoleInfoList = new ArrayList<UserRoleInfo>();
        // Get user roles
        Set<String> roleSet = new HashSet<String>();
        UserRoleQuery userRoleQuery = new UserRoleQuery();
        userRoleQuery.userLoginId(userLoginId);
        userRoleQuery.roleStatus(RecordStatus.ACTIVE);
        if (excludeRoleCode != null) {
            userRoleQuery.roleCodeNot(excludeRoleCode);
        }

        if (activeAt != null) {
            userRoleQuery.roleActiveTime(activeAt);
        }

        for (UserRole userRole : environment().listAll(userRoleQuery)) {
            userRoleInfoList.add(
                    new UserRoleInfo(userRole.getRoleCode(), userRole.getRoleDesc(), userRole.getDepartmentCode()));
            roleSet.add(userRole.getRoleCode());
        }

        // Add Group Roles
        List<Long> userGroupIdList = environment().valueList(Long.class, "userGroupId",
                new UserGroupMemberQuery().userLoginId(userLoginId));
        if (!DataUtils.isBlank(userGroupIdList)) {
            UserGroupRoleQuery userGroupRoleQuery = new UserGroupRoleQuery();
            userGroupRoleQuery.userGroupIdIn(userGroupIdList);
            userGroupRoleQuery.roleStatus(RecordStatus.ACTIVE);
            if (excludeRoleCode != null) {
                userGroupRoleQuery.roleCodeNot(excludeRoleCode);
            }

            if (activeAt != null) {
                userGroupRoleQuery.roleActiveTime(activeAt);
            }

            if (!roleSet.isEmpty()) {
                userGroupRoleQuery.roleCodeNotIn(roleSet);
            }

            for (UserGroupRole userGroupRole : environment().listAll(userGroupRoleQuery)) {
                userRoleInfoList.add(new UserRoleInfo(userGroupRole.getRoleCode(), userGroupRole.getRoleDesc(),
                        userGroupRole.getUserGroupName(), userGroupRole.getUserGroupDesc(),
                        userGroupRole.getDepartmentCode()));
            }
        }

        return userRoleInfoList;
    }

    private UserToken createUserToken(User user) throws UnifyException {
        boolean allowMultipleLogin = user.isReserved();
        if (!allowMultipleLogin) {
            allowMultipleLogin = Boolean.TRUE.equals(user.getAllowMultipleLogin());
        }

        if (systemModuleService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.ENABLE_SYSTEMWIDE_MULTILOGIN_RULE)) {
            allowMultipleLogin = systemModuleService.getSysParameterValue(boolean.class,
                    SecurityModuleSysParamConstants.SYSTEMWIDE_MULTILOGIN);
        }

        boolean globalAccess = user.isReserved();
        String colorScheme = ColorUtils.getConformingColorSchemeCode(
                getContainerSetting(String.class, UnifyCorePropertyConstants.APPLICATION_COLORSCHEME));
        return UserToken.newBuilder().userLoginId(user.getLoginId()).userName(user.getFullName())
                .ipAddress(getSessionContext().getRemoteAddress()).branchCode(user.getBranchCode())
                .zoneCode(user.getZoneCode()).colorScheme(colorScheme).globalAccess(globalAccess)
                .reservedUser(user.isReserved()).allowMultipleLogin(allowMultipleLogin).build();
    }

    private String generatePasswordAndSendEmail(User user) throws UnifyException {
        PasswordGenerator passwordGenerator = (PasswordGenerator) getComponent(systemModuleService
                .getSysParameterValue(String.class, SecurityModuleSysParamConstants.USER_PASSWORD_GENERATOR));
        int passwordLength = systemModuleService.getSysParameterValue(int.class,
                SecurityModuleSysParamConstants.USER_PASSWORD_LENGTH);

        String password = passwordGenerator.generatePassword(user.getLoginId(), passwordLength);

        // Send email if necessary
        if (systemModuleService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.USER_PASSWORD_SEND_EMAIL)) {
            Dictionary dictionary = new Dictionary();
            dictionary.setValue("fullName", user.getFullName());
            dictionary.setValue("loginId", user.getLoginId());
            dictionary.setValue("plainPassword", password);
            NotificationChannelMessage msg = notificationModuleService.constructNotificationChannelMessage(
                    "security.userPasswordReset", dictionary, new Recipient(user.getFullName(), user.getEmail()));
            notificationModuleService.sendNotification(msg);
        }

        return password;
    }

}
