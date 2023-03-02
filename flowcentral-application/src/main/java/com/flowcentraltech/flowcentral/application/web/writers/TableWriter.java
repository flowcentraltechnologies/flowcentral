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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.TableFilterDef;
import com.flowcentraltech.flowcentral.application.web.panels.SummaryPanel;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable.Section;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTableWidget;
import com.flowcentraltech.flowcentral.application.web.widgets.TableSummaryLine;
import com.flowcentraltech.flowcentral.common.business.policies.FixedRowActionType;
import com.flowcentraltech.flowcentral.common.business.policies.TableStateOverride;
import com.flowcentraltech.flowcentral.common.data.EntryTableMessage;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.flowcentraltech.flowcentral.common.web.panels.DetailsPanel;
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
import com.tcdng.unify.web.ui.widget.WriteWork;
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
            final boolean isContainerEditable = tableWidget.isContainerEditable();
            TableDef tableDef = table.getTableDef();
            final boolean multiSelect = tableDef.isMultiSelect() || tableWidget.isMultiSelect();
            if (multiSelect) {
                writeHiddenPush(writer, tableWidget.getSelectCtrl(), PushType.CHECKBOX);
            }

            if (tableWidget.isFocusManagement()) {
                WriteWork work = tableWidget.getWriteWork();
                work.set("focusWidgetId", tableWidget.getTabMemoryId());
                tableWidget.setTabMemoryId(null);
                writer.writeStructureAndContent(tableWidget.getTabMemCtrl());
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
            if (StringUtils.isBlank(errMsg)) {
                errMsg = (String) getRequestAttribute(AppletRequestAttributeConstants.SILENT_FORM_ERROR_MSG);
            }

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
                        TableColumnDef tabelColumnDef = tableDef.getVisibleColumnDef(index);
                        writer.write("<col ");
                        writeTagStyle(writer, tabelColumnDef.getHeaderStyle());
                        writer.write(">");
                        index++;
                    }
                }

                if (supportSelect && multiSelect && entryMode) {
                    writer.write("<col class=\"cselh\">");
                }

                if (isContainerEditable) {
                    if (tableWidget.isFixedRows()) {
                        writer.write("<col class=\"cfixedh\">");
                    } else if (tableWidget.isActionColumn()) {
                        writer.write("<col class=\"cactionh\">");
                    }
                }

                writer.write("</colgroup>");
            } else {
                // Header
                writeHeaderRow(writer, tableWidget);
            }

            tableWidget.setActionBindings();
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
            final boolean entryMode = table.isEntryMode();
            final int detailsIndex = table.getDetailsIndex();
            final boolean details = tableWidget.isDetails() && detailsIndex >= 0;
            final boolean multiSelect = tableDef.isMultiSelect() || tableWidget.isMultiSelect();
            final List<EventHandler> switchOnChangeHandlers = table.getSwitchOnChangeHandlers();
            final EventHandler switchOnChangeHandler = tableWidget.getSwitchOnChangeHandler();
            final List<EventHandler> crudActionHandlers = table.getCrudActionHandlers();
            final EventHandler[] actionHandler = tableWidget.getActionEventHandler();
            final boolean isFixedRows = isContainerEditable && tableWidget.isFixedRows();
            final boolean isActionColumn = isContainerEditable && tableWidget.isActionColumn();
            final boolean focusManagement = tableWidget.isFocusManagement();
            final boolean isCrudMode = tableWidget.isCrudMode();
            final Control[] fixedCtrl = isFixedRows ? tableWidget.getFixedCtrl() : null;
            final Control[] actionCtrl = tableWidget.getActionCtrl();
            final RowChangeInfo lastRowChangeInfo = focusManagement ? table.getLastRowChangeInfo() : null;

            List<String> tabWidgetIds = focusManagement ? new ArrayList<String>() : null;
            List<CommandInfo> switchCmdList = null;
            WriteWork work = tableWidget.getWriteWork();
            String focusWidgetId = focusManagement ? (String) work.get("focusWidgetId") : null;
            TableStateOverride[] tableStateOverride = (TableStateOverride[]) work.get("overrides");

            Control _crudCtrl = null;
            if (isCrudMode) {
                _crudCtrl = table.isView() ? tableWidget.getViewCtrl() : tableWidget.getEditCtrl();
            }

            List<ValueStore> valueList = tableWidget.getValueList();
            int len = valueList.size();
            for (int i = 0; i < len; i++) {
                ValueStore valueStore = valueList.get(i);
                boolean matchRowFocus = focusWidgetId == null && lastRowChangeInfo != null
                        && lastRowChangeInfo.matchRowIndex(i);
                int index = 0;
                for (ChildWidgetInfo widgetInfo : tableWidget.getChildWidgetInfos()) {
                    if (widgetInfo.isExternal() && widgetInfo.isControl()) {
                        TableColumnDef tabelColumnDef = tableDef.getVisibleColumnDef(index);
                        String fieldName = tabelColumnDef.getFieldName();
                        Widget chWidget = widgetInfo.getWidget();
                        if (entryMode) {
                            chWidget.setEditable(
                                    tableStateOverride[i].isColumnEditable(fieldName, tabelColumnDef.isEditable()));
                            chWidget.setDisabled(
                                    tableStateOverride[i].isColumnDisabled(fieldName, tabelColumnDef.isDisabled()));
                        } else {
                            chWidget.setEditable(tabelColumnDef.isEditable());
                            chWidget.setDisabled(tabelColumnDef.isDisabled());
                        }

                        chWidget.setValueStore(valueStore);
                        writer.writeBehavior(chWidget);

                        if (isContainerEditable && chWidget.isEditable() && !chWidget.isDisabled()) {
                            final String cId = chWidget.isBindEventsToFacade() ? chWidget.getFacadeId()
                                    : chWidget.getId();
                            if (focusManagement) {
                                tabWidgetIds.add(cId);
                            }

                            if (tabelColumnDef.isSwitchOnChange()) {
                                if (switchOnChangeHandlers != null) {
                                    for (EventHandler eventHandler : switchOnChangeHandlers) {
                                        writer.writeBehavior(eventHandler, cId, fieldName);
                                    }
                                }

                                if (switchOnChangeHandler != null) {
                                    writer.writeBehavior(switchOnChangeHandler, cId, null);
                                }

                                if (switchOnChangeHandler == null && switchOnChangeHandlers == null) {
                                    if (switchCmdList == null) {
                                        switchCmdList = new ArrayList<CommandInfo>();
                                    }

                                    switchCmdList.add(new CommandInfo(cId, fieldName));
                                }

                                if (matchRowFocus && lastRowChangeInfo.matchTrigger(fieldName)) {
                                    focusWidgetId = cId;
                                    matchRowFocus = false;
                                }
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

                if (isCrudMode) {
                    _crudCtrl.setValueStore(valueStore);
                    for (EventHandler eventHandler : crudActionHandlers) {
                        writer.writeBehavior(eventHandler, _crudCtrl.getId(), null);
                    }
                }

                // Details
                if (details && detailsIndex == i) {
                    DetailsPanel detailsPanel = tableWidget.getDetailsPanel();
                    if (detailsPanel != null) {
                        writer.writeBehavior(detailsPanel);
                    }
                }

                // Summary
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
            if (focusWidgetId != null) {
                writer.writeParam("pFocusId", focusWidgetId);
            }

            if (switchCmdList != null) {
                writer.writeObjectParam("pSwhCmdList", DataUtils.toArray(CommandInfo.class, switchCmdList));
            }

            if (focusManagement) {
                writer.writeParam("pTabMemId", tableWidget.getTabMemCtrl().getId());
                writer.writeParam("pTabWidId", tabWidgetIds.toArray(new String[tabWidgetIds.size()]));
            }

            if (supportSelect && multiSelect) {
                writer.writeParam("pSelAllId", tableWidget.getSelectAllId());
                writer.writeParam("pSelCtrlId", tableWidget.getSelectCtrl().getId());
                writer.writeParam("pSelCount", table.getSelectedCount());
                writer.writeParam("pMultiSel", true);
                writer.writeParam("pMultiSelLink", table.isMultiSelectDetailLinked() && tableWidget.isDetails());
                writer.writeParam("pMultiSelDepList",
                        DataUtils.toArray(String.class, tableWidget.getMultiSelDependentList()));
            }

            writer.writeParam("pRefPanels", table.getRefreshPanelIds());

            if (isFixedRows) {
                writer.writeParam("pFixedRows", true);
                writer.writeParam("pfExcCtrlId", fixedCtrl[FixedRowActionType.REMOVE.index()].getGroupId());
                writer.writeParam("pfIncCtrlId", fixedCtrl[FixedRowActionType.ATTACH.index()].getGroupId());
                writer.writeParam("pfDelCtrlId", fixedCtrl[FixedRowActionType.DELETE.index()].getGroupId());
            }

            boolean sortable = tableDef.isSortable() && table.getNumberOfPages() > 0;
            if (sortable) {
                writer.writeParam("pSortIndexId", tableWidget.getSortColumnCtrl().getId());
                writer.writeParam("pColHeaderId", tableWidget.getColumnHeaderId());
                writer.writeParam("pColCount", tableDef.getVisibleColumnCount());
            }

            if (table.getTotalItemCount() <= 0) {
                writer.writeParam("pConDepList",
                        DataUtils.toArray(String.class, tableWidget.getContentDependentList()));
            }
            writer.endFunction();
        }
    }

    public class CommandInfo {
        private final String cId;

        private final String cmdTag;

        public CommandInfo(String cId, String cmdTag) {
            this.cId = cId;
            this.cmdTag = cmdTag;
        }

        public String getCId() {
            return cId;
        }

        public String getCmdTag() {
            return cmdTag;
        }
    }

    private void writeHeaderRow(ResponseWriter writer, AbstractTableWidget<?, ?, ?> tableWidget) throws UnifyException {
        writer.write("<tr>");
        final AbstractTable<?, ?> table = tableWidget.getTable();
        if (table != null) {
            final boolean isContainerEditable = tableWidget.isContainerEditable();
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
                    TableColumnDef tabelColumnDef = tableDef.getVisibleColumnDef(index);
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

            if (isContainerEditable) {
                if (tableWidget.isFixedRows()) {
                    writer.write("<th  class=\"mfixedh\"></th>");
                } else if (tableWidget.isActionColumn()) {
                    writer.write("<th  class=\"mactionh\"></th>");
                }
            }

            if (tableWidget.isCrudMode()) {
                writer.write("<th  class=\"mcrudh\"></th>");
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
            final boolean isContainerEditable = tableWidget.isContainerEditable();
            final boolean entryMode = table.isEntryMode();
            final boolean supportSelect = !table.isFixedAssignment();
            final int pageIndex = table.getDispStartIndex() + 1;
            final TableDef tableDef = table.getTableDef();
            final boolean isSerialNo = tableDef.isSerialNo();
            final boolean totalSummary = table.isTotalSummary();
            final boolean isFixedRows = isContainerEditable && tableWidget.isFixedRows();
            final boolean isActionColumn = isContainerEditable && tableWidget.isActionColumn();
            final boolean multiSelect = tableDef.isMultiSelect() || tableWidget.isMultiSelect();
            table.clearSummaries();

            List<ValueStore> valueList = tableWidget.getValueList();
            final int len = valueList.size();
            if (len == 0) {
                writeNoRecordsFoundRow(writer, tableWidget, entryMode, multiSelect, isSerialNo);
            } else {
                final Control[] fixedCtrl = isFixedRows ? tableWidget.getFixedCtrl() : null;
                final Control[] actionCtrl = tableWidget.getActionCtrl();
                final int detailsIndex = table.getDetailsIndex();
                final boolean details = tableWidget.isDetails() && detailsIndex >= 0;
                final boolean alternatingRows = tableWidget.isAlternatingRows();
                final boolean isRowAction = !DataUtils.isBlank(table.getCrudActionHandlers())
                        && !tableWidget.isFixedRows() && !tableWidget.isActionColumn();
                final boolean rowColors = tableDef.isRowColorFilters();
                final boolean isCrudMode = tableWidget.isCrudMode();
                final Date now = table.au().getNow();
                final String even = isRowAction ? "even pnt" : "even";
                final String odd = isRowAction ? "odd pnt" : "odd";
                final int highlightRow = table.getHighlightedRow();
                final EntryTableMessage entryMessage = table.getEntryMessage();
                TableStateOverride[] tableStateOverride = entryMode ? new TableStateOverride[len] : null;
                WriteWork work = tableWidget.getWriteWork();
                work.set("overrides", tableStateOverride);

                Control _crudCtrl = null;
                if (isCrudMode) {
                    _crudCtrl = table.isView() ? tableWidget.getViewCtrl() : tableWidget.getEditCtrl();
                }

                final List<Section> sections = table.getSections();
                final int slen = sections.size();
                final boolean sectionHeaders = !sections.isEmpty();
                Section currentSection = null;
                int sectionIndex = -1;
                for (int i = 0; i < len; i++) {
                    ValueStore valueStore = valueList.get(i);
                    valueStore.setTempValue("parentReader", table.getParentReader());
                    if (entryMode) {
                        tableStateOverride[i] = table.getTableStateOverride(valueStore);
                    }

                    // Section header
                    if (sectionHeaders) {
                        while ((currentSection == null || !currentSection.isIndexWithin(i))
                                && ((++sectionIndex) < slen)) {
                            currentSection = sections.get(sectionIndex);
                            if (sectionIndex > 0) {
                                writer.write("<tr><td class=\"sfooter\" colspan=\"100%\"><span>");
                                writer.write("</span></td></tr>");
                            }

                            writer.write("<tr><td class=\"sheader\" colspan=\"100%\"><span>");
                            writer.writeWithHtmlEscape(currentSection.getLabel());
                            writer.write("</span></td></tr>");
                        }
                    }

                    // Normal row
                    String summaryClass = null;
                    String summaryColor = null;
                    Long id = valueStore.retrieve(Long.class, "id");
                    writer.write("<tr");
                    if (i == highlightRow) {
                        writeTagStyleClass(writer, MessageType.INFO.styleClass());
                    } else if (entryMessage != null && entryMessage.isMessagePresent()
                            && entryMessage.isRowReferred(i)) {
                        writeTagStyleClass(writer, entryMessage.getMessageType().styleClass());
                    } else if (alternatingRows) {
                        if (i % 2 == 0) {
                            summaryClass = even;
                            writeTagStyleClass(writer, even);
                        } else {
                            summaryClass = odd;
                            writeTagStyleClass(writer, odd);
                        }
                    } else if (isRowAction) {
                        writeTagStyleClass(writer, "pnt");
                    }

                    writeTagName(writer, tableWidget.getRowId());

                    if (rowColors) {
                        for (TableFilterDef tableFilterDef : tableDef.getRowColorFilterList()) {
                            if (tableFilterDef.getFilterDef()
                                    .getObjectFilter(tableDef.getEntityDef(), valueStore.getReader(), now)
                                    .match(valueStore)) {
                                writer.write(" style=\"background-color:");
                                writer.write(summaryColor = tableFilterDef.getRowColor());
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
                            TableColumnDef tabelColumnDef = tableDef.getVisibleColumnDef(index);
                            String fieldName = tabelColumnDef.getFieldName();

                            Widget chWidget = widgetInfo.getWidget();
                            if (entryMode) {
                                chWidget.setEditable(
                                        tableStateOverride[i].isColumnEditable(fieldName, tabelColumnDef.isEditable()));
                                chWidget.setDisabled(
                                        tableStateOverride[i].isColumnDisabled(fieldName, tabelColumnDef.isDisabled()));
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
                            if (tabelColumnDef.isHiddenOnNull()) {
                                if (chWidget.getValue() != null) {
                                    writer.writeStructureAndContent(chWidget);
                                }
                            } else {
                                writer.writeStructureAndContent(chWidget);
                            }

                            if (entryMode) {
                                writeTargetHidden(writer, chWidget.getId(), i);
                            }

                            writer.write("</td>");

                            if (totalSummary) {
                                table.addTableColumnSummary(fieldName, valueStore);
                            }

                            index++;
                        }
                    }

                    if (supportSelect && multiSelect && entryMode) {
                        writeRowMultiSelect(writer, tableWidget, id, i);
                    }

                    if (isFixedRows) {
                        writer.write("<td>");
                        FixedRowActionType fixedType = table.resolveFixedIndex(valueStore, i, len);
                        if (!fixedType.fixed()) {
                            Control _fixedCtrl = fixedCtrl[fixedType.index()];
                            _fixedCtrl.setValueStore(valueStore);
                            writer.writeStructureAndContent(_fixedCtrl);
                        }
                        writer.write("</td>");
                    } else if (isActionColumn) {
                        writer.write("<td>");
                        int _index = table.resolveActionIndex(valueStore, i, len);
                        Control _actionCtrl = actionCtrl[_index];
                        _actionCtrl.setValueStore(valueStore);
                        writer.writeStructureAndContent(_actionCtrl);
                        writer.write("</td>");
                    }

                    if (isCrudMode) {
                        writer.write("<td>");
                        _crudCtrl.setValueStore(valueStore);
                        writer.writeStructureAndContent(_crudCtrl);
                        writer.write("</td>");
                    }
                    writer.write("</tr>");

                    // Details
                    if (details && detailsIndex == i) {
                        DetailsPanel detailsPanel = tableWidget.getDetailsPanel();
                        if (detailsPanel != null) {
                            writer.write("<tr");
                            writer.write(">");

                            int skip = 0;
                            if (supportSelect && multiSelect && !entryMode) {
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
                            detailsPanel.loadDetails(valueStore);
                            writer.writeStructureAndContent(detailsPanel);
                            writer.write("</td>");

                            if (supportSelect && multiSelect && entryMode) {
                                writer.write("<td class=\"mseld\"></td>");
                            }
                            writer.write("</tr>");
                        }
                    }

                    // Summary
                    SummaryPanel summaryPanel = tableWidget.getSummaryPanel(i);
                    if (summaryPanel != null) {
                        writer.write("<tr");
                        if (summaryClass != null) {
                            writeTagStyleClass(writer, summaryClass);
                        }

                        if (summaryColor != null) {
                            writer.write(" style=\"background-color:");
                            writer.write(summaryColor);
                            writer.write(";\"");
                        }
                        writer.write(">");

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
                if (!table.isWithTableSummaryLines()) {
                    table.addTotalTableSummaryLine();
                }
                
                for (TableSummaryLine summaryLine : table.getTableSummaryLines()) {
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
                            TableColumnDef tabelColumnDef = tableDef.getVisibleColumnDef(index);
                            Widget chWidget = table.getSummaryWidget(tabelColumnDef.getFieldName());
                            if (chWidget != null) {
                                chWidget.setEditable(false);
                                chWidget.setValueStore(summaryLine.getValueStore());
                                writer.write("<td");
                                writeTagStyle(writer, chWidget.getColumnStyle());
                                writer.write(">");
                                writer.writeStructureAndContent(chWidget);
                                writer.write("</td>");
                            } else {
                                writer.write("<td class=\"blank\">");
                                if (table.isTotalLabelColumn(tabelColumnDef.getFieldName())) {
                                    writer.writeWithHtmlEscape(summaryLine.getLabel());
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
    }

    private void writeNoRecordsFoundRow(ResponseWriter writer, AbstractTableWidget<?, ?, ?> tableWidget,
            boolean entryMode, boolean multiSelect, boolean isSerialNo) throws UnifyException {
        writeNonDataRow(writer, tableWidget, entryMode, multiSelect, isSerialNo, "even", "mnorec",
                resolveSessionMessage("$m{tablewidget.norecordsfound}"));
    }

    private void writeNonDataRow(ResponseWriter writer, AbstractTableWidget<?, ?, ?> tableWidget, boolean entryMode,
            boolean multiSelect, boolean isSerialNo, String rowClass, String styleClass, String text)
            throws UnifyException {
        writer.write("<tr class=\"").write(rowClass).write("\">");
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
        writer.write("\"><span class=\"").write(styleClass).write("\">");
        writer.writeWithHtmlEscape(text);
        writer.write("</span></td>");
        writer.write("</tr>");
    }

    private void writeRowMultiSelect(ResponseWriter writer, AbstractTableWidget<?, ?, ?> tableWidget, Long id,
            int index) throws UnifyException {
        final Control selectCtrl = tableWidget.getSelectCtrl();
        final boolean selected = tableWidget.getTable().isSelectedRow(index);
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
