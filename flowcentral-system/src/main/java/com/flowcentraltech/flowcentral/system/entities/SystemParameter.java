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
package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.configuration.constants.SysParamType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * System parameter entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_SYSPARAMETER",
        uniqueConstraints = { @UniqueConstraint({ "code" }), @UniqueConstraint({ "description" }) })
public class SystemParameter extends BaseAuditEntity {

    @ForeignKey(Module.class)
    private Long moduleId;

    @ForeignKey(name = "PARAM_TY")
    private SysParamType type;

    @Column(name = "PARAMETER_CD", length = 32)
    private String code;

    @Column(name = "PARAMETER_DESC", length = 196)
    private String description;

    @Column(name = "PARAM_VAL", length = 512, nullable = true)
    private String value;

    @Column(name = "DEFAULT_PARAM_VAL", length = 512, nullable = true)
    private String defaultValue;

    @Column(length = 256)
    private String editor;

    @Column
    private Boolean control;

    @Column
    private Boolean editable;

    @ListOnly(key = "moduleId", property = "name")
    private String moduleName;

    @ListOnly(key = "moduleId", property = "description")
    private String moduleDesc;

    @ListOnly(key = "moduleId", property = "label")
    private String moduleLabel;

    @ListOnly(key = "moduleId", property = "shortCode")
    private String moduleShortCode;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return code;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public SysParamType getType() {
        return type;
    }

    public void setType(SysParamType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Boolean getControl() {
        return control;
    }

    public void setControl(Boolean control) {
        this.control = control;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleShortCode() {
        return moduleShortCode;
    }

    public void setModuleShortCode(String moduleShortCode) {
        this.moduleShortCode = moduleShortCode;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public String getModuleLabel() {
        return moduleLabel;
    }

    public void setModuleLabel(String moduleLabel) {
        this.moduleLabel = moduleLabel;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
