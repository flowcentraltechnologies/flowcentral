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

package com.flowcentraltech.flowcentral.workflow.business.policies;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.UniqueConditionDef;
import com.flowcentraltech.flowcentral.application.data.UniqueConstraintDef;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractWfBinaryPolicy;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Query;

/**
 * Unique entity workflow binary policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = "uniqueentity-wfbinarypolicy", description = "$m{workflow.binarypolicy.uniqueentity}")
public class UniqueEntityWfBinaryPolicy extends AbstractWfBinaryPolicy {

    @Configurable
    private ApplicationModuleService applicationModuleService;
    
    @Configurable
    private EnvironmentService environmentService;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean evaluate(ValueStoreReader wfItemReader, String entity, String rule) throws UnifyException {
        EntityDef entityDef = applicationModuleService.getEntityDef(entity);
        if (entityDef.isWithUniqueConstraints()) {
            EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(entity);
            Long id = wfItemReader.read(Long.class, "id");
            for (UniqueConstraintDef uniqueConstraintDef: entityDef.getUniqueConstraintList()) {
                Query query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
                if (id != null) {
                    query.addNotEquals("id", id);
                }

                for (String fieldName : uniqueConstraintDef.getFieldList()) {
                    query.addEquals(fieldName, wfItemReader.read(fieldName));
                }

                if (uniqueConstraintDef.isWithConditionList()) {
                    for (UniqueConditionDef ucd: uniqueConstraintDef.getConditionList()) {
                        query.addRestriction(ucd.getRestriction());
                    }
                }

                if (!query.isEmptyCriteria() && environmentService.countAll(query) > 0) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return Collections.emptyList();
    }

}
