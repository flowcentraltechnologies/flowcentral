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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.configuration.constants.APIType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Index;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Application API entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_API", indexes = { @Index("entity") })
public class AppAPI extends BaseApplicationEntity {

    @ForeignKey(name = "API_TYPE")
    private APIType type;
    
    @Column(length = 128)
    private String entity;
    
    @Column(name = "CREATE_SETVALUES", length = 64, nullable = true)
    private String createSetValues;
    
    @Column(name = "UPDATE_SETVALUES", length = 64, nullable = true)
    private String updateSetValues;

    @Column(name = "SUPPORT_CREATE_FG", nullable = true)
    private Boolean supportCreate;

    @Column(name = "SUPPORT_READ_FG", nullable = true)
    private Boolean supportRead;

    @Column(name = "SUPPORT_UPDATE_FG", nullable = true)
    private Boolean supportUpdate;

    @Column(name = "SUPPORT_DELETE_FG", nullable = true)
    private Boolean supportDelete;
    
    @ListOnly(key = "type", property = "description")
    private String typeDesc;
    
    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public APIType getType() {
        return type;
    }

    public void setType(APIType type) {
        this.type = type;
    }

    public String getCreateSetValues() {
        return createSetValues;
    }

    public void setCreateSetValues(String createSetValues) {
        this.createSetValues = createSetValues;
    }

    public String getUpdateSetValues() {
        return updateSetValues;
    }

    public void setUpdateSetValues(String updateSetValues) {
        this.updateSetValues = updateSetValues;
    }

    public Boolean getSupportCreate() {
        return supportCreate;
    }

    public void setSupportCreate(Boolean supportCreate) {
        this.supportCreate = supportCreate;
    }

    public Boolean getSupportRead() {
        return supportRead;
    }

    public void setSupportRead(Boolean supportRead) {
        this.supportRead = supportRead;
    }

    public Boolean getSupportUpdate() {
        return supportUpdate;
    }

    public void setSupportUpdate(Boolean supportUpdate) {
        this.supportUpdate = supportUpdate;
    }

    public Boolean getSupportDelete() {
        return supportDelete;
    }

    public void setSupportDelete(Boolean supportDelete) {
        this.supportDelete = supportDelete;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
