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
package com.flowcentraltech.flowcentral.audit.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;

/**
 * Entity audit keys query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityAuditKeysQuery extends BaseAuditEntityQuery<EntityAuditKeys> {

    public EntityAuditKeysQuery() {
        super(EntityAuditKeys.class);
    }

    public EntityAuditKeysQuery entityAuditDetailsId(Long entityAuditDetailsId) {
        return (EntityAuditKeysQuery) addEquals("entityAuditDetailsId", entityAuditDetailsId);
    }

    public EntityAuditKeysQuery entityAuditConfigId(Long entityAuditConfigId) {
        return (EntityAuditKeysQuery) addEquals("entityAuditConfigId", entityAuditConfigId);
    }

    public EntityAuditKeysQuery entityId(Long entityId) {
        return (EntityAuditKeysQuery) addEquals("entityId", entityId);
    }

    public EntityAuditKeysQuery auditConfigName(String auditConfigName) {
        return (EntityAuditKeysQuery) addEquals("auditConfigName", auditConfigName);
    }

    public EntityAuditKeysQuery eventType(AuditEventType eventType) {
        return (EntityAuditKeysQuery) addEquals("eventType", eventType);
    }

    public EntityAuditKeysQuery auditNo(String auditNo) {
        return (EntityAuditKeysQuery) addEquals("auditNo", auditNo);
    }

    public EntityAuditKeysQuery userLoginId(String userLoginId) {
        return (EntityAuditKeysQuery) addEquals("userLoginId", userLoginId);
    }

}
