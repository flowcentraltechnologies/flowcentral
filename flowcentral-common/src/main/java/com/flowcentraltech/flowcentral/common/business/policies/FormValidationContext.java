/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Form validation context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormValidationContext {

    private EvaluationMode evaluationMode;

    private String action;

    public FormValidationContext(EvaluationMode evaluationMode, String action) {
        this.evaluationMode = evaluationMode;
        this.action = action;
    }

    public FormValidationContext(EvaluationMode evaluationMode) {
        this.evaluationMode = evaluationMode;
    }

    public EvaluationMode getEvaluationMode() {
        return evaluationMode;
    }

    public boolean isCreate() {
        return evaluationMode.isCreate();
    }

    public boolean isCreateSubmit() {
        return evaluationMode.isCreateSubmit();
    }

    public boolean isUpdate() {
        return evaluationMode.isUpdate();
    }

    public boolean isUpdateSubmit() {
        return evaluationMode.isUpdateSubmit();
    }

    public boolean isUpdateTabAction() {
        return evaluationMode.isUpdateTabAction();
    }

    public boolean isDelete() {
        return evaluationMode.isDelete();
    }

    public boolean isRequired() {
        return evaluationMode.isRequired();
    }

    public boolean isSwitchOnChange() {
        return evaluationMode.isSwitchOnChange();
    }

    public boolean isNop() {
        return evaluationMode.isNop();
    }

    public boolean isOfSubmit() {
        return evaluationMode.isOfSubmit();
    }

    public boolean isOfCreate() {
        return evaluationMode.isOfCreate();
    }

    public boolean isOfUpdate() {
        return evaluationMode.isOfUpdate();
    }

    public String getAction() {
        return action;
    }

    public boolean isWithAction() {
        return !StringUtils.isBlank(action);
    }
    
    public boolean isEvaluation() {
        return evaluationMode.isEvaluation();
    }
    
    public boolean isReview() {
        return evaluationMode.isReview();
    }
}
