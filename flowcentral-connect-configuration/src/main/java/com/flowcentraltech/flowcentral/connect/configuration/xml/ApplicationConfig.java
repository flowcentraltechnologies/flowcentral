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
 * Application configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "application")
public class ApplicationConfig {

    private String name;

    private String description;

    private String dataSourceAlias;

    private String redirect;

    private String entityManagerFactory;

    private EntitiesConfig entitiesConfig;

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

    public String getDataSourceAlias() {
        return dataSourceAlias;
    }

    @XmlAttribute(required = true)
    public void setDataSourceAlias(String dataSourceAlias) {
        this.dataSourceAlias = dataSourceAlias;
    }

    public String getRedirect() {
        return redirect;
    }

    @XmlAttribute
    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @XmlAttribute
    public void setEntityManagerFactory(String entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntitiesConfig getEntitiesConfig() {
        return entitiesConfig;
    }

    @XmlElement(name = "entities")
    public void setEntitiesConfig(EntitiesConfig entitiesConfig) {
        this.entitiesConfig = entitiesConfig;
    }
}
