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
 * Property item class.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PropertyListItem {

    private String name;

    private String description;

    private String value;

    private String displayValue;

    private String editor;

    private boolean disabled;
    
    public PropertyListItem(String name, String description, String value, String displayValue, String editor,
            boolean disabled) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.displayValue = displayValue;
        this.editor = editor;
        this.disabled = disabled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getEditor() {
        return editor;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
