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
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.entities.ChartDataSource;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.ChartDatasourceView;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio chart data source applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioChartDatasourceApplet extends AbstractStudioAppComponentApplet {

    private ChartDatasourceView chartDatasourceView;

    private final ChartModuleService cms;

    public StudioChartDatasourceApplet(Page page, StudioModuleService sms, ChartModuleService cms, AppletUtilities au,
            List<String> pathVariables, String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
        this.cms = cms;
        createDesign();
    }

    public ChartDatasourceView getChartDatasourceView() {
        return chartDatasourceView;
    }

    public void createDesign() throws UnifyException {
        final ChartDataSource chartDataSource = (ChartDataSource) form.getFormBean();
        final Long chartDataSourceId = chartDataSource.getId();
        if (chartDataSourceId != null) {
            String subTitle = chartDataSource.getDescription();
            chartDatasourceView = constructNewChartDatasourceView(
                    ApplicationNameUtils.getApplicationEntityLongName(getApplicationName(), chartDataSource.getName()),
                    chartDataSourceId, subTitle);
            chartDatasourceView.reloadContent();
        }
    }

    private ChartDatasourceView constructNewChartDatasourceView(String chartDatasourceName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        ChartDataSourceDef chartDataSourceDef = cms.getChartDataSourceDef(chartDatasourceName);
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{chartdatasourceeditor.chartdatasourcedesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new ChartDatasourceView(au(), chartDataSourceDef, id, breadCrumbs);
    }

}
