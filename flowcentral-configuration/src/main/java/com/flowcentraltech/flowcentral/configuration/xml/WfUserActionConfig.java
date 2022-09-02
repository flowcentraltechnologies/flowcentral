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

    private String nextStepName;
    
    private String setValuesName;

    private RequirementType commentRequirement;

    private HighlightType highlightType;

    private int orderIndex;

    private boolean formReview;
    
    private boolean validatePage;
    
    private boolean forwarderPreferred;
    
    public WfUserActionConfig() {
        this.commentRequirement = RequirementType.NONE;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    @XmlAttribute(name = "nextStep", required = true)
    public void setNextStepName(String nextStepName) {
        this.nextStepName = nextStepName;
    }

    public String getSetValuesName() {
        return setValuesName;
    }

    @XmlAttribute(name = "setValues")
    public void setSetValuesName(String setValuesName) {
        this.setValuesName = setValuesName;
    }

    public RequirementType getCommentRequirement() {
        return commentRequirement;
    }

    @XmlJavaTypeAdapter(RequirementTypeXmlAdapter.class)
    @XmlAttribute(name = "commentRequirement")
    public void setCommentRequirement(RequirementType commentRequirement) {
        this.commentRequirement = commentRequirement;
    }

    public HighlightType getHighlightType() {
        return highlightType;
    }

    @XmlJavaTypeAdapter(HighlightTypeXmlAdapter.class)
    @XmlAttribute(name = "highlight")
    public void setHighlightType(HighlightType highlightType) {
        this.highlightType = highlightType;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    @XmlAttribute
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean isFormReview() {
        return formReview;
    }

    @XmlAttribute
    public void setFormReview(boolean formReview) {
        this.formReview = formReview;
    }

    public boolean isValidatePage() {
        return validatePage;
    }

    @XmlAttribute(name = "validatePage")
    public void setValidatePage(boolean validatePage) {
        this.validatePage = validatePage;
    }

    public boolean isForwarderPreferred() {
        return forwarderPreferred;
    }

    @XmlAttribute
    public void setForwarderPreferred(boolean forwarderPreferred) {
        this.forwarderPreferred = forwarderPreferred;
    }
}
