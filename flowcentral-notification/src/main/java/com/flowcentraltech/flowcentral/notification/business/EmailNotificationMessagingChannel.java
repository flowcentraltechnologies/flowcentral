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

package com.flowcentraltech.flowcentral.notification.business;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.util.HtmlUtils;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.configuration.constants.NotifRecipientType;
import com.flowcentraltech.flowcentral.notification.constants.NotificationHostServerConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.notification.data.ChannelMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifChannelDef;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.NetworkSecurityType;
import com.tcdng.unify.core.notification.Email;
import com.tcdng.unify.core.notification.EmailServer;
import com.tcdng.unify.core.notification.EmailServerConfig;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Email notification messaging channel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(NotificationModuleNameConstants.EMAILMESSAGINGCHANNEL)
public class EmailNotificationMessagingChannel extends AbstractNotificationMessagingChannel {

    @Configurable(ApplicationComponents.APPLICATION_DEFAULTEMAILSERVER)
    private EmailServer emailServer;

    @Override
    public boolean sendMessage(NotifChannelDef notifChannelDef, ChannelMessage channelMessage) {
        try {
            ensureServerConfigured(notifChannelDef);
            EmailContext ctx = getEmailContext();
            if (ctx.isTestMode() && !ctx.isEmailsPresent()) {
                logDebug("Aborting send email. Test mode system parameters not properly set.");
                return setError(channelMessage,
                        "System is in nootification test mode. Test Mode TO email(s) is required.");
            }

            Email email = null;
            try {
                email = conctructEmail(ctx, notifChannelDef, channelMessage);
            } catch (Exception e) {
                return setError(channelMessage, StringUtils.getPrintableStackTrace(e));
            }

            emailServer.sendEmail(notifChannelDef.getName(), email);
            channelMessage.setError(email.getError());
            channelMessage.setSent(email.isSent());
            return email.isSent();
        } catch (UnifyException e) {
            logError(e);
            channelMessage.setError(StringUtils.getPrintableStackTrace(e));
        }

        return false;
    }

