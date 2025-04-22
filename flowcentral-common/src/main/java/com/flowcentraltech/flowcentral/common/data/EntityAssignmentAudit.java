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

package com.flowcentraltech.flowcentral.common.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Assignment entity audit information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityAssignmentAudit {

    private final EnvironmentService env;

    private final EntityAuditInfo entityAuditInfo;

    private final Class<? extends Entity> assignClass;

    private final String baseFieldName;

    private Object baseId;

    private EntityAuditSnapshot lastSnapshot;

    public EntityAssignmentAudit(EnvironmentService env, EntityAuditInfo entityAuditInfo,
            Class<? extends Entity> assignClass, String baseFieldName, Object baseId) {
        this.env = env;
        this.entityAuditInfo = entityAuditInfo;
        this.assignClass = assignClass;
        this.baseFieldName = baseFieldName;
        this.baseId = baseId;
    }

    public EntityAuditInfo getEntityAuditInfo() {
        return entityAuditInfo;
    }

    public Class<? extends Entity> getAssignClass() {
        return assignClass;
    }

    public EntityAuditSnapshot getLastSnapshot() {
        return lastSnapshot;
    }

    public boolean isWithLastSnapshot() {
        return lastSnapshot != null;
    }

    public String getBaseFieldName() {
        return baseFieldName;
    }

    public Object getBaseId() {
        return baseId;
    }

    public void setBaseId(Object baseId) {
        this.baseId = baseId;
    }

    public EntityAuditSnapshot takeSnapshot(AuditEventType eventType) throws UnifyException {
        if (entityAuditInfo.isWithInclusions()) {
            List<EntityFieldAudit> fieldAudits = new ArrayList<EntityFieldAudit>();
            final Map<String, List<Object>> vals = new LinkedHashMap<String, List<Object>>();
            Query<? extends Entity> query = Query.of(assignClass).addEquals(baseFieldName, baseId);
            for (String fieldName : entityAuditInfo.getInclusions()) {
                query.addSelect(fieldName);
                vals.put(fieldName, new ArrayList<Object>());
            }

            for (Entity entity : env.listAll(query)) {
                for (String fieldName : entityAuditInfo.getInclusions()) {
                    Object val = DataUtils.getBeanProperty(Object.class, entity, fieldName);
                    vals.get(fieldName).add(val);
                }
            }

            if (eventType.isCreate()) {
                for (String fieldName : entityAuditInfo.getInclusions()) {
                    Object newVal = vals.get(fieldName);
                    fieldAudits.add(new EntityFieldAudit(fieldName, null, newVal));
                }
            } else if (eventType.isUpdate()) {
                final boolean newAsOldSource = lastSnapshot.getEventType().isCreate()
                        || lastSnapshot.getEventType().isUpdate();
                for (EntityFieldAudit oldFieldAudit : lastSnapshot.getFieldAudits()) {
                    Object newVal = vals.get(oldFieldAudit.getFieldName());
                    Object oldVal = newAsOldSource ? oldFieldAudit.getNewValue() : oldFieldAudit.getOldValue();
                    fieldAudits.add(new EntityFieldAudit(oldFieldAudit.getFieldName(), oldVal, newVal));
                }
            } else if (eventType.isDelete()) {
                for (EntityFieldAudit oldFieldAudit : lastSnapshot.getFieldAudits()) {
                    final boolean newAsOldSource = lastSnapshot.getEventType().isCreate()
                            || lastSnapshot.getEventType().isUpdate();
                    Object oldVal = newAsOldSource ? oldFieldAudit.getNewValue() : oldFieldAudit.getOldValue();
                    fieldAudits.add(new EntityFieldAudit(oldFieldAudit.getFieldName(), oldVal, null));
                }
            } else {
                for (String fieldName : entityAuditInfo.getInclusions()) {
                    Object oldVal = vals.get(fieldName);
                    fieldAudits.add(new EntityFieldAudit(fieldName, oldVal, null));
                }
            }
            lastSnapshot = new EntityAuditSnapshot(eventType, null,  entityAuditInfo.getEntity(), fieldAudits);
        }

        return lastSnapshot;
    }
}
