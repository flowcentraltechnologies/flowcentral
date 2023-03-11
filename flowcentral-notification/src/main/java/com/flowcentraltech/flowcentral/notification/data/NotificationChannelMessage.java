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
package com.flowcentraltech.flowcentral.notification.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationRecipientType;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Notification channel message object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotificationChannelMessage {

    private NotificationType notificationType;

    private String subject;

    private String reference;

    private String body;

    private Long id;

    private Long tenantId;

    private NotificationMessageFormat format;

    private List<Recipient> recipients;

    private List<Attachment> attachments;

    private boolean sent;

    private NotificationChannelMessage(NotificationType notificationType, String subject, String reference, String body,
            Long id, Long tenantId, NotificationMessageFormat format, List<Recipient> recipients,
            List<Attachment> attachments) {
        this.notificationType = notificationType;
        this.subject = subject;
        this.reference = reference;
        this.body = body;
        this.id = id;
        this.tenantId = tenantId;
        this.format = format;
        this.recipients = recipients;
        this.attachments = attachments;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getSubject() {
        return subject;
    }

    public String getReference() {
        return reference;
    }

    public String getBody() {
        return body;
    }

    public Long getId() {
        return id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isSent() {
        return sent;
    }

    public NotificationMessageFormat getFormat() {
        return format;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public static Builder newBuilder(NotificationType notificationType, Long id, Long tenantId) {
        return new Builder(notificationType, id, tenantId);
    }

    public static class Builder {

        private NotificationType notificationType;

        private String subject;

        private String reference;

        private String body;

        private Long id;

        private Long tenantId;

        private List<Recipient> recipients;

        private List<Attachment> attachments;

        private NotificationMessageFormat format;

        private Builder(NotificationType notificationType, Long id, Long tenantId) {
            this.notificationType = notificationType;
            this.id = id;
            this.tenantId = tenantId;
            recipients = new ArrayList<Recipient>();
            format = NotificationMessageFormat.PLAIN_TEXT;
        }

        public Builder toRecipient(NotificationRecipientType type, String name, String contact) {
            recipients.add(new Recipient(type, name, contact));
            return this;
        }

        public Builder toRecipients(Recipient... _recipients) {
            for (Recipient recipient : _recipients) {
                recipients.add(recipient);
            }

            return this;
        }

        public Builder toRecipients(List<Recipient> _recipients) {
            if (_recipients != null) {
                recipients.addAll(_recipients);
            }

            return this;
        }

        public Builder forReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withBody(String body) {
            this.body = body;
            return this;
        }

        public Builder usingBodyFormat(NotificationMessageFormat format) {
            this.format = format;
            return this;
        }

        public Builder withAttachment(FileAttachmentType type, String name, String title, String fileName,
                byte[] data) {
            getAttachments()
                    .add(Attachment.newBuilder(type).name(name).title(title).fileName(fileName).data(data).build());
            return this;
        }

        public Builder withAttachment(FileAttachmentType type, String name, String title, byte[] data) {
            getAttachments().add(Attachment.newBuilder(type).name(name).title(title).data(data).build());
            return this;
        }

        public Builder withAttachment(Attachment attachment) {
            getAttachments().add(attachment);
            return this;
        }

        public Builder withAttachments(Attachment... _attachments) {
            for (Attachment attachment : _attachments) {
                getAttachments().add(attachment);
            }
            return this;
        }

        public Builder withAttachments(List<Attachment> _attachments) {
            if (_attachments != null) {
                getAttachments().addAll(_attachments);
            }

            return this;
        }

        public NotificationChannelMessage build() {
            return new NotificationChannelMessage(notificationType, subject, reference, body, id, tenantId, format,
                    DataUtils.unmodifiableList(recipients), DataUtils.unmodifiableList(attachments));
        }

        private List<Attachment> getAttachments() {
            if (attachments == null) {
                attachments = new ArrayList<Attachment>();
            }

            return attachments;
        }

    }
}
