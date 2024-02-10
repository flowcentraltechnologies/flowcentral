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

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntity;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Application entity category.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ENTITYCATEGORY", uniqueConstraints = { @UniqueConstraint({ "appEntityId", "name" }),
        @UniqueConstraint({ "appEntityId", "description" }) })
public class AppEntityCategory extends BaseConfigEntity {

    @ForeignKey(AppEntity.class)
    private Long appEntityId;

    @Column(name = "CATEGORY_NM", length = 64)
    private String name;

    @Column(name = "CATEGORY_DESC", length = 128)
    private String description;

    @Column(name = "CATEGORY_LABEL", length = 64)
    private String label;

    @ListOnly(key = "appEntityId", property = "name")
    private String appEntityName;

    @ListOnly(key = "appEntityId", property = "applicationName")
    private String applicationName;
    
    @Child(category = "entity-category")
    private AppFilter filter;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public AppFilter getFilter() {
        return filter;
    }

    public void setFilter(AppFilter filter) {
        this.filter = filter;
    }

}
