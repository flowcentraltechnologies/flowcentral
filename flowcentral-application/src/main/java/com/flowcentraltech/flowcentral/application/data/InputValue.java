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

/**
 * Input value;
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class InputValue {

    private Object key;

    private Object value;

    private String label;

    private boolean editable;

    private boolean selected;

    public InputValue(Object key, Object value, String label, boolean editable, boolean selected) {
        this.key = key;
        this.value = value;
        this.label = label;
        this.editable = editable;
        this.selected = selected;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isSelected() {
        return selected;
    }

}
