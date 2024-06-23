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
 * Property list property configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PropertyListPropConfig extends BaseConfig {

    private String name;

    private String description;

    private String references;

    private String inputWidget;

    private String defaultVal;

    private boolean required;

    private boolean mask;

    private boolean encrypt;

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

    public String getReferences() {
        return references;
    }

    @XmlAttribute
    public void setReferences(String references) {
        this.references = references;
    }

    public String getInputWidget() {
        return inputWidget;
    }

    @XmlAttribute(required = true)
    public void setInputWidget(String inputWidget) {
        this.inputWidget = inputWidget;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    @XmlAttribute
    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public boolean isRequired() {
        return required;
    }

    @XmlAttribute
    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isMask() {
        return mask;
    }

    @XmlAttribute
    public void setMask(boolean mask) {
        this.mask = mask;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    @XmlAttribute
    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

}
