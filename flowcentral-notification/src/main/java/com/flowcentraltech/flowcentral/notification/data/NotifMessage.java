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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifRecipientType;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Notification message object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotifMessage {

    private NotifType notifType;

    private String from;

    private String template;

    private Long id;

    private Map<String, String> params;

    private List<Recipient> recipients;

    private List<Attachment> attachments;

    private NotifMessage(NotifType notifType, String from, String template, Long id, Map<String, String> params,
            List<Recipient> recipients, List<Attachment> attachments) {
        this.notifType = notifType;
        this.from = from;
        this.template = template;
        this.id = id;
        this.params = params;
        this.recipients = recipients;
        this.attachments = attachments;
    }

    public NotifType getNotificationType() {
        return notifType;
    }

    public NotifType getNotifType() {
        return notifType;
    }

    public String getFrom() {
        return from;
    }

    public String getTemplate() {
        return template;
    }

    public Long getId() {
        return id;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public static Builder newEmailMessageBuilder(Long id, String template) {
        return new Builder(NotifType.EMAIL, id, template);
    }

    public static Builder newSmsMessageBuilder(Long id, String template) {
        return new Builder(NotifType.SMS, id, template);
    }

    public static Builder newSystemMessageBuilder(Long id, String template) {
        return new Builder(NotifType.SYSTEM, id, template);
    }

    public static class Builder {

        private NotifType notifType;

        private String from;

        private String template;

        private Map<String, String> params;

        private Long id;

        private List<Recipient> recipients;

        private List<Attachment> attachments;

        private Builder(NotifType notifType, Long id, String template) {
            this.notifType = notifType;
            this.id = id;
            this.template = template;
            this.params = new HashMap<String, String>();
            this.recipients = new ArrayList<Recipient>();
            this.attachments = new ArrayList<Attachment>();
        }

        public Builder toRecipient(String name, String contact) {
            recipients.add(new Recipient(NotifRecipientType.TO, name, contact));
            return this;
        }

        public Builder ccRecipient(String name, String contact) {
            recipients.add(new Recipient(NotifRecipientType.CC, name, contact));
            return this;
        }

        public Builder bccRecipient(String name, String contact) {
            recipients.add(new Recipient(NotifRecipientType.BCC, name, contact));
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder withAttachment(FileAttachmentType type, String name, String title, String fileName,
                byte[] data) {
            attachments.add(Attachment.newBuilder(type).name(name).title(title).fileName(fileName).data(data).build());
            return this;
        }

        public Builder withAttachment(FileAttachmentType type, String name, String title, byte[] data) {
            attachments.add(Attachment.newBuilder(type).name(name).title(title).data(data).build());
            return this;
        }

        public Builder withAttachment(Attachment attachment) {
            attachments.add(attachment);
            return this;
        }

        public NotifMessage build() {
            return new NotifMessage(notifType, from, template, id, DataUtils.unmodifiableMap(params),
                    DataUtils.unmodifiableList(recipients), DataUtils.unmodifiableList(attachments));
        }

    }
}
