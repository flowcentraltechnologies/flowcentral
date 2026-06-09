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
package com.flowcentraltech.flowcentral.chart.apexcharts.business;

import com.flowcentraltech.flowcentral.chart.apexcharts.constants.ApexChartsNameConstants;
import com.flowcentraltech.flowcentral.chart.business.AbstractChartOptionsProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.json.JsonWriter;

/**
 * Apex chart options provider implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Deprecated
@Component(ApexChartsNameConstants.APEXCHARTS_OPTIONS_PROVIDER)
public class ApexChartsOptionsProviderImpl extends AbstractChartOptionsProvider {

    @Override
    public String getOptionsType() throws UnifyException {
        return "apexcharts";
    }

    @Override
    public JsonWriter getChartOptions(ChartDetails chartDetails) throws UnifyException {
        JsonWriter jw = new JsonWriter();
        jw.beginObject();
//        final ChartType chartType = chartDef.getType();
//        final boolean isDynamicCategories = chartDetails.isDynamicCategories();
//        final Map<String, AbstractSeries<?, ?>> series = isDynamicCategories && chartType.axisChart()
//                ? chartDetails.getGroupingSeries(chartDef)
//                : (chartDetails.getSeries(chartDetails.isWithSeriesInclusion() ? chartDetails.getSeriesInclusion()
//                        : chartDef.getSeriesInclusion()));
//        final ChartCategoryDataType categoryType = chartDetails.getCategoryType();
//
//        // Title
//        String title = !StringUtils.isBlank(chartDetails.getTitle()) ? chartDetails.getTitle() : chartDef.getTitle();
//        if (!StringUtils.isBlank(title)) {
//            jw.beginObject("title");
//            jw.write("text", title);
//            if (chartDetails.getTitleOffsetX() > 0) {
//                jw.write("offsetX", chartDetails.getTitleOffsetX());
//            }
//
//            if (chartDetails.getTitleFontSize() > 0) {
//                jw.beginObject("style");
//                jw.write("fontSize", chartDetails.getTitleFontSize() + "px");
//                jw.endObject();
//            }
//
//            jw.endObject();
//        }
//
//        // Sub-title
//        String subTitle = !StringUtils.isBlank(chartDetails.getSubTitle()) ? chartDetails.getSubTitle()
//                : chartDef.getSubTitle();
//        if (!StringUtils.isBlank(subTitle)) {
//            jw.beginObject("subtitle");
//            jw.write("text", subTitle);
//            if (chartDetails.getSubTitleOffsetX() > 0) {
//                jw.write("offsetX", chartDetails.getSubTitleOffsetX());
//            }
//
//            jw.beginObject("style");
//            jw.write("color", "#a0a0a0");
//            if (chartDetails.getSubTitleFontSize() > 0) {
//                jw.write("fontSize", chartDetails.getSubTitleFontSize() + "px");
//            }
//            jw.endObject();
//            
//            jw.endObject();
//        }
//
//        // Chart
//        jw.beginObject("chart");
//        if (chartDef.getWidth() > 0) {
//            jw.write("width", chartDef.getWidth());
//        }
//
//        int _preferredHeight = chartDef.getHeight() > 0 ? chartDef.getHeight() : preferredHeight;
//        if (_preferredHeight > 0) {
//            jw.write("height", _preferredHeight);
//        }
//
//        jw.write("type", chartType.optionsType());
//        jw.write("stacked", chartDef.isStacked());
//        jw.beginObject("toolbar");
//        jw.write("show", false);
//        jw.endObject();
//
//        jw.beginObject("sparkline");
//        jw.write("enabled", sparkLine);
//        jw.endObject();
//
//        jw.endObject();
//
//        // Grid
//        jw.beginObject("grid");
//        jw.write("show", chartDef.isShowGrid());
//        jw.endObject();
//
//        // Stroke
//        jw.beginObject("stroke");
//        if (chartDef.isSmooth()) {
//            jw.write("curve", "smooth");
//        } else {
//            jw.write("curve", "straight");
//        }
//        jw.write("width", 1.5);
//        jw.endObject();
//
//        // Data labels
//        jw.beginObject("dataLabels");
//        jw.write("enabled", chartDef.isShowDataLabels());
//        if (chartType.isPieChart() || chartType.isDonutChart()) {
//            jw.write("_cformatter", true);
//        } else {
//            jw.write("_dformatter", chartDef.isFormatDataLabels());
//        }
//        jw.endObject();
//
//        // Theme
//        jw.beginObject("theme");
//        jw.write("mode", "light");
//        if (chartDef.getPaletteType().monochrome()) {
//            jw.beginObject("monochrome");
//            jw.write("enabled", true);
//            jw.write("color", chartDef.isWithColor() ? chartDef.getColor() : "#606060");
//            jw.write("shadeTo", "light");
//            jw.write("shadeIntensity", 0.65);
//            jw.endObject();
//        } else {
//            jw.write("palette", chartDef.getPaletteType().optionsType());
//        }
//
//        jw.endObject();
//
//        // Options
//        if (chartType.plotOptions()) {
//            jw.beginObject("plotOptions");
//            if (chartType.axisChart()) {
//                jw.beginObject("bar");
//                jw.write("horizontal", chartType.isBar());
//                jw.endObject();
//            } else {
//                jw.beginObject("pie");
//                jw.write("customScale", 0.8);
//                if (ChartType.DONUT.equals(chartType)) {
//                    jw.beginObject("donut");
//                    jw.write("size", "65%");
//                    jw.endObject();
//                }
//                jw.endObject();
//            }
//
//            jw.endObject();
//        }
//
//        final Set<String> categoryInclusion = isDynamicCategories ? Collections.emptySet()
//                : (chartDetails.isWithCategoryInclusion() ? chartDetails.getCategoryInclusion()
//                        : chartDef.getCategoryInclusion());
//        List<AbstractSeries<?, ?>> actseries = new ArrayList<AbstractSeries<?, ?>>(series.values());
//        if (!DataUtils.isBlank(actseries)) {
//            if (chartType.axisChart()) {
//                // Series
//                boolean integers = true;
//                jw.beginArray("series");
//                for (AbstractSeries<?, ?> _series : actseries) {
//                    _series.setCategoryInclusion(categoryInclusion);
//                    _series.writeAsObject(jw);
//                    integers &= _series.getDataType().isInteger();
//                }
//                jw.endArray();
//
//                if (chartType.isColumn() || chartType.isLine() || chartType.isArea()) {
//                    // Y-axis
//                    jw.write("_yintegers", integers);
//                    jw.write("_yformatter", chartDef.isFormatYLabels());
//                    jw.beginObject("yaxis");
//                    jw.beginObject("labels");
//                    jw.endObject();
//                    jw.endObject();
//
//                    // X-axis
//                    jw.beginObject("xaxis");
//                    jw.write("type", categoryType.optionsType());
//                    jw.endObject();
//                }
//            } else {
//                AbstractSeries<?, ?> pseries = actseries.get(0);
//                pseries.setCategoryInclusion(categoryInclusion);
//
//                // Series
//                pseries.writeYValuesArray("series", jw);
//
//                // Labels
//                pseries.writeXValuesArray("labels", jw);
//
//                // Legend
//                jw.beginObject("legend");
//                jw.write("position", "left");
//                jw.write("offsetY", 60);
//                jw.endObject();
//            }
//        }
//
        jw.endObject();
        return jw;
    }

}
