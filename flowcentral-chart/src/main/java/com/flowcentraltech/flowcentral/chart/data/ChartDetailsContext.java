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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.constant.TimeSeriesType;

/**
 * Chart details context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartDetailsContext {

    private static final Map<TimeSeriesType, String[]> categoryFills;

    static {
        Map<TimeSeriesType, String[]> map = new HashMap<TimeSeriesType, String[]>();
        categoryFills = Collections.unmodifiableMap(map);
    }

    private final CDSnapshot cdSnapshot;

    private final Long chartDatasourceId;

    private final Map<String, CatInfo> catmap;

    private final List<String> groupingNames;

    private final TimeSeriesType timeSeriesType;

    public ChartDetailsContext(CDSnapshot cdSnapshot, Long chartDatasourceId) {
        this.cdSnapshot = cdSnapshot;
        this.chartDatasourceId = chartDatasourceId;

        this.catmap = new HashMap<String, CatInfo>();
        for (CDSnapshotCategory cat : cdSnapshot.getCategories()) {
            Map<String, CDSnapshotSeries> series = new HashMap<String, CDSnapshotSeries>();
            for (CDSnapshotSeries _series : cat.getSeries()) {
                series.put(_series.getNm(), _series);
            }

            this.catmap.put(cat.getNm(), new CatInfo(cat, series));
        }

        this.groupingNames = Arrays.asList(cdSnapshot.getGroupingNames());

        timeSeriesType = TimeSeriesType.fromCode(cdSnapshot.getTimeSeries());
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
        return timeSeriesType != null;
    }

    public boolean isNumericMerged() {
        return timeSeriesType != null && timeSeriesType.numericMerged();
    }

    public boolean isFill() {
        return timeSeriesType != null && timeSeriesType.fill();
    }

    public ChartCategory newChartCategory(String catName) {
        final CatInfo catinfo = catmap.get(catName);
        if (catinfo == null) {
            throw new IllegalArgumentException("Category with name [" + catName + "] is unknown.");
        }

        final CDSnapshotCategory cat = catinfo.getCat();
        return new ChartCategory(cat.getNm(), cat.getLbl(), cat.getNm());
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
        final Object[] vals = convertVals(type, series.getVals());
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

    public ChartSeries[] newChartSeriesAcrossFirst(List<String> catNames, List<String> seriesNames) {
        final int len = seriesNames.size();
        ChartSeries[] series = new ChartSeries[len];
        for (int i = 0; i < len; i++) {
            series[i] = newChartSeriesAcrossFirst(catNames, seriesNames.get(i));
        }

        return series;
    }

    public ChartSeries newChartSeriesAcrossFirst(List<String> catNames, String seriesName) {
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

            vals[i] = convertVal(_type, _series.getVals()[0]); // Zero Index (first)
        }

        if (series != null) {
            return new ChartSeries(EntityFieldDataType.fromCode(series.getTy()), series.getNm(), series.getLbl(),
                    series.getFld(), series.getGrouping(), vals, type.isDatetime());
        }

        return ChartSeries.BLANK;
    }

    public ChartSeries[] newChartSeriesAcrossAll(List<String> catNames, List<String> seriesNames) {
        List<ChartSeries> series = new ArrayList<ChartSeries>();
        for (String seriesName : seriesNames) {
            for (ChartSeries _series : newChartSeriesAcrossAll(catNames, seriesName)) {
                series.add(_series);
            }
        }

        return series.toArray(new ChartSeries[series.size()]);
    }

    public ChartSeries[] newChartSeriesAcrossAll(List<String> catNames, String seriesName) {
        final Map<String, SeriesInfo> map = new LinkedHashMap<String, SeriesInfo>();
        final int clen = catNames.size();
        CDSnapshotSeries _series = null;
        EntityFieldDataType _type = null;
        for (int i = 0; i < clen; i++) {
            final String catName = catNames.get(i);
            final CatInfo catinfo = catmap.get(catName);
            if (catinfo == null) {
                throw new IllegalArgumentException("Category with name [" + catName + "] is unknown.");
            }

            _series = catinfo.getSeries(seriesName);
            _type = EntityFieldDataType.fromCode(_series.getTy());

            final String[] svals = _series.getVals();
            for (int d = 0; d < svals.length; d++) {
                StringBuilder ksb = new StringBuilder(_series.getNm());
                StringBuilder lsb = new StringBuilder(_series.getLbl());
                for (String groupName : groupingNames) {
                    final String gval = catinfo.getSeries(groupName).getVals()[d];
                    ksb.append(" - ").append(gval);
                    lsb.append(" - ").append(gval);
                }

                final String key = ksb.toString();
                final String label = lsb.toString();
                SeriesInfo seriesinfo = map.get(key);
                if (seriesinfo == null) {
                    seriesinfo = new SeriesInfo(new Object[clen], label);
                    map.put(key, seriesinfo);
                }

                seriesinfo.getVals()[i] = convertVal(_type, svals[d]);
            }
        }

        final ChartSeries[] res = new ChartSeries[map.size()];
        int i = 0;
        for (Map.Entry<String, SeriesInfo> entry : map.entrySet()) {
            final SeriesInfo seriesinfo = entry.getValue();
            res[i] = new ChartSeries(_type, entry.getKey(), seriesinfo.getLabel(), _series.getFld(),
                    _series.getGrouping(), seriesinfo.getVals(), _type.isDatetime());
            i++;
        }

        return res;
    }

    public ChartAxisSet newChartAxisSetAcrossDatetime(List<String> catNames, List<String> seriesNames) {
        ChartSeries[] series = new ChartSeries[catNames.size() * seriesNames.size()];
        ChartCategory[] categories = null;
        int k = 0;
        for (String catName: catNames) {
            ChartAxisSet set = newChartAxisSetAcrossDatetime(catName, seriesNames, true, k == 0 );
            if (k == 0) {
                categories = set.getCategories();
            }
            
            for (ChartSeries _series: set.getSeries()) {
                series[k] = _series;
                k++;
            }
        }
        
        return new ChartAxisSet(categories, series);
    }

    public ChartAxisSet newChartAxisSetAcrossDatetime(String catName, List<String> seriesNames) {
        return newChartAxisSetAcrossDatetime(catName, seriesNames, false, true);
    }

    private ChartAxisSet newChartAxisSetAcrossDatetime(String catName, List<String> seriesNames, boolean byCategory, boolean getCat) {
        final CatInfo catinfo = catmap.get(catName);
        if (catinfo == null) {
            throw new IllegalArgumentException("Category with name [" + catName + "] is unknown.");
        }

        final CDSnapshotCategory cat = byCategory ? catinfo.getCat() : null;
        final CDSnapshotSeries _gseries = catinfo.getSeries("group0");
        final EntityFieldDataType _gtype = EntityFieldDataType.fromCode(_gseries.getTy());
        final String[] _gvals = _gseries.getVals();
        int[] _givals = null;
        final boolean fill = isFill();
        if (fill) {
            _givals = new int[_gvals.length];
            final boolean zeroOffset = timeSeriesType.zeroBased();
            for (int i = 0; i < _gvals.length; i++) {
                _givals[i] = Integer.parseInt(_gvals[i]) - (zeroOffset ? 0 : 1);
            }
        }

        final int flen = timeSeriesType.fillLength();
        final int slen = seriesNames.size();
        ChartSeries[] series = new ChartSeries[slen];
        for (int i = 0; i < slen; i++) {
            CDSnapshotSeries _series = catinfo.getSeries(seriesNames.get(i));
            final EntityFieldDataType _type = EntityFieldDataType.fromCode(_series.getTy());
            Object[] vals = convertVals(_type, _series.getVals());
            if (fill && vals.length < flen) {
                Object[] _vals = new Object[flen];
                for (int j = 0; j < vals.length; i++) {
                    _vals[_givals[j]] = vals[j];
                }

                vals = _vals;
            }

            series[i] = byCategory
                    ? new ChartSeries(_type, _series.getNm() + " - " + cat.getNm(),
                            _series.getLbl() + " - " + cat.getLbl(), _series.getFld(), _series.getGrouping(), vals,
                            _type.isDatetime())
                    : new ChartSeries(_type, _series.getNm(), _series.getLbl(), _series.getFld(), _series.getGrouping(),
                            vals, _type.isDatetime());
        }

        if (getCat) {
        Object[] vals = fill ? categoryFills.get(timeSeriesType) : convertVals(_gtype, _gvals);
        ChartCategory[] categories = new ChartCategory[vals.length];
        for (int c = 0; c < vals.length; c++) {
            final Object val = vals[c];
            final String name = String.valueOf(val);
            categories[c] = new ChartCategory(name, name, val);
        }

        return new ChartAxisSet(categories, series);
        }
        
        return new ChartAxisSet(null, series);
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

    private class SeriesInfo {

        private final Object[] vals;

        private final String label;

        public SeriesInfo(Object[] vals, String label) {
            this.vals = vals;
            this.label = label;
        }

        public Object[] getVals() {
            return vals;
        }

        public String getLabel() {
            return label;
        }

    }

    private Object[] convertVals(EntityFieldDataType type, String[] svals) {
        final Object[] vals = new Object[svals.length];
        if (type.isInteger()) {
            for (int i = 0; i < vals.length; i++) {
                if (svals[i] != null) {
                    vals[i] = Integer.parseInt(svals[i]);
                }
            }
        } else if (type.isDecimal()) {
            for (int i = 0; i < vals.length; i++) {
                if (svals[i] != null) {
                    vals[i] = new BigDecimal(svals[i]);
                }
            }
        } else if (type.isDatetime()) {
            for (int i = 0; i < vals.length; i++) {
                if (svals[i] != null) {
                    vals[i] = Long.parseLong(svals[i]);
                }
            }
        } else {
            for (int i = 0; i < vals.length; i++) {
                if (svals[i] != null) {
                    vals[i] = svals[i];
                }
            }
        }

        return vals;
    }

    private Object convertVal(EntityFieldDataType type, String sval) {
        if (sval != null) {
            if (type.isInteger()) {
                return Integer.parseInt(sval);
            } else if (type.isDecimal()) {
                return new BigDecimal(sval);
            } else if (type.isDatetime()) {
                return Long.parseLong(sval);
            } else {
                return sval;
            }
        }

        return null;
    }

}
