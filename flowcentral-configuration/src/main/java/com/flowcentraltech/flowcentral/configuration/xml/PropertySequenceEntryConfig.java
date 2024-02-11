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
 * Property sequence entry configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PropertySequenceEntryConfig {

    private String property;

    private String label;

    public PropertySequenceEntryConfig(String property, String label) {
        this.property = property;
        this.label = label;
    }

    public PropertySequenceEntryConfig() {

    }

    public String getProperty() {
        return property;
    }

    @XmlAttribute(name = "property", required = true)
    public void setProperty(String property) {
        this.property = property;
    }

    public String getLabel() {
        return label;
    }

    @XmlAttribute(name = "label", required = true)
    public void setLabel(String label) {
        this.label = label;
    }

}
