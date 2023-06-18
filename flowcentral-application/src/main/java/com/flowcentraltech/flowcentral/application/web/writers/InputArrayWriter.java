/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.DynamicField;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Input array widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(InputArrayWidget.class)
@Component("fc-inputarray-writer")
public class InputArrayWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        InputArrayWidget inputArrayWidget = (InputArrayWidget) widget;        
        writer.write("<table");
        writeTagAttributes(writer, inputArrayWidget);
        writer.write(">");
        
        final int columns = inputArrayWidget.getColumns() <= 0 ? 1 : inputArrayWidget.getColumns();
        final String caption = inputArrayWidget.getCaption();
        if (!StringUtils.isBlank(caption)) {
            writer.write("<tr class=\"iacaption\"><td colspan=\"");
            writer.write(columns).write("\"><span>");
            writer.writeWithHtmlEscape(caption);
            writer.write("</span></tr>");
        }
        
        List<ValueStore> valueStoreList = inputArrayWidget.getValueList();
        if (valueStoreList != null) {
            DynamicField editCtrl = inputArrayWidget.getEditCtrl();
            DynamicField viewCtrl = inputArrayWidget.getViewCtrl();
            Control selectCtrl = inputArrayWidget.getSelectCtrl();
            final int len = valueStoreList.size();
            final String sectionStyle = "width:" + 100 / columns + "%;";
            for (int i = 0; i < len;) {
                writer.write("<tr class=\"line\">");
                int j = 0;
                for (; j < columns && i < len; j++, i++) {
                    ValueStore lineValueStore = valueStoreList.get(i);
                    boolean editable = lineValueStore.retrieve(boolean.class, "editable");
                    Control valueCtrl = editable ? editCtrl : viewCtrl;
                    writer.write("<td class=\"section\" style=\"");
                    writer.write(sectionStyle);
                    writer.write("\">");
                    writeValuesItem(writer, lineValueStore, valueCtrl, selectCtrl);
                    writer.write("</td>");
                }

                for (; j < columns; j++) {
                    writer.write("<td class=\"section\" style=\"");
                    writer.write(sectionStyle);
                    writer.write("\">");
                    writer.write("</td>");
                }
                writer.write("</tr>");
            }

        }
        writer.write("</table>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        InputArrayWidget inputArrayWidget = (InputArrayWidget) widget;
        List<ValueStore> valueStoreList = inputArrayWidget.getValueList();
        if (valueStoreList != null) {
            DynamicField editCtrl = inputArrayWidget.getEditCtrl();
            DynamicField viewCtrl = inputArrayWidget.getViewCtrl();
            Control selectCtrl = inputArrayWidget.getSelectCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                boolean editable = lineValueStore.retrieve(boolean.class, "editable");
                Control valueCtrl = editable ? editCtrl : viewCtrl;
                writeBehavior(writer, inputArrayWidget, lineValueStore, valueCtrl, selectCtrl);
            }
        }

        writer.beginFunction("fux.rigInputArray");
        writer.writeParam("pId", inputArrayWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", inputArrayWidget.getContainerId());
        writer.endFunction();
    }

    private void writeValuesItem(ResponseWriter writer, ValueStore lineValueStore, Control valueCtrl,
            Control selectCtrl) throws UnifyException {
        writer.write("<div class=\"iatable\">");
        writer.write("<div class=\"iarow\">");

        writer.write("<div class=\"iapost\">");
        selectCtrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(selectCtrl);
        writer.write("</div>");

        writer.write("<div class=\"iapre\">");
        writer.write("<span>");
        String label = lineValueStore.retrieve(String.class, "label");
        writer.writeWithHtmlEscape(label);
        writer.write("</span>");
        writer.write("</div>");

        writer.write("<div class=\"iamid\">");
        valueCtrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(valueCtrl);
        writer.write("</div>");

        writer.write("</div>");
        writer.write("</div>");
    }

    private void writeBehavior(ResponseWriter writer, InputArrayWidget inputArrayWidget, ValueStore lineValueStore,
            Control valueCtrl, Control selectCtrl) throws UnifyException {
        valueCtrl.setValueStore(lineValueStore);
        writer.writeBehavior(valueCtrl);

        selectCtrl.setValueStore(lineValueStore);
        writer.writeBehavior(selectCtrl);

        if (inputArrayWidget.isContainerEditable()) {
            addPageAlias(inputArrayWidget.getId(), valueCtrl);
            addPageAlias(inputArrayWidget.getId(), selectCtrl);
        }
    }
}
