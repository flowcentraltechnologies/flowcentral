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

import com.flowcentraltech.flowcentral.common.constants.SectorStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.system.entities.Module;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Application entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_APPLICATION",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class Application extends BaseConfigNamedEntity {

    @ForeignKey(Module.class)
    private Long moduleId;

    @Column(name = "APPLICATION_LABEL", length = 128)
    private String label;

    @Column
    private int displayIndex;

    @Column
    private boolean developable;

    @Column(defaultVal = "'Y'")
    private boolean menuAccess;

    @Column
    private boolean allowSecondaryTenants;

    @ListOnly(key = "moduleId", property = "name")
    private String moduleName;

    @ListOnly(key = "moduleId", property = "description")
    private String moduleDesc;

    @ListOnly(key = "moduleId", property = "label")
    private String moduleLabel;

    @ListOnly(key = "moduleId", property = "shortCode")
    private String moduleShortCode;

    @ListOnly(key = "moduleId", property = "sectorName")
    private String sectorName;

    @ListOnly(key = "moduleId", property = "sectorDesc")
    private String sectorDesc;

    @ListOnly(key = "moduleId", property = "sectorColor")
    private String sectorColor;

    @ListOnly(key = "moduleId", property = "sectorShortCode")
    private String sectorShortCode;

    @ListOnly(key = "moduleId", property = "sectorStatus")
    private SectorStatus sectorStatus;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }

    public boolean isDevelopable() {
        return developable;
    }

    public void setDevelopable(boolean developable) {
        this.developable = developable;
    }

    public boolean isMenuAccess() {
        return menuAccess;
    }

    public void setMenuAccess(boolean menuAccess) {
        this.menuAccess = menuAccess;
    }

    public boolean isAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    public void setAllowSecondaryTenants(boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
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

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorDesc() {
        return sectorDesc;
    }

    public void setSectorDesc(String sectorDesc) {
        this.sectorDesc = sectorDesc;
    }

    public String getSectorColor() {
        return sectorColor;
    }

    public void setSectorColor(String sectorColor) {
        this.sectorColor = sectorColor;
    }

    public String getSectorShortCode() {
        return sectorShortCode;
    }

    public void setSectorShortCode(String sectorShortCode) {
        this.sectorShortCode = sectorShortCode;
    }

    public SectorStatus getSectorStatus() {
        return sectorStatus;
    }

    public void setSectorStatus(SectorStatus sectorStatus) {
        this.sectorStatus = sectorStatus;
    }

}
