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

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Application entity search input entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ENTITYSEARCHINPUT", uniqueConstraints = { @UniqueConstraint({ "appEntityId", "name" }),
        @UniqueConstraint({ "appEntityId", "description" }) })
public class AppEntitySearchInput extends BaseConfigNamedEntity {

    @ForeignKey(AppEntity.class)
    private Long appEntityId;

    @Column(name = "RESTRICTION_RESOLVER", length = 64, nullable = true)
    private String restrictionResolver;
    
    @ListOnly(key = "appEntityId", property = "applicationId")
    private Long applicationId;

    @ListOnly(key = "appEntityId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "appEntityId", property = "name")
    private String entityName;
    
    @Child(category = "entity-searchinput")
    private AppSearchInput searchInput;

    public AppEntitySearchInput(String name, String description, String definition) {
        this.setName(name);
        this.setDescription(description);
        this.searchInput = new AppSearchInput(definition);
    }

    public AppEntitySearchInput() {
        
    }

    public Long getAppEntityId() {
        return appEntityId;
    }

    public void setAppEntityId(Long appEntityId) {
        this.appEntityId = appEntityId;
    }

    public String getRestrictionResolver() {
        return restrictionResolver;
    }

    public void setRestrictionResolver(String restrictionResolver) {
        this.restrictionResolver = restrictionResolver;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public AppSearchInput getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(AppSearchInput searchInput) {
        this.searchInput = searchInput;
    }

}
