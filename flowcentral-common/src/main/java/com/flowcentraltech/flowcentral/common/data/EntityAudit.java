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

package com.flowcentraltech.flowcentral.common.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Entity audit information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityAudit {

    private EntityAuditInfo entityAuditInfo;

    private Entity entity;

    private EntityAuditSnapshot lastSnapshot;

    public EntityAudit(EntityAuditInfo entityAuditInfo, Entity entity) {
        this.entityAuditInfo = entityAuditInfo;
        this.entity = entity;
    }

    public EntityAuditInfo getEntityAuditInfo() {
        return entityAuditInfo;
    }

    public EntityAuditSnapshot getLastSnapshot() {
        return lastSnapshot;
    }

    public boolean isWithLastSnapshot() {
        return lastSnapshot != null;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityAuditSnapshot takeSnapshot(AuditEventType eventType) throws UnifyException {
        List<EntityFieldAudit> fieldAudits = new ArrayList<EntityFieldAudit>();
        if (eventType.isCreate()) {
            for (String fieldName : entityAuditInfo.getInclusions()) {
                Object newVal = DataUtils.getBeanProperty(Object.class, entity, fieldName);
                fieldAudits.add(new EntityFieldAudit(fieldName, null, newVal));
            }
        } else if (eventType.isUpdate()) {
            if (lastSnapshot != null) {
                final boolean newAsOldSource = lastSnapshot.getEventType().isCreate()
                        || lastSnapshot.getEventType().isUpdate();
                for (EntityFieldAudit oldFieldAudit : lastSnapshot.getFieldAudits()) {
                    Object newVal = DataUtils.getBeanProperty(Object.class, entity, oldFieldAudit.getFieldName());
                    Object oldVal = newAsOldSource ? oldFieldAudit.getNewValue() : oldFieldAudit.getOldValue();
                    fieldAudits.add(new EntityFieldAudit(oldFieldAudit.getFieldName(), oldVal, newVal));
                }
            }
        } else if (eventType.isDelete()) {
            if (lastSnapshot != null) {
                for (EntityFieldAudit oldFieldAudit : lastSnapshot.getFieldAudits()) {
                    Object oldVal = oldFieldAudit.getOldValue();
                    fieldAudits.add(new EntityFieldAudit(oldFieldAudit.getFieldName(), oldVal, null));
                }
            }
        } else {
            for (String fieldName : entityAuditInfo.getInclusions()) {
                Object oldVal = DataUtils.getBeanProperty(Object.class, entity, fieldName);
                fieldAudits.add(new EntityFieldAudit(fieldName, oldVal, null));
            }
        }

        lastSnapshot = new EntityAuditSnapshot(eventType, (Long) entity.getId(), entityAuditInfo.getEntity(),
                fieldAudits);
        return lastSnapshot;
    }
}
