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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.APIType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.APITypeXmlAdapter;

/**
 * API configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class APIConfig extends BaseNameConfig {

    @JsonSerialize(using = APITypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = APITypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private APIType type;

    @JacksonXmlProperty(isAttribute = true)
    private String entity;
    
    @JacksonXmlProperty(isAttribute = true)
    private String applet;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean supportCreate;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean supportRead;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean supportUpdate;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean supportDelete;
    
    public APIConfig() {
        this.supportCreate = Boolean.FALSE;
        this.supportRead = Boolean.FALSE;
        this.supportUpdate = Boolean.FALSE;
        this.supportDelete = Boolean.FALSE;
    }
    
    public APIType getType() {
        return type;
    }

    public void setType(APIType type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getApplet() {
        return applet;
    }

    public void setApplet(String applet) {
        this.applet = applet;
    }

    public Boolean getSupportCreate() {
        return supportCreate;
    }

    public void setSupportCreate(Boolean supportCreate) {
        this.supportCreate = supportCreate;
    }

    public Boolean getSupportRead() {
        return supportRead;
    }

    public void setSupportRead(Boolean supportRead) {
        this.supportRead = supportRead;
    }

    public Boolean getSupportUpdate() {
        return supportUpdate;
    }

    public void setSupportUpdate(Boolean supportUpdate) {
        this.supportUpdate = supportUpdate;
    }

    public Boolean getSupportDelete() {
        return supportDelete;
    }

    public void setSupportDelete(Boolean supportDelete) {
        this.supportDelete = supportDelete;
    }

}