    @Override
    public void sendMessages(NotifChannelDef notifChannelDef, ChannelMessage... channelMessages) {
        try {
            EmailContext ctx = getEmailContext();
            if (ctx.isTestMode() && !ctx.isEmailsPresent()) {
                logDebug("Aborting send email. Test mode system parameters not properly set.");
                for (ChannelMessage channelMessage : channelMessages) {
                    setError(channelMessage,
                            "System is in nootification test mode. Test Mode TO email(s) is required.");
                }

                return;
            }

            ensureServerConfigured(notifChannelDef);
            Email[] email = new Email[channelMessages.length];
            for (int i = 0; i < channelMessages.length; i++) {
                try {
                    email[i] = conctructEmail(ctx, notifChannelDef, channelMessages[i]);
                } catch (Exception e) {
                    email[i] = new Email(StringUtils.getPrintableStackTrace(e));
                    channelMessages[i].setRetry(false);
                }
            }

            emailServer.sendEmail(notifChannelDef.getName(), email);

            for (int i = 0; i < channelMessages.length; i++) {
                channelMessages[i].setError(email[i].getError());
                channelMessages[i].setSent(email[i].isSent());
            }
        } catch (UnifyException e) {
            final String error = StringUtils.getPrintableStackTrace(e);
            for (int i = 0; i < channelMessages.length; i++) {
                channelMessages[i].setError(error);
            }
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

    private Email conctructEmail(EmailContext textCtx, NotifChannelDef notifChannelDef, ChannelMessage channelMessage)
            throws UnifyException {
        Email.Builder eb = Email.newBuilder();
        if (textCtx.isTestMode()) {
            textCtx.resetUsed();
            for (Recipient recipient : channelMessage.getRecipients()) {
                final NotifRecipientType type = recipient.getType();
                if (textCtx.checkUnused(type)) {
                    List<String> emails = Collections.emptyList();
                    switch (type) {
                        case BCC:
                            emails = textCtx.getBccEmails();
                            break;
                        case CC:
                            emails = textCtx.getCcEmails();
                            break;
                        case TO:
                            emails = textCtx.getToEmails();
                            break;
                        default:
                            break;

                    }

                    for (String contact : emails) {
                        eb.toRecipient(type.emailRecipientType(), contact);
                    }
                }
            }
        } else {
            for (Recipient recipient : channelMessage.getRecipients()) {
                eb.toRecipient(recipient.getType().emailRecipientType(), recipient.getContact());
            }
        }

        final boolean isHTML = NotifMessageFormat.HTML.equals(channelMessage.getFormat());
        final String msg = isHTML ? HtmlUtils.formatEmailHTML(channelMessage.getMessage())
                : channelMessage.getMessage();
        eb.fromSender(notifChannelDef.getSenderContact(), channelMessage.getFrom())
                .withSubject(channelMessage.getSubject()).containingMessage(msg).asHTML(isHTML);

        for (Attachment attachment : channelMessage.getAttachments()) {
            if (attachment.getFile() != null) {
                eb.withAttachment(attachment.getFileName(), attachment.getFile().getDataAndInvalidate(),
                        attachment.getType(), attachment.isInline());
            } else {
                eb.withAttachment(attachment.getFileName(), attachment.getProvider(), attachment.getSourceId(),
                        attachment.getType(), attachment.isInline());
            }
        }

        return eb.build();
    }

    private boolean setError(ChannelMessage channelMessage, String errorMsg) {
        channelMessage.setError(errorMsg);
        channelMessage.setSent(false);
        channelMessage.setRetry(false);
        return false;
    }

    private EmailContext getEmailContext() throws UnifyException {
        final boolean testMode = system().getSysParameterValue(boolean.class,
                NotificationModuleSysParamConstants.NOTIFICATION_TEST_MODE_ENABLED);
        EmailContext ctx = EmailContext.TEST_MODE_OFF;
        if (testMode) {
            String emails = system().getSysParameterValue(String.class,
                    NotificationModuleSysParamConstants.NOTIFICATION_TEST_MODE_TO_EMAILS);
            List<String> toEmails = !StringUtils.isBlank(emails) ? StringUtils.charToListSplit(emails, ';')
                    : Collections.emptyList();

            emails = system().getSysParameterValue(String.class,
                    NotificationModuleSysParamConstants.NOTIFICATION_TEST_MODE_CC_EMAILS);
            List<String> ccEmails = !StringUtils.isBlank(emails) ? StringUtils.charToListSplit(emails, ';')
                    : Collections.emptyList();

            emails = system().getSysParameterValue(String.class,
                    NotificationModuleSysParamConstants.NOTIFICATION_TEST_MODE_BCC_EMAILS);
            List<String> bccEmails = !StringUtils.isBlank(emails) ? StringUtils.charToListSplit(emails, ';')
                    : Collections.emptyList();
            ctx = new EmailContext(toEmails, ccEmails, bccEmails);
        }

        logDebug("Using email context [{0}]...", ctx);
        return ctx;
    }

    private static class EmailContext {

        public static final EmailContext TEST_MODE_OFF = new EmailContext();

        private final List<String> toEmails;

        private final List<String> ccEmails;

        private final List<String> bccEmails;

        private final boolean testMode;

        private Set<NotifRecipientType> used;

        public EmailContext(List<String> toEmails, List<String> ccEmails, List<String> bccEmails) {
            this.toEmails = toEmails;
            this.ccEmails = ccEmails;
            this.bccEmails = bccEmails;
            this.testMode = true;
            this.used = new HashSet<NotifRecipientType>();
        }

        private EmailContext() {
            this.toEmails = Collections.emptyList();
            this.ccEmails = Collections.emptyList();
            this.bccEmails = Collections.emptyList();
            this.testMode = false;
            this.used = new HashSet<NotifRecipientType>();
        }

        public List<String> getToEmails() {
            return toEmails;
        }

        public List<String> getCcEmails() {
            return ccEmails;
        }

        public List<String> getBccEmails() {
            return bccEmails;
        }

        public boolean isTestMode() {
            return testMode;
        }

        public boolean isEmailsPresent() {
            return !toEmails.isEmpty();
        }

        public boolean checkUnused(NotifRecipientType type) {
            return used.add(type);
        }

        public void resetUsed() {
            used.clear();
        }

        @Override
        public String toString() {
            return "EmailContext [toEmails=" + toEmails + ", ccEmails=" + ccEmails + ", bccEmails=" + bccEmails
                    + ", testMode=" + testMode + ", used=" + used + "]";
        }
    }
}
