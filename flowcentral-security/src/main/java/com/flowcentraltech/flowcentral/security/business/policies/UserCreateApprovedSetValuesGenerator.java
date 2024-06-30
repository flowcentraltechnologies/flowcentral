/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
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
 * @since 1.0
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
        if (valueStore.retrieve(Long.class, "originalCopyId") == null) {
            PasswordGenerator passwordGenerator = (PasswordGenerator) getComponent(systemModuleService
                    .getSysParameterValue(String.class, SecurityModuleSysParamConstants.USER_PASSWORD_GENERATOR));
            int passwordLength = systemModuleService.getSysParameterValue(int.class,
                    SecurityModuleSysParamConstants.USER_PASSWORD_LENGTH);

            String plainPassword = passwordGenerator.generatePassword(valueStore.retrieve(String.class, "loginId"),
                    passwordLength);
            String encryptedPassword = passwordCryptograph.encrypt(plainPassword);
            valueStore.setTempValue("plainPassword", plainPassword);
            valueStore.store("password", encryptedPassword);
        }
    }

}
