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
package com.flowcentraltech.flowcentral.workflow.web.panels.applet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPageAttributeConstants;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.TableLoadingDef;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.panels.LoadingSearch;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTable;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowNameUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.web.ui.widget.data.ButtonInfo;

/**
 * Exception work items applet.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ExceptionWorkItemsApplet extends AbstractWorkItemsApplet {

    public ExceptionWorkItemsApplet(WorkflowModuleService workflowModuleService, String loadingTableName,
            String roleCode, AppletUtilities au, String pathVariable, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(au, pathVariable, appletWidgetReferences, formEventHandlers);
        List<TableLoadingDef> altTableLoadingDefs = new ArrayList<TableLoadingDef>();
        UserToken userToken = au.getSessionUserToken();
        List<WorkflowStepInfo> workflowStepList = loadingTableName != null
                ? workflowModuleService.findWorkflowLoadingExceptionStepInfoByRole(loadingTableName, roleCode,
                        userToken.getBranchCode(), userToken.getDepartmentCode())
                : Collections.emptyList();
        int orderIndex = 0;
        boolean withTableActions = false;
        for (WorkflowStepInfo workflowStepInfo : workflowStepList) {
            List<ButtonInfo> actionBtnInfos = getActionButtons(workflowModuleService, workflowStepInfo);
            altTableLoadingDefs.add(new TableLoadingDef(
                    WorkflowNameUtils.getWfStepLongName(workflowStepInfo.getWorkflowLongName(),
                            workflowStepInfo.getStepName()),
                    workflowStepInfo.getStepDesc(), workflowStepInfo.getStepLabel(),
                    WorkflowModuleNameConstants.WORKFLOW_MY_WORKITEMS_LOADING_TABLE_PROVIDER, workflowStepInfo,
                    orderIndex++, actionBtnInfos));
            withTableActions |= (!actionBtnInfos.isEmpty());
        }

        final LoadingSearch loadingSearch = getLoadingSearch();
        final LoadingTable loadingTable = loadingSearch.getLoadingTable();
        loadingTable.setAltTableLoadingDefs(altTableLoadingDefs);
        loadingTable.setDisableLinks(true);
        if (withTableActions) {
            loadingSearch.setShowActionFooter(withTableActions);
            List<TableAction> actions = getTableActions(loadingTable.getActionBtnInfos());
            au.setSessionAttribute(
                    AppletPageAttributeConstants.TABLE_ACTIONS + ":" + loadingTable.getTableDef().getId(), actions);
        }

        loadingSearch.applySearchEntriesToSearch();
    }

}
