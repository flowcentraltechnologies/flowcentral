/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.TableFilterDef;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTableWidget;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.business.policies.TableStateOverride;
import com.flowcentraltech.flowcentral.common.data.EntryTableMessage;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.constant.MessageType;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl.ChildWidgetInfo;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.PushType;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.panel.StandalonePanel;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Table widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(AbstractTableWidget.class)
@Component("fc-table-writer")
public class TableWriter extends AbstractControlWriter {

    @Configurable
    private SystemModuleService systemModuleService;

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        AbstractTableWidget<?, ?, ?> tableWidget = (AbstractTableWidget<?, ?, ?>) widget;

        AbstractTable<?, ?> table = tableWidget.getTable(); // Must call this here to initialize table
        if (table != null) {
            TableDef tableDef = table.getTableDef();
            final boolean multiSelect = tableDef.isMultiSelect() || tableWidget.isMultiSelect();
            if (multiSelect) {
                writeHiddenPush(writer, tableWidget.getSelectCtrl(), PushType.CHECKBOX);
            }

            final boolean classicMode = !tableDef.isNonConforming() && systemModuleService
                    .getSysParameterValue(boolean.class, ApplicationModuleSysParamConstants.ALL_TABLE_IN_CLASSIC_MODE);
            boolean sortable = tableDef.isSortable() && table.getNumberOfPages() > 0;
            writer.write("<div");
            writeTagStyleClassWithLeadingExtraStyleClasses(writer, tableWidget, "fc-table");
            writeTagStyle(writer, tableWidget);
            writer.write(">");
            writer.write("<div><table");
            writeTagId(writer, tableWidget);
            if (classicMode) {
                writeTagStyleClass(writer, "table classic");
            } else {
                writeTagStyleClass(writer, "table");
            }
            writer.write(">");

            String errMsg = (String) getRequestAttribute(
                    AppletRequestAttributeConstants.SILENT_MULTIRECORD_SEARCH_ERROR_MSG);
            if (!StringUtils.isBlank(errMsg)) {
                writer.write("<div class=\"mwarn\"><span style=\"display:block;text-align:center;\">");
                writer.write(errMsg);
                writer.write("</span></div>");
            }

            if (tableDef.isHeaderless()) {
                // Column widths
                writer.write("<colgroup>");
                final boolean entryMode = table.isEntryMode();
                final boolean supportSelect = !table.isFixedAssignment();
                if (supportSelect && multiSelect && !entryMode) {
                    writer.write("<col class=\"cselh\">");
                }

                if (tableDef.isSerialNo()) {
                    writer.write("<col class=\"cserialh\">");
                }

                int index = 0;
                for (ChildWidgetInfo widgetInfo : tableWidget.getChildWidgetInfos()) {
                    if (widgetInfo.isExternal() && widgetInfo.isControl()) {
                        TableColumnDef tabelColumnDef = tableDef.getColumnDef(index);
                        writer.write("<col ");
                        writeTagStyle(writer, tabelColumnDef.getHeaderStyle());
                        writer.write(">");
                        index++;
                    }
                }

                if (supportSelect && multiSelect && entryMode) {
                    writer.write("<col class=\"cselh\">");
                }

                if (tableWidget.isActionColumn()) {
                    writer.write("<col class=\"cactionh\">");
                }

                writer.write("</colgroup>");
            } else {
                // Header
                writeHeaderRow(writer, tableWidget);
            }

            writeBodyRows(writer, tableWidget);
            writer.write("</table></div>");
            if (sortable) {
                writer.writeStructureAndContent(tableWidget.getSortColumnCtrl());
            }

            writer.write("</div>");
        }
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        AbstractTableWidget<?, ?, ?> tableWidget = (AbstractTableWidget<?, ?, ?>) widget;
        final String tableWidgetId = tableWidget.getId();
        final boolean isContainerEditable = tableWidget.isContainerEditable();

