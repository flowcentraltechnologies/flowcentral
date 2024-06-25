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
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Table;

/**
 * Secured link.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_SECUREDLINK")
public class SecuredLink extends BaseAuditTenantEntity {

    @Column(length = 128)
    private String title;

    @Column(length = 128)
    private String contentPath;

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

    @Override
    public String getDescription() {
        return title;
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

}
