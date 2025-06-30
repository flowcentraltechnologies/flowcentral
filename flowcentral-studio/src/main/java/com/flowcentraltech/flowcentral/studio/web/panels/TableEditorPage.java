/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppTableColumn;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetEventHandler;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.flowcentraltech.flowcentral.studio.web.widgets.TableEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.TableEditor.TableColumn;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Query;

/**
 * Table editor page.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class TableEditorPage extends AbstractStudioEditorPage implements TabSheetEventHandler {

    private static final int DESIGN_INDEX = 0;

    private static final int PREVIEW_INDEX = 1;

    private final EntityDef entityDef;

    private final Object baseId;

    private TabSheet tabSheet;

    private TableEditor tableEditor;

    private TablePreview tablePreview;

    public TableEditorPage(AppletUtilities au, EntityDef entityDef, Object baseId, BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.entityDef = entityDef;
        this.baseId = baseId;
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public TableEditor getTableEditor() {
        return tableEditor;
    }

    public TablePreview getTablePreview() {
        return tablePreview;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public Object getBaseId() {
        return baseId;
    }

    @Override
    public void onChoose(TabSheetItem tabSheetItem) throws UnifyException {
        switch (tabSheetItem.getIndex()) {
            case DESIGN_INDEX:
                break;
            case PREVIEW_INDEX:
                tablePreview.reload();
                break;
            default:
        }
    }

    public void commitDesign() throws UnifyException {
        AppTable appTable = au().environment().find(AppTable.class, baseId);
        List<AppTableColumn> columnList = Collections.emptyList();
        if (tableEditor.getDesign() != null && tableEditor.getDesign().getColumns() != null) {
            columnList = new ArrayList<AppTableColumn>();
            for (TableColumn tableColumn : tableEditor.getDesign().getColumns()) {
                AppTableColumn appTableColumn = new AppTableColumn();
                appTableColumn.setField(tableColumn.getFldNm());
                appTableColumn.setRenderWidget(tableColumn.getWidget());
                appTableColumn.setLabel(tableColumn.getLabel());
                appTableColumn.setLinkAct(tableColumn.getLink());
                appTableColumn.setSymbol(tableColumn.getSymbol());
                appTableColumn.setOrder(tableColumn.getOrder());
                appTableColumn.setWidthRatio(tableColumn.getWidth());
                appTableColumn.setSwitchOnChange(tableColumn.isSwitchOnChange());
                appTableColumn.setEditable(tableColumn.isEditable());
                appTableColumn.setHiddenOnNull(tableColumn.isHiddenOnNull());
                appTableColumn.setHidden(tableColumn.isHidden());
                appTableColumn.setDisabled(tableColumn.isDisabled());
                appTableColumn.setSortable(tableColumn.isSort());
                appTableColumn.setSummary(tableColumn.isSummary());
                columnList.add(appTableColumn);
            }
        }

        appTable.setColumnList(columnList);
        au().environment().updateByIdVersion(appTable);
    }

    public void newEditor() throws UnifyException {
        TableEditor.Builder teb = TableEditor.newBuilder(entityDef);
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
        tableEditor = teb.build();
        tablePreview = new TablePreview(au(), tableEditor);
        final String appletName = null;
        tabSheet = new TabSheet(tsdb.build(),
                Arrays.asList(new TabSheetItem("tableEditor", appletName, tableEditor, DESIGN_INDEX, true),
                        new TabSheetItem("tablePreview", appletName, tablePreview, PREVIEW_INDEX, true)));
        tabSheet.setEventHandler(this);
    }
}
