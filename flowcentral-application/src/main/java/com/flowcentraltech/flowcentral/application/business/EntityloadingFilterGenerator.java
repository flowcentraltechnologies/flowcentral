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
package com.flowcentraltech.flowcentral.application.business;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.ValueStoreReader;

@EntityReferences({"application.appRef"})
@Component(name = "entityloading-filtergenerator", description = "Entity Loading Filter Generator")
public class EntityloadingFilterGenerator extends AbstractEntityBasedFilterGenerator {

    @Override
    public Restriction generate(ValueStoreReader entityInstReader, String rule) throws UnifyException {
        AppletType type = entityInstReader.read(AppletType.class, "type");
        if (AppletType.MANAGE_LOADINGLIST.equals(type)) {
            return new Equals("delegate", ApplicationModuleNameConstants.TABLE_LOADING_ENVIRONMENT_DELEGATE);
        }
        
        return allRecordsRestriction();
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return Collections.emptyList();
    }

}
