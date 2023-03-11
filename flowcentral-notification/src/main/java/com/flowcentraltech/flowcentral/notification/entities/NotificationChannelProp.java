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
package com.flowcentraltech.flowcentral.notification.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Notification channel property entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_CHANNELPROP", uniqueConstraints = { @UniqueConstraint({ "notificationChannelId", "name" }) })
public class NotificationChannelProp extends BaseEntity {

    @ForeignKey(NotificationChannel.class)
    private Long notificationChannelId;

    @Column(name = "CHANNELPROP_NM", length = 128)
    private String name;

    @Column(name = "CHANNELPROP_VAL", length = 256, nullable = true)
    private String value;

    @Override
    public String getDescription() {
        return name;
    }

    public Long getNotificationChannelId() {
        return notificationChannelId;
    }

    public void setNotificationChannelId(Long notificationChannelId) {
        this.notificationChannelId = notificationChannelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
