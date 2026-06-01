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
package com.flowcentraltech.flowcentral.chart.echarts.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.chart.business.AbstractChartOptionsProvider;
import com.flowcentraltech.flowcentral.chart.data.AbstractSeries;
import com.flowcentraltech.flowcentral.chart.data.AbstractSeries.AbstractSeriesData;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.echarts.constants.EChartsNameConstants;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartColorType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartPaletteType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.json.JsonWriter;

/**
 * Echart options provider implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(EChartsNameConstants.ECHARTS_OPTIONS_PROVIDER)
public class EChartsOptionsProviderImpl extends AbstractChartOptionsProvider {

    private static final Map<ChartPaletteType, List<String>> PALLETE_MAP;

    private static final Map<ChartColorType, List<String>> MONOCHROME_MAP;

    static {
        Map<ChartPaletteType, List<String>> map = new HashMap<ChartPaletteType, List<String>>();
        map.put(ChartPaletteType.PALETTE1,
                Arrays.asList("#5470C6", "#91CC75", "#FAC858", "#EE6666", "#73C0DE", "#3BA272", "#FC8452", "#9A60B4"));
        map.put(ChartPaletteType.PALETTE2,
                Arrays.asList("#1f4e5b", "#2e7d7a", "#dfcdb4", "#e09f3e", "#9e2a2b", "#540b0e", "#6f5060", "#8cb369"));
        map.put(ChartPaletteType.PALETTE3,
                Arrays.asList("#FF6B6B", "#4ECDC4", "#FFE66D", "#1A535C", "#FF9F1C", "#2EC4B6", "#E71D36", "#011627"));
        map.put(ChartPaletteType.PALETTE4,
                Arrays.asList("#A5DEE5", "#FFDAC1", "#E2F0CB", "#C7CEEA", "#FFB7B2", "#B5EAD7", "#F6EAC2", "#D7BDE2"));
        map.put(ChartPaletteType.PALETTE5,
                Arrays.asList("#003F5C", "#2F4B7C", "#665191", "#A05195", "#D45087", "#F95D6A", "#FF7C43", "#FFA600"));
        map.put(ChartPaletteType.PALETTE6,
                Arrays.asList("#2E7D32", "#66BB6A", "#1E88E5", "#FB8C00", "#8E24AA", "#D81B60", "#00897B", "#FDD835"));
        map.put(ChartPaletteType.PALETTE7,
                Arrays.asList("#FF6F61", "#FF9472", "#FFB88C", "#FFD3A5", "#FF8A80", "#FF5252", "#FF7043", "#FFAB91"));
        map.put(ChartPaletteType.PALETTE8,
                Arrays.asList("#FF00FF", "#00FFFF", "#39FF14", "#FF073A", "#FFD300", "#7C00FF", "#00FF9F", "#FF6EC7"));
        map.put(ChartPaletteType.PALETTE9,
                Arrays.asList("#8D6E63", "#3F51B5", "#FF7043", "#26A69A", "#AB47BC", "#5C6BC0", "#EF5350", "#9CCC65"));
        map.put(ChartPaletteType.PALETTE10,
                Arrays.asList("#ff0054", "#ff5400", "#ffbd00", "#70e000", "#38b000", "#00b4d8", "#0077b6", "#9d4edd"));
        PALLETE_MAP = Collections.unmodifiableMap(map);
        
        Map<ChartColorType, List<String>> map2 = new HashMap<ChartColorType, List<String>>();
        map2.put(ChartColorType.RED,
                Arrays.asList("#72170e", "#83332b", "#944f48", "#a56b65", "#b68783", "#c7a3a0", "#d8c0bd", "#e9dcda"));
        map2.put(ChartColorType.ORANGE,
                Arrays.asList("#e65c00", "#e96f1e", "#ec833d", "#ef975c", "#f2ab7b", "#f5be9a", "#f8d2b9", "#fbe6d8"));
        map2.put(ChartColorType.YELLOW,
                Arrays.asList("#f1c927", "#f2cf41", "#f4d65b", "#f6dc75", "#f7e38f", "#f9e9aa", "#fbf0c4", "#fcf6de"));
        map2.put(ChartColorType.GREEN,
                Arrays.asList("#1d6355", "#387569", "#53887e", "#6f9b92", "#8aaea7", "#a6c1bc", "#c1d4d0", "#dde7e5"));
        map2.put(ChartColorType.CYAN,
                Arrays.asList("#199a95", "#34a6a1", "#50b2ae", "#6cbebb", "#88cbc8", "#a4d7d5", "#c0e3e2", "#dcefef"));
        map2.put(ChartColorType.BLUE,
                Arrays.asList("#13496c", "#2f5f7d", "#4c758f", "#688ba1", "#85a1b3", "#a2b7c5", "#becdd7", "#dbe3e8"));
        map2.put(ChartColorType.VIOLET,
                Arrays.asList("#4a2759", "#5f416d", "#755b81", "#8b7595", "#a18fa9", "#b7aabd", "#cdc4d1", "#e3dee6"));
        map2.put(ChartColorType.GRAY,
                Arrays.asList("#22262b", "#3c4044", "#575a5e", "#727578", "#8d8f91", "#a8a9ab", "#c3c4c5", "#dddedf"));
        MONOCHROME_MAP = Collections.unmodifiableMap(map2);
    }

    @Override
    public String getOptionsType() throws UnifyException {
        return "echarts";
    }

    @SuppressWarnings("rawtypes")
    @Override
    public JsonWriter getChartOptions(ChartDef chartDef, ChartDetails chartDetails, boolean sparkLine,
            int preferredHeight) throws UnifyException {
        JsonWriter jw = new JsonWriter();
        jw.beginObject(); // Main body start

        final ChartType chartType = chartDef.getType();
        final boolean isDynamicCategories = chartDetails.isDynamicCategories();
        final Map<String, AbstractSeries<?, ?>> series = isDynamicCategories && chartType.axisChart()
                ? chartDetails.getGroupingSeries(chartDef)
                : (chartDetails.getSeries(chartDetails.isWithSeriesInclusion() ? chartDetails.getSeriesInclusion()
                        : chartDef.getSeriesInclusion()));
        final ChartCategoryDataType categoryType = chartDetails.getCategoryType();

        // Colors
        if (chartDef.getPaletteType() != null) {
            jw.beginArray("color");
            if (chartDef.getPaletteType().monochrome()) {
                ChartColorType type = chartDef.isWithColor() ? ChartColorType.fromCode(chartDef.getColor()): ChartColorType.GRAY;
                for (String col : MONOCHROME_MAP.get(type)) {
                    jw.writeObject(col);
                }
            } else {
                for (String col : PALLETE_MAP.get(chartDef.getPaletteType())) {
                    jw.writeObject(col);
                }
            }
            jw.endArray();
        }

        // Title & Sub-title
        String title = !StringUtils.isBlank(chartDetails.getTitle()) ? chartDetails.getTitle() : chartDef.getTitle();
        String subTitle = !StringUtils.isBlank(chartDetails.getSubTitle()) ? chartDetails.getSubTitle()
                : chartDef.getSubTitle();
        if (!StringUtils.isBlank(title) || !StringUtils.isBlank(subTitle)) {
            jw.beginObject("title");

            if (!StringUtils.isBlank(title)) {
                jw.write("text", title);
                if (chartDetails.getTitleOffsetX() > 0) {
                    jw.write("left", chartDetails.getTitleOffsetX());
                }

                if (chartDetails.getTitleFontSize() > 0) {
                    jw.beginObject("textStyle");
                    jw.write("fontSize", chartDetails.getTitleFontSize());
                    jw.endObject();
                }
            }

            if (!StringUtils.isBlank(subTitle)) {
                jw.write("subtext", subTitle);

                jw.beginObject("subtextStyle");
                jw.write("color", "#a0a0a0");
                if (chartDetails.getSubTitleFontSize() > 0) {
                    jw.write("fontSize", chartDetails.getSubTitleFontSize());
                }

                jw.endObject();
            }

            jw.endObject();
        }

        jw.beginObject("tooltip");
        if (chartType.triggerAxisChart()) {
            jw.write("trigger", "axis");
        }
        jw.endObject();

        final Set<String> categoryInclusion = isDynamicCategories ? Collections.emptySet()
                : (chartDetails.isWithCategoryInclusion() ? chartDetails.getCategoryInclusion()
                        : chartDef.getCategoryInclusion());
        List<AbstractSeries<?, ?>> actseries = new ArrayList<AbstractSeries<?, ?>>(series.values());
        if (!DataUtils.isBlank(actseries)) {
            if (chartType.triggerAxisChart()) {
                // Legend
                jw.beginObject("legend");
                jw.beginArray("data");
                for (AbstractSeries<?, ?> _series : actseries) {
                    jw.writeObject(_series.getName());
                }
                jw.endArray();
                jw.endObject();

                // Grid
                jw.beginObject("grid");
                jw.write("top", 80);
                jw.endObject();

                // Category
                jw.beginArray(chartType.isHorizontal() ? "yAxis" : "xAxis");
                for (AbstractSeries<?, ?> _series : actseries) {
                    _series.setCategoryInclusion(categoryInclusion);
                    jw.beginObject();
                    jw.write("type",categoryType.isDate() ? "time": "category");
                    if (!chartType.isHorizontal()) {
                        jw.write("position", "bottom");
                    }

                    if (chartType.isArea()) {
                        jw.write("boundaryGap", false);
                    }

                    jw.beginArray("data");
                    for (Object cat : _series.getXList()) {
                        jw.writeObject(cat);
                    }

                    jw.endArray();

                    jw.endObject();
                }

                jw.endArray();

                jw.beginObject(chartType.isHorizontal() ? "xAxis" : "yAxis");
                jw.write("type", "value");
                jw.endObject();

                // Series
                jw.beginArray("series");
                for (AbstractSeries<?, ?> _series : actseries) {
                    jw.beginObject();
                    jw.write("name", _series.getName());
                    jw.write("type", chartType.isArea() ? "line" : chartType.optionsType());
                    jw.write("smooth", chartDef.isSmooth());
                    jw.write("stack", "total");
                    jw.write("showSymbol", false);

                    jw.beginArray("data");
                    for (Number num : _series.getYList()) {
                        jw.writeObject(num);
                    }

                    jw.endArray();

                    if (chartType.isArea()) {
                        jw.beginObject("areaStyle");
                        jw.endObject();
                    }

                    jw.endObject();
                }

                jw.endArray();
            } else {
                AbstractSeries<?, ?> pseries = actseries.get(0);
                pseries.setCategoryInclusion(categoryInclusion);

                if (chartType.isCircularChart()) {
                    // Legend
                    jw.beginObject("legend");
                    jw.write("orient", "vertical");
                    jw.write("top", "middle");
                    jw.write("left", 10);
                    jw.endObject();

                    // Series
                    jw.beginArray("series");

                    jw.beginObject();
                    jw.write("name", pseries.getName());
                    jw.write("type", ChartType.PIE.optionsType());
                    if (chartType.isPieChart()) {
                        jw.write("radius", "55%");
                    } else {
                        jw.beginArray("radius");
                        jw.writeObject("40%");
                        jw.writeObject("70%");
                        jw.endArray();
                    }
                    jw.write("top", 40);

                    jw.beginArray("data");
                    for (AbstractSeriesData _data : pseries.getDataList()) {
                        jw.beginObject();
                        jw.write("name", _data.resolveX(_data.getX()));
                        jw.write("value", _data.getY());
                        jw.endObject();
                    }
                    jw.endArray();

                    jw.endObject();

                    jw.endArray();
                } else if (chartType.isPolarChart()) {
                    // Polar
                    jw.beginObject("polar");
                    jw.beginArray("radius");
                    jw.writeObject(30);
                    jw.writeObject("80%");
                    jw.endArray();
                    jw.endObject();

                    // Radius
                    jw.beginObject("radiusAxis");
                    jw.endObject();
                    
                    // Angle
                    jw.beginObject("angleAxis");
                    jw.write("type", "category");
                    jw.write("startAngle", 75);
                    jw.beginArray("data");
                    for (Object cat : pseries.getXList()) {
                        jw.writeObject(cat);
                    }

                    jw.endArray();
                    jw.endObject();

                    // Series
                    jw.beginArray("series");
                    jw.beginObject();
                    jw.write("name", pseries.getName());
                    jw.write("type", ChartType.BAR.optionsType());
                    jw.write("coordinateSystem", "polar");

                    jw.beginArray("data");
                    for (Number num : pseries.getYList()) {
                        jw.writeObject(num);
                    }
                    jw.endArray();

                    jw.endObject();
                    jw.endArray();
                } else if (chartType.isRadarChart()) {
                    // Legend
                    jw.beginObject("legend");
                    jw.beginArray("data");
                    for (AbstractSeries<?, ?> _series : actseries) {
                        jw.writeObject(_series.getName());
                    }
                    jw.endArray();
                    jw.endObject();

                    // Radar
                    jw.beginObject("radar");
                    jw.beginArray("indicator");
                    for (Object cat : pseries.getXList()) {
                        jw.beginObject();
                        jw.write("name", (String) cat);
                        jw.endObject();
                    }

                    jw.endArray();
                    jw.endObject();

                    // Series
                    jw.beginArray("series");

                    jw.beginObject();
                    jw.write("type", ChartType.RADAR.optionsType());

                    jw.beginArray("data");
                    for (AbstractSeries<?, ?> _series : actseries) {
                        jw.beginObject();
                        jw.write("name", _series.getName());
                        jw.beginArray("value");
                        for (Number num : _series.getYList()) {
                            jw.writeObject(num);
                        }
                        jw.endArray();
                        jw.endObject();
                    }
                    jw.endArray();

                    jw.endObject();

                    jw.endArray();
                }
            }
        }

        jw.endObject(); // Main body end
        return jw;
    }

}
