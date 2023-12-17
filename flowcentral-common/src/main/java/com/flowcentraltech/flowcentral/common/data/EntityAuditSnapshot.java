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

package com.flowcentraltech.flowcentral.common.data;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity audit snapshot information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityAuditSnapshot {

    private AuditEventType eventType;

    private String entity;

    private List<EntityFieldAudit> fieldAudits;

    public EntityAuditSnapshot(AuditEventType eventType, String entity, List<EntityFieldAudit> fieldAudits) {
        this.eventType = eventType;
        this.entity = entity;
        this.fieldAudits = fieldAudits;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public String getEntity() {
        return entity;
    }

    public List<EntityFieldAudit> getFieldAudits() {
        return fieldAudits;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}