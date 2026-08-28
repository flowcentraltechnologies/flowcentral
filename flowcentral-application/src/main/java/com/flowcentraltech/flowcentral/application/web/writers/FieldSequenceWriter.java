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

import com.flowcentraltech.flowcentral.application.web.widgets.FieldSequenceEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.FieldSequenceWidget;
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

/**
 * Field sequence widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(FieldSequenceWidget.class)
@Component("fc-fieldsequence-writer")
public class FieldSequenceWriter extends AbstractFlowCentralControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FieldSequenceWidget fieldSequenceWidget = (FieldSequenceWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, fieldSequenceWidget);
        writer.write(">");
        final int len = fieldSequenceWidget.getItemCount();
        if (len > 0) {
            final Control fieldSelectCtrl = fieldSequenceWidget.getFieldSelectCtrl();
            final Control timeSeriesCtrl = fieldSequenceWidget.getTimeSeriesCtrl();
            final Control paramCtrl = fieldSequenceWidget.getParamCtrl();
            final Control moveUpCtrl = fieldSequenceWidget.getMoveUpCtrl();
            final Control moveDownCtrl = fieldSequenceWidget.getMoveDownCtrl();
            final Control deleteCtrl = fieldSequenceWidget.getDeleteCtrl();

            final String usesLabel = resolveSessionMessage("$m{fieldsequence.usesformatter}");
            final boolean useTimeSeries = fieldSequenceWidget.isUseTimeSeries();
            writer.write("<div style=\"display:table;table-layout:fixed;width:100%;\">");

            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = fieldSequenceWidget.getItemValueStoreAt(i);
                FieldSequenceEntry fso = fieldSequenceWidget.getItemAt();
                writer.write("<div class=\"line\">");

                final String columnLabel = resolveSessionMessage("$m{fieldsequence.column}", i + 1);
                final boolean isWithFieldName = !StringUtils.isBlank(fso.getFieldName());
                writer.write("<div class=\"itab\">");
                writeValuesItem(writer, lineValueStore, fieldSelectCtrl, columnLabel, true);
                if (isWithFieldName) {
                    writeValuesItem(writer, lineValueStore,
                            useTimeSeries && fso.isDateTimeField() ? timeSeriesCtrl : paramCtrl, usesLabel, false);
                }
                writer.write("</div>");

                writer.write("<div class=\"atab\">");
                if (isWithFieldName) {
                    moveUpCtrl.setDisabled(i == 0);
                    moveDownCtrl.setDisabled(i >= (len - 2));
                    writeActionItem(writer, lineValueStore, moveUpCtrl);
                    writeActionItem(writer, lineValueStore, moveDownCtrl);
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
        FieldSequenceWidget fieldSequenceWidget = (FieldSequenceWidget) widget;
        List<String> csb = new ArrayList<String>();
        final int len = fieldSequenceWidget.getItemCount();
        if (len > 0) {
            final Control fieldSelectCtrl = fieldSequenceWidget.getFieldSelectCtrl();
            final Control paramCtrl = fieldSequenceWidget.getParamCtrl();
            final Control timeSeriesCtrl = fieldSequenceWidget.getTimeSeriesCtrl();
            final boolean useTimeSeries = fieldSequenceWidget.isUseTimeSeries();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = fieldSequenceWidget.getItemValueStoreAt(i);
                FieldSequenceEntry fso = (FieldSequenceEntry) fieldSequenceWidget.getItemAt();
                writeBehavior(writer, fieldSequenceWidget, lineValueStore, fieldSelectCtrl);
                csb.add(fieldSelectCtrl.getId());

                if (!StringUtils.isBlank(fso.getFieldName())) {
                    writeBehavior(writer, fieldSequenceWidget, lineValueStore,
                            useTimeSeries && fso.isDateTimeField() ? timeSeriesCtrl : paramCtrl);
                }
            }
        }

        writer.beginFunction("fux.rigLineEntries");
        writer.writeParam("pId", fieldSequenceWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", fieldSequenceWidget.getContainerId());
        writer.writeParam("pMoveUpId", fieldSequenceWidget.getMoveUpCtrl().getBaseId());
        writer.writeParam("pMoveDownId", fieldSequenceWidget.getMoveDownCtrl().getBaseId());
        writer.writeParam("pDelId", fieldSequenceWidget.getDeleteCtrl().getBaseId());
        writer.writeParam("pOnChgId", DataUtils.toArray(String.class, csb));
        writer.endFunction();
    }

    private void writeValuesItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl, String label,
            boolean bar) throws UnifyException {
        writer.write("<span class=\"label\">");
        writer.write(label);
        writer.write("</span>");
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

    private void writeBehavior(ResponseWriter writer, FieldSequenceWidget fieldSequenceWidget,
            ValueStore lineValueStore, Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (fieldSequenceWidget.isContainerEditable()) {
            addPageAlias(fieldSequenceWidget.getId(), ctrl);
        }
    }
}
