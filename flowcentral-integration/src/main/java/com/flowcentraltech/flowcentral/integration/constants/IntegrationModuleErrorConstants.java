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
package com.flowcentraltech.flowcentral.integration.constants;

/**
 * Integration module error constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface IntegrationModuleErrorConstants {

    /** Can not find end-point configuration with name {0} */
    String CANNOT_FIND_ENDPOINT_CONFIG = "INTEGRATION_0001";

    /** Can not find destination {0} for end-point configuration with name {1} */
    String CANNOT_FIND_DESTINATION_FOR_ENDPOINT_CONFIG = "INTEGRATION_0002";

    /** Can not find source {0} for end-point configuration with name {1} */
    String CANNOT_FIND_SOURCE_FOR_ENDPOINT_CONFIG = "INTEGRATION_0003";

}
