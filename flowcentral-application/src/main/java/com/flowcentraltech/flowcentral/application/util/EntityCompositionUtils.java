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
package com.flowcentraltech.flowcentral.application.util;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.EntityComposition;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityCompositionEntry;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DynamicEntityFieldType;
import com.tcdng.unify.core.util.EntityTypeFieldInfo;
import com.tcdng.unify.core.util.EntityTypeInfo;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity composition utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class EntityCompositionUtils {

    private EntityCompositionUtils() {

    }

    public static EntityComposition createEntityComposition(String tablePrefix, String moduleShortCode,
            List<EntityTypeInfo> entityTypeList) throws UnifyException {
        List<EntityCompositionEntry> entries = new ArrayList<EntityCompositionEntry>();
        for (EntityTypeInfo entityTypeInfo : entityTypeList) {
            final int depth = entityTypeInfo.getDepth();
            EntityCompositionEntry entry = new EntityCompositionEntry();
            entry.setEntityName(entityTypeInfo.getName());
            entry.setTable(ApplicationCodeGenUtils.generateCustomEntityTableName(tablePrefix, moduleShortCode,
                    entityTypeInfo.getName()));
            entry.setDepth(depth);
            entries.add(entry);

            for (EntityTypeFieldInfo entityTypeFieldInfo : entityTypeInfo.getFields()) {
                entry = new EntityCompositionEntry();
                DynamicEntityFieldType fieldType = entityTypeFieldInfo.getType();
                entry.setFieldType(fieldType);
                entry.setDataType(
                        EntityFieldDataType.fromName(entityTypeFieldInfo.getDataType(), entityTypeFieldInfo.isArray()));
                entry.setName(StringUtils.decapitalize(entityTypeFieldInfo.getName()));
                entry.setJsonName(entityTypeFieldInfo.getName());
                entry.setColumn(entityTypeFieldInfo.getColumn());
                entry.setSample(entityTypeFieldInfo.getSample());
                entry.setDepth(depth);

                if (fieldType.isChild() || fieldType.isChildList()) {
                    entry.setReferences(entityTypeFieldInfo.getChildEntityName());
                } else if (fieldType.isForeignKey()) {
                    entry.setReferences(entityTypeFieldInfo.getParentEntityName());
                }

                entries.add(entry);
            }
        }

        return new EntityComposition(entries);
    }

}
