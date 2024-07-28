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
package com.flowcentraltech.flowcentral.workflow.web.controllers;

import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AbstractEntityFormAppletController;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.configuration.constants.FlowCentralAppletPathConstants;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.web.panels.applet.ReviewWizardWorkItemsApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Review wizard work items applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(FlowCentralAppletPathConstants.REVIEW_WIZARDWORKITEMS)
@UplBinding("web/workflow/upl/reviewwizardworkitemsappletpage.upl")
@ResultMappings({ @ResultMapping(name = ApplicationResultMappingConstants.SHOW_FILE_ATTACHMENTS,
        response = { "!validationerrorresponse", "!showpopupresponse popup:$s{fileAttachmentsPopup}" }) })
public class ReviewWizardWorkItemsAppletController
        extends AbstractEntityFormAppletController<ReviewWizardWorkItemsApplet, ReviewWizardWorkItemsAppletPageBean> {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    public ReviewWizardWorkItemsAppletController() {
        super(ReviewWizardWorkItemsAppletPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String closeFileAttachments() throws UnifyException {
        return "refreshapplet";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        ReviewWizardWorkItemsAppletPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            AppletWidgetReferences appletWidgetReferences = getAppletWidgetReferences();
            EntityFormEventHandlers formEventHandlers = getEntityFormEventHandlers();
            ReviewWizardWorkItemsApplet applet = new ReviewWizardWorkItemsApplet(getPage(), au(), workflowModuleService,
                    getPathVariables(), getUserToken().getUserLoginId(), appletWidgetReferences, formEventHandlers);
            pageBean.setApplet(applet);
        }
    }

}
