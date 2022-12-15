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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.SearchEntries;
import com.flowcentraltech.flowcentral.application.web.widgets.SearchEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.SearchWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl.ChildWidgetInfo;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.DynamicField;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Search widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(SearchWidget.class)
@Component("fc-search-writer")
public class SearchWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        SearchWidget searchWidget = (SearchWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, searchWidget);
        writer.write(">");
        List<ValueStore> valueStoreList = searchWidget.getValueList();
        if (valueStoreList != null) {
            SearchEntries searchEntries = searchWidget.getSearchEntries();
            DynamicField paramCtrl = searchWidget.getParamCtrl();
            final String captionSuffix = searchWidget.getCaptionSuffix();
            final int len = valueStoreList.size();
            final int columns = searchEntries.getColumns() > 0 ? searchEntries.getColumns() : 1;
            writer.write("<div class=\"sftable\">");

            int i = 0;
            while (i < len) {
                writer.write("<div class=\"sfrow\">");

                int j = 0;
                for (j = 0; j < columns && i < len; j++, i++) {
                    writer.write("<div class=\"sfcol\">");
                    ValueStore itemValueStore = valueStoreList.get(i);
                    writeFieldCell(writer, searchEntries, itemValueStore, paramCtrl, captionSuffix);
                    writer.write("</div>");
                }

                for (; j < columns; j++) { // Filler
                    writer.write("<div class=\"sfcol\">");
                    writer.write("</div>");
                }

                writer.write("</div>");
            }

            writer.write("</div>");

        }
        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);

        SearchWidget searchWidget = (SearchWidget) widget;
        List<ValueStore> valueStoreList = searchWidget.getValueList();
        if (valueStoreList != null) {
            DynamicField paramCtrlA = searchWidget.getParamCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                writeBehavior(writer, searchWidget, lineValueStore, paramCtrlA);
            }
        }

        writer.beginFunction("fux.rigSearch");
        writer.writeParam("pId", searchWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", searchWidget.getContainerId());
        writer.endFunction();
    }

    private void writeFieldCell(ResponseWriter writer, SearchEntries searchEntries, ValueStore lineValueStore,
            Control ctrl, String captionSuffix) throws UnifyException {
        SearchEntry searchEntry = (SearchEntry) lineValueStore.getValueObject();
        writer.write("<div class=\"sffield\">");
        writer.write("<div class=\"sffieldrow\">");

        // Label
        writer.write("<div class=\"sfpre").write("\">");
        writer.write("<div class=\"sflabel\">");
        writer.write("<span>");
        writer.writeWithHtmlEscape(searchEntry.getLabel());
        if (captionSuffix != null) {
            writer.write(captionSuffix);
        }

        writer.write("</span>");
        writer.write("</div>");
        writer.write("</div>");

        // Search field
        writer.write("<div class=\"sfmid\">");
        writer.write("<div class=\"sfcon\">");
        writer.write("<div class=\"sfcontent\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</div>");

        writer.write("</div>");
        writer.write("</div>");
    }

    private void writeBehavior(ResponseWriter writer, SearchWidget searchWidget, ValueStore lineValueStore,
            DynamicField ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        addPageAlias(searchWidget.getId(), ctrl);
        
        if (!getRequestContextUtil().isFocusOnWidget()) {
            ChildWidgetInfo info = new ArrayList<ChildWidgetInfo>(ctrl.getChildWidgetInfos()).get(0);
            final String cId = info.getWidget().isUseFacadeFocus() ? info.getWidget().getFacadeId()
                    : info.getWidget().getId();
            getRequestContextUtil().setFocusOnWidgetId(cId);
        }
    }
}
