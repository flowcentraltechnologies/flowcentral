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
package com.flowcentraltech.flowcentral.application.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Form review policy definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormReviewPolicyDef extends FormValidationPolicyDef {

    private Set<FormReviewType> reviewTypes;

    private MessageType messageType;

    public FormReviewPolicyDef(List<FormReviewType> reviewTypeList, FilterDef errorCondition, String name,
            String description, MessageType messageType, String message, String errorMatcher, List<String> targetList) {
        super(errorCondition, name, description, message, errorMatcher, targetList);
        this.messageType = messageType;
        if (!DataUtils.isBlank(reviewTypeList)) {
            this.reviewTypes = Collections.unmodifiableSet(new HashSet<FormReviewType>(reviewTypeList));
        } else {
            this.reviewTypes = Collections.emptySet();
        }
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public boolean supports(FormReviewType type) {
        return reviewTypes.contains(type);
    }

    @Override
    public String toString() {
        return "FormReviewPolicyDef [reviewTypes=" + reviewTypes + ", messageType=" + messageType + ", getName()="
                + getName() + ", getDescription()=" + getDescription() + ", getMessage()=" + getMessage()
                + ", getErrorMatcher()=" + getErrorMatcher() + ", getErrorCondition()=" + getErrorCondition()
                + ", isErrorMatcher()=" + isErrorMatcher() + ", isErrorCondition()=" + isErrorCondition()
                + ", getTarget()=" + getTarget() + ", isWithTarget()=" + isWithTarget() + ", getClass()=" + getClass()
                + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
}
