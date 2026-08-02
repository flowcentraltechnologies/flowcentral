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
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.ReportEditorPage;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application report configuration applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioReportConfigurationApplet extends AbstractStudioAppComponentApplet<ReportEditorPage> {

    public StudioReportConfigurationApplet(Page page, StudioModuleService sms, AppletUtilities au,
            List<String> pathVariables, String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
    }

    public void commitDesign() throws UnifyException {
        if (getDesign() != null) {
            getDesign().commitDesign();
        }
    }

    @Override
    protected void onRootHwtFormUpdated(Entity inst) throws UnifyException {
        ReportConfiguration reportConfiguration = (ReportConfiguration) inst;
        Long reportConfigurationId = reportConfiguration != null ? reportConfiguration.getId() : null;
        ReportEditorPage reportEditorPage = reportConfigurationId != null
                ? constructNewReportEditorPage(reportConfiguration.getReportable(), reportConfigurationId,
                        reportConfiguration.getDescription())
                : constructNewReportEditorPage(null, null, au().resolveSessionMessage("$m{reporteditor.newreport}"));
        reportEditorPage.newEditor();
        setDesign(reportEditorPage);
    }

    private ReportEditorPage constructNewReportEditorPage(String entityName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = getForm().getBreadCrumbs().advance();
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{reporteditor.reportdesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        EntityDef entityDef = entityName != null ? getEntityDef(entityName) : null;
        return new ReportEditorPage(studio(), au(), entityDef, id, breadCrumbs);
    }

}
