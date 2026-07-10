/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workflow.organization.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationWorkItemRoleUtilities;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.WorkflowLoadingTableInfo;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.application.entities.AppTableQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.RolePrivilegeBackupAgent;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.organization.entities.RoleQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfItem;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowQuery;
import com.flowcentraltech.flowcentral.workflow.organization.constants.WorkflowOrganizationModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.organization.entities.WfStepRole;
import com.flowcentraltech.flowcentral.workflow.organization.entities.WfStepRoleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Implementation of workflow organization module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component(WorkflowOrganizationModuleNameConstants.WORKFLOW_ORGANIZATION_MODULE_SERVICE)
public class WorkflowOrganizationModuleServiceImpl extends AbstractFlowCentralService
        implements WorkflowOrganizationModuleService, RolePrivilegeBackupAgent, ApplicationWorkItemRoleUtilities {

    private static final List<WorkflowStepType> USER_INTERACTIVE_STEP_TYPES = Arrays
            .asList(WorkflowStepType.USER_ACTION, WorkflowStepType.ERROR, WorkflowStepType.DELAY);

    @Configurable
    private AppletUtilities appletUtil;

    private final Map<String, Set<WfStepInfo>> roleWfStepBackup;

    public WorkflowOrganizationModuleServiceImpl() {
        this.roleWfStepBackup = new HashMap<String, Set<WfStepInfo>>();
    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {

    }

    @Override
    public void unregisterApplicationRolePrivileges(Long applicationId) throws UnifyException {
        environment().deleteAll(new WfStepRoleQuery().workflowRunnable(true).applicationId(applicationId));
    }

    @Override
    public void unregisterCustomApplicationRolePrivileges(Long applicationId) throws UnifyException {
        environment().deleteAll(new WfStepRoleQuery().workflowRunnable(true).applicationId(applicationId).isCustom());
    }

    @Override
    public void backupApplicationRolePrivileges(Long applicationId) throws UnifyException {
        List<WfStepRole> wfStepRoleList = environment().listAll(new WfStepRoleQuery().workflowRunnable(true)
                .applicationId(applicationId).addSelect("roleCode", "wfStepName", "workflowName", "applicationName"));
        for (WfStepRole wfStepRole : wfStepRoleList) {
            Set<WfStepInfo> wfStepInfos = roleWfStepBackup.get(wfStepRole.getRoleCode());
            if (wfStepInfos == null) {
                wfStepInfos = new HashSet<WfStepInfo>();
                roleWfStepBackup.put(wfStepRole.getRoleCode(), wfStepInfos);
            }

            wfStepInfos.add(new WfStepInfo(wfStepRole.getApplicationName(), wfStepRole.getWorkflowName(),
                    wfStepRole.getWfStepName()));
        }
    }

    @Override
    public void restoreApplicationRolePrivileges() throws UnifyException {
        WfStepRole wfStepRole = new WfStepRole();
        for (Map.Entry<String, Set<WfStepInfo>> entry : roleWfStepBackup.entrySet()) {
            final Long roleId = environment().value(Long.class, "id", new RoleQuery().code(entry.getKey()));
            for (WfStepInfo wfStepInfo : entry.getValue()) {
                Optional<Long> stepId = environment().valueOptional(Long.class, "id",
                        new WfStepQuery().applicationName(wfStepInfo.getApplicationName())
                                .workflowName(wfStepInfo.getWorkflowName()).name(wfStepInfo.getStepName()));
                if (stepId.isPresent() && environment().countAll(
                        new WfStepRoleQuery().workflowRunnable(true).roleId(roleId).wfStepId(stepId.get())) == 0) {
                    wfStepRole.setId(null);
                    wfStepRole.setRoleId(roleId);
                    wfStepRole.setWfStepId(stepId.get());
                    environment().create(wfStepRole);
                }
            }
        }
    }

    @Override
    public int countWorkflowLoadingTableInfoByRole(String roleCode) throws UnifyException {
        return StringUtils.isBlank(roleCode)
                ? environment().countAll(new WorkflowQuery().runnable().isWithLoadingTable())
                : environment().countAll(new WfStepRoleQuery().workflowRunnable(true)
                        .wfStepTypeIn(USER_INTERACTIVE_STEP_TYPES).roleCode(roleCode).isWithLoadingTable());
    }

    @Override
    public List<WorkflowLoadingTableInfo> findWorkflowLoadingTableInfoByRole(String roleCode) throws UnifyException {
        Set<String> loadingTableNameList = StringUtils.isBlank(roleCode)
                ? environment().valueSet(String.class, "loadingTable",
                        new WorkflowQuery().runnable().isWithLoadingTable())
                : environment().valueSet(String.class, "workflowLoadingTable",
                        new WfStepRoleQuery().workflowRunnable(true).wfStepTypeIn(USER_INTERACTIVE_STEP_TYPES)
                                .roleCode(roleCode).isWithLoadingTable());
        if (!DataUtils.isBlank(loadingTableNameList)) {
            List<WorkflowLoadingTableInfo> workflowLoadingTableInfoList = new ArrayList<WorkflowLoadingTableInfo>();
            for (String loadingTableName : loadingTableNameList) {
                final ApplicationEntityNameParts tnp = ApplicationNameUtils
                        .getApplicationEntityNameParts(loadingTableName);
                final String label = resolveSessionMessage(environment().value(String.class, "label",
                        new AppTableQuery().applicationName(tnp.getApplicationName()).name(tnp.getEntityName())));
                workflowLoadingTableInfoList.add(new WorkflowLoadingTableInfo(loadingTableName, label));
            }

            DataUtils.sortAscending(workflowLoadingTableInfoList, WorkflowLoadingTableInfo.class, "description");
            return workflowLoadingTableInfoList;
        }

        return Collections.emptyList();
    }

    @Override
    public WorkflowStepInfo getWorkflowLoadingStepInfoByWorkItemEventId(Long workItemEventId, String branchCode,
            String departmentCode) throws UnifyException {
        WfItem wfItem = environment().list(new WfItemQuery().wfItemEventId(workItemEventId));
        if (wfItem != null) {
            ApplicationEntityNameParts parts = ApplicationNameUtils
                    .getApplicationEntityNameParts(wfItem.getWorkflowName());
            WfStep wfStep = environment().list(new WfStepQuery().applicationName(parts.getApplicationName())
                    .workflowName(parts.getEntityName()).name(wfItem.getWfStepName()));
            return new WorkflowStepInfo(wfItem.getWorkflowName(), wfItem.getApplicationName(), parts.getEntityName(),
                    null, wfItem.getEntity(), wfItem.getWfStepName(), wfStep.getDescription(), wfStep.getLabel(),
                    wfStep.isBranchOnly() ? branchCode : null, wfStep.isDepartmentOnly() ? departmentCode : null,
                    wfItem.getId());
        }

        return WorkflowStepInfo.EMPTY;
    }

    @Override
    public List<WorkflowStepInfo> findWorkflowLoadingStepInfoByRole(String loadingTableName, String roleCode,
            String branchCode, String departmentCode) throws UnifyException {
        return findWorkflowLoadingStepInfoByRole(WorkflowStepType.USER_ACTION, loadingTableName, roleCode, branchCode,
                departmentCode);
    }

    @Override
    public List<WorkflowStepInfo> findWorkflowLoadingExceptionStepInfoByRole(String loadingTableName, String roleCode,
            String branchCode, String departmentCode) throws UnifyException {
        return findWorkflowLoadingStepInfoByRole(WorkflowStepType.ERROR, loadingTableName, roleCode, branchCode,
                departmentCode);
    }

    @Override
    protected void doInstallModuleFeatures(final InstallationContext ctx, ModuleInstall moduleInstall)
            throws UnifyException {

    }

    private List<WorkflowStepInfo> findWorkflowLoadingStepInfoByRole(WorkflowStepType type, String loadingTableName,
            String roleCode, String branchCode, String departmentCode) throws UnifyException {
        if (StringUtils.isBlank(roleCode)) {
            List<WfStep> wfStepList = environment().listAll(new WfStepQuery().workflowRunnable(true)
                    .workflowLoadingTable(loadingTableName).type(type).addSelect("name", "description", "label",
                            "entityName", "applicationName", "workflowName", "branchOnly", "departmentOnly"));
            if (!DataUtils.isBlank(wfStepList)) {
                List<WorkflowStepInfo> workflowStepInfoList = new ArrayList<WorkflowStepInfo>();
                for (WfStep wfStep : wfStepList) {
                    workflowStepInfoList.add(new WorkflowStepInfo(
                            ApplicationNameUtils.getApplicationEntityLongName(wfStep.getApplicationName(),
                                    wfStep.getWorkflowName()),
                            wfStep.getApplicationName(), wfStep.getWorkflowName(), null, wfStep.getEntityName(),
                            wfStep.getName(), wfStep.getDescription(), wfStep.getLabel(),
                            wfStep.isBranchOnly() ? branchCode : null,
                            wfStep.isDepartmentOnly() ? departmentCode : null));
                }

                DataUtils.sortAscending(workflowStepInfoList, WorkflowStepInfo.class, "stepLabel");
                return workflowStepInfoList;
            }
        } else {
            List<WfStepRole> wfStepRoleList = environment().listAll(new WfStepRoleQuery().workflowRunnable(true)
                    .roleCode(roleCode).workflowLoadingTable(loadingTableName).wfStepType(type).isWithLoadingTable()
                    .isOriginal().addSelect("wfStepName", "wfStepDesc", "wfStepLabel", "entityName", "applicationName",
                            "workflowName", "branchOnly", "departmentOnly"));
            if (!DataUtils.isBlank(wfStepRoleList)) {
                List<WorkflowStepInfo> workflowStepInfoList = new ArrayList<WorkflowStepInfo>();
                TableDef tableDef = appletUtil.getTableDef(loadingTableName);
                for (WfStepRole wfStepRole : wfStepRoleList) {
                    workflowStepInfoList.add(new WorkflowStepInfo(
                            ApplicationNameUtils.getApplicationEntityLongName(wfStepRole.getApplicationName(),
                                    wfStepRole.getWorkflowName()),
                            wfStepRole.getApplicationName(), wfStepRole.getWorkflowName(),
                            tableDef.getLoadingFilterGen(), wfStepRole.getEntityName(), wfStepRole.getWfStepName(),
                            wfStepRole.getWfStepDesc(), wfStepRole.getWfStepLabel(),
                            wfStepRole.isBranchOnly() ? branchCode : null,
                            wfStepRole.isDepartmentOnly() ? departmentCode : null));
                }

                DataUtils.sortAscending(workflowStepInfoList, WorkflowStepInfo.class, "stepLabel");
                return workflowStepInfoList;
            }
        }

        return Collections.emptyList();
    }

    private class WfStepInfo {

        private final String applicationName;

        private final String workflowName;

        private final String stepName;

        public WfStepInfo(String applicationName, String workflowName, String stepName) {
            this.applicationName = applicationName;
            this.workflowName = workflowName;
            this.stepName = stepName;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public String getWorkflowName() {
            return workflowName;
        }

        public String getStepName() {
            return stepName;
        }

    }

}
