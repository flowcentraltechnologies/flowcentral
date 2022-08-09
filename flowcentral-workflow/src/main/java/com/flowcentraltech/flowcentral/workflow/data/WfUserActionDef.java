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

package com.flowcentraltech.flowcentral.workflow.data;

import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.tcdng.unify.core.constant.RequirementType;

/**
 * Workflow user action definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfUserActionDef {

    private RequirementType commentRequirement;

    private HighlightType highlightType;

    private String name;

    private String description;

    private String label;

    private String symbol;

    private String styleClass;

    private String nextStepName;

    private int orderIndex;

    private boolean formReview;

    private boolean validatePage;

    private boolean forwarderPreferred;

    public WfUserActionDef(RequirementType commentRequirement, HighlightType highlightType, String name,
            String description, String label, String symbol, String styleClass, String nextStepName, int orderIndex,
            boolean formReview, boolean validatePage, boolean forwarderPreferred) {
        this.commentRequirement = commentRequirement;
        this.highlightType = highlightType;
        this.name = name;
        this.description = description;
        this.label = label;
        this.symbol = symbol;
        this.styleClass = styleClass;
        this.nextStepName = nextStepName;
        this.orderIndex = orderIndex;
        this.formReview = formReview;
        this.validatePage = validatePage;
        this.forwarderPreferred = forwarderPreferred;
    }

    public RequirementType getCommentRequirement() {
        return commentRequirement;
    }

    public HighlightType getHighlightType() {
        return highlightType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public boolean isFormReview() {
        return formReview;
    }

    public boolean isValidatePage() {
        return validatePage;
    }

    public boolean isForwarderPreferred() {
        return forwarderPreferred;
    }

}
