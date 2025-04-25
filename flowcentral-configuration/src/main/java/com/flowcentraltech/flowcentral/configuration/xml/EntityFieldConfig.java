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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.EntityFieldDataTypeXmlAdapter;
import com.tcdng.unify.core.constant.TextCase;
import com.tcdng.unify.core.util.xml.adapter.TextCaseXmlAdapter;

/**
 * Entity field configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class EntityFieldConfig extends BaseConfig {

    @JsonSerialize(using = EntityFieldDataTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = EntityFieldDataTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private EntityFieldDataType type;

    @JsonSerialize(using = TextCaseXmlAdapter.Serializer.class)
    @JsonDeserialize(using = TextCaseXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "case")
    private TextCase textCase;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String label;

    @JacksonXmlProperty(isAttribute = true, localName = "column")
    private String columnName;

    @JacksonXmlProperty(isAttribute = true)
    private String references;

    @JacksonXmlProperty(isAttribute = true)
    private String jsonName;

    @JacksonXmlProperty(isAttribute = true)
    private String jsonFormatter;

    @JacksonXmlProperty(isAttribute = true)
    private String key;

    @JacksonXmlProperty(isAttribute = true)
    private String property;

    @JacksonXmlProperty(isAttribute = true)
    private String category;

    @JacksonXmlProperty(isAttribute = true)
    private String inputLabel;

    @JacksonXmlProperty(isAttribute = true)
    private String inputWidget;

    @JacksonXmlProperty(isAttribute = true)
    private String suggestionType;

    @JacksonXmlProperty(isAttribute = true)
    private String inputListKey;

    @JacksonXmlProperty(isAttribute = true)
    private String lingualWidget;

    @JacksonXmlProperty(isAttribute = true)
    private String lingualListKey;

    @JacksonXmlProperty(isAttribute = true)
    private String autoFormat;

    @JacksonXmlProperty(isAttribute = true)
    private String defaultVal;

    @JacksonXmlProperty(isAttribute = true)
    private String mapped;

    @JacksonXmlProperty(isAttribute = true)
    private Integer columns;

    @JacksonXmlProperty(isAttribute = true)
    private Integer rows;

    @JacksonXmlProperty(isAttribute = true)
    private Integer maxLen;

    @JacksonXmlProperty(isAttribute = true)
    private Integer minLen;

    @JacksonXmlProperty(isAttribute = true)
    private Integer precision;

    @JacksonXmlProperty(isAttribute = true)
    private Integer scale;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean branchScoping;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean trim;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean allowNegative;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean readOnly;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean nullable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean auditable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean reportable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean descriptive;

    @JacksonXmlProperty(isAttribute = true, localName = "maintainAction")
    private Boolean maintainLink;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean basicSearch;

    public EntityFieldConfig() {
        this.branchScoping = Boolean.FALSE;
        this.trim = Boolean.FALSE;
        this.allowNegative = Boolean.FALSE;
        this.readOnly = Boolean.FALSE;
        this.nullable = Boolean.FALSE;
        this.auditable = Boolean.FALSE;
        this.reportable = Boolean.TRUE;
        this.descriptive = Boolean.FALSE;
        this.maintainLink = Boolean.FALSE;
        this.basicSearch = Boolean.FALSE;
    }

    public EntityFieldDataType getType() {
        return type;
    }

    public void setType(EntityFieldDataType type) {
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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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

    public Boolean getBranchScoping() {
        return branchScoping;
    }

    public void setBranchScoping(Boolean branchScoping) {
        this.branchScoping = branchScoping;
    }

    public Boolean getTrim() {
        return trim;
    }

    public void setTrim(Boolean trim) {
        this.trim = trim;
    }

    public Boolean getAllowNegative() {
        return allowNegative;
    }

    public void setAllowNegative(Boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Boolean getAuditable() {
        return auditable;
    }

    public void setAuditable(Boolean auditable) {
        this.auditable = auditable;
    }

    public Boolean getReportable() {
        return reportable;
    }

    public void setReportable(Boolean reportable) {
        this.reportable = reportable;
    }

    public Boolean getDescriptive() {
        return descriptive;
    }

    public void setDescriptive(Boolean descriptive) {
        this.descriptive = descriptive;
    }

    public Boolean getMaintainLink() {
        return maintainLink;
    }

    public void setMaintainLink(Boolean maintainLink) {
        this.maintainLink = maintainLink;
    }

    public Boolean getBasicSearch() {
        return basicSearch;
    }

    public void setBasicSearch(Boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

}
