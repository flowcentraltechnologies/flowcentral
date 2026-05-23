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
package com.flowcentraltech.flowcentral.notification.data;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.ImportanceType;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Notification template wrapper.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface NotifTemplateWrapper {

    NotifType getNotifType();

    String getTemplateName();

    String getEntity();

    void setImportance(ImportanceType importance);

    void addTORecipient(String name, String contact);

    void addCCRecipient(String name, String contact);

    void addBCCRecipient(String name, String contact);

    void addRecipient(Recipient recipient);

    void setFrom(String from);

    void addParams(ValueStoreReader reader) throws UnifyException;

    void addParam(String name, Object val);

    void addAttachment(FileAttachmentType type, String name, String title, String fileName, byte[] data)
            throws UnifyException;

    void addAttachment(FileAttachmentType type, String name, String title, byte[] data) throws UnifyException;

    void addAttachment(FileAttachmentType type, String name, String title, String fileName, String provider,
            String sourceId) throws UnifyException;

    void addAttachment(FileAttachmentType type, String name, String title, String provider, String sourceId)
            throws UnifyException;

    void addAttachment(FileAttachmentType type, String name, String title, String fileName, byte[] data, boolean inline)
            throws UnifyException;

    void addAttachment(FileAttachmentType type, String name, String title, byte[] data, boolean inline)
            throws UnifyException;

    void addAttachment(FileAttachmentType type, String name, String title, String fileName, String provider,
            String sourceId, boolean inline) throws UnifyException;

    void addAttachment(FileAttachmentType type, String name, String title, String provider, String sourceId,
            boolean inline) throws UnifyException;

    void addAttachment(Attachment attachment) throws UnifyException;

    NotifMessage getMessage();

    boolean isWithRecipients();
}
