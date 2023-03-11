/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.chart.util;

import com.flowcentraltech.flowcentral.chart.data.ChartCategories;
import com.flowcentraltech.flowcentral.chart.data.ChartData;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartSeries;
import com.flowcentraltech.flowcentral.chart.data.DateChartCategories;
import com.flowcentraltech.flowcentral.chart.data.DoubleChartSeries;
import com.flowcentraltech.flowcentral.chart.data.IntegerChartCategories;
import com.flowcentraltech.flowcentral.chart.data.IntegerChartSeries;
import com.flowcentraltech.flowcentral.chart.data.LongChartCategories;
import com.flowcentraltech.flowcentral.chart.data.LongChartSeries;
import com.flowcentraltech.flowcentral.chart.data.StringChartCategories;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.json.JsonWriter;

/**
 * Chart utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ChartUtils {

    private ChartUtils() {

    }

    public static JsonWriter getOptionsJsonWriter(ChartDef chartDef, ChartData chartData, boolean sparkLine)
            throws UnifyException {
        JsonWriter jw = new JsonWriter();
        jw.beginObject();
        final ChartType chartType = chartDef.getType();
        final ChartCategories<?> categories = chartData.getCategories();
        final ChartCategoryDataType categoryType = categories.getCategoryType();
        // Title
        String title = !StringUtils.isBlank(chartData.getTitle()) ? chartData.getTitle() : chartDef.getTitle();
        if (!StringUtils.isBlank(title)) {
            jw.beginObject("title");
            jw.write("text", title);
            if (chartData.getTitleOffsetX() > 0) {
                jw.write("offsetX", chartData.getTitleOffsetX());
            }

            if (chartData.getTitleFontSize() > 0) {
                jw.beginObject("style");
                jw.write("fontSize", chartData.getTitleFontSize() + "px");
                jw.endObject();
            }

            jw.endObject();
        }

        // Sub-title
        String subTitle = !StringUtils.isBlank(chartData.getSubTitle()) ? chartData.getSubTitle()
                : chartDef.getSubTitle();
        if (!StringUtils.isBlank(subTitle)) {
            jw.beginObject("subtitle");
            jw.write("text", subTitle);
            if (chartData.getSubTitleOffsetX() > 0) {
                jw.write("offsetX", chartData.getSubTitleOffsetX());
            }

            jw.beginObject("style");
            jw.write("color", "#a0a0a0");
            if (chartData.getSubTitleFontSize() > 0) {
                jw.write("fontSize", chartData.getSubTitleFontSize() + "px");
            }
            jw.endObject();
            jw.endObject();
        }

        // Chart
        jw.beginObject("chart");
        if (chartDef.getWidth() > 0) {
            jw.write("width", chartDef.getWidth());
        }

        if (chartDef.getHeight() > 0) {
            jw.write("height", chartDef.getHeight());
        }

        jw.write("type", chartType.optionsType());
        jw.write("stacked", chartDef.isStacked());
        jw.beginObject("toolbar");
        jw.write("show", false);
        jw.endObject();

        jw.beginObject("sparkline");
        jw.write("enabled", sparkLine);
        jw.endObject();

        jw.endObject();

        // Grid
        jw.beginObject("grid");
        jw.write("show", chartDef.isShowGrid());
        jw.endObject();

        // Stroke
        jw.beginObject("stroke");
        if (chartDef.isSmooth()) {
            jw.write("curve", "smooth");
        } else {
            jw.write("curve", "straight");
        }
        jw.write("width", 1.5);
        jw.endObject();

        // Data labels
        jw.beginObject("dataLabels");
        jw.write("enabled", chartDef.isShowDataLabels());
        jw.endObject();

        // Theme
        jw.beginObject("theme");
        jw.write("mode", "light");
        jw.write("palette", chartDef.getPaletteType().optionsType());
        jw.endObject();

        // Options
        if (chartType.plotOptions()) {
            jw.beginObject("plotOptions");
            if (chartType.axisChart()) {
                jw.beginObject("bar");
                jw.write("horizontal", ChartType.BAR.equals(chartType));
                jw.endObject();
            } else {
                jw.beginObject("pie");
                jw.write("customScale", 0.8);
                // jw.write("offsetY", -20);
                if (ChartType.DONUT.equals(chartType)) {
                    jw.beginObject("donut");
                    jw.write("size", "75%");
                    jw.endObject();
                }
                jw.endObject();
            }

            jw.endObject();
        }

        if (chartType.axisChart()) {
            // Series
            boolean integers = true;
            jw.beginArray("series");
            for (ChartSeries<?> series : chartData.getSeries()) {
                jw.beginObject();
                jw.write("name", series.getName());
                switch (series.getSeriesType()) {
                    case DOUBLE:
                        jw.write("data", ((DoubleChartSeries) series).dataToArray());
                        integers = false;
                        break;
                    case INTEGER:
                        jw.write("data", ((IntegerChartSeries) series).dataToArray());
                        break;
                    case LONG:
                        jw.write("data", ((LongChartSeries) series).dataToArray());
                        break;
                    default:
                        break;
                }
                jw.endObject();
            }
            jw.endArray();

            // Y-axis
            jw.write("_yintegers", integers);

            // X-axis
            jw.beginObject("xaxis");
            jw.write("type", categoryType.optionsType());
            switch (categoryType) {
                case DATE:
                    jw.write("categories",
                            DataUtils.convert(Long[].class, ((DateChartCategories) categories).categoriesToArray()));
                    break;
                case INTEGER:
                    jw.write("categories", ((IntegerChartCategories) categories).categoriesToArray());
                    break;
                case LONG:
                    jw.write("categories", ((LongChartCategories) categories).categoriesToArray());
                    break;
                case STRING:
                    jw.write("categories", ((StringChartCategories) categories).categoriesToArray());
                    break;
                default:
                    break;
            }
            jw.endObject();

            // Legend
        } else {
            // Series
            ChartSeries<?> series = chartData.getSeries().get(0);
            switch (series.getSeriesType()) {
                case DOUBLE:
                    jw.write("series", ((DoubleChartSeries) series).dataToArray());
                    break;
                case INTEGER:
                    jw.write("series", ((IntegerChartSeries) series).dataToArray());
                    break;
                case LONG:
                    jw.write("series", ((LongChartSeries) series).dataToArray());
                    break;
                default:
                    break;
            }

            // Labels
            jw.write("labels", DataUtils.convert(String[].class, categories.categoriesToArray()));

            // Legend
            jw.beginObject("legend");
            jw.write("position", "left");
            jw.write("offsetY", 60);
            jw.endObject();
        }

        jw.endObject();
        return jw;
    }

    public static String getOptionsJson(ChartDef chartDef, ChartData chartData, boolean sparkLine)
            throws UnifyException {
        return ChartUtils.getOptionsJsonWriter(chartDef, chartData, sparkLine).toString();
    }
}
