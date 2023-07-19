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
package com.flowcentraltech.flowcentral.workflow.business.senders;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.senders.AbstractNotificationAlertSender;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Update draft approval alert sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = WorkflowModuleNameConstants.UPDATE_DRAFT_APPROVAL_EMAIL_SENDER,
        description = "Update Draft Approval Email Sender.")
public class UpdateDraftApprovalAlertSender extends AbstractNotificationAlertSender {

    @Override
    public NotifType getNotifType() throws UnifyException {
        return NotifType.EMAIL;
    }

    @Override
    public void composeAndSend(ValueStoreReader reader, List<Recipient> recipientList) throws UnifyException {
        // TODO Auto-generated method stub

    }

    @Override
    protected List<Attachment> generateAttachments(ValueStoreReader reader) throws UnifyException {
        return Collections.emptyList();
    }

}
