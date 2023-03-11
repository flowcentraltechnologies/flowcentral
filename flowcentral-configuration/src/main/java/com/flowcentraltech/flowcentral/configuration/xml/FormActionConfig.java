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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.flowcentraltech.flowcentral.configuration.constants.UIActionType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.HighlightTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.UIActionTypeXmlAdapter;

/**
 * Form action configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormActionConfig extends BaseNameConfig {
    
    private FilterConfig onCondition;

    private UIActionType type;
    
    private HighlightType highlightType;

    private String policy;

    private String symbol;

    private String styleClass;

    private int orderIndex;

    private boolean showOnCreate;

    private boolean showOnMaintain;

    private boolean validateForm;

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    @XmlElement(required = false)
    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
    }

    public UIActionType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(UIActionTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(UIActionType type) {
        this.type = type;
    }

    public HighlightType getHighlightType() {
        return highlightType;
    }

    @XmlJavaTypeAdapter(HighlightTypeXmlAdapter.class)
    @XmlAttribute(name = "highlight")
    public void setHighlightType(HighlightType highlightType) {
        this.highlightType = highlightType;
    }

    public String getPolicy() {
        return policy;
    }

    @XmlAttribute(required = true)
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSymbol() {
        return symbol;
    }

    @XmlAttribute
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStyleClass() {
        return styleClass;
    }

    @XmlAttribute
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    @XmlAttribute
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean isShowOnCreate() {
        return showOnCreate;
    }

    @XmlAttribute
    public void setShowOnCreate(boolean showOnCreate) {
        this.showOnCreate = showOnCreate;
    }

    public boolean isShowOnMaintain() {
        return showOnMaintain;
    }

    @XmlAttribute
    public void setShowOnMaintain(boolean showOnMaintain) {
        this.showOnMaintain = showOnMaintain;
    }

    public boolean isValidateForm() {
        return validateForm;
    }

    @XmlAttribute
    public void setValidateForm(boolean validateForm) {
        this.validateForm = validateForm;
    }
}
