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

package com.flowcentraltech.flowcentral.studio.business.policies;

import com.flowcentraltech.flowcentral.application.business.AbstractFieldSetValueGenerator;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.studio.constants.StudioModuleSysParamConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Studio entity table name set value generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "application.appEntity" })
@Component(name = "studioentitytablename-generator", description = "$m{studio.setvaluegenerator.entitytablename}")
public class EntityTableNameSetValueGenerator extends AbstractFieldSetValueGenerator {

    @Override
    public Object generate(EntityDef entityDef, ValueStore valueStore, String rule) throws UnifyException {
        final String tablePrefix = system().getSysParameterValue(String.class,
                StudioModuleSysParamConstants.DEFAULT_TABLE_PREFIX);
        ApplicationDef applicationDef = application().getApplicationDef(entityDef.getApplicationName());
        return ApplicationCodeGenUtils.generateCustomEntityTableName(tablePrefix, applicationDef.getModuleShortCode(),
                valueStore.retrieve(String.class, "name"));
    }

}
