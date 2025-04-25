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
package com.flowcentraltech.flowcentral.report.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ReportConfigType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.constant.PageSizeType;

/**
 * Report configuration data. Represents a report setup.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_REPORTCONFIG")
public class ReportConfiguration extends BaseApplicationEntity {

    @ForeignKey
    private ReportConfigType type;

    @ForeignKey(nullable = true)
    private PageSizeType sizeType;
    
    @Column(length = 128)
    private String reportable;

    @Column(length = 128)
    private String title;

    @Column(length = 64, nullable = true)
    private String template;

    @Column(length = 64, nullable = true)
    private String processor;

    @Column(length = 64, nullable = true)
    private String letterGenerator;

    @Column(length = 64, nullable = true)
    private String summaryDatasource;

    @Column
    private int width;

    @Column
    private int height;

    @Column
    private int marginTop;

    @Column
    private int marginBottom;

    @Column
    private int marginLeft;

    @Column
    private int marginRight;

    @Column
    private boolean showGrandFooter;

    @Column
    private boolean invertGroupColors;

    @Column
    private boolean landscape;

    @Column
    private boolean underlineRows;

    @Column
    private boolean shadeOddRows;

    @Column
    private boolean allowSecondaryTenants;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "sizeType", property = "description")
    private String sizeTypeDesc;
   
    @ChildList
    private List<ReportColumn> columnList;

    @ChildList
    private List<ReportPlacement> placementList;

    @ChildList
    private List<ReportParameter> parameterList;

    @Child(category = "report-config")
    private AppFilter filter;

    public ReportConfigType getType() {
        return type;
    }

    public void setType(ReportConfigType type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getReportable() {
        return reportable;
    }

    public void setReportable(String reportable) {
        this.reportable = reportable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getLetterGenerator() {
        return letterGenerator;
    }

    public void setLetterGenerator(String letterGenerator) {
        this.letterGenerator = letterGenerator;
    }

    public String getSummaryDatasource() {
        return summaryDatasource;
    }

    public void setSummaryDatasource(String summaryDatasource) {
        this.summaryDatasource = summaryDatasource;
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

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public boolean isUnderlineRows() {
        return underlineRows;
    }

    public void setUnderlineRows(boolean underlineRows) {
        this.underlineRows = underlineRows;
    }

    public boolean isShadeOddRows() {
        return shadeOddRows;
    }

    public void setShadeOddRows(boolean shadeOddRows) {
        this.shadeOddRows = shadeOddRows;
    }

    public boolean isAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    public void setAllowSecondaryTenants(boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public List<ReportColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ReportColumn> columnList) {
        this.columnList = columnList;
    }

    public List<ReportPlacement> getPlacementList() {
        return placementList;
    }

    public void setPlacementList(List<ReportPlacement> placementList) {
        this.placementList = placementList;
    }

    public List<ReportParameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<ReportParameter> parameterList) {
        this.parameterList = parameterList;
    }

    public AppFilter getFilter() {
        return filter;
    }

    public void setFilter(AppFilter filter) {
        this.filter = filter;
    }

    public PageSizeType getSizeType() {
        return sizeType;
    }

    public void setSizeType(PageSizeType sizeType) {
        this.sizeType = sizeType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public String getSizeTypeDesc() {
        return sizeTypeDesc;
    }

    public void setSizeTypeDesc(String sizeTypeDesc) {
        this.sizeTypeDesc = sizeTypeDesc;
    }

}
