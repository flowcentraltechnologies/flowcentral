/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.configuration.constants.FormAnnotationType;
import com.flowcentraltech.flowcentral.configuration.constants.VisibilityType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormAnnotationTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.VisibilityTypeXmlAdapter;
import com.tcdng.unify.core.util.xml.adapter.CDataXmlAdapter;

/**
 * Form annotation configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class FormAnnotationConfig extends BaseConfig {

    @JsonSerialize(using = FormAnnotationTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = FormAnnotationTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private FormAnnotationType type;
    
    @JsonSerialize(using = VisibilityTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = VisibilityTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private VisibilityType visibility;
    
    @JacksonXmlProperty
    private FilterConfig onCondition;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JsonSerialize(using = CDataXmlAdapter.Serializer.class)
    @JsonDeserialize(using = CDataXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private String message;

    @JacksonXmlProperty(isAttribute = true)
    private boolean html;
    
    @JacksonXmlProperty(isAttribute = true)
    private boolean directPlacement;

    public FormAnnotationConfig() {
        type = FormAnnotationType.INFO;
    }

    public FormAnnotationType getType() {
        return type;
    }

    public void setType(FormAnnotationType type) {
        this.type = type;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public boolean isDirectPlacement() {
        return directPlacement;
    }

    public void setDirectPlacement(boolean directPlacement) {
        this.directPlacement = directPlacement;
    }

}
