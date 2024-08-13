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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.SearchConditionTypeXmlAdapter;

/**
 * Search input configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class SearchInputConfig extends BaseConfig {

    @JsonSerialize(using = SearchConditionTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = SearchConditionTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private SearchConditionType type;

    @JacksonXmlProperty(isAttribute = true)
    private String field;

    @JacksonXmlProperty(isAttribute = true)
    private String widget;

    @JacksonXmlProperty(isAttribute = true)
    private String label;

    public SearchConditionType getType() {
        return type;
    }

    public void setType(SearchConditionType type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
