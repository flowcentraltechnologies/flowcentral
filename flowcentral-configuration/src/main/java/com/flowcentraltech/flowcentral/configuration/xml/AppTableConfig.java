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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Table configuration
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class AppTableConfig extends BaseNameConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true, localName = "detailsPanel")
    private String detailsPanelName;

    @JacksonXmlProperty(isAttribute = true)
    private String loadingSearchInput;

    @JacksonXmlProperty(isAttribute = true)
    private String loadingFilterGen;

    @JacksonXmlProperty(isAttribute = true)
    private int sortHistory;

    @JacksonXmlProperty(isAttribute = true)
    private int itemsPerPage;
    
    @JacksonXmlProperty(isAttribute = true)
    private int summaryTitleColumns;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean serialNo;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean sortable;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean showLabelHeader;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean headerToUpperCase;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean headerCenterAlign;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean basicSearch;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean totalSummary;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean headerless;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean multiSelect;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean nonConforming;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean fixedRows;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean limitSelectToColumns;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "loading")
    private List<TableLoadingConfig> loadingList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "action")
    private List<TableActionConfig> actionList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "column")
    private List<TableColumnConfig> columnList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "quickFilter")
    private List<FilterConfig> quickFilterList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "filter")
    private List<TableFilterConfig> filterList;

    public AppTableConfig() {
        this.serialNo = Boolean.FALSE;
        this.sortable = Boolean.TRUE;
        this.showLabelHeader = Boolean.FALSE;
        this.headerToUpperCase = Boolean.FALSE;
        this.headerCenterAlign = Boolean.FALSE;
        this.basicSearch = Boolean.FALSE;
        this.totalSummary = Boolean.FALSE;
        this.headerless = Boolean.FALSE;
        this.multiSelect = Boolean.FALSE;
        this.nonConforming = Boolean.FALSE;
        this.fixedRows = Boolean.FALSE;
        this.limitSelectToColumns = Boolean.TRUE;
        this.sortHistory = -1;
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

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Boolean getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Boolean serialNo) {
        this.serialNo = serialNo;
    }

    public Boolean getShowLabelHeader() {
        return showLabelHeader;
    }

    public void setShowLabelHeader(Boolean showLabelHeader) {
        this.showLabelHeader = showLabelHeader;
    }

    public Boolean getHeaderToUpperCase() {
        return headerToUpperCase;
    }

    public void setHeaderToUpperCase(Boolean headerToUpperCase) {
        this.headerToUpperCase = headerToUpperCase;
    }

    public Boolean getHeaderCenterAlign() {
        return headerCenterAlign;
    }

    public void setHeaderCenterAlign(Boolean headerCenterAlign) {
        this.headerCenterAlign = headerCenterAlign;
    }

    public Boolean getBasicSearch() {
        return basicSearch;
    }

    public void setBasicSearch(Boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

    public Boolean getTotalSummary() {
        return totalSummary;
    }

    public void setTotalSummary(Boolean totalSummary) {
        this.totalSummary = totalSummary;
    }

    public Boolean getHeaderless() {
        return headerless;
    }

    public void setHeaderless(Boolean headerless) {
        this.headerless = headerless;
    }

    public Boolean getMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public Boolean getNonConforming() {
        return nonConforming;
    }

    public void setNonConforming(Boolean nonConforming) {
        this.nonConforming = nonConforming;
    }

    public Boolean getFixedRows() {
        return fixedRows;
    }

    public void setFixedRows(Boolean fixedRows) {
        this.fixedRows = fixedRows;
    }

    public Boolean getLimitSelectToColumns() {
        return limitSelectToColumns;
    }

    public void setLimitSelectToColumns(Boolean limitSelectToColumns) {
        this.limitSelectToColumns = limitSelectToColumns;
    }

    public List<TableLoadingConfig> getLoadingList() {
        return loadingList;
    }

    public void setLoadingList(List<TableLoadingConfig> loadingList) {
        this.loadingList = loadingList;
    }

    public List<TableActionConfig> getActionList() {
        return actionList;
    }

    public void setActionList(List<TableActionConfig> actionList) {
        this.actionList = actionList;
    }

    public List<TableColumnConfig> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<TableColumnConfig> columnList) {
        this.columnList = columnList;
    }

    public List<FilterConfig> getQuickFilterList() {
        return quickFilterList;
    }

    public void setQuickFilterList(List<FilterConfig> quickFilterList) {
        this.quickFilterList = quickFilterList;
    }

    public List<TableFilterConfig> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<TableFilterConfig> filterList) {
        this.filterList = filterList;
    }

}
