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
package com.flowcentraltech.flowcentral.notification.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Notification template parameter entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_NOTIFTEMPLATEPRM", uniqueConstraints = { @UniqueConstraint({ "notificationTemplateId", "name" }) })
public class NotificationTemplateParam extends BaseAuditEntity {

    @ForeignKey(NotificationTemplate.class)
    private Long notificationTemplateId;

    @Column(name = "PARAM_NM", length = 128)
    private String name;

    @Column(name = "PARAM_LABEL", length = 128)
    private String label;

    @Override
    public String getDescription() {
        return label;
    }

    public Long getNotificationTemplateId() {
        return notificationTemplateId;
    }

    public void setNotificationTemplateId(Long notificationTemplateId) {
        this.notificationTemplateId = notificationTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
