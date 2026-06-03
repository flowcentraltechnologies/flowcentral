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

import com.flowcentraltech.flowcentral.application.web.widgets.SearchInputEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.SearchInputsWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.DynamicField;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Search inputs widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(SearchInputsWidget.class)
@Component("fc-searchinputs-writer")
public class SearchInputsWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        SearchInputsWidget searchInputsWidget = (SearchInputsWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, searchInputsWidget);
        writer.write(">");

        final int len = searchInputsWidget.getItemCount();
        if (len > 0) {
            Control labelCtrl = searchInputsWidget.getLabelCtrl();
            Control fieldSelectCtrl = searchInputsWidget.getFieldSelectCtrl();
            Control widgetCtrl = searchInputsWidget.getWidgetCtrl();
            Control conditionTypeCtrl = searchInputsWidget.getConditionTypeCtrl();
            DynamicField defValCtrl = searchInputsWidget.getDefValCtrl();
            Control fixedCtrl = searchInputsWidget.getFixedCtrl();
            Control moveUpCtrl = searchInputsWidget.getMoveUpCtrl();
            Control moveDownCtrl = searchInputsWidget.getMoveDownCtrl();
            Control deleteCtrl = searchInputsWidget.getDeleteCtrl();

            final String labelLabel = resolveSessionMessage("$m{searchinputs.label}");
            final String fieldLabel = resolveSessionMessage("$m{searchinputs.field}");
            final String widgetLabel = resolveSessionMessage("$m{searchinputs.widget}");
            final String conditionLabel = resolveSessionMessage("$m{searchinputs.condition}");
            final String defValLabel = resolveSessionMessage("$m{searchinputs.default}");
            final String fixedLabel = resolveSessionMessage("$m{searchinputs.fixed}");
            writer.write("<div style=\"display:table;table-layout:fixed;width:100%;\">");

            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = searchInputsWidget.getItemValueStoreAt(i);
                SearchInputEntry sie = searchInputsWidget.getItemAt();
                final boolean isWithFieldName = sie.isWithFieldName();
                final boolean isWithLabel = sie.isWithLabel();
                final boolean isWithWidget = sie.isWithWidget();
                writer.write("<div class=\"line\">");
                writer.write("<div class=\"itab\">");
                writeValuesItem(writer, lineValueStore, fieldSelectCtrl, fieldLabel);
                if (isWithFieldName) {
                    writeValuesItem(writer, lineValueStore, labelCtrl, labelLabel);
                    if (isWithLabel) {
                        writeValuesItem(writer, lineValueStore, widgetCtrl, widgetLabel);
                    }
                }
                if (isWithFieldName && isWithLabel && isWithWidget) {
                    writeValuesItem(writer, lineValueStore, conditionTypeCtrl, conditionLabel);
                    if (sie.isWithDefValInput()) {
                        writeValuesItem(writer, lineValueStore, defValCtrl, defValLabel);
                        writeValuesItem(writer, lineValueStore, fixedCtrl, fixedLabel);
                    }
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
        SearchInputsWidget searchInputsWidget = (SearchInputsWidget) widget;
        List<String> csb = new ArrayList<String>();
        final int len = searchInputsWidget.getItemCount();
        if (len > 0) {
            Control labelCtrl = searchInputsWidget.getLabelCtrl();
            Control fieldSelectCtrl = searchInputsWidget.getFieldSelectCtrl();
            Control widgetCtrl = searchInputsWidget.getWidgetCtrl();
            Control conditionTypeCtrl = searchInputsWidget.getConditionTypeCtrl();
            DynamicField defValCtrl = searchInputsWidget.getDefValCtrl();
            Control fixedCtrl = searchInputsWidget.getFixedCtrl();

            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = searchInputsWidget.getItemValueStoreAt(i);
                SearchInputEntry sie = searchInputsWidget.getItemAt();
                writeBehavior(writer, searchInputsWidget, lineValueStore, fieldSelectCtrl);
                csb.add(fieldSelectCtrl.getId());
                if (sie.isWithFieldName()) {
                    writeBehavior(writer, searchInputsWidget, lineValueStore, labelCtrl);
                    csb.add(labelCtrl.getId());
                    if (sie.isWithLabel()) {
                        writeBehavior(writer, searchInputsWidget, lineValueStore, widgetCtrl);
                        csb.add(widgetCtrl.getId());
                        if (sie.isWithWidget() && sie.isFieldInput()) {
                            writeBehavior(writer, searchInputsWidget, lineValueStore, conditionTypeCtrl);
                            csb.add(conditionTypeCtrl.getId());
                            if (sie.isWithDefValInput()) {
                                writeBehavior(writer, searchInputsWidget, lineValueStore, defValCtrl);
                                writeBehavior(writer, searchInputsWidget, lineValueStore, fixedCtrl);
                                csb.add(defValCtrl.getId());
                                csb.add(fixedCtrl.getId());
                            }
                        }
                    }
                }
            }
        }

        writer.beginFunction("fux.rigLineEntries");
        writer.writeParam("pId", searchInputsWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", searchInputsWidget.getContainerId());
        writer.writeParam("pMoveUpId", searchInputsWidget.getMoveUpCtrl().getBaseId());
        writer.writeParam("pMoveDownId", searchInputsWidget.getMoveDownCtrl().getBaseId());
        writer.writeParam("pDelId", searchInputsWidget.getDeleteCtrl().getBaseId());
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

    private void writeBehavior(ResponseWriter writer, SearchInputsWidget searchInputsWidget, ValueStore lineValueStore,
            Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (searchInputsWidget.isContainerEditable()) {
            addPageAlias(searchInputsWidget.getId(), ctrl);
        }
    }
}
