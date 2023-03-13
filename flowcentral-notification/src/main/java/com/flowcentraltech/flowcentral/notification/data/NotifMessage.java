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
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
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
    
    private NotifMessageFormat format;

    private String from;

    private String template;

    private Map<String, Object> params;

    private List<Recipient> recipients;

    private List<Attachment> attachments;

    private NotifMessage(NotifType notifType, NotifMessageFormat format, String from, String template, Map<String, Object> params,
            List<Recipient> recipients, List<Attachment> attachments) {
        this.notifType = notifType;
        this.format = format;
        this.from = from;
        this.template = template;
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

    public NotifMessageFormat getFormat() {
        return format;
    }

    public String getFrom() {
        return from;
    }

    public String getTemplate() {
        return template;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public static Builder newEmailMessageBuilder(String template) {
        return new Builder(NotifType.EMAIL, template);
    }

    public static Builder newSmsMessageBuilder(String template) {
        return new Builder(NotifType.SMS, template);
    }

    public static Builder newSystemMessageBuilder(String template) {
        return new Builder(NotifType.SYSTEM, template);
    }

    public static class Builder {

        private NotifType notifType;
        
        private NotifMessageFormat format;

        private String from;

        private String template;

        private Map<String, Object> params;

        private List<Recipient> recipients;

        private List<Attachment> attachments;

        private Builder(NotifType notifType, String template) {
            this.notifType = notifType;
            this.format = NotifMessageFormat.PLAIN_TEXT;
            this.template = template;
            this.params = new HashMap<String, Object>();
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

        public Builder format(NotifMessageFormat format) {
            this.format = format;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder addParam(String name, Object val) {
            params.put(name, val);
            return this;
        }

        public Builder addAttachment(FileAttachmentType type, String name, String title, String fileName, byte[] data) {
            attachments.add(Attachment.newBuilder(type).name(name).title(title).fileName(fileName).data(data).build());
            return this;
        }

        public Builder addAttachment(FileAttachmentType type, String name, String title, byte[] data) {
            attachments.add(Attachment.newBuilder(type).name(name).title(title).data(data).build());
            return this;
        }

        public Builder addAttachment(Attachment attachment) {
            attachments.add(attachment);
            return this;
        }

        public NotifMessage build() {
            if (DataUtils.isBlank(recipients)) {
                throw new IllegalArgumentException("At least one recipient is required.");
            }
            
            return new NotifMessage(notifType, format, from, template, DataUtils.unmodifiableMap(params),
                    DataUtils.unmodifiableList(recipients), DataUtils.unmodifiableList(attachments));
        }

    }
}
