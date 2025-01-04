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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntity;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.constant.TextCase;

/**
 * Application entity field.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Policy("appentityfield-entitypolicy")
@Table(name = "FC_ENTITYFIELD", uniqueConstraints = { @UniqueConstraint({ "appEntityId", "name" }) },
        indexes = { @Index({ "references" }) })
public class AppEntityField extends BaseConfigEntity {

    @ForeignKey(AppEntity.class)
    private Long appEntityId;

    @ForeignKey(name = "DATA_TYPE")
    private EntityFieldDataType dataType;

    @ForeignKey(name = "FIELD_TYPE")
    private EntityFieldType type;

    @Column(nullable = true)
    private TextCase textCase;

    @Column(name = "ENTITYFIELD_NM", length = 64)
    private String name;

    @Column(name = "ENTITYFIELD_LABEL", length = 128)
    private String label;

    @Column(name = "ENTITY_REF", length = 128, nullable = true)
    private String references;

    @Column(name = "JSON_NM", length = 64, nullable = true)
    private String jsonName;

    @Column(name = "JSON_FORMATTER", length = 64, nullable = true)
    private String jsonFormatter;

    @Column(name = "LIST_KEY", length = 64, nullable = true)
    private String key;

    @Column(name = "LIST_PROP", length = 64, nullable = true)
    private String property;

    @Column(name = "COLUMN_NM", length = 32, nullable = true)
    private String columnName;

    @Column(length = 64, nullable = true)
    private String category;

    @Column(length = 98, nullable = true)
    private String inputLabel;

    @Column(length = 128, nullable = true)
    private String inputWidget;

    @Column(length = 128, nullable = true)
    private String suggestionType;

    @Column(length = 64, nullable = true)
    private String inputListKey;

    @Column(length = 128, nullable = true)
    private String lingualWidget;

    @Column(length = 64, nullable = true)
    private String lingualListKey;

    @Column(length = 64, nullable = true)
    private String autoFormat;

    @Column(length = 64, nullable = true)
    private String mapped;

    @Column(length = 32, nullable = true)
    private String defaultVal;

    @Column(name = "FIELD_COLUMNS", nullable = true)
    private Integer columns;

    @Column(name = "FIELD_ROWS", nullable = true)
    private Integer rows;

    @Column(name = "MAX_LEN", nullable = true)
    private Integer maxLen;

    @Column(name = "MIN_LEN", nullable = true)
    private Integer minLen;

    @Column(name = "FIELD_PRECISION", nullable = true)
    private Integer precision;

    @Column(name = "FIELD_SCALE", nullable = true)
    private Integer scale;

    @Column(name = "BRANCH_SCOPING")
    private boolean branchScoping;

    @Column(name = "TRIM_FG")
    private boolean trim;

    @Column(name = "ALLOW_NEGATIVE_FG")
    private boolean allowNegative;

    @Column(name = "READONLY_FG")
    private boolean readOnly;

    @Column(name = "NULLABLE_FG")
    private boolean nullable;

    @Column(name = "AUDITABLE_FG")
    private boolean auditable;

    @Column(name = "REPORTABLE_FG")
    private boolean reportable;

    @Column(name = "DESCRIPTIVE_FG")
    private boolean descriptive;

    @Column(name = "MAINTAINLINK_FG")
    private boolean maintainLink;

    @Column(name = "BASIC_SEARCH_FG")
    private boolean basicSearch;

    @ListOnly(key = "appEntityId", property = "name")
    private String appEntityName;

    @ListOnly(key = "appEntityId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "dataType", property = "description")
    private String dataTypeDesc;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    public AppEntityField(EntityFieldDataType dataType, String name, String label, String references, String jsonName,
            String jsonFormatter, String key, String property, String category, String inputLabel, String inputWidget,
            String suggestionType, String inputListKey, Integer maxLen, boolean branchScoping, boolean trim,
            boolean allowNegative, boolean readOnly, boolean nullable, boolean auditable, boolean reportable,
            boolean maintainLink) {
        setConfigType(ConfigType.STATIC);
        this.dataType = dataType;
        this.name = name;
        this.label = label;
        this.references = references;
        this.jsonName = jsonName;
        this.jsonFormatter = jsonFormatter;
        this.key = key;
        this.property = property;
        this.category = category;
        this.inputLabel = inputLabel;
        this.inputWidget = inputWidget;
        this.suggestionType = suggestionType;
        this.inputListKey = inputListKey;
        this.maxLen = maxLen;
        this.branchScoping = branchScoping;
        this.trim = trim;
        this.allowNegative = allowNegative;
        this.readOnly = readOnly;
        this.nullable = nullable;
        this.auditable = auditable;
        this.reportable = reportable;
        this.maintainLink = maintainLink;
    }

    public AppEntityField() {
        this.type = EntityFieldType.CUSTOM;
    }

    @Override
    public String getDescription() {
        return label;
    }

    public Long getAppEntityId() {
        return appEntityId;
    }

    public void setAppEntityId(Long appEntityId) {
        this.appEntityId = appEntityId;
    }

    public EntityFieldDataType getDataType() {
        return dataType;
    }

    public void setDataType(EntityFieldDataType dataType) {
        this.dataType = dataType;
    }

    public EntityFieldType getType() {
        return type;
    }

    public void setType(EntityFieldType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getJsonName() {
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public String getJsonFormatter() {
        return jsonFormatter;
    }

    public void setJsonFormatter(String jsonFormatter) {
        this.jsonFormatter = jsonFormatter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInputLabel() {
        return inputLabel;
    }

    public void setInputLabel(String inputLabel) {
        this.inputLabel = inputLabel;
    }

    public String getInputWidget() {
        return inputWidget;
    }

    public void setInputWidget(String inputWidget) {
        this.inputWidget = inputWidget;
    }

    public String getSuggestionType() {
        return suggestionType;
    }

    public void setSuggestionType(String suggestionType) {
        this.suggestionType = suggestionType;
    }

    public String getInputListKey() {
        return inputListKey;
    }

    public void setInputListKey(String inputListKey) {
        this.inputListKey = inputListKey;
    }

    public String getLingualWidget() {
        return lingualWidget;
    }

    public void setLingualWidget(String lingualWidget) {
        this.lingualWidget = lingualWidget;
    }

    public String getLingualListKey() {
        return lingualListKey;
    }

    public void setLingualListKey(String lingualListKey) {
        this.lingualListKey = lingualListKey;
    }

    public String getAutoFormat() {
        return autoFormat;
    }

    public void setAutoFormat(String autoFormat) {
        this.autoFormat = autoFormat;
    }

    public String getMapped() {
        return mapped;
    }

    public void setMapped(String mapped) {
        this.mapped = mapped;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getAppEntityName() {
        return appEntityName;
    }

    public void setAppEntityName(String appEntityName) {
        this.appEntityName = appEntityName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(Integer maxLen) {
        this.maxLen = maxLen;
    }

    public Integer getMinLen() {
        return minLen;
    }

    public void setMinLen(Integer minLen) {
        this.minLen = minLen;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public boolean isBranchScoping() {
        return branchScoping;
    }

    public void setBranchScoping(boolean branchScoping) {
        this.branchScoping = branchScoping;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    public boolean isAllowNegative() {
        return allowNegative;
    }

    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isAuditable() {
        return auditable;
    }

    public void setAuditable(boolean auditable) {
        this.auditable = auditable;
    }

    public boolean isReportable() {
        return reportable;
    }

    public void setReportable(boolean reportable) {
        this.reportable = reportable;
    }

    public boolean isDescriptive() {
        return descriptive;
    }

    public void setDescriptive(boolean descriptive) {
        this.descriptive = descriptive;
    }

    public boolean isMaintainLink() {
        return maintainLink;
    }

    public void setMaintainLink(boolean maintainLink) {
        this.maintainLink = maintainLink;
    }

    public boolean isBasicSearch() {
        return basicSearch;
    }

    public void setBasicSearch(boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

    public String getDataTypeDesc() {
        return dataTypeDesc;
    }

    public void setDataTypeDesc(String dataTypeDesc) {
        this.dataTypeDesc = dataTypeDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public TextCase getTextCase() {
        return textCase;
    }

    public void setTextCase(TextCase textCase) {
        this.textCase = textCase;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

}
