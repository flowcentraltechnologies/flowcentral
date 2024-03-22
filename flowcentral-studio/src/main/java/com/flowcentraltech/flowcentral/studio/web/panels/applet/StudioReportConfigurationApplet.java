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

package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.ReportEditorPage;
import com.tcdng.unify.core.UnifyException;

/**
 * Studio application report configuration applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StudioReportConfigurationApplet extends StudioAppComponentApplet {

    private ReportEditorPage reportEditorPage;

    public StudioReportConfigurationApplet(StudioModuleService sms, AppletUtilities au, List<String> pathVariables,
            String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
    }

    public ReportEditorPage getReportEditorPage() {
        return reportEditorPage;
    }

    public void designChildItem(int childTabIndex) throws UnifyException {
        ReportConfiguration reportConfiguration = (ReportConfiguration) form.getFormBean();
        Object id = reportConfiguration.getId();
        String subTitle = reportConfiguration.getDescription();
        setTabDefAndSaveCurrentForm(childTabIndex);
        reportEditorPage = constructNewReportEditorPage(reportConfiguration.getReportable(), id, subTitle);
        reportEditorPage.newEditor();
        viewMode = ViewMode.CUSTOM_PAGE;
    }

    private ReportEditorPage constructNewReportEditorPage(String entityName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        EntityDef entityDef = getEntityDef(entityName);
        breadCrumbs.setLastCrumbTitle(au.resolveSessionMessage("$m{reporteditor.reportdesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new ReportEditorPage(au, entityDef, id, breadCrumbs);
    }

}
