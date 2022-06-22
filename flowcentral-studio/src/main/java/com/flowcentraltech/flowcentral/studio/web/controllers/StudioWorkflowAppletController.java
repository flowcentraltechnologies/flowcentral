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
package com.flowcentraltech.flowcentral.studio.web.controllers;

import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AbstractEntityFormAppletController;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.flowcentraltech.flowcentral.studio.web.panels.applet.StudioWorkflowApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Studio application workflow applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/studioworkflowapplet")
@UplBinding("web/studio/upl/studioworkflowappletpage.upl")
public class StudioWorkflowAppletController
        extends AbstractEntityFormAppletController<StudioWorkflowApplet, StudioWorkflowAppletPageBean> {

    @Configurable
    private StudioModuleService studioModuleService;

    public StudioWorkflowAppletController() {
        super(StudioWorkflowAppletPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public void setStudioModuleService(StudioModuleService studioModuleService) {
        this.studioModuleService = studioModuleService;
    }

    @Action
    public String designChildItem() throws UnifyException {
        StudioWorkflowAppletPageBean pageBean = getPageBean();
        StudioWorkflowApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        applet.designChildItem(childTabIndex);
        return "refreshapplet";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        StudioWorkflowAppletPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            AppletWidgetReferences appletWidgetReferences = getAppletWidgetReferences();
            EntityFormEventHandlers formEventHandlers = getEntityFormEventHandlers();
            StudioWorkflowApplet applet = new StudioWorkflowApplet(studioModuleService, getAu(), getPathVariable(),
                    (String) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME),
                    appletWidgetReferences, formEventHandlers);
            pageBean.setApplet(applet);
        }
    }

}
