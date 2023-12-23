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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.flowcentraltech.flowcentral.configuration.constants.AuditSourceType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Audit information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AuditSnapshot {

    private AuditSourceType sourceType;

    private AuditEventType eventType;

    private Date eventTimestamp;

    private String sourceName;

    private String entity;

    private String userLoginId;

    private String userName;

    private String userIpAddress;

    private String roleCode;

    private List<EntityAuditSnapshot> snapshots;

    private AuditSnapshot(AuditSourceType sourceType, AuditEventType eventType, Date eventTimestamp, String sourceName,
            String entity, String userLoginId, String userName, String userIpAddress, String roleCode,
            List<EntityAuditSnapshot> snapshots) {
        this.sourceType = sourceType;
        this.eventType = eventType;
        this.eventTimestamp = eventTimestamp;
        this.sourceName = sourceName;
        this.entity = entity;
        this.userLoginId = userLoginId;
        this.userName = userName;
        this.userIpAddress = userIpAddress;
        this.roleCode = roleCode;
        this.snapshots = snapshots;
    }

    public AuditSourceType getSourceType() {
        return sourceType;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public Date getEventTimestamp() {
        return eventTimestamp;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getEntity() {
        return entity;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public List<EntityAuditSnapshot> getSnapshots() {
        return snapshots;
    }

    public boolean isWithSnapshots() {
        return !DataUtils.isBlank(snapshots);
    }
    
    public static Builder newBuilder(AuditSourceType sourceType, AuditEventType eventType, Date eventTimestamp,
            String sourceName) {
        return new Builder(sourceType, eventType, eventTimestamp, sourceName);
    }

    public static class Builder {

        private AuditSourceType sourceType;

        private AuditEventType eventType;

        private Date eventTimestamp;

        private String sourceName;

        private String userLoginId;

        private String userName;

        private String userIpAddress;

        private String roleCode;

        private List<EntityAuditSnapshot> snapshots;

        private Builder(AuditSourceType sourceType, AuditEventType eventType, Date eventTimestamp, String sourceName) {
            this.sourceType = sourceType;
            this.sourceName = sourceName;
            this.eventType = eventType;
            this.eventTimestamp = eventTimestamp;
            this.snapshots = new ArrayList<EntityAuditSnapshot>();
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder userLoginId(String userLoginId) {
            this.userLoginId = userLoginId;
            return this;
        }

        public Builder userIpAddress(String userIpAddress) {
            this.userIpAddress = userIpAddress;
            return this;
        }

        public Builder roleCode(String roleCode) {
            this.roleCode = roleCode;
            return this;
        }

        public Builder addSnapshot(EntityAudit entityAudit, AuditEventType eventType) throws UnifyException {
            snapshots.add(entityAudit.takeSnapshot(eventType));
            return this;
        }

        public Builder addSnapshot(EntityAssignmentAudit entityAssignmentAudit, AuditEventType eventType)
                throws UnifyException {
            snapshots.add(entityAssignmentAudit.takeSnapshot(eventType));
            return this;
        }

        public AuditSnapshot build() {
            if (snapshots.size() == 0) {
                 throw new IllegalArgumentException("No snapshot added to this builder.");
            }
            
            final String entity = snapshots.get(0).getEntity();
            return new AuditSnapshot(sourceType, eventType, eventTimestamp, sourceName, entity, userLoginId, userName,
                    userIpAddress, roleCode, Collections.unmodifiableList(snapshots));
        }
    }
}
