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

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Notification template wrapper.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface NotifTemplateWrapper {

    NotifType getNotifType();
    
    String getTemplateName();
    
    String getEntity();
    
    void addTORecipient(String name, String contact);

    void addCCRecipient(String name, String contact);

    void addBCCRecipient(String name, String contact);

    void addRecipient(Recipient recipient);

    void setFrom(String from);

    void addParam(String name, Object val);

    void addAttachment(FileAttachmentType type, String name, String title, String fileName, byte[] data);

    void addAttachment(FileAttachmentType type, String name, String title, byte[] data);

    void addAttachment(Attachment attachment);

    NotifMessage getMessage();
}
