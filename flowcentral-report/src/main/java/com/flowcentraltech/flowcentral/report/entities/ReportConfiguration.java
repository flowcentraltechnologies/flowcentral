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
package com.flowcentraltech.flowcentral.report.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.report.ReportLayoutType;

/**
 * Report configuration data. Represents a report setup.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_REPORTCONFIG")
public class ReportConfiguration extends BaseApplicationEntity {

    @Column(length = 128)
    private String reportable;

    @Column(length = 96)
    private String title;

    @Column(length = 64, nullable = true)
    private String template;

    @Column(length = 64, nullable = true)
    private ReportLayoutType layout;

    @Column(length = 64, nullable = true)
    private String processor;

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

    @ChildList
    private List<ReportColumn> columnList;

    @ChildList
    private List<ReportParameter> parameterList;

    @Child(category = "report-config")
    private AppFilter filter;

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

    public ReportLayoutType getLayout() {
        return layout;
    }

    public void setLayout(ReportLayoutType layout) {
        this.layout = layout;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
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

    public List<ReportColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ReportColumn> columnList) {
        this.columnList = columnList;
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

}
