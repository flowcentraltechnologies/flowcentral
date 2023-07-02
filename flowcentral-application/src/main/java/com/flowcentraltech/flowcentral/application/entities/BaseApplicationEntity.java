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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.constants.VersionType;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.UniqueConstraints;

/**
 * Convenient abstract base class for application definitions.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Policy("baseapplication-entitypolicy")
@UniqueConstraints({ @UniqueConstraint({ "versionType", "applicationId", "name" }),
        @UniqueConstraint({ "versionType", "applicationId", "description" }) })
public abstract class BaseApplicationEntity extends BaseConfigNamedEntity {

    @ForeignKey(Application.class)
    private Long applicationId;
    
    @Column
    private VersionType versionType;
    
    @Column(length = 36, nullable = true)
    private String rootMergeVersionNo;
    
    @ListOnly(key = "applicationId", property = "name")
    private String applicationName;

    @ListOnly(key = "applicationId", property = "description")
    private String applicationDesc;

    @ListOnly(key = "applicationId", property = "moduleName")
    private String moduleName;

    public final Long getApplicationId() {
        return applicationId;
    }

    public final void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public final VersionType getVersionType() {
        return versionType;
    }

    public final void setVersionType(VersionType versionType) {
        this.versionType = versionType;
    }

    public final String getRootMergeVersionNo() {
        return rootMergeVersionNo;
    }

    public final void setRootMergeVersionNo(String rootMergeVersionNo) {
        this.rootMergeVersionNo = rootMergeVersionNo;
    }

    public final String getApplicationName() {
        return applicationName;
    }

    public final void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public final String getApplicationDesc() {
        return applicationDesc;
    }

    public final void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

    public final String getModuleName() {
        return moduleName;
    }

    public final void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

}
