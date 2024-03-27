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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.panels.applet.CreateEntityApplet;
import com.flowcentraltech.flowcentral.configuration.constants.FlowCentralAppletPathConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Create entity applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(FlowCentralAppletPathConstants.CREATE_ENTITY)
@UplBinding("web/application/upl/createentityappletpage.upl")
@ResultMappings({ @ResultMapping(name = "showfileattachments",
        response = { "!validationerrorresponse", "!showpopupresponse popup:$s{fileAttachmentsPopup}" }) })
public class CreateEntityAppletController
        extends AbstractEntityFormAppletController<CreateEntityApplet, CreateEntityAppletPageBean> {

    public CreateEntityAppletController() {
        super(CreateEntityAppletPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String closeFileAttachments() throws UnifyException {
        return "refreshapplet";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        CreateEntityAppletPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            AppletWidgetReferences appletWidgetReferences = getAppletWidgetReferences();
            EntityFormEventHandlers entityFormEventHandlers = getEntityFormEventHandlers();
            CreateEntityApplet applet = new CreateEntityApplet(getPage(), au(), getPathVariables(), appletWidgetReferences,
                    entityFormEventHandlers);
            pageBean.setApplet(applet);
            if (pageBean.getAltCaption() == null) {
                setPageTitle(applet);
            }
        }
    }

}
