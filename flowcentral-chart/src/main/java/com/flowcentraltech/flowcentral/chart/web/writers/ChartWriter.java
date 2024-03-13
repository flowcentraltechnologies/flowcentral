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

import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
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
 * @since 1.0
 */
@Writes(ChartWidget.class)
@Component("fc-chart-writer")
public class ChartWriter extends AbstractWidgetWriter {

    private final String CHART_DETAILS = "CHART_DETAILS";

    private final String CHART_DETAILS_CACHE = "CHART_DETAILS_CACHE";

    @Configurable
    private ChartModuleService chartModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        ChartWidget chartWidget = (ChartWidget) widget;
        final ChartConfiguration configuration = chartWidget.getChartConfiguration();
        final String chartLongName = chartWidget.getValue(String.class);
        ChartDef chartDef = chartModuleService.getChartDef(chartLongName);
        ChartDetails chartDetails = getChartDetailsCache().getChartDetails(configuration, chartDef.getProvider(),
                chartDef.getRule());
        writer.write("<div");
        writeTagAttributes(writer, chartWidget);
        writer.write(">");
        if (chartDef.getType().isCard()) {
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
            String fmt = ChartUtils.getFormattedCardValue(num);
            writer.writeWithHtmlEscape(fmt);
            writer.write("</span>");

            writer.write("</div>");
        } else if (chartDef.getType().isTable()) {
            if (chartDetails.isWithTableSeries()) {
                FormatterOptions.Instance options = FormatterOptions.DEFAULT.createInstance(getUnifyComponentContext());
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
                final int cols = chartDetails.getTableHeaders().length;
                final ChartTableColumn[] headers = chartDetails.getTableHeaders();
                writer.write("<div class=\"bdy\" style=\"width:100%;overflow-y:auto;overflow-x: hidden;\">");
                writer.write("<table class=\"cont\" style=\"width:100%;\"><thead>");
                writer.write("<tr style=\"background-color:");
                writer.write(chartDef.getColor());
                writer.write(";position: sticky;top: 0px;\">");
                for (ChartTableColumn header : headers) {
                    writer.write("<th>");
                    writer.writeWithHtmlEscape(header.getLabel());
                    writer.write("</th>");
                }
                writer.write("</tr></thead>");

                writer.write("<tbody>");
                for (Object[] row : chartDetails.getTableSeries()) {
                    writer.write("<tr>");
                    for (int i = 0; i < cols; i++) {
                        ChartTableColumn header = headers[i];
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

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);

        ChartWidget chartWidget = (ChartWidget) widget;
        WriteWork work = chartWidget.getWriteWork();
        ChartDetails chartDetails = work.get(ChartDetails.class, CHART_DETAILS);

        final String chartLongName = chartWidget.getValue(String.class);
        ChartDef chartDef = chartModuleService.getChartDef(chartLongName);
        if (chartDef.getType().isFlowCentralType()) {

        } else {
            writer.beginFunction("fux.rigChart");
            writer.writeParam("pId", chartWidget.getId());
            writer.writeParam("pOptions", ChartUtils.getOptionsJsonWriter(chartDef, chartDetails,
                    chartWidget.isSparkLine(), chartWidget.getPreferredHeight()));
            writer.endFunction();
        }
    }

    private ChartDetailsCache getChartDetailsCache() throws UnifyException {
        ChartDetailsCache cache = getRequestAttribute(ChartDetailsCache.class, CHART_DETAILS_CACHE);
        if (cache == null) {
            setRequestAttribute(CHART_DETAILS_CACHE, cache = new ChartDetailsCache());
        }

        return cache;
    }

    /**
     * For reuse of chart details when charts to be rendered in the same request
     * context share the same provider details. No synchronization needed since it's
     * request context (same thread).
     */
    private class ChartDetailsCache {

        private Map<String, ChartDetails> cache;

        private ChartDetailsCache() {
            this.cache = new HashMap<String, ChartDetails>();
        }

        public ChartDetails getChartDetails(ChartConfiguration configuration, String providerName, String rule)
                throws UnifyException {
            final String key = providerName + (!StringUtils.isBlank(rule) ? "." + rule : "");
            ChartDetails chartDetails = cache.get(key);
            if (chartDetails == null) {
                Restriction restriction = null;
                ChartDetailsProvider provider = (ChartDetailsProvider) getComponent(providerName);
                if (provider.isUsesChartDataSource()) {
                    final ChartDataSourceDef chartDataSourceDef = chartModuleService.getChartDataSourceDef(rule);
                    final EntityDef entityDef = chartDataSourceDef.getEntityDef();
                    restriction = InputWidgetUtils.getRestriction(entityDef,
                            configuration.getCatBase(chartDataSourceDef.getLongName()),
                            appletUtilities.specialParamProvider(), chartModuleService.getNow());
                }
                
                cache.put(key, chartDetails = provider.provide(rule, restriction));
            }

            return chartDetails;
        }
    }
}
