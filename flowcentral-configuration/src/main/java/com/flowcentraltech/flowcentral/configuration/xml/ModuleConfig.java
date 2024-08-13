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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Module configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "module")
public class ModuleConfig extends BaseNameConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String shortCode;

    @JacksonXmlProperty(isAttribute = true)
    private boolean principal;
    
    @JacksonXmlProperty(localName = "applications")
    private ModuleAppsConfig moduleAppsConfig;

    @JacksonXmlProperty(localName = "sysParameters")
    private SysParamsConfig sysParamsConfig;

    public ModuleConfig() {
        this.principal = true;
    }
    
    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public ModuleAppsConfig getModuleAppsConfig() {
        return moduleAppsConfig;
    }

    public void setModuleAppsConfig(ModuleAppsConfig moduleAppsConfig) {
        this.moduleAppsConfig = moduleAppsConfig;
    }

    public SysParamsConfig getSysParamsConfig() {
        return sysParamsConfig;
    }

    public void setSysParamsConfig(SysParamsConfig sysParamsConfig) {
        this.sysParamsConfig = sysParamsConfig;
    }

}
