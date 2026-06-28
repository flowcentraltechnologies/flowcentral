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
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.ChartView;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application chart applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioAppChartApplet extends AbstractStudioAppComponentApplet {

    private ChartView chartView;

    private final ChartModuleService cms;

    public StudioAppChartApplet(Page page, StudioModuleService sms, ChartModuleService cms, AppletUtilities au,
            List<String> pathVariables, String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
        this.cms = cms;
        createDesign();
    }

    public ChartView getChartView() {
        return chartView;
    }

    public void createDesign() throws UnifyException {
        final Chart chart = (Chart) form.getFormBean();
        final Long chartId = chart.getId();
        if (chartId != null) {
            String subTitle = chart.getDescription();
            chartView = constructNewChartView(
                    ApplicationNameUtils.getApplicationEntityLongName(getApplicationName(), chart.getName()),
                    chartId, subTitle);
            chartView.reloadContent();
        }
    }

    private ChartView constructNewChartView(String chartName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{charteditor.chartdesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new ChartView(au(), cms, chartName, id, breadCrumbs);
    }

}
