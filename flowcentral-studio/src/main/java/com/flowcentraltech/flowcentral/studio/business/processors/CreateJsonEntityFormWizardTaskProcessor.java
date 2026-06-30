/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.studio.business.processors;

import java.lang.reflect.Field;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Create JSON entity form wizard policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "studio.studioJsonEntity" })
@Component("createjsonentity-formwizardprocessor")
public class CreateJsonEntityFormWizardTaskProcessor extends AbstractCreateEntityFormWizardTaskProcessor {

    @SuppressWarnings("unchecked")
    @Override
    protected void loadSource(TaskMonitor taskMonitor, String source, String entity) throws UnifyException {
        final EntityClassDef entityClassDef = au().getEntityClassDef(entity);
        System.out.println("@prime: comp = " + StringUtils.toXmlString(entityClassDef.getJsonComposition(au())));
        System.out.println("@prime: source = " + source);
        for (Field field : entityClassDef.getEntityClass().getDeclaredFields()) {
            System.out.println("@prime: field = " + field.getName());
        }

        final List<? extends Entity> items = DataUtils.listFromJsonString(entityClassDef.getJsonComposition(au()),
                (Class<? extends Entity>) entityClassDef.getEntityClass(), source);
        for (Entity inst : items) {
            au().environment().create(inst);
        }
    }

}
