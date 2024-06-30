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

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Notification alert sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface NotificationAlertSender extends FlowCentralComponent {

    String TEMPLATE_VARIABLE = "rsvTemplate";

    String WFITEM_LINK_VARIABLE = "rsvWfitemlink";
    
    /**
     * Gets the notification type for this sender.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    NotifType getNotifType() throws UnifyException;

    /**
     * Composes and sends a notification.
     * 
     * @param reader
     *                      backing value store reader
     * @param recipientList
     *                      the recipient list
     * @throws UnifyException
     *                        if an error occurs
     */
    void composeAndSend(ValueStoreReader reader, List<Recipient> recipientList) throws UnifyException;
}
