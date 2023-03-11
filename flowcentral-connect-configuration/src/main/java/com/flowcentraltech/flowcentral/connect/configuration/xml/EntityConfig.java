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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Entity configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityConfig {

    private String name;

    private String description;

    private String implementation;

    private String idFieldName;

    private String versionNoFieldName;

    private String handler;

    private String actionPolicy;

    private List<EntityFieldConfig> entityFieldList;

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

    public String getImplementation() {
        return implementation;
    }

    @XmlAttribute(name = "impl", required = true)
    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    @XmlAttribute(name = "id-field")
    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getVersionNoFieldName() {
        return versionNoFieldName;
    }

    @XmlAttribute(name = "version-field")
    public void setVersionNoFieldName(String versionNoFieldName) {
        this.versionNoFieldName = versionNoFieldName;
    }

    public String getHandler() {
		return handler;
	}

    @XmlAttribute(name = "handler")
	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getActionPolicy() {
        return actionPolicy;
    }

    @XmlAttribute(name = "actionPolicy")
    public void setActionPolicy(String actionPolicy) {
        this.actionPolicy = actionPolicy;
    }

    public List<EntityFieldConfig> getEntityFieldList() {
        return entityFieldList;
    }

    @XmlElement(name = "field")
    public void setEntityFieldList(List<EntityFieldConfig> entityFieldList) {
        this.entityFieldList = entityFieldList;
    }

}
