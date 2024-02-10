/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;

/**
 * Form section definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormSectionDef {

    private List<FormFieldDef> formFieldDefList;

    private String name;

    private String label;

    private FormColumnsType columns;

    private boolean visible;

    private boolean editable;

    private boolean disabled;

    public FormSectionDef(List<FormFieldDef> formFieldDefList, String name, String label, FormColumnsType columns, boolean visible,
            boolean editable, boolean disabled) {
        this.formFieldDefList = formFieldDefList;
        this.name = name;
        this.label = label;
        this.columns = columns;
        this.visible = visible;
        this.editable = editable;
        this.disabled = disabled;
    }

    public FormSectionDef(FormSectionDef srcFormSectionDef, List<FormFieldDef> formFieldDefList, FormColumnsType columns) {
        this.formFieldDefList = formFieldDefList;
        this.columns = columns;
        this.name = srcFormSectionDef.name;
        this.label = srcFormSectionDef.label;
        this.visible = srcFormSectionDef.visible;
        this.editable = srcFormSectionDef.editable;
        this.disabled = srcFormSectionDef.disabled;
    }

    public List<FormFieldDef> getFormFieldDefList() {
        return formFieldDefList;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public FormColumnsType getColumns() {
        return columns;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isDisabled() {
        return disabled;
    }

}
