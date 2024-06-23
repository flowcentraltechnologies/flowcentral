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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.util.xml.adapter.FilterConditionTypeXmlAdapter;

/**
 * Filter restriction configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FilterRestrictionConfig extends BaseConfig {

    private FilterConditionType type;

    private String field;

    private String paramA;

    private String paramB;

    private List<FilterRestrictionConfig> restrictionList;

    public FilterConditionType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(FilterConditionTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(FilterConditionType type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    @XmlAttribute(required = true)
    public void setField(String field) {
        this.field = field;
    }

    public String getParamA() {
        return paramA;
    }

    @XmlAttribute(required = true)
    public void setParamA(String paramA) {
        this.paramA = paramA;
    }

    public String getParamB() {
        return paramB;
    }

    @XmlAttribute(required = true)
    public void setParamB(String paramB) {
        this.paramB = paramB;
    }

    public List<FilterRestrictionConfig> getRestrictionList() {
        return restrictionList;
    }

    @XmlElement(name = "restriction")
    public void setRestrictionList(List<FilterRestrictionConfig> restrictionList) {
        this.restrictionList = restrictionList;
    }

}
