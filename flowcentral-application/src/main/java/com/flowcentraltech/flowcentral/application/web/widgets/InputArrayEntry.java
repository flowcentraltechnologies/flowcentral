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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.common.input.AbstractInput;

/**
 * Input array entry.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class InputArrayEntry {

    private Object key;

    private String label;

    private AbstractInput<?> valueInput;

    private boolean editable;

    private boolean selected;

    public InputArrayEntry(Object key, String label, AbstractInput<?> valueInput, boolean editable, boolean selected) {
        this.key = key;
        this.label = label;
        this.valueInput = valueInput;
        this.editable = editable;
        this.selected = selected;
    }

    public AbstractInput<?> getValueInput() {
        return valueInput;
    }

    public Object getKey() {
        return key;
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

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
