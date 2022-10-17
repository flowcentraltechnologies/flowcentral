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
package com.flowcentraltech.flowcentral.organization.business;

import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.PostBootSetup;
import com.flowcentraltech.flowcentral.common.business.StudioProvider;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.organization.constants.OrganizationModuleNameConstants;
import com.flowcentraltech.flowcentral.organization.entities.BranchQuery;
import com.flowcentraltech.flowcentral.organization.entities.Department;
import com.flowcentraltech.flowcentral.organization.entities.DepartmentQuery;
import com.flowcentraltech.flowcentral.organization.entities.Privilege;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeCategory;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeCategoryQuery;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeQuery;
import com.flowcentraltech.flowcentral.organization.entities.Role;
import com.flowcentraltech.flowcentral.organization.entities.RolePrivilege;
import com.flowcentraltech.flowcentral.organization.entities.RolePrivilegeQuery;
import com.flowcentraltech.flowcentral.organization.entities.RoleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;

/**
 * Implementation of organization module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(OrganizationModuleNameConstants.ORGANIZATION_MODULE_SERVICE)
public class OrganizationModuleServiceImpl extends AbstractFlowCentralService
        implements OrganizationModuleService, ApplicationPrivilegeManager, PostBootSetup {

    private static final String DEVOPS_DEPARTMENT_CODE = "DEVOPS";

    private static final String DEVOPS_DEVELOPER_CODE = "DEVELOPER";

    private static final String DEVOPS_JUNIOR_DEVELOPER_CODE = "JDEVELOPER";

    private FactoryMap<String, RolePrivileges> privilegesByRole;

    @Configurable
    private StudioProvider studioProvider;
    
    public OrganizationModuleServiceImpl() {
        privilegesByRole = new FactoryMap<String, RolePrivileges>(true)
            {
                @Override
                protected boolean stale(String roleCode, RolePrivileges rolePrivileges) throws Exception {
                    return rolePrivileges.getVersion() < environment().value(Long.class, "versionNo",
                            new RoleQuery().code(roleCode));
                }

                @Override
                protected RolePrivileges create(String roleCode, Object... arg2) throws Exception {
                    Set<String> privileges = environment().valueSet(String.class, "privilegeCode",
                            new RolePrivilegeQuery().roleCode(roleCode));
                    long version = environment().value(Long.class, "versionNo", new RoleQuery().code(roleCode));
                    return new RolePrivileges(privileges, version);
                }
            };
    }

    public final void setStudioProvider(StudioProvider studioProvider) {
        this.studioProvider = studioProvider;
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
    public void registerPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode,
            String privilegeDesc) throws UnifyException {
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
            environment().create(privilege);
        } else {
            oldPrivilege.setDescription(privilegeDesc);
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
    public boolean isRegisteredPrivilege(String privilegeCategoryCode, String privilegeCode) throws UnifyException {
        return environment().countAll(new PrivilegeQuery().privilegeCatCode(privilegeCategoryCode)
                .code(privilegeCode)) > 0;
    }

    @Override
    public List<String> findRolePrivileges(String privilegeCategoryCode, String roleCode) throws UnifyException {
        if (getUserToken().isReservedUser()) {
            return environment().valueList(String.class, "code",
                    new PrivilegeQuery().privilegeCatCode(privilegeCategoryCode));
        }

        return environment().valueList(String.class, "privilegeCode",
                new RolePrivilegeQuery().privilegeCatCode(privilegeCategoryCode).roleCode(roleCode));
    }

    @Synchronized("org:assignprivtorole")
    @Override
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
                || privilegesByRole.get(roleCode).isPrivilege(privilegeCode);
    }

    @Override
    public Long getBranchID(String branchCode) throws UnifyException {
        return environment().value(Long.class, "id", new BranchQuery().code(branchCode));
    }

    @Override
    public Long getDepartmentID(String departmentCode) throws UnifyException {
        return environment().value(Long.class, "id", new DepartmentQuery().code(departmentCode));
    }

    @Broadcast
    public synchronized void invalidateRolePrivilegesCache(String... roleCodes) throws UnifyException {
        for (String roleCode : roleCodes) {
            if (privilegesByRole.isKey(roleCode)) {
                privilegesByRole.get(roleCode).invalidate();
            }
        }
    }

    @Override
    public void performPostBootSetup() throws UnifyException {
        if (studioProvider != null && studioProvider.isInstallDefaultDeveloperRoles()) {
            // Ensure DevOps department
            Department department = environment().find(new DepartmentQuery().code(DEVOPS_DEPARTMENT_CODE));
            if (department == null) {
                department = new Department();
                department.setCode(DEVOPS_DEPARTMENT_CODE);
                department.setDescription(resolveApplicationMessage("$m{organization.default.department.devops.desc}"));
                environment().create(department);
            }

            // Ensure developer role
            Role role = environment().find(new RoleQuery().code(DEVOPS_DEVELOPER_CODE));
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
            }

            // Ensure junior developer role
            role = environment().find(new RoleQuery().code(DEVOPS_JUNIOR_DEVELOPER_CODE));
            if (role == null) {
                role = new Role();
                role.setDepartmentId(department.getId());
                role.setCode(DEVOPS_JUNIOR_DEVELOPER_CODE);
                role.setDescription(resolveApplicationMessage("$m{organization.default.department.juniordeveloper.desc}"));
                Long roleId = (Long) environment().create(role);

                List<String> privilegeCodeList = ConfigurationUtils.readStringList(
                        "data/organization-privileges-juniordeveloper.dat", getUnifyComponentContext().getWorkingPath());
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
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {
        installPrivilegeCategories(moduleInstall);
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
