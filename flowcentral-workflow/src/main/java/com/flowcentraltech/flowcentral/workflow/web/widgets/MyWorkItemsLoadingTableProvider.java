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
package com.flowcentraltech.flowcentral.workflow.web.widgets;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationLoadingTableProvider;
import com.flowcentraltech.flowcentral.application.policies.LoadingParams;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.workflow.business.WorkItemLoadingFilterGenerator;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowEntityUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;

/**
 * My work items loading table provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(WorkflowModuleNameConstants.WORKFLOW_MY_WORKITEMS_LOADING_TABLE_PROVIDER)
public class MyWorkItemsLoadingTableProvider extends AbstractApplicationLoadingTableProvider {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    public void setWorkingParameter(Object parameter) throws UnifyException {
        super.setWorkingParameter(parameter);
        setSourceEntity(getParameter(WorkflowStepInfo.class).getEntity());
    }

    @Override
    public String getLoadingLabel() throws UnifyException {
        return getParameter(WorkflowStepInfo.class).getStepLabel();
    }

    @Override
    public LoadingWorkItemInfo getLoadingWorkItemInfo(WorkEntity inst) throws UnifyException {
        WorkflowStepInfo workflowStepInfo = getParameter(WorkflowStepInfo.class);
        WfDef wfDef = workflowModuleService.getWfDef(workflowStepInfo.getWorkflowLongName());
        WfStepDef wfStepDef = wfDef.getWfStepDef(workflowStepInfo.getStepName());
        ValueStoreReader reader = new BeanValueStore(inst).getReader();
        final boolean comments = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities, reader, wfDef,
                wfStepDef.getComments());
        final boolean emails = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities, reader, wfDef,
                wfStepDef.getEmails());
        final boolean readOnly = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtilities, reader, wfDef,
                wfStepDef.getReadOnlyConditionName());
        return new LoadingWorkItemInfo(wfStepDef.getFormActionDefList(), wfStepDef.getLabel(), readOnly, comments,
                emails, wfStepDef.isError(), wfStepDef.isWithAttachmentProviderName());
    }

    @Override
    public List<? extends Entity> getLoadingItems(LoadingParams params) throws UnifyException {
        WorkflowStepInfo workflowStepInfo = getParameter(WorkflowStepInfo.class);
        logDebug("Loading items with workflow step info [{0}]...", workflowStepInfo);
        WfItemQuery _query = new WfItemQuery().workflowName(workflowStepInfo.getWorkflowLongName())
                .wfStepName(workflowStepInfo.getStepName());
        if (workflowStepInfo.isWithBranchCode()) {
            _query.branchCode(workflowStepInfo.getBranchCode());
        }

        if (workflowStepInfo.isWithDepartmentCode()) {
            _query.departmentCode(workflowStepInfo.getDepartmentCode());
        }

        List<Long> workItemIdList = environment().valueList(Long.class, "workRecId", _query);
        logDebug("Including work items with ID in [{0}]...", workItemIdList);
        if (!DataUtils.isBlank(workItemIdList)) {
            Query<? extends Entity> query = application().queryOf(getSourceEntity());
            if (params.isWithRestriction()) {
                query.addRestriction(params.getRestriction());
            }

            if (workflowStepInfo.isWithWorkItemFilterGen()) {
                WorkItemLoadingFilterGenerator loadingFilterGenerator = getComponent(
                        WorkItemLoadingFilterGenerator.class, workflowStepInfo.getWorkItemFilterGenName());
                Restriction restriction = loadingFilterGenerator.generate(workflowStepInfo.getWorkflowLongName(),
                        workflowStepInfo.getStepName());
                if (restriction != null) {
                    query.addRestriction(restriction);
                }
            }

            query.addAmongst("id", workItemIdList);
            return environment().listAll(query);
        }

        return Collections.emptyList();
    }

    @Override
    public int getSourceItemOptions(Entity loadingEntity) throws UnifyException {
        return ~0;
    }

    @Override
    public EntityItem getSourceItem(Long sourceItemId, int options) throws UnifyException {
        final WorkflowStepInfo workflowStepInfo = getParameter(WorkflowStepInfo.class);
        final Long workItemId = environment().value(Long.class, "id", new WfItemQuery().workRecId(sourceItemId)
                .workflowName(workflowStepInfo.getWorkflowLongName()).wfStepName(workflowStepInfo.getStepName()));
        return workflowModuleService.getWfItemWorkEntityFromWorkItemId(workItemId, WfReviewMode.NORMAL);
    }

    @Override
    public String getSourceItemFormApplet() throws UnifyException {
        WorkflowStepInfo workflowStepInfo = getParameter(WorkflowStepInfo.class);
        WfDef wfDef = workflowModuleService.getWfDef(workflowStepInfo.getWorkflowLongName());
        WfStepDef wfStepDef = wfDef.getWfStepDef(workflowStepInfo.getStepName());
        return wfStepDef.getStepAppletName();
    }

    @Override
    public boolean applyUserAction(WorkEntity wfEntityInst, Long sourceItemId, String userAction, String comment,
            InputArrayEntries emails, boolean listing) throws UnifyException {
        final WorkflowStepInfo workflowStepInfo = getParameter(WorkflowStepInfo.class);
        final Long workItemId = environment().value(Long.class, "id", new WfItemQuery().workRecId(sourceItemId)
                .workflowName(workflowStepInfo.getWorkflowLongName()).wfStepName(workflowStepInfo.getStepName()));
        return workflowModuleService.applyUserAction(wfEntityInst, workItemId, workflowStepInfo.getStepName(),
                userAction, comment, emails, WfReviewMode.NORMAL, listing);
    }

    @Override
    public boolean isNewCommentRequired(String userAction) throws UnifyException {
        final WorkflowStepInfo workflowStepInfo = getParameter(WorkflowStepInfo.class);
        WfDef wfDef = workflowModuleService.getWfDef(workflowStepInfo.getWorkflowLongName());
        WfStepDef wfStepDef = wfDef.getWfStepDef(workflowStepInfo.getStepName());
        return RequirementType.MANDATORY.equals(wfStepDef.getUserActionDef(userAction).getCommentRequirement());
    }

    @Override
    public void commitChange(ValueStore itemValueStore, boolean listing) throws UnifyException {
        // TODO Auto-generated method stub

    }

}
