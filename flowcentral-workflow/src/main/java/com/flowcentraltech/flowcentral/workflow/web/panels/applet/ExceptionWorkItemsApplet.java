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
package com.flowcentraltech.flowcentral.workflow.web.panels.applet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.TableLoadingDef;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.panels.LoadingSearch;
import com.flowcentraltech.flowcentral.application.web.panels.applet.ManageLoadingListApplet;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.UnifyException;

/**
 * Exception work items applet.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ExceptionWorkItemsApplet extends ManageLoadingListApplet {

    public ExceptionWorkItemsApplet(WorkflowModuleService workflowModuleService, String loadingTableName,
            String roleCode, AppletUtilities au, String pathVariable, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(au, pathVariable, appletWidgetReferences, formEventHandlers);
        List<TableLoadingDef> altTableLoadingDefs = new ArrayList<TableLoadingDef>();
        List<WorkflowStepInfo> workflowStepList = loadingTableName != null
                ? workflowModuleService.findWorkflowLoadingExceptionStepInfoByRole(loadingTableName, roleCode)
                : Collections.emptyList();
        int orderIndex = 0;
        for (WorkflowStepInfo workflowStepInfo : workflowStepList) {
            altTableLoadingDefs.add(new TableLoadingDef(workflowStepInfo.getStepName(), workflowStepInfo.getStepDesc(),
                    workflowStepInfo.getStepLabel(),
                    WorkflowModuleNameConstants.WORKFLOW_MY_WORKITEMS_LOADING_TABLE_PROVIDER, workflowStepInfo,
                    orderIndex++));
        }

        final LoadingSearch loadingSearch = getLoadingSearch();
        loadingSearch.getLoadingTable().setAltTableLoadingDefs(altTableLoadingDefs);
        loadingSearch.getLoadingTable().setDisableLinks(true);
        loadingSearch.applySearchEntriesToSearch();
    }

}