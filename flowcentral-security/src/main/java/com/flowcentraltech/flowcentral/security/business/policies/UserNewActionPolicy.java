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

package com.flowcentraltech.flowcentral.security.business.policies;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractAppletActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleSysParamConstants;
import com.flowcentraltech.flowcentral.security.entities.User;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.security.OneWayStringCryptograph;
import com.tcdng.unify.core.security.PasswordGenerator;

/**
 * User new action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "security.user" })
@Component(name = "user-newactionpolicy", description = "$m{security.entityactionpolicy.newuser}")
public class UserNewActionPolicy extends AbstractAppletActionPolicy {

//    private static final String USER_PASSWORD = "USER_PASSWORD";

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable("oneway-stringcryptograph")
    private OneWayStringCryptograph passwordCryptograph;

    @Override
    public boolean checkAppliesTo(Entity inst) throws UnifyException {
        return true;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        User user = (User) ctx.getInst();
        if (user.getOriginalCopyId() == null) {
            logDebug("Executing new user pre-action policy for user [{0}] ...", user.getFullName());
            PasswordGenerator passwordGenerator = (PasswordGenerator) getComponent(systemModuleService
                    .getSysParameterValue(String.class, SecurityModuleSysParamConstants.USER_PASSWORD_GENERATOR));
            int passwordLength = systemModuleService.getSysParameterValue(int.class,
                    SecurityModuleSysParamConstants.USER_PASSWORD_LENGTH);

            String password = passwordGenerator.generatePassword(user.getLoginId(), passwordLength);
            user.setPassword(passwordCryptograph.encrypt(password));
//        ctx.setAttribute(USER_PASSWORD, password);
        }

        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        User user = (User) ctx.getInst();
        logDebug("Executing new user post-action policy for user [{0}] ...", user.getFullName());
        // TODO Send new password notification
        // String password = (String) ctx.getAttribute(USER_PASSWORD);

        return null;
    }

}
