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
package com.flowcentraltech.flowcentral.collaboration.entities;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Entity for storing collaboration lock information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_COLLABORATIONLOCK", uniqueConstraints = { @UniqueConstraint({ "type", "resourceName" }) },
        indexes = { @Index({ "applicationName" }), @Index({ "resourceName" }) })
public class CollaborationLock extends BaseAuditEntity {

    @ForeignKey
    private CollaborationType type;

    @Column(name = "APPLICATION_NM", length = 64)
    private String applicationName;

    @Column(name = "RESOURCE_NM", length = 128)
    private String resourceName;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    public CollaborationLock(CollaborationType type, String resourceName) throws UnifyException {
        this.type = type;
        this.applicationName = ApplicationNameUtils.getApplicationEntityNameParts(resourceName).getApplicationName();
        this.resourceName = resourceName;
    }

    public CollaborationLock() {

    }

    @Override
    public String getDescription() {
        return resourceName;
    }

    public CollaborationType getType() {
        return type;
    }

    public void setType(CollaborationType type) {
        this.type = type;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
