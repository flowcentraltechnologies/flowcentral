/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.connect.common.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.connect.configuration.constants.FieldDataType;

/**
 * Entity information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityInfo {

    private String entityManagerFactory;

    private String name;

    private String description;

    private String idFieldName;

    private String versionNoFieldName;

    private String handler;

    private Class<?> implClass;

    private Map<String, EntityFieldInfo> fieldsByName;

    private List<EntityFieldInfo> refFieldList;

    private List<EntityFieldInfo> fieldList;

    private List<EntityFieldInfo> listOnlyFieldList;

    private List<EntityFieldInfo> childFieldList;

    private List<EntityFieldInfo> childListFieldList;

    public EntityInfo(String entityManagerFactory, String name, String description, String idFieldName,
            String versionNoFieldName, String handler, Class<?> implClass, Map<String, EntityFieldInfo> fieldsByName) {
        this.entityManagerFactory = entityManagerFactory;
        this.name = name;
        this.description = description;
        this.idFieldName = idFieldName;
        this.versionNoFieldName = versionNoFieldName;
        this.handler = handler;
        this.implClass = implClass;
        this.fieldsByName = fieldsByName;
        this.refFieldList = new ArrayList<EntityFieldInfo>();
        this.fieldList = new ArrayList<EntityFieldInfo>();
        this.listOnlyFieldList = new ArrayList<EntityFieldInfo>();
        this.childFieldList = new ArrayList<EntityFieldInfo>();
        this.childListFieldList = new ArrayList<EntityFieldInfo>();
        for (EntityFieldInfo entityFieldInfo : fieldsByName.values()) {
            if (entityFieldInfo.isRef()) {
                this.refFieldList.add(entityFieldInfo);
            } else if (entityFieldInfo.isListOnly()) {
                this.listOnlyFieldList.add(entityFieldInfo);
            } else if (entityFieldInfo.isChild()) {
                this.childFieldList.add(entityFieldInfo);
            } else if (entityFieldInfo.isChildList()) {
                this.childListFieldList.add(entityFieldInfo);
            } else {
                this.fieldList.add(entityFieldInfo);
            }
        }

        this.refFieldList = Collections.unmodifiableList(this.refFieldList);
        this.fieldList = Collections.unmodifiableList(this.fieldList);
        this.listOnlyFieldList = Collections.unmodifiableList(this.listOnlyFieldList);
        this.childFieldList = Collections.unmodifiableList(this.childFieldList);
        this.childListFieldList = Collections.unmodifiableList(this.childListFieldList);
    }
    
    public String getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public String getVersionNoFieldName() {
        return versionNoFieldName;
    }

    public boolean isWithVersionNo() {
        return versionNoFieldName != null;
    }
    
    public String getHandler() {
        return handler;
    }

    public boolean isWithHandler() {
        return handler != null && !handler.trim().isEmpty();
    }

    public Class<?> getImplClass() {
        return implClass;
    }

    public Map<String, EntityFieldInfo> getFieldsByName() {
        return fieldsByName;
    }

    public List<EntityFieldInfo> getRefFieldList() {
        return refFieldList;
    }

    public List<EntityFieldInfo> getFieldList() {
        return fieldList;
    }

    public List<EntityFieldInfo> getListOnlyFieldList() {
        return listOnlyFieldList;
    }

    public List<EntityFieldInfo> getChildFieldList() {
        return childFieldList;
    }

    public List<EntityFieldInfo> getChildListFieldList() {
        return childListFieldList;
    }

    public EntityFieldInfo getEntityFieldInfo(String fieldName) throws Exception {
        EntityFieldInfo entityFieldInfo = fieldsByName.get(fieldName);
        if (entityFieldInfo == null) {
            throw new RuntimeException("Information for field [" + fieldName + "] not found.");
        }

        return entityFieldInfo;
    }

    public static Builder newBuilder(String entityManagerFactory) {
        return new Builder(entityManagerFactory);
    }

    public static class Builder {

        private String entityManagerFactory;

        private String name;

        private String description;

        private String idFieldName;

        private String versionNoFieldName;

        private String handler;

        private String implementation;

        private Map<String, EntityFieldInfo> fieldsByName;

        public Builder(String entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
            this.fieldsByName = new HashMap<String, EntityFieldInfo>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder idFieldName(String idFieldName) {
            this.idFieldName = idFieldName;
            return this;
        }

        public Builder versionNoFieldName(String versionNoFieldName) {
            this.versionNoFieldName = versionNoFieldName;
            return this;
        }

        public Builder handler(String handler) {
            this.handler = handler;
            return this;
        }

        public Builder implementation(String implementation) {
            this.implementation = implementation;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder addField(FieldDataType type, String fieldName, String references, String enumImpl)
                throws Exception {
            if (type == null) {
                throw new RuntimeException("Entity information field type is required");
            }

            if (fieldsByName.containsKey(name)) {
                throw new RuntimeException("Entity information for entity [" + name
                        + "] already contains information for field [" + fieldName + "]");
            }

            if (type.references() && (references == null || references.trim().isEmpty())) {
                throw new RuntimeException(
                        "References property required for entity [" + name + "] field[" + fieldName + "]");
            }

            Class<? extends Enum<?>> enumImplClass = enumImpl != null
                    ? (Class<? extends Enum<?>>) Class.forName(enumImpl)
                    : null;
            fieldsByName.put(fieldName, new EntityFieldInfo(type, fieldName, references, enumImplClass));
            return this;
        }

        public EntityInfo build() throws Exception {
            if (implementation == null) {
                throw new RuntimeException("Entity information implementation is required");
            }

            if (idFieldName == null) {
                throw new RuntimeException("Entity information ID field name is required");
            }

            Class<?> implClass = Class.forName(implementation);
            return new EntityInfo(entityManagerFactory, name, description, idFieldName, versionNoFieldName, handler,
                    implClass, fieldsByName);
        }
    }

}
