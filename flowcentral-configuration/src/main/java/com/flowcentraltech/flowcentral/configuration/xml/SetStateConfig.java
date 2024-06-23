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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormElementTypeXmlAdapter;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.util.xml.adapter.TriStateXmlAdapter;

/**
 * Set state configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SetStateConfig extends BaseConfig {

    private FormElementType type;

    private String target;

    private TriState required;

    private TriState visible;

    private TriState editable;

    private TriState disabled;

    public SetStateConfig() {
        required = TriState.CONFORMING;
        visible = TriState.CONFORMING;
        editable = TriState.CONFORMING;
        disabled = TriState.CONFORMING;
    }

    public FormElementType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(FormElementTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(FormElementType type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    @XmlAttribute(name = "target", required = true)
    public void setTarget(String target) {
        this.target = target;
    }

    public TriState getRequired() {
        return required;
    }

    @XmlJavaTypeAdapter(TriStateXmlAdapter.class)
    @XmlAttribute
    public void setRequired(TriState required) {
        this.required = required;
    }

    public TriState getVisible() {
        return visible;
    }

    @XmlJavaTypeAdapter(TriStateXmlAdapter.class)
    @XmlAttribute
    public void setVisible(TriState visible) {
        this.visible = visible;
    }

    public TriState getEditable() {
        return editable;
    }

    @XmlJavaTypeAdapter(TriStateXmlAdapter.class)
    @XmlAttribute
    public void setEditable(TriState editable) {
        this.editable = editable;
    }

    public TriState getDisabled() {
        return disabled;
    }

    @XmlJavaTypeAdapter(TriStateXmlAdapter.class)
    @XmlAttribute
    public void setDisabled(TriState disabled) {
        this.disabled = disabled;
    }

}
