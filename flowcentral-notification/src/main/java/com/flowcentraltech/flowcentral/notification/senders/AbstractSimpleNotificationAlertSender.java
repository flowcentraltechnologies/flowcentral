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
package com.flowcentraltech.flowcentral.notification.senders;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ProcessVariable;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.data.NotifMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateDef;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient base class for simple alert sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSimpleNotificationAlertSender extends AbstractNotificationAlertSender {

    private final NotifType notifType;

    public AbstractSimpleNotificationAlertSender(NotifType notifType) {
        this.notifType = notifType;
    }

    @Override
    public final NotifType getNotifType() throws UnifyException {
        return notifType;
    }

    @Override
    public final void composeAndSend(ValueStoreReader reader, List<Recipient> recipientList, int sendDelayInMinutes)
            throws UnifyException {
        final String template = reader.read(String.class, NotificationAlertSender.TEMPLATE_VARIABLE);
        if (StringUtils.isBlank(template)) {
            throwOperationErrorException(new IllegalArgumentException("Could not retreive template variable."));
        }

        final NotifTemplateDef notifTemplateDef = notification().getNotifTemplateDef(template);
        if (!notifTemplateDef.getNotifType().equals(notifType)) {
            throwOperationErrorException(new IllegalArgumentException("Sender notification type [" + notifType
                    + "] not compatible with template notification type [" + notifTemplateDef.getNotifType() + "]."));
        }

        logDebug("Composing and sending notification using template [{0}] and of type [{1}]...", template,
                notifTemplateDef.getNotifType());
        if (!DataUtils.isBlank(recipientList)) {
            NotifMessage.Builder nb = NotifMessage.newBuilder(notifTemplateDef.getSubjectTokenList(),
                    notifTemplateDef.getTemplateTokenList());
            nb.from(reader.read(String.class, ProcessVariable.APP_CORRESPONDER.variableKey()));

            // Set recipients
            for (Recipient recipient : recipientList) {
                nb.addRecipient(recipient);
            }

            // Extract parameters
            for (StringToken token : notifTemplateDef.getSubjectTokenList()) {
                if (token.isParam()) {
                    String _token = token.getToken();
                    nb.addParam(_token, reader.read(_token));
                }
            }

            for (StringToken token : notifTemplateDef.getTemplateTokenList()) {
                if (token.isParam()) {
                    String _token = token.getToken();
                    nb.addParam(_token, reader.read(_token));
                }
            }

            // Set attachments
            List<Attachment> attachmentList = generateAttachments(reader);
            if (!DataUtils.isBlank(attachmentList)) {
                for (Attachment attachment : attachmentList) {
                    nb.addAttachment(attachment);
                }
            }

            nb.sendDelayInMinutes(sendDelayInMinutes);
            notification().sendNotification(nb.build());
            logDebug("Notification successfully prepared for sending.");
        } else {
            logDebug("Send notifcation aborted because there are no recipients.");
        }
    }

    @Override
    protected final List<Attachment> generateAttachments(ValueStoreReader reader) throws UnifyException {
        return Collections.emptyList();
    }

}
