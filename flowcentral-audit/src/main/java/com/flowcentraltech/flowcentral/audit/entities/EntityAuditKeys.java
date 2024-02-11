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

package com.flowcentraltech.flowcentral.audit.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Index;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Entity audit keys entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ENTITYAUDITKEYS",
        indexes = { @Index({ "keyA" }), @Index({ "keyB" }), @Index({ "keyC" }), @Index({ "keyD" }) })
public class EntityAuditKeys extends BaseAuditEntity {

    @ForeignKey(EntityAuditConfig.class)
    private Long entityAuditConfigId;

    @ForeignKey(EntityAuditDetails.class)
    private Long entityAuditDetailsId;
    
    @Column(name = "KEY_A", length = 64, nullable = true)
    private String keyA;
    
    @Column(name = "KEY_B", length = 64, nullable = true)
    private String keyB;
    
    @Column(name = "KEY_C", length = 64, nullable = true)
    private String keyC;
    
    @Column(name = "KEY_D", length = 64, nullable = true)
    private String keyD;
    
    @ListOnly(key = "entityAuditConfigId", property = "name")
    private String auditConfigName;
    
    @ListOnly(key = "entityAuditConfigId", property = "description")
    private String auditConfigDesc;
   
    @ListOnly(key = "entityAuditConfigId", property = "entity")
    private String entity;

    @ListOnly(key = "entityAuditConfigId", property = "sourceTypeDesc")
    private String sourceTypeDesc;
    
    @ListOnly(key = "entityAuditDetailsId", property = "entityId")
    private Long entityId;
   
    @ListOnly(key = "entityAuditDetailsId", property = "auditNo")
    private String auditNo;

    @ListOnly(key = "entityAuditDetailsId", property = "eventType")
    private AuditEventType eventType;

    @ListOnly(key = "entityAuditDetailsId", property = "eventTimestamp")
    private Date eventTimestamp;

    @ListOnly(key = "entityAuditDetailsId", property = "sourceName")
    private String sourceName;

    @ListOnly(key = "entityAuditDetailsId", property = "userLoginId")
    private String userLoginId;

    @ListOnly(key = "entityAuditDetailsId", property = "userName")
    private String userName;

    @ListOnly(key = "entityAuditDetailsId", property = "userIpAddress")
    private String userIpAddress;

    @ListOnly(key = "entityAuditDetailsId", property = "roleCode")
    private String roleCode;

    @ListOnly(key = "entityAuditDetailsId", property = "eventTypeDesc")
    private String eventTypeDesc;

    @Override
    public String getDescription() {
        return keyA;
    }

    public Long getEntityAuditConfigId() {
        return entityAuditConfigId;
    }

    public void setEntityAuditConfigId(Long entityAuditConfigId) {
        this.entityAuditConfigId = entityAuditConfigId;
    }

    public Long getEntityAuditDetailsId() {
        return entityAuditDetailsId;
    }

    public void setEntityAuditDetailsId(Long entityAuditDetailsId) {
        this.entityAuditDetailsId = entityAuditDetailsId;
    }

    public String getKeyA() {
        return keyA;
    }

    public void setKeyA(String keyA) {
        this.keyA = keyA;
    }

    public String getKeyB() {
        return keyB;
    }

    public void setKeyB(String keyB) {
        this.keyB = keyB;
    }

    public String getKeyC() {
        return keyC;
    }

    public void setKeyC(String keyC) {
        this.keyC = keyC;
    }

    public String getKeyD() {
        return keyD;
    }

    public void setKeyD(String keyD) {
        this.keyD = keyD;
    }

    public String getAuditConfigName() {
        return auditConfigName;
    }

    public void setAuditConfigName(String auditConfigName) {
        this.auditConfigName = auditConfigName;
    }

    public String getAuditConfigDesc() {
        return auditConfigDesc;
    }

    public void setAuditConfigDesc(String auditConfigDesc) {
        this.auditConfigDesc = auditConfigDesc;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getSourceTypeDesc() {
        return sourceTypeDesc;
    }

    public void setSourceTypeDesc(String sourceTypeDesc) {
        this.sourceTypeDesc = sourceTypeDesc;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getAuditNo() {
        return auditNo;
    }

    public void setAuditNo(String auditNo) {
        this.auditNo = auditNo;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public void setEventType(AuditEventType eventType) {
        this.eventType = eventType;
    }

    public Date getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Date eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
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
