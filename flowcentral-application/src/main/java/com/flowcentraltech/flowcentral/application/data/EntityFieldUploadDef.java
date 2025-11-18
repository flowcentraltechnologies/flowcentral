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

/**
 * Entity field upload definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityFieldUploadDef {

    private FieldSequenceEntryDef fieldSequenceEntryDef;

    private String keyFieldName;

    private String keyEntityLongName;

    private String keyUniqueConstraintName;

    private boolean enumType;
    
    public EntityFieldUploadDef(String keyFieldName, String keyEntityLongName) {
        this.keyFieldName = keyFieldName;
        this.keyEntityLongName = keyEntityLongName;
        this.enumType = true;
    }

    public EntityFieldUploadDef(String keyFieldName, String keyEntityLongName, String keyUniqueConstraintName) {
        this.keyFieldName = keyFieldName;
        this.keyEntityLongName = keyEntityLongName;
        this.keyUniqueConstraintName = keyUniqueConstraintName;
    }

    public EntityFieldUploadDef(FieldSequenceEntryDef fieldSequenceEntryDef) {
        this.fieldSequenceEntryDef = fieldSequenceEntryDef;
    }

    public String getFieldName() {
        return fieldSequenceEntryDef != null ? fieldSequenceEntryDef.getFieldName() : keyFieldName;
    }

    public String getKeyFieldName() {
        return keyFieldName;
    }

    public String getKeyEntityLongName() {
        return keyEntityLongName;
    }

    public String getKeyUniqueConstraintName() {
        return keyUniqueConstraintName;
    }

    public boolean isResolveKeyValue() {
        return keyFieldName != null;
    }

    public boolean isEnumTypeValue() {
        return enumType;
    }

}
