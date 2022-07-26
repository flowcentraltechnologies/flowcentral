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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Form tab definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormTabDef {

    private TabContentType contentType;

    private String name;

    private String label;

    private String applet;

    private String filter;

    private String reference;

    private String editAction;

    private List<FormSectionDef> formSectionDefList;

    private List<FormFieldDef> condRefDefFormFieldDefList;

    private List<String> fieldNameList;

    private boolean visible;

    private boolean editable;

    private boolean disabled;

    private int listOnlyCheck;

    public FormTabDef(TabContentType contentType, String name, String label, String applet, String reference,
            String filter, String editAction, List<FormSectionDef> formSectionDefList, boolean visible,
            boolean editable, boolean disabled) {
        this.contentType = contentType;
        this.name = name;
        this.label = label;
        this.applet = applet;
        this.filter = filter;
        this.reference = reference;
        this.editAction = editAction;
        this.formSectionDefList = formSectionDefList;
        this.visible = visible;
        this.editable = editable;
        this.disabled = disabled;
        this.listOnlyCheck = -1;
    }

    public FormTabDef(FormTabDef srcFormTabDef, List<FormSectionDef> formSectionDefList) {
        this.formSectionDefList = formSectionDefList;
        this.contentType = srcFormTabDef.contentType;
        this.name = srcFormTabDef.name;
        this.label = srcFormTabDef.label;
        this.applet = srcFormTabDef.applet;
        this.filter = srcFormTabDef.filter;
        this.reference = srcFormTabDef.reference;
        this.editAction = srcFormTabDef.editAction;
        this.visible = srcFormTabDef.visible;
        this.editable = srcFormTabDef.editable;
        this.disabled = srcFormTabDef.disabled;
        this.listOnlyCheck = srcFormTabDef.listOnlyCheck;
    }

    public TabContentType getContentType() {
        return contentType;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getApplet() {
        return applet;
    }

    public String getFilter() {
        return filter;
    }

    public String getReference() {
        return reference;
    }

    public String getEditAction() {
        return editAction;
    }

    public boolean isChildOrChildList() {
        return contentType.isChildOrChildList();
    }

    public boolean isWithListOnlyFields() {
        if (listOnlyCheck < 0) {
            listOnlyCheck = 0;
            for (FormSectionDef formSectionDef : formSectionDefList) {
                for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                    if (formFieldDef.isListOnly()) {
                        listOnlyCheck = 1;
                        return true;
                    }
                }
            }
        }

        return listOnlyCheck > 0;
    }

    public List<FormSectionDef> getFormSectionDefList() {
        return formSectionDefList;
    }

    public List<FormFieldDef> getCondRefDefFormFieldDefList() {
        if (condRefDefFormFieldDefList == null) {
            synchronized (this) {
                if (condRefDefFormFieldDefList == null) {
                    condRefDefFormFieldDefList = new ArrayList<FormFieldDef>();
                    for (FormSectionDef formSectionDef : formSectionDefList) {
                        for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                            if (formFieldDef.isWithInputRefDef() && formFieldDef.getInputRefDef().isWithCondition()) {
                                condRefDefFormFieldDefList.add(formFieldDef);
                            }
                        }
                    }

                    condRefDefFormFieldDefList = DataUtils.unmodifiableList(condRefDefFormFieldDefList);
                }
            }
        }

        return condRefDefFormFieldDefList;
    }

    public boolean isWithCondRefDefFormFields() {
        return !getCondRefDefFormFieldDefList().isEmpty();
    }

    public List<String> getFieldNameList() {
        if (fieldNameList == null) {
            fieldNameList = new ArrayList<String>();
            for (FormSectionDef formSectionDef : formSectionDefList) {
                for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                    fieldNameList.add(formFieldDef.getFieldName());
                }
            }

            fieldNameList = DataUtils.unmodifiableList(fieldNameList);
        }

        return fieldNameList;
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
