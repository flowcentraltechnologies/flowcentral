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
package com.flowcentraltech.flowcentral.report.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.constant.DataType;

/**
 * Report parameter.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_REPORTPARAM", uniqueConstraints = { @UniqueConstraint({ "reportConfigurationId", "name" }) })
public class ReportParameter extends BaseEntity {

    @ForeignKey(ReportConfiguration.class)
    private Long reportConfigurationId;

    @ForeignKey
    private DataType type;

    @Column(name = "PARAMETER_NM")
    private String name;

    @Column(name = "PARAMETER_DESC", length = 128)
    private String description;

    @Column(name = "PARAMETER_LABEL", length = 128)
    private String label;

    @Column(name = "MANDATORY_FG")
    private Boolean mandatory;

    @Column(name = "DEFAULT_VAL", nullable = true, length = 128)
    private String defaultVal;

    @Column(name = "EDITOR", length = 128, nullable = true)
    private String editor;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getReportConfigurationId() {
        return reportConfigurationId;
    }

    public void setReportConfigurationId(Long reportConfigurationId) {
        this.reportConfigurationId = reportConfigurationId;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
