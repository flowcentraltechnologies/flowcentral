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
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.entities.AppTableColumn;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.chart.data.ChartDataSourceDef;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.flowcentraltech.flowcentral.studio.web.widgets.TableEditor;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Query;

/**
 * Chart datasource view.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartDatasourceView extends AbstractStudioEditorPage {

    private final ChartDataSourceDef chartDataSourceDef;

    private final Object baseId;

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

    public void newEditor() throws UnifyException {
        TableEditor.Builder teb = TableEditor.newBuilder(au(), null);
        for (AppTableColumn appTableColumn : au().environment()
                .findAll(Query.of(AppTableColumn.class).addEquals("appTableId", baseId).addOrder("id"))) {
            teb.addColumn(appTableColumn.getField(), appTableColumn.getRenderWidget(), appTableColumn.getLabel(),
                    appTableColumn.getLinkAct(), appTableColumn.getSymbol(), appTableColumn.getOrder(),
                    appTableColumn.getWidthRatio(), appTableColumn.isSwitchOnChange(), appTableColumn.isHiddenOnNull(),
                    appTableColumn.isHidden(), appTableColumn.isDisabled(), appTableColumn.isEditable(),
                    appTableColumn.isSortable(), appTableColumn.isSummary());
        }

        TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
        tsdb.addTabDef("editor", au().resolveSessionMessage("$m{studio.apptable.form.design}"), "!fc-tableeditor",
                RendererType.SIMPLE_WIDGET);
        tsdb.addTabDef("preview", au().resolveSessionMessage("$m{studio.apptable.form.preview}"),
                "fc-tablepreviewpanel", RendererType.STANDALONE_PANEL);
    }
}
