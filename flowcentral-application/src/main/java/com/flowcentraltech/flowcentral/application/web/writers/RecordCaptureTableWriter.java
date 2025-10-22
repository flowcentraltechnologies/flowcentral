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

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.AbstractRecordCapture;
import com.flowcentraltech.flowcentral.application.data.RecordCaptureColumnDef;
import com.flowcentraltech.flowcentral.application.data.RecordCaptureTableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.RecordCaptureTable;
import com.flowcentraltech.flowcentral.application.web.widgets.RecordCaptureTableWidget;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl.ChildWidgetInfo;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Record capture table widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(RecordCaptureTableWidget.class)
@Component("fc-recordcapturetable-writer")
public class RecordCaptureTableWriter extends AbstractControlWriter {

    @Configurable
    private SystemModuleService systemModuleService;

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final RecordCaptureTableWidget tableWidget = (RecordCaptureTableWidget) widget;
        final RecordCaptureTable<? extends AbstractRecordCapture> table = tableWidget.getTable();
        if (table != null) {
            final boolean classicMode = true ; //emModuleService.getSysParameterValue(boolean.class,
                    //ApplicationModuleSysParamConstants.ALL_TABLE_IN_CLASSIC_MODE);
            writer.write("<div");
            writeTagAttributes(writer, tableWidget);
            writer.write(">");
            writer.write("<div><table");
            writeTagId(writer, tableWidget);
            if (classicMode) {
                writeTagStyleClass(writer, "table classic");
            } else {
                writeTagStyleClass(writer, "table");
            }
            writer.write(">");

            writeColumnGroup(writer, tableWidget);
            writeHeaderRow(writer, tableWidget);
            writeBodyRows(writer, tableWidget);

            writer.write("</table></div>");
            writer.write("</div>");
        }
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);

        final RecordCaptureTableWidget tableWidget = (RecordCaptureTableWidget) widget;
        final String tableWidgetId = tableWidget.getId();
        final boolean isTableDisabled = tableWidget.isContainerDisabled();

        // External control behavior
        final RecordCaptureTable<? extends AbstractRecordCapture> table = tableWidget.getTable();
        if (table != null) {
            final boolean isTableEditable = table.isEditable();
            final boolean entryMode = table.isEditable();

            final List<ValueStore> valueList = tableWidget.getValueList();
            final int vlen = valueList.size();
            for (int i = 0; i < vlen; i++) {
                ValueStore valueStore = valueList.get(i);
                int columnIndex = 0;
                for (ChildWidgetInfo widgetInfo : tableWidget.getChildWidgetInfos()) {
                    if (widgetInfo.isExternal() && widgetInfo.isControl()) {
                        Widget chWidget = widgetInfo.getWidget();
                        if (isTableDisabled) {
                            chWidget.setEditable(false);
                            chWidget.setDisabled(true);
                        } else {
                            chWidget.setEditable(entryMode);
                            chWidget.setDisabled(false);
                        }

                        chWidget.setValueStore(valueStore);
                        writer.writeBehavior(chWidget);

                        if (isTableEditable && columnIndex > 0) {
                            addPageAlias(tableWidgetId, chWidget);
                        }

                        columnIndex++;
                    }
                }

            }

            if (isTableEditable) {
                getRequestContextUtil().addOnSaveContentWidget(tableWidgetId);
            }
        }
    }

    private int writeColumnGroup(ResponseWriter writer, RecordCaptureTableWidget tableWidget) throws UnifyException {
        int columnCount = 0;
        final RecordCaptureTable<? extends AbstractRecordCapture> table = tableWidget.getTable();
        final RecordCaptureTableDef tableDef = table.getTableDef();

        // Column widths
        writer.write("<colgroup>");
        if (tableDef.isSerialNo()) {
            writer.write("<col class=\"cserialh\">");
            columnCount++;
        }

        final int len = tableDef.getColumnDefs().size();
        for (int i = 0; i < len; i++) {
            final RecordCaptureColumnDef tabelColumnDef = tableDef.getRecordCaptureColumnDef(i);
            writer.write("<col ");
            writeTagStyle(writer, tabelColumnDef.getHeaderStyle());
            writer.write(">");
        }
        columnCount += len;

        writer.write("</colgroup>");

        return columnCount;
    }

    private void writeHeaderRow(ResponseWriter writer, RecordCaptureTableWidget tableWidget) throws UnifyException {
        writer.write("<tr>");
        final RecordCaptureTable<? extends AbstractRecordCapture> table = tableWidget.getTable();
        if (table != null) {
            final RecordCaptureTableDef tableDef = table.getTableDef();
            if (tableDef.isSerialNo()) {
                writer.write("<th class=\"mserialh\"><span>");
                writer.write(getSessionMessage("tablewidget.serialno"));
                writer.write("</span></th>");
            }

            final boolean sysHeaderUppercase = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ALL_TABLE_HEADER_TO_UPPERCASE);
            final boolean sysHeaderCenterAlign = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ALL_TABLE_HEADER_CENTER_ALIGNED);
            final int len = tableDef.getColumnDefs().size();
            for (int columnIndex = 0; columnIndex < len; columnIndex++) {
                RecordCaptureColumnDef tabelColumnDef = tableDef.getRecordCaptureColumnDef(columnIndex);
                writer.write("<th");
                if (sysHeaderCenterAlign) {
                    writeTagStyle(writer, "text-align:center;");
                }

                writer.write("><span ");
                String caption = resolveSessionMessage(tabelColumnDef.getCaption());
                if (caption != null && (sysHeaderUppercase)) {
                    caption = caption.toUpperCase();
                }

                if (caption != null) {
                    writer.write(" title=\"").writeWithHtmlEscape(caption).write("\"");
                }

                writer.write(">");

                if (caption != null) {
                    writer.writeWithHtmlEscape(caption);
                } else {
                    writer.writeHtmlFixedSpace();
                }

                writer.write("</span>");
                writer.write("</th>");
            }

        }

        writer.write("</tr>");
    }

    private void writeBodyRows(ResponseWriter writer, RecordCaptureTableWidget tableWidget) throws UnifyException {
        final RecordCaptureTable<? extends AbstractRecordCapture> table = tableWidget.getTable();
        if (table != null) {
            final RecordCaptureTableDef tableDef = table.getTableDef();
            final boolean entryMode = table.isEditable();
            final boolean isSerialNo = tableDef.isSerialNo();
            final int pageIndex = 1;
            List<ValueStore> valueList = tableWidget.getValueList();
            final int vlen = valueList.size();
            if (vlen == 0) {
                writeNoRecordsFoundRow(writer, tableWidget, entryMode, false, isSerialNo);
            } else {
                final String even = "even";
                final String odd = "odd";
                for (int i = 0; i < vlen; i++) {
                    ValueStore valueStore = valueList.get(i);
                    // Normal row
                    final String rowClass = resolveRowStyleClass(i, true, even, odd);
                    writer.write("<tr");
                    if (!StringUtils.isBlank(rowClass)) {
                        writeTagStyleClass(writer, rowClass);
                    }

                    writeTagName(writer, tableWidget.getRowId());
                    writer.write(">");

                    if (isSerialNo) {
                        writer.write("<td class=\"mseriald\"><span>");
                        writer.write(pageIndex + i);
                        writer.write(".</span></td>");
                    }

                    int columnIndex = 0;
                    for (ChildWidgetInfo widgetInfo : tableWidget.getChildWidgetInfos()) {
                        if (widgetInfo.isExternal() && widgetInfo.isControl()) {
                            final RecordCaptureColumnDef tabelColumnDef = tableDef.getRecordCaptureColumnDef(columnIndex);
                            final String fieldName = tabelColumnDef.getFieldName();
                            widgetInfo.setName(fieldName);

                            Widget chWidget = widgetInfo.getWidget();
                            chWidget.setEditable(entryMode);
                            chWidget.setDisabled(!entryMode);
                            chWidget.setValueStore(valueStore);
                            writer.write("<td");
                            writeTagStyle(writer, chWidget.getColumnStyle());
                            writer.write(">");
                            writer.writeStructureAndContent(chWidget);
                            if (entryMode) {
                                writeTargetHidden(writer, chWidget.getId(), i);
                            }

                            writer.write("</td>");

                            columnIndex++;
                        }
                    }

                    writer.write("</tr>");
                }
            }

        }
    }

    private String resolveRowStyleClass(final int i, boolean alternatingRows, String even, String odd)
            throws UnifyException {
        if (alternatingRows) {
            if (i % 2 == 0) {
                return even;
            }

            return odd;
        }

        return null;
    }

    private void writeNoRecordsFoundRow(ResponseWriter writer, RecordCaptureTableWidget tableWidget, boolean entryMode,
            boolean multiSelect, boolean isSerialNo) throws UnifyException {
        writeNonDataRow(writer, tableWidget, entryMode, multiSelect, isSerialNo, "even", "mnorec",
                resolveSessionMessage("$m{tablewidget.norecordsfound}"));
    }

    private void writeNonDataRow(ResponseWriter writer, RecordCaptureTableWidget tableWidget, boolean entryMode,
            boolean multiSelect, boolean isSerialNo, String rowClass, String styleClass, String text)
            throws UnifyException {
        writer.write("<tr class=\"").write(rowClass).write("\">");
        int skip = 0;
        if (isSerialNo) {
            writer.write("<td class=\"mseriald\"><span></span></td>");
            skip++;
        }

        writer.write("<td colspan=\"");
        writer.write(tableWidget.getChildWidgetInfos().size() - skip);
        writer.write("\"><span class=\"").write(styleClass).write("\">");
        writer.writeWithHtmlEscape(text);
        writer.write("</span></td>");
        writer.write("</tr>");
    }
}
