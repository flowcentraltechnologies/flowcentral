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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.EntityTypeFieldInfo;
import com.tcdng.unify.core.util.EntityTypeInfo;

/**
 * Entity composition object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityComposition {

    private final AppletUtilities au;

    private List<EntityCompositionEntry> entries;

    public EntityComposition(AppletUtilities au) {
        this.au = au;
        this.entries = new ArrayList<EntityCompositionEntry>();
    }

    public void normalize() throws UnifyException {
        for (EntityCompositionEntry entry : entries) {
            entry.normalize();
        }
    }

    public List<EntityCompositionEntry> getEntries() {
        return entries;
    }

    public void load(String applicationName, List<EntityTypeInfo> entityTypeList) throws UnifyException {
        final ApplicationDef applicationDef = au.application().getApplicationDef(applicationName);
        entries.clear();
        for (EntityTypeInfo entityTypeInfo : entityTypeList) {
            final int depth = entityTypeInfo.getDepth();
            EntityCompositionEntry entry = new EntityCompositionEntry();
            entry.setEntityName(entityTypeInfo.getName());
            entry.setTable(ApplicationCodeGenUtils.generateCustomEntityTableName(applicationDef.getModuleShortCode(),
                    entityTypeInfo.getName()));
            entry.setDepth(depth);
            entries.add(entry);

            for (EntityTypeFieldInfo entityTypeFieldInfo : entityTypeInfo.getFields()) {
                entry = new EntityCompositionEntry();
                entry.setFieldType(entityTypeFieldInfo.getType());
                entry.setDataType(entityTypeFieldInfo.getDataType());
                entry.setName(entityTypeFieldInfo.getName());
                entry.setColumn(entityTypeFieldInfo.getColumn());
                entry.setSample(entityTypeFieldInfo.getSample());
                entry.setDepth(depth);
                entries.add(entry);
            }
        }
    }

}
