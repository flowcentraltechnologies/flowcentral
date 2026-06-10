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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;

/**
 * Chart details context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartDetailsContext {

    private final CDSnapshot cdSnapshot;

    private final Long chartDatasourceId;

    private final Map<String, CatInfo> catmap;

    private final List<String> groupingNames;

    public ChartDetailsContext(CDSnapshot cdSnapshot, Long chartDatasourceId) {
        this.cdSnapshot = cdSnapshot;
        this.chartDatasourceId = chartDatasourceId;

        this.catmap = new HashMap<String, CatInfo>();
        for (CDSnapshotCategory cat : cdSnapshot.getCategories()) {
            Map<String, CDSnapshotSeries> series = new HashMap<String, CDSnapshotSeries>();
            for (CDSnapshotSeries _series : cat.getSeries()) {
                series.put(_series.getNm(), _series);
            }

            this.catmap.put(cat.getCat(), new CatInfo(cat, series));
        }

        this.groupingNames = Arrays.asList(cdSnapshot.getGroupingNames());
    }

    public Long getChartDatasourceId() {
        return chartDatasourceId;
    }

    public String getViewOption() {
        return cdSnapshot.getView();
    }

    public Date getSnapshotExpiresOn() {
        return cdSnapshot.getExpiresOn();
    }

    public List<String> getGroupingNames() {
        return groupingNames;
    }

    public boolean isWithGrouping() {
        return cdSnapshot.getGroupingStart() > 0;
    }

    public boolean isDatetimeGrouping() {
        return cdSnapshot.isDatetimeGrouping();
    }

    public boolean isNumericMerged() {
        return cdSnapshot.isNumericMerged();
    }

    public ChartCategory newChartCategory(String catName) {
        final CatInfo catinfo = catmap.get(catName);
        if (catinfo == null) {
            throw new IllegalArgumentException("Category with name [" + catName + "] is unknown.");
        }

        final CDSnapshotCategory cat = catinfo.getCat();
        return new ChartCategory(cat.getCat(), cat.getLbl(), cat.getCat());
    }

    public ChartCategory[] newChartCategories(List<String> catNames) {
        final int len = catNames.size();
        ChartCategory[] categories = new ChartCategory[len];
        for (int i = 0; i < len; i++) {
            categories[i] = newChartCategory(catNames.get(i));
        }

        return categories;
    }

    public ChartSeries newChartSeries(String catName, String seriesName) {
        final CatInfo catinfo = catmap.get(catName);
        if (catinfo == null) {
            throw new IllegalArgumentException("Category with name [" + catName + "] is unknown.");
        }

        final CDSnapshotSeries series = catinfo.getSeries(seriesName);
        final EntityFieldDataType type = EntityFieldDataType.fromCode(series.getTy());
        final String[] svals = series.getVals();
        final Object[] vals = new Object[svals.length];
        if (type.isInteger()) {
            for (int i = 0; i < vals.length; i++) {
                vals[i] = Integer.parseInt(svals[i]);
            }
        } else if (type.isDecimal()) {
            for (int i = 0; i < vals.length; i++) {
                vals[i] = new BigDecimal(svals[i]);
            }
        } else if (type.isDatetime()) {
            for (int i = 0; i < vals.length; i++) {
                vals[i] = Long.parseLong(svals[i]);
            }
        } else {
            for (int i = 0; i < vals.length; i++) {
                vals[i] = svals[i];
            }
        }

        return new ChartSeries(type, series.getNm(), series.getLbl(), series.getFld(), series.getGrouping(), vals,
                type.isDatetime());
    }

    public ChartSeries[] newChartSeries(String catName, List<String> seriesNames) {
        final int len = seriesNames.size();
        ChartSeries[] series = new ChartSeries[len];
        for (int i = 0; i < len; i++) {
            series[i] = newChartSeries(catName, seriesNames.get(i));
        }

        return series;
    }

    public ChartSeries[] newChartSeriesAcross(List<String> catNames, List<String> seriesNames) {
        final int len = seriesNames.size();
        ChartSeries[] series = new ChartSeries[len];
        for (int i = 0; i < len; i++) {
            series[i] = newChartSeriesAcross(catNames, seriesNames.get(i));
        }

        return series;
    }

    public ChartSeries newChartSeriesAcross(List<String> catNames, String seriesName) {
        CDSnapshotSeries series = null;
        EntityFieldDataType type = null;
        final int len = catNames.size();
        final Object[] vals = new Object[len];
        for (int i = 0; i < len; i++) {
            final String catName = catNames.get(i);
            final CatInfo catinfo = catmap.get(catName);
            if (catinfo == null) {
                throw new IllegalArgumentException("Category with name [" + catName + "] is unknown.");
            }

            CDSnapshotSeries _series = catinfo.getSeries(seriesName);
            EntityFieldDataType _type = EntityFieldDataType.fromCode(_series.getTy());
            if (series == null) {
                series = _series;
                type = _type;
            }

            String sval = _series.getVals()[0]; // Zero Index
            if (_type.isInteger()) {
                vals[i] = Integer.parseInt(sval);
            } else if (_type.isDecimal()) {
                vals[i] = new BigDecimal(sval);
            } else if (_type.isDatetime()) {
                vals[i] = Long.parseLong(sval);
            } else {
                vals[i] = sval;
            }
        }

        if (series != null) {
            return new ChartSeries(EntityFieldDataType.fromCode(series.getTy()), series.getNm(), series.getLbl(),
                    series.getFld(), series.getGrouping(), vals, type.isDatetime());
        }

        return ChartSeries.BLANK;
    }

    private class CatInfo {

        private final CDSnapshotCategory cat;

        private final Map<String, CDSnapshotSeries> series;

        public CatInfo(CDSnapshotCategory cat, Map<String, CDSnapshotSeries> series) {
            this.cat = cat;
            this.series = series;
        }

        public CDSnapshotCategory getCat() {
            return cat;
        }

        public CDSnapshotSeries getSeries(String seriesName) {
            CDSnapshotSeries _series = series.get(seriesName);
            if (_series == null) {
                throw new IllegalArgumentException("Series with name [" + seriesName + "] is unknown.");
            }

            return _series;
        }
    }
}
