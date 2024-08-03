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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.HighlightTypeXmlAdapter;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.util.xml.adapter.RequirementTypeXmlAdapter;

/**
 * Workflow user action configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
public class WfUserActionConfig extends BaseNameConfig {

    @JacksonXmlProperty(isAttribute = true, localName = "nextStep")
    private String nextStepName;

    @JacksonXmlProperty(isAttribute = true, localName = "setValues")
    private String setValuesName;

    @JacksonXmlProperty(isAttribute = true, localName = "appletSetValues")
    private String appletSetValuesName;

    @JacksonXmlProperty(isAttribute = true)
    private String showOnCondition;

    @JsonSerialize(using = RequirementTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = RequirementTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "commentRequirement")
    private RequirementType commentRequirement;

    @JsonSerialize(using = HighlightTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = HighlightTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "highlight")
    private HighlightType highlightType;

    @JacksonXmlProperty(isAttribute = true)
    private int orderIndex;

    @JacksonXmlProperty(isAttribute = true)
    private boolean formReview;

    @JacksonXmlProperty(isAttribute = true, localName = "validatePage")
    private boolean validatePage;

    @JacksonXmlProperty(isAttribute = true)
    private boolean forwarderPreferred;

    public WfUserActionConfig() {
        this.commentRequirement = RequirementType.NONE;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public void setNextStepName(String nextStepName) {
        this.nextStepName = nextStepName;
    }

    public String getSetValuesName() {
        return setValuesName;
    }

    public void setSetValuesName(String setValuesName) {
        this.setValuesName = setValuesName;
    }

    public String getAppletSetValuesName() {
        return appletSetValuesName;
    }

    public void setAppletSetValuesName(String appletSetValuesName) {
        this.appletSetValuesName = appletSetValuesName;
    }

    public String getShowOnCondition() {
        return showOnCondition;
    }

    public void setShowOnCondition(String showOnCondition) {
        this.showOnCondition = showOnCondition;
    }

    public RequirementType getCommentRequirement() {
        return commentRequirement;
    }

    public void setCommentRequirement(RequirementType commentRequirement) {
        this.commentRequirement = commentRequirement;
    }

    public HighlightType getHighlightType() {
        return highlightType;
    }

    public void setHighlightType(HighlightType highlightType) {
        this.highlightType = highlightType;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean isFormReview() {
        return formReview;
    }

    public void setFormReview(boolean formReview) {
        this.formReview = formReview;
    }

    public boolean isValidatePage() {
        return validatePage;
    }

    public void setValidatePage(boolean validatePage) {
        this.validatePage = validatePage;
    }

    public boolean isForwarderPreferred() {
        return forwarderPreferred;
    }

    public void setForwarderPreferred(boolean forwarderPreferred) {
        this.forwarderPreferred = forwarderPreferred;
    }
}
