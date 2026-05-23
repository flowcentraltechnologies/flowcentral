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
package com.flowcentraltech.flowcentral.dashboard.web.providers;

import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.flowcentraltech.flowcentral.dashboard.constants.DashboardIFrameHtmlProviderConstants;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.web.controllers.DashboardSlateController;
import com.flowcentraltech.flowcentral.dashboard.web.controllers.DashboardSlatePageBean;
import com.flowcentraltech.flowcentral.dashboard.web.widgets.DashboardSlate;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.ControllerFinder;
import com.tcdng.unify.web.PathInfoRepository;
import com.tcdng.unify.web.ui.widget.AbstractIFrameHtmlProvider;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;

@Component(DashboardIFrameHtmlProviderConstants.PROVIDER_NAME)
public class DashboardIFrameHtmlProvider extends AbstractIFrameHtmlProvider {

    @Configurable
    private ControllerFinder controllerFinder;
    
    @Configurable
    private PathInfoRepository pathInfoRepository;

    @Configurable
    private DashboardModuleService dashboardModuleService;
    
    @Override
    protected void writeHtml(ResponseWriter writer) throws UnifyException {
        final DashboardSlateController controller = (DashboardSlateController) controllerFinder
                .findController(pathInfoRepository.getControllerPathParts("/application/dashboardslate"));
        final DashboardSlatePageBean pageBean = (DashboardSlatePageBean) controller.getPage().getPageBean();
        final String dashboard = getRequestAttribute(String.class, DashboardIFrameHtmlProviderConstants.REQ_DASHBOARD);
        final String selOption = getRequestAttribute(String.class, DashboardIFrameHtmlProviderConstants.REQ_SELOPTION);
        
        DashboardDef dashboardDef = dashboardModuleService.getDashboardDef(dashboard);
        pageBean.setDashboardSlate(new DashboardSlate(dashboardDef, selOption));
        
        final Widget slate = controller.getPageWidgetByShortName(Widget.class, "dashboardSlate");
        setRequestAttribute(DashboardIFrameHtmlProviderConstants.REQ_SLATE_WIDGET, slate);
        writer.writeStructureAndContent(slate);
    }

    @Override
    protected void writeScript(ResponseWriter writer) throws UnifyException {
        Widget slate = getRequestAttribute(Widget.class, DashboardIFrameHtmlProviderConstants.REQ_SLATE_WIDGET);
        writer.writeBehavior(slate);
    }

}
