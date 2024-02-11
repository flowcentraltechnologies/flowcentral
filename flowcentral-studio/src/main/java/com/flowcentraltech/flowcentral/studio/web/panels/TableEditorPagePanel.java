/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.studio.web.widgets.TableEditor;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Table editor page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-tableeditorpagepanel")
@UplBinding("web/studio/upl/tableeditorpagepanel.upl")
public class TableEditorPagePanel extends AbstractStudioEditorPagePanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final boolean readOnly = isAppletContextReadOnly();
        TableEditor tableEditor = getTableEditorPage().getTableEditor();
        tableEditor.setReadOnly(readOnly);
        if (tableEditor.getEditColumnId() == null) {
            tableEditor.setEditColumnId(getWidgetByShortName("editColumnPanel").getId());
        }

        setWidgetVisible("saveBtn", !readOnly);
        setWidgetEditable("editColumnPanel", !readOnly);
    }

    @Action
    public void preview() throws UnifyException {
        getTableEditorPage().getTablePreview().reload();
    }

    @Action
    public void saveDesign() throws UnifyException {
        TableEditorPage tableEditorPage = getTableEditorPage();
        tableEditorPage.commitDesign();
        hintUser("$m{studioapptableapplet.tableeditor.success.hint}", tableEditorPage.getSubTitle());
    }

    private TableEditorPage getTableEditorPage() throws UnifyException {
        return getValue(TableEditorPage.class);
    }
}
