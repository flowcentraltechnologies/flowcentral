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

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Module application configuration
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ModuleAppConfig {

    private String name;

    private String shortDescription;

    private String longDescription;

    private String configFile;

    private boolean autoInstall;

    public String getName() {
        return name;
    }

    @XmlAttribute(required = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    @XmlAttribute(required = true)
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    @XmlAttribute(required = true)
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getConfigFile() {
        return configFile;
    }

    @XmlAttribute(required = true)
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public boolean isAutoInstall() {
        return autoInstall;
    }

    @XmlAttribute
    public void setAutoInstall(boolean autoInstall) {
        this.autoInstall = autoInstall;
    }

}
