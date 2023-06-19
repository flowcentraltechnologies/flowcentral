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

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.FilterCondition;
import com.flowcentraltech.flowcentral.application.web.widgets.FilterWidget;
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
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Filter widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(FilterWidget.class)
@Component("fc-filter-writer")
public class FilterWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FilterWidget filterWidget = (FilterWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, filterWidget);
        writer.write(">");
        List<ValueStore> valueStoreList = filterWidget.getValueList();
        if (valueStoreList != null) {
            Control fieldSelectCtrl = filterWidget.getFieldSelectCtrl();
            DynamicField conditionTypeCtrl = filterWidget.getConditionTypeCtrl();
            DynamicField paramCtrlA = filterWidget.getParamCtrlA();
            DynamicField paramCtrlB = filterWidget.getParamCtrlB();
            Control fieldParamCtrlA = filterWidget.getFieldParamCtrlA();
            Control swapCtrl = filterWidget.getSwapCtrl();
            Control andCtrl = filterWidget.getAndCtrl();
            Control orCtrl = filterWidget.getOrCtrl();
            Control addCtrl = filterWidget.getAddCtrl();
            Control deleteCtrl = filterWidget.getDeleteCtrl();
            int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                FilterCondition fo = (FilterCondition) lineValueStore.getValueObject();
                writer.write("<div class=\"line\">");
                // Write depth tabs
                int depth = fo.getDepth();
                for (int j = 0; j < depth; j++) {
                    writer.write("<span class=\"tab\">&nbsp;</span>");
                }

                if (fo.getType() != null && fo.getType().isCompound()) {
                    writer.write("<span class=\"tab\">");
                    writer.write(getSessionMessage(fo.getType().symbolKey()));
                    writer.write("</span>");

                    writer.write("<div class=\"atab1\">");
                    writeActionItem(writer, lineValueStore, swapCtrl);
                    writeActionItem(writer, lineValueStore, andCtrl);
                    writeActionItem(writer, lineValueStore, orCtrl);
                    writeActionItem(writer, lineValueStore, addCtrl);
                    if (i > 0) {
                        writeActionItem(writer, lineValueStore, deleteCtrl);
                    }
                    writer.write("</div>");
                } else {
                    writeFilterItem(writer, lineValueStore, fieldSelectCtrl);
                    if (!StringUtils.isBlank(fo.getFieldName())) {
                        writeFilterItem(writer, lineValueStore, conditionTypeCtrl);
                        if (fo.getType() != null) {
                            if (fo.getType().isFieldVal() || fo.getType().isParameterVal()) {
                                writeFilterItem(writer, lineValueStore, fieldParamCtrlA);
                            } else {
                                if (fo.getParamInputA() != null) {
                                    writeFilterItem(writer, lineValueStore, paramCtrlA);
                                }

                                if (fo.getParamInputB() != null) {
                                    writeFilterItem(writer, lineValueStore, paramCtrlB);
                                }
                            }
                        }
                    }

                    writer.write("<div class=\"atab2\">");
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
        FilterWidget filterWidget = (FilterWidget) widget;
        List<ValueStore> valueStoreList = filterWidget.getValueList();
        List<String> csb = new ArrayList<String>();
        if (valueStoreList != null) {
            Control fieldSelectCtrl = filterWidget.getFieldSelectCtrl();
            DynamicField conditionTypeCtrl = filterWidget.getConditionTypeCtrl();
            DynamicField paramCtrlA = filterWidget.getParamCtrlA();
            DynamicField paramCtrlB = filterWidget.getParamCtrlB();
            Control fieldParamCtrlA = filterWidget.getFieldParamCtrlA();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                FilterCondition fo = (FilterCondition) lineValueStore.getValueObject();
                writeBehavior(writer, filterWidget, lineValueStore, fieldSelectCtrl);
                csb.add(fieldSelectCtrl.getId());

                if (!StringUtils.isBlank(fo.getFieldName())) {
                    writeBehavior(writer, filterWidget, lineValueStore, conditionTypeCtrl);
                    csb.add(conditionTypeCtrl.getControl().getId());
                    if (fo.getType() != null) {
                        if (fo.getType().isFieldVal() || fo.getType().isParameterVal()) {
                            writeBehavior(writer, filterWidget, lineValueStore, fieldParamCtrlA);
                        } else {
                            if (fo.getParamInputA() != null) {
                                writeBehavior(writer, filterWidget, lineValueStore, paramCtrlA);
                            }

                            if (fo.getParamInputB() != null) {
                                writeBehavior(writer, filterWidget, lineValueStore, paramCtrlB);
                            }
                        }
                    }
                }
            }
        }

        writer.beginFunction("fux.rigFilter");
        writer.writeParam("pId", filterWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", filterWidget.getContainerId());
        writer.writeParam("pSwpId", filterWidget.getSwapCtrl().getBaseId());
        writer.writeParam("pAndId", filterWidget.getAndCtrl().getBaseId());
        writer.writeParam("pOrId", filterWidget.getOrCtrl().getBaseId());
        writer.writeParam("pAddId", filterWidget.getAddCtrl().getBaseId());
        writer.writeParam("pDelId", filterWidget.getDeleteCtrl().getBaseId());
        writer.writeParam("pOnChgId", DataUtils.toArray(String.class, csb));
        writer.endFunction();
    }

    private void writeFilterItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl) throws UnifyException {
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

    private void writeBehavior(ResponseWriter writer, FilterWidget filterWidget, ValueStore lineValueStore,
            Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (filterWidget.isContainerEditable()) {
            addPageAlias(filterWidget.getId(), ctrl);
        }
    }
}
