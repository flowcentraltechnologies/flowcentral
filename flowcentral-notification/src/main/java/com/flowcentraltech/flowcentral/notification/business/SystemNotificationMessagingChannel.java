/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.util.HtmlUtils;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifMessageFormat;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.data.ChannelMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifChannelDef;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * System notification messaging channel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(NotificationModuleNameConstants.SYSTEMMESSAGINGCHANNEL)
public class SystemNotificationMessagingChannel extends AbstractNotificationMessagingChannel {

    @Configurable
    private NotificationExternalAccessibilityProvider externalAccessibilityProvider;

    @Override
    public boolean sendMessage(NotifChannelDef notifChannelDef, ChannelMessage channelMessage) {
        try {
            if (externalAccessibilityProvider != null) {
                final String subject = channelMessage.getSubject();
                final boolean isHTML = NotifMessageFormat.HTML.equals(channelMessage.getFormat());
                final String msg = isHTML ? HtmlUtils.formatEmailHTML(channelMessage.getMessage())
                        : channelMessage.getMessage();
                for (Recipient recipient : channelMessage.getRecipients()) {
                    externalAccessibilityProvider.routeSystemMessage(recipient.getContact(), subject, msg);
                }
            }

            channelMessage.setSent(true);
            return true;
        } catch (Exception e) {
            logError(e);
            channelMessage.setError(StringUtils.getPrintableStackTrace(e));
        }

        return false;
    }

    @Override
    public void sendMessages(NotifChannelDef notifChannelDef, ChannelMessage... channelMessages) {
        for (ChannelMessage channelMessage : channelMessages) {
            sendMessage(notifChannelDef, channelMessage);
        }
    }

}
