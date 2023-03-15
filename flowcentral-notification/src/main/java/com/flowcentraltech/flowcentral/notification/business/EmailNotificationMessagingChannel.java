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

package com.flowcentraltech.flowcentral.notification.business;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.notification.constants.NotificationHostServerConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.data.ChannelMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifChannelDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.NetworkSecurityType;
import com.tcdng.unify.core.notification.Email;
import com.tcdng.unify.core.notification.EmailServer;
import com.tcdng.unify.core.notification.EmailServerConfig;

/**
 * Email notification messaging channel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(NotificationModuleNameConstants.EMAILMESSAGINGCHANNEL)
public class EmailNotificationMessagingChannel extends AbstractNotificationMessagingChannel {

    @Configurable
    private EmailServer emailServer;

    public void setEmailServer(EmailServer emailServer) {
        this.emailServer = emailServer;
    }

    @Override
    public boolean sendMessage(NotifChannelDef notifChannelDef, ChannelMessage channelMessage) {
        try {
            ensureServerConfigured(notifChannelDef);
            Email email = conctructEmail(notifChannelDef, channelMessage);
            emailServer.sendEmail(notifChannelDef.getName(), email);
            channelMessage.setSent(email.isSent());
            return email.isSent();
        } catch (UnifyException e) {
            logError(e);
        }

        return false;
    }

    @Override
    public void sendMessages(NotifChannelDef notifChannelDef, ChannelMessage... channelMessage) {
        try {
            ensureServerConfigured(notifChannelDef);
            Email[] email = new Email[channelMessage.length];
            for (int i = 0; i < channelMessage.length; i++) {
                email[i] = conctructEmail(notifChannelDef, channelMessage[i]);
            }

            emailServer.sendEmail(notifChannelDef.getName(), email);

            for (int i = 0; i < channelMessage.length; i++) {
                channelMessage[i].setSent(email[i].isSent());
            }
        } catch (UnifyException e) {
            logError(e);
        }
    }

    private void ensureServerConfigured(NotifChannelDef notifChannelDef) throws UnifyException {
        final String configCode = notifChannelDef.getName();
        if (!notifChannelDef.isChannelConfigured() || !emailServer.isConfigured(configCode)) {
            EmailServerConfig emailServerConfig = EmailServerConfig.newBuilder()
                    .hostAddress(notifChannelDef.getPropValue(String.class,
                            NotificationHostServerConstants.ADDRESS_PROPERTY))
                    .hostPort(
                            notifChannelDef.getPropValue(Integer.class, NotificationHostServerConstants.PORT_PROPERTY))
                    .useSecurityType(notifChannelDef.getPropValue(NetworkSecurityType.class,
                            NotificationHostServerConstants.SECURITYTYPE_PROPERTY))
                    .username(notifChannelDef.getPropValue(String.class,
                            NotificationHostServerConstants.USERNAME_PROPERTY))
                    .password(notifChannelDef.getPropValue(String.class,
                            NotificationHostServerConstants.PASSWORD_PROPERTY))
                    .build();
            emailServer.configure(configCode, emailServerConfig);
            notifChannelDef.setChannelConfigured();
        }
    }

    private Email conctructEmail(NotifChannelDef notifChannelDef, ChannelMessage channelMessage)
            throws UnifyException {
        Email.Builder eb = Email.newBuilder();
        if (channelMessage.isWithFrom()) {
            eb.fromSender(channelMessage.getFrom());
        }
        
        for (Recipient recipient : channelMessage.getRecipients()) {
            eb.toRecipient(recipient.getType().emailRecipientType(), recipient.getContact());
        }

        eb.fromSender(notifChannelDef.getSenderContact()).withSubject(channelMessage.getSubject())
                .containingMessage(channelMessage.getMessage())
                .asHTML(NotifMessageFormat.HTML.equals(channelMessage.getFormat()));

        for (Attachment attachment : channelMessage.getAttachments()) {
            eb.withAttachment(attachment.getFileName(), attachment.getData(), attachment.getType());
        }

        return eb.build();
    }
}
