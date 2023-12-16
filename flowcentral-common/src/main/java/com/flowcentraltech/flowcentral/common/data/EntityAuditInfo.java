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

package com.flowcentraltech.flowcentral.common.data;

import java.util.Collections;
import java.util.List;

/**
 * Entity audit information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityAuditInfo {

    private final String entity;

    private List<String> inclusions;

    private boolean auditable;

    public EntityAuditInfo(String entity, List<String> inclusions, boolean auditable) {
        this.entity = entity;
        this.inclusions = inclusions;
        this.auditable = auditable;
    }

    public EntityAuditInfo(String entity, boolean auditable) {
        this.entity = entity;
        this.auditable = auditable;
        this.inclusions = Collections.emptyList();
    }

    public EntityAuditInfo(String entity) {
        this.entity = entity;
        this.auditable = false;
        this.inclusions = Collections.emptyList();
    }

    public String getEntity() {
        return entity;
    }

    public List<String> getInclusions() {
        return inclusions;
    }

    public boolean inclusions() {
        return !inclusions.isEmpty();
    }

    public boolean auditable() {
        return auditable;
    }

}
