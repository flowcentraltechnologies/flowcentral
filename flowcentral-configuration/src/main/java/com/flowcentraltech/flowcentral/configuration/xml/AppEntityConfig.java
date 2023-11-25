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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;

/**
 * Entity configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppEntityConfig extends BaseNameConfig {

    private String type;

    private String emailProducerConsumer;
    
    private String delegate;

    private String table;
    
    private String dataSourceName;
    
    private Boolean mapped;
    
    private Boolean auditable;

    private Boolean reportable;
    
    private Boolean actionPolicy;

    private List<EntityFieldConfig> entityFieldList;

    private List<EntityAttachmentConfig> attachmentList;

    private List<EntityExpressionConfig> expressionList;
    
    private List<EntityUniqueConstraintConfig> uniqueConstraintList;

    private List<EntityIndexConfig> indexList;

    private List<EntityUploadConfig> uploadList;

    private List<EntitySearchInputConfig> searchInputList;

    private List<EntitySeriesConfig> seriesList;

    private List<EntityCategoryConfig> categoryList;
    
    public AppEntityConfig() {
        this.mapped = Boolean.FALSE;
        this.auditable = Boolean.FALSE;
        this.reportable = Boolean.FALSE;
        this.actionPolicy = Boolean.FALSE;
    }
    
    public String getType() {
        return type;
    }

    @XmlAttribute(required = true)
    public void setType(String type) {
        this.type = type;
    }

    public String getEmailProducerConsumer() {
        return emailProducerConsumer;
    }

    @XmlAttribute
    public void setEmailProducerConsumer(String emailProducerConsumer) {
        this.emailProducerConsumer = emailProducerConsumer;
    }

    public String getDelegate() {
        return delegate;
    }

    @XmlAttribute
    public void setDelegate(String delegate) {
        this.delegate = delegate;
    }

    public String getTable() {
        return table;
    }

    @XmlAttribute
    public void setTable(String table) {
        this.table = table;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    @XmlAttribute
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public Boolean getMapped() {
        return mapped;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setMapped(Boolean mapped) {
        this.mapped = mapped;
    }

    public Boolean getAuditable() {
        return auditable;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setAuditable(Boolean auditable) {
        this.auditable = auditable;
    }

    public Boolean getReportable() {
        return reportable;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setReportable(Boolean reportable) {
        this.reportable = reportable;
    }

    public Boolean getActionPolicy() {
        return actionPolicy;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setActionPolicy(Boolean actionPolicy) {
        this.actionPolicy = actionPolicy;
    }

    public List<EntityFieldConfig> getEntityFieldList() {
        return entityFieldList;
    }

    @XmlElement(name = "field", required = true)
    public void setEntityFieldList(List<EntityFieldConfig> entityFieldList) {
        this.entityFieldList = entityFieldList;
    }

    public List<EntityAttachmentConfig> getAttachmentList() {
        return attachmentList;
    }

    @XmlElement(name = "attachment")
    public void setAttachmentList(List<EntityAttachmentConfig> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<EntityExpressionConfig> getExpressionList() {
        return expressionList;
    }

    @XmlElement(name = "expression")
    public void setExpressionList(List<EntityExpressionConfig> expressionList) {
        this.expressionList = expressionList;
    }

    public List<EntityUniqueConstraintConfig> getUniqueConstraintList() {
        return uniqueConstraintList;
    }

    @XmlElement(name = "uniqueConstraint")
    public void setUniqueConstraintList(List<EntityUniqueConstraintConfig> uniqueConstraintList) {
        this.uniqueConstraintList = uniqueConstraintList;
    }

    public List<EntityIndexConfig> getIndexList() {
        return indexList;
    }

    @XmlElement(name = "index")
    public void setIndexList(List<EntityIndexConfig> indexList) {
        this.indexList = indexList;
    }

    public List<EntityUploadConfig> getUploadList() {
        return uploadList;
    }

    @XmlElement(name = "upload")
    public void setUploadList(List<EntityUploadConfig> uploadList) {
        this.uploadList = uploadList;
    }

    public List<EntitySearchInputConfig> getSearchInputList() {
        return searchInputList;
    }

    @XmlElement(name = "searchInput")
    public void setSearchInputList(List<EntitySearchInputConfig> searchInputList) {
        this.searchInputList = searchInputList;
    }

    public List<EntitySeriesConfig> getSeriesList() {
        return seriesList;
    }

    @XmlElement(name = "series")
    public void setSeriesList(List<EntitySeriesConfig> seriesList) {
        this.seriesList = seriesList;
    }

    public List<EntityCategoryConfig> getCategoryList() {
        return categoryList;
    }

    @XmlElement(name = "category")
    public void setCategoryList(List<EntityCategoryConfig> categoryList) {
        this.categoryList = categoryList;
    }

}
