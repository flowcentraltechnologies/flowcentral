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
package com.flowcentraltech.flowcentral.notification.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotifRecipientType;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Channel message object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ChannelMessage {

    private NotifMessageFormat format;

    private String from;

    private String subject;

    private String message;

    private String error;

    private List<Recipient> recipients;

    private List<Attachment> attachments;

    private Long originId;

    private int attempts;

    private boolean sent;

    private boolean retry;

    public ChannelMessage(NotifMessageFormat format, Long originId, String from, String subject, String message,
            List<Recipient> recipients, List<Attachment> attachments, int attempts) {
        this.format = format;
        this.originId = originId;
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.recipients = recipients;
        this.attachments = attachments;
        this.attempts = attempts;
        this.retry = true;
    }

    public NotifMessageFormat getFormat() {
        return format;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Long getOriginId() {
        return originId;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isWithError() {
        return error != null;
    }

    public boolean isWithFrom() {
        return !StringUtils.isBlank(from);
    }

    public static Builder newBuilder(NotifMessageFormat format, Long originId) {
        return new Builder(format, originId);
    }

    public static class Builder {

        private NotifMessageFormat format;

        private String from;

        private String subject;

        private String message;

        private List<Recipient> recipients;

        private List<Attachment> attachments;

        private Long originId;

        private int attempts;

        private Builder(NotifMessageFormat format, Long originId) {
            this.format = format;
            this.originId = originId;
            this.recipients = new ArrayList<Recipient>();
            this.attachments = new ArrayList<Attachment>();
        }

        public Builder addRecipient(NotifRecipientType type, String name, String contact) {
            recipients.add(new Recipient(type, name, contact));
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder attempts(int attempts) {
            this.attempts = attempts;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder addAttachment(FileAttachmentType type, String name, String title, byte[] data, boolean inline) {
            attachments
                    .add(Attachment.newBuilder(type, inline).name(name).fileName(name).title(title).data(data).build());
            return this;
        }

        public Builder addAttachment(FileAttachmentType type, String name, String title, String provider,
                String sourceId, boolean inline) {
            attachments.add(Attachment.newBuilder(type, inline).name(name).fileName(name).title(title)
                    .source(provider, sourceId).build());
            return this;
        }

        public ChannelMessage build() {
            return new ChannelMessage(format, originId, from, subject, message, DataUtils.unmodifiableList(recipients),
                    DataUtils.unmodifiableList(attachments), attempts);
        }

    }
}
