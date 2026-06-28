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
package com.flowcentraltech.flowcentral.system.constants;

import com.flowcentraltech.flowcentral.system.util.SystemUtils;

/**
 * System process variable constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface SystemProcessVariableConstants {
    
    String ENTITY_NAME = SystemUtils.encodeProcessVariableCode("entityName");
    
    String ENTITY_DESC = SystemUtils.encodeProcessVariableCode("entityDesc");
    
    String APP_TITLE = SystemUtils.encodeProcessVariableCode("appTitle");
    
    String APP_CORRESPONDER =  SystemUtils.encodeProcessVariableCode("appCorresponder");
    
    String APP_URL = SystemUtils.encodeProcessVariableCode("appUrl");
    
    String APP_HTML_LINK = SystemUtils.encodeProcessVariableCode("appHtmlLink");
}
