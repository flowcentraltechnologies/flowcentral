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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalTrueToNullXmlAdapter;

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
    
    private int summaryTitleColumns;

    private Boolean serialNo;

    private Boolean sortable;
    
    private Boolean showLabelHeader;

    private Boolean headerToUpperCase;

    private Boolean headerCenterAlign;

    private Boolean basicSearch;

    private Boolean totalSummary;

    private Boolean headerless;

    private Boolean multiSelect;

    private Boolean nonConforming;

    private Boolean fixedRows;

    private Boolean limitSelectToColumns;

    private List<TableLoadingConfig> loadingList;

    private List<TableActionConfig> actionList;

    private List<TableColumnConfig> columnList;

    private List<FilterConfig> quickFilterList;

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

    public int getSummaryTitleColumns() {
        return summaryTitleColumns;
    }

    @XmlAttribute
    public void setSummaryTitleColumns(int summaryTitleColumns) {
        this.summaryTitleColumns = summaryTitleColumns;
    }

    public Boolean getSortable() {
        return sortable;
    }

    @XmlJavaTypeAdapter(MarshalTrueToNullXmlAdapter.class)
    @XmlAttribute
    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Boolean getSerialNo() {
        return serialNo;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setSerialNo(Boolean serialNo) {
        this.serialNo = serialNo;
    }

    public Boolean getShowLabelHeader() {
        return showLabelHeader;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setShowLabelHeader(Boolean showLabelHeader) {
        this.showLabelHeader = showLabelHeader;
    }

    public Boolean getHeaderToUpperCase() {
        return headerToUpperCase;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setHeaderToUpperCase(Boolean headerToUpperCase) {
        this.headerToUpperCase = headerToUpperCase;
    }

    public Boolean getHeaderCenterAlign() {
        return headerCenterAlign;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setHeaderCenterAlign(Boolean headerCenterAlign) {
        this.headerCenterAlign = headerCenterAlign;
    }

    public Boolean getBasicSearch() {
        return basicSearch;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setBasicSearch(Boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

    public Boolean getTotalSummary() {
        return totalSummary;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setTotalSummary(Boolean totalSummary) {
        this.totalSummary = totalSummary;
    }

    public Boolean getHeaderless() {
        return headerless;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setHeaderless(Boolean headerless) {
        this.headerless = headerless;
    }

    public Boolean getMultiSelect() {
        return multiSelect;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public Boolean getNonConforming() {
        return nonConforming;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setNonConforming(Boolean nonConforming) {
        this.nonConforming = nonConforming;
    }

    public Boolean getFixedRows() {
        return fixedRows;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setFixedRows(Boolean fixedRows) {
        this.fixedRows = fixedRows;
    }

    public Boolean getLimitSelectToColumns() {
        return limitSelectToColumns;
    }

    @XmlJavaTypeAdapter(MarshalTrueToNullXmlAdapter.class)
    @XmlAttribute
    public void setLimitSelectToColumns(Boolean limitSelectToColumns) {
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
