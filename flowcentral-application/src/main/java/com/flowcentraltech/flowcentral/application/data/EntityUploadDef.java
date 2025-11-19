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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFieldUploadDef.ListProp;
import com.tcdng.unify.common.constants.StandardFormatType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.batch.ConstraintAction;

/**
 * Entity upload definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityUploadDef implements Listable {

    private String entity;

    private String name;

    private String description;

    private FieldSequenceDef fieldSequenceDef;

    private List<String> fieldNameList;

    private ConstraintAction constraintAction;

    private List<EntityFieldUploadDef> fieldUploadDefList;

    public EntityUploadDef(String entity, String name, String description, FieldSequenceDef fieldSequenceDef,
            ConstraintAction constraintAction) {
        this.entity = entity;
        this.name = name;
        this.description = description;
        this.fieldSequenceDef = fieldSequenceDef;
        this.constraintAction = constraintAction;
    }

    @Override
    public String getListDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public FieldSequenceDef getFieldSequenceDef() {
        return fieldSequenceDef;
    }

    public ConstraintAction getConstraintAction() {
        return constraintAction;
    }

    public String getHeader(AppletUtilities au, EntityDef entityDef) throws UnifyException {
        if (!entityDef.getLongName().equals(entity)) {
            throw new IllegalArgumentException("Incompatible with supplied entity definition.");
        }

        StringBuilder sb = new StringBuilder();
        boolean appendSym = false;
        for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
            if (appendSym) {
                sb.append(", ");
            } else {
                appendSym = true;
            }

            sb.append("[");
            sb.append(au.resolveSessionMessage(
                    entityDef.getFieldDef(fieldSequenceEntryDef.getFieldName()).getFieldLabel()));
            if (fieldSequenceEntryDef.isWithStandardFormatCode()) {
                StandardFormatType type = StandardFormatType.fromCode(fieldSequenceEntryDef.getStandardFormatCode());
                if (type != null) {
                    sb.append("{").append(type.format()).append("}");
                }
            }
            sb.append("]");
        }

        return sb.toString();
    }

    public List<EntityFieldUploadDef> getFieldUploadDefList(AppletUtilities au, EntityDef entityDef)
            throws UnifyException {
        if (!entityDef.getLongName().equals(entity)) {
            throw new IllegalArgumentException("Incompatible with supplied entity definition.");
        }

        if (fieldUploadDefList == null) {
            synchronized (this) {
                if (fieldUploadDefList != null) {
                    fieldUploadDefList = new ArrayList<EntityFieldUploadDef>();
                    Map<String, List<ListProp>> listOnlyToKeyMap = new HashMap<String, List<ListProp>>();
                    for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
                        EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldSequenceEntryDef.getFieldName());
                        if (entityFieldDef.isListOnly()) {
                            List<ListProp> properties = listOnlyToKeyMap.get(entityFieldDef.getKey());
                            if (properties == null) {
                                properties = new ArrayList<ListProp>();
                                listOnlyToKeyMap.put(entityFieldDef.getKey(), properties);
                            }

                            properties.add(new ListProp(fieldSequenceEntryDef.getFieldName(), entityFieldDef.getProperty()));
                        } else {
                            fieldUploadDefList.add(new EntityFieldUploadDef(fieldSequenceEntryDef.getFieldName()));
                        }
                    }

                    for (Map.Entry<String, List<ListProp>> entry : listOnlyToKeyMap.entrySet()) {
                        String keyFieldName = null;
                        EntityFieldDef refEntityFieldDef = entityDef.getFieldDef(keyFieldName = entry.getKey());
                        if (refEntityFieldDef.isEnumDataType()) {
                            fieldUploadDefList
                                    .add(new EntityFieldUploadDef(keyFieldName, refEntityFieldDef.getReferences()));
                        } else {
                            final RefDef refDef = au.getRefDef(refEntityFieldDef.getRefLongName());
                            final EntityDef refEntityDef = au.getEntityDef(refDef.getEntity());
                            List<String> properties = new ArrayList<String>();
                            for (ListProp listProp: entry.getValue()) {
                                properties.add(listProp.getProperty());
                            }
                            
                            String keyUniqueConstraintName = null;
                            for (UniqueConstraintDef uniqueConstraintDef : refEntityDef.getUniqueConstraintList()) {
                                if (uniqueConstraintDef.isAllFieldsMatch(properties)) {
                                    keyUniqueConstraintName = uniqueConstraintDef.getName();
                                    break;
                                }
                            }

                            fieldUploadDefList.add(new EntityFieldUploadDef(keyFieldName, refDef.getEntity(),
                                    keyUniqueConstraintName, entry.getValue()));
                        }

                    }

                    fieldUploadDefList = Collections.unmodifiableList(fieldUploadDefList);
                }
            }
        }

        return fieldUploadDefList;
    }

    public List<String> getFieldNameList() {
        if (fieldNameList == null) {
            synchronized (this) {
                if (fieldNameList == null) {
                    fieldNameList = new ArrayList<String>();
                    for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
                        fieldNameList.add(fieldSequenceEntryDef.getFieldName());
                    }

                    fieldNameList = Collections.unmodifiableList(fieldNameList);
                }
            }
        }

        return fieldNameList;
    }

}
