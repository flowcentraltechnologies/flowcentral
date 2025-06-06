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
package com.flowcentraltech.flowcentral.common.entities;

import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.CategoryColumn;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.FosterParentId;
import com.tcdng.unify.core.annotation.FosterParentType;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Common file attachment entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_FILEATTACHMENT", indexes = { @Index(value = { "entity" }), @Index(value = { "category" }) })
public class FileAttachment extends BaseAuditEntity {

    @ForeignKey(name = "ATTACHMENT_TY")
    private FileAttachmentType type;

    @FosterParentId
    private Long entityInstId;

    @FosterParentType(length = 128)
    private String entity;

    @CategoryColumn(name = "ATTACHMENT_CAT")
    private String category;

    @Column(length = 64)
    private String name;

    @Column(length = 128)
    private String title;

    @Column(name = "FILE_NM", length = 64, nullable = true)
    private String fileName;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @Child
    private FileAttachmentDoc file;

    @Override
    public String getDescription() {
        return title;
    }

    public FileAttachmentType getType() {
        return type;
    }

    public void setType(FileAttachmentType type) {
        this.type = type;
    }

    public Long getEntityInstId() {
        return entityInstId;
    }

    public void setEntityInstId(Long entityInstId) {
        this.entityInstId = entityInstId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public FileAttachmentDoc getFile() {
        return file;
    }

    public void setFile(FileAttachmentDoc file) {
        this.file = file;
    }

}
