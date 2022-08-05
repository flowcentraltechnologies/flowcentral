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

package com.flowcentraltech.flowcentral.application.web.widgets;

/**
 * Input array value;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class InputArrayValue {

    private Object key;
    
    private Object value;
    
    private boolean selected;

    public InputArrayValue(Object key, Object value, boolean selected) {
        this.key = key;
        this.value = value;
        this.selected = selected;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public String toString() {
        return "InputArrayValue [key=" + key + ", value=" + value + ", selected=" + selected + "]";
    }
}
