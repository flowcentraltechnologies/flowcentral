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

import java.util.Arrays;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetEventHandler;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.tcdng.unify.core.UnifyException;

/**
 * Applet editor page.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AppletEditorPage extends AbstractStudioEditorPage implements TabSheetEventHandler {

    private static final int TABLE_PREVIEW_INDEX = 0;

    private static final int FORM_PREVIEW_INDEX = 1;

    private final String tableName;

    private final String formName;

    private final Object baseId;

    private TabSheet tabSheet;

    private TablePreview tablePreview;

    private FormPreview formPreview;

    public AppletEditorPage(AppletUtilities au, String tableName, String formName, Object baseId,
            BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.tableName = tableName;
        this.formName = formName;
        this.baseId = baseId;
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public TablePreview getTablePreview() {
        return tablePreview;
    }

    public FormPreview getFormPreview() {
        return formPreview;
    }

    public Object getBaseId() {
        return baseId;
    }

    @Override
    public void onChoose(TabSheetItem tabSheetItem) throws UnifyException {
        switch (tabSheetItem.getIndex()) {
            case FORM_PREVIEW_INDEX:
                formPreview.reload();
                break;
            case TABLE_PREVIEW_INDEX:
                tablePreview.reload();
                break;
            default:
        }
    }

    public void commitDesign() throws UnifyException {

    }

    public void newEditor() throws UnifyException {
        TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
        tsdb.addTabDef("tablePreview", au().resolveSessionMessage("$m{studio.appapplet.table.tablepreview}"),
                "fc-tablepreviewpanel", RendererType.STANDALONE_PANEL);
        tsdb.addTabDef("preview", au().resolveSessionMessage("$m{studio.appapplet.form.formpreview}"),
                "fc-formpreviewpanel", RendererType.STANDALONE_PANEL);
        tablePreview = new TablePreview(au(), tableName);
        formPreview = new FormPreview(au(), formName);
        final String appletName = null;
        tabSheet = new TabSheet(tsdb.build(),
                Arrays.asList(new TabSheetItem("tablePreview", appletName, tablePreview, TABLE_PREVIEW_INDEX, true),
                        new TabSheetItem("formPreview", appletName, formPreview, FORM_PREVIEW_INDEX, true)));
        tabSheet.setEventHandler(this);
    }
}
