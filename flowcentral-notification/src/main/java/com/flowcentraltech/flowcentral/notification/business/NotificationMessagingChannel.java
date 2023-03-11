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

import com.flowcentraltech.flowcentral.notification.data.NotificationChannelDef;
import com.flowcentraltech.flowcentral.notification.data.NotificationChannelMessage;
import com.tcdng.unify.core.UnifyComponent;

/**
 * Notification messaging channel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface NotificationMessagingChannel extends UnifyComponent {

    /**
     * Sends a message through a notification channel.
     * 
     * @param notifChannelDef
     *                            the channel definition
     * @param notifChannelMessage
     *                            the message to send
     * @return true if message was successfully sent otherwise false
     */
    boolean sendMessage(NotificationChannelDef notifChannelDef, NotificationChannelMessage notifChannelMessage);

    /**
     * Sends multiple messages through a notification channel.
     * 
     * @param notifChannelDef
     *                             the channel definition
     * @param notifChannelMessages
     *                             the messages to send
     */
    void sendMessages(NotificationChannelDef notifChannelDef, NotificationChannelMessage... notifChannelMessages);
}
