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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Module configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "module")
public class ModuleConfig extends BaseNameConfig {

    private String shortCode;

    private boolean principal;
    
    private ModuleAppsConfig moduleAppsConfig;

    private SysParamsConfig sysParamsConfig;

    public ModuleConfig() {
        this.principal = true;
    }
    
    public String getShortCode() {
        return shortCode;
    }

    @XmlAttribute(required = true)
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public boolean isPrincipal() {
        return principal;
    }

    @XmlAttribute
    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public ModuleAppsConfig getModuleAppsConfig() {
        return moduleAppsConfig;
    }

    @XmlElement(name = "applications")
    public void setModuleAppsConfig(ModuleAppsConfig moduleAppsConfig) {
        this.moduleAppsConfig = moduleAppsConfig;
    }

    public SysParamsConfig getSysParamsConfig() {
        return sysParamsConfig;
    }

    @XmlElement(name = "sysParameters")
    public void setSysParamsConfig(SysParamsConfig sysParamsConfig) {
        this.sysParamsConfig = sysParamsConfig;
    }

    @Override
    public String toString() {
        return "ModuleConfig [shortCode=" + shortCode + ", principal=" + principal + ", moduleAppsConfig="
                + moduleAppsConfig + ", sysParamsConfig=" + sysParamsConfig + "]";
    }

}
