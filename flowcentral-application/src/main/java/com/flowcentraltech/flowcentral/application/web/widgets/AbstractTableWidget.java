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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldTotalSummary;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.panels.SummaryPanel;
import com.flowcentraltech.flowcentral.common.business.policies.FixedRowActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.flowcentraltech.flowcentral.common.web.panels.DetailsPanel;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralValueListMultiControl;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.data.UniqueHistory;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.upl.UplElementReferences;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.util.DataTransferUtils;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Page;
import com.tcdng.unify.web.ui.widget.UploadControl;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.panel.StandalonePanel;

/**
 * Convenient abstract base class for table widgets.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplAttributes({
        @UplAttribute(name = "contentDependentList", type = UplElementReferences.class),
        @UplAttribute(name = "multiSelDependentList", type = UplElementReferences.class),
        @UplAttribute(name = "multiSelect", type = boolean.class),
        @UplAttribute(name = "detached", type = boolean.class),
        @UplAttribute(name = "actionSymbol", type = String[].class),
        @UplAttribute(name = "actionHandler", type = EventHandler[].class),
        @UplAttribute(name = "switchOnChangeHandler", type = EventHandler.class),
        @UplAttribute(name = "summary", type = String.class),
        @UplAttribute(name = "details", type = String.class),
        @UplAttribute(name = "uploadFlag", type = String.class),
        @UplAttribute(name = "viewButtonClass", type = String.class, defaultVal = "mbtn"),
        @UplAttribute(name = "viewButtonCaptionBinding", type = String.class, defaultVal = "viewButtonCaption"),
        @UplAttribute(name = "editButtonClass", type = String.class, defaultVal = "mbtn"),
        @UplAttribute(name = "editButtonCaptionBinding", type = String.class, defaultVal = "editButtonCaption"),
        @UplAttribute(name = "fixedRows", type = boolean.class, defaultVal = "false"),
        @UplAttribute(name = "alternatingRows", type = boolean.class, defaultVal = "true"),
        @UplAttribute(name = "focusManagement", type = boolean.class, defaultVal = "true") })
public abstract class AbstractTableWidget<T extends AbstractTable<V, U>, U, V>
        extends AbstractFlowCentralValueListMultiControl<ValueStore, U> implements TableSelect<U> {

    public static final int ATTACH_SELECTED_INDEX = 0;

    public static final int REMOVE_SELECTED_INDEX = 1;

    public static final int ATTACH_ALL_INDEX = 2;

    public static final int REMOVE_ALL_INDEX = 3;

    private Class<T> tableClass;

    private Class<U> itemClass;

    private T oldTable;

    private Control selectCtrl;

    private Control sortColumnCtrl;

    private Control tabMemCtrl;

    private Control[] actionCtrl;

    private Control[] fixedCtrl;

    private Control[] fixedMultiCtrl;

    private Control viewCtrl;

    private Control editCtrl;

    private UploadControl uploadCtrl;

    private List<SummaryPanel> summaryPanelList;

    private List<DetailsPanel> detailsPanelList;

    private String tabMemoryId;

    private Sort sort;

    private int sortColumnIndex;

    private List<String> contentDependentList;

    private List<String> multiSelDependentList;

    private UniqueHistory<Sort> sortHistory;

    private Set<Widget> inputs;

    private Map<String, Widget> renderers;

    public AbstractTableWidget(Class<T> tableClass, Class<U> itemClass) {
        this.tableClass = tableClass;
        this.itemClass = itemClass;
        this.sortColumnIndex = -1;
    }

    @Override
    public boolean isContainerDisabled() throws UnifyException {
        return super.isContainerDisabled() || isTableDisabled();
    }

    public boolean isNotViewOnlyAndIsContainerEditable() throws UnifyException {
        return super.isContainerEditable() && !isTableViewOnly();
    }

    @Override
    public void addPageAliases() throws UnifyException {
        if (selectCtrl != null) {
            addPageAlias(selectCtrl);
        }

        if (tabMemCtrl != null) {
            addPageAlias(tabMemCtrl);
        }
    }

    @Override
    public void populate(DataTransferBlock transferBlock) throws UnifyException {
        if (transferBlock != null) {
            DataTransferBlock childBlock = transferBlock.getChildBlock();
            ChildWidgetInfo childWidgetInfo = getChildWidgetInfo(childBlock.getId());
            if (childWidgetInfo.isPanel()) {
                StandalonePanel summaryPanel = (StandalonePanel) childWidgetInfo.getWidget();
                summaryPanel.setValueStore(getValueList().get(childBlock.getItemIndex()));
                summaryPanel.populate(childBlock);
            } else {
                Control control = (Control) childWidgetInfo.getWidget();
                if (control == selectCtrl) {
                    selectCtrl.populate(childBlock);
                } else if (control == tabMemCtrl) {
                    tabMemCtrl.populate(childBlock);
                } else if (control == sortColumnCtrl) {
                    sortColumnCtrl.populate(childBlock);
                } else if (control == uploadCtrl) {
                    childBlock = childBlock.getChildBlock();
                    uploadCtrl.setValueStore(getValueList().get(childBlock.getItemIndex()));
                    uploadCtrl.populate(childBlock);
                } else {
                    control.setValueStore(getValueList().get(childBlock.getItemIndex()));
                    control.populate(childBlock);
                }
            }
        }
    }

    @Action
    public void switchOnChange() throws UnifyException {
        int index = getRequestTarget(int.class);
        String trigger = getRequestCommandTag();
        T table = getTable();
        table.fireOnRowChange(new RowChangeInfo(trigger, index));
        FormValidationErrors errors = new FormValidationErrors();
        table.validate(EvaluationMode.SWITCH_ONCHANGE, errors);
        if (errors.isWithValidationErrors()) {
            StringBuilder sb = new StringBuilder();
            boolean appendSym = false;
            for (FormMessage msg : errors.getValidationErrors()) {
                if (appendSym) {
                    sb.append(" ");
                } else {
                    appendSym = true;
                }

                sb.append(msg.getMessage());
            }

            setRequestAttribute(AppletRequestAttributeConstants.SILENT_MULTIRECORD_SEARCH_ERROR_MSG, sb.toString());
        }
    }

    @Action
    public void sortColumn() throws UnifyException {
        if (oldTable != null) {
            TableDef tableDef = oldTable.getTableDef();
            if (sort == null || sort.getColumnIndex() != sortColumnIndex) {
                if (tableDef.isUseSortHistory()) {
                    if (sort != null) {
                        if (sortHistory == null) {
                            sortHistory = new UniqueHistory<Sort>(tableDef.getSortHistory());
                        }
                        sortHistory.add(sort);
                    }
                }
                sort = new Sort(sortColumnIndex);
            }

            sort.flip();

            Order order = new Order().add(tableDef.getVisibleColumnDef(sort.getColumnIndex()).getFieldName(),
                    sort.getSortType());
            if (sortHistory != null) {
                int index = sortHistory.size();
                while (--index >= 0) {
                    Sort oldSort = sortHistory.get(index);
                    order.add(tableDef.getVisibleColumnDef(oldSort.getColumnIndex()).getFieldName(),
                            oldSort.getSortType());
                }
            }

            oldTable.setOrder(order);
            oldTable.firstPage();
        }
    }

    @Action
    public void multiSelect() throws UnifyException {
        final int index = getRequestTarget(int.class);
        T table = getTable();
        if (table.isMultiSelectDetailLinked()) {
            table.setDetailsIndex(index);
        }
    }

    @Action
    public void exclude() throws UnifyException {
        applyFixedAction(FixedRowActionType.REMOVE);
    }

    @Action
    public void include() throws UnifyException {
        applyFixedAction(FixedRowActionType.ATTACH);
    }

    @Action
    public void delete() throws UnifyException {
        applyFixedAction(FixedRowActionType.DELETE);
    }

    @Action
    public void excludeSelect() throws UnifyException {
        applyFixedActionSelect(FixedRowActionType.REMOVE);
    }

    @Action
    public void excludeAll() throws UnifyException {
        applyFixedActionAll(FixedRowActionType.REMOVE);
    }

    @Action
    public void includeSelect() throws UnifyException {
        applyFixedActionSelect(FixedRowActionType.ATTACH);
    }

    @Action
    public void includeAll() throws UnifyException {
        applyFixedActionAll(FixedRowActionType.ATTACH);
    }

    public String resolveChildWidgetName(String transferId) throws UnifyException {
        String childId = DataTransferUtils.stripTransferDataIndexPart(transferId);
        ChildWidgetInfo childWidgetInfo = getChildWidgetInfo(childId);
        return childWidgetInfo.getName();
    }

    public List<String> getContentDependentList() throws UnifyException {
        if (contentDependentList == null) {
            contentDependentList = getPageNames(getUplAttribute(UplElementReferences.class, "contentDependentList"));
        }
        return contentDependentList;
    }

    public List<String> getMultiSelDependentList() throws UnifyException {
        if (multiSelDependentList == null) {
            multiSelDependentList = getPageNames(getUplAttribute(UplElementReferences.class, "multiSelDependentList"));
        }
        return multiSelDependentList;
    }

    public boolean isMultiSelect() throws UnifyException {
        return getUplAttribute(boolean.class, "multiSelect");
    }

    public boolean isDetached() throws UnifyException {
        return getUplAttribute(boolean.class, "detached");
    }

    public boolean isAlternatingRows() throws UnifyException {
        return getUplAttribute(boolean.class, "alternatingRows");
    }

    public boolean isFocusManagement() throws UnifyException {
        return getUplAttribute(boolean.class, "focusManagement");
    }

    public String getUploadFlag() throws UnifyException {
        return getUplAttribute(String.class, "uploadFlag");
    }

    public String getViewButtonCaptionBinding() throws UnifyException {
        return getUplAttribute(String.class, "viewButtonCaptionBinding");
    }

    public String getEditButtonCaptionBinding() throws UnifyException {
        return getUplAttribute(String.class, "editButtonCaptionBinding");
    }

    public boolean isFixedRows() throws UnifyException {
        T table = getTable();
        return (table != null && table.isFixedRows()) || getUplAttribute(boolean.class, "fixedRows");
    }

    public boolean isInCrudMode() throws UnifyException {
        T table = getTable();
        return table != null && table.isInCrudMode();
    }

    public boolean isUploadMode() throws UnifyException {
        T table = getTable();
        return table != null && table.isUploadCrudMode();
    }

    public boolean isUploadEnabled() throws UnifyException {
        T table = getTable();
        return table != null && table.isUploadEnabled();
    }

    public boolean isDetails() throws UnifyException {
        return !StringUtils.isBlank(getDetails());
    }

    public boolean isActionColumn() throws UnifyException {
        return actionCtrl != null && !isFixedRows();
    }

    public String getSelectAllId() throws UnifyException {
        return getPrefixedId("sela_");
    }

    public String getColumnHeaderId() throws UnifyException {
        return getPrefixedId("th_");
    }

    public String getRowId() throws UnifyException {
        return getPrefixedId("row_");
    }

    public boolean isTableDisabled() throws UnifyException {
        T table = getTable();
        return table != null && table.isDisabled();
    }

    public boolean isTableViewOnly() throws UnifyException {
        T table = getTable();
        return table != null && table.isViewOnly();
    }

    public Control[] getFixedCtrl() throws UnifyException {
        if (fixedCtrl == null) {
            List<Control> controls = new ArrayList<Control>();
            for (FixedRowActionType fixedType : FixedRowActionType.values()) {
                if (!fixedType.editable()) {
                    Control control = (Control) addInternalChildWidget(
                            "!ui-button alwaysValueIndex:true styleClass:$e{mbtn} caption:" + fixedType.label());
                    control.setGroupId(getPrefixedId(fixedType.prefix()));
                    controls.add(control);
                }
            }

            fixedCtrl = DataUtils.toArray(Control.class, controls);
            fixedCtrl[FixedRowActionType.FIXED.index()].setDisabled(true);
        }

        return fixedCtrl;
    }

    public Control[] getFixedMultiCtrl() throws UnifyException {
        if (fixedMultiCtrl == null) {
            List<Control> controls = new ArrayList<Control>();
            controls.add((Control) addInternalChildWidget(
                    "!ui-button styleClass:$e{mbtn mbtnr} caption:$m{table.attach.selected}"));

            controls.add((Control) addInternalChildWidget(
                    "!ui-button styleClass:$e{mbtn mbtnr} caption:$m{table.remove.selected}"));

            controls.add((Control) addInternalChildWidget(
                    "!ui-button styleClass:$e{mbtn mbtnr} caption:$m{table.attach.all}"));

            controls.add((Control) addInternalChildWidget(
                    "!ui-button styleClass:$e{mbtn mbtnr} caption:$m{table.remove.all}"));
            fixedMultiCtrl = DataUtils.toArray(Control.class, controls);
        }

        return fixedMultiCtrl;
    }

    public Control getViewCtrl() throws UnifyException {
        if (viewCtrl == null) {
            viewCtrl = (Control) addInternalChildWidget(
                    "!ui-button symbol:$s{file} alwaysValueIndex:true styleClass:$e{"
                            + getUplAttribute(String.class, "viewButtonClass") + " g_fsm} caption:"
                            + "$m{table.row.view} captionBinding:$s{"
                            + getUplAttribute(String.class, "viewButtonCaptionBinding") + "}");
        }

        return viewCtrl;
    }

    public Control getEditCtrl() throws UnifyException {
        if (editCtrl == null) {
            editCtrl = (Control) addInternalChildWidget(
                    "!ui-button symbol:$s{edit} alwaysValueIndex:true styleClass:$e{"
                            + getUplAttribute(String.class, "editButtonClass") + " g_fsm} caption:"
                            + "$m{table.row.edit} captionBinding:$s{"
                            + getUplAttribute(String.class, "editButtonCaptionBinding") + "}");
        }

        return editCtrl;
    }

    public UploadControl getUploadCtrl() throws UnifyException {
        if (uploadCtrl == null) {
            uploadCtrl = (UploadControl) addInternalChildWidget("!ui-fileuploadbutton styleClass:$s{"
                    + getUplAttribute(String.class, "viewButtonClass") + " g_fsm}");
        }

        return uploadCtrl;
    }

    public Control[] getActionCtrl() {
        return actionCtrl;
    }

    public boolean isSummary() {
        return summaryPanelList != null && !summaryPanelList.isEmpty();
    }

    public SummaryPanel getSummaryPanel(int index) {
        return isSummary() ? summaryPanelList.get(index) : null;
    }

    public boolean isExpandAllDetails() throws UnifyException {
        final T table = getValue(tableClass);
        return table != null && table.isExpandAllDetails();
    }

    public void validate(EvaluationMode evaluationMode, FormValidationErrors errors) throws UnifyException {
        List<ValueStore> valueList = getValueList();
        final int len = valueList.size();
        for (int i = 0; i < len; i++) {
            ValueStore valueStore = valueList.get(i);
            SummaryPanel summaryPanel = getSummaryPanel(i);
            if (summaryPanel != null) {
                summaryPanel.setValueStore(valueStore);
                summaryPanel.validate(evaluationMode, errors);
            }
        }
    }

    public DetailsPanel getDetailsPanel(int index) throws UnifyException {
        final List<ValueStore> valueList = super.getValueList();
        if (valueList == null) {
            detailsPanelList = null;
        } else {
            detailsPanelList = Collections.emptyList();
            final String details = getDetails();
            if (!StringUtils.isBlank(details)) {
                final T table = getValue(tableClass);
                detailsPanelList = new ArrayList<DetailsPanel>();
                if (table != null && table.isExpandAllDetails()) {
                    final int len = valueList.size();
                    for (int i = 0; i < len; i++) {
                        DetailsPanel detailsPanel = (DetailsPanel) addExternalChildStandalonePanel(details,
                                getId() + "_" + detailsPanelList.size() + "_dtl");
                        detailsPanelList.add(detailsPanel);
                    }
                } else {
                    DetailsPanel detailsPanel = (DetailsPanel) addExternalChildStandalonePanel(details,
                            getId() + "_dtl");
                    detailsPanelList.add(detailsPanel);
                }
            }
        }

        return detailsPanelList != null && detailsPanelList.size() > index ? detailsPanelList.get(index) : null;
    }

    public EventHandler[] getActionEventHandler() throws UnifyException {
        return getUplAttribute(EventHandler[].class, "actionHandler");
    }

    public EventHandler getSwitchOnChangeHandler() throws UnifyException {
        return getUplAttribute(EventHandler.class, "switchOnChangeHandler");
    }

    public T getTable() throws UnifyException {
        T table = getValue(tableClass);
        if (table != oldTable) {
            TableDef oldTableDef = null;
            if (oldTable != null) {
                oldTableDef = oldTable != null ? oldTable.getTableDef() : null;
            }

            TableDef tableDef = table != null ? table.getTableDef() : null;
            if (oldTableDef != tableDef) {
                if (selectCtrl == null && tableDef != null && tableDef.isMultiSelect()) {
                    selectCtrl = createInternalHiddenControl("selected");
                }

                clearRenderers();
                removeAllExternalChildWidgets();
                if (table != null) {
                    StandalonePanel standalonePanel = getStandalonePanel();
                    while (standalonePanel != null) {
                        if (standalonePanel instanceof Page) {
                            break;
                        }

                        standalonePanel = standalonePanel.getStandalonePanel();
                    }

                    final boolean entryMode = table.isEntryMode();
                    for (TableColumnDef tableColumnDef : tableDef.getVisibleColumnDefList()) {
                        final boolean useCellEditor = tableColumnDef.isWithCellEditor() && tableColumnDef.isEditable();
                        final boolean _entry = entryMode && useCellEditor;
                        final String columnWidgetUpl = _entry ? tableColumnDef.getCellEditor()
                                : (table.isDisableLinks() && tableColumnDef.isWithLinkAct()
                                        ? "!ui-label binding:" + tableColumnDef.getFieldName()
                                        : tableColumnDef.getCellRenderer());
                        Widget widget = addExternalChildWidget(columnWidgetUpl);
                        if (!_entry) {
                            EntityFieldDef entityFieldDef = tableDef.getFieldDef(tableColumnDef.getFieldName());
                            if (entityFieldDef.isNumber()) {
                                widget.setPrecision(entityFieldDef.getPrecision());
                                widget.setScale(entityFieldDef.getScale());
                            }
                        }

                        if (useCellEditor) {
                            if (inputs == null) {
                                inputs = new HashSet<Widget>();
                            }

                            inputs.add(widget);
                        }

                        standalonePanel.resolvePageActions(widget);
                    }

                }

                detailsPanelList = null;
                summaryPanelList = null;
            }

            if (table != null) {
                table.setTableSelect(this);
                final boolean totalSummary = table.isTotalSummary();
                final Map<String, EntityFieldTotalSummary> summaries = totalSummary
                        ? new HashMap<String, EntityFieldTotalSummary>()
                        : Collections.emptyMap();
                Order defaultOrder = new Order();
                String totalLabelColumn = null;
                int totalLabelColumnIndex = -1;
                for (TableColumnDef tableColumnDef : tableDef.getColumnDefList()) {
                    if (tableColumnDef.getOrder() != null) {
                        defaultOrder.add(tableColumnDef.getFieldName(), tableColumnDef.getOrder());
                    }

                    if (totalSummary && !tableColumnDef.isHidden()) {
                        EntityFieldDef entityFieldDef = tableDef.getFieldDef(tableColumnDef.getFieldName());
                        DataType dataType = entityFieldDef.isWithResolvedTypeFieldDef()
                                ? entityFieldDef.getResolvedTypeFieldDef().getDataType().dataType()
                                : entityFieldDef.getDataType().dataType();
                        if (dataType != null) {
                            if (entityFieldDef.isNumber()) {
                                Widget renderer = getRenderer(table.isDisableLinks() && tableColumnDef.isWithLinkAct()
                                        ? "!ui-label binding:" + tableColumnDef.getFieldName()
                                        : tableColumnDef.getCellRenderer());
                                EntityFieldTotalSummary entityFieldTotalSummary = new EntityFieldTotalSummary(
                                        entityFieldDef, renderer);
                                if (!tableDef.isWithSummaryFields()
                                        || tableDef.isSummaryField(entityFieldDef.getFieldName())) {
                                    summaries.put(entityFieldDef.getFieldName(), entityFieldTotalSummary);
                                }
                            }
                        }

                        if (summaries.isEmpty()) {
                            totalLabelColumn = entityFieldDef.getFieldName();
                            totalLabelColumnIndex++;
                        }
                    }
                }

                if (defaultOrder.isParts()) {
                    table.setDefaultOrder(defaultOrder);
                    table.reset();
                }

                if (totalSummary) {
                    TableTotalSummary tableTotalSummary = new TableTotalSummary(totalLabelColumn, totalLabelColumnIndex,
                            summaries);
                    table.setTableTotalSummary(tableTotalSummary);
                }
            }

            oldTable = table;
            sortHistory = null;
        }

        return table;
    }

    public Control getSelectCtrl() {
        return selectCtrl;
    }

    public Control getTabMemCtrl() {
        return tabMemCtrl;
    }

    public Control getSortColumnCtrl() {
        return sortColumnCtrl;
    }

    public Set<Integer> getSelected() throws UnifyException {
        return getTable().getSelectedRows();
    }

    public void setSelected(Set<Integer> selected) throws UnifyException {
        getTable().setSelectedRows(selected);
    }

    public void clearSelected() throws UnifyException {
        getTable().clearSelectedRows();
    }

    public String getTabMemoryId() {
        return tabMemoryId;
    }

    public void setTabMemoryId(String tabMemoryId) {
        this.tabMemoryId = tabMemoryId;
    }

    public OrderType getSortType() {
        return sort != null ? sort.getSortType() : OrderType.ASCENDING;
    }

    public int getSortColumnIndex() {
        return sortColumnIndex;
    }

    public void setSortColumnIndex(int sortColumnIndex) {
        this.sortColumnIndex = sortColumnIndex;
    }

    public boolean isInputWidget(Widget widget) {
        return inputs != null && inputs.contains(widget);
    }

    @Override
    public List<U> getSelectedItems() throws UnifyException {
        Set<Integer> selected = getSelected();
        if (!selected.isEmpty()) {
            List<Integer> _selected = new ArrayList<Integer>(selected);
            Collections.sort(_selected);
            List<U> list = new ArrayList<U>();
            for (Integer rowIndex : _selected) {
                list.add(getItem(rowIndex));
            }

            return list;
        }

        return Collections.emptyList();
    }

    @Override
    public boolean isRowSelected(int rowIndex) throws UnifyException {
        return getTable().isSelectedRow(rowIndex);
    }

    @Override
    public void setRowSelected(int rowIndex, boolean selected) throws UnifyException {
        getTable().setSelected(rowIndex, selected);
    }

    public void setActionBindings() throws UnifyException {
        final AbstractTable<?, ?> table = getTable();
        if (table != null) {
            final String viewCaption = table.getViewButtonCaption();
            final String editCaption = table.getEditButtonCaption();
            final boolean view = !StringUtils.isBlank(viewCaption);
            final boolean edit = !StringUtils.isBlank(editCaption);
            if (view || edit) {
                final String viewBinding = getViewButtonCaptionBinding();
                final String editBinding = getEditButtonCaptionBinding();
                for (ValueStore valueStore : getValueList()) {
                    if (view) {
                        valueStore.setTempValue(viewBinding, viewCaption);
                    }

                    if (edit) {
                        valueStore.setTempValue(editBinding, editCaption);
                    }
                }
            }
        }
    }

    @Override
    public List<ValueStore> getValueList() throws UnifyException {
        List<ValueStore> valueList = super.getValueList();
        if (valueList == null) {
            summaryPanelList = null;
        } else {
            final String summary = getUplAttribute(String.class, "summary");
            if (!StringUtils.isBlank(summary)) {
                if (summaryPanelList == null) {
                    summaryPanelList = new ArrayList<SummaryPanel>();
                }

                int extra = valueList.size() - summaryPanelList.size();
                for (int i = 0; i < extra; i++) {
                    SummaryPanel summaryPanel = (SummaryPanel) addExternalChildStandalonePanel(summary,
                            getId() + "_" + summaryPanelList.size() + "sm");
                    summaryPanelList.add(summaryPanel);
                }
            }
        }

        return valueList;
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        if (isMultiSelect()) {
            selectCtrl = createInternalHiddenControl("selected");
        }

        if (isFocusManagement()) {
            tabMemCtrl = createInternalHiddenControl("tabMemoryId");
        }

        String[] actionSymbol = getUplAttribute(String[].class, "actionSymbol");
        if (actionSymbol != null && actionSymbol.length > 0) {
            EventHandler[] actionHandler = getActionEventHandler();
            if (actionHandler == null || actionHandler.length != actionSymbol.length) {
                throwOperationErrorException(new IllegalArgumentException(
                        "Number of action handlers must match number of action symbols. Widget [" + getLongName()
                                + "]."));
            }

            actionCtrl = new Control[actionSymbol.length];
            for (int i = 0; i < actionSymbol.length; i++) {
                String symbol = actionSymbol[i];
                actionCtrl[i] = (Control) addInternalChildWidget(
                        "!ui-symbol alwaysValueIndex:true styleClass:$e{mact} symbol:$s{" + symbol
                                + "} ignoreParentState:true");
            }
        }

        sortColumnCtrl = (Control) addInternalChildWidget("!ui-hidden binding:sortColumnIndex");
    }

    @Override
    protected ValueStore newValue(U object, int index) throws UnifyException {
        return createValueStore(object, index);
    }

    @Override
    protected void onCreateValueList(List<ValueStore> valueStoreList) throws UnifyException {

    }

    @Override
    protected final List<U> getItemList() throws UnifyException {
        T table = getTable();
        if (table != null) {
            return table.getDispItemList();
        }

        return Collections.emptyList();
    }

    protected Class<U> getItemClass() {
        return itemClass;
    }

    private String getDetails() throws UnifyException {
        return oldTable != null && oldTable.getTableDef().isWithDetailsPanelName()
                ? oldTable.getTableDef().getDetailsPanelName()
                : getUplAttribute(String.class, "details");
    }

    private void applyFixedAction(FixedRowActionType fixedActionType) throws UnifyException {
        int target = getRequestTarget(int.class);
        T table = getTable();
        if (table != null) {
            table.applyFixedAction(getValueList().get(target), target, fixedActionType);
            table.reset();
        }
    }

    private void applyFixedActionSelect(FixedRowActionType fixedActionType) throws UnifyException {
        T table = getTable();
        if (table != null) {
            for (int i : table.getSelectedRows()) {
                table.applyFixedAction(getValueList().get(i), i, fixedActionType);
            }

            table.reset();
        }
    }

    private void applyFixedActionAll(FixedRowActionType fixedActionType) throws UnifyException {
        T table = getTable();
        if (table != null) {
            final int len = getValueList().size();
            for (int i = 0; i < len; i++) {
                table.applyFixedAction(getValueList().get(i), i, fixedActionType);
            }

            table.reset();
        }
    }

    private void clearRenderers() {
        renderers = null;
    }

    private Widget getRenderer(String renderer) throws UnifyException {
        if (renderers == null) {
            renderers = new HashMap<String, Widget>();
        }

        Widget _renderer = renderers.get(renderer);
        if (_renderer == null) {
            _renderer = addInternalChildWidget(renderer);
            renderers.put(renderer, _renderer);
        }

        return _renderer;
    }

    private class Sort {

        private OrderType sortType;

        private int columnIndex;

        public Sort(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public OrderType getSortType() {
            return sortType;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        public void flip() {
            sortType = (!OrderType.ASCENDING.equals(sortType)) ? OrderType.ASCENDING : OrderType.DESCENDING;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }

            if (getClass() != o.getClass()) {
                return false;
            }

            return this.columnIndex == ((Sort) o).columnIndex;
        }
    }
}
