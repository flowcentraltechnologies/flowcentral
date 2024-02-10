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

import com.flowcentraltech.flowcentral.application.web.widgets.WidgetRuleEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.WidgetRulesWidget;
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
 * Widget rules widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(WidgetRulesWidget.class)
@Component("fc-widgetrules-writer")
public class WidgetRulesWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        WidgetRulesWidget widgetRulesWidget = (WidgetRulesWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, widgetRulesWidget);
        writer.write(">");
        List<ValueStore> valueStoreList = widgetRulesWidget.getValueList();
        if (valueStoreList != null) {
            Control fieldSelectCtrl = widgetRulesWidget.getFieldSelectCtrl();
            Control widgetCtrl = widgetRulesWidget.getWidgetCtrl();
            Control deleteCtrl = widgetRulesWidget.getDeleteCtrl();
            int len = valueStoreList.size();

            final String usesLabel = resolveSessionMessage("$m{widgetrules.renderedwith}");
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                WidgetRuleEntry fso = (WidgetRuleEntry) lineValueStore.getValueObject();
                writer.write("<div class=\"line\">");
                final String columnLabel = resolveSessionMessage("$m{widgetrules.field}");
                writeRuleItem(writer, lineValueStore, fieldSelectCtrl, columnLabel);
                if (!StringUtils.isBlank(fso.getFieldName())) {
                    writeRuleItem(writer, lineValueStore, widgetCtrl, usesLabel);
                    writer.write("<div class=\"atab\">");
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
        WidgetRulesWidget widgetRulesWidget = (WidgetRulesWidget) widget;
        List<ValueStore> valueStoreList = widgetRulesWidget.getValueList();
        List<String> csb = new ArrayList<String>();
        if (valueStoreList != null) {
            Control fieldSelectCtrl = widgetRulesWidget.getFieldSelectCtrl();
            Control widgetCtrl = widgetRulesWidget.getWidgetCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                WidgetRuleEntry fso = (WidgetRuleEntry) lineValueStore.getValueObject();
                writeBehavior(writer, widgetRulesWidget, lineValueStore, fieldSelectCtrl);
                csb.add(fieldSelectCtrl.getId());

                if (!StringUtils.isBlank(fso.getFieldName())) {
                    writeBehavior(writer, widgetRulesWidget, lineValueStore, widgetCtrl);
                }
            }
        }

        writer.beginFunction("fux.rigWidgetRules");
        writer.writeParam("pId", widgetRulesWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", widgetRulesWidget.getContainerId());
        writer.writeParam("pDelId", widgetRulesWidget.getDeleteCtrl().getBaseId());
        writer.writeParam("pOnChgId", DataUtils.toArray(String.class, csb));
        writer.endFunction();
    }

    private void writeRuleItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl, String label)
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

    private void writeBehavior(ResponseWriter writer, WidgetRulesWidget widgetRulesWidget, ValueStore lineValueStore,
            Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (widgetRulesWidget.isContainerEditable()) {
            addPageAlias(widgetRulesWidget.getId(), ctrl);
        }
    }
}
