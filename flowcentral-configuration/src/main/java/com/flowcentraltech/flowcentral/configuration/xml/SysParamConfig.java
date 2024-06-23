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

import com.flowcentraltech.flowcentral.configuration.constants.SysParamType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.SysParamTypeXmlAdapter;

/**
 * System parameter configuration
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SysParamConfig extends BaseCodeConfig {

    private SysParamType type;

    private String editor;

    private String defaultVal;

    private boolean control;

    private boolean editable;

    public SysParamConfig() {
        type = SysParamType.STRING;
    }

    public SysParamType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(SysParamTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(SysParamType type) {
        this.type = type;
    }

    public String getEditor() {
        return editor;
    }

    @XmlAttribute
    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    @XmlAttribute
    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public boolean isControl() {
        return control;
    }

    @XmlAttribute
    public void setControl(boolean control) {
        this.control = control;
    }

    public boolean isEditable() {
        return editable;
    }

    @XmlAttribute
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
