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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.EntityBaseTypeXmlAdapter;

/**
 * Entity configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class AppEntityConfig extends BaseNameConfig {

    @JsonSerialize(using = EntityBaseTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = EntityBaseTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private EntityBaseType baseType;
    
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String emailProducerConsumer;
    
    @JacksonXmlProperty(isAttribute = true)
    private String delegate;

    @JacksonXmlProperty(isAttribute = true)
    private String table;
    
    @JacksonXmlProperty(isAttribute = true)
    private String dataSourceName;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean mapped;

    @JacksonXmlProperty(isAttribute = true)
    private boolean supportsChangeEvents;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean auditable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean reportable;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean actionPolicy;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "field")
    private List<EntityFieldConfig> entityFieldList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "attachment")
    private List<EntityAttachmentConfig> attachmentList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "expression")
    private List<EntityExpressionConfig> expressionList;
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "uniqueConstraint")
    private List<EntityUniqueConstraintConfig> uniqueConstraintList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "index")
    private List<EntityIndexConfig> indexList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "upload")
    private List<EntityUploadConfig> uploadList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "searchInput")
    private List<EntitySearchInputConfig> searchInputList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "series")
    private List<EntitySeriesConfig> seriesList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "category")
    private List<EntityCategoryConfig> categoryList;
    
    public AppEntityConfig() {
        this.mapped = Boolean.FALSE;
        this.supportsChangeEvents = Boolean.FALSE;
        this.auditable = Boolean.FALSE;
        this.reportable = Boolean.FALSE;
        this.actionPolicy = Boolean.FALSE;
    }
    
    public EntityBaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(EntityBaseType baseType) {
        this.baseType = baseType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmailProducerConsumer() {
        return emailProducerConsumer;
    }

    public void setEmailProducerConsumer(String emailProducerConsumer) {
        this.emailProducerConsumer = emailProducerConsumer;
    }

    public String getDelegate() {
        return delegate;
    }

    public void setDelegate(String delegate) {
        this.delegate = delegate;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public Boolean getMapped() {
        return mapped;
    }

    public void setMapped(Boolean mapped) {
        this.mapped = mapped;
    }

    public boolean isSupportsChangeEvents() {
        return supportsChangeEvents;
    }

    public void setSupportsChangeEvents(boolean supportsChangeEvents) {
        this.supportsChangeEvents = supportsChangeEvents;
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

    public Boolean getActionPolicy() {
        return actionPolicy;
    }

    public void setActionPolicy(Boolean actionPolicy) {
        this.actionPolicy = actionPolicy;
    }

    public List<EntityFieldConfig> getEntityFieldList() {
        return entityFieldList;
    }

    public void setEntityFieldList(List<EntityFieldConfig> entityFieldList) {
        this.entityFieldList = entityFieldList;
    }

    public List<EntityAttachmentConfig> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<EntityAttachmentConfig> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<EntityExpressionConfig> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<EntityExpressionConfig> expressionList) {
        this.expressionList = expressionList;
    }

    public List<EntityUniqueConstraintConfig> getUniqueConstraintList() {
        return uniqueConstraintList;
    }

    public void setUniqueConstraintList(List<EntityUniqueConstraintConfig> uniqueConstraintList) {
        this.uniqueConstraintList = uniqueConstraintList;
    }

    public List<EntityIndexConfig> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<EntityIndexConfig> indexList) {
        this.indexList = indexList;
    }

    public List<EntityUploadConfig> getUploadList() {
        return uploadList;
    }

    public void setUploadList(List<EntityUploadConfig> uploadList) {
        this.uploadList = uploadList;
    }

    public List<EntitySearchInputConfig> getSearchInputList() {
        return searchInputList;
    }

    public void setSearchInputList(List<EntitySearchInputConfig> searchInputList) {
        this.searchInputList = searchInputList;
    }

    public List<EntitySeriesConfig> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<EntitySeriesConfig> seriesList) {
        this.seriesList = seriesList;
    }

    public List<EntityCategoryConfig> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<EntityCategoryConfig> categoryList) {
        this.categoryList = categoryList;
    }

}
