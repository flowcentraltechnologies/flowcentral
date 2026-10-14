/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

    @JacksonXmlProperty(isAttribute = true, namespace = "http://www.w3.org/2001/XMLSchema-instance",
            localName = "noNamespaceSchemaLocation")
    private final String noNamespaceSchemaLocation;

    protected BaseRootAppConfig(String xsd) {
        this.noNamespaceSchemaLocation = "https://schema.flowcentralplatform.com/xsd/4.2.0/" + xsd;
    }

    protected BaseRootAppConfig() {
        this.noNamespaceSchemaLocation = null;
    }

    public String getNoNamespaceSchemaLocation() {
        return noNamespaceSchemaLocation;
    }

    @Override
    public boolean includeXmlDeclaration() {
        return true;
    }

}
