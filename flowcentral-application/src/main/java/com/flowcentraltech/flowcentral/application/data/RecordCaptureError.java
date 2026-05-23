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
package com.flowcentraltech.flowcentral.application.data;

import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Record capture error.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class RecordCaptureError {
    
    private Map<String, String> errors;
    
    private Map<String, String> warning;

    public RecordCaptureError() {
        this.errors = null;
        this.warning = null;
    }

    public void addError(String fieldName, String err) {
        if (!StringUtils.isBlank(err)) {
            if (errors == null) {
                errors = new HashMap<String, String>();
            }
            
            errors.put(fieldName, err);
        }
    }

    public void addWarning(String fieldName, String warn) {
        if (!StringUtils.isBlank(warn)) {
            if (warning == null) {
                warning = new HashMap<String, String>();
            }
            
            warning.put(fieldName, warn);
        }
    }
    
    public boolean isWithError(String fieldName) {
        return errors != null && errors.containsKey(fieldName);
    }
    
    public String getError(String fieldName) {
        return errors != null ? errors.get(fieldName) : null;
    }
 
    public boolean isErrorPresent() {
        return errors != null && !errors.isEmpty();
    }
    
    public boolean isWithWarning(String fieldName) {
        return warning != null && warning.containsKey(fieldName);
    }
    
    public String getWarning(String fieldName) {
        return warning != null ? warning.get(fieldName) : null;
    }
 
    public boolean isWarningPresent() {
        return warning != null && !warning.isEmpty();
    }
}