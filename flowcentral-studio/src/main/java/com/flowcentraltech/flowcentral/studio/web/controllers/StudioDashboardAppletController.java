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
package com.flowcentraltech.flowcentral.studio.web.controllers;

import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AbstractEntityFormAppletController;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.flowcentraltech.flowcentral.studio.web.panels.applet.StudioDashboardApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Studio application dashboard applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/studiodashboardapplet")
@UplBinding("web/studio/upl/studiodashboardappletpage.upl")
public class StudioDashboardAppletController
        extends AbstractEntityFormAppletController<StudioDashboardApplet, StudioDashboardAppletPageBean> {

    @Configurable
    private StudioModuleService studioModuleService;

    @Configurable
    private DashboardModuleService dashboardModuleService;

    @Configurable
    private ChartModuleService chartModuleService;

    public StudioDashboardAppletController() {
        super(StudioDashboardAppletPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String designChildItem() throws UnifyException {
        StudioDashboardAppletPageBean pageBean = getPageBean();
        StudioDashboardApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        applet.designChildItem(childTabIndex);
        return "refreshapplet";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        StudioDashboardAppletPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            AppletWidgetReferences appletWidgetReferences = getAppletWidgetReferences();
            EntityFormEventHandlers formEventHandlers = getEntityFormEventHandlers();
            StudioDashboardApplet applet = new StudioDashboardApplet(studioModuleService, chartModuleService,
                    dashboardModuleService, au(), getPathVariables(),
                    (String) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME),
                    appletWidgetReferences, formEventHandlers);
            pageBean.setApplet(applet);
        }
    }

}
