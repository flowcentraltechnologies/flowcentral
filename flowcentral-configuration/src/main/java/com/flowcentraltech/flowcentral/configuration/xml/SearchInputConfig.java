/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.util.xml.adapter.FilterConditionTypeXmlAdapter;

/**
 * Search input configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SearchInputConfig {

    private FilterConditionType type;

    private String field;

    private String widget;

    private String label;

    public FilterConditionType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(FilterConditionTypeXmlAdapter.class)
    @XmlAttribute
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

    public String getWidget() {
        return widget;
    }

    @XmlAttribute(required = true)
    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getLabel() {
        return label;
    }

    @XmlAttribute(required = true)
    public void setLabel(String label) {
        this.label = label;
    }

}
