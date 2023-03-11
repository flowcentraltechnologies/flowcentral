/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Form validation errors.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormValidationErrors {

    private Map<String, String> invalidFields;

    private List<FormMessage> validationErrors;

    private int hiddenErrors;

    private boolean hidden;

    public void addValidationError(String message) {
        addValidationError(new FormMessage(MessageType.ERROR, message));
    }

    public void addLocalValidationError(String message) {
        addValidationError(new FormMessage(MessageType.ERROR, message, true));
    }

    public void addValidationError(FormMessage message) {
        if (validationErrors == null) {
            validationErrors = new ArrayList<FormMessage>();
        }

        validationErrors.add(message);
    }

    public void addValidationError(String fieldName, String message) {
        if (invalidFields == null) {
            invalidFields = new HashMap<String, String>();
        }

        invalidFields.put(fieldName, message);
    }

    public void merge(List<FormValidationErrors> errors) {
        if (errors != null) {
            for (FormValidationErrors error : errors) {
                merge(error);
            }
        }
    }

    public FormValidationErrors hide() {
        FormValidationErrors errors = new FormValidationErrors();
        errors.invalidFields = invalidFields;
        errors.validationErrors = validationErrors;
        errors.hidden = true;
        return errors;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void merge(FormValidationErrors errors) {
        if (errors.isHidden()) {
            if (errors.invalidFields != null || errors.validationErrors != null) {
                hiddenErrors++;
            }
            return;
        }

        hiddenErrors += errors.hiddenErrors;
        
        if (errors.invalidFields != null) {
            if (invalidFields == null) {
                invalidFields = new HashMap<String, String>();
            }

            invalidFields.putAll(errors.invalidFields);
        }

        if (errors.validationErrors != null) {
            if (validationErrors == null) {
                validationErrors = new ArrayList<FormMessage>();
            }

            for (FormMessage formMessage : errors.validationErrors) {
                if (!formMessage.isLocal()) {
                    validationErrors.add(formMessage);
                }
            }
        }
    }

    public void clearValidationErrors() {
        hiddenErrors = 0;
        invalidFields = null;
        validationErrors = null;
    }

    public boolean isWithFormErrors() {
        return isWithHiddenErrors() || isWithFieldErrors() || isWithValidationErrors();
    }

    public boolean isWithHiddenErrors() {
        return hiddenErrors > 0;
    }

    public boolean isWithFieldErrors() {
        return !DataUtils.isBlank(invalidFields);
    }

    public boolean isWithFieldError(String fieldName) {
        return invalidFields != null && invalidFields.containsKey(fieldName);
    }

    public boolean isWithFieldError(Collection<String> fieldNames) {
        if (invalidFields != null) {
            for (String fieldName : fieldNames) {
                if (invalidFields.containsKey(fieldName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getFieldError(String fieldName) {
        return invalidFields.get(fieldName);
    }

    public boolean isWithValidationErrors() {
        return !DataUtils.isBlank(validationErrors);
    }

    public Map<String, String> getInvalidFields() {
        return invalidFields;
    }

    public int getHiddenErrors() {
        return hiddenErrors;
    }

    public List<FormMessage> getValidationErrors() {
        return validationErrors;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}
