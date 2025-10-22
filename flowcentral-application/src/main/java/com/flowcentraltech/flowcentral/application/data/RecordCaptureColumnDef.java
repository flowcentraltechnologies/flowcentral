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
 * Record capture column definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class RecordCaptureColumnDef {

    private String fieldName;

    private String caption;

    private String editor;

    private String headerStyle;

    private int widthRatio;

    public RecordCaptureColumnDef(String fieldName, String caption, String editor, int widthRatio) {
        this.fieldName = fieldName;
        this.caption = caption;
        this.editor = editor;
        this.widthRatio = widthRatio;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getCaption() {
        return caption;
    }

    public String getEditor() {
        return editor;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

}