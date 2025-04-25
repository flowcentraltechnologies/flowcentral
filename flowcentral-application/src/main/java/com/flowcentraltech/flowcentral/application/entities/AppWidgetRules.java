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

package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.CategoryColumn;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.FosterParentId;
import com.tcdng.unify.core.annotation.FosterParentType;

/**
 * Application widget rules entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_WIDGETRULES", indexes = { @Index(value = { "entity" }), @Index(value = { "category" }) })
public class AppWidgetRules extends BaseAuditEntity {

    @FosterParentId
    private Long entityInstId;

    @FosterParentType(length = 128)
    private String entity;

    @CategoryColumn(name = "WIDGETRULES_CAT")
    private String category;

    @Column(type = ColumnType.CLOB, name = "WIDGETRULES_DEF", nullable = true)
    private String definition;

    public AppWidgetRules(String definition) {
        this.definition = definition;
    }

    public AppWidgetRules() {

    }

    @Override
    public String getDescription() {
        return category;
    }

    public Long getEntityInstId() {
        return entityInstId;
    }

    public void setEntityInstId(Long entityInstId) {
        this.entityInstId = entityInstId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
