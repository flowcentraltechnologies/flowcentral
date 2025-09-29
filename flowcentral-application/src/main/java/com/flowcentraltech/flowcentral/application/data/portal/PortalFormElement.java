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
package com.flowcentraltech.flowcentral.application.data.portal;

/**
 * Portal form element object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PortalFormElement {

    private String type;

    private String color;

    private String label;

    private String name;

    private String editor;

    private String contentType;

    private String columnType;

    private int fieldColumn;

    private boolean required;

    public PortalFormElement(String type, String color, String label, String name, String editor, String contentType,
            String columnType, int fieldColumn, boolean required) {
        this.type = type;
        this.color = color;
        this.label = label;
        this.name = name;
        this.editor = editor;
        this.contentType = contentType;
        this.columnType = columnType;
        this.fieldColumn = fieldColumn;
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getEditor() {
        return editor;
    }

    public String getContentType() {
        return contentType;
    }

    public String getColumnType() {
        return columnType;
    }

    public int getFieldColumn() {
        return fieldColumn;
    }

    public boolean isRequired() {
        return required;
    }
}
