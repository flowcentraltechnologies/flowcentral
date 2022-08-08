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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

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

    @Column(length = 96)
    private String label;

    @Column(length = 64, nullable = true)
    private String emailProducerConsumer;

    @Column(length = 64, nullable = true)
    private String delegate;

    @Column(length = 128)
    private String entityClass;

    @Column(name = "TABLE_NM", length = 32)
    private String tableName;

    @Column(name = "AUDITABLE_FG")
    private boolean auditable;

    @Column(name = "REPORTABLE_FG")
    private boolean reportable;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean getAuditable() {
        return auditable;
    }

    public void setAuditable(boolean auditable) {
        this.auditable = auditable;
    }

    public boolean getReportable() {
        return reportable;
    }

    public void setReportable(boolean reportable) {
        this.reportable = reportable;
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

}
