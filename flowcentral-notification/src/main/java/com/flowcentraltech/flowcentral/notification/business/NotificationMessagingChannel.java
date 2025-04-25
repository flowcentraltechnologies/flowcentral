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

package com.flowcentraltech.flowcentral.notification.business;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.notification.data.ChannelMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifChannelDef;

/**
 * Notification messaging channel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface NotificationMessagingChannel extends FlowCentralComponent {

    /**
     * Sends a message through a notification channel.
     * 
     * @param notifChannelDef
     *                            the channel definition
     * @param channelMessage
     *                            the message to send
     * @return true if message was successfully sent otherwise false
     */
    boolean sendMessage(NotifChannelDef notifChannelDef, ChannelMessage channelMessage);

    /**
     * Sends multiple messages through a notification channel.
     * 
     * @param notifChannelDef
     *                             the channel definition
     * @param channelMessages
     *                             the messages to send
     */
    void sendMessages(NotifChannelDef notifChannelDef, ChannelMessage... channelMessages);
}
