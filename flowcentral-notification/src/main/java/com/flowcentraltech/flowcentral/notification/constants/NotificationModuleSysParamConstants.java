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
package com.flowcentraltech.flowcentral.notification.constants;

/**
 * Notification module system parameter constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface NotificationModuleSysParamConstants {

    String NOTIFICATION_ENABLED = "NOT-0001";

    String NOTIFICATION_MAXIMUM_TRIES = "NOT-0002";

    String NOTIFICATION_RETRY_MINUTES = "NOT-0003";

    String NOTIFICATION_MAX_BATCH_SIZE = "NOT-0004";

    String NOTIFICATION_TEST_MODE_ENABLED = "NOT-0005";

    String NOTIFICATION_TEST_MODE_TO_EMAILS = "NOT-0006";

    String NOTIFICATION_TEST_MODE_CC_EMAILS = "NOT-0007";
    
    String NOTIFICATION_TEST_MODE_BCC_EMAILS = "NOT-0008";
    
    String NOTIFICATION_HTML_EMAILS_ENABLED = "NOT-0009";
}
