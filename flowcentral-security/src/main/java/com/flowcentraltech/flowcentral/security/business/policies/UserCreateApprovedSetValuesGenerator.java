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

package com.flowcentraltech.flowcentral.security.business.policies;

import com.flowcentraltech.flowcentral.application.business.AbstractEntitySetValuesGenerator;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.security.OneWayStringCryptograph;
import com.tcdng.unify.core.security.PasswordGenerator;

/**
 * User creation approved set values generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "security.user" })
@Component("user-createapprovedsetvaluesgenerator")
public class UserCreateApprovedSetValuesGenerator extends AbstractEntitySetValuesGenerator {

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable("oneway-stringcryptograph")
    private OneWayStringCryptograph passwordCryptograph;

    @Override
    public void generate(EntityDef entityDef, ValueStore valueStore, String trigger) throws UnifyException {
        logDebug("Generating set values...");
        if (valueStore.retrieve(Long.class, "originalCopyId") == null) {
            final String generator = systemModuleService.getSysParameterValue(String.class,
                    SecurityModuleSysParamConstants.USER_PASSWORD_GENERATOR);
            logDebug("Using password generator [{0}]...", generator);
            PasswordGenerator passwordGenerator = (PasswordGenerator) getComponent(generator);
            int passwordLength = systemModuleService.getSysParameterValue(int.class,
                    SecurityModuleSysParamConstants.USER_PASSWORD_LENGTH);

            final String plainPassword = passwordGenerator.generatePassword(valueStore.retrieve(String.class, "loginId"),
                    passwordLength);
            final String encryptedPassword = passwordCryptograph.encrypt(plainPassword);
            setProcessVariable(valueStore, "plainPassword", plainPassword);
            setProperty(valueStore, "password", encryptedPassword);
        }
    }

}
