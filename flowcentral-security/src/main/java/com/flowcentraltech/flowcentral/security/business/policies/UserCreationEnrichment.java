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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractWfEnrichmentPolicy;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.data.ValueStoreWriter;
import com.tcdng.unify.core.security.OneWayStringCryptograph;
import com.tcdng.unify.core.security.PasswordGenerator;

/**
 * User creation enrichment.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "security.user" }) 
@Component(name = "usercreation-enrichment", description = "$m{security.enrichmentpolicy.usercreation}")
public class UserCreationEnrichment extends AbstractWfEnrichmentPolicy {

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable("oneway-stringcryptograph")
    private OneWayStringCryptograph passwordCryptograph;

    @Override
    public void enrich(ValueStoreWriter wfItemWriter, ValueStoreReader wfItemReader, String rule)
            throws UnifyException {
        if (wfItemReader.read(Long.class, "originalCopyId") == null) {
            PasswordGenerator passwordGenerator = (PasswordGenerator) getComponent(systemModuleService
                    .getSysParameterValue(String.class, SecurityModuleSysParamConstants.USER_PASSWORD_GENERATOR));
            int passwordLength = systemModuleService.getSysParameterValue(int.class,
                    SecurityModuleSysParamConstants.USER_PASSWORD_LENGTH);

            final String plainPassword = passwordGenerator.generatePassword(wfItemReader.read(String.class, "loginId"),
                    passwordLength);
            final String encryptedPassword = passwordCryptograph.encrypt(plainPassword);
            setProcessVariable(wfItemWriter, "plainPassword", plainPassword);
            setProperty(wfItemWriter, "password", encryptedPassword);
        }
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return Collections.emptyList();
    }
}
