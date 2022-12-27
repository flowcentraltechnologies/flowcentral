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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.EntityFieldDataTypeXmlAdapter;
import com.tcdng.unify.core.constant.TextCase;
import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalTrueToNullXmlAdapter;
import com.tcdng.unify.core.util.xml.adapter.TextCaseXmlAdapter;

/**
 * Entity field configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFieldConfig {

    private EntityFieldDataType type;

    private TextCase textCase;

    private String name;

    private String label;

    private String columnName;

    private String references;

    private String key;

    private String property;

    private String category;

    private String inputLabel;

    private String inputWidget;

    private String suggestionType;

    private String inputListKey;

    private String lingualWidget;

    private String lingualListKey;

    private String autoFormat;

    private String defaultVal;

    private String mapped;

    private Integer columns;

    private Integer rows;

    private Integer maxLen;

    private Integer minLen;

    private Integer precision;

    private Integer scale;

    private Boolean allowNegative;

    private Boolean nullable;

    private Boolean auditable;

    private Boolean reportable;

    private Boolean descriptive;

    private Boolean maintainLink;

    private Boolean basicSearch;

    public EntityFieldConfig() {
        this.allowNegative = Boolean.FALSE;
        this.nullable = Boolean.FALSE;
        this.auditable = Boolean.TRUE;
        this.reportable = Boolean.TRUE;
        this.descriptive = Boolean.FALSE;
        this.maintainLink = Boolean.FALSE;
        this.basicSearch = Boolean.FALSE;
    }

    public EntityFieldDataType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(EntityFieldDataTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(EntityFieldDataType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(required = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    @XmlAttribute(required = true)
    public void setLabel(String label) {
        this.label = label;
    }

    public String getColumnName() {
        return columnName;
    }

    @XmlAttribute(name = "column", required = true)
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getReferences() {
        return references;
    }

    @XmlAttribute
    public void setReferences(String references) {
        this.references = references;
    }

    public String getKey() {
        return key;
    }

    @XmlAttribute
    public void setKey(String key) {
        this.key = key;
    }

    public String getProperty() {
        return property;
    }

    @XmlAttribute
    public void setProperty(String property) {
        this.property = property;
    }

    public String getCategory() {
        return category;
    }

    @XmlAttribute
    public void setCategory(String category) {
        this.category = category;
    }

    public String getInputLabel() {
        return inputLabel;
    }

    @XmlAttribute
    public void setInputLabel(String inputLabel) {
        this.inputLabel = inputLabel;
    }

    public String getInputWidget() {
        return inputWidget;
    }

    @XmlAttribute
    public void setInputWidget(String inputWidget) {
        this.inputWidget = inputWidget;
    }

    public String getSuggestionType() {
        return suggestionType;
    }

    @XmlAttribute
    public void setSuggestionType(String suggestionType) {
        this.suggestionType = suggestionType;
    }

    public String getInputListKey() {
        return inputListKey;
    }

    @XmlAttribute
    public void setInputListKey(String inputListKey) {
        this.inputListKey = inputListKey;
    }

    public String getLingualWidget() {
        return lingualWidget;
    }

    @XmlAttribute
    public void setLingualWidget(String lingualWidget) {
        this.lingualWidget = lingualWidget;
    }

    public String getLingualListKey() {
        return lingualListKey;
    }

    @XmlAttribute
    public void setLingualListKey(String lingualListKey) {
        this.lingualListKey = lingualListKey;
    }

    public String getAutoFormat() {
        return autoFormat;
    }

    @XmlAttribute
    public void setAutoFormat(String autoFormat) {
        this.autoFormat = autoFormat;
    }

    public String getMapped() {
        return mapped;
    }

    @XmlAttribute
    public void setMapped(String mapped) {
        this.mapped = mapped;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    @XmlAttribute
    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public TextCase getTextCase() {
        return textCase;
    }

    @XmlJavaTypeAdapter(TextCaseXmlAdapter.class)
    @XmlAttribute(name = "case")
    public void setTextCase(TextCase textCase) {
        this.textCase = textCase;
    }

    public Integer getColumns() {
        return columns;
    }

    @XmlAttribute
    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getRows() {
        return rows;
    }

    @XmlAttribute
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getMaxLen() {
        return maxLen;
    }

    @XmlAttribute
    public void setMaxLen(Integer maxLen) {
        this.maxLen = maxLen;
    }

    public Integer getMinLen() {
        return minLen;
    }

    @XmlAttribute
    public void setMinLen(Integer minLen) {
        this.minLen = minLen;
    }

    public Integer getPrecision() {
        return precision;
    }

    @XmlAttribute
    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getScale() {
        return scale;
    }

    @XmlAttribute
    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Boolean getAllowNegative() {
        return allowNegative;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setAllowNegative(Boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public Boolean getNullable() {
        return nullable;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public Boolean getAuditable() {
        return auditable;
    }

    @XmlJavaTypeAdapter(MarshalTrueToNullXmlAdapter.class)
    @XmlAttribute
    public void setAuditable(Boolean auditable) {
        this.auditable = auditable;
    }

    public Boolean getReportable() {
        return reportable;
    }

    @XmlJavaTypeAdapter(MarshalTrueToNullXmlAdapter.class)
    @XmlAttribute
    public void setReportable(Boolean reportable) {
        this.reportable = reportable;
    }

    public Boolean getDescriptive() {
        return descriptive;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setDescriptive(Boolean descriptive) {
        this.descriptive = descriptive;
    }

    public Boolean getMaintainLink() {
        return maintainLink;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute(name = "maintainAction")
    public void setMaintainLink(Boolean maintainLink) {
        this.maintainLink = maintainLink;
    }

    public Boolean getBasicSearch() {
        return basicSearch;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setBasicSearch(Boolean basicSearch) {
        this.basicSearch = basicSearch;
    }

}
