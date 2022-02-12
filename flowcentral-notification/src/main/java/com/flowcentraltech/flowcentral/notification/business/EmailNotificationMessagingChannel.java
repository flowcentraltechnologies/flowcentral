/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */

package com.flowcentraltech.flowcentral.notification.business;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationMessageFormat;
import com.flowcentraltech.flowcentral.notification.constants.NotificationHostServerConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.data.NotificationChannelDef;
import com.flowcentraltech.flowcentral.notification.data.NotificationChannelMessage;
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
 * @author Lateef Ojulari
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
    public boolean sendMessage(NotificationChannelDef notifChannelDef, NotificationChannelMessage notifChannelMessage) {
        try {
            ensureServerConfigured(notifChannelDef);
            Email email = conctructEmail(notifChannelDef, notifChannelMessage);
            emailServer.sendEmail(notifChannelDef.getName(), email);
            notifChannelMessage.setSent(email.isSent());
            return email.isSent();
        } catch (UnifyException e) {
            logError(e);
        }

        return false;
    }

    @Override
    public void sendMessages(NotificationChannelDef notifChannelDef,
            NotificationChannelMessage... notifChannelMessages) {
        try {
            ensureServerConfigured(notifChannelDef);
            Email[] email = new Email[notifChannelMessages.length];
            for (int i = 0; i < notifChannelMessages.length; i++) {
                email[i] = conctructEmail(notifChannelDef, notifChannelMessages[i]);
            }

            emailServer.sendEmail(notifChannelDef.getName(), email);

            for (int i = 0; i < notifChannelMessages.length; i++) {
                notifChannelMessages[i].setSent(email[i].isSent());
            }
        } catch (UnifyException e) {
            logError(e);
        }
    }

    private void ensureServerConfigured(NotificationChannelDef notifChannelDef) throws UnifyException {
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

    private Email conctructEmail(NotificationChannelDef notifChannelDef, NotificationChannelMessage notifChannelMessage)
            throws UnifyException {
        Email.Builder eb = Email.newBuilder();
        for (Recipient recipient : notifChannelMessage.getRecipients()) {
            eb.toRecipient(recipient.getType().emailRecipientType(), recipient.getContact());
        }

        eb.fromSender(notifChannelDef.getSenderContact()).withSubject(notifChannelMessage.getSubject())
                .containingMessage(notifChannelMessage.getBody())
                .asHTML(NotificationMessageFormat.HTML.equals(notifChannelMessage.getFormat()));

        for (Attachment attachment : notifChannelMessage.getAttachments()) {
            eb.withAttachment(attachment.getFileName(), attachment.getData(), attachment.getType());
        }

        return eb.build();
    }
}
