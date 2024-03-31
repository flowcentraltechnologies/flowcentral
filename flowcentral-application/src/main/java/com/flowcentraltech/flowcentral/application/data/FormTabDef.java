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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Form tab definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormTabDef {

    private TabContentType contentType;

    private FilterGroupDef filterGroupDef;

    private String name;

    private String label;

    private String applet;

    private String reference;

    private String mappedFieldName;

    private String mappedForm;

    private String editAction;

    private String editViewOnly;

    private String editAllowAddition;

    private String editFixedRows;

    private List<FormSectionDef> formSectionDefList;

    private List<FormFieldDef> condRefDefFormFieldDefList;

    private List<String> fieldNameList;

    private boolean ignoreParentCondition;

    private boolean showSearch;

    private boolean quickEdit;

    private boolean quickOrder;

    private boolean visible;

    private boolean editable;

    private boolean disabled;

    private int listOnlyCheck;

    public FormTabDef(TabContentType contentType, FilterGroupDef filterGroupDef, String name, String label,
            String applet, String reference, String mappedFieldName, String mappedForm, String editAction,
            String editViewOnly, String editAllowAddition, String editFixedRows,
            List<FormSectionDef> formSectionDefList, boolean ignoreParentCondition, boolean showSearch,
            boolean quickEdit, boolean quickOrder, boolean visible, boolean editable, boolean disabled) {
        this.contentType = contentType;
        this.filterGroupDef = filterGroupDef;
        this.name = name;
        this.label = label;
        this.applet = applet;
        this.reference = reference;
        this.mappedFieldName = mappedFieldName;
        this.mappedForm = mappedForm;
        this.editAction = editAction;
        this.editViewOnly = editViewOnly;
        this.editAllowAddition = editAllowAddition;
        this.editFixedRows = editFixedRows;
        this.formSectionDefList = formSectionDefList;
        this.ignoreParentCondition = ignoreParentCondition;
        this.showSearch = showSearch;
        this.quickEdit = quickEdit;
        this.quickOrder = quickOrder;
        this.visible = visible;
        this.editable = editable;
        this.disabled = disabled;
        this.listOnlyCheck = -1;
    }

    public FormTabDef(FormTabDef srcFormTabDef, List<FormSectionDef> formSectionDefList) {
        this.formSectionDefList = formSectionDefList;
        this.contentType = srcFormTabDef.contentType;
        this.filterGroupDef = srcFormTabDef.filterGroupDef;
        this.name = srcFormTabDef.name;
        this.label = srcFormTabDef.label;
        this.applet = srcFormTabDef.applet;
        this.reference = srcFormTabDef.reference;
        this.mappedFieldName = srcFormTabDef.mappedFieldName;
        this.mappedForm = srcFormTabDef.mappedForm;
        this.editAction = srcFormTabDef.editAction;
        this.editViewOnly = srcFormTabDef.editViewOnly;
        this.editAllowAddition = srcFormTabDef.editAllowAddition;
        this.editFixedRows = srcFormTabDef.editFixedRows;
        this.ignoreParentCondition = srcFormTabDef.ignoreParentCondition;
        this.showSearch = srcFormTabDef.showSearch;
        this.quickEdit = srcFormTabDef.quickEdit;
        this.quickOrder = srcFormTabDef.quickOrder;
        this.visible = srcFormTabDef.visible;
        this.editable = srcFormTabDef.editable;
        this.disabled = srcFormTabDef.disabled;
        this.listOnlyCheck = srcFormTabDef.listOnlyCheck;
    }

    public TabContentType getContentType() {
        return contentType;
    }

    public FilterGroupDef getFilterGroupDef() {
        return filterGroupDef;
    }

    public boolean isWithFilterType(FilterType type) {
        return filterGroupDef != null && filterGroupDef.isWithFilterType(type);
    }

    public Restriction getRestriction(FilterType type, ValueStoreReader reader, Date now) throws UnifyException {
        return filterGroupDef != null ? filterGroupDef.getRestriction(type, reader, now) : null;
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

    public String getReference() {
        return reference;
    }

    public String getMappedFieldName() {
        return mappedFieldName;
    }

    public String getMappedForm() {
        return mappedForm;
    }

    public String getEditAction() {
        return editAction;
    }

    public String getEditViewOnly() {
        return editViewOnly;
    }

    public boolean isWithEditViewOnly() {
        return !StringUtils.isBlank(editViewOnly);
    }

    public String getEditAllowAddition() {
        return editAllowAddition;
    }

    public boolean isWithEditAllowAddition() {
        return !StringUtils.isBlank(editAllowAddition);
    }

    public String getEditFixedRows() {
        return editFixedRows;
    }

    public boolean isWithEditFixedRows() {
        return !StringUtils.isBlank(editFixedRows);
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
                            if (formFieldDef.isWithInputRefDef() && formFieldDef.getInputRefDef().isWithFilter()) {
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

    public boolean isIgnoreParentCondition() {
        return ignoreParentCondition;
    }

    public boolean isShowSearch() {
        return showSearch;
    }

    public boolean isQuickEdit() {
        return quickEdit;
    }

    public boolean isQuickOrder() {
        return quickOrder;
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

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}
