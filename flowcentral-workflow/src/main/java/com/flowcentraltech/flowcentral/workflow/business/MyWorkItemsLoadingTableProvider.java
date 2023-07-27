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
package com.flowcentraltech.flowcentral.workflow.business;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationLoadingTableProvider;
import com.flowcentraltech.flowcentral.application.policies.LoadingParams;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStore;
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

    public final void setWorkflowModuleService(WorkflowModuleService workflowModuleService) {
        this.workflowModuleService = workflowModuleService;
    }

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
    public List<? extends Entity> getLoadingItems(LoadingParams params) throws UnifyException {
        WorkflowStepInfo workflowStepInfo = getParameter(WorkflowStepInfo.class);
        List<Long> workItemIdList = environment().valueList(Long.class, "workRecId", new WfItemQuery()
                .workflowName(workflowStepInfo.getWorkflowLongName()).wfStepName(workflowStepInfo.getStepName()));
        if (!DataUtils.isBlank(workItemIdList)) {
            Query<? extends Entity> query = application().queryOf(getSourceEntity());
            if (params.isWithRestriction()) {
                query.addRestriction(params.getRestriction());
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
    public String getSourceItemFormApplet() throws UnifyException {
        WorkflowStepInfo workflowStepInfo = getParameter(WorkflowStepInfo.class);
        WfDef wfDef = workflowModuleService.getWfDef(workflowStepInfo.getWorkflowLongName());
        WfStepDef wfStepDef = wfDef.getWfStepDef(workflowStepInfo.getStepName());
        return wfStepDef.getStepAppletName();
    }

    @Override
    public void commitChange(ValueStore itemValueStore, boolean listing) throws UnifyException {
        // TODO Auto-generated method stub

    }

}
