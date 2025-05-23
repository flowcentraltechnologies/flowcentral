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
package com.flowcentraltech.flowcentral.notification.senders;

import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.tcdng.unify.core.annotation.Component;

/**
 * Simple system alert sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = NotificationModuleNameConstants.SIMPLE_SYSTEM_ALERT_SENDER, description = "Simple System Alert Sender")
public class SimpleSystemNotificationAlertSender extends AbstractSimpleNotificationAlertSender {

    public SimpleSystemNotificationAlertSender() {
        super(NotifType.SYSTEM);
    }

}
