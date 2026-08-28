/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Application entity process variable.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_ENTITYPROCVARIABLE",
    uniqueConstraints = {
            @UniqueConstraint({ "appEntityId", "name" }) })
public class AppEntityProcessVariable extends BaseConfigEntity {

    @ForeignKey(AppEntity.class)
    private Long appEntityId;

    @Column(name = "ENTITYPROCVAR_NM", length = 64)
    private String name;

    @Column(name = "ENTITYPROCVAR_LABEL", length = 128)
    private String label;

    @Column
    private boolean supportFilter;

    @Column
    private boolean supportValues;

    @Override
    public String getDescription() {
        return label;
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

    public boolean isSupportFilter() {
        return supportFilter;
    }

    public void setSupportFilter(boolean supportFilter) {
        this.supportFilter = supportFilter;
    }

    public boolean isSupportValues() {
        return supportValues;
    }

    public void setSupportValues(boolean supportValues) {
        this.supportValues = supportValues;
    }

}
