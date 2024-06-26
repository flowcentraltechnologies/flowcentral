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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Property lists configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PropertyListsConfig extends BaseConfig {

    private List<PropertyListConfig> propertyConfigList;

    public List<PropertyListConfig> getPropertyConfigList() {
        return propertyConfigList;
    }

    @XmlElement(name = "propertyList")
    public void setPropertyConfigList(List<PropertyListConfig> propertyConfigList) {
        this.propertyConfigList = propertyConfigList;
    }

}
