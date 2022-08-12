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


package com.flowcentraltech.flowcentral.common.constants;

/**
 * Evaluation mode.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum EvaluationMode {

    CREATE(true, true),
    CREATE_SUBMIT(true, true),
    UPDATE(true, true),
    UPDATE_SUBMIT(true, true),
    UPDATE_TABACTION(true, false),
    DELETE(false, false),
    REQUIRED(true, true),
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
    
    public boolean submit() {
        return this.equals(CREATE_SUBMIT) || this.equals(UPDATE_SUBMIT);
    }
    
    public boolean create() {
        return this.equals(CREATE) || this.equals(CREATE_SUBMIT);
    }
    
    public boolean update() {
        return this.equals(UPDATE) || this.equals(UPDATE_SUBMIT);
    }
    
    public boolean evaluation() {
        return evaluation;
    }
    
    public boolean review() {
        return review;
    }
}
