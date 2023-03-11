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
package com.flowcentraltech.flowcentral.configuration.constants;

/**
 * Configuration module error constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ConfigurationModuleErrorConstants {

    /** Module configuration validation error. Config = {0}. */
    String MODULECONFIG_VALIDATION_ERROR = "CONFIG_0001";

    /** Application configuration validation error. Config = {0}. */
    String APPLICATIONCONFIG_VALIDATION_ERROR = "CONFIG_0002";

    /** Report configuration validation error. Config = {0}. */
    String REPORTCONFIG_VALIDATION_ERROR = "CONFIG_0003";

    /** Notification template configuration validation error. Config = {0}. */
    String NOTIFTEMPLATECONFIG_VALIDATION_ERROR = "CONFIG_0004";

    /** Workflow configuration validation error. Config = {0}. */
    String WFCONFIG_VALIDATION_ERROR = "CONFIG_0005";

    /** Workflow wizard configuration validation error. Config = {0}. */
    String WFWIZARDCONFIG_VALIDATION_ERROR = "CONFIG_0006";

}
