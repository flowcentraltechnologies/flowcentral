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

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Application entity entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ENTITY", uniqueConstraints = { @UniqueConstraint({ "entityClass" }) })
public class AppEntity extends BaseApplicationEntity {

    @ForeignKey(name = "ENTITY_BASE_TY")
    private EntityBaseType baseType;

    @Column(length = 128)
    private String label;

    @Column(length = 64, nullable = true)
    private String emailProducerConsumer;

    @Column(length = 64, nullable = true)
    private String delegate;

    @Column(length = 128)
    private String entityClass;

    @Column(name = "DATASOURCE_NM", length = 64, nullable = true)
    private String dataSourceName;

    @Column(name = "TABLE_NM", length = 64)
    private String tableName;

    @Column(name = "DATE_FORMATTER", length = 64, nullable = true)
    private String dateFormatter;

    @Column(name = "DATETIME_FORMATTER", length = 64, nullable = true)
    private String dateTimeFormatter;

    @Column(name = "SUPPORTS_CHANGE_EVENT_FG")
    private boolean supportsChangeEvents;
    
    @Column(name = "AUDITABLE_FG")
    private boolean auditable;

    @Column(name = "MAPPED_FG")
    private boolean mapped;

    @Column(name = "REPORTABLE_FG")
    private boolean reportable;

    @Column(name = "ACTION_POLICY_FG")
    private boolean actionPolicy;

    @Column(name = "SCHEMA_UPDATE_REQUIRED_FG")
    private boolean schemaUpdateRequired;

    @ListOnly(key = "baseType", property = "description")
    private String baseTypeDesc;

    @ChildList
    private List<AppEntityField> fieldList;

    @ChildList
    private List<AppEntityAttachment> attachmentList;

    @ChildList
    private List<AppEntityExpression> expressionList;

    @ChildList
    private List<AppEntityUniqueConstraint> uniqueConstraintList;

    @ChildList
    private List<AppEntityIndex> indexList;

    @ChildList
    private List<AppEntityUpload> uploadList;

    @ChildList
    private List<AppEntitySearchInput> searchInputList;

    @ChildList
    private List<AppEntitySeries> seriesList;

    @ChildList
    private List<AppEntityCategory> categoryList;

    public EntityBaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(EntityBaseType baseType) {
        this.baseType = baseType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public boolean isSupportsChangeEvents() {
        return supportsChangeEvents;
    }

    public void setSupportsChangeEvents(boolean supportsChangeEvents) {
        this.supportsChangeEvents = supportsChangeEvents;
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

    public boolean isActionPolicy() {
        return actionPolicy;
    }

    public void setActionPolicy(boolean actionPolicy) {
        this.actionPolicy = actionPolicy;
    }

    public boolean isSchemaUpdateRequired() {
        return schemaUpdateRequired;
    }

    public void setSchemaUpdateRequired(boolean schemaUpdateRequired) {
        this.schemaUpdateRequired = schemaUpdateRequired;
    }

    public boolean isMapped() {
        return mapped;
    }

    public void setMapped(boolean mapped) {
        this.mapped = mapped;
    }

    public String getBaseTypeDesc() {
        return baseTypeDesc;
    }

    public void setBaseTypeDesc(String baseTypeDesc) {
        this.baseTypeDesc = baseTypeDesc;
    }

    public List<AppEntityField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<AppEntityField> fieldList) {
        this.fieldList = fieldList;
    }

    public List<AppEntityAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AppEntityAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<AppEntityExpression> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<AppEntityExpression> expressionList) {
        this.expressionList = expressionList;
    }

    public List<AppEntityUniqueConstraint> getUniqueConstraintList() {
        return uniqueConstraintList;
    }

    public void setUniqueConstraintList(List<AppEntityUniqueConstraint> uniqueConstraintList) {
        this.uniqueConstraintList = uniqueConstraintList;
    }

    public List<AppEntityIndex> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<AppEntityIndex> indexList) {
        this.indexList = indexList;
    }

    public List<AppEntityUpload> getUploadList() {
        return uploadList;
    }

    public void setUploadList(List<AppEntityUpload> uploadList) {
        this.uploadList = uploadList;
    }

    public List<AppEntitySearchInput> getSearchInputList() {
        return searchInputList;
    }

    public void setSearchInputList(List<AppEntitySearchInput> searchInputList) {
        this.searchInputList = searchInputList;
    }

    public List<AppEntitySeries> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<AppEntitySeries> seriesList) {
        this.seriesList = seriesList;
    }

    public List<AppEntityCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<AppEntityCategory> categoryList) {
        this.categoryList = categoryList;
    }

}
