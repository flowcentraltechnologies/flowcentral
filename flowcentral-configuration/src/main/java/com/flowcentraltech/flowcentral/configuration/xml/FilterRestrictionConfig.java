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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.util.xml.adapter.FilterConditionTypeXmlAdapter;

/**
 * Filter restriction configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class FilterRestrictionConfig extends BaseConfig {

    @JsonSerialize(using = FilterConditionTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = FilterConditionTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private FilterConditionType type;

    @JacksonXmlProperty(isAttribute = true)
    private String field;

    @JacksonXmlProperty(isAttribute = true)
    private String paramA;

    @JacksonXmlProperty(isAttribute = true)
    private String paramB;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "restriction")
    private List<FilterRestrictionConfig> restrictionList;

    public FilterConditionType getType() {
        return type;
    }

    public void setType(FilterConditionType type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getParamA() {
        return paramA;
    }

    public void setParamA(String paramA) {
        this.paramA = paramA;
    }

    public String getParamB() {
        return paramB;
    }

    public void setParamB(String paramB) {
        this.paramB = paramB;
    }

    public List<FilterRestrictionConfig> getRestrictionList() {
        return restrictionList;
    }

    public void setRestrictionList(List<FilterRestrictionConfig> restrictionList) {
        this.restrictionList = restrictionList;
    }

}
