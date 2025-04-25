/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.List;

import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;

/**
 * Application table entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_TABLE")
public class AppTable extends BaseApplicationEntity {

    @Column(name = "TABLE_LABEL", length = 128)
    private String label;

    @Column(length = 128)
    private String entity;

    @Column(name = "DETAILS_PANEL_NM", length = 64, nullable = true)
    private String detailsPanelName;

    @Column(length = 64, nullable = true)
    private String loadingSearchInput;

    @Column(length = 64, nullable = true)
    private String loadingFilterGen;

    @Column
    private int sortHistory;

    @Column
    private int itemsPerPage;

    @Column
    private int summaryTitleColumns;

    @Column
    private boolean sortable;

    @Column
    private boolean serialNo;
    
    @Column
    private boolean showLabelHeader;

    @Column
    private boolean headerToUpperCase;

    @Column
    private boolean headerCenterAlign;

    @Column
    private boolean basicSearch;

    @Column
    private boolean totalSummary;

    @Column
    private boolean headerless;

    @Column
    private boolean multiSelect;

    @Column
    private boolean nonConforming;

    @Column
    private boolean fixedRows;

    @Column
    private boolean limitSelectToColumns;

    @ChildList
    private List<AppTableLoading> loadingList;
    
    @ChildList
    private List<AppTableAction> actionList;

    @ChildList
    private List<AppTableColumn> columnList;

    @ChildList
    private List<AppTableFilter> filterList;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getDetailsPanelName() {
        return detailsPanelName;
    }

    public void setDetailsPanelName(String detailsPanelName) {
        this.detailsPanelName = detailsPanelName;
    }

    public String getLoadingSearchInput() {
        return loadingSearchInput;
    }

    public void setLoadingSearchInput(String loadingSearchInput) {
        this.loadingSearchInput = loadingSearchInput;
    }

    public String getLoadingFilterGen() {
        return loadingFilterGen;
    }

    public void setLoadingFilterGen(String loadingFilterGen) {
        this.loadingFilterGen = loadingFilterGen;
    }

    public int getSortHistory() {
        return sortHistory;
    }

    public void setSortHistory(int sortHistory) {
        this.sortHistory = sortHistory;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getSummaryTitleColumns() {
        return summaryTitleColumns;
    }

    public void setSummaryTitleColumns(int summaryTitleColumns) {
        this.summaryTitleColumns = summaryTitleColumns;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isSerialNo() {
        return serialNo;
    }

    public void setSerialNo(boolean serialNo) {
        this.serialNo = serialNo;
    }

    public boolean isShowLabelHeader() {
        return showLabelHeader;
    }

    public void setShowLabelHeader(boolean showLabelHeader) {
        this.showLabelHeader = showLabelHeader;
    }

    public boolean isHeaderToUpperCase() {
        return headerToUpperCase;
    }

    public void setHeaderToUpperCase(boolean headerToUpperCase) {
        this.headerToUpperCase = headerToUpperCase;
    }

    public boolean isHeaderCenterAlign() {
        return headerCenterAlign;
    }

    public void setHeaderCenterAlign(boolean headerCenterAlign) {
        this.headerCenterAlign = headerCenterAlign;
    }

    public boolean isBasicSearch() {
        return basicSearch;
    }

    public void setBasicSearch(boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

    public boolean isTotalSummary() {
        return totalSummary;
    }

    public void setTotalSummary(boolean totalSummary) {
        this.totalSummary = totalSummary;
    }

    public boolean isHeaderless() {
        return headerless;
    }

    public void setHeaderless(boolean headerless) {
        this.headerless = headerless;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public boolean isNonConforming() {
        return nonConforming;
    }

    public void setNonConforming(boolean nonConforming) {
        this.nonConforming = nonConforming;
    }

    public boolean isFixedRows() {
        return fixedRows;
    }

    public void setFixedRows(boolean fixedRows) {
        this.fixedRows = fixedRows;
    }

    public boolean isLimitSelectToColumns() {
        return limitSelectToColumns;
    }

    public void setLimitSelectToColumns(boolean limitSelectToColumns) {
        this.limitSelectToColumns = limitSelectToColumns;
    }

    public List<AppTableLoading> getLoadingList() {
        return loadingList;
    }

    public void setLoadingList(List<AppTableLoading> loadingList) {
        this.loadingList = loadingList;
    }

    public List<AppTableAction> getActionList() {
        return actionList;
    }

    public void setActionList(List<AppTableAction> actionList) {
        this.actionList = actionList;
    }

    public List<AppTableColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<AppTableColumn> columnList) {
        this.columnList = columnList;
    }

    public List<AppTableFilter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<AppTableFilter> filterList) {
        this.filterList = filterList;
    }

}
