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

package com.flowcentraltech.flowcentral.chart.web.writers;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.business.ChartOptionsProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartSeries;
import com.flowcentraltech.flowcentral.chart.util.ChartUtils;
import com.flowcentraltech.flowcentral.chart.web.widgets.ChartWidget;
import com.flowcentraltech.flowcentral.common.data.FormatterOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.writer.AbstractWidgetWriter;

/**
 * Chart writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(ChartWidget.class)
@Component("fc-chart-writer")
public class ChartWriter extends AbstractWidgetWriter {

    private final String CHART_DETAILS = "CHART_DETAILS";

    @Configurable
    private ChartModuleService chartModuleService;

    @Configurable
    private ChartOptionsProvider chartOptionsProvider;

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        ChartWidget chartWidget = (ChartWidget) widget;
        final String chartLongName = chartWidget.getValue(String.class);
        if (!StringUtils.isBlank(chartLongName)) {
            final ChartDetails chartDetails = chartModuleService.getChartDetails(chartWidget.getChartConfiguration());
            if (chartDetails.isPresent()) {
                final ChartDef chartDef = chartDetails.getChartDef();
                writer.write("<div ");
                writeTagAttributes(writer, chartWidget);
                if (chartDef.getHeight() > 0 && !chartDef.getType().isTable()) {
                    writer.write(" style=\"height:").write(chartDef.getHeight()).write("px;\"");
                }

                writer.write(">");
                if (chartDef.getType().isCard()) {
                    if (true) {
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
                        Number num = (Number) chartDetails.getSeries()[0].getVals()[0];
                        String fmt = ChartUtils.getFormattedCardValue(num);
                        writer.writeWithHtmlEscape(fmt);
                        writer.write("</span>");

                        writer.write("</div>");
                    }
                } else if (chartDef.getType().isTable()) {
                    if (true) {
                        FormatterOptions.Instance options = FormatterOptions.DEFAULT
                                .createInstance(getUnifyComponentContext());
                        writer.write("<div class=\"tbl\"");
                        if (chartDef.getHeight() > 0) {
                            writer.write(" style=\"height:").write(chartDef.getHeight()).write("px;\"");
                        }

                        writer.write(">");
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

                        // Header
                        final ChartSeries[] series = chartDetails.getSeries();
                        final int cols = series.length;
                        final int rows = series[0].getVals().length;
                        writer.write("<div class=\"bdy\" style=\"width:100%;overflow-y:auto;overflow-x: hidden;\">");
                        writer.write("<table class=\"cont\" style=\"width:100%;\"><thead>");
                        writer.write("<tr style=\"background-color:");
                        writer.write(chartDef.getColor());
                        writer.write(";position: sticky;top: 0px;\">");
                        for (ChartSeries _series : series) {
                            writer.write("<th>");
                            writer.writeWithHtmlEscape(_series.getLabel());
                            writer.write("</th>");
                        }
                        writer.write("</tr></thead>");

                        writer.write("<tbody>");
                        for (int r = 0; r < rows; r++) {
                            writer.write("<tr>");
                            for (int c = 0; c < cols; c++) {
                                ChartSeries _series = series[c];

                                String[] sval = options.format(_series.getType(), _series.getVals()[r]);
                                writer.write("<td><span class=\"");
                                writer.write(_series.getType().alignType().styleClass());
                                writer.write("\">");
                                writer.writeWithHtmlEscape(sval[0]);
                                writer.write("</span></td>");
                            }
                            writer.write("</tr>");
                        }
                        writer.write("</tbody>");
                        writer.write("</table>");
                        writer.write("</div>");

                        writer.write("</div>");
                    }
                }

                writer.write("</div>");
            }

            WriteWork work = chartWidget.getWriteWork();
            work.set(CHART_DETAILS, chartDetails);
        }
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);

        ChartWidget chartWidget = (ChartWidget) widget;
        WriteWork work = chartWidget.getWriteWork();
        ChartDetails chartDetails = work.get(ChartDetails.class, CHART_DETAILS);
        if (chartDetails != null && chartDetails.isPresent()) {
            ChartDef chartDef = chartDetails.getChartDef();
            if (!chartDef.getType().isFlowCentralType() && chartDetails != null) {
                writer.beginFunction("fux.rigChart");
                writer.writeParam("pId", chartWidget.getId());
                writer.writeParam("pType", chartOptionsProvider.getOptionsType());
                writer.writeParam("pOptions", chartOptionsProvider.getChartOptions(chartDetails));
                writer.endFunction();
            }
        }
    }

}
