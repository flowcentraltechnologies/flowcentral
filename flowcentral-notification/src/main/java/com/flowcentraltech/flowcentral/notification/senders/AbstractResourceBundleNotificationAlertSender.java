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
package com.flowcentraltech.flowcentral.notification.senders;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.data.NotifMessage;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for resource-bundle based notification alert
 * sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractResourceBundleNotificationAlertSender extends AbstractNotificationAlertSender {

    private final NotifType notifType;

    private final String subjectMessageKey;

    private final String bodyMessageKey;

    private List<StringToken> subjectTokenList;

    private List<StringToken> templateTokenList;

    public AbstractResourceBundleNotificationAlertSender(NotifType notifType, String subjectMessageKey,
            String bodyMessageKey) {
        this.notifType = notifType;
        this.subjectMessageKey = subjectMessageKey;
        this.bodyMessageKey = bodyMessageKey;
    }

    @Override
    public final NotifType getNotifType() throws UnifyException {
        return notifType;
    }

    @Override
    public final void composeAndSend(ValueStoreReader reader, List<Recipient> recipientList) throws UnifyException {
        logDebug("Composing and sending notification using type...");

        List<Recipient> allRecipientList = new ArrayList<Recipient>(recipientList);
        List<Recipient> additionalRecipientList = getAdditionalRecipients(reader);
        if (!DataUtils.isBlank(additionalRecipientList)) {
            allRecipientList.addAll(additionalRecipientList);
        }

        if (!DataUtils.isBlank(allRecipientList)) {
            ensureTokenList();
            
            NotifMessage.Builder nb = NotifMessage.newBuilder(subjectTokenList, templateTokenList);
            // Set recipients
            for (Recipient recipient : allRecipientList) {
                nb.addRecipient(recipient);
            }

            // Extract parameters
            for (StringToken token : subjectTokenList) {
                if (token.isParam()) {
                    String _token = token.getToken();
                    nb.addParam(_token, reader.read(_token));
                }
            }

            for (StringToken token : templateTokenList) {
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

            notification().sendNotification(nb.build());
            logDebug("Notification successfully prepared for sending.");
        } else {
            logDebug("Send notifcation aborted because there are no recipients.");
        }
    }

    /**
     * Gets additional notification recipients from reader.
     * 
     * @param reader
     *               the reader
     * @return list of additional recipients
     * @throws UnifyException
     *                        if an error occurs
     */
    protected abstract List<Recipient> getAdditionalRecipients(ValueStoreReader reader) throws UnifyException;

    private void ensureTokenList() throws UnifyException {
        if (subjectTokenList == null) {
            synchronized (this) {
                if (subjectTokenList == null) {
                    subjectTokenList = StringUtils.breakdownParameterizedString(getApplicationMessage(subjectMessageKey));
                    templateTokenList = StringUtils.breakdownParameterizedString(getApplicationMessage(bodyMessageKey));
                }
            }
        }
    }

}
