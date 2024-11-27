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

import com.flowcentraltech.flowcentral.application.web.widgets.EntityCompositionEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityCompositionWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.constant.DynamicEntityFieldType;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Entity composition widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(EntityCompositionWidget.class)
@Component("fc-entitycomposition-writer")
public class EntityCompositionWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final EntityCompositionWidget entityCompositionWidget = (EntityCompositionWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, entityCompositionWidget);
        writer.write(">");
        List<ValueStore> valueStoreList = entityCompositionWidget.getValueList();
        if (valueStoreList != null) {
            final Control entityNameCtrl = entityCompositionWidget.getFieldNameCtrl();
            final Control entityTableCtrl = entityCompositionWidget.getEntityTableCtrl();
            final Control fieldTypeCtrl = entityCompositionWidget.getFieldTypeCtrl();
            final Control dataTypeCtrl = entityCompositionWidget.getDataTypeCtrl();
            final Control fieldNameCtrl = entityCompositionWidget.getFieldNameCtrl();
            final Control columnCtrl = entityCompositionWidget.getColumnCtrl();

            final Control addEntityCtrl = entityCompositionWidget.getAddEntityCtrl();
            final Control delEntityCtrl = entityCompositionWidget.getDelEntityCtrl();

            final Control addFieldCtrl = entityCompositionWidget.getAddFieldCtrl();
            final Control delFieldCtrl = entityCompositionWidget.getDelFieldCtrl();
            
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                EntityCompositionEntry entry = (EntityCompositionEntry) lineValueStore.getValueObject();
                writer.write("<div class=\"line\">");
                // Write depth tabs
                int depth = entry.getDepth();
                for (int j = 0; j < depth; j++) {
                    writer.write("<span class=\"tab\">&nbsp;</span>");
                }

                final DynamicEntityFieldType fieldType = entry.getFieldType();
                if (fieldType == null) {
                    writeCompositionItem(writer, lineValueStore, entityNameCtrl);
                    writeCompositionItem(writer, lineValueStore, entityTableCtrl);
                    
                    writer.write("<div class=\"atab1\">");
                    writeActionItem(writer, lineValueStore, addEntityCtrl);
                    writeActionItem(writer, lineValueStore, addFieldCtrl);
                    if (i > 0) {
                        writeActionItem(writer, lineValueStore, delEntityCtrl);
                    }
                    writer.write("</div>");
                } else {
                    writeCompositionItem(writer, lineValueStore, fieldTypeCtrl);
                    writeCompositionItem(writer, lineValueStore, dataTypeCtrl);
                    writeCompositionItem(writer, lineValueStore, fieldNameCtrl);
                    writeCompositionItem(writer, lineValueStore, columnCtrl);

                    writer.write("<div class=\"atab2\">");
                    writeActionItem(writer, lineValueStore, delFieldCtrl);
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
        final EntityCompositionWidget entityCompositionWidget = (EntityCompositionWidget) widget;
        List<ValueStore> valueStoreList = entityCompositionWidget.getValueList();
        List<String> csb = new ArrayList<String>();
        if (valueStoreList != null) {
            final Control entityNameCtrl = entityCompositionWidget.getFieldNameCtrl();
            final Control entityTableCtrl = entityCompositionWidget.getEntityTableCtrl();
            final Control fieldTypeCtrl = entityCompositionWidget.getFieldTypeCtrl();
            final Control dataTypeCtrl = entityCompositionWidget.getDataTypeCtrl();
            final Control fieldNameCtrl = entityCompositionWidget.getFieldNameCtrl();
            final Control columnCtrl = entityCompositionWidget.getColumnCtrl();

            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                EntityCompositionEntry entry = (EntityCompositionEntry) lineValueStore.getValueObject();

                final DynamicEntityFieldType fieldType = entry.getFieldType();
                if (fieldType == null) {
                    writeBehavior(writer, entityCompositionWidget, lineValueStore, entityNameCtrl);
                    writeBehavior(writer, entityCompositionWidget, lineValueStore, entityTableCtrl);
                    csb.add(entityNameCtrl.getId());
                    csb.add(entityTableCtrl.getId());
                } else {
                    writeBehavior(writer, entityCompositionWidget, lineValueStore, fieldTypeCtrl);
                    writeBehavior(writer, entityCompositionWidget, lineValueStore, dataTypeCtrl);
                    writeBehavior(writer, entityCompositionWidget, lineValueStore, fieldNameCtrl);
                    writeBehavior(writer, entityCompositionWidget, lineValueStore, columnCtrl);
                    csb.add(fieldTypeCtrl.getId());
                    csb.add(dataTypeCtrl.getId());
                    csb.add(fieldNameCtrl.getId());
                    csb.add(columnCtrl.getId());
                }
            }
        }

        writer.beginFunction("fux.rigEntityComposition");
        writer.writeParam("pId", entityCompositionWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", entityCompositionWidget.getContainerId());
        writer.writeParam("pAddEId", entityCompositionWidget.getAddEntityCtrl().getBaseId());
        writer.writeParam("pDelEId", entityCompositionWidget.getDelEntityCtrl().getBaseId());
        writer.writeParam("pAddFId", entityCompositionWidget.getAddFieldCtrl().getBaseId());
        writer.writeParam("pDelFId", entityCompositionWidget.getDelFieldCtrl().getBaseId());
        writer.writeParam("pOnChgId", DataUtils.toArray(String.class, csb));
        writer.endFunction();
    }

    private void writeCompositionItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl) throws UnifyException {
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

    private void writeBehavior(ResponseWriter writer, EntityCompositionWidget entityCompositionWidget, ValueStore lineValueStore,
            Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (entityCompositionWidget.isContainerEditable()) {
            addPageAlias(entityCompositionWidget.getId(), ctrl);
        }
    }
}
