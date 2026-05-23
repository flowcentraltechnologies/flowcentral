/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.common.data;

import java.util.List;

/**
 * Form validation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class FormValidation {
    
    private List<FormError> formErrors;
    
    private List<FieldError> fieldErrors;

    public FormValidation(List<FormError> formErrors, List<FieldError> fieldErrors) {
        this.formErrors = formErrors;
        this.fieldErrors = fieldErrors;
    }

    public List<FormError> getFormErrors() {
        return formErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

}
