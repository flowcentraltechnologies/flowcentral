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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.data.CDSnapshot;
import com.flowcentraltech.flowcentral.chart.data.CDSnapshotCategory;
import com.flowcentraltech.flowcentral.chart.data.CDSnapshotSeries;
import com.flowcentraltech.flowcentral.chart.data.ChartViewOption;
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

    final ChartModuleService cms;

    final String chartDatasourceName;

    private final Object baseId;

    private BeanListTable snapshotTable;

    public ChartDatasourceView(AppletUtilities au, ChartModuleService cms, String chartDatasourceName, Object baseId,
            BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.cms = cms;
        this.chartDatasourceName = chartDatasourceName;
        this.baseId = baseId;
    }

    public Object getBaseId() {
        return baseId;
    }

    public BeanListTable getSnapshotTable() {
        return snapshotTable;
    }

    public boolean isWithSnapshot() {
        return snapshotTable != null;
    }

    public void reloadContent() throws UnifyException {
        final CDSnapshot cdSnapshot = cms.getChartDatasourceSnapshot(chartDatasourceName, ChartViewOption.DEFAULT);
        final CDSnapshotCategory[] categories = cdSnapshot.getCategories();
        snapshotTable = null;
        if (categories != null && categories.length > 0) {
            // Structure
            final EntityDef.Builder edb = EntityDef.newBuilder(ConfigType.CUSTOM,
                    "com.flowcentraltech.flowcentral.preview.Az", "Preview", null, null, null, null, null, false, false,
                    false, false, false, "application.preview", "Preview", 1L, 1L);
            edb.addFieldDef("application.text", "application.text", EntityFieldDataType.STRING, EntityFieldType.STATIC,
                    "category", "Category");
            final CDSnapshotCategory category = categories[0];
            if (category.getSeries() != null && category.getSeries().length > 0) {
                for (CDSnapshotSeries series : category.getSeries()) {
                    final int grouping = series.getGrouping();
                    if (grouping == 0) {
                        edb.addFieldDef("application.decimal", "application.decimal", EntityFieldDataType.DECIMAL,
                                EntityFieldType.CUSTOM, series.getNm(),
                                series.getLbl() != null ? series.getLbl() : series.getNm());
                    } else if (grouping == 1) {
                        edb.addFieldDef("application.text", "application.text", EntityFieldDataType.STRING,
                                EntityFieldType.CUSTOM, series.getNm(),
                                series.getLbl() != null ? series.getLbl() : series.getNm());
                    } else {
                        edb.addFieldDef("application.datetime", "application.datetime", EntityFieldDataType.TIMESTAMP,
                                EntityFieldType.CUSTOM, series.getNm(),
                                series.getLbl() != null ? series.getLbl() : series.getNm());
                    }
                }
            }

            final EntityDef entityDef = edb.build(au());
            final TableDef.Builder tdb = TableDef.newBuilder(entityDef, "Preview", false, false, "studio.snapshotTable",
                    "Priview Table", 0L, 0L);
            tdb.itemsPerPage(-1);

            // Data
            final List<ValueStore> list = new ArrayList<ValueStore>();
            for (CDSnapshotCategory _category : categories) {
                if (_category.getSeries() != null && _category.getSeries().length > 0) {
                    if (_category.getSeries()[0] != null) {
                        final String[] vals = _category.getSeries()[0].getVals();
                        final int len = vals != null && vals.length > 0 ? vals.length : 0;
                        for (int i = 0; i < len; i++) {
                            final Map<String, Object> map = new HashMap<String, Object>();
                            map.put("category", _category.getLbl() != null ? _category.getLbl() : _category.getNm());

                            for (CDSnapshotSeries series : _category.getSeries()) {
                                final int grouping = series.getGrouping();
                                final String val = series.getVals()[i];
                                Object _val = null;
                                if (val != null) {
                                    if (grouping == 0) {
                                        _val = new BigDecimal(val);
                                    } else if (grouping == 1) {
                                        _val = val;
                                    } else {
                                        _val = new Date(Long.parseLong(val));
                                    }
                                }

                                map.put(series.getNm(), _val);
                            }

                            list.add(new MapValueStore(map));
                        }
                    }
                }
            }

            snapshotTable = new BeanListTable(au(), tdb.build(au()));
            snapshotTable.setSourceObjectClearSelected(list);
        }
    }
}
