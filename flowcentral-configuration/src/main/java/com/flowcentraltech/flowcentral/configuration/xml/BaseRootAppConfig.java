/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Convenient abstract base class for root application configurations.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseRootAppConfig extends BaseXmlConfig {

    @JacksonXmlProperty(isAttribute = true)
    private final String xmlns;

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsi")
    private final String xmlnsXsi;

    @JacksonXmlProperty(isAttribute = true, localName = "xsi:schemaLocation")
    private final String xsiSchemaLocation;

    private String schemaLocation;

    protected BaseRootAppConfig(String xsd) {
        this.xmlns = "http://flowcentraltech.com/schema-common";
        this.xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";
        this.xsiSchemaLocation = this.xmlns + " https://schema.flowcentralplatform.com/xsd/" + xsd;
    }

    protected BaseRootAppConfig() {
        this.xmlns = null;
        this.xmlnsXsi = null;
        this.xsiSchemaLocation = null;
    }

    public final String getXmlns() {
        return xmlns;
    }

    public final String getXmlnsXsi() {
        return xmlnsXsi;
    }

    public final String getXsiSchemaLocation() {
        return xsiSchemaLocation;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    @Override
    public boolean includeXmlDeclaration() {
        return true;
    }

}
