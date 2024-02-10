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

/**
 * Formatted entity audit information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormattedEntityAudit {

    private String eventType;

    private String entity;

    private FormattedFieldAudit[] fields;

    public FormattedEntityAudit(String eventType, String entity, FormattedFieldAudit[] fields) {
        this.eventType = eventType;
        this.entity = entity;
        this.fields = fields;
    }

    public FormattedEntityAudit() {

    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public FormattedFieldAudit[] getFields() {
        return fields;
    }

    public void setFields(FormattedFieldAudit[] fields) {
        this.fields = fields;
    }

}
