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

package com.flowcentraltech.flowcentral.chart.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.chart.data.AbstractSeries;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartType;
import com.tcdng.unify.core.UnifyException;
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

    public static JsonWriter getOptionsJsonWriter(ChartDef chartDef, ChartDetails chartDetails, boolean sparkLine,
            int preferredHeight) throws UnifyException {
        JsonWriter jw = new JsonWriter();
        jw.beginObject();
        final ChartType chartType = chartDef.getType();
        final Map<String, AbstractSeries<?, ?>> series = chartDetails.getSeries();
        final ChartCategoryDataType categoryType = chartDetails.getCategoryType();

        // Title
        String title = !StringUtils.isBlank(chartDetails.getTitle()) ? chartDetails.getTitle() : chartDef.getTitle();
        if (!StringUtils.isBlank(title)) {
            jw.beginObject("title");
            jw.write("text", title);
            if (chartDetails.getTitleOffsetX() > 0) {
                jw.write("offsetX", chartDetails.getTitleOffsetX());
            }

            if (chartDetails.getTitleFontSize() > 0) {
                jw.beginObject("style");
                jw.write("fontSize", chartDetails.getTitleFontSize() + "px");
                jw.endObject();
            }

            jw.endObject();
        }

        // Sub-title
        String subTitle = !StringUtils.isBlank(chartDetails.getSubTitle()) ? chartDetails.getSubTitle()
                : chartDef.getSubTitle();
        if (!StringUtils.isBlank(subTitle)) {
            jw.beginObject("subtitle");
            jw.write("text", subTitle);
            if (chartDetails.getSubTitleOffsetX() > 0) {
                jw.write("offsetX", chartDetails.getSubTitleOffsetX());
            }

            jw.beginObject("style");
            jw.write("color", "#a0a0a0");
            if (chartDetails.getSubTitleFontSize() > 0) {
                jw.write("fontSize", chartDetails.getSubTitleFontSize() + "px");
            }
            jw.endObject();
            jw.endObject();
        }

        // Chart
        jw.beginObject("chart");
        if (chartDef.getWidth() > 0) {
            jw.write("width", chartDef.getWidth());
        }

        int _preferredHeight = chartDef.getHeight() > 0 ? chartDef.getHeight() : preferredHeight;
        if (_preferredHeight > 0) {
            jw.write("height", _preferredHeight);
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
        jw.write("_dformatter", chartDef.isFormatDataLabels());
        jw.endObject();

        // Theme
        jw.beginObject("theme");
        jw.write("mode", "light");
        if (chartDef.getPaletteType().monochrome()) {
            jw.beginObject("monochrome");
            jw.write("enabled", true);
            jw.write("color", chartDef.isWithColor() ? chartDef.getColor() : "#606060");
            jw.write("shadeTo", "light");
            jw.write("shadeIntensity", 0.65);
            jw.endObject();
        } else {
            jw.write("palette", chartDef.getPaletteType().optionsType());
        }
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

        List<AbstractSeries<?, ?>> actseries = new ArrayList<AbstractSeries<?, ?>>(series.values());
        if (chartType.axisChart()) {
            // Series
            boolean integers = true;
            jw.beginArray("series");
            for (AbstractSeries<?, ?> _series : actseries) {
                _series.writeAsObject(jw);
                integers &= _series.getDataType().isInteger();
            }
            jw.endArray();

            // Y-axis
            jw.write("_yintegers", integers);
            jw.write("_yformatter", chartDef.isFormatYLabels());
            jw.beginObject("yaxis");
            jw.beginObject("labels");
            jw.endObject();
            jw.endObject();

            // X-axis
            jw.beginObject("xaxis");
            jw.write("type", categoryType.optionsType());
            jw.endObject();

            // Legend
        } else {
            AbstractSeries<?, ?> pseries = actseries.get(0);

            // Series
            pseries.writeYValuesArray("series", jw);

            // Labels
            pseries.writeXValuesArray("labels", jw);

            // Legend
            jw.beginObject("legend");
            jw.write("position", "left");
            jw.write("offsetY", 60);
            jw.endObject();
        }

        jw.endObject();
        return jw;
    }

    public static String getOptionsJson(ChartDef chartDef, ChartDetails chartDetails, boolean sparkLine,
            int preferredHeight) throws UnifyException {
        return ChartUtils.getOptionsJsonWriter(chartDef, chartDetails, sparkLine, preferredHeight).toString();
    }
}
