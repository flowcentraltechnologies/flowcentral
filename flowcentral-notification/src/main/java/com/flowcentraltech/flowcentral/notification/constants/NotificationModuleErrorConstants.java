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
package com.flowcentraltech.flowcentral.notification.constants;

/**
 * Notification module error constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface NotificationModuleErrorConstants {

    /** Can not find notification template {0}. */
    String CANNOT_FIND_NOTIFICATION_TEMPLATE = "NOTIF_0001";

    /** Notification channel with name [{0}] is unknown. */
    String NOTIFICATION_CHANNEL_WITH_NAME_UNKNOWN = "NOTIF_0002";

    /** Can not send a message of type [{0}] through notification channel [{1}]. */
    String CANNOT_SEND_NOTIFICATION_TYPE_THROUGH_CHANNEL = "NOTIF_0003";

    /** No notification messaging channel available for [{0}]. */
    String NOTIFICATION_CHANNEL_NOT_AVAILABLE = "NOTIF_0004";

    /** Can not construct message with no recipients. */
    String NOTIFICATION_CHANNEL_CONSTRUCT_NO_RECIPIENT = "NOTIF_0005";

    /** Can not find notification large text {0}. */
    String CANNOT_FIND_NOTIFICATION_LARGETEXT = "NOTIF_0006";

}