        // External control behavior
        final AbstractTable<?, ?> table = tableWidget.getTable();
        if (table != null) {
            final TableDef tableDef = table.getTableDef();
            final boolean multiSelect = tableDef.isMultiSelect() || tableWidget.isMultiSelect();
            final List<EventHandler> switchOnChangeHandlers = table.getSwitchOnChangeHandlers();
            final EventHandler switchOnChangeHandler = tableWidget.getSwitchOnChangeHandler();
            final List<EventHandler> crudActionHandlers = table.getCrudActionHandlers();
            final Control[] actionCtrl = tableWidget.getActionCtrl();
            final EventHandler[] actionHandler = tableWidget.getActionEventHandler();
            final boolean isActionColumn = tableWidget.isActionColumn();
            final boolean isRowAction = !isActionColumn && !DataUtils.isBlank(crudActionHandlers);

            List<ValueStore> valueList = tableWidget.getValueList();
            int len = valueList.size();
            for (int i = 0; i < len; i++) {
                ValueStore valueStore = valueList.get(i);
                int index = 0;
                for (ChildWidgetInfo widgetInfo : tableWidget.getChildWidgetInfos()) {
                    if (widgetInfo.isExternal() && widgetInfo.isControl()) {
                        TableColumnDef tabelColumnDef = tableDef.getColumnDef(index);
                        Widget chWidget = widgetInfo.getWidget();
                        chWidget.setValueStore(valueStore);
                        writer.writeBehavior(chWidget);
                        if (tabelColumnDef.isSwitchOnChange()) {
                            final String cId = chWidget.isBindEventsToFacade() ? chWidget.getFacadeId()
                                    : chWidget.getId();
                            if (switchOnChangeHandlers != null) {
                                for (EventHandler eventHandler : switchOnChangeHandlers) {
                                    writer.writeBehavior(eventHandler, cId, tabelColumnDef.getFieldName());
                                }
                            }
                            
                            if (switchOnChangeHandler != null) {
                                writer.writeBehavior(switchOnChangeHandler, cId, null);
                            }
                        }

                        if (isContainerEditable && tableWidget.isInputWidget(chWidget)) {
                            addPageAlias(tableWidgetId, chWidget);
                        }

                        index++;
                    }
                }

                if (isActionColumn) {
                    int _index = table.resolveActionIndex(valueStore, i, len);
                    Control _actionCtrl = actionCtrl[_index];
                    _actionCtrl.setValueStore(valueStore);
                    writer.writeBehavior(actionHandler[_index], _actionCtrl.getId(), null);
                }

                StandalonePanel summaryPanel = tableWidget.getSummaryPanel(i);
                if (summaryPanel != null) {
                    summaryPanel.setValueStore(valueStore);
                    try {
                        valueStore.setDataPrefix(summaryPanel.getId());
                        writer.writeBehavior(summaryPanel);
                        addPageAlias(tableWidget.getId(), summaryPanel);
                        summaryPanel.addPageAliases();
                    } finally {
                        valueStore.setDataPrefix(null);
                    }
                }
            }

            if (isRowAction) {
                for (EventHandler eventHandler : crudActionHandlers) {
                    writer.writeBehavior(eventHandler, tableWidget.getRowId(), null);
                }
            }

            final boolean supportSelect = !table.isFixedAssignment();
            if (isContainerEditable && table.isEntryMode()) {
                getRequestContextUtil().addOnSaveContentWidget(tableWidgetId);
            }

            // Append table rigging
            writer.beginFunction("fux.rigTable");
            writer.writeParam("pId", tableWidgetId);
            writer.writeParam("pContId", tableWidget.getContainerId());
            writer.writeParam("pRowId", tableWidget.getRowId());
            writer.writeCommandURLParam("pCmdURL");
            if (supportSelect && multiSelect) {
                writer.writeParam("pSelAllId", tableWidget.getSelectAllId());
                writer.writeParam("pSelCtrlId", tableWidget.getSelectCtrl().getId());
                writer.writeParam("pMultiSel", true);
                writer.writeParam("pMultiSelDepList",
                        DataUtils.toArray(String.class, tableWidget.getMultiSelDependentList()));
            }

            writer.writeParam("pRefPanels", table.getRefreshPanelIds());

            boolean sortable = tableDef.isSortable() && table.getNumberOfPages() > 0;
            if (sortable) {
                writer.writeParam("pSortIndexId", tableWidget.getSortColumnCtrl().getId());
                writer.writeParam("pColHeaderId", tableWidget.getColumnHeaderId());
                writer.writeParam("pColCount", tableDef.getColumnCount());
            }

            if (table.getTotalItemCount() <= 0) {
                writer.writeParam("pConDepList",
                        DataUtils.toArray(String.class, tableWidget.getContentDependentList()));
            }
            writer.endFunction();
        }
    }

    private void writeHeaderRow(ResponseWriter writer, AbstractTableWidget<?, ?, ?> tableWidget) throws UnifyException {
        writer.write("<tr>");
        final AbstractTable<?, ?> table = tableWidget.getTable();
        if (table != null) {
            final TableDef tableDef = table.getTableDef();
            final boolean entryMode = table.isEntryMode();
            final boolean supportSelect = !table.isFixedAssignment();
            final boolean multiSelect = tableDef.isMultiSelect() || tableWidget.isMultiSelect();
            if (supportSelect && multiSelect && !entryMode) {
                writeHeaderMultiSelect(writer, tableWidget);
            }

            if (tableDef.isSerialNo()) {
                writer.write("<th class=\"mserialh\"><span>");
                writer.write(getSessionMessage("tablewidget.serialno"));
                writer.write("</span></th>");
            }

            final boolean sysHeaderUppercase = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ALL_TABLE_HEADER_TO_UPPERCASE);
            final boolean sysHeaderCenterAlign = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ALL_TABLE_HEADER_CENTER_ALIGNED);
            final boolean sortable = tableDef.isSortable() && tableWidget.getTable().getNumberOfPages() > 0;
            String columnHeaderId = tableWidget.getColumnHeaderId();
            int index = 0;
            for (ChildWidgetInfo widgetInfo : tableWidget.getChildWidgetInfos()) {
                if (widgetInfo.isExternal() && widgetInfo.isControl()) {
                    TableColumnDef tabelColumnDef = tableDef.getColumnDef(index);
                    writer.write("<th");
                    if (sysHeaderCenterAlign || tableDef.isHeaderCenterAlign()) {
                        writeTagStyle(writer, tabelColumnDef.getHeaderStyle() + "text-align:center;");
                    } else {
                        writeTagStyle(writer, tabelColumnDef.getHeaderStyle());
                    }
                    writer.write("><span ");
                    boolean appendSortedSym = false;
                    if (sortable && tabelColumnDef.isSortable()) {
                        writer.write("id = \"").write(columnHeaderId).write(index).write("\"");
                        if (appendSortedSym = (index == tableWidget.getSortColumnIndex())) {
                            writer.write(" class = \"sorted g_fsm\"");
                        } else {
                            writer.write(" class = \"sort\"");
                        }
                    }

                    String caption = tableDef.getFieldLabel(index);
                    if (caption != null && (sysHeaderUppercase || tableDef.isHeaderToUpperCase())) {
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

                    if (appendSortedSym) {
                        writer.write("&nbsp;");
                        if (OrderType.ASCENDING.equals(tableWidget.getSortType())) {
                            writer.write(resolveSymbolHtmlHexCode("caret-up"));
                        } else {
                            writer.write(resolveSymbolHtmlHexCode("caret-down"));
                        }
                    }
                    writer.write("</span>");
                    writer.write("</th>");
                    index++;
                }
            }

            if (supportSelect && multiSelect && entryMode) {
                writeHeaderMultiSelect(writer, tableWidget);
            }

            if (tableWidget.isActionColumn()) {
                writer.write("<th  class=\"mactionh\">");
                writer.write("</th>");
            }

            writer.write("</tr>");
        }
    }

    private void writeHeaderMultiSelect(ResponseWriter writer, AbstractTableWidget<?, ?, ?> tableWidget)
            throws UnifyException {
        writer.write("<th class=\"mselh\">");
        writer.write("<span");
        writeTagId(writer, "fac_" + tableWidget.getSelectAllId());
        if (tableWidget.isContainerDisabled()) {
            writeTagStyleClass(writer, "g_cbd");
        } else {
            writeTagStyleClass(writer, "g_cbb");
        }
        writer.write("/>");
        writer.write("<input type=\"checkbox\"");
        writeTagId(writer, tableWidget.getSelectAllId());
        writer.write("/>");
        writer.write("</th>");
    }

    private void writeBodyRows(ResponseWriter writer, AbstractTableWidget<?, ?, ?> tableWidget) throws UnifyException {
        final AbstractTable<?, ?> table = tableWidget.getTable();
        if (table != null) {
            final boolean entryMode = table.isEntryMode();
            final boolean supportSelect = !table.isFixedAssignment();
            final int pageIndex = table.getDispStartIndex() + 1;
            final TableDef tableDef = table.getTableDef();
            final boolean isSerialNo = tableDef.isSerialNo();
            final boolean totalSummary = table.isTotalSummary();
            final boolean isActionColumn = tableWidget.isActionColumn();
            final boolean multiSelect = tableDef.isMultiSelect() || tableWidget.isMultiSelect();
            table.clearSummaries();

            List<ValueStore> valueList = tableWidget.getValueList();
            int len = valueList.size();
            if (len == 0) {
                writer.write("<tr class=\"even\">");
                int skip = 0;
                if (!entryMode && multiSelect) {
                    writer.write("<td class=\"mseld\"><span></span></td>");
                    skip++;
                }

                if (isSerialNo) {
                    writer.write("<td class=\"mseriald\"><span></span></td>");
                    skip++;
                }

                if (tableWidget.isSummary()) {
                    skip++;
                }

                writer.write("<td colspan=\"");
                writer.write(tableWidget.getChildWidgetInfos().size() - skip);
                writer.write("\"><span class=\"mnorec\" style=\"display:block;text-align:center;\">");
                writer.write(resolveSessionMessage("$m{tablewidget.norecordsfound}"));
                writer.write("</span></td>");
                writer.write("</tr>");
            } else {
                final Control[] actionCtrl = tableWidget.getActionCtrl();
                final boolean isRowAction = !DataUtils.isBlank(table.getCrudActionHandlers())
                        && !tableWidget.isActionColumn();
                final boolean rowColors = tableDef.isRowColorFilters();
                final Date now = table.getAu().getNow();
                final SpecialParamProvider specialParamProvider = table.getAu().getSpecialParamProvider();
                final String even = isRowAction ? "even pnt" : "even";
                final String odd = isRowAction ? "odd pnt" : "odd";
                final int highlightRow = table.getHighlightedRow();
                final EntryTableMessage entryMessage = table.getEntryMessage();
                TableStateOverride tableStateOverride = null;
                for (int i = 0; i < len; i++) {
                    ValueStore valueStore = valueList.get(i);
                    if (entryMode) {
                        tableStateOverride = table.getTableStateOverride(valueStore);
                    }

                    // Normal row
                    Long id = valueStore.retrieve(Long.class, "id");
                    writer.write("<tr");
                    if (i == highlightRow) {
                        writeTagStyleClass(writer, MessageType.INFO.styleClass());
                    } else if (entryMessage != null && entryMessage.isMessagePresent()
                            && entryMessage.isRowReferred(i)) {
                        writeTagStyleClass(writer, entryMessage.getMessageType().styleClass());
                    } else if (i % 2 == 0) {
                        writeTagStyleClass(writer, even);
                    } else {
                        writeTagStyleClass(writer, odd);
                    }

                    writeTagName(writer, tableWidget.getRowId());

                    if (rowColors) {
                        for (TableFilterDef tableFilterDef : tableDef.getRowColorFilterList()) {
                            if (tableFilterDef.getFilterDef()
                                    .getObjectFilter(tableDef.getEntityDef(), specialParamProvider, now)
                                    .match(valueStore)) {
                                writer.write(" style=\"background-color:");
                                writer.write(tableFilterDef.getRowColor());
                                writer.write(";\"");
                                break;
                            }
                        }
                    }

                    writer.write(">");

                    if (supportSelect && multiSelect && !entryMode) {
                        writeRowMultiSelect(writer, tableWidget, id, i);
                    }

                    if (isSerialNo) {
                        writer.write("<td class=\"mseriald\"><span>");
                        writer.write(pageIndex + i);
                        writer.write(".</span></td>");
                    }

                    int index = 0;
                    for (ChildWidgetInfo widgetInfo : tableWidget.getChildWidgetInfos()) {
                        if (widgetInfo.isExternal() && widgetInfo.isControl()) {
                            TableColumnDef tabelColumnDef = tableDef.getColumnDef(index);
                            String fieldName = tabelColumnDef.getFieldName();

                            Widget chWidget = widgetInfo.getWidget();
                            if (entryMode) {
                                chWidget.setEditable(
                                        tableStateOverride.isColumnEditable(fieldName, tabelColumnDef.isEditable()));
                                chWidget.setDisabled(
                                        tableStateOverride.isColumnDisabled(fieldName, tabelColumnDef.isDisabled()));
                                if (i == 0) {
                                    chWidget.initValueStoreMemory(len);
                                    widgetInfo.setName(fieldName);
                                }
                            } else {
                                chWidget.setEditable(tabelColumnDef.isEditable());
                                chWidget.setDisabled(tabelColumnDef.isDisabled());
                            }

                            
                            chWidget.setValueStore(valueStore);
                            writer.write("<td");
                            writeTagStyle(writer, chWidget.getColumnStyle());
                            writer.write(">");
                            writer.writeStructureAndContent(chWidget);
                            if (entryMode) {
                                writeTargetHidden(writer, chWidget.getId(), i);
                            }
                            writer.write("</td>");

                            if (totalSummary) {
                                table.addTotalSummary(fieldName, valueStore.retrieve(fieldName));
                            }

                            index++;
                        }
                    }

                    if (supportSelect && multiSelect && entryMode) {
                        writeRowMultiSelect(writer, tableWidget, id, i);
                    }

                    if (isActionColumn) {
                        writer.write("<td>");
                        int _index = table.resolveActionIndex(valueStore, i, len);
                        Control _actionCtrl = actionCtrl[_index];
                        _actionCtrl.setValueStore(valueStore);
                        writer.writeStructureAndContent(_actionCtrl);
                        writer.write("</td>");
                    }

                    writer.write("</tr>");

                    // Summary
                    StandalonePanel summaryPanel = tableWidget.getSummaryPanel(i);
                    if (summaryPanel != null) {
                        writer.write("<tr>");
                        int skip = 0;
                        if (supportSelect && !entryMode) {
                            writer.write("<td class=\"mseld\"></td>");
                            skip++;
                        }

                        if (isSerialNo) {
                            writer.write("<td class=\"mseriald\"></td>");
                            skip++;
                        }

                        if (tableWidget.isSummary()) {
                            skip++;
                        }

                        writer.write("<td colspan=\"");
                        writer.write(tableWidget.getChildWidgetInfos().size() - skip);
                        writer.write("\">");
                        summaryPanel.setValueStore(valueStore);
                        try {
                            valueStore.setDataPrefix(summaryPanel.getId());
                            writer.writeStructureAndContent(summaryPanel);
                        } finally {
                            valueStore.setDataPrefix(null);
                        }
                        writer.write("</td>");

                        if (supportSelect && entryMode) {
                            writer.write("<td class=\"mseld\"></td>");
                        }
                        writer.write("</tr>");
                    }
                }
            }

            // Total summary
            if (totalSummary) {
                table.loadTotalSummaryValueStore();
                ValueStore totalSummaryValueStore = table.getTableTotalSummary().getTotalSummaryValueStore();

                writer.write("<tr class=\"total\">");
                if (!entryMode && multiSelect) {
                    writer.write("<td class=\"mseld\"><span></span></td>");
                }

                if (isSerialNo) {
                    writer.write("<td class=\"mseriald\"><span></span></td>");
                }

                int index = 0;
                for (ChildWidgetInfo widgetInfo : tableWidget.getChildWidgetInfos()) {
                    if (widgetInfo.isExternal() && widgetInfo.isControl()) {
                        TableColumnDef tabelColumnDef = tableDef.getColumnDef(index);
                        Widget chWidget = table.getSummaryWidget(tabelColumnDef.getFieldName());
                        if (chWidget != null) {
                            chWidget.setEditable(false);
                            chWidget.setValueStore(totalSummaryValueStore);
                            writer.write("<td");
                            writeTagStyle(writer, chWidget.getColumnStyle());
                            writer.write(">");
                            writer.writeStructureAndContent(chWidget);
                            writer.write("</td>");
                        } else {
                            writer.write("<td class=\"blank\">");
                            if (table.isTotalLabelColumn(tabelColumnDef.getFieldName())) {
                                writer.writeWithHtmlEscape(table.getTotalLabel());
                            }

                            writer.write("</td>");
                        }

                        index++;
                    }
                }

                if (entryMode && multiSelect) {
                    writer.write("<td class=\"mseld\"><span></span></td>");
                }

                writer.write("</tr>");
            }
        }
    }

    public void writeRowMultiSelect(ResponseWriter writer, AbstractTableWidget<?, ?, ?> tableWidget, Long id, int index)
            throws UnifyException {
        final Control selectCtrl = tableWidget.getSelectCtrl();
        final boolean selected = tableWidget.getTable().isSelected(index);
        writer.write("<td class=\"mseld\">");
        String namingIndexId = selectCtrl.getNamingIndexedId(index);
        writer.write("<span ");
        writeTagId(writer, "fac_" + namingIndexId);
        if (tableWidget.isContainerDisabled()) {
            if (selected) {
                writeTagStyleClass(writer, "g_cbc");
            } else {
                writeTagStyleClass(writer, "g_cbd");
            }
        } else {
            if (selected) {
                writeTagStyleClass(writer, "g_cba");
            } else {
                writeTagStyleClass(writer, "g_cbb");
            }
        }

        writer.write("/>");
        writer.write("<input type=\"checkbox\"");
        writeTagId(writer, namingIndexId);
        writeTagName(writer, selectCtrl.getId());
        writeTagValue(writer, index);
        if (selected) {
            writer.write(" checked=\"checked\"");
        }
        writer.write("/>");
        writer.write("</span>");
        writer.write("</td>");
    }
}
