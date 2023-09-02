/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.common.data.UserRoleInfo;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.organization.business.OrganizationModuleService;
import com.flowcentraltech.flowcentral.organization.entities.MappedBranch;
import com.flowcentraltech.flowcentral.security.constants.LoginEventType;
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
import com.flowcentraltech.flowcentral.security.entities.UserLoginEvent;
import com.flowcentraltech.flowcentral.security.entities.UserQuery;
import com.flowcentraltech.flowcentral.security.entities.UserRole;
import com.flowcentraltech.flowcentral.security.entities.UserRoleQuery;
import com.flowcentraltech.flowcentral.security.templatewrappers.UserPasswordResetTemplateWrapper;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.entities.MappedTenant;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.SessionContext;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.UnifyCoreSessionAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.security.OneWayStringCryptograph;
import com.tcdng.unify.core.security.PasswordAutenticationService;
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
        implements SecurityModuleService, NotificationRecipientProvider {

    @Configurable
    private UserSessionManager userSessionManager;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private OrganizationModuleService organizationModuleService;

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    @Configurable
    private NotificationModuleService notificationModuleService;

    @Configurable("oneway-stringcryptograph")
    private OneWayStringCryptograph passwordCryptograph;

    public final void setUserSessionManager(UserSessionManager userSessionManager) {
        this.userSessionManager = userSessionManager;
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public final void setOrganizationModuleService(OrganizationModuleService organizationModuleService) {
        this.organizationModuleService = organizationModuleService;
    }

    public final void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
        this.fileAttachmentProvider = fileAttachmentProvider;
    }

    public final void setNotificationModuleService(NotificationModuleService notificationModuleService) {
        this.notificationModuleService = notificationModuleService;
    }

    public final void setPasswordCryptograph(OneWayStringCryptograph passwordCryptograph) {
        this.passwordCryptograph = passwordCryptograph;
    }

    @Override
    public List<User> findUsers(UserQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public User loginUser(String loginId, String password, Locale loginLocale, Long loginTenantId)
            throws UnifyException {
        final boolean isSystem = DefaultApplicationConstants.SYSTEM_LOGINID.equalsIgnoreCase(loginId);
        if (isTenancyEnabled()) {
            if (loginTenantId == null && systemModuleService.getTenantCount() > 0) {
                if (isSystem) {
                    loginTenantId = Entity.PRIMARY_TENANT_ID;
                } else {
                    throw new UnifyException(SecurityModuleErrorConstants.TENANCY_IS_REQUIRED);
                }
            }
        } else {
            loginTenantId = Entity.PRIMARY_TENANT_ID;
        }

        User user = isSystem ? environment().list(new UserQuery().id(DefaultApplicationConstants.SYSTEM_ENTITY_ID))
                : environment().list(new UserQuery().loginId(loginId).tenantId(loginTenantId));
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

        if (!isSystem && !Boolean.TRUE.equals(user.getLocalPasswordOnly()) && systemModuleService.getSysParameterValue(
                boolean.class, SecurityModuleSysParamConstants.ENABLE_THIRDPARTY_PASSWORD_AUTHENTICATION)) {
            PasswordAutenticationService passwordAuthService = (PasswordAutenticationService) getComponent(
                    ApplicationComponents.APPLICATION_PASSWORDAUTHENTICATIONSERVICE);
            if (!passwordAuthService.authenticate(loginId, user.getEmail(), password)) {
                throw new UnifyException(SecurityModuleErrorConstants.INVALID_LOGIN_ID_PASSWORD);
            }
        } else {
            password = passwordCryptograph.encrypt(password);
            if (user.getPassword() == null || !user.getPassword().equals(password)) {
                if (accountLockingEnabled && !user.isReserved()) { // No locking for reserved users
                    updateLoginAttempts(user);
                }

                throw new UnifyException(SecurityModuleErrorConstants.INVALID_LOGIN_ID_PASSWORD);
            }
        }

        // Update login details
        Date now = environment().getNow();
        Update update = new Update().add("lastLoginDt", now).add("loginAttempts", Integer.valueOf(0));
        Date paswwordExpiryDt = user.getPasswordExpiryDt();
        if (paswwordExpiryDt != null && paswwordExpiryDt.before(now)) {
            update.add("changePassword", Boolean.TRUE);
            user.setChangePassword(Boolean.TRUE);
        }
        environment().updateAll(new UserQuery().id(user.getId()), update);
        // Log login event
        final boolean keepUserLoginEvents = systemModuleService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.KEEP_USER_LOGIN_EVENTS);
        if (keepUserLoginEvents) {
            SessionContext ctx = getSessionContext();
            UserLoginEvent userLoginEvent = new UserLoginEvent();
            userLoginEvent.setEventType(LoginEventType.LOGIN);
            userLoginEvent.setUserId(user.getId());
            userLoginEvent.setRemoteAddress(ctx.getRemoteAddress());
            userLoginEvent.setRemoteHost(ctx.getRemoteHost());
            environment().create(userLoginEvent);
        }

        MappedTenant mappedTenant = systemModuleService.findPrimaryMappedTenant(loginTenantId);
        String businessUnitDesc = mappedTenant != null ? mappedTenant.getDescription() : null;
        if (StringUtils.isBlank(businessUnitDesc)) {
            businessUnitDesc = getApplicationMessage("application.no.businessunit");
        }

        // Set session locale
        SessionContext sessionCtx = getSessionContext();
        final MappedBranch userBranch = user.getBranchId() != null
                ? environment().find(MappedBranch.class, user.getBranchId())
                : null;
        if (loginLocale != null) {
            sessionCtx.setLocale(loginLocale);
        } else if (userBranch != null && StringUtils.isNotBlank(userBranch.getLanguageTag())) {
            sessionCtx.setLocale(Locale.forLanguageTag(userBranch.getLanguageTag()));
        }

        // Set session time zone
        if (userBranch != null && StringUtils.isNotBlank(userBranch.getTimeZone())) {
            sessionCtx.setTimeZone(TimeZone.getTimeZone(userBranch.getTimeZone()));
        } else {
            sessionCtx.setTimeZone(getApplicationTimeZone());
        }

        sessionCtx.setUseDaylightSavings(sessionCtx.getTimeZone().inDaylightTime(now));

        // Login to session and set session attributes
        userSessionManager.login(createUserToken(user, userBranch, loginTenantId));
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.USERNAME, user.getFullName());
        String branchDesc = userBranch != null ? userBranch.getDescription() : null;
        if (StringUtils.isBlank(branchDesc)) {
            branchDesc = getApplicationMessage("application.no.branch");
        }

        String dateFormatUpl = mappedTenant != null && !StringUtils.isBlank(mappedTenant.getDateFormat())
                ? "!fixeddatetimeformat pattern:$s{" + mappedTenant.getDateFormat() + "}"
                : null;

        final boolean globalAccounting = systemModuleService.getSysParameterValue(boolean.class,
                SystemModuleSysParamConstants.SYSTEM_GLOBAL_ACCOUNTING_INPUT_ENABLED);
        final boolean useTenantDateFormat = systemModuleService.getSysParameterValue(boolean.class,
                SystemModuleSysParamConstants.SYSTEM_USE_TENANT_DATEFORMAT);

        setSessionStickyAttribute(UnifyWebSessionAttributeConstants.MESSAGEBOX, null);
        setSessionStickyAttribute(UnifyWebSessionAttributeConstants.TASKMONITORINFO, null);
        setSessionStickyAttribute(UnifyCoreSessionAttributeConstants.INPUT_GLOBAL_ACCOUNTING_FLAG, globalAccounting);
        setSessionStickyAttribute(UnifyCoreSessionAttributeConstants.OVERRIDE_WIDGET_DATEFORMAT_FLAG,
                useTenantDateFormat);
        setSessionStickyAttribute(UnifyCoreSessionAttributeConstants.WIDGET_DATEFORMAT_OVERRIDE, dateFormatUpl);
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.BRANCHDESC, branchDesc);
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.BUSINESSUNITDESC, businessUnitDesc);
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.RESERVEDFLAG, user.isReserved());
        setSessionStickyAttribute(FlowCentralSessionAttributeConstants.SUPERVISORFLAG, user.getSupervisor());
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
        // Log login event
        final boolean keepUserLoginEvents = systemModuleService.getSysParameterValue(boolean.class,
                SecurityModuleSysParamConstants.KEEP_USER_LOGIN_EVENTS);
        if (keepUserLoginEvents) {
            Long userId = environment().value(Long.class, "id",
                    new UserQuery().loginId(getUserToken().getUserLoginId()));
            SessionContext ctx = getSessionContext();
            UserLoginEvent userLoginEvent = new UserLoginEvent();
            userLoginEvent.setEventType(LoginEventType.LOGOUT);
            userLoginEvent.setUserId(userId);
            userLoginEvent.setRemoteAddress(ctx.getRemoteAddress());
            userLoginEvent.setRemoteHost(ctx.getRemoteHost());
            environment().create(userLoginEvent);
        }

        // Logout
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
    public Recipient getRecipientByLoginId(Long tenantId, NotifType type, String userLoginId) throws UnifyException {
        User user = environment().find(new UserQuery().loginId(userLoginId).tenantId(tenantId));
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
    public List<Recipient> getRecipientsByRole(Long tenantId, NotifType type, String roleCode) throws UnifyException {
        return getRecipientsByRole(tenantId, type, Arrays.asList(roleCode));
    }

    @Override
    public List<Recipient> getRecipientsByRole(Long tenantId, NotifType type, Collection<String> roles)
            throws UnifyException {
        Set<Long> userIdSet = findIdsOfAllUsersWithRole(tenantId, roles);
        if (!userIdSet.isEmpty()) {
            List<User> userList = environment().findAll(new UserQuery().idIn(userIdSet));
            List<Recipient> recipientList = new ArrayList<Recipient>();
            switch (type) {
                case EMAIL:
                    for (User user : userList) {
                        recipientList.add(new Recipient(user.getLoginId(), user.getEmail()));
                    }
                    break;
                case SMS:
                    for (User user : userList) {
                        recipientList.add(new Recipient(user.getLoginId(), user.getMobileNo()));
                    }
                    break;
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
    public User findUser(String userLoginId) throws UnifyException {
        return environment().list(new UserQuery().loginId(userLoginId));
    }

    @Override
    public String getUserFullName(String userLoginId) throws UnifyException {
        List<String> fullNameList = environment().valueList(String.class, "fullName",
                new UserQuery().loginId(userLoginId));
        return !DataUtils.isBlank(fullNameList) ? fullNameList.get(0) : null;
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

    private Set<Long> findIdsOfAllUsersWithRole(final Long tenantId, final Collection<String> roleCodes)
            throws UnifyException {
        Set<Long> userIds = new HashSet<Long>();
        if (!DataUtils.isBlank(roleCodes)) {
            userIds.addAll(environment().valueSet(Long.class, "userId",
                    new UserRoleQuery().roleCodeIn(roleCodes).tenantId(tenantId)));

            Set<Long> userGroupIds = environment().valueSet(Long.class, "userGroupId",
                    new UserGroupRoleQuery().roleCodeIn(roleCodes).tenantId(tenantId));
            if (!DataUtils.isBlank(userGroupIds)) {
                userIds.addAll(environment().valueSet(Long.class, "userId",
                        new UserGroupMemberQuery().userGroupIdIn(userGroupIds)));
            }
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

        final FactoryMap<Long, String> departmentCodes = organizationModuleService.getMappedDepartmentCodeFactoryMap();
        for (UserRole userRole : environment().listAll(userRoleQuery)) {
            userRoleInfoList.add(new UserRoleInfo(userRole.getRoleCode(), userRole.getRoleDesc(),
                    departmentCodes.get(userRole.getDepartmentId())));
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
                        departmentCodes.get(userGroupRole.getDepartmentId())));
            }
        }

        return userRoleInfoList;
    }

    private UserToken createUserToken(User user, MappedBranch userBranch, Long loginTenantId) throws UnifyException {
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
                .userEmail(user.getEmail()).userId((Long) user.getId()).tenantId(loginTenantId)
                .ipAddress(getSessionContext().getRemoteAddress())
                .branchCode(userBranch != null ? userBranch.getCode() : null)
                .zoneCode(userBranch != null ? userBranch.getZoneCode() : null).colorScheme(colorScheme)
                .globalAccess(globalAccess).reservedUser(user.isReserved()).allowMultipleLogin(allowMultipleLogin)
                .build();
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
            UserPasswordResetTemplateWrapper wrapper = notificationModuleService
                    .wrapperOfNotifTemplate(UserPasswordResetTemplateWrapper.class);
            wrapper.setFullName(user.getFullName());
            wrapper.setLoginId(user.getLoginId());
            wrapper.setPlainPassword(password);
            wrapper.addTORecipient(user.getFullName(), user.getEmail());
            notificationModuleService.sendNotification(wrapper.getMessage());
        }

        return password;
    }

}
