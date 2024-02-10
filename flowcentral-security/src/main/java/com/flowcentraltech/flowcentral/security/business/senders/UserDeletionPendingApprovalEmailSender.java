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
package com.flowcentraltech.flowcentral.security.business.senders;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.senders.AbstractResourceBundleNotificationAlertSender;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * 
 * User deletion approval pending email sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({"security.user"})
@Component(name = "userdeletion-approvalpending", description = "User Deletion Approval Pending Email Sender")
public class UserDeletionPendingApprovalEmailSender extends AbstractResourceBundleNotificationAlertSender {

    public UserDeletionPendingApprovalEmailSender() {
        super(NotifType.EMAIL, "security.user.deletion.approval.required.subject",
                "security.user.deletion.approval.required.body");
    }

    @Override
    protected List<Recipient> getAdditionalRecipients(ValueStoreReader reader) throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    protected List<Attachment> generateAttachments(ValueStoreReader reader) throws UnifyException {
        return Collections.emptyList();
    }

}
