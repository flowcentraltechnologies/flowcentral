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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.flowcentraltech.flowcentral.configuration.constants.ReportConfigType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.PageSizeTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ReportConfigTypeXmlAdapter;
import com.tcdng.unify.core.constant.PageSizeType;

/**
 * Report configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "report")
public class ReportConfig extends BaseRootConfig {

    @JsonSerialize(using = ReportConfigTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = ReportConfigTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName="type")
    private ReportConfigType type;
    
    @JacksonXmlProperty(isAttribute = true)
    private String title;

    @JacksonXmlProperty(isAttribute = true)
    private String processor;

    @JacksonXmlProperty(isAttribute = true)
    private String reportable;

    @JacksonXmlProperty(isAttribute = true)
    private String template;

    @JacksonXmlProperty(isAttribute = true)
    private String letterGenerator;
    
    @JacksonXmlProperty(isAttribute = true)
    private String summaryDatasource;
    
    @JacksonXmlProperty
    private ReportColumnsConfig columns;

    @JacksonXmlProperty
    private ReportPlacementsConfig placements;
    
    @JacksonXmlProperty
    private ParametersConfig parameters;

    @JacksonXmlProperty(localName="filter")
    private FilterConfig filter;

    @JsonSerialize(using = PageSizeTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = PageSizeTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName="sizeType")
    private PageSizeType sizeType;
    
    @JacksonXmlProperty(isAttribute = true)
    private int width;

    @JacksonXmlProperty(isAttribute = true)
    private int height;

    @JacksonXmlProperty(isAttribute = true)
    private int marginTop;

    @JacksonXmlProperty(isAttribute = true)
    private int marginBottom;

    @JacksonXmlProperty(isAttribute = true)
    private int marginLeft;

    @JacksonXmlProperty(isAttribute = true)
    private int marginRight;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean showGrandFooter;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean invertGroupColors;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean landscape;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean underlineRows;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean shadeOddRows;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean allowSecondaryTenants;

    public ReportConfig() {
        super("flowcentral-report-4.0.0.xsd");
        this.type = ReportConfigType.TABULAR;
        this.sizeType = PageSizeType.A4;
        this.showGrandFooter = Boolean.FALSE;
        this.invertGroupColors = Boolean.FALSE;
        this.landscape = Boolean.FALSE;
        this.underlineRows = Boolean.FALSE;
        this.shadeOddRows = Boolean.FALSE;
        this.allowSecondaryTenants = Boolean.FALSE;
    }

    public ReportConfigType getType() {
        return type;
    }

    public void setType(ReportConfigType type) {
        this.type = type;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getReportable() {
        return reportable;
    }

    public void setReportable(String reportable) {
        this.reportable = reportable;
    }

    public ReportColumnsConfig getColumns() {
        return columns;
    }

    public void setColumns(ReportColumnsConfig columns) {
        this.columns = columns;
    }

    public ReportPlacementsConfig getPlacements() {
        return placements;
    }

    public void setPlacements(ReportPlacementsConfig placements) {
        this.placements = placements;
    }

    public ParametersConfig getParameters() {
        return parameters;
    }

    public void setParameters(ParametersConfig parameters) {
        this.parameters = parameters;
    }

    public FilterConfig getFilter() {
        return filter;
    }

    public void setFilter(FilterConfig filter) {
        this.filter = filter;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean getShowGrandFooter() {
        return showGrandFooter;
    }

    public void setShowGrandFooter(Boolean showGrandFooter) {
        this.showGrandFooter = showGrandFooter;
    }

    public Boolean getInvertGroupColors() {
        return invertGroupColors;
    }

    public void setInvertGroupColors(Boolean invertGroupColors) {
        this.invertGroupColors = invertGroupColors;
    }

    public Boolean getLandscape() {
        return landscape;
    }

    public void setLandscape(Boolean landscape) {
        this.landscape = landscape;
    }

    public boolean getUnderlineRows() {
        return underlineRows;
    }

    public void setUnderlineRows(Boolean underlineRows) {
        this.underlineRows = underlineRows;
    }

    public Boolean getShadeOddRows() {
        return shadeOddRows;
    }

    public void setShadeOddRows(Boolean shadeOddRows) {
        this.shadeOddRows = shadeOddRows;
    }

    public Boolean getAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    public void setAllowSecondaryTenants(Boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }
}
