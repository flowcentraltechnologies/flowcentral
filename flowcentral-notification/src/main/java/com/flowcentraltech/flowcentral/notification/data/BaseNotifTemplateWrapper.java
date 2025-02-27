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

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.ImportanceType;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for notification template wrappers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseNotifTemplateWrapper implements NotifTemplateWrapper {

    protected final NotifTemplateDef notifTemplateDef;

    protected final NotifMessage.Builder nmb;

    public BaseNotifTemplateWrapper(NotifTemplateDef notifTemplateDef) {
        this.notifTemplateDef = notifTemplateDef;
        this.nmb = NotifMessage.newBuilder(notifTemplateDef.getLongName());
    }

    @Override
    public NotifType getNotifType() {
        return notifTemplateDef.getNotifType();
    }

    @Override
    public String getTemplateName() {
        return notifTemplateDef.getLongName();
    }

    @Override
    public String getEntity() {
        return notifTemplateDef.getEntity();
    }

    @Override
    public void setImportance(ImportanceType importance) {
        nmb.importance(importance);
    }

    @Override
    public void addTORecipient(String name, String contact) {
        nmb.addTORecipient(name, contact);
    }

    @Override
    public void addCCRecipient(String name, String contact) {
        nmb.addCCRecipient(name, contact);
    }

    @Override
    public void addBCCRecipient(String name, String contact) {
        nmb.addBCCRecipient(name, contact);
    }

    @Override
    public void addRecipient(Recipient recipient) {
        nmb.addRecipient(recipient);
    }

    @Override
    public void setFrom(String from) {
        nmb.from(from);
    }

    @Override
    public void addParams(ValueStoreReader reader) throws UnifyException {
        for (StringToken token : notifTemplateDef.getSubjectTokenList()) {
            if (token.isParam()) {
                String _token = token.getToken();
                nmb.addParam(_token, reader.read(_token));
            }
        }

        for (StringToken token : notifTemplateDef.getTemplateTokenList()) {
            if (token.isParam()) {
                String _token = token.getToken();
                nmb.addParam(_token, reader.read(_token));
            }
        }
    }

    @Override
    public void addParam(String name, Object val) {
        nmb.addParam(name, val);
    }

    @Override
    public void addAttachment(FileAttachmentType type, String name, String title, String fileName, byte[] data) {
        nmb.addAttachment(type, name, title, fileName, data, false);
    }

    @Override
    public void addAttachment(FileAttachmentType type, String name, String title, byte[] data) {
        nmb.addAttachment(type, name, title, data, false);
    }

    @Override
    public void addAttachment(FileAttachmentType type, String name, String title, String fileName, String provider,
            String sourceId) {
        nmb.addAttachment(type, name, title, fileName, provider, sourceId, false);
    }

    @Override
    public void addAttachment(FileAttachmentType type, String name, String title, String provider, String sourceId) {
        nmb.addAttachment(type, name, title, provider, sourceId, false);
    }

    @Override
    public void addAttachment(FileAttachmentType type, String name, String title, String fileName, byte[] data,
            boolean inline) {
        nmb.addAttachment(type, name, title, fileName, data, inline);
    }

    @Override
    public void addAttachment(FileAttachmentType type, String name, String title, byte[] data, boolean inline) {
        nmb.addAttachment(type, name, title, data, inline);
    }

    @Override
    public void addAttachment(FileAttachmentType type, String name, String title, String fileName, String provider,
            String sourceId, boolean inline) {
        nmb.addAttachment(type, name, title, fileName, provider, sourceId, inline);
    }

    @Override
    public void addAttachment(FileAttachmentType type, String name, String title, String provider, String sourceId,
            boolean inline) {
        nmb.addAttachment(type, name, title, provider, sourceId, inline);
    }

    @Override
    public void addAttachment(Attachment attachment) {
        nmb.addAttachment(attachment);
    }

    @Override
    public NotifMessage getMessage() {
        return nmb.build();
    }

    @Override
    public boolean isWithRecipients() {
        return nmb.isWithRecipients();
    }

}
