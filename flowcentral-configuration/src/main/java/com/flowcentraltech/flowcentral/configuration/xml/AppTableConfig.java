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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Table configuration
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppTableConfig extends BaseNameConfig {

    private String entity;

    private String detailsPanelName;

    private int sortHistory;

    private int itemsPerPage;

    private boolean serialNo;

    private boolean sortable;

    private boolean headerToUpperCase;

    private boolean headerCenterAlign;

    private boolean basicSearch;

    private boolean totalSummary;

    private boolean headerless;

    private boolean multiSelect;

    private boolean nonConforming;

    private boolean fixedRows;

    private boolean limitSelectToColumns;

    private List<TableLoadingConfig> loadingList;

    private List<TableActionConfig> actionList;

    private List<TableColumnConfig> columnList;

    private List<FilterConfig> quickFilterList;

    private List<TableFilterConfig> filterList;

    public AppTableConfig() {
        this.sortable = true;
        this.limitSelectToColumns = true;
        this.sortHistory = -1;
    }

    public String getEntity() {
        return entity;
    }

    @XmlAttribute(required = true)
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getDetailsPanelName() {
        return detailsPanelName;
    }

    @XmlAttribute(required = true, name = "detailsPanel")
    public void setDetailsPanelName(String detailsPanelName) {
        this.detailsPanelName = detailsPanelName;
    }

    public int getSortHistory() {
        return sortHistory;
    }

    @XmlAttribute
    public void setSortHistory(int sortHistory) {
        this.sortHistory = sortHistory;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    @XmlAttribute
    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public boolean isSortable() {
        return sortable;
    }

    @XmlAttribute
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isSerialNo() {
        return serialNo;
    }

    @XmlAttribute
    public void setSerialNo(boolean serialNo) {
        this.serialNo = serialNo;
    }

    public boolean isHeaderToUpperCase() {
        return headerToUpperCase;
    }

    @XmlAttribute
    public void setHeaderToUpperCase(boolean headerToUpperCase) {
        this.headerToUpperCase = headerToUpperCase;
    }

    public boolean isHeaderCenterAlign() {
        return headerCenterAlign;
    }

    @XmlAttribute
    public void setHeaderCenterAlign(boolean headerCenterAlign) {
        this.headerCenterAlign = headerCenterAlign;
    }

    public boolean isBasicSearch() {
        return basicSearch;
    }

    @XmlAttribute
    public void setBasicSearch(boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

    public boolean isTotalSummary() {
        return totalSummary;
    }

    @XmlAttribute
    public void setTotalSummary(boolean totalSummary) {
        this.totalSummary = totalSummary;
    }

    public boolean isHeaderless() {
        return headerless;
    }

    @XmlAttribute
    public void setHeaderless(boolean headerless) {
        this.headerless = headerless;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    @XmlAttribute
    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public boolean isNonConforming() {
        return nonConforming;
    }

    @XmlAttribute
    public void setNonConforming(boolean nonConforming) {
        this.nonConforming = nonConforming;
    }

    public boolean isFixedRows() {
        return fixedRows;
    }

    @XmlAttribute
    public void setFixedRows(boolean fixedRows) {
        this.fixedRows = fixedRows;
    }

    public boolean isLimitSelectToColumns() {
        return limitSelectToColumns;
    }

    @XmlAttribute
    public void setLimitSelectToColumns(boolean limitSelectToColumns) {
        this.limitSelectToColumns = limitSelectToColumns;
    }

    public List<TableLoadingConfig> getLoadingList() {
        return loadingList;
    }

    @XmlElement(name = "loading", required = true)
    public void setLoadingList(List<TableLoadingConfig> loadingList) {
        this.loadingList = loadingList;
    }

    public List<TableActionConfig> getActionList() {
        return actionList;
    }

    @XmlElement(name = "action", required = true)
    public void setActionList(List<TableActionConfig> actionList) {
        this.actionList = actionList;
    }

    public List<TableColumnConfig> getColumnList() {
        return columnList;
    }

    @XmlElement(name = "column", required = true)
    public void setColumnList(List<TableColumnConfig> columnList) {
        this.columnList = columnList;
    }

    public List<FilterConfig> getQuickFilterList() {
        return quickFilterList;
    }

    @XmlElement(name = "quickFilter")
    public void setQuickFilterList(List<FilterConfig> quickFilterList) {
        this.quickFilterList = quickFilterList;
    }

    public List<TableFilterConfig> getFilterList() {
        return filterList;
    }

    @XmlElement(name = "filter", required = true)
    public void setFilterList(List<TableFilterConfig> filterList) {
        this.filterList = filterList;
    }

}
