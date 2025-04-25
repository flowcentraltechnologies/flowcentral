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

package com.flowcentraltech.flowcentral.audit.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.flowcentraltech.flowcentral.configuration.constants.AuditSourceType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Entity audit configuration entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_ENTITYAUDITCONFIG", indexes = { @Index({ "sourceType" }), @Index({ "entity" }) },
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class EntityAuditConfig extends BaseStatusEntity {

    @ForeignKey(name = "SOURCE_TYPE")
    private AuditSourceType sourceType;

    @Column(name = "ENTITYAUDITCONFIG_NM", length = 64)
    private String name;

    @Column(name = "ENTITYAUDITCONFIG_DESC", length = 128)
    private String description;

    @Column(name = "ENTITY", length = 64)
    private String entity;

    @Column(name = "SEARCH_FIELDA", length = 32, nullable = true)
    private String searchFieldA;

    @Column(name = "SEARCH_FIELDB", length = 32, nullable = true)
    private String searchFieldB;

    @Column(name = "SEARCH_FIELDC", length = 32, nullable = true)
    private String searchFieldC;

    @Column(name = "SEARCH_FIELDD", length = 32, nullable = true)
    private String searchFieldD;

    @ListOnly(key = "sourceType", property = "description")
    private String sourceTypeDesc;

    public AuditSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(AuditSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getSearchFieldA() {
        return searchFieldA;
    }

    public void setSearchFieldA(String searchFieldA) {
        this.searchFieldA = searchFieldA;
    }

    public String getSearchFieldB() {
        return searchFieldB;
    }

    public void setSearchFieldB(String searchFieldB) {
        this.searchFieldB = searchFieldB;
    }

    public String getSearchFieldC() {
        return searchFieldC;
    }

    public void setSearchFieldC(String searchFieldC) {
        this.searchFieldC = searchFieldC;
    }

    public String getSearchFieldD() {
        return searchFieldD;
    }

    public void setSearchFieldD(String searchFieldD) {
        this.searchFieldD = searchFieldD;
    }

    public String getSourceTypeDesc() {
        return sourceTypeDesc;
    }

    public void setSourceTypeDesc(String sourceTypeDesc) {
        this.sourceTypeDesc = sourceTypeDesc;
    }

}
