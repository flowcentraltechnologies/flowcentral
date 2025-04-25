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
package com.flowcentraltech.flowcentral.configuration.constants;

/**
 * Default application constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface DefaultApplicationConstants {

    String ROOT_WORKSPACE_CODE = "wksp_root";

    String ROOT_WORKSPACE_NAME = "root";

    String ROOT_WORKSPACE_DESC = "$m{system.rootworkspace}";

    Long ROOT_WORKSPACE_ENTITY_ID = -99L;

    String SYSTEM_LOGINID = "SYSTEM";

    String SYSTEM_FULLNAME = "$m{system.fullname}";

    Long SYSTEM_ENTITY_ID = -99L;

    Long DEVOPS_DEPARTMENT_ID = -99L;
}
