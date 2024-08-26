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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.common.constants.StandardFormatType;
import com.tcdng.unify.core.batch.ConstraintAction;
import com.tcdng.unify.core.data.Listable;

/**
 * Entity upload definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityUploadDef implements Listable {

    private String name;

    private String description;

    private FieldSequenceDef fieldSequenceDef;

    private List<String> fieldNameList;

    private ConstraintAction constraintAction;

    private String header;

    public EntityUploadDef(String name, String description, FieldSequenceDef fieldSequenceDef,
            ConstraintAction constraintAction) {
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

    public String getHeader(EntityDef entityDef) {
        if (header == null) {
            synchronized (this) {
                if (header == null) {
                    StringBuilder sb = new StringBuilder();
                    boolean appendSym = false;
                    for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
                        if (appendSym) {
                            sb.append(", ");
                        } else {
                            appendSym = true;
                        }

                        sb.append("[");
                        sb.append(entityDef.getFieldDef(fieldSequenceEntryDef.getFieldName()).getFieldLabel());
                        if (fieldSequenceEntryDef.isWithStandardFormatCode()) {
                            StandardFormatType type = StandardFormatType
                                    .fromCode(fieldSequenceEntryDef.getStandardFormatCode());
                            if (type != null) {
                                sb.append("{").append(type.format()).append("}");
                            }
                        }
                        sb.append("]");
                    }

                    header = sb.toString();
                }
            }
        }

        return header;
    }

    public List<String> getFieldNameList() {
        if (fieldNameList == null) {
            fieldNameList = new ArrayList<String>();
            for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
                fieldNameList.add(fieldSequenceEntryDef.getFieldName());
            }

            fieldNameList = Collections.unmodifiableList(fieldNameList);
        }

        return fieldNameList;
    }

}
