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

package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.common.data.VersionedEntityDef;

/**
 * Application definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ApplicationDef implements VersionedEntityDef {

    private String name;

    private String description;

    private Long id;

    private long version;

    private boolean developable;

    private boolean menuAccess;

    private String moduleName;

    private String moduleDesc;

    private String moduleLabel;

    private String moduleShortCode;

    private String sectorShortCode;

    private String sectorColor;

    public ApplicationDef(String name, String description, Long id, long version, boolean developable,
            boolean menuAccess, String moduleName, String moduleDesc, String moduleLabel, String moduleShortCode,
            String sectorShortCode, String sectorColor) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.version = version;
        this.developable = developable;
        this.menuAccess = menuAccess;
        this.moduleName = moduleName;
        this.moduleDesc = moduleDesc;
        this.moduleLabel = moduleLabel;
        this.moduleShortCode = moduleShortCode;
        this.sectorShortCode = sectorShortCode;
        this.sectorColor = sectorColor;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public boolean isDevelopable() {
        return developable;
    }

    public boolean isMenuAccess() {
        return menuAccess;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public String getModuleLabel() {
        return moduleLabel;
    }

    public String getModuleShortCode() {
        return moduleShortCode;
    }

    public String getSectorShortCode() {
        return sectorShortCode;
    }

    public String getSectorColor() {
        return sectorColor;
    }
}
