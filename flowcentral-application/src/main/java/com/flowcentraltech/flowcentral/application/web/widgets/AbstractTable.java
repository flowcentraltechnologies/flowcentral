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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldTotalSummary;
import com.flowcentraltech.flowcentral.application.data.LabelSuggestionDef;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.common.business.policies.EntryTablePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.TableStateOverride;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.DefaultReportColumn;
import com.flowcentraltech.flowcentral.common.data.EntryTableMessage;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Convenient abstract base class for table object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractTable<T, U> {

    public static final int ENTRY_ENABLED = 0x00000001;

    public static final int ENTRY_SUMMARY_IGNORE_LAST = 0x00000002;

    protected final AppletUtilities au;

    private TableDef tableDef;

    private T sourceObject;

    private List<U> dispItemList;

    private Order order;

    private Order defaultOrder;

    private String[] refreshPanelIds;

    private int dispStartIndex;

    private int dispEndIndex;

    private int pageIndex;

    private int numberOfPages;

    private int totalItemCount;

    private int highlightedRow;

    private int entryMode;

    private boolean basicSearchMode;

    private boolean editMode;

    private boolean fixedAssignment;

    private ValueStoreReader parentReader;

    private TableStateOverride tableStateOverride;

    private EntryTableMessage entryMessage;

    private EntryTablePolicy entryPolicy;

    private TableTotalSummary tableTotalSummary;

    private Set<Integer> selected;

    private TableSelect<?> tableSelect;

    private List<EventHandler> switchOnChangeHandlers;

    private List<EventHandler> crudActionHandlers;

    private RowChangeInfo lastRowChangeInfo;

    private int detailsIndex;
    
    public AbstractTable(AppletUtilities au, TableDef tableDef, Order defaultOrder, int entryMode) {
        this.au = au;
        this.tableDef = tableDef;
        this.defaultOrder = defaultOrder;
        this.basicSearchMode = tableDef.isBasicSearch();
        this.entryMode = entryMode;
        this.selected = Collections.emptySet();
        this.highlightedRow = -1;
        this.detailsIndex = -1;
    }

    public void setTableSelect(TableSelect<?> tableSelect) {
        this.tableSelect = tableSelect;
    }

    public List<EventHandler> getSwitchOnChangeHandlers() {
        return switchOnChangeHandlers;
    }

    public void setSwitchOnChangeHandlers(List<EventHandler> switchOnChangeHandlers) {
        this.switchOnChangeHandlers = switchOnChangeHandlers;
    }

    public List<EventHandler> getCrudActionHandlers() {
        return crudActionHandlers;
    }

    public void setCrudActionHandlers(List<EventHandler> crudActionHandlers) {
        this.crudActionHandlers = crudActionHandlers;
    }

    public List<?> getSelectedItems() throws UnifyException {
        if (tableSelect != null) {
            return tableSelect.getSelectedItems();
        }

        return Collections.emptyList();
    }

    public final AppletUtilities au() {
        return au;
    }

    public EntityDef getEntityDef() {
        return tableDef.getEntityDef();
    }

    public LabelSuggestionDef getLabelSuggestionDef() {
        return tableDef.getLabelSuggestionDef();
    }

    public TableDef getTableDef() {
        return tableDef;
    }

    public List<TableColumnDef> getColumnDefList() {
        return tableDef.getColumnDefList();
    }

    public void setPolicy(EntryTablePolicy policy) {
        this.entryPolicy = policy;
    }

    public int getItemsPerPage() {
        return tableDef.getItemsPerPage();
    }

    public List<DefaultReportColumn> getDefaultReportColumnList() {
        return tableDef.getDefaultReportColumnList();
    }

    public int getHighlightedRow() {
        return highlightedRow;
    }

    public void setHighlightedRow(int highlightedRow) {
        this.highlightedRow = highlightedRow;
    }

    public void setSelected(Set<Integer> selected) {
        this.selected = selected;
    }

    public boolean isSelected(Integer index) {
        return selected.contains(index);
    }

    public boolean isSupportsBasicSearch() {
        return tableDef.isBasicSearch();
    }

    public void setBasicSearchMode(boolean basicSearchMode) {
        this.basicSearchMode = isSupportsBasicSearch() && basicSearchMode;
    }

    public boolean isBasicSearchMode() {
        return basicSearchMode;
    }

    public boolean isTotalSummary() {
        return tableDef.isTotalSummary();
    }

    public boolean isEntryMode() {
        return (entryMode & ENTRY_ENABLED) > 0;
    }

    public boolean isEntrySummaryIgnoreLast() {
        return (entryMode & ENTRY_SUMMARY_IGNORE_LAST) > 0;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public int getDetailsIndex() {
        return detailsIndex;
    }

    public void setDetailsIndex(int detailsIndex) {
        this.detailsIndex = detailsIndex;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public ValueStoreReader getParentReader() {
        return parentReader;
    }

    public void setParentReader(ValueStoreReader parentReader) {
        this.parentReader = parentReader;
    }

    public boolean isFixedAssignment() {
        return fixedAssignment;
    }

    public void setFixedAssignment(boolean fixedAssignment) {
        this.fixedAssignment = fixedAssignment;
    }

    public TableTotalSummary getTableTotalSummary() {
        return tableTotalSummary;
    }

    public void setTableTotalSummary(TableTotalSummary tableTotalSummary) {
        this.tableTotalSummary = tableTotalSummary;
    }

    public EntryTableMessage getEntryMessage() {
        return entryMessage;
    }

    public void clearSummaries() throws UnifyException {
        if (tableTotalSummary != null) {
            for (EntityFieldTotalSummary summary : tableTotalSummary.getSummaries().values()) {
                summary.clear();
            }
        }
    }

    public void addTotalSummary(String fieldName, Object val) throws UnifyException {
        if (tableTotalSummary != null && tableTotalSummary.getSummaries().containsKey(fieldName)) {
            EntityFieldTotalSummary summary = tableTotalSummary.getSummaries().get(fieldName);
            summary.add(val);
        }
    }

    public void loadTotalSummaryValueStore() throws UnifyException {
        if (tableTotalSummary != null) {
            ValueStore totalSummaryValueStore = tableTotalSummary.getTotalSummaryValueStore();
            for (EntityFieldTotalSummary summary : tableTotalSummary.getSummaries().values()) {
                totalSummaryValueStore.store(summary.getFieldName(), summary.getTotal());
            }
        }
    }

    public boolean isTotalLabelColumn(String fieldName) {
        return fieldName.equals(tableTotalSummary.getTotalLabelColumn());
    }

    public String getTotalLabel() throws UnifyException {
        return au.resolveSessionMessage("$m{tablewidget.total}");
    }

    public Widget getSummaryWidget(String fieldName) {
        if (tableTotalSummary != null && tableTotalSummary.getSummaries().containsKey(fieldName)) {
            EntityFieldTotalSummary summary = tableTotalSummary.getSummaries().get(fieldName);
            return summary.getRenderer();
        }

        return null;
    }

    public void setSourceObject(T sourceObject) throws UnifyException {
        this.sourceObject = sourceObject;
        onLoadSourceObject(sourceObject, selected);
        reset();
    }

    public void fireOnTableChange() throws UnifyException {
        onFireOnTableChange(sourceObject, selected);
    }

    public void validate(EvaluationMode evaluationMode, FormValidationErrors errors) throws UnifyException {
        validate(evaluationMode, sourceObject, errors);
    }

    public void fireOnRowChange(RowChangeInfo rowChangeInfo) throws UnifyException {
        lastRowChangeInfo = rowChangeInfo;
        onFireOnRowChange(sourceObject, rowChangeInfo);
    }

    public void setDefaultOrder(Order defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public T getSourceObject() {
        return sourceObject;
    }

    public Order getOrder() {
        return order != null ? order : defaultOrder;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<U> getDispItemList() {
        return dispItemList;
    }

    public U getDisplayItem(int displayIndex) {
        return dispItemList.get(displayIndex);
    }

    public boolean isWithDisplayItems() {
        return !DataUtils.isBlank(dispItemList);
    }

    public int getDispStartIndex() {
        return dispStartIndex;
    }

    public int getDispEndIndex() {
        return dispEndIndex;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public String[] getRefreshPanelIds() {
        return refreshPanelIds;
    }

    public void setRefreshPanelIds(String[] refreshPanelIds) {
        this.refreshPanelIds = refreshPanelIds;
    }

    public boolean isWithRefreshPanels() {
        return refreshPanelIds != null;
    }

    public RowChangeInfo getLastRowChangeInfo() {
        RowChangeInfo _lastRowChangeInfo = lastRowChangeInfo;
        lastRowChangeInfo = null;
        return _lastRowChangeInfo;
    }

    public void reset() throws UnifyException {
        int _pageIndex = pageIndex;
        calcPageDimensions();
        if (_pageIndex >= 0) {
            // Page memory
            pageIndex = _pageIndex < numberOfPages ? _pageIndex : (numberOfPages > 0 ? numberOfPages - 1 : 0);
        }

        orderOnReset();
        getDispItems();
    }

    public boolean firstPage() throws UnifyException {
        if (tableDef.isPagination()) {
            pageIndex = 0;
            getDispItems();
            return true;
        }

        getDispItems();
        return false;
    }

    public boolean prevPage() throws UnifyException {
        if (tableDef.isPagination() && pageIndex > 0) {
            pageIndex--;
            getDispItems();
            return true;
        }

        return false;
    }

    public boolean nextPage() throws UnifyException {
        if (tableDef.isPagination() && pageIndex < (numberOfPages - 1)) {
            pageIndex++;
            getDispItems();
            return true;
        }

        return false;
    }

    public boolean lastPage() throws UnifyException {
        if (tableDef.isPagination()) {
            pageIndex = numberOfPages - 1;
            getDispItems();
            return true;
        }

        return false;
    }

    public boolean isAtFirstPage() {
        return numberOfPages == 0 || pageIndex == 0;
    }

    public boolean isAtLastPage() {
        return numberOfPages == 0 || pageIndex >= numberOfPages - 1;
    }

    public TableStateOverride getTableStateOverride(ValueStore rowValueStore) throws UnifyException {
        if (tableStateOverride == null) {
            tableStateOverride = new TableStateOverride();
        }

        tableStateOverride.reset();
        if (entryPolicy != null) {
            entryPolicy.applyTableStateOverride(parentReader, rowValueStore, tableStateOverride);
            ;
        }

        return tableStateOverride;
    }

    public int resolveActionIndex(ValueStore valueStore, int index, int size) throws UnifyException {
        return entryPolicy != null ? entryPolicy.resolveActionIndex(parentReader, valueStore, index, size) : 0;
    }

    public EntryTablePolicy getEntryPolicy() {
        return entryPolicy;
    }

    protected boolean isWithEntryPolicy() {
        return entryPolicy != null;
    }

    protected abstract void validate(EvaluationMode evaluationMode, T sourceObject, FormValidationErrors errors)
            throws UnifyException;

    protected abstract void onLoadSourceObject(T sourceObject, Set<Integer> selected) throws UnifyException;

    protected abstract void onFireOnTableChange(T sourceObject, Set<Integer> selected) throws UnifyException;

    protected abstract void onFireOnRowChange(T sourceObject, RowChangeInfo rowChangeInfo) throws UnifyException;

    protected abstract int getSourceObjectSize(T sourceObject) throws UnifyException;

    protected abstract List<U> getDisplayItems(T sourceObject, int dispStartIndex, int dispEndIndex)
            throws UnifyException;

    protected abstract void orderOnReset() throws UnifyException;

    private void calcPageDimensions() throws UnifyException {
        pageIndex = 0;
        totalItemCount = getSourceObjectSize(sourceObject);
        if (totalItemCount <= 0) {
            totalItemCount = 0;
            numberOfPages = 0;
        } else {
            if (tableDef.isPagination()) {
                int itemsPerPage = tableDef.getItemsPerPage();
                numberOfPages = totalItemCount / itemsPerPage;
                if (totalItemCount % itemsPerPage > 0) {
                    numberOfPages++;
                }
            } else {
                numberOfPages = 1;
            }
        }
    }

    private void getDispItems() throws UnifyException {
        if (tableDef.isPagination()) {
            int itemsPerPage = tableDef.getItemsPerPage();
            dispStartIndex = pageIndex * itemsPerPage;
            dispEndIndex = dispStartIndex + itemsPerPage;
            if (dispEndIndex > totalItemCount) {
                dispEndIndex = totalItemCount;
            }
        } else {
            dispStartIndex = 0;
            dispEndIndex = totalItemCount;
        }

        dispItemList = getDisplayItems(sourceObject, dispStartIndex, dispEndIndex);
    }
}
