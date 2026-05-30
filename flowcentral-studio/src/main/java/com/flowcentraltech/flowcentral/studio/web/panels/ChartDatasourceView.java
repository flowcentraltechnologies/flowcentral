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

package com.flowcentraltech.flowcentral.studio.web.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.AbstractSeries;
import com.flowcentraltech.flowcentral.chart.data.AbstractSeries.AbstractSeriesData;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartDetailsProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartTableColumn;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.MapValueStore;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Chart datasource view.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartDatasourceView extends AbstractStudioEditorPage {

    private final ChartDataSourceDef chartDataSourceDef;

    private final Object baseId;

    private BeanListTable dataSetTable;

    private BeanListTable groupDataSetTable;

    private ChartDetails chartDetails;

    public ChartDatasourceView(AppletUtilities au, ChartDataSourceDef chartDataSourceDef, Object baseId,
            BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.chartDataSourceDef = chartDataSourceDef;
        this.baseId = baseId;
    }

    public ChartDataSourceDef getChartDataSourceDef() {
        return chartDataSourceDef;
    }

    public Object getBaseId() {
        return baseId;
    }

    public BeanListTable getDataSetTable() {
        return dataSetTable;
    }

    public BeanListTable getGroupDataSetTable() {
        return groupDataSetTable;
    }

    public ChartDetails getChartDetails() {
        return chartDetails;
    }

    @SuppressWarnings("rawtypes")
    public void reloadContent() throws UnifyException {
        final ChartDetailsProvider detailsProvider = au().getComponent(ChartDetailsProvider.class,
                ChartModuleNameConstants.CHARTDATASOURCE_PROVIDER);
        chartDetails = detailsProvider.provide(chartDataSourceDef);
        dataSetTable = null;
        groupDataSetTable = null;
        if (chartDetails.isWithSeries()) {
            final Map<String, AbstractSeries<?, ?>> series = chartDetails.getSeries();
            final EntityDef.Builder edb = EntityDef.newBuilder(ConfigType.CUSTOM,
                    "com.flowcentraltech.flowcentral.preview.Az", "Preview", null, null, null, null, null, false, false,
                    false, false, false, "application.preview", "Preview", 1L, 1L);
            edb.addFieldDef("application.text", "application.text", EntityFieldDataType.STRING, EntityFieldType.STATIC,
                    "series", "Series");
            final Set<String> used = new HashSet<String>();
            for (AbstractSeries<?, ?> _series : series.values()) {
                for (AbstractSeriesData data : _series.getDataList()) {
                    final String cat = data.resolveX(data.getX());
                    if (!used.contains(cat)) {
                        if (_series.getDataType().isInteger()) {
                            edb.addFieldDef("application.integer", "application.integer", EntityFieldDataType.INTEGER,
                                    EntityFieldType.CUSTOM, cat, cat);
                        } else {
                            edb.addFieldDef("application.decimal", "application.decimal", EntityFieldDataType.DECIMAL,
                                    EntityFieldType.CUSTOM, cat, cat);
                        }
                        used.add(cat);
                    }
                }
            }

            final EntityDef entityDef = edb.build(au());
            final TableDef.Builder tdb = TableDef.newBuilder(entityDef, "Preview", false, false, "studio.dataSetTable",
                    "Priview Table", 0L, 0L);
            tdb.sortHistory(4);
            tdb.itemsPerPage(-1);

            final List<ValueStore> list = new ArrayList<ValueStore>();
            for (Map.Entry<String, AbstractSeries<?, ?>> entry : chartDetails.getSeries().entrySet()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("series", entry.getKey());
                for (AbstractSeriesData data : entry.getValue().getDataList()) {
                    map.put(data.resolveX(data.getX()), data.getY());
                }

                list.add(new MapValueStore(map));
            }

            dataSetTable = new BeanListTable(au(), tdb.build(au()));
            dataSetTable.setSourceObjectClearSelected(list);
        }

        if (chartDetails.isWithTableSeries()) {
            final EntityDef.Builder edb = EntityDef.newBuilder(ConfigType.CUSTOM,
                    "com.flowcentraltech.flowcentral.preview.Az", "Preview", null, null, null, null, null, false, false,
                    false, false, false, "application.preview", "Preview", 1L, 1L);
            final ChartTableColumn[] headers = chartDetails.getTableHeaders();
            for (ChartTableColumn col : headers) {
                if (col.getType().isInteger()) {
                    edb.addFieldDef("application.integer", "application.integer", col.getType(), EntityFieldType.CUSTOM,
                            col.getFieldName(), col.getLabel());
                } else if (col.getType().isDecimal()) {
                    edb.addFieldDef("application.decimal", "application.decimal", col.getType(), EntityFieldType.CUSTOM,
                            col.getFieldName(), col.getLabel());
                } else {
                    edb.addFieldDef("application.text", "application.text", col.getType(), EntityFieldType.CUSTOM,
                            col.getFieldName(), col.getLabel());
                }
            }

            final EntityDef entityDef = edb.build(au());
            final TableDef.Builder tdb = TableDef.newBuilder(entityDef, "Preview", false, false, "studio.dataSetTable",
                    "Priview Table", 0L, 0L);
            tdb.sortHistory(4);
            tdb.itemsPerPage(-1);

            final List<ValueStore> list = new ArrayList<ValueStore>();
            for (Object[] row : chartDetails.getTableSeries()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < headers.length; i++) {
                    map.put(headers[i].getFieldName(), row[i]);
                }

                list.add(new MapValueStore(map));
            }

            groupDataSetTable = new BeanListTable(au(), tdb.build(au()));
            groupDataSetTable.setSourceObjectClearSelected(list);
        }
    }
}
