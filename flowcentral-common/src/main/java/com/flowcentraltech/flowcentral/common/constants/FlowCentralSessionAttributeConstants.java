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
package com.flowcentraltech.flowcentral.common.constants;

/**
 * Keys for storing certain values in session scope.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface FlowCentralSessionAttributeConstants {

    /** Shortcut deck */
    String SHORTCUTDECK = "fc.shortcutdeck";
    
    /** User name */
    String USERNAME = "fc.username";
    
    /** User login ID */
    String USERLOGINID = "fc.userloginid";

    /** Branch Code */
    String BRANCHCODE = "fc.branchcode";

    /** Branch Description */
    String BRANCHDESC = "fc.branchdesc";

    /** Business Unit Description */
    String BUSINESSUNITDESC = "fc.businessunitdesc";

    /** Department code */
    String DEPARTMENTCODE = "fc.departmentcode";

    /** Role code */
    String ROLECODE = "fc.rolecode";

    /** Role description */
    String ROLEDESCRIPTION = "fc.roledescription";

    /** Reserved flag */
    String RESERVEDFLAG = "fc.reservedflag";

    /** Supervisor flag */
    String SUPERVISORFLAG = "fc.supervisorflag";

    /** Workspace code */
    String WORKSPACE_CODE = "fc.workspacecode";

    /** Locked resource options */
    String LOCKED_RESOURCEOPTIONS = "fc.lockedresourceoptions";

    /** Alternative resources bundle */
    String ALTERNATIVE_RESOURCES_BUNDLE = "fc.alternativeresourcesbundle";

    /** Current application IDe */
    String CURRENT_APPLICATION_ID = "fc.CURRENT_APPLICATION_ID";
}
