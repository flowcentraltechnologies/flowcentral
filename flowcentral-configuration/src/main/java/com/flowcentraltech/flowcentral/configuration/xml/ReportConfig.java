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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.xml.adapter.ReportLayoutTypeXmlAdapter;
import com.tcdng.unify.core.report.ReportLayoutType;
import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;

/**
 * Report configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@XmlRootElement(name = "report")
public class ReportConfig extends BaseNameConfig {

    private String title;

    private String processor;

    private String reportable;

    private String template;

    private ReportLayoutType layout;

    private ReportColumnsConfig columns;

    private ReportPlacementsConfig placements;
    
    private ParametersConfig parameters;

    private FilterConfig filter;

    private Boolean showGrandFooter;

    private Boolean invertGroupColors;

    private Boolean landscape;

    private Boolean underlineRows;

    private Boolean shadeOddRows;

    private Boolean allowSecondaryTenants;

    public ReportConfig() {
        this.showGrandFooter = Boolean.FALSE;
        this.invertGroupColors = Boolean.FALSE;
        this.landscape = Boolean.FALSE;
        this.underlineRows = Boolean.FALSE;
        this.shadeOddRows = Boolean.FALSE;
        this.allowSecondaryTenants = Boolean.FALSE;
    }

    public String getTitle() {
        return title;
    }

    @XmlAttribute(required = true)
    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessor() {
        return processor;
    }

    @XmlAttribute
    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getReportable() {
        return reportable;
    }

    @XmlAttribute
    public void setReportable(String reportable) {
        this.reportable = reportable;
    }

    public ReportColumnsConfig getColumns() {
        return columns;
    }

    @XmlElement
    public void setColumns(ReportColumnsConfig columns) {
        this.columns = columns;
    }

    public ReportPlacementsConfig getPlacements() {
        return placements;
    }

    @XmlElement
    public void setPlacements(ReportPlacementsConfig placements) {
        this.placements = placements;
    }

    public ParametersConfig getParameters() {
        return parameters;
    }

    @XmlElement
    public void setParameters(ParametersConfig parameters) {
        this.parameters = parameters;
    }

    public FilterConfig getFilter() {
        return filter;
    }

    @XmlElement(name = "filter")
    public void setFilter(FilterConfig filter) {
        this.filter = filter;
    }

    public String getTemplate() {
        return template;
    }

    @XmlAttribute
    public void setTemplate(String template) {
        this.template = template;
    }

    public ReportLayoutType getLayout() {
        return layout;
    }

    @XmlJavaTypeAdapter(ReportLayoutTypeXmlAdapter.class)
    @XmlAttribute
    public void setLayout(ReportLayoutType layout) {
        this.layout = layout;
    }

    public boolean getShowGrandFooter() {
        return showGrandFooter;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setShowGrandFooter(Boolean showGrandFooter) {
        this.showGrandFooter = showGrandFooter;
    }

    public Boolean getInvertGroupColors() {
        return invertGroupColors;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setInvertGroupColors(Boolean invertGroupColors) {
        this.invertGroupColors = invertGroupColors;
    }

    public Boolean getLandscape() {
        return landscape;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setLandscape(Boolean landscape) {
        this.landscape = landscape;
    }

    public boolean getUnderlineRows() {
        return underlineRows;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setUnderlineRows(Boolean underlineRows) {
        this.underlineRows = underlineRows;
    }

    public Boolean getShadeOddRows() {
        return shadeOddRows;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setShadeOddRows(Boolean shadeOddRows) {
        this.shadeOddRows = shadeOddRows;
    }

    public Boolean getAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setAllowSecondaryTenants(Boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }
}
