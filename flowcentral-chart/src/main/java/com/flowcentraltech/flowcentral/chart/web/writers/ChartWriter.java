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

package com.flowcentraltech.flowcentral.chart.web.writers;

import java.text.DecimalFormat;

import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartDetailsProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.util.ChartUtils;
import com.flowcentraltech.flowcentral.chart.web.widgets.ChartWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
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

    private final String CHART_DETAILS = "CHART_DETAILS";

    @Configurable
    private ChartModuleService chartModuleService;

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        ChartWidget chartWidget = (ChartWidget) widget;
        final String chartLongName = chartWidget.getValue(String.class);
        ChartDef chartDef = chartModuleService.getChartDef(chartLongName);
        ChartDetails chartDetails = ((ChartDetailsProvider) getComponent(chartDef.getProvider()))
                .provide(chartDef.getRule());
        writer.write("<div");
        writeTagAttributes(writer, chartWidget);
        writer.write(">");
        if (chartDef.getType().card()) {
            writer.write("<div class=\"card\">");

            writer.write("<span class=\"title\">");
            if (chartDef.isWithTitle()) {
                writer.writeWithHtmlEscape(chartDef.getTitle());
            }
            writer.write("</span>");

            writer.write("<span class=\"subtitle\">");
            if (chartDef.isWithSubtitle()) {
                writer.writeWithHtmlEscape(chartDef.getSubTitle());
            }
            writer.write("</span>");

            writer.write("<span class=\"content\" style=\"color:");
            writer.write(chartDef.isWithColor() ? chartDef.getColor() : "#606060");
            writer.write(";\">");
            Number num = chartDetails.getSeries().get(chartDef.getSeries()).getData(chartDef.getCategory());
            String fmt = num != null ? new DecimalFormat("###,###").format(num) : "";
            writer.writeWithHtmlEscape(fmt);
            writer.write("</span>");

            writer.write("</div>");
        } else if (chartDef.getType().table()) {

        }

        writer.write("</div>");

        WriteWork work = chartWidget.getWriteWork();
        work.set(CHART_DETAILS, chartDetails);
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);

        ChartWidget chartWidget = (ChartWidget) widget;
        WriteWork work = chartWidget.getWriteWork();
        ChartDetails chartDetails = work.get(ChartDetails.class, CHART_DETAILS);

        final String chartLongName = chartWidget.getValue(String.class);
        ChartDef chartDef = chartModuleService.getChartDef(chartLongName);
        if (chartDef.getType().custom()) {

        } else {
            writer.beginFunction("fux.rigChart");
            writer.writeParam("pId", chartWidget.getId());
            writer.writeParam("pOptions", ChartUtils.getOptionsJsonWriter(chartDef, chartDetails,
                    chartWidget.isSparkLine(), chartWidget.getPreferredHeight()));
            writer.endFunction();
        }
    }
}
