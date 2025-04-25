/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.SecuredLinkType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Secured link.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_SECUREDLINK")
public class SecuredLink extends BaseAuditEntity {

    @ForeignKey(name = "SECURED_LINK_TY", nullable = true)
    private SecuredLinkType type;
    
    @Column(length = 128, nullable = true)
    private String title;

    @Column(length = 256)
    private String contentPath;

    @Column(length = 32)
    private String accessKey;

    @Column(length = 64, nullable = true)
    private String assignedToLoginId;

    @Column(length = 64, nullable = true)
    private String assignedRole;
    
    @Column(type = ColumnType.TIMESTAMP)
    private Date expiresOn;

    @Column(length = 64, nullable = true)
    private String lastAccessedBy;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date lastAccessedOn;

    @Column(nullable = true)
    private Boolean invalidated;

    @ListOnly(key = "type", property="description")
    private String typeDesc;
    
    @Override
    public String getDescription() {
        return title;
    }

    public SecuredLinkType getType() {
        return type;
    }

    public void setType(SecuredLinkType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAssignedToLoginId() {
        return assignedToLoginId;
    }

    public void setAssignedToLoginId(String assignedToLoginId) {
        this.assignedToLoginId = assignedToLoginId;
    }

    public String getAssignedRole() {
        return assignedRole;
    }

    public void setAssignedRole(String assignedRole) {
        this.assignedRole = assignedRole;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    public String getLastAccessedBy() {
        return lastAccessedBy;
    }

    public void setLastAccessedBy(String lastAccessedBy) {
        this.lastAccessedBy = lastAccessedBy;
    }

    public Date getLastAccessedOn() {
        return lastAccessedOn;
    }

    public void setLastAccessedOn(Date lastAccessedOn) {
        this.lastAccessedOn = lastAccessedOn;
    }

    public Boolean getInvalidated() {
        return invalidated;
    }

    public void setInvalidated(Boolean invalidated) {
        this.invalidated = invalidated;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
