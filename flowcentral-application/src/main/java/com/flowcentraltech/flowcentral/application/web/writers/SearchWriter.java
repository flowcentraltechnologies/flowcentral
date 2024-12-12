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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.SearchEntries;
import com.flowcentraltech.flowcentral.application.web.widgets.SearchEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.SearchWidget;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl.ChildWidgetInfo;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
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
            final boolean vertical = searchWidget.isVertical();
            final int columns = !vertical && searchEntries.getColumns() > 0 ? searchEntries.getColumns() : 1;
            writer.write("<div class=\"sftable\">");

            int i = 0;
            paramCtrl.initValueStoreMemory(len);
            while (i < len) {
                writer.write("<div class=\"sfrow\">");

                int j = 0;
                for (j = 0; j < columns && i < len; j++, i++) {
                    writer.write("<div class=\"sfcol\">");
                    ValueStore itemValueStore = valueStoreList.get(i);
                    writeFieldCell(writer, searchEntries, itemValueStore, paramCtrl, captionSuffix, vertical);
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
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        SearchWidget searchWidget = (SearchWidget) widget;
        List<ValueStore> valueStoreList = searchWidget.getValueList();
        if (valueStoreList != null) {
            final String searchId = searchWidget.getId();
            DynamicField paramCtrlA = searchWidget.getParamCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore inputValueStore = valueStoreList.get(i);
                writeBehavior(writer, searchId, handlers, inputValueStore, paramCtrlA);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void writeFieldCell(ResponseWriter writer, SearchEntries searchEntries, ValueStore lineValueStore,
            Control ctrl, String captionSuffix, boolean vertical) throws UnifyException {
        SearchEntry searchEntry = (SearchEntry) lineValueStore.getValueObject();
        writer.write("<div class=\"sffield\">");
        writer.write("<div class=\"sffieldrow\">");

        // Label
        writer.write("<div class=\"sfpre").write("\">");
        writer.write(vertical ? "<div class=\"sflabelv\">" : "<div class=\"sflabel\">");
        writer.write("<span class=\"sfbasetxt\">");
        writer.writeWithHtmlEscape(searchEntry.getLabel());
        if (captionSuffix != null) {
            writer.write(captionSuffix);
        }

        writer.write("</span>");
        if (searchEntries.isShowConditions()) {
            writer.write("<span class=\"sfcondtxt\">");
            String symbol = searchEntry.isSessionEntry() ? getSessionMessage("searchwriter.session")
                    : (searchEntry.isFieldEntry()
                            ? getSessionMessage(searchEntry.getConditionType().filterType().labelKey())
                            : getSessionMessage("searchwriter.generated"));
            writer.writeWithHtmlEscape(symbol);
            writer.write("</span>");
        }
        writer.write("</div>");
        writer.write("</div>");

        if (searchEntry.isSessionEntry() && searchEntry.isWithParamInput()) {
            final Object val = getSessionAttribute(searchEntry.getSessionAttributeName());
            if (val != null) {
                ((AbstractInput<Object>) searchEntry.getParamInput()).setValue(val);
            }
        }

        // Search field
        writer.write("<div class=\"sfmid\">");
        writer.write("<div class=\"sfcon\">");
        writer.write("<div class=\"sfcontent\">");
        ctrl.setValueStore(lineValueStore);
        ctrl.setDisabled(searchEntry.isSessionEntry());
        writer.writeStructureAndContent(ctrl);
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</div>");

        writer.write("</div>");
        writer.write("</div>");
    }

    private void writeBehavior(ResponseWriter writer, String searchId, EventHandler[] handlers,
            ValueStore inputValueStore, DynamicField ctrl) throws UnifyException {
        ctrl.setValueStore(inputValueStore);
        writer.writeBehavior(ctrl, handlers);
        addPageAlias(searchId, ctrl);

        if (!getRequestContextUtil().isFocusOnWidget()) {
            ChildWidgetInfo info = new ArrayList<ChildWidgetInfo>(ctrl.getChildWidgetInfos()).get(0);
            final String cId = info.getWidget().isUseFacadeFocus() ? info.getWidget().getFacadeId()
                    : info.getWidget().getId();
            getRequestContextUtil().setFocusOnWidgetId(cId);
        }
    }
}
