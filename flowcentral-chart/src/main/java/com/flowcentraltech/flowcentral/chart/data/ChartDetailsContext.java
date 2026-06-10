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
import java.util.Date;
import java.util.HashMap;
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

    public ChartCategory newChartCategory(String catName) {
        final CatInfo catinfo = catmap.get(catName);
        if (catinfo == null) {
            throw new IllegalArgumentException("Category with name [" + catName + "] is unknown.");
        }

        final CDSnapshotCategory cat = catinfo.getCat();
        return new ChartCategory(cat.getCat(), cat.getLbl(), cat.getCat());
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
                vals[i] = new Date(Long.parseLong(svals[i]));
            }
        } else {
            for (int i = 0; i < vals.length; i++) {
                vals[i] = svals[i];
            }
        }

        return new ChartSeries(type, series.getNm(), series.getLbl(), series.getFld(), series.getGrouping(), vals,
                series.isTime());
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
