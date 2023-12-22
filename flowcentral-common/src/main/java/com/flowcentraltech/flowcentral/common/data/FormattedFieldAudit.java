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

/**
 * Formatted field audit information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormattedFieldAudit {

    private String fieldName;

    private String[] oldValue;

    private String[] newValue;

    public FormattedFieldAudit(String fieldName, String[] oldValue, String[] newValue) {
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public FormattedFieldAudit() {

    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String[] getOldValue() {
        return oldValue;
    }

    public void setOldValue(String[] oldValue) {
        this.oldValue = oldValue;
    }

    public String[] getNewValue() {
        return newValue;
    }

    public void setNewValue(String[] newValue) {
        this.newValue = newValue;
    }

}
