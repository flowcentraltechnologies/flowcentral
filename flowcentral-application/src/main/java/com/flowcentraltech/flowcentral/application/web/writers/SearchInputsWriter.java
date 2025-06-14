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

        List<ValueStore> valueStoreList = searchInputsWidget.getValueList();
        if (valueStoreList != null) {
            Control labelCtrl = searchInputsWidget.getLabelCtrl();
            Control fieldSelectCtrl = searchInputsWidget.getFieldSelectCtrl();
            Control widgetCtrl = searchInputsWidget.getWidgetCtrl();
            Control conditionTypeCtrl = searchInputsWidget.getConditionTypeCtrl();
            DynamicField defValCtrl = searchInputsWidget.getDefValCtrl();
            Control fixedCtrl = searchInputsWidget.getFixedCtrl();
            Control moveUpCtrl = searchInputsWidget.getMoveUpCtrl();
            Control moveDownCtrl = searchInputsWidget.getMoveDownCtrl();
            Control deleteCtrl = searchInputsWidget.getDeleteCtrl();
            final int len = valueStoreList.size();
            final int last = len - 1;

            final String labelLabel = resolveSessionMessage("$m{searchinputs.label}");
            final String fieldLabel = resolveSessionMessage("$m{searchinputs.field}");
            final String widgetLabel = resolveSessionMessage("$m{searchinputs.widget}");
            final String conditionLabel = resolveSessionMessage("$m{searchinputs.condition}");
            final String defValLabel = resolveSessionMessage("$m{searchinputs.default}");
            final String fixedLabel = resolveSessionMessage("$m{searchinputs.fixed}");
            writer.write("<table class=\"editor\" style=\"display: block;width:100%;table-layout:fixed;\">");
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                SearchInputEntry sie = (SearchInputEntry) lineValueStore.getValueObject();
                writer.write("<tr class=\"line\">");
                writeValuesItem(writer, lineValueStore, fieldSelectCtrl, fieldLabel);
                if (sie.isWithFieldName()) {
                    writeValuesItem(writer, lineValueStore, labelCtrl, labelLabel);
                    if (sie.isWithLabel()) {
                        writeValuesItem(writer, lineValueStore, widgetCtrl, widgetLabel);
                        if (sie.isWithWidget() && sie.isFieldInput()) {
                            writeValuesItem(writer, lineValueStore, conditionTypeCtrl, conditionLabel);
                            if (sie.isWithDefValInput()) {
                                writeValuesItem(writer, lineValueStore, defValCtrl, defValLabel);
                                writeValuesItem(writer, lineValueStore, fixedCtrl, fixedLabel);
                            } else {
                                writeBlankValuesItem(writer);
                                writeBlankValuesItem(writer);
                            }
                        } else {
                            writeBlankValuesItem(writer);
                            writeBlankValuesItem(writer);
                            writeBlankValuesItem(writer);
                        }
                    } else {
                        writeBlankValuesItem(writer);
                        writeBlankValuesItem(writer);
                        writeBlankValuesItem(writer);
                        writeBlankValuesItem(writer);
                    }
                } else {
                    writeBlankValuesItem(writer);
                    writeBlankValuesItem(writer);
                    writeBlankValuesItem(writer);
                    writeBlankValuesItem(writer);
                    writeBlankValuesItem(writer);
                }

                writer.write("<td class=\"atab\">");
                moveUpCtrl.setDisabled(i == 0);
                moveDownCtrl.setDisabled(i >= (len - 2));
                writeActionItem(writer, lineValueStore, moveUpCtrl);
                writeActionItem(writer, lineValueStore, moveDownCtrl);
                if (i < last) {
                    writeActionItem(writer, lineValueStore, deleteCtrl);
                }
                writer.write("</td>");
                writer.write("</tr>");
            }

            writer.write("</table>");
        }

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        SearchInputsWidget searchInputsWidget = (SearchInputsWidget) widget;
        List<ValueStore> valueStoreList = searchInputsWidget.getValueList();
        List<String> csb = new ArrayList<String>();
        if (valueStoreList != null) {
            Control labelCtrl = searchInputsWidget.getLabelCtrl();
            Control fieldSelectCtrl = searchInputsWidget.getFieldSelectCtrl();
            Control widgetCtrl = searchInputsWidget.getWidgetCtrl();
            Control conditionTypeCtrl = searchInputsWidget.getConditionTypeCtrl();
            DynamicField defValCtrl = searchInputsWidget.getDefValCtrl();
            Control fixedCtrl = searchInputsWidget.getFixedCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                SearchInputEntry sie = (SearchInputEntry) lineValueStore.getValueObject();
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
        writer.write("<td class=\"vitem\">");
        writer.write("<div style=\"display:table;width:100%;\">");
        writer.write("<div style=\"display:table-row;\">");
        writer.write("<div style=\"display:table-cell;vertical-align:top;\">");
        writer.write("<span class=\"label\">");
        writer.write(label);
        writer.write("</span>");
        writer.write("</div>");
        writer.write("<div style=\"display:table-cell;vertical-align:top;\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</td>");
    }

    private void writeBlankValuesItem(ResponseWriter writer) throws UnifyException {
        writer.write("<td class=\"vitem\">");
        writer.write("</td>");
    }

    private void writeActionItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl) throws UnifyException {
        writer.write("<span style=\"display:inline-block;\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</span>");
    }

    private void writeBehavior(ResponseWriter writer, SearchInputsWidget tokenSequenceWidget, ValueStore lineValueStore,
            Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (tokenSequenceWidget.isContainerEditable()) {
            addPageAlias(tokenSequenceWidget.getId(), ctrl);
        }
    }
}
