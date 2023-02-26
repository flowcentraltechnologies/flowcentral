/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.studio.web.writers;

import com.flowcentraltech.flowcentral.studio.web.panels.TableColumnEditorPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.panel.DetachedPanelWriter;

/**
 * Table column editor panel writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(TableColumnEditorPanel.class)
@Component("tablecolumneditorpanel-writer")
public class TableColumnEditorPanelWriter extends DetachedPanelWriter {

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        TableColumnEditorPanel tableColumnEditorPanel = (TableColumnEditorPanel) widget;
        writer.beginFunction("fuxstudio.rigTableColumnEditorPanel");
        writer.writeParam("pId", tableColumnEditorPanel.getId());
        writer.writeParam("pFieldId", tableColumnEditorPanel.getFieldId());
        writer.writeParam("pLabelId", tableColumnEditorPanel.getLabelId());
        writer.writeParam("pLinkId", tableColumnEditorPanel.getLinkId());
        writer.writeParam("pSymbolId", tableColumnEditorPanel.getSymbolId());
        writer.writeParam("pOrderId", tableColumnEditorPanel.getOrderId());
        writer.writeParam("pWidgetId", tableColumnEditorPanel.getWidgetId());
        writer.writeParam("pWidthId", tableColumnEditorPanel.getWidthId());
        writer.writeParam("pSwitchId", tableColumnEditorPanel.getSwitchId());
        writer.writeParam("pHiddenId", tableColumnEditorPanel.getHiddenId());
        writer.writeParam("pEditableId", tableColumnEditorPanel.getEditableId());
        writer.writeParam("pDisabledId", tableColumnEditorPanel.getDisabledId());
        writer.writeParam("pSortId", tableColumnEditorPanel.getSortableId());
        writer.writeParam("pSummaryId", tableColumnEditorPanel.getSummaryId());
        writer.writeParam("pApplyId", tableColumnEditorPanel.getApplyId());
        writer.writeParam("pCancelId", tableColumnEditorPanel.getCancelId());
        writer.endFunction();
    }

}
