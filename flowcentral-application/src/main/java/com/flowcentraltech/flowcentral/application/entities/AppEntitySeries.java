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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntity;
import com.flowcentraltech.flowcentral.configuration.constants.SeriesType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Application entity series.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_ENTITYSERIES", uniqueConstraints = { @UniqueConstraint({ "appEntityId", "name" }),
        @UniqueConstraint({ "appEntityId", "description" }) })
public class AppEntitySeries extends BaseConfigEntity {

    @ForeignKey(AppEntity.class)
    private Long appEntityId;

    @ForeignKey(name = "SERIES_TYPE")
    private SeriesType type;

    @Column(name = "SERIES_NM", length = 64)
    private String name;

    @Column(name = "SERIES_DESC", length = 128)
    private String description;

    @Column(name = "SERIES_LABEL", length = 64)
    private String label;

    @Column(name = "FIELD_NM", length = 64, nullable = true)
    private String fieldName;

    @ListOnly(key = "appEntityId", property = "name")
    private String appEntityName;

    @ListOnly(key = "appEntityId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getAppEntityId() {
        return appEntityId;
    }

    public void setAppEntityId(Long appEntityId) {
        this.appEntityId = appEntityId;
    }

    public SeriesType getType() {
        return type;
    }

    public void setType(SeriesType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getAppEntityName() {
        return appEntityName;
    }

    public void setAppEntityName(String appEntityName) {
        this.appEntityName = appEntityName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
