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

package com.flowcentraltech.flowcentral.audit.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Index;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Entity audit details entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ENTITYAUDITDETAILS",
        indexes = { @Index({ "auditNo" }), @Index({ "eventType" }), @Index({ "userLoginId" }) })
public class EntityAuditDetails extends BaseAuditEntity {

    @ForeignKey(name = "event_type")
    private AuditEventType eventType;

    @Column(name = "ENTITY_ID")
    private Long entityId;
    
    @Column(name = "EVENT_TIMESTAMP", type = ColumnType.TIMESTAMP_UTC)
    private Date eventTimestamp;

    @Column(name = "AUDIT_NO", length = 24)
    private String auditNo;

    @Column(name = "SOURCE_NM", length = 128)
    private String sourceName;

    @Column(name = "USER_LOGIN_ID", length = 64)
    private String userLoginId;

    @Column(name = "USER_NM", length = 128)
    private String userName;

    @Column(name = "USER_IP_ADDRESS", length = 64)
    private String userIpAddress;

    @Column(name = "ROLE_CD", length = 32, nullable = true)
    private String roleCode;

    @ListOnly(key = "eventType", property = "description")
    private String eventTypeDesc;

    @Override
    public String getDescription() {
        return auditNo;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public void setEventType(AuditEventType eventType) {
        this.eventType = eventType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Date getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Date eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getAuditNo() {
        return auditNo;
    }

    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getEventTypeDesc() {
        return eventTypeDesc;
    }

    public void setEventTypeDesc(String eventTypeDesc) {
        this.eventTypeDesc = eventTypeDesc;
    }

}
