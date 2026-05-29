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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.chart.business.AbstractChartOptionsProvider;
import com.flowcentraltech.flowcentral.chart.data.AbstractSeries;
import com.flowcentraltech.flowcentral.chart.data.AbstractSeries.AbstractSeriesData;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.echarts.constants.EChartsNameConstants;
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
        jw.write("trigger", "axis");
        jw.endObject();
     
        final Set<String> categoryInclusion = isDynamicCategories ? Collections.emptySet()
                : (chartDetails.isWithCategoryInclusion() ? chartDetails.getCategoryInclusion()
                        : chartDef.getCategoryInclusion());
        List<AbstractSeries<?, ?>> actseries = new ArrayList<AbstractSeries<?, ?>>(series.values());
        if (!DataUtils.isBlank(actseries)) {
            if (chartType.axisChart()) {
                // Legend
                jw.beginObject("legend");
                jw.beginArray("data");
                for (AbstractSeries<?, ?> _series : actseries) {
                    jw.writeObject(_series.getName());
                }
                jw.endArray();
                jw.endObject();
                
                // Category
                jw.beginArray(chartType.isHorizontal() ? "yAxis" : "xAxis");
                for (AbstractSeries<?, ?> _series : actseries) {
                    _series.setCategoryInclusion(categoryInclusion);
                    jw.beginObject();
                    jw.write("type", "category");
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
                    jw.write("type", chartType.isArea() ? "line": chartType.optionsType());
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
                    
                    jw.endObject();

                    // Series
                    jw.beginArray("series");
                    
                    jw.beginObject();
                    jw.write("name", pseries.getName());
                    jw.write("type", ChartType.PIE.optionsType());
                    if (chartType.isPieChart()) {
                        jw.write("radius", "60%");
                    } else {
                        jw.beginArray("radius");
                        jw.writeObject("40%");
                        jw.writeObject("70%");
                        jw.endArray();
                    }

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
