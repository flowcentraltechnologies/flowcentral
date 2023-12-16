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

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.AuditEventType;

/**
 * Entity audit information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Audit {

    private AuditEventType eventType;

    private Date eventTimestamp;

    private String userLoginId;

    private String userIpAddress;

    private EntityAudit rootEntityAudit;

    public Audit(AuditEventType eventType, Date eventTimestamp, String userLoginId, String userIpAddress,
            EntityAudit rootEntityAudit) {
        this.eventType = eventType;
        this.eventTimestamp = eventTimestamp;
        this.userLoginId = userLoginId;
        this.userIpAddress = userIpAddress;
        this.rootEntityAudit = rootEntityAudit;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public Date getEventTimestamp() {
        return eventTimestamp;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public EntityAudit getRootEntityAudit() {
        return rootEntityAudit;
    }

}
