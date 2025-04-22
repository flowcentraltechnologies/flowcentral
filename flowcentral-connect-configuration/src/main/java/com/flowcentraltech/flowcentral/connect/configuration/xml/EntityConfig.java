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

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.connect.configuration.xml.adapter.EntityBaseTypeXmlAdapter;
import com.tcdng.unify.common.constants.ConnectEntityBaseType;

/**
 * Entity configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityConfig {

    @JsonSerialize(using = EntityBaseTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = EntityBaseTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private ConnectEntityBaseType base;
    
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private String table;

    @JacksonXmlProperty(isAttribute = true, localName = "impl")
    private String implementation;

    @JacksonXmlProperty(isAttribute = true, localName = "id-field")
    private String idFieldName;

    @JacksonXmlProperty(isAttribute = true, localName = "version-field")
    private String versionNoFieldName;

    @JacksonXmlProperty(isAttribute = true, localName = "handler")
    private String handler;

    @JacksonXmlProperty(isAttribute = true, localName = "action-policy")
    private String actionPolicy;

    @JacksonXmlProperty(isAttribute = true, localName = "ignore-on-sync")
    private boolean ignoreOnSync;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "field")
    private List<EntityFieldConfig> entityFieldList;

    public ConnectEntityBaseType getBase() {
        return base;
    }

    public void setBase(ConnectEntityBaseType base) {
        this.base = base;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getVersionNoFieldName() {
        return versionNoFieldName;
    }

    public void setVersionNoFieldName(String versionNoFieldName) {
        this.versionNoFieldName = versionNoFieldName;
    }

    public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getActionPolicy() {
        return actionPolicy;
    }

    public void setActionPolicy(String actionPolicy) {
        this.actionPolicy = actionPolicy;
    }

    public boolean isIgnoreOnSync() {
        return ignoreOnSync;
    }

    public void setIgnoreOnSync(boolean ignoreOnSync) {
        this.ignoreOnSync = ignoreOnSync;
    }

    public List<EntityFieldConfig> getEntityFieldList() {
        return entityFieldList;
    }

    public void setEntityFieldList(List<EntityFieldConfig> entityFieldList) {
        this.entityFieldList = entityFieldList;
    }

}
