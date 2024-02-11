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

package com.flowcentraltech.flowcentral.workflow.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseNamedEntity;
import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.RequirementType;

/**
 * Workflow step user action entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_WORKSTEPUSERACTION", uniqueConstraints = { @UniqueConstraint({ "wfStepId", "name" }),
        @UniqueConstraint({ "wfStepId", "description" }), @UniqueConstraint({ "wfStepId", "label" }) })
public class WfStepUserAction extends BaseNamedEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @ForeignKey
    private RequirementType commentRequirement;

    @ForeignKey(nullable = true)
    private HighlightType highlightType;
    
    @Column(name = "USERACTION_LABEL", length = 96)
    private String label;

    @Column(name = "USERACTION_SYMBOL", length = 64, nullable = true)
    private String symbol;

    @Column(length = 64, nullable = true)
    private String styleClass;

    @Column(name = "NEXT_STEP_NM", length = 64, nullable = true)
    private String nextStepName;

    @Column(name = "SET_VALUES_NM", length = 64, nullable = true)
    private String setValuesName;

    @Column(name = "APPLET_SET_VALUES_NM", length = 64, nullable = true)
    private String appletSetValuesName;

    @Column(name = "SHOW_ON_CONDITION", length = 64, nullable = true)
    private String showOnCondition;

    @Column
    private int orderIndex;

    @Column
    private boolean formReview;

    @Column
    private boolean validatePage;

    @Column
    private boolean forwarderPreferred;
    
    @ListOnly(key = "wfStepId", property = "description")
    private String wfStepDesc;

    @ListOnly(key = "commentRequirement", property = "description")
    private String commentRequirementDesc;

    @ListOnly(key = "highlightType", property = "description")
    private String highlightTypeDesc;

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public RequirementType getCommentRequirement() {
        return commentRequirement;
    }

    public void setCommentRequirement(RequirementType commentRequirement) {
        this.commentRequirement = commentRequirement;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getWfStepDesc() {
        return wfStepDesc;
    }

    public void setWfStepDesc(String wfStepDesc) {
        this.wfStepDesc = wfStepDesc;
    }

    public String getCommentRequirementDesc() {
        return commentRequirementDesc;
    }

    public void setCommentRequirementDesc(String commentRequirementDesc) {
        this.commentRequirementDesc = commentRequirementDesc;
    }

    public HighlightType getHighlightType() {
        return highlightType;
    }

    public void setHighlightType(HighlightType highlightType) {
        this.highlightType = highlightType;
    }

    public String getHighlightTypeDesc() {
        return highlightTypeDesc;
    }

    public void setHighlightTypeDesc(String highlightTypeDesc) {
        this.highlightTypeDesc = highlightTypeDesc;
    }

}
