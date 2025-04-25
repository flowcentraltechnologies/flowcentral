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

package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.WorkflowEditorPage;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio workflow applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioWorkflowApplet extends StudioAppComponentApplet {

    private WorkflowEditorPage workflowEditorPage;

    private final WorkflowModuleService workflowModuleService;

    public StudioWorkflowApplet(WorkflowModuleService workflowModuleService, Page page, StudioModuleService sms,
            AppletUtilities au, List<String> pathVariables, String applicationName,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
        this.workflowModuleService = workflowModuleService;
    }

    public WorkflowEditorPage getWorkflowEditorPage() {
        return workflowEditorPage;
    }

    public void designChildItem(int childTabIndex) throws UnifyException {
        Workflow workflow = (Workflow) form.getFormBean();
        Long workflowId = workflow.getId();
        String subTitle = workflow.getDescription();
        setTabDefAndSaveCurrentForm(childTabIndex);
        workflowEditorPage = constructNewWorkflowEditorPage(workflow.getEntity(), workflowId, subTitle);
        workflowEditorPage.newEditor();
        viewMode = ViewMode.CUSTOM_PAGE;
    }

    private WorkflowEditorPage constructNewWorkflowEditorPage(String entityName, Long workflowId, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        EntityDef entityDef = getEntityDef(entityName);
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{workfloweditor.workflowdesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new WorkflowEditorPage(workflowModuleService, au(), entityDef, workflowId, breadCrumbs);
    }

}
