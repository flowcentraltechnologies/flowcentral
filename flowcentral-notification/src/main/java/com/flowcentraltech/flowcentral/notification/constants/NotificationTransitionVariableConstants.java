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
package com.flowcentraltech.flowcentral.notification.constants;

import com.flowcentraltech.flowcentral.system.util.SystemUtils;

/**
 * Notification transition variable constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface NotificationTransitionVariableConstants {

    String TEMPLATE_VARIABLE = SystemUtils.encodeProcessVariableCode("template");

    String WFITEM_LINK_VARIABLE = SystemUtils.encodeProcessVariableCode("wfItemLink");

    String WFITEM_HTMLLINK_VARIABLE = SystemUtils.encodeProcessVariableCode("wfItemHtmlLink");

    String PLAIN_PASSWORD = SystemUtils.encodeProcessVariableCode("plainPassword");
}
