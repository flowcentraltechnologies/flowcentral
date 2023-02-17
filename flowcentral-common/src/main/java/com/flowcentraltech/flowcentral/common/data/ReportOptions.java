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

package com.flowcentraltech.flowcentral.common.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.ReportConfigType;
import com.tcdng.unify.core.constant.PageSizeType;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.core.report.ReportFormat;

/**
 * Report options.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReportOptions {

    private final ReportConfigType type;
    
    private PageSizeType sizeType;
    
    private String reportName;

    private String reportDescription;

    private String reportFormat;

    private String reportResourcePath;

    private String recordName;

    private String processor;

    private String dataSource;

    private String title;

    private String filename;

    private String query;

    private String tableName;

    private String entity;

    private List<ReportColumnOptions> columnOptionsList;

    private List<ReportPlacementOptions> placementOptionsList;

    private List<ReportJoinOptions> joinOptionsList;

    private ReportFilterOptions filterOptions;

    private List<?> content;

    private List<Input<?>> userInputList;

    private List<Input<?>> systemInputList;

    private Restriction restriction;

    private int marginTop;

    private int marginBottom;

    private int marginLeft;

    private int marginRight;

    private int pageWidth;

    private int pageHeight;

    private boolean dynamicDataSource;

    private boolean printColumnNames;

    private boolean printGroupColumnNames;

    private boolean showGrandFooter;

    private boolean invertGroupColors;

    private boolean shadeOddRows;

    private boolean underlineRows;

    private boolean landscape;

    private boolean download;

    private boolean userInputOnly;

    private boolean reportEntityList;

    public ReportOptions(ReportConfigType type) {
        this.type = type;
        this.columnOptionsList = new ArrayList<ReportColumnOptions>();
        this.placementOptionsList = new ArrayList<ReportPlacementOptions>();
        this.joinOptionsList = new ArrayList<ReportJoinOptions>();
        this. printColumnNames = true;
        this.printGroupColumnNames = true;
        this.shadeOddRows = true;
    }

    public void reset() {
        printColumnNames = true;
        printGroupColumnNames = true;
        shadeOddRows = true;
        underlineRows = false;
        landscape = false;
        download = false;
    }

    public ReportConfigType getType() {
        return type;
    }

    public boolean isTabular() {
        return type.isTabular();
    }

    public boolean isPlacement() {
        return type.isPlacement();
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public final String getEntity() {
        return entity;
    }

    public final void setEntity(String entity) {
        this.entity = entity;
    }

    public String getReportFormat() {
        return reportFormat == null ? String.valueOf(ReportFormat.PDF) : reportFormat;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getReportResourcePath() {
        return reportResourcePath;
    }

    public void setReportResourcePath(String reportResourcePath) {
        this.reportResourcePath = reportResourcePath;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public List<?> getContent() {
        return content;
    }

    public void setContent(List<?> content) {
        this.content = content;
    }

    public boolean isBeanCollection() {
        return content != null;
    }

    public void addColumnOptions(ReportColumnOptions reportColumnOptions) {
        columnOptionsList.add(reportColumnOptions);
    }

    public List<ReportColumnOptions> getColumnOptionsList() {
        return columnOptionsList;
    }

    public void addPlacementOptions(ReportPlacementOptions reportPlacementOptions) {
        placementOptionsList.add(reportPlacementOptions);
    }

    public List<ReportPlacementOptions> getPlacementOptionsList() {
        return placementOptionsList;
    }

    public void addJoinOptions(ReportJoinOptions reportJoinOptions) {
        joinOptionsList.add(reportJoinOptions);
    }

    public List<ReportJoinOptions> getJoinOptionsList() {
        return joinOptionsList;
    }

    public ReportFilterOptions getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(ReportFilterOptions filterOptions) {
        this.filterOptions = filterOptions;
    }

    public List<Input<?>> getUserInputList() {
        return userInputList;
    }

    public void setUserInputList(List<Input<?>> userInputList) {
        this.userInputList = userInputList;
    }

    public List<Input<?>> getSystemInputList() {
        return systemInputList;
    }

    public void setSystemInputList(List<Input<?>> systemInputList) {
        this.systemInputList = systemInputList;
    }

    public boolean isWithUserInput() {
        return userInputList != null && !userInputList.isEmpty();
    }

    public boolean isWithColumnOptions() {
        return columnOptionsList != null && !columnOptionsList.isEmpty();
    }

    public boolean isWithPlacementOptions() {
        return placementOptionsList != null && !placementOptionsList.isEmpty();
    }

    public boolean isWithJoinOptions() {
        return joinOptionsList != null && !joinOptionsList.isEmpty();
    }

    public boolean isWithFilterOptions() {
        return filterOptions != null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public void setRestriction(Restriction restriction) {
        this.restriction = restriction;
    }

    public boolean isWithRestriction() {
        return restriction != null;
    }

    public PageSizeType getSizeType() {
        return sizeType;
    }

    public void setSizeType(PageSizeType sizeType) {
        this.sizeType = sizeType;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public boolean isDynamicDataSource() {
        return dynamicDataSource;
    }

    public void setDynamicDataSource(boolean dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
    }

    public boolean isPrintColumnNames() {
        return printColumnNames;
    }

    public void setPrintColumnNames(boolean printColumnNames) {
        this.printColumnNames = printColumnNames;
    }

    public boolean isPrintGroupColumnNames() {
        return printGroupColumnNames;
    }

    public void setPrintGroupColumnNames(boolean printGroupColumnNames) {
        this.printGroupColumnNames = printGroupColumnNames;
    }

    public boolean isShowGrandFooter() {
        return showGrandFooter;
    }

    public void setShowGrandFooter(boolean showGrandFooter) {
        this.showGrandFooter = showGrandFooter;
    }

    public boolean isInvertGroupColors() {
        return invertGroupColors;
    }

    public void setInvertGroupColors(boolean invertGroupColors) {
        this.invertGroupColors = invertGroupColors;
    }

    public boolean isShadeOddRows() {
        return shadeOddRows;
    }

    public void setShadeOddRows(boolean shadeOddRows) {
        this.shadeOddRows = shadeOddRows;
    }

    public boolean isUnderlineRows() {
        return underlineRows;
    }

    public void setUnderlineRows(boolean underlineRows) {
        this.underlineRows = underlineRows;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean isUserInputOnly() {
        return userInputOnly;
    }

    public void setUserInputOnly(boolean userInputOnly) {
        this.userInputOnly = userInputOnly;
    }

    public boolean isReportEntityList() {
        return reportEntityList;
    }

    public void setReportEntityList(boolean reportEntityList) {
        this.reportEntityList = reportEntityList;
    }
}
