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
package com.flowcentraltech.flowcentral.notification.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.ImportanceType;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotifRecipientType;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Notification message object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class NotifMessage {

    private ImportanceType importance;

    private NotifType notifType;

    private NotifMessageFormat format;

    private String template;

    private String from;

    private List<StringToken> subjectTokenList;

    private List<StringToken> templateTokenList;

    private Map<String, Object> params;

    private List<Recipient> recipients;

    private List<Attachment> attachments;

    private NotifMessage(ImportanceType importance, NotifType notifType, NotifMessageFormat format, String from,
            String template, List<StringToken> subjectTokenList, List<StringToken> templateTokenList,
            Map<String, Object> params, List<Recipient> recipients, List<Attachment> attachments) {
        this.importance = importance;
        this.notifType = notifType;
        this.format = format;
        this.from = from;
        this.template = template;
        this.subjectTokenList = subjectTokenList;
        this.templateTokenList = templateTokenList;
        this.params = params;
        this.recipients = recipients;
        this.attachments = attachments;
    }

    public String getTemplate() {
        return template;
    }

    public ImportanceType getImportance() {
        return importance;
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

    public List<StringToken> getSubjectTokenList() {
        return subjectTokenList;
    }

    public List<StringToken> getTemplateTokenList() {
        return templateTokenList;
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

    public boolean isUseTemplate() {
        return template != null;
    }

    public static Builder newBuilder(String template) {
        return new Builder(template);
    }

    public static Builder newBuilder(List<StringToken> subjectTokenList, List<StringToken> templateTokenList) {
        return new Builder(subjectTokenList, templateTokenList);
    }

    public static class Builder {

        private ImportanceType importance;

        private NotifType notifType;

        private NotifMessageFormat format;

        private String template;

        private String from;

        private List<StringToken> subjectTokenList;

        private List<StringToken> templateTokenList;

        private Map<String, Object> params;

        private List<Recipient> recipients;

        private List<Attachment> attachments;

        private Builder(String template) {
            this.importance = ImportanceType.LOW;
            this.notifType = NotifType.EMAIL;
            this.format = NotifMessageFormat.HTML;
            this.template = template;
            this.params = new HashMap<String, Object>();
            this.recipients = new ArrayList<Recipient>();
            this.attachments = new ArrayList<Attachment>();
        }

        public Builder(List<StringToken> subjectTokenList, List<StringToken> templateTokenList) {
            if (DataUtils.isBlank(subjectTokenList) || DataUtils.isBlank(templateTokenList)) {
                throw new IllegalArgumentException("Token lists can not be empty.");
            }

            this.importance = ImportanceType.LOW;
            this.notifType = NotifType.EMAIL;
            this.format = NotifMessageFormat.HTML;
            this.subjectTokenList = subjectTokenList;
            this.templateTokenList = templateTokenList;
            this.params = new HashMap<String, Object>();
            this.recipients = new ArrayList<Recipient>();
            this.attachments = new ArrayList<Attachment>();
        }

        public Builder addTORecipient(String name, String contact) {
            recipients.add(new Recipient(NotifRecipientType.TO, name, contact));
            return this;
        }

        public Builder addCCRecipient(String name, String contact) {
            recipients.add(new Recipient(NotifRecipientType.CC, name, contact));
            return this;
        }

        public Builder addBCCRecipient(String name, String contact) {
            recipients.add(new Recipient(NotifRecipientType.BCC, name, contact));
            return this;
        }

        public Builder addRecipient(Recipient recipient) {
            recipients.add(recipient);
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder notifType(NotifType notifType) {
            this.notifType = notifType;
            return this;
        }

        public Builder format(NotifMessageFormat format) {
            this.format = format;
            return this;
        }

        public Builder importance(ImportanceType importance) {
            this.importance = importance != null ? importance : ImportanceType.LOW;
            return this;
        }

        public Builder addParam(String name, Object val) {
            params.put(name, val);
            return this;
        }

        public Builder addParams(Map<String, Object> params) {
            params.putAll(params);
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

        public boolean isWithRecipients() {
            return !DataUtils.isBlank(recipients);
        }

        public NotifMessage build() {
            if (DataUtils.isBlank(recipients)) {
                throw new IllegalArgumentException("At least one recipient is required.");
            }

            return new NotifMessage(importance, notifType, format, from, template, subjectTokenList, templateTokenList,
                    DataUtils.unmodifiableMap(params), DataUtils.unmodifiableList(recipients),
                    DataUtils.unmodifiableList(attachments));
        }

    }
}
