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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.SetValueEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.SetValuesWidget;
import com.flowcentraltech.flowcentral.common.web.writers.AbstractFlowCentralControlWriter;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.DynamicField;

/**
 * Set values widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(SetValuesWidget.class)
@Component("fc-setvalues-writer")
public class SetValuesWriter extends AbstractFlowCentralControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        SetValuesWidget setValuesWidget = (SetValuesWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, setValuesWidget);
        writer.write(">");
        final int len = setValuesWidget.getItemCount();
        if (len > 0) {
            Control fieldSelectCtrl = setValuesWidget.getFieldSelectCtrl();
            Control typeSelectCtrl = setValuesWidget.getTypeSelectCtrl();
            DynamicField paramCtrl = setValuesWidget.getParamCtrl();
            Control deleteCtrl = setValuesWidget.getDeleteCtrl();
            final String fieldLabel = resolveSessionMessage("$m{searchinputs.field}");
            writer.write("<div style=\"display:table;table-layout:fixed;width:100%;\">");

            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = setValuesWidget.getItemValueStoreAt(i);
                SetValueEntry svo = setValuesWidget.getItemAt();
                writer.write("<div class=\"line\">");

                final boolean isWithFieldName = !StringUtils.isBlank(svo.getFieldName());
                writer.write("<div class=\"itab\">");
                writeValuesItem(writer, lineValueStore, fieldSelectCtrl, fieldLabel, true);
                if (isWithFieldName) {
                    writeValuesItem(writer, lineValueStore, typeSelectCtrl, null, false);
                }
                if (isWithFieldName && svo.getType() != null && !svo.getType().isNoParam()
                        && svo.getParamInput() != null) {
                    writeValuesItem(writer, lineValueStore, paramCtrl, null, false);
                }
                writer.write("</div>");

                writer.write("<div class=\"atab\">");
                if (isWithFieldName) {
                    writeActionItem(writer, lineValueStore, deleteCtrl);
                }
                writer.write("</div>");

                writer.write("</div>");
            }

            writer.write("</div>");

        }
        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        SetValuesWidget setValuesWidget = (SetValuesWidget) widget;
        List<String> csb = new ArrayList<String>();
        final int len = setValuesWidget.getItemCount();
        if (len > 0) {
            Control fieldSelectCtrl = setValuesWidget.getFieldSelectCtrl();
            Control typeSelectCtrl = setValuesWidget.getTypeSelectCtrl();
            DynamicField paramCtrl = setValuesWidget.getParamCtrl();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = setValuesWidget.getItemValueStoreAt(i);
                SetValueEntry svo = setValuesWidget.getItemAt();
                writeBehavior(writer, setValuesWidget, lineValueStore, fieldSelectCtrl);
                csb.add(fieldSelectCtrl.getId());

                if (!StringUtils.isBlank(svo.getFieldName())) {
                    writeBehavior(writer, setValuesWidget, lineValueStore, typeSelectCtrl);
                    csb.add(typeSelectCtrl.getId());
                    if (svo.getType() != null && !svo.getType().isNoParam()) {
                        if (svo.getParamInput() != null) {
                            writeBehavior(writer, setValuesWidget, lineValueStore, paramCtrl);
                        }
                    }
                }
            }
        }

        writer.beginFunction("fux.rigSetValues");
        writer.writeParam("pId", setValuesWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", setValuesWidget.getContainerId());
        writer.writeParam("pDelId", setValuesWidget.getDeleteCtrl().getBaseId());
        writer.writeParam("pOnChgId", DataUtils.toArray(String.class, csb));
        writer.endFunction();
    }

    private void writeValuesItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl, String label, boolean bar)
            throws UnifyException {
        if (label != null) {
            writer.write("<span class=\"label\">");
            writer.write(label);
            writer.write("</span>");
        }

        writer.write(bar ? "<span class=\"item bar\">" : "<span class=\"item\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</span>");
    }

    private void writeActionItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl) throws UnifyException {
        writer.write("<span style=\"display:inline-block;\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</span>");
    }

    private void writeBehavior(ResponseWriter writer, SetValuesWidget setValuesWidget, ValueStore lineValueStore,
            Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (setValuesWidget.isContainerEditable()) {
            addPageAlias(setValuesWidget.getId(), ctrl);
        }
    }
}
