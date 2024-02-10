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

import com.flowcentraltech.flowcentral.application.web.widgets.PropertySequence;
import com.flowcentraltech.flowcentral.application.web.widgets.PropertySequenceEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.PropertySequenceWidget;
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
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Property sequence widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(PropertySequenceWidget.class)
@Component("fc-propertysequence-writer")
public class PropertySequenceWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        PropertySequenceWidget propertySequenceWidget = (PropertySequenceWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, propertySequenceWidget);
        writer.write(">");
        List<ValueStore> valueStoreList = propertySequenceWidget.getValueList();
        if (valueStoreList != null) {
            final PropertySequence propertySequence = propertySequenceWidget.getPropertySequence();
            final String lineTypeLabel = resolveSessionMessage(
                    propertySequence.getType() != null ? propertySequence.getType().label()
                            : "$m{propertysequence.type}");
            final String lineLabel = resolveSessionMessage("$m{propertysequence.label}");
            Control propertySelectCtrl = propertySequenceWidget.getPropertySelectCtrl();
            Control labelCtrl = propertySequenceWidget.getLabelCtrl();
            Control moveUpCtrl = propertySequenceWidget.getMoveUpCtrl();
            Control moveDownCtrl = propertySequenceWidget.getMoveDownCtrl();
            Control deleteCtrl = propertySequenceWidget.getDeleteCtrl();
            int len = valueStoreList.size();

            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                PropertySequenceEntry pso = (PropertySequenceEntry) lineValueStore.getValueObject();
                writer.write("<div class=\"line\">");
                writeValuesItem(writer, lineValueStore, propertySelectCtrl, lineTypeLabel);
                if (!StringUtils.isBlank(pso.getProperty())) {
                    writeValuesItem(writer, lineValueStore, labelCtrl, lineLabel);
                    writer.write("<div class=\"atab\">");
                    moveUpCtrl.setDisabled(i == 0);
                    moveDownCtrl.setDisabled(i >= (len - 2));
                    writeActionItem(writer, lineValueStore, moveUpCtrl);
                    writeActionItem(writer, lineValueStore, moveDownCtrl);
                    writeActionItem(writer, lineValueStore, deleteCtrl);
                    writer.write("</div>");
                }
                writer.write("</div>");
            }

        }
        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        PropertySequenceWidget propertySequenceWidget = (PropertySequenceWidget) widget;
        List<ValueStore> valueStoreList = propertySequenceWidget.getValueList();
        List<String> csb = new ArrayList<String>();
        if (valueStoreList != null) {
            Control propertySelectCtrl = propertySequenceWidget.getPropertySelectCtrl();
            Control labelCtrl = propertySequenceWidget.getLabelCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                PropertySequenceEntry fso = (PropertySequenceEntry) lineValueStore.getValueObject();
                writeBehavior(writer, propertySequenceWidget, lineValueStore, propertySelectCtrl);
                csb.add(propertySelectCtrl.getId());

                if (!StringUtils.isBlank(fso.getProperty())) {
                    writeBehavior(writer, propertySequenceWidget, lineValueStore, labelCtrl);
                }
            }
        }

        writer.beginFunction("fux.rigLineEntries");
        writer.writeParam("pId", propertySequenceWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", propertySequenceWidget.getContainerId());
        writer.writeParam("pMoveUpId", propertySequenceWidget.getMoveUpCtrl().getBaseId());
        writer.writeParam("pMoveDownId", propertySequenceWidget.getMoveDownCtrl().getBaseId());
        writer.writeParam("pDelId", propertySequenceWidget.getDeleteCtrl().getBaseId());
        writer.writeParam("pOnChgId", DataUtils.toArray(String.class, csb));
        writer.endFunction();
    }

    private void writeValuesItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl, String label)
            throws UnifyException {
        writer.write("<span class=\"label\">");
        writer.write(label);
        writer.write("</span>");
        writer.write("<span class=\"item\">");
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

    private void writeBehavior(ResponseWriter writer, PropertySequenceWidget propertySequenceWidget,
            ValueStore lineValueStore, Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (propertySequenceWidget.isContainerEditable()) {
            addPageAlias(propertySequenceWidget.getId(), ctrl);
        }
    }
}
