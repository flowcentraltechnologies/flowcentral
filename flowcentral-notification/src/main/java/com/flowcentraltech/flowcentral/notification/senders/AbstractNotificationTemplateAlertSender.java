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
package com.flowcentraltech.flowcentral.notification.senders;

import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for notification template alert sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractNotificationTemplateAlertSender<T extends NotifTemplateWrapper>
        extends AbstractNotificationAlertSender {

    private final Class<T> notifWrapperType;

    public AbstractNotificationTemplateAlertSender(Class<T> notifWrapperType) {
        this.notifWrapperType = notifWrapperType;
    }

    @Override
    public NotifType getNotifType() throws UnifyException {
        return notification().wrapperOfNotifTemplate(notifWrapperType).getNotifType();
    }

    @Override
    public final void composeAndSend(ValueStoreReader reader, List<Recipient> recipientList) throws UnifyException {
        logDebug("Composing and sending notification for value [{0}]...", reader.getValueObject());
        T notifWrapper = getTemplateWrapper(notifWrapperType);

        // Set recipients
        if (!DataUtils.isBlank(recipientList)) {
            for (Recipient recipient : recipientList) {
                notifWrapper.addRecipient(recipient);
            }
        }

        List<Recipient> additionalRecipientList = getAdditionalRecipients(reader);
        if (!DataUtils.isBlank(additionalRecipientList)) {
            for (Recipient recipient : additionalRecipientList) {
                notifWrapper.addRecipient(recipient);
            }
        }

        if (notifWrapper.isWithRecipients()) {
            // Set template variables
            setTemplateVariables(notifWrapper, reader);

            // Set attachments
            List<Attachment> attachmentList = generateAttachments(reader);
            if (!DataUtils.isBlank(attachmentList)) {
                for (Attachment attachment : attachmentList) {
                    notifWrapper.addAttachment(attachment);
                }
            }

            notification().sendNotification(notifWrapper.getMessage());
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

    /**
     * Sets the notification template variables.
     * 
     * @param notifWrapper
     *                     the notification template wrapper
     * @param reader
     *                     the backing value store reader
     * @throws UnifyException
     *                        if an error occurs
     */
    protected abstract void setTemplateVariables(T notifWrapper, ValueStoreReader reader) throws UnifyException;

}
