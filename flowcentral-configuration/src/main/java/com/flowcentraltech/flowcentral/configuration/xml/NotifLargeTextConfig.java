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

package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.util.xml.adapter.CDataXmlAdapter;

/**
 * Notification large text configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "notifLargeText")
public class NotifLargeTextConfig extends BaseNameConfig {

    private String entity;

    private String body;
    
    private Integer fontSizeInPixels;

    private List<NotifLargeTextParamConfig> paramList;

    public String getEntity() {
        return entity;
    }

    @XmlAttribute(required = true)
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getFontSizeInPixels() {
        return fontSizeInPixels;
    }

    @XmlAttribute
    public void setFontSizeInPixels(Integer fontSizeInPixels) {
        this.fontSizeInPixels = fontSizeInPixels;
    }

    public String getBody() {
        return body;
    }

    @XmlJavaTypeAdapter(CDataXmlAdapter.class)
    @XmlElement(required = true)
    public void setBody(String body) {
        this.body = body;
    }

    public List<NotifLargeTextParamConfig> getParamList() {
        return paramList;
    }

    @XmlElement(name = "parameter", required = true)
    public void setParamList(List<NotifLargeTextParamConfig> paramList) {
        this.paramList = paramList;
    }

}
