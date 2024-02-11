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
package com.flowcentraltech.flowcentral.configuration.constants;

/**
 * Module installation level constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ModuleInstallLevelConstants {

    int CONFIGURATION_MODULE_LEVEL = -8;

    int COMMON_MODULE_LEVEL = -4;

    int MESSAGING_MODULE_LEVEL = -2;

    int SYSTEM_MODULE_LEVEL = 0;

    int ORGANIZATION_MODULE_LEVEL = 4;

    int APPLICATION_MODULE_LEVEL = 8;

    int AUDIT_MODULE_LEVEL = 12;

    int NOTIFICATION_MODULE_LEVEL = 16;

    int REPORT_MODULE_LEVEL = 16;

    int CHART_MODULE_LEVEL = 16;

    int DASHBOARD_MODULE_LEVEL = 20;

    int WORKFLOW_MODULE_LEVEL = 24;

    int INTEGRATION_MODULE_LEVEL = 28;

    int SECURITY_MODULE_LEVEL = 32;

    int PRINCIPAL_MODULE_LEVEL = 48;

    int EXTENSION_MODULE_LEVEL = 64;

    int STUDIO_MODULE_LEVEL = 96;

    int MINIMAL_PRIORITY_LEVEL = 128;

}
