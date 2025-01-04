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
package com.flowcentraltech.flowcentral.organization.entities;

import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Privilege entity.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@Table(name = "FC_PRIVILEGE", uniqueConstraints = { @UniqueConstraint({ "code" }) })
public class Privilege extends BaseAuditEntity {

    @ForeignKey(Application.class)
    private Long applicationId;

    @ForeignKey(PrivilegeCategory.class)
    private Long privilegeCategoryId;

    @Column(nullable = true)
    private ConfigType configType;
    
    @Column(name = "PRIVILEGE_CD", length = 64)
    private String code;

    @Column(name = "PRIVILEGE_DESC", length = 96)
    private String description;

    @ListOnly(key = "applicationId", property = "name")
    private String applicationName;

    @ListOnly(key = "applicationId", property = "description")
    private String applicationDesc;

    @ListOnly(key = "privilegeCategoryId", property = "code")
    private String privilegeCatCode;

    @ListOnly(key = "privilegeCategoryId", property = "description")
    private String privilegeCatDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getPrivilegeCategoryId() {
        return privilegeCategoryId;
    }

    public void setPrivilegeCategoryId(Long privilegeCategoryId) {
        this.privilegeCategoryId = privilegeCategoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrivilegeCatCode() {
        return privilegeCatCode;
    }

    public void setPrivilegeCatCode(String privilegeCatCode) {
        this.privilegeCatCode = privilegeCatCode;
    }

    public String getPrivilegeCatDesc() {
        return privilegeCatDesc;
    }

    public void setPrivilegeCatDesc(String privilegeCatDesc) {
        this.privilegeCatDesc = privilegeCatDesc;
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

    public String getApplicationDesc() {
        return applicationDesc;
    }

    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

}
