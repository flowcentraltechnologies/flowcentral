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

package com.flowcentraltech.flowcentral.chart.data;

/**
 * Chart details.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartDetails {

    public static final ChartDetails BLANK = new ChartDetails();
    
    private ChartDef chartDef;

    private ChartSeries[] series;

    private ChartCategory[] categories;

    public ChartDetails(ChartDef chartDef, ChartSeries[] series, ChartCategory[] categories) {
        this.chartDef = chartDef;
        this.series = series;
        this.categories = categories;
    }

    private ChartDetails() {
        
    }
    
    public ChartDef getChartDef() {
        return chartDef;
    }

    public ChartSeries getSeries(int index) {
        return series[index];
    }

    public int getSeriesCount() {
        return series != null ? series.length : 0;
    }

    public int getDataDepth() {
        return series != null ? series[0].getDataDepth() : 0;
    }
    
    public Object getSeriesVal(int seriesIndex, int dataIndex) {
        return series[seriesIndex].getVal(dataIndex);
    }

    public ChartCategory getCategory(int index) {
        return categories[index];
    }

    public int getCategoryCount() {
        return categories != null ? categories.length : 0;
    }
    
    public Object getCategoryVal(int index) {
        return categories[index].getVal();
    }
    
    public boolean isPresent() {
        return chartDef != null;
    }
    
    public boolean isWithSeries () {
        return series != null && series.length > 0;
    }

    public boolean isWithCategories () {
        return categories != null && categories.length > 0;
    }
}
