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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.data.ChartDetailsProvider;
import com.tcdng.unify.core.UnifyException;

/**
 * Chart datasource view.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartDatasourceView extends AbstractStudioEditorPage {

    private final ChartDataSourceDef chartDataSourceDef;

    private final Object baseId;

    private EntityTable dataSetTable;

    public ChartDatasourceView(AppletUtilities au, ChartDataSourceDef chartDataSourceDef, Object baseId, BreadCrumbs breadCrumbs) {
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

    public EntityTable getDataSetTable() {
        return dataSetTable;
    }

    public void reloadContent() throws UnifyException {
        final EntityDef entityDef = chartDataSourceDef.getEntityDef();
        TableDef.Builder tdb = TableDef.newBuilder(entityDef, "Preview", false, false, "studio.dataSetTable",
                "Priview Table", 0L, 0L);
        tdb.sortHistory(4);
        tdb.itemsPerPage(-1);
        
        ChartDetailsProvider detailsProvider = au().getComponent(ChartDetailsProvider.class, ChartModuleNameConstants.CHARTDATASOURCE_PROVIDER);
        ChartDetails chartDetails = detailsProvider.provide(chartDataSourceDef);
//        if (design != null && design.getColumns() != null) {
//            for (TableColumn tableColumn : design.getColumns()) {
//                String renderer = InputWidgetUtils.constructRenderer(au.getWidgetTypeDef(tableColumn.getWidget()),
//                        entityDef.getFieldDef(tableColumn.getFldNm()));
//                OrderType order = OrderType.fromCode(tableColumn.getOrder());
//                tdb.addColumnDef(tableColumn.getLabel(), tableColumn.getFldNm(), renderer, tableColumn.getLink(),
//                        order, tableColumn.getWidth(), tableColumn.isSwitchOnChange(), tableColumn.isHiddenOnNull(),
//                        tableColumn.isHidden(), tableColumn.isDisabled(), tableColumn.isEditable(),
//                        tableColumn.isSort(), tableColumn.isSummary());
//            }
//        }

        dataSetTable = new EntityTable(au(), tdb.build(au().enumProvider()), null);
    }
}
