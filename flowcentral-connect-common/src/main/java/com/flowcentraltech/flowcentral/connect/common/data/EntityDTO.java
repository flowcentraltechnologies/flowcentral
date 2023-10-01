/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
import java.util.List;

import com.flowcentraltech.flowcentral.connect.configuration.constants.XConnectEntityBaseType;

/**
 * Entity DTO.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityDTO {

    private XConnectEntityBaseType baseType;

    private String dataSourceAlias;

    private String name;

    private String description;

    private String tableName;

    private List<EntityFieldDTO> fields;

    public EntityDTO(EntityInfo entityInfo) {
        this.baseType = entityInfo.getBaseType();
        this.dataSourceAlias = entityInfo.getDataSourceAlias();
        this.name = entityInfo.getName();
        this.description = entityInfo.getDescription();
        this.tableName = entityInfo.getTableName();
        this.fields = new ArrayList<EntityFieldDTO>();
        for (EntityFieldInfo entityFieldInfo : entityInfo.getAllFields()) {
            EntityFieldDTO entityFieldDTO = new EntityFieldDTO(entityFieldInfo);
            this.fields.add(entityFieldDTO);
        }
    }

    public EntityDTO() {

    }

    public XConnectEntityBaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(XConnectEntityBaseType baseType) {
        this.baseType = baseType;
    }

    public String getDataSourceAlias() {
        return dataSourceAlias;
    }

    public void setDataSourceAlias(String dataSourceAlias) {
        this.dataSourceAlias = dataSourceAlias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<EntityFieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<EntityFieldDTO> fields) {
        this.fields = fields;
    }
}
