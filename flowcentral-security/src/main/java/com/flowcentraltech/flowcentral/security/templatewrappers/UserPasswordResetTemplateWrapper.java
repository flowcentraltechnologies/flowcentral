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
package com.flowcentraltech.flowcentral.security.templatewrappers;

import com.flowcentraltech.flowcentral.notification.data.BaseNotifTemplateWrapper;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateDef;
import com.tcdng.unify.common.util.ProcessVariableUtils;

/**
 * User password reset notification template wrapper.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class UserPasswordResetTemplateWrapper extends BaseNotifTemplateWrapper {
    
    public static final String __TEMPLATE_NAME = "security.userPasswordReset";
    private static final String FULL_NAME = "fullName";
    private static final String LOGIN_ID = "loginId";
    private static final String PLAIN_PASSWORD = ProcessVariableUtils.getVariable("plainPassword");

    public UserPasswordResetTemplateWrapper(NotifTemplateDef notifTemplateDef) {
        super(notifTemplateDef);
    }

    public void setFullName(String val) {
        nmb.addParam(FULL_NAME, val);
    }

    public void setLoginId(String val) {
        nmb.addParam(LOGIN_ID, val);
    }

    public void setPlainPassword(String val) {
        nmb.addParam(PLAIN_PASSWORD, val);
    }
}
