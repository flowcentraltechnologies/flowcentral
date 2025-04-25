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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.flowcentraltech.flowcentral.configuration.constants.UIActionType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.HighlightTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.UIActionTypeXmlAdapter;

/**
 * Form action configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class FormActionConfig extends BaseNameConfig {
    
    @JacksonXmlProperty
    private FilterConfig onCondition;

    @JsonSerialize(using = UIActionTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = UIActionTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private UIActionType type;
    
    @JsonSerialize(using = HighlightTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = HighlightTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "highlight")
    private HighlightType highlightType;

    @JacksonXmlProperty(isAttribute = true)
    private String policy;

    @JacksonXmlProperty(isAttribute = true)
    private String symbol;

    @JacksonXmlProperty(isAttribute = true)
    private String styleClass;

    @JacksonXmlProperty(isAttribute = true)
    private int orderIndex;

    @JacksonXmlProperty(isAttribute = true)
    private boolean showOnCreate;

    @JacksonXmlProperty(isAttribute = true)
    private boolean showOnMaintain;

    @JacksonXmlProperty(isAttribute = true)
    private boolean validateForm;

    public FilterConfig getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(FilterConfig onCondition) {
        this.onCondition = onCondition;
    }

    public UIActionType getType() {
        return type;
    }

    public void setType(UIActionType type) {
        this.type = type;
    }

    public HighlightType getHighlightType() {
        return highlightType;
    }

    public void setHighlightType(HighlightType highlightType) {
        this.highlightType = highlightType;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean isShowOnCreate() {
        return showOnCreate;
    }

    public void setShowOnCreate(boolean showOnCreate) {
        this.showOnCreate = showOnCreate;
    }

    public boolean isShowOnMaintain() {
        return showOnMaintain;
    }

    public void setShowOnMaintain(boolean showOnMaintain) {
        this.showOnMaintain = showOnMaintain;
    }

    public boolean isValidateForm() {
        return validateForm;
    }

    public void setValidateForm(boolean validateForm) {
        this.validateForm = validateForm;
    }
}
