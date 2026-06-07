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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.business.ChartOptionsProvider;
import com.flowcentraltech.flowcentral.chart.constants.ChartRequestAttributeConstants;
import com.flowcentraltech.flowcentral.chart.data.ChartConfiguration;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartDetailsProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartTableColumn;
import com.flowcentraltech.flowcentral.chart.util.ChartUtils;
import com.flowcentraltech.flowcentral.chart.web.widgets.ChartWidget;
import com.flowcentraltech.flowcentral.common.data.FormatterOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.constant.TimeResolutionType;
import com.tcdng.unify.core.criterion.Restriction;
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
        final ChartConfiguration configuration = chartWidget.getChartConfiguration();
        final String chartLongName = chartWidget.getValue(String.class);
        if (!StringUtils.isBlank(chartLongName)) {
            final ChartDef chartDef = chartModuleService.getChartDef(chartLongName);
            final ChartDetails chartDetails = getChartDetails(configuration, chartDef);
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
                    Number num = null;//TODO chartDetails.getSeries().get(chartDef.getSeries()).getData(chartDef.getCategory());
                    String fmt = null;//TODO ChartUtils.getFormattedCardValue(num);
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
                    final int cols = 0;//TODO chartDetails.getTableHeaders().length;
                    final ChartTableColumn[] headers = null;//chartDetails.getTableHeaders();
                    writer.write("<div class=\"bdy\" style=\"width:100%;overflow-y:auto;overflow-x: hidden;\">");
                    writer.write("<table class=\"cont\" style=\"width:100%;\"><thead>");
                    writer.write("<tr style=\"background-color:");
                    writer.write(chartDef.getColor());
                    writer.write(";position: sticky;top: 0px;\">");
                    for (ChartTableColumn header : headers) {
                        if (header.isSeries()/* && !chartDetails.isSeriesFieldInclusion(header.getFieldName())*/) {
                            continue;
                        }

                        writer.write("<th>");
                        writer.writeWithHtmlEscape(header.getLabel());
                        writer.write("</th>");
                    }
                    writer.write("</tr></thead>");

                    writer.write("<tbody>");
                    for (Object[] row : new ArrayList<Object[]>()) { // TODO
                        writer.write("<tr>");
                        for (int i = 0; i < cols; i++) {
                            ChartTableColumn header = headers[i];
                            if (header.isSeries()/* && !chartDetails.isSeriesFieldInclusion(header.getFieldName())*/) {
                                continue;
                            }

                            String[] sval = options.format(header.getType(), row[i]);
                            writer.write("<td><span class=\"");
                            writer.write(header.getType().alignType().styleClass());
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

        final String chartLongName = chartWidget.getValue(String.class);
        if (!StringUtils.isBlank(chartLongName)) {
            ChartDef chartDef = chartModuleService.getChartDef(chartLongName);
            if (!chartDef.getType().isFlowCentralType() && chartDetails != null) {
                writer.beginFunction("fux.rigChart");
                writer.writeParam("pId", chartWidget.getId());
                writer.writeParam("pType", chartOptionsProvider.getOptionsType());
                writer.writeParam("pOptions", chartOptionsProvider.getChartOptions(chartDef, chartDetails,
                        chartWidget.isSparkLine(), chartWidget.getPreferredHeight()));
                writer.endFunction();
            }
        }
    }

    private ChartDetails getChartDetails(ChartConfiguration configuration, ChartDef chartDef) {
        // TODO Auto-generated method stub
        return null;
    }

}
