/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Filter configuration
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FilterConfig {

    private String name;

    private String description;

    private List<FilterRestrictionConfig> restrictionList;

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlAttribute
    public void setDescription(String description) {
        this.description = description;
    }

    public List<FilterRestrictionConfig> getRestrictionList() {
        return restrictionList;
    }

    @XmlElement(name = "restriction", required = true)
    public void setRestrictionList(List<FilterRestrictionConfig> restrictionList) {
        this.restrictionList = restrictionList;
    }

}
