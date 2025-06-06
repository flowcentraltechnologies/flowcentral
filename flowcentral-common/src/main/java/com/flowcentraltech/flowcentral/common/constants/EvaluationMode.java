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


package com.flowcentraltech.flowcentral.common.constants;

/**
 * Evaluation mode.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum EvaluationMode {

    CREATE(true, true),
    CREATE_SUBMIT(true, true),
    UPDATE(true, true),
    UPDATE_SUBMIT(true, true),
    UPDATE_TABACTION(true, false),
    DELETE(false, false),
    REQUIRED(true, true),
    SWITCH_ONCHANGE(true, false),
    NOP(false, false);

    private final boolean evaluation;

    private final boolean review;
    
    private EvaluationMode(boolean evaluation, boolean review) {
        this.evaluation = evaluation;
        this.review = review;
    }
    
    public static EvaluationMode getCreateMode(boolean create) {
        if (create) {
            return CREATE;
        }

        return NOP;
    }

    public static EvaluationMode getUpdateMode(boolean update) {
        if (update) {
            return UPDATE;
        }

        return NOP;
    }

    public static EvaluationMode getCreateSubmitMode(boolean create) {
        if (create) {
            return CREATE_SUBMIT;
        }

        return NOP;
    }

    public static EvaluationMode getUpdateSubmitMode(boolean update) {
        if (update) {
            return UPDATE_SUBMIT;
        }

        return NOP;
    }

    public static EvaluationMode getRequiredMode(boolean required) {
        if (required) {
            return REQUIRED;
        }

        return NOP;
    }
    
    public boolean isCreate() {
        return this.equals(CREATE);
    }
    
    public boolean isCreateSubmit() {
        return this.equals(CREATE_SUBMIT);
    }
    
    public boolean isUpdate() {
        return this.equals(UPDATE);
    }
    
    public boolean isUpdateSubmit() {
        return this.equals(UPDATE_SUBMIT);
    }
    
    public boolean isUpdateTabAction() {
        return this.equals(UPDATE_TABACTION);
    }
    
    public boolean isDelete() {
        return this.equals(DELETE);
    }
    
    public boolean isRequired() {
        return this.equals(REQUIRED);
    }
    
    public boolean isSwitchOnChange() {
        return this.equals(SWITCH_ONCHANGE);
    }
    
    public boolean isNop() {
        return this.equals(NOP);
    }
    
    public boolean isOfSubmit() {
        return this.equals(CREATE_SUBMIT) || this.equals(UPDATE_SUBMIT);
    }
    
    public boolean isOfCreate() {
        return this.equals(CREATE) || this.equals(CREATE_SUBMIT);
    }
    
    public boolean isOfUpdate() {
        return this.equals(UPDATE) || this.equals(UPDATE_SUBMIT);
    }
    
    public boolean isEvaluation() {
        return evaluation;
    }
    
    public boolean isReview() {
        return review;
    }
}
