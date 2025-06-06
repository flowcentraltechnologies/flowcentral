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
package com.flowcentraltech.flowcentral.notification.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Notification attachment entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_NOTIFOUTBOXATTACH")
public class NotificationOutboxAttachment extends BaseAuditEntity {

    @ForeignKey(NotificationOutbox.class)
    private Long notificationOutboxId;
    
    @ForeignKey(name = "ATTACHMENT_TY")
    private FileAttachmentType type;

    @Column(name = "ATTACHMENT_NM", length = 128)
    private String name;
    
    @Column(name = "ATTACHMENT_TITLE", length = 128)
    private String title;
    
    @Column(name = "ATTACHMENT_DATA", length = 128, nullable = true)
    private byte[] data;
    
    @Column(name = "SOURCE_ID", length = 128, nullable = true)
    private String sourceId;
    
    @Column(name = "PROVIDER", length = 64, nullable = true)
    private String provider;
    
    @Column(name = "INLINE_ATTACH")
    private boolean inline;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return title;
    }

    public Long getNotificationOutboxId() {
        return notificationOutboxId;
    }

    public void setNotificationOutboxId(Long notificationOutboxId) {
        this.notificationOutboxId = notificationOutboxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FileAttachmentType getType() {
        return type;
    }

    public void setType(FileAttachmentType type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
