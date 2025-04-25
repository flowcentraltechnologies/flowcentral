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

package com.flowcentraltech.flowcentral.application.validation;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Entity name exists validator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = "fc-entitynameexistvalidator", description = "$m{application.validator.uniqueentity}")
public class EntityNameExistsValidator extends AbstractValidator {

    @Configurable
    private ApplicationModuleService applicationModuleService;
    
    public EntityNameExistsValidator() {
        super("$m{application.validator.uniqueentity.failure}");
    }

    @Override
    public boolean validate(String rule, Object val) throws UnifyException {
        return !applicationModuleService.isEntityExist(String.valueOf(val));
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return Collections.emptyList();
    }

}
