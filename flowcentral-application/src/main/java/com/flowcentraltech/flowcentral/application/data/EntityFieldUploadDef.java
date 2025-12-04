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

import java.util.List;

/**
 * Entity field upload definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityFieldUploadDef {

    private String fieldName;

    private String keyFieldName;

    private String keyEntityLongName;

    private List<ListProp> listProps;

    private boolean enumType;

    public EntityFieldUploadDef(String keyFieldName, String keyEntityLongName) {
        this.keyFieldName = keyFieldName;
        this.keyEntityLongName = keyEntityLongName;
        this.enumType = true;
    }

    public EntityFieldUploadDef(String keyFieldName, String keyEntityLongName,
            List<ListProp> listProps) {
        this.keyFieldName = keyFieldName;
        this.keyEntityLongName = keyEntityLongName;
        this.listProps = listProps;
    }

    public EntityFieldUploadDef(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName != null ? fieldName : keyFieldName;
    }

    public String getKeyFieldName() {
        return keyFieldName;
    }

    public String getKeyEntityLongName() {
        return keyEntityLongName;
    }

    public List<ListProp> getListProps() {
        return listProps;
    }

    public boolean isResolveKeyValue() {
        return keyFieldName != null;
    }

    public boolean isEnumTypeValue() {
        return enumType;
    }

    public static class ListProp {

        final private String fieldName;

        final private String property;

        public ListProp(String fieldName, String property) {
            this.fieldName = fieldName;
            this.property = property;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getProperty() {
            return property;
        }
    }
}
