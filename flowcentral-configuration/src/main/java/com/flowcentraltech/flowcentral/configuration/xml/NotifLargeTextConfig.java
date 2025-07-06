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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.flowcentraltech.flowcentral.configuration.constants.FontFamilyType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FontFamilyTypeXmlAdapter;
import com.tcdng.unify.core.util.xml.adapter.CDataXmlAdapter;

/**
 * Notification large text configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "notifLargeText")
public class NotifLargeTextConfig extends BaseRootAppConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JsonSerialize(using = CDataXmlAdapter.Serializer.class)
    @JsonDeserialize(using = CDataXmlAdapter.Deserializer.class)
    @JacksonXmlProperty
    private String body;

    @JsonSerialize(using = FontFamilyTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = FontFamilyTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private FontFamilyType fontFamily;

    @JacksonXmlProperty(isAttribute = true)
    private Integer fontSizeInPixels;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "parameter")
    private List<NotifLargeTextParamConfig> paramList;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public FontFamilyType getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(FontFamilyType fontFamily) {
        this.fontFamily = fontFamily;
    }

    public Integer getFontSizeInPixels() {
        return fontSizeInPixels;
    }

    public void setFontSizeInPixels(Integer fontSizeInPixels) {
        this.fontSizeInPixels = fontSizeInPixels;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<NotifLargeTextParamConfig> getParamList() {
        return paramList;
    }

    public void setParamList(List<NotifLargeTextParamConfig> paramList) {
        this.paramList = paramList;
    }

}
