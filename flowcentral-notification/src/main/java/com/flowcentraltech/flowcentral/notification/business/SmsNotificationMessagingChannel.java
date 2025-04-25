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

import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.notification.constants.NotificationHostServerConstants;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.data.ChannelMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifChannelDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.NetworkSecurityType;
import com.tcdng.unify.core.notification.BulkSms;
import com.tcdng.unify.core.notification.Sms;
import com.tcdng.unify.core.notification.SmsServer;
import com.tcdng.unify.core.notification.SmsServerConfig;

/**
 * SMS notification messaging channel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(NotificationModuleNameConstants.SMSMESSAGINGCHANNEL)
public class SmsNotificationMessagingChannel extends AbstractNotificationMessagingChannel {

    @Configurable
    private SmsServer smsServer;

    @Override
    public boolean sendMessage(NotifChannelDef notifChannelDef, ChannelMessage channelMessage) {
        try {
            ensureServerConfigured(notifChannelDef);
            return sendSms(notifChannelDef, channelMessage);
        } catch (UnifyException e) {
            logError(e);
        }

        return false;
    }

    @Override
    public void sendMessages(NotifChannelDef notifChannelDef, ChannelMessage... channelMessages) {
        for (ChannelMessage _msg : channelMessages) {
            sendSms(notifChannelDef, _msg);
        }
    }

    private void ensureServerConfigured(NotifChannelDef notifChannelDef) throws UnifyException {
        final String configCode = notifChannelDef.getName();
        if (!notifChannelDef.isChannelConfigured() || !smsServer.isConfigured(configCode)) {
            SmsServerConfig smsServerConfig = SmsServerConfig.newBuilder()
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
            smsServer.configure(configCode, smsServerConfig);
            notifChannelDef.setChannelConfigured();
        }
    }

    private boolean sendSms(NotifChannelDef notifChannelDef, ChannelMessage channelMessage) {
        try {
            List<Recipient> recipientList = channelMessage.getRecipients();
            if (recipientList.size() == 1) {
                Sms sms = channelMessage.isWithFrom()
                        ? Sms.newBuilder().fromSender(channelMessage.getFrom())
                                .toRecipient(recipientList.get(0).getContact())
                                .containingMessage(channelMessage.getMessage()).build()
                        : Sms.newBuilder().fromSender(notifChannelDef.getSenderContact())
                                .toRecipient(recipientList.get(0).getContact())
                                .containingMessage(channelMessage.getMessage()).build();
                smsServer.sendSms(notifChannelDef.getName(), sms);
            } else {
                BulkSms.Builder bsb = BulkSms.newBuilder();
                for (Recipient recipient : recipientList) {
                    bsb.toRecipient(recipient.getContact());
                }

                bsb.fromSender(notifChannelDef.getSenderContact()).containingMessage(channelMessage.getMessage());
                smsServer.sendSms(notifChannelDef.getName(), bsb.build());
            }

            channelMessage.setSent(true);
            return true;
        } catch (UnifyException e) {
            logError(e);
        }

        return false;
    }

}
