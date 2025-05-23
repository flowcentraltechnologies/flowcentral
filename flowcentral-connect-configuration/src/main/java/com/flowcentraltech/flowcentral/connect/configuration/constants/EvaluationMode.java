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


package com.flowcentraltech.flowcentral.connect.configuration.constants;

/**
 * Interconnect evaluation mode.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum EvaluationMode {

    CREATE,
    CREATE_SUBMIT,
    UPDATE,
    UPDATE_SUBMIT,
    UPDATE_TABACTION,
    DELETE,
    REQUIRED,
    SWITCH_ONCHANGE,
    NOP;

    public boolean submit() {
        return this.equals(CREATE_SUBMIT) || this.equals(UPDATE_SUBMIT);
    }
    
    public boolean create() {
        return this.equals(CREATE) || this.equals(CREATE_SUBMIT);
    }
    
    public boolean update() {
        return this.equals(UPDATE) || this.equals(UPDATE_SUBMIT) || this.equals(UPDATE_TABACTION);
    }
    
    public boolean delete() {
        return this.equals(DELETE);
    }

}
