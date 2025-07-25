/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.organization.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.PostBootSetup;
import com.flowcentraltech.flowcentral.common.business.RolePrivilegeBackupAgent;
import com.flowcentraltech.flowcentral.common.business.StudioProvider;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.data.BranchInfo;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.organization.constants.BranchViewType;
import com.flowcentraltech.flowcentral.organization.constants.OrganizationModuleNameConstants;
import com.flowcentraltech.flowcentral.organization.entities.Branch;
import com.flowcentraltech.flowcentral.organization.entities.BranchQuery;
import com.flowcentraltech.flowcentral.organization.entities.Department;
import com.flowcentraltech.flowcentral.organization.entities.DepartmentQuery;
import com.flowcentraltech.flowcentral.organization.entities.MappedBranch;
import com.flowcentraltech.flowcentral.organization.entities.MappedBranchQuery;
import com.flowcentraltech.flowcentral.organization.entities.MappedDepartment;
import com.flowcentraltech.flowcentral.organization.entities.MappedDepartmentQuery;
import com.flowcentraltech.flowcentral.organization.entities.Privilege;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeCategory;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeCategoryQuery;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeQuery;
import com.flowcentraltech.flowcentral.organization.entities.Role;
import com.flowcentraltech.flowcentral.organization.entities.RolePrivilege;
import com.flowcentraltech.flowcentral.organization.entities.RolePrivilegeQuery;
import com.flowcentraltech.flowcentral.organization.entities.RoleQuery;
import com.tcdng.unify.common.constants.WfItemVersionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.StaleableFactoryMap;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Implementation of organization module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component(OrganizationModuleNameConstants.ORGANIZATION_MODULE_SERVICE)
public class OrganizationModuleServiceImpl extends AbstractFlowCentralService
        implements OrganizationModuleService, ApplicationPrivilegeManager, PostBootSetup {

    private static final String ASSIGN_PRIVILEGE_LOCK = "org::assignprivtorole";

    private static final String DEVOPS_DEPARTMENT_CODE = "DEVOPS";

    private static final String DEVOPS_DEVELOPER_CODE = "DEVELOPER";

    private static final String DEVOPS_JUNIOR_DEVELOPER_CODE = "JDEVELOPER";

    private final FactoryMap<Long, TenantRolePrivileges> tenantRolePrivileges;

    private final Map<String, Set<String>> privilegeBackup;

    private List<RolePrivilegeBackupAgent> roleBackupAgentList;

    @Configurable
    private StudioProvider studioProvider;

    public OrganizationModuleServiceImpl() {
        this.tenantRolePrivileges = new FactoryMap<Long, TenantRolePrivileges>()
            {
                @Override
                protected TenantRolePrivileges create(Long tenantId, Object... arg2) throws Exception {
                    return new TenantRolePrivileges();
                }
            };

        this.privilegeBackup = new ConcurrentHashMap<String, Set<String>>();
    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {
        logDebug("Clearing definitions cache...");
        tenantRolePrivileges.clear();
        logDebug("Definitions cache clearing successfully completed.");
    }

    @Override
    public FactoryMap<Long, String> getMappedBranchCodeFactoryMap() throws UnifyException {
        return new FactoryMap<Long, String>()
            {
                @Override
                protected String create(Long branchId, Object... arg2) throws Exception {
                    return environment().value(String.class, "code", new MappedBranchQuery().id(branchId));
                }
            };
    }

    @Override
    public FactoryMap<Long, String> getMappedDepartmentCodeFactoryMap() throws UnifyException {
        return new FactoryMap<Long, String>()
            {
                @Override
                protected String create(Long departmentId, Object... arg2) throws Exception {
                    return environment().value(String.class, "code", new MappedDepartmentQuery().id(departmentId));
                }
            };
    }

    @Override
    public String getMappedDepartmentCode(Long departmentId) throws UnifyException {
        return environment().value(String.class, "code", new MappedDepartmentQuery().id(departmentId));
    }

    @Override
    public String getMappedBranchCode(Long branchId) throws UnifyException {
        return environment().value(String.class, "code", new MappedBranchQuery().id(branchId));
    }

    @Override
    public List<MappedDepartment> findMappedDepartments(MappedDepartmentQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<MappedBranch> findMappedBranches(MappedBranchQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public Department findDepartment(DepartmentQuery query) throws UnifyException {
        return environment().find(query);
    }

    @Override
    public List<Role> findRoles(RoleQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public String getRoleCode(Long roleId) throws UnifyException {
        return environment().value(String.class, "code", new RoleQuery().id(roleId));
    }

    @Override
    public List<PrivilegeCategory> findPrivilegeCategories(PrivilegeCategoryQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<Privilege> findPrivileges(PrivilegeQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public void registerPrivilege(ConfigType configType, Long applicationId, String privilegeCategoryCode,
            String privilegeCode, String privilegeDesc) throws UnifyException {
        Long privilegeCategoryId = environment().value(Long.class, "id",
                new PrivilegeCategoryQuery().code(privilegeCategoryCode));
        Privilege oldPrivilege = environment().find(new PrivilegeQuery().privilegeCategoryId(privilegeCategoryId)
                .code(privilegeCode).applicationId(applicationId));
        if (oldPrivilege == null) {
            Privilege privilege = new Privilege();
            privilege.setApplicationId(applicationId);
            privilege.setPrivilegeCategoryId(privilegeCategoryId);
            privilege.setCode(privilegeCode);
            privilege.setDescription(privilegeDesc);
            privilege.setConfigType(configType);
            environment().create(privilege);
        } else {
            oldPrivilege.setDescription(privilegeDesc);
            oldPrivilege.setConfigType(configType);
            environment().updateByIdVersion(oldPrivilege);
        }
    }

    @Override
    public boolean unregisterPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode)
            throws UnifyException {
        Privilege oldPrivilege = environment().find(new PrivilegeQuery().privilegeCatCode(privilegeCategoryCode)
                .code(privilegeCode).applicationId(applicationId));
        if (oldPrivilege != null) {
            environment().deleteAll(new RolePrivilegeQuery().privilegeId(oldPrivilege.getId()));
            environment().deleteByIdVersion(oldPrivilege);
            return true;
        }

        return false;
    }

    @Override
    public void unregisterApplicationPrivileges(Long applicationId) throws UnifyException {
        environment().deleteAll(new RolePrivilegeQuery().applicationId(applicationId));
        environment().deleteAll(new PrivilegeQuery().applicationId(applicationId));

        if (!DataUtils.isBlank(roleBackupAgentList)) {
            for (RolePrivilegeBackupAgent rolePrivilegeBackupAgent : roleBackupAgentList) {
                rolePrivilegeBackupAgent.unregisterApplicationRolePrivileges(applicationId);
            }
        }
    }

    @Override
    public void unregisterCustomApplicationPrivileges(Long applicationId) throws UnifyException {
        environment().deleteAll(new RolePrivilegeQuery().applicationId(applicationId).isCustom());
        environment().deleteAll(new PrivilegeQuery().applicationId(applicationId).isCustom());

        if (!DataUtils.isBlank(roleBackupAgentList)) {
            for (RolePrivilegeBackupAgent rolePrivilegeBackupAgent : roleBackupAgentList) {
                rolePrivilegeBackupAgent.unregisterCustomApplicationRolePrivileges(applicationId);
            }
        }
    }

    @Override
    public boolean isRegisteredPrivilege(String privilegeCategoryCode, String privilegeCode) throws UnifyException {
        return environment()
                .countAll(new PrivilegeQuery().privilegeCatCode(privilegeCategoryCode).code(privilegeCode)) > 0;
    }

    @Override
    public void backupApplicationRolePrivileges(Long applicationId) throws UnifyException {
        List<RolePrivilege> rolePrivilegeList = environment()
                .listAll(new RolePrivilegeQuery().applicationId(applicationId).addSelect("roleCode", "privilegeCode"));
        for (RolePrivilege rolePrivilege : rolePrivilegeList) {
            Set<String> privilegeCodes = privilegeBackup.get(rolePrivilege.getRoleCode());
            if (privilegeCodes == null) {
                privilegeCodes = new HashSet<String>();
                privilegeBackup.put(rolePrivilege.getRoleCode(), privilegeCodes);
            }

            privilegeCodes.add(rolePrivilege.getPrivilegeCode());
        }

        if (!DataUtils.isBlank(roleBackupAgentList)) {
            for (RolePrivilegeBackupAgent rolePrivilegeBackupAgent : roleBackupAgentList) {
                rolePrivilegeBackupAgent.backupApplicationRolePrivileges(applicationId);
            }
        }
    }

    @Override
    public void restoreApplicationRolePrivileges() throws UnifyException {
        RolePrivilege rolePrivilege = new RolePrivilege();
        for (Map.Entry<String, Set<String>> entry : privilegeBackup.entrySet()) {
            final Long roleId = environment().value(Long.class, "id", new RoleQuery().code(entry.getKey()));
            for (String privilegeCode : entry.getValue()) {
                Optional<Long> privilegeId = environment().valueOptional(Long.class, "id",
                        new PrivilegeQuery().code(privilegeCode));
                if (privilegeId.isPresent()) {
                    if (environment()
                            .countAll(new RolePrivilegeQuery().privilegeId(privilegeId.get()).roleId(roleId)) == 0) {
                        rolePrivilege.setId(null);
                        rolePrivilege.setRoleId(roleId);
                        rolePrivilege.setPrivilegeId(privilegeId.get());
                        environment().create(rolePrivilege);
                    }
                }
            }
        }

        if (!DataUtils.isBlank(roleBackupAgentList)) {
            for (RolePrivilegeBackupAgent rolePrivilegeBackupAgent : roleBackupAgentList) {
                rolePrivilegeBackupAgent.restoreApplicationRolePrivileges();
            }
        }

        privilegeBackup.clear();
    }

    @Override
    public List<String> findRolePrivileges(String privilegeCategoryCode, String roleCode) throws UnifyException {
        if (getUserToken().isReservedUser()) {
            return environment().valueList(String.class, "code",
                    new PrivilegeQuery().privilegeCatCode(privilegeCategoryCode));
        }

        return environment().valueList(String.class, "privilegeCode",
                new RolePrivilegeQuery().roleWfItemVersionType(WfItemVersionType.ORIGINAL)
                        .privilegeCatCode(privilegeCategoryCode).roleCode(roleCode));
    }

    @Override
    @Synchronized(ASSIGN_PRIVILEGE_LOCK)
    public boolean assignPrivilegeToRole(String roleCode, String privilegeCode) throws UnifyException {
        if (!isRoleWithPrivilege(roleCode, privilegeCode)) {
            Long roleId = environment().value(Long.class, "id", new RoleQuery().code(roleCode));
            Long privilegeId = environment().value(Long.class, "id", new PrivilegeQuery().code(privilegeCode));
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setRoleId(roleId);
            rolePrivilege.setPrivilegeId(privilegeId);
            environment().create(rolePrivilege);

            invalidateRolePrivilegesCache(roleCode);
            return true;
        }

        return false;
    }

    @Override
    public boolean isRoleWithPrivilege(String roleCode, String privilegeCode) throws UnifyException {
        return (roleCode == null && getUserToken().isReservedUser())
                || tenantRolePrivileges.get(getUserTenantId()).getRolePrivileges(roleCode).isPrivilege(privilegeCode);
    }

    @Override
    public Branch getBranch(Long branchId) throws UnifyException {
        return environment().list(Branch.class, branchId);
    }

    @Override
    public Branch findBranch(BranchQuery query) throws UnifyException {
        return environment().list(query);
    }

    @Override
    public List<Branch> findBranches(BranchQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public Long getBranchId(String branchCode) throws UnifyException {
        return environment().value(Long.class, "id", new BranchQuery().code(branchCode));
    }

    @Override
    public Optional<Long> getBranchId(BranchQuery query) throws UnifyException {
        return environment().valueOptional(Long.class, "id", query);
    }

    @Override
    public List<Long> getCurrentUserBranchIds(String userLoginId, BranchViewType branchViewType) throws UnifyException {
        if (branchViewType != null) {
            UserToken userToken = getUserToken();
            if (!StringUtils.isBlank(userToken.getBranchCode())) {
                final Long userBranchId = getBranchId(userToken.getBranchCode());
                if (branchViewType.isUserBranchOnly()) {
                    return Arrays.asList(userBranchId);
                }

                return getAssociatedBranchIds(userBranchId);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<Long> getAssociatedBranchIds(Long branchId) throws UnifyException {
        Optional<Long> optionalHub = environment().valueOptional(Long.class, "hubId", new BranchQuery().id(branchId));
        return optionalHub.isPresent()
                ? environment().valueList(Long.class, "id", new BranchQuery().hubId(optionalHub.get()))
                : Arrays.asList(branchId);
    }

    @Override
    public List<BranchInfo> getAssociatedBranches(String branchCode) throws UnifyException {
        if (!StringUtils.isBlank(branchCode)) {
            Optional<Long> optionalHub = environment().valueOptional(Long.class, "hubId",
                    new BranchQuery().code(branchCode));
            List<String> branchCodeList = optionalHub.isPresent()
                    ? environment().valueList(String.class, "code", new BranchQuery().hubId(optionalHub.get()))
                    : Arrays.asList(branchCode);
            if (!DataUtils.isBlank(branchCodeList)) {
                List<BranchInfo> branchInfoList = new ArrayList<BranchInfo>();
                List<Branch> branchList = environment().findAll(new BranchQuery().codeIn(branchCodeList)
                        .addSelect("code", "description").addOrder("description"));
                for (Branch branch : branchList) {
                    branchInfoList.add(new BranchInfo(branch.getCode(), branch.getDescription()));
                }

                return branchInfoList;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Long getDepartmentId(String departmentCode) throws UnifyException {
        return environment().value(Long.class, "id", new DepartmentQuery().code(departmentCode));
    }

    @Override
    public String getDepartmentCode(Long departmentId) throws UnifyException {
        return environment().value(String.class, "code", new DepartmentQuery().id(departmentId));
    }

    @Broadcast
    public synchronized void invalidateRolePrivilegesCache(String... roleCodes) throws UnifyException {
        for (TenantRolePrivileges tenantRolePrivileges : tenantRolePrivileges.values()) {
            tenantRolePrivileges.invalidate(roleCodes);
        }
    }

    @Override
    public void performPostBootSetup(final boolean isInstallationPerformed) throws UnifyException {
        if (studioProvider != null && studioProvider.isInstallDefaultDeveloperRoles()) {
            // Ensure DevOps department
            Department department = environment().find(new DepartmentQuery().code(DEVOPS_DEPARTMENT_CODE));
            if (department == null) {
                department = new Department();
                department.setId(DefaultApplicationConstants.DEVOPS_DEPARTMENT_ID);
                department.setCode(DEVOPS_DEPARTMENT_CODE);
                department.setDescription(resolveApplicationMessage("$m{organization.default.department.devops.desc}"));
                environment().create(department);
            }

            // Ensure developer role
            Role role = environment().findLean(new RoleQuery().code(DEVOPS_DEVELOPER_CODE));
            if (role == null) {
                role = new Role();
                role.setDepartmentId(department.getId());
                role.setCode(DEVOPS_DEVELOPER_CODE);
                role.setDescription(resolveApplicationMessage("$m{organization.default.department.developer.desc}"));
                Long roleId = (Long) environment().create(role);

                List<String> privilegeCodeList = ConfigurationUtils.readStringList(
                        "data/organization-privileges-developer.dat", getUnifyComponentContext().getWorkingPath());
                RolePrivilege rolePrivilege = new RolePrivilege();
                rolePrivilege.setRoleId(roleId);
                for (Long privilegeId : environment().valueList(Long.class, "id",
                        new PrivilegeQuery().codeIn(privilegeCodeList))) {
                    rolePrivilege.setPrivilegeId(privilegeId);
                    environment().create(rolePrivilege);
                }
            } else { // TODO Remove
                if (environment().countAll(new RolePrivilegeQuery().roleId(role.getId())) == 0) {
                    List<String> privilegeCodeList = ConfigurationUtils.readStringList(
                            "data/organization-privileges-developer.dat", getUnifyComponentContext().getWorkingPath());
                    RolePrivilege rolePrivilege = new RolePrivilege();
                    rolePrivilege.setRoleId(role.getId());
                    for (Long privilegeId : environment().valueList(Long.class, "id",
                            new PrivilegeQuery().codeIn(privilegeCodeList))) {
                        rolePrivilege.setPrivilegeId(privilegeId);
                        environment().create(rolePrivilege);
                    }
                }
            }

            // Ensure junior developer role
            role = environment().findLean(new RoleQuery().code(DEVOPS_JUNIOR_DEVELOPER_CODE));
            if (role == null) {
                role = new Role();
                role.setDepartmentId(department.getId());
                role.setCode(DEVOPS_JUNIOR_DEVELOPER_CODE);
                role.setDescription(
                        resolveApplicationMessage("$m{organization.default.department.juniordeveloper.desc}"));
                Long roleId = (Long) environment().create(role);

                List<String> privilegeCodeList = ConfigurationUtils.readStringList(
                        "data/organization-privileges-juniordeveloper.dat",
                        getUnifyComponentContext().getWorkingPath());
                RolePrivilege rolePrivilege = new RolePrivilege();
                rolePrivilege.setRoleId(roleId);
                for (Long privilegeId : environment().valueList(Long.class, "id",
                        new PrivilegeQuery().codeIn(privilegeCodeList))) {
                    rolePrivilege.setPrivilegeId(privilegeId);
                    environment().create(rolePrivilege);
                }
            }
        }
    }

    @Override
    protected void doInstallModuleFeatures(final InstallationContext ctx, ModuleInstall moduleInstall) throws UnifyException {
        installPrivilegeCategories(moduleInstall);
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        roleBackupAgentList = getComponents(RolePrivilegeBackupAgent.class);
    }

    private void installPrivilegeCategories(ModuleInstall moduleInstall) throws UnifyException {
        if (OrganizationModuleNameConstants.ORGANIZATION_MODULE_NAME
                .equals(moduleInstall.getModuleConfig().getName())) {
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_CATEGORY_CODE,
                    "organization.privilegecategory.applications");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                    "organization.privilegecategory.applets");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                    "organization.privilegecategory.entities");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE,
                    "organization.privilegecategory.reportableentities");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_REPORTCONFIG_CATEGORY_CODE,
                    "organization.privilegecategory.configuredreport");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_FORMACTION_CATEGORY_CODE,
                    "organization.privilegecategory.formaction");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_FEATURE_CATEGORY_CODE,
                    "organization.privilegecategory.feature");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_WORKFLOW_WIZARD_CATEGORY_CODE,
                    "organization.privilegecategory.workflowwizards");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_DASHBOARD_CATEGORY_CODE,
                    "organization.privilegecategory.dashboards");
            installPrivilegeCategory(ApplicationPrivilegeConstants.APPLICATION_WORKSPACE_CATEGORY_CODE,
                    "organization.privilegecategory.workspaces");
        }
    }

    private void installPrivilegeCategory(String privilegeCategoryCode, String descriptionKey) throws UnifyException {
        PrivilegeCategory oldPrivilegeCategory = environment()
                .find(new PrivilegeCategoryQuery().code(privilegeCategoryCode));
        String description = getApplicationMessage(descriptionKey);
        if (oldPrivilegeCategory == null) {
            PrivilegeCategory privilegeCategory = new PrivilegeCategory();
            privilegeCategory.setCode(privilegeCategoryCode);
            privilegeCategory.setDescription(description);
            environment().create(privilegeCategory);
        } else {
            oldPrivilegeCategory.setDescription(description);
            environment().updateByIdVersion(oldPrivilegeCategory);
        }
    }

    private class TenantRolePrivileges {

        private final FactoryMap<String, RolePrivileges> privilegesByRole;

        public TenantRolePrivileges() {
            this.privilegesByRole = new StaleableFactoryMap<String, RolePrivileges>()
                {
                    @Override
                    protected boolean stale(String roleCode, RolePrivileges rolePrivileges) throws Exception {
                        return rolePrivileges.getVersion() < environment().value(Long.class, "versionNo",
                                new RoleQuery().code(roleCode));
                    }

                    @Override
                    protected RolePrivileges create(String roleCode, Object... arg2) throws Exception {
                        Set<String> privileges = environment().valueSet(String.class, "privilegeCode",
                                new RolePrivilegeQuery().roleWfItemVersionType(WfItemVersionType.ORIGINAL)
                                        .roleCode(roleCode));
                        long version = environment().value(Long.class, "versionNo",
                                new RoleQuery().code(roleCode));
                        return new RolePrivileges(privileges, version);
                    }
                };
        }

        public RolePrivileges getRolePrivileges(String roleCode) throws UnifyException {
            return privilegesByRole.get(roleCode);
        }

        public void invalidate(String... roleCodes) throws UnifyException {
            for (String roleCode : roleCodes) {
                if (privilegesByRole.isKey(roleCode)) {
                    privilegesByRole.get(roleCode).invalidate();
                }
            }
        }

    }

    private class RolePrivileges {

        private Set<String> privileges;

        private long version;

        public RolePrivileges(Set<String> privileges, long version) {
            this.privileges = privileges;
            this.version = version;
        }

        public boolean isPrivilege(String privilege) {
            return privileges.contains(privilege);
        }

        public long getVersion() {
            return version;
        }

        public void invalidate() {
            version = 0;
        }
    }
}
