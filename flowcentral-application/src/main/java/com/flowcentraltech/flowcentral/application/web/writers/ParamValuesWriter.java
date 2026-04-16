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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.ParamValueEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.ParamValuesWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.DynamicField;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Parameter values widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(ParamValuesWidget.class)
@Component("fc-paramvalues-writer")
public class ParamValuesWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        ParamValuesWidget paramValuesWidget = (ParamValuesWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, paramValuesWidget);
        writer.write(">");
        List<ValueStore> valueStoreList = paramValuesWidget.getValueList();
        if (valueStoreList != null) {
            DynamicField paramCtrl = paramValuesWidget.getParamCtrl();
            int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                ParamValueEntry pvo = (ParamValueEntry) lineValueStore.getValueObject();
                writer.write("<div class=\"line\"><div class=\"lbl\">");
                if (pvo.getParamConfig().isMandatory()) {
                    writer.write("<img class=\"req\" src=\"");
                    writer.writeFileImageContextURL("$t{images/red_asterix.png}");
                    writer.write("\"/>");
                }
                writer.write("<span>").writeWithHtmlEscape(pvo.getParamConfig().getParamDesc()).write("</span></div>");
                writeValuesItem(writer, lineValueStore, paramCtrl);
                writer.write("</div>");
            }

        }
        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        ParamValuesWidget paramValuesWidget = (ParamValuesWidget) widget;
        List<ValueStore> valueStoreList = paramValuesWidget.getValueList();
        if (valueStoreList != null) {
            DynamicField paramCtrl = paramValuesWidget.getParamCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                writeBehavior(writer, paramValuesWidget, lineValueStore, paramCtrl);
            }
        }
    }

    private void writeValuesItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl) throws UnifyException {
        writer.write("<span class=\"item\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</span>");
    }

    private void writeBehavior(ResponseWriter writer, ParamValuesWidget paramValuesWidget, ValueStore lineValueStore,
            Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (paramValuesWidget.isContainerEditable()) {
            addPageAlias(paramValuesWidget.getId(), ctrl);
        }
    }
}
