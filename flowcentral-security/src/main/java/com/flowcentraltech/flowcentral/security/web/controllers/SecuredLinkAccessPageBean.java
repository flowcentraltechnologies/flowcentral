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

package com.flowcentraltech.flowcentral.security.web.controllers;

import com.flowcentraltech.flowcentral.common.web.controllers.AbstractForwarderPageBean;

/**
 * Secured link access page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SecuredLinkAccessPageBean extends AbstractForwarderPageBean {

    private String targetParam;
    
    private String message;

    public String getTargetParam() {
        return targetParam;
    }

    public void setTargetParam(String targetParam) {
        this.targetParam = targetParam;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
        
}
