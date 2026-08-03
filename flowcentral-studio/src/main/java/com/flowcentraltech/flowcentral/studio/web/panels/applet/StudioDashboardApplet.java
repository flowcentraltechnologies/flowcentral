/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.DashboardEditorPage;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application dashboard applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioDashboardApplet extends AbstractStudioAppComponentApplet<DashboardEditorPage> {

    private final DashboardModuleService dms;

    public StudioDashboardApplet(Page page, StudioModuleService sms,  DashboardModuleService dms,
            AppletUtilities au, List<String> pathVariables, String applicationName,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
        this.dms = dms;
    }
    
    public void commitDesign() throws UnifyException {
        if (getDesign() != null) {
            getDesign().commitDesign();
        }
    }

    @Override
    protected void onRootHwtFormUpdated(Entity inst) throws UnifyException {
        Dashboard dashboard = (Dashboard) inst;
        Long dashboardId = dashboard != null ? dashboard.getId() : null;
        DashboardEditorPage dashboardEditorPage = dashboardId != null
                ? constructNewDashboardEditorPage(
                        ApplicationNameUtils.getApplicationEntityLongName(getApplicationName(), dashboard.getName()),
                        dashboardId, dashboard.getDescription())
                : constructNewDashboardEditorPage(null, null,
                        au().resolveSessionMessage("$m{dashboardeditor.newdashboard}"));
        dashboardEditorPage.newEditor();
        setDesign(dashboardEditorPage);
    }

    private DashboardEditorPage constructNewDashboardEditorPage(String dashboardName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = getForm().getBreadCrumbs().advance();
        DashboardDef dashboardDef = dms.getDashboardDef(dashboardName);
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{dashboardeditor.dashboarddesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        setBreadCrumbs(breadCrumbs);

        return new DashboardEditorPage(studio(), au(), au().getComponent(ChartModuleService.class), dashboardDef, id);
    }

}
