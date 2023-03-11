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
package com.flowcentraltech.flowcentral.integration.constants;

/**
 * Integration module error constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface IntegrationModuleErrorConstants {

    /** Local folder transport reader {0} failed to access local path {1} */
    String LOCALFILE_TRANSPORTREADER_UNKNOWNFOLDER = "INTEGRATION_0001";

    /** JMS text transport reader {0} received message in unsupported format */
    String JMSTEXT_TRANSPORTREADER_UNSUPPORTED_FORMAT = "INTEGRATION_0002";

    /** Can not find end-point configuration with name {0} */
    String CANNOT_FIND_ENDPOINT_CONFIG = "INTEGRATION_0003";

    /** Can not find inward end-point with name {0} */
    String CANNOT_FIND_INWARD_ENDPOINT = "INTEGRATION_0004";
}
