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
package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Module application entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_MODULEAPP",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "shortDescription" }) })
public class ModuleApp extends BaseAuditEntity {

    @ForeignKey(Module.class)
    private Long moduleId;

    @Column(length = 64)
    private String name;

    @Column(length = 96)
    private String shortDescription;

    @Column(length = 256)
    private String longDescription;

    @Column(length = 128)
    private String configFile;

    @Column
    private boolean autoInstall;

    @ListOnly(key = "moduleId", property = "name")
    private String moduleName;

    @ListOnly(key = "moduleId", property = "description")
    private String moduleDesc;

    @ListOnly(key = "moduleId", property = "label")
    private String moduleLabel;

    @ListOnly(key = "moduleId", property = "shortCode")
    private String moduleShortCode;

    @Override
    public String getDescription() {
        return shortDescription;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public boolean isAutoInstall() {
        return autoInstall;
    }

    public void setAutoInstall(boolean autoInstall) {
        this.autoInstall = autoInstall;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public String getModuleLabel() {
        return moduleLabel;
    }

    public void setModuleLabel(String moduleLabel) {
        this.moduleLabel = moduleLabel;
    }

    public String getModuleShortCode() {
        return moduleShortCode;
    }

    public void setModuleShortCode(String moduleShortCode) {
        this.moduleShortCode = moduleShortCode;
    }

}
