/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workspace.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.WorkspacePrivilegeManager;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.workspace.constants.WorkspaceModuleNameConstants;
import com.flowcentraltech.flowcentral.workspace.entities.Workspace;
import com.flowcentraltech.flowcentral.workspace.entities.WorkspacePrivilegeQuery;
import com.flowcentraltech.flowcentral.workspace.entities.WorkspaceQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.StaleableFactoryMap;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Implementation of workspace module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(WorkspaceModuleNameConstants.WORKSPACE_MODULE_SERVICE)
public class WorkspaceModuleServiceImpl extends AbstractFlowCentralService
        implements WorkspaceModuleService, WorkspacePrivilegeManager {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private ApplicationPrivilegeManager appPrivilegeManager;

    private FactoryMap<String, WorkspacePrivileges> privilegesByWorkspace;

    public WorkspaceModuleServiceImpl() {
        privilegesByWorkspace = new StaleableFactoryMap<String, WorkspacePrivileges>()
            {
                @Override
                protected boolean stale(String workspaceCode, WorkspacePrivileges workspacePrivileges)
                        throws Exception {
                    return workspacePrivileges.getVersion() < environment().value(Long.class, "versionNo",
                            new WorkspaceQuery().code(workspaceCode));
                }

                @Override
                protected WorkspacePrivileges create(String workspaceCode, Object... arg2) throws Exception {
                    Set<String> privileges = environment().valueSet(String.class, "privilegeCode",
                            new WorkspacePrivilegeQuery().workspaceCode(workspaceCode));
                    long version = environment().value(Long.class, "versionNo",
                            new WorkspaceQuery().code(workspaceCode));
                    return new WorkspacePrivileges(privileges, version);
                }
            };
    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {
        logDebug("Clearing definitions cache...");
        privilegesByWorkspace.clear();
        logDebug("Definitions cache clearing successfully completed.");
    }

    @Override
    public List<String> findRoleWorkspaceCodes(String roleCode) throws UnifyException {
        List<String> privilegeCodes = appPrivilegeManager
                .findRolePrivileges(ApplicationPrivilegeConstants.APPLICATION_WORKSPACE_CATEGORY_CODE, roleCode);
        if (!DataUtils.isBlank(privilegeCodes)) {
            List<String> workspaceCodes = new ArrayList<String>();
            for (String privilegeCode : privilegeCodes) {
                workspaceCodes.add(PrivilegeNameUtils.getPrivilegeNameParts(privilegeCode).getEntityName());
            }

            return workspaceCodes;
        }

        return Collections.emptyList();
    }

    @Override
    public List<Workspace> findWorkspaces(String roleCode) throws UnifyException {
        return environment().findAll(new WorkspaceQuery().codeIn(findRoleWorkspaceCodes(roleCode)));
    }

    @Override
    public List<Workspace> findWorkspaces(WorkspaceQuery query) throws UnifyException {
        return environment().findAll(query);
    }

    @Override
    public int countRoleWorkspaces(String roleCode) throws UnifyException {
        if (roleCode == null || getUserToken().isReservedUser()) {
            return environment().countAll(new WorkspaceQuery().ignoreEmptyCriteria(true));
        }

        return appPrivilegeManager
                .findRolePrivileges(ApplicationPrivilegeConstants.APPLICATION_WORKSPACE_CATEGORY_CODE, roleCode).size();
    }

    @Override
    public boolean isWorkspaceWithPrivilege(String workspaceCode, String privilegeCode) throws UnifyException {
        if (workspaceCode == null) {
            return false;
        }

        if (DefaultApplicationConstants.ROOT_WORKSPACE_CODE.equals(workspaceCode)) {
            return true;
        }

        return privilegesByWorkspace.get(workspaceCode).isPrivilege(privilegeCode);
    }

    @Override
    protected void doInstallModuleFeatures(final ModuleInstall moduleInstall) throws UnifyException {
        installDefaultWorkspaces(moduleInstall);
    }

    private void installDefaultWorkspaces(final ModuleInstall moduleInstall) throws UnifyException {
        if (WorkspaceModuleNameConstants.WORKSPACE_MODULE_NAME.equals(moduleInstall.getModuleConfig().getName())) {
            logInfo("Installing default workspaces ...");
            if (environment()
                    .countAll(new WorkspaceQuery().id(DefaultApplicationConstants.ROOT_WORKSPACE_ENTITY_ID)) == 0) {
                Workspace workspace = new Workspace(DefaultApplicationConstants.ROOT_WORKSPACE_ENTITY_ID,
                        DefaultApplicationConstants.ROOT_WORKSPACE_CODE,
                        DefaultApplicationConstants.ROOT_WORKSPACE_NAME,
                        resolveSessionMessage(DefaultApplicationConstants.ROOT_WORKSPACE_DESC));
                environment().create(workspace);
                final Long applicationId = applicationModuleService.getApplicationDef("workspace").getId();
                appPrivilegeManager.registerPrivilege(ConfigType.STATIC, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_WORKSPACE_CATEGORY_CODE,
                        PrivilegeNameUtils.getWorkspacePrivilegeName(DefaultApplicationConstants.ROOT_WORKSPACE_CODE),
                        resolveSessionMessage(DefaultApplicationConstants.ROOT_WORKSPACE_DESC));
            }
        }
    }

    private class WorkspacePrivileges {

        private Set<String> privileges;

        private long version;

        public WorkspacePrivileges(Set<String> privileges, long version) {
            this.privileges = privileges;
            this.version = version;
        }

        public boolean isPrivilege(String privilege) {
            return privileges.contains(privilege);
        }

        public long getVersion() {
            return version;
        }
    }

}
