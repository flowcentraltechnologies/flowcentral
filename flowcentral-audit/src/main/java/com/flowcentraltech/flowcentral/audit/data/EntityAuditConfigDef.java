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
package com.flowcentraltech.flowcentral.audit.data;

import com.flowcentraltech.flowcentral.configuration.constants.AuditSourceType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity audit configuration definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityAuditConfigDef {

    private Long id;

    private long version;

    private AuditSourceType sourceType;

    private String name;

    private String description;

    private String entity;

    private String searchFieldA;

    private String searchFieldB;

    private String searchFieldC;

    private String searchFieldD;

    public EntityAuditConfigDef(Long id, long version, AuditSourceType sourceType, String name, String description,
            String entity, String searchFieldA, String searchFieldB, String searchFieldC, String searchFieldD) {
        this.id = id;
        this.version = version;
        this.sourceType = sourceType;
        this.name = name;
        this.description = description;
        this.entity = entity;
        this.searchFieldA = searchFieldA;
        this.searchFieldB = searchFieldB;
        this.searchFieldC = searchFieldC;
        this.searchFieldD = searchFieldD;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public AuditSourceType getSourceType() {
        return sourceType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEntity() {
        return entity;
    }

    public String getSearchFieldA() {
        return searchFieldA;
    }

    public boolean isWithSearchFieldA() {
        return !StringUtils.isBlank(searchFieldA);
    }

    public String getSearchFieldB() {
        return searchFieldB;
    }

    public boolean isWithSearchFieldB() {
        return !StringUtils.isBlank(searchFieldB);
    }

    public String getSearchFieldC() {
        return searchFieldC;
    }

    public boolean isWithSearchFieldC() {
        return !StringUtils.isBlank(searchFieldC);
    }

    public String getSearchFieldD() {
        return searchFieldD;
    }

    public boolean isWithSearchFieldD() {
        return !StringUtils.isBlank(searchFieldD);
    }

}
