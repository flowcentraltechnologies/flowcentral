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
package com.flowcentraltech.flowcentral.common.web.widgets;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.AbstractContainer;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.Section;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Form widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-form")
@UplAttributes({ @UplAttribute(name = "columns", type = int.class, defaultVal = "1"),
        @UplAttribute(name = "section", type = Section[].class, mandatory = true) })
public class Form extends AbstractContainer {

    private FormSection[] formSections;

    private String dataGroupId;

    private FormValidationErrors formValidationErrors;

    public Form() {
        super(false);
        this.formValidationErrors = new FormValidationErrors();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();

        final int columns = getColumns();
        Section[] sections = getUplAttribute(Section[].class, "section");
        formSections = new FormSection[sections.length];
        for (int i = 0; i < sections.length; i++) {
            Section section = sections[i];
            List<Widget>[] formWidgetLists = new List[columns];
            for (int j = 0; j < columns; j++) {
                formWidgetLists[j] = new ArrayList<Widget>();
            }

            int j = 0;
            for (String longName : section.getReferences()) {
                Widget widget = getWidgetByLongName(longName);
                if (widget.isSupportStretch()) {
                    widget.setExtraStyle("width:100%;");
                }
                
                formWidgetLists[j].add(widget);

                j++;
                if (j >= columns) {
                    j = 0;
                }
            }

            formSections[i] = new FormSection(section, formWidgetLists);
        }

        dataGroupId = getPrefixedId("data_");
    }

    @Override
    public List<String> getShallowReferencedLongNames(String attribute) throws UnifyException {
        return getWidgetInfo().getShallowNames();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void cascadeValueStore() throws UnifyException {
        super.cascadeValueStore();

        ValueStore valueStore = getChildBindingValueStore();
        for (FormSection formSection : formSections) {
            if (formSection.isBinding()) {
                Object newValue = null;
                if (valueStore != null) {
                    newValue = valueStore.retrieve(formSection.getBinding());
                }

                if (formSection.oldValue != newValue) {
                    formSection.valueStore = null;
                    formSection.valueStoreList = null;
                    if (newValue != null) {
                        if (newValue.getClass().isArray()) {
                            List<ValueStore> valueList = new ArrayList<ValueStore>();
                            int len = Array.getLength(newValue);
                            for (int i = 0; i < len; i++) {
                                valueList.add(createArrayValueStore((Object[]) newValue, i));
                            }

                            formSection.valueStoreList = valueList;
                        } else if (newValue instanceof List) {
                            List<Object> list = (List<Object>) newValue;
                            List<ValueStore> valueList = new ArrayList<ValueStore>(list.size());
                            for (int i = 0; i < list.size(); i++) {
                                valueList.add(createListValueStore(list, i));
                            }

                            formSection.valueStoreList = valueList;
                        } else {
                            formSection.valueStore = createValueStore(newValue);
                        }
                    }
                    formSection.oldValue = newValue;
                }
            }
        }
    }

    @Override
    public void addPageAliases() throws UnifyException {
        if (isContainerEditable()) {
            getRequestContextUtil().addPageAlias(getId(), getDataGroupId());
        }
    }

    public int getColumns() throws UnifyException {
        return getUplAttribute(int.class, "columns");
    }

    public FormSection[] getSections() throws UnifyException {
        return formSections;
    }

    public int getSectionCount() throws UnifyException {
        return formSections.length;
    }

    public String getDataGroupId() {
        return dataGroupId;
    }

    public void setSectionState(String id, boolean visible, boolean editable, boolean disabled) throws UnifyException {
        FormSection formSection = getFormSection(id);
        formSection.setVisible(visible);
        formSection.setEditable(editable);
        formSection.setDisabled(disabled);
    }

    public void setSectionVisible(int index, boolean visible) {
        formSections[index].setVisible(visible);
    }

    public boolean isSectionVisible(int index) {
        return formSections[index].isVisible();
    }

    public void setSectionEditable(int index, boolean editable) {
        formSections[index].setEditable(editable);
    }

    public boolean isSectionEditable(int index) {
        return formSections[index].isEditable();
    }

    public void setSectionDisabled(int index, boolean disabled) {
        formSections[index].setDisabled(disabled);
    }

    public boolean isSectionDisabled(int index) {
        return formSections[index].isDisabled();
    }

    public FormValidationErrors getFormValidationErrors() {
        return formValidationErrors;
    }

    public FormValidationErrors validate(EvaluationMode evaluationMode) throws UnifyException {
        formValidationErrors.clearValidationErrors();

        final int columns = getColumns();
        for (FormSection formSection : getSections()) {
            if (formSection.isVisible()) {
                for (int i = 0; i < columns; i++) {
                    for (Widget widget : formSection.getWidgetList(i)) {
                        Object val = widget.getValue();
                        if (widget.isVisible() && widget instanceof Control) {
                            TriState required = ((Control) widget).getRequired();
                            if (TriState.TRUE.equals(required)
                                    && com.flowcentraltech.flowcentral.common.util.ValidationUtils.isBlank(val)) {
                                addFieldRequiredError(widget.getBinding(), widget.getCaption());
                            }
                        }
                    }
                }
            }
        }

        return formValidationErrors;
    }

    public void addFieldRequiredError(String fieldName, String caption) throws UnifyException {
        addValidationError(fieldName, getApplicationMessage("form.validation.formfield.required", caption));
    }

    public void addValidationError(String fieldName, String message) throws UnifyException {
        formValidationErrors.addValidationError(fieldName, message);
    }

    public void reset() throws UnifyException {
        setEditable(true);

        for (FormSection formSection : formSections) {
            formSection.reset();

            for (String ref : formSection.getSection().getReferences()) {
                Widget widget = getWidgetByLongName(ref);
                widget.setVisible(true);
                widget.setEditable(true);
                widget.setDisabled(false);
            }
        }
    }

    protected FormSection getFormSection(String id) throws UnifyException {
        for (FormSection section : formSections) {
            if (id.equals(section.getId())) {
                return section;
            }
        }

        return null;
    }

    public static class FormSection {

        private Section section;

        private Object oldValue;

        private ValueStore valueStore;

        private List<ValueStore> valueStoreList;

        private List<Widget>[] formWidgetLists;

        private boolean visible;

        private boolean editable;

        private boolean disabled;

        public FormSection(Section section, List<Widget>[] formWidgetLists) {
            this.section = section;
            this.formWidgetLists = formWidgetLists;
            reset();
        }

        public Section getSection() {
            return section;
        }

        public List<Widget> getWidgetList(int column) {
            return formWidgetLists[column];
        }

        public String getId() throws UnifyException {
            return section.getUplId();
        }

        public String getBinding() throws UnifyException {
            return section.getBinding();
        }

        public boolean isBinding() throws UnifyException {
            return section.isBinding();
        }

        public boolean isBindingValue() throws UnifyException {
            return valueStore != null;
        }

        public boolean isBindingValueList() throws UnifyException {
            return valueStoreList != null;
        }

        public ValueStore getValueStore() {
            return valueStore;
        }

        public List<ValueStore> getValueStoreList() {
            return valueStoreList;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public void reset() {
            visible = true;
            editable = true;
            disabled = false;
        }
    }
}
