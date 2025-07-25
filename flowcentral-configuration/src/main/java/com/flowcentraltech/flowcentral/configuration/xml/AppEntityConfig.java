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
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.EntityBaseTypeXmlAdapter;

/**
 * Entity configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class AppEntityConfig extends BaseClassifiedConfig {

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
    private String dateFormatter;

    @JacksonXmlProperty(isAttribute = true)
    private String dateTimeFormatter;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean mapped;

    @JacksonXmlProperty(isAttribute = true, localName = "changeEvents")
    private boolean supportsChangeEvents;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean auditable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean reportable;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean actionPolicy;

    @JacksonXmlProperty
    private EntityFieldsConfig fields;

    @JacksonXmlProperty
    private EntityAttachmentsConfig attachments;

    @JacksonXmlProperty
    private EntityExpressionsConfig expressions;
    
    @JacksonXmlProperty
    private EntityUniqueConstraintsConfig uniqueConstraints;

    @JacksonXmlProperty
    private EntityIndexesConfig indexes;

    @JacksonXmlProperty
    private EntityUploadsConfig uploads;

    @JacksonXmlProperty
    private EntitySearchInputsConfig searchInputs;

    @JacksonXmlProperty(localName = "seriesset")
    private EntitySeriesSetConfig seriesSet;

    @JacksonXmlProperty
    private EntityCategoriesConfig categories;
    
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

    public String getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(String dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public String getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(String dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
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

    public EntityFieldsConfig getFields() {
        return fields;
    }

    public void setFields(EntityFieldsConfig fields) {
        this.fields = fields;
    }

    public EntityAttachmentsConfig getAttachments() {
        return attachments;
    }

    public void setAttachments(EntityAttachmentsConfig attachments) {
        this.attachments = attachments;
    }

    public EntityExpressionsConfig getExpressions() {
        return expressions;
    }

    public void setExpressions(EntityExpressionsConfig expressions) {
        this.expressions = expressions;
    }

    public EntityUniqueConstraintsConfig getUniqueConstraints() {
        return uniqueConstraints;
    }

    public void setUniqueConstraints(EntityUniqueConstraintsConfig uniqueConstraints) {
        this.uniqueConstraints = uniqueConstraints;
    }

    public EntityIndexesConfig getIndexes() {
        return indexes;
    }

    public void setIndexes(EntityIndexesConfig indexes) {
        this.indexes = indexes;
    }

    public EntityUploadsConfig getUploads() {
        return uploads;
    }

    public void setUploads(EntityUploadsConfig uploads) {
        this.uploads = uploads;
    }

    public EntitySearchInputsConfig getSearchInputs() {
        return searchInputs;
    }

    public void setSearchInputs(EntitySearchInputsConfig searchInputs) {
        this.searchInputs = searchInputs;
    }

    public EntitySeriesSetConfig getSeriesSet() {
        return seriesSet;
    }

    public void setSeriesSet(EntitySeriesSetConfig seriesSet) {
        this.seriesSet = seriesSet;
    }

    public EntityCategoriesConfig getCategories() {
        return categories;
    }

    public void setCategories(EntityCategoriesConfig categories) {
        this.categories = categories;
    }

}
