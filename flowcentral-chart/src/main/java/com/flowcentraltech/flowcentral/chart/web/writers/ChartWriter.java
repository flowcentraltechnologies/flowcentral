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

package com.flowcentraltech.flowcentral.chart.web.writers;

import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.data.ChartData;
import com.flowcentraltech.flowcentral.chart.data.ChartDataProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.util.ChartUtils;
import com.flowcentraltech.flowcentral.chart.web.widgets.ChartWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractWidgetWriter;

/**
 * Chart writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(ChartWidget.class)
@Component("fc-chart-writer")
public class ChartWriter extends AbstractWidgetWriter {

    @Configurable
    private ChartModuleService chartModuleService;

    public void setChartModuleService(ChartModuleService chartModuleService) {
        this.chartModuleService = chartModuleService;
    }

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        ChartWidget chartWidget = (ChartWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, chartWidget);
        writer.write(">");
        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);

        ChartWidget chartWidget = (ChartWidget) widget;
        final String chartLongName = chartWidget.getValue(String.class);
        ChartDef chartDef = chartModuleService.getChartDef(chartLongName);
        ChartData chartData = ((ChartDataProvider) getComponent(chartDef.getProvider())).provide(chartDef.getRule());
        writer.beginFunction("fux.rigChart");
        writer.writeParam("pId", chartWidget.getId());
        writer.writeParam("pOptions", ChartUtils.getOptionsJsonWriter(chartDef, chartData, chartWidget.isSparkLine()));
        writer.endFunction();
    }
}
