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

package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.DashboardEditorPage;
import com.tcdng.unify.core.UnifyException;

/**
 * Studio application dashboard applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StudioDashboardApplet extends StudioAppComponentApplet {

    private DashboardEditorPage dashboardEditorPage;

    private DashboardModuleService dms;

    public StudioDashboardApplet(StudioModuleService sms, DashboardModuleService dms, AppletUtilities au,
            String pathVariable, String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(sms, au, pathVariable, applicationName, appletWidgetReferences, formEventHandlers);
    }

    public DashboardEditorPage getDashboardEditorPage() {
        return dashboardEditorPage;
    }

    public void designChildItem(int childTabIndex) throws UnifyException {
        Dashboard dashboard = (Dashboard) form.getFormBean();
        Object id = dashboard.getId();
        String subTitle = dashboard.getDescription();
        setTabDefAndSaveCurrentForm(childTabIndex);
        dashboardEditorPage = constructNewDashboardEditorPage(
                ApplicationNameUtils.getApplicationEntityLongName(getApplicationName(), dashboard.getName()), id,
                subTitle);
        dashboardEditorPage.newEditor();
        viewMode = ViewMode.CUSTOM_PAGE;
    }

    private DashboardEditorPage constructNewDashboardEditorPage(String dashboardName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        DashboardDef dashboardDef = dms.getDashboardDef(dashboardName);
        breadCrumbs.setLastCrumbTitle(au.resolveSessionMessage("$m{dashboardeditor.dashboarddesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new DashboardEditorPage(au, dashboardDef, id, breadCrumbs);
    }

}