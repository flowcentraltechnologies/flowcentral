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
package com.flowcentraltech.flowcentral.application.data;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;

/**
 * Entity schema.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntitySchema {

    private EntityBaseType baseType;

    private String delegate;

    private String dataSourceAlias;

    private String implClass;

    private String entity;

    private String name;

    private String description;

    private String tableName;

    private boolean dynamic;

    private boolean actionPolicy;

    private List<EntityFieldSchema> fields;

    public EntitySchema(EntityBaseType baseType, String delegate, String dataSourceAlias, String implClass,
            String entity, String name, String description, String tableName, boolean dynamic, boolean actionPolicy,
            List<EntityFieldSchema> fields) {
        this.baseType = baseType;
        this.delegate = delegate;
        this.dataSourceAlias = dataSourceAlias;
        this.implClass = implClass;
        this.entity = entity;
        this.name = name;
        this.description = description;
        this.tableName = tableName;
        this.dynamic = dynamic;
        this.actionPolicy = actionPolicy;
        this.fields = fields;
    }

    public EntityBaseType getBaseType() {
        return baseType;
    }

    public String getDelegate() {
        return delegate;
    }

    public String getDataSourceAlias() {
        return dataSourceAlias;
    }

    public String getEntity() {
        return entity;
    }

    public String getImplClass() {
        return implClass;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public boolean isActionPolicy() {
        return actionPolicy;
    }

    public List<EntityFieldSchema> getFields() {
        return fields;
    }

}
