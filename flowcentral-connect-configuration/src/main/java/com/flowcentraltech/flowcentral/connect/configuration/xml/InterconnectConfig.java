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
package com.flowcentraltech.flowcentral.connect.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Interconnect configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "interconnect")
public class InterconnectConfig {

    private String name;

    private String description;

    private String redirect;

    private InterconnectAppConfigs interconnectAppConfigs;

    public String getName() {
        return name;
    }

    @XmlAttribute(required = true)
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlAttribute(required = true)
    public void setDescription(String description) {
        this.description = description;
    }

    public String getRedirect() {
        return redirect;
    }

    @XmlAttribute
    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public InterconnectAppConfigs getInterconnectAppConfigs() {
        return interconnectAppConfigs;
    }

    @XmlElement(name = "interconnect-applications")
    public void setInterconnectAppConfigs(InterconnectAppConfigs interconnectAppConfigs) {
        this.interconnectAppConfigs = interconnectAppConfigs;
    }

}
