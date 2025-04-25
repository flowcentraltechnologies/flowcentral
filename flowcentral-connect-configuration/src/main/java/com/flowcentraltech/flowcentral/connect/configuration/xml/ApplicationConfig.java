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
package com.flowcentraltech.flowcentral.connect.configuration.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Application configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JacksonXmlRootElement(localName = "application")
public class ApplicationConfig extends BaseRootConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private String dataSourceAlias;

    @JacksonXmlProperty(isAttribute = true)
    private String redirect;

    @JacksonXmlProperty(isAttribute = true)
    private String entityManagerFactory;

    @JacksonXmlProperty(localName = "entities")
    private EntitiesConfig entitiesConfig;

    public ApplicationConfig() {
        super("flowcentral-interconnect-application-4.0.0.xsd");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataSourceAlias() {
        return dataSourceAlias;
    }

    public void setDataSourceAlias(String dataSourceAlias) {
        this.dataSourceAlias = dataSourceAlias;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(String entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntitiesConfig getEntitiesConfig() {
        return entitiesConfig;
    }

    public void setEntitiesConfig(EntitiesConfig entitiesConfig) {
        this.entitiesConfig = entitiesConfig;
    }
}
