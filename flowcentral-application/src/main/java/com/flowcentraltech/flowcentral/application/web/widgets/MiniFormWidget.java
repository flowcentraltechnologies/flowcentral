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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormSectionDef;
import com.flowcentraltech.flowcentral.application.data.FormStatePolicyDef;
import com.flowcentraltech.flowcentral.application.data.SetStateDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext.FormTab;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.data.FormStateRule;
import com.flowcentraltech.flowcentral.common.data.TargetFormState;
import com.flowcentraltech.flowcentral.common.data.TargetFormWidgetStates;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.CheckBox;
import com.tcdng.unify.web.ui.widget.control.TextArea;
import com.tcdng.unify.web.ui.widget.control.TextField;

/**
 * Mini form widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-miniform")
@UplAttributes({ @UplAttribute(name = "strictRows", type = boolean.class) })
public class MiniFormWidget extends AbstractMultiControl implements FormTriggerEvaluator {

    private MiniForm oldMiniForm;

    private Object oldFormBean;

    private Map<String, FormSection> formSections;

    private Map<String, FormWidget> formWidgets;

    @SuppressWarnings("unchecked")
    public MiniForm getMiniForm() throws UnifyException {
        MiniForm miniForm = getValue(MiniForm.class);
        if (miniForm != oldMiniForm) {
            removeAllExternalChildWidgets();
            if (oldMiniForm != null && formWidgets != null) {
                oldMiniForm.getCtx().removeFormWidgetStateList(formWidgets.values());
            }

            formSections = null;
            formWidgets = null;
            if (miniForm != null) {
                boolean isStrictRows = isStrictRows();
                formSections = new LinkedHashMap<String, FormSection>();
                formWidgets = new HashMap<String, FormWidget>();
                for (FormSectionDef formSectionDef : miniForm.getFormTabDef().getFormSectionDefList()) {
                    int columns = formSectionDef.getColumns().columns();
                    List<FormWidget>[] formWidgetLists = new List[columns];
                    for (int i = 0; i < columns; i++) {
                        formWidgetLists[i] = new ArrayList<FormWidget>();
                    }

                    for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                        FormWidget formWidget = new FormWidget(formSectionDef.getName(), formFieldDef,
                                addExternalChildWidget(formFieldDef.getRenderer()));
                        formWidgets.put(formWidget.getFieldName(), formWidget);
                        formWidgetLists[formFieldDef.getColumn()].add(formWidget);
                    }

                    int rows = 0;
                    for (int i = 0; i < columns; i++) {
                        List<FormWidget> list = formWidgetLists[i];
                        if (rows < list.size()) {
                            rows = list.size();
                        }

                        formWidgetLists[i] = DataUtils.unmodifiableList(list);
                    }

                    formSections.put(formSectionDef.getName(),
                            new FormSection(formSectionDef, formWidgetLists, rows, isStrictRows));
                }

                miniForm.getCtx().addFormWidgetStateList(formWidgets.values());
                formSections = DataUtils.unmodifiableMap(formSections);
            }

            oldMiniForm = miniForm;
        }

        Object formBean = miniForm != null ? miniForm.getFormBean() : null;
        if (formBean != oldFormBean) {
            ValueStore formValueStore = miniForm.getCtx().getFormValueStore();
            if (formWidgets != null) {
                for (FormWidget formWidget : formWidgets.values()) {
                    formWidget.getWidget().setValueStore(formValueStore);
                }
            }

            oldFormBean = formBean;
        }

        return miniForm;
    }

    public boolean isStrictRows() throws UnifyException {
        return getUplAttribute(boolean.class, "strictRows");
    }

    public FormContext getCtx() throws UnifyException {
        return getMiniForm().getCtx();
    }

    public boolean isMainForm() throws UnifyException {
        return getMiniForm().getScope().isMainForm();
    }

    public Collection<FormSection> getFormSectionList() throws UnifyException {
        getMiniForm();
        return formSections.values();
    }

    @Override
    public String evaluateTrigger() throws UnifyException {
        return getTrigger(false);
    }

    public void evaluateWidgetStates() throws UnifyException {
        final MiniForm form = getMiniForm();
        final FormContext ctx = form.getCtx();
        final AppletUtilities au = (AppletUtilities) getComponent(ApplicationModuleNameConstants.APPLET_UTILITIES);
        final Date now = au.getNow();

        final ValueStore formValueStore = ctx.getFormValueStore();
        for (FormSection formSection : formSections.values()) {
            formSection.revertState();
        }

        String trigger = getTrigger(true);
        if (form.getScope().isMainForm()) {
            FormDef formDef = ctx.getFormDef();
            boolean setValuesExecuted = false;

            if (formDef.isWithConsolidatedFormState()) {
                ConsolidatedFormStatePolicy policy = ctx.getAu().getComponent(ConsolidatedFormStatePolicy.class,
                        formDef.getConsolidatedFormState());
                TargetFormWidgetStates states = policy.evaluateWidgetStates(formValueStore.getReader(), trigger);
                for (TargetFormState state : states.getTargetStateList()) {
                    if (state.isSectionRule()) {
                        for (String target : state.getTarget()) {
                            FormSection fs = formSections.get(target);
                            if (fs != null) {
                                fs.applyStatePolicy(state);
                            }
                        }
                    } else if (state.isFieldRule()) {
                        for (String target : state.getTarget()) {
                            FormWidget formWidget = formWidgets.get(target);
                            if (formWidget != null) {
                                formWidget.applyStatePolicy(state);
                            }
                        }
                    }
                }
                
                if (states.isWithValueList()) {
                    states.applyValues(formValueStore);
                    setValuesExecuted = true;
                }
                
                policy.onFormSwitch(formValueStore, trigger);
            }

            final Map<String, Object> variables = Collections.emptyMap();
            for (FormStatePolicyDef formStatePolicyDef : formDef.getOnSwitchFormStatePolicyDefList()) {
                if (formStatePolicyDef.isTriggered(trigger)) {
                    ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                            ? formStatePolicyDef.getOnCondition().getObjectFilter(formDef.getEntityDef(),
                                    au.getSpecialParamProvider(), now)
                            : null;
                    if (objectFilter == null || objectFilter.match(formValueStore)) {
                        for (SetStateDef setStateDef : formStatePolicyDef.getSetStatesDef().getSetStateList()) {
                            if (setStateDef.isSectionRule()) {
                                for (String target : setStateDef.getTarget()) {
                                    FormSection fs = formSections.get(target);
                                    if (fs != null) {
                                        fs.applyStatePolicy(setStateDef);
                                    }
                                }
                            } else if (setStateDef.isFieldRule()) {
                                for (String target : setStateDef.getTarget()) {
                                    FormWidget formWidget = formWidgets.get(target);
                                    if (formWidget != null) {
                                        formWidget.applyStatePolicy(setStateDef);
                                    }
                                }
                            }
                        }

                        if (formStatePolicyDef.isSetValues()) {
                            formStatePolicyDef.getSetValuesDef().apply(au, formDef.getEntityDef(), now, formValueStore,
                                    variables, trigger);
                            setValuesExecuted = true;
                        }
                    }
                }
            }

            if (setValuesExecuted) {
                ctx.getAu().populateListOnlyFields(formDef.getEntityDef(), (Entity) formValueStore.getValueObject());
            }
        }

        if (form.isAllocateTabIndex()) {
            allocateTabIndex(ctx);
        }
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {

    }

    private String getTrigger(boolean revertState) throws UnifyException {
        getMiniForm();
        String trigger = null;
        String focusWidgetId = getRequestContextUtil().getTriggerWidgetId();
        boolean isResolveFocus = focusWidgetId != null;
        final boolean isFacade = isResolveFocus && focusWidgetId.startsWith("fac");
        for (FormWidget formWidget : formWidgets.values()) {
            if (isResolveFocus) {
                if (isFacade) {
                    if (focusWidgetId.equals(formWidget.widget.getFacadeId())) {
                        trigger = formWidget.getFieldName();
                    }
                } else {
                    if (focusWidgetId.equals(formWidget.widget.getId())) {
                        trigger = formWidget.getFieldName();
                    }
                }

                isResolveFocus = trigger == null;
            }

            if (revertState) {
                formWidget.revertState();
            }
        }

        return trigger;
    }
    
    private void allocateTabIndex(FormContext ctx) throws UnifyException {
        ctx.setFormFocused(false);
        if (ctx.isWithTabWidgetId()) { // Tab memory
            getRequestContextUtil().setFocusOnWidgetId(ctx.getTabWidgetId());
            ctx.setTabWidgetId(null);
            ctx.setFormFocused(true);
        }

        List<String> tabWidgetIdList = new ArrayList<String>();
        for (FormSection formSection : formSections.values()) {
            if (formSection.isVisible()) {
                final int rows = formSection.rows;
                final int columns = formSection.getColumns();
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        List<FormWidget> list = formSection.formWidgetList[j];
                        if (i < list.size()) {
                            FormWidget formWidget = list.get(i);
                            formWidget.widget.setTabIndex(ctx.nextTabIndex());
                            if (formWidget.isFocusable() && formWidget.isEditable() && !formWidget.isDisabled()) {
                                final String fId = formWidget.widget.isUseFacadeFocus()
                                        ? formWidget.widget.getFacadeId()
                                        : formWidget.widget.getId();
                                tabWidgetIdList.add(fId);
                                if (!ctx.isFormFocused()) {
                                    getRequestContextUtil().setFocusOnWidgetId(fId);
                                    ctx.setTabWidgetId(fId);
                                    ctx.setFormFocused(true);
                                }
                            }
                        }
                    }
                }
            }
        }

        ctx.setTabWidgetIds(DataUtils.toArray(String.class, tabWidgetIdList));
    }

    public class FormSection {

        private FormSectionDef formSectionDef;

        private List<FormWidget>[] formWidgetList;

        private RowRegulator rowRegulator;

        private final int rows;

        private boolean visible;

        private boolean editable;

        private boolean disabled;

        private FormSection(FormSectionDef formSectionDef, List<FormWidget>[] formWidgetLists, int rows,
                boolean isStrictRows) {
            this.formSectionDef = formSectionDef;
            this.formWidgetList = formWidgetLists;
            this.rows = rows;
            if (isStrictRows) {
                rowRegulator = new RowRegulator();
            }
        }

        public String getLabel() {
            return formSectionDef.getLabel();
        }

        public boolean isWithLabel() {
            return !StringUtils.isBlank(formSectionDef.getLabel());
        }

        public RowRegulator getRowRegulator() {
            return rowRegulator;
        }

        public List<FormWidget> getFormWidgetList(int column) {
            return formWidgetList[column];
        }

        public FormWidget getFormWidget(int column, int row) {
            return formWidgetList[column].get(row);
        }

        public int getColumnSize(int column) {
            return formWidgetList[column].size();
        }

        public int getColumns() {
            return formWidgetList.length;
        }

        public FormColumnsType getColumnsType() {
            return formSectionDef.getColumns();
        }

        public boolean isEditable() {
            return editable;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public boolean isVisible() {
            return visible;
        }

        public void applyStatePolicy(FormStateRule rule) {
            if (!rule.getVisible().isConforming()) {
                visible = rule.getVisible().isTrue();
            }

            if (!rule.getEditable().isConforming()) {
                editable = editable && rule.getEditable().isTrue();
            }

            if (!rule.getDisabled().isConforming()) {
                disabled = disabled || rule.getDisabled().isTrue();
            }
        }

        public void revertState() {
            final FormTab formTab = oldMiniForm.getCtx().getFormTab(oldMiniForm.getFormTabDef().getName());
            visible = formSectionDef.isVisible();
            if (formTab == null) {
                editable = formSectionDef.isEditable();
                disabled = formSectionDef.isDisabled();
            } else {
                editable = formTab.isEditable() && formSectionDef.isEditable();
                disabled = formTab.isDisabled() || formSectionDef.isDisabled();
            }
        }

        public class RowRegulator {
            private int[] findexes;

            private int[] flens;

            private FormWidget[] rowWidgets;

            public RowRegulator() {
                findexes = new int[formWidgetList.length];
                flens = new int[formWidgetList.length];
                rowWidgets = new FormWidget[formWidgetList.length];
                for (int i = 0; i < formWidgetList.length; i++) {
                    flens[i] = formWidgetList[i].size();
                }
            }

            public FormWidget[] getRowWidgets() {
                return rowWidgets;
            }

            public void resetRows() {
                Arrays.fill(findexes, 0);
            }

            public boolean nextRow() throws UnifyException {
                boolean result = false;
                int columns = getColumns();
                for (int col = 0; col < columns; col++) {
                    rowWidgets[col] = null;
                    int rows = flens[col];
                    int row = findexes[col];
                    while (row < rows) {
                        FormWidget formWidget = formWidgetList[col].get(row);
                        row++;
                        if (formWidget.getWidget().isVisible()) {
                            rowWidgets[col] = formWidget;
                            result = true;
                            break;
                        }
                    }

                    findexes[col] = row;
                }

                return result;
            }
        }
    }

    public class FormWidget implements FormContext.FormWidgetState {

        private FormFieldDef formFieldDef;

        private final Widget widget;

        private String sectionName;

        private boolean required;

        private FormWidget(String sectionName, FormFieldDef formFieldDef, Widget widget) {
            this.sectionName = sectionName;
            this.formFieldDef = formFieldDef;
            this.widget = widget;
        }

        @Override
        public String getFieldName() {
            return formFieldDef.getFieldName();
        }

        @Override
        public String getWidgetName() {
            return formFieldDef.getWidgetName();
        }

        @Override
        public String getFieldLabel() {
            return formFieldDef.getFieldLabel();
        }

        public boolean isSwitchOnChange() {
            return formFieldDef.isSwitchOnChange();
        }

        @Override
        public Integer getMinLen() {
            return formFieldDef.getMinLen();
        }

        @Override
        public Integer getMaxLen() {
            return formFieldDef.getMaxLen();
        }

        public Widget getWidget() {
            return widget;
        }

        @Override
        public boolean isRequired() {
            return required;
        }

        public boolean isFocusable() {
            return widget instanceof TextField || widget instanceof TextArea
                    || widget instanceof CheckBox /* && !(widget instanceof AbstractPopupTextField) */;
        }

        @Override
        public boolean isVisible() throws UnifyException {
            return widget.isVisible();
        }

        @Override
        public boolean isEditable() throws UnifyException {
            return widget.isEditable();
        }

        @Override
        public boolean isDisabled() throws UnifyException {
            return widget.isDisabled();
        }

        public void applyStatePolicy(FormStateRule rule) throws UnifyException {
            FormSection formSection = formSections.get(sectionName);
            if (!rule.getVisible().isConforming()) {
                widget.setVisible(rule.getVisible().isTrue());
            }

            if (!rule.getEditable().isConforming()) {
                widget.setEditable(isContainerEditable() && formSection.isEditable() && rule.getEditable().isTrue()
                        && !formFieldDef.isListOnly());
            }

            if (!rule.getDisabled().isConforming()) {
                widget.setDisabled(formSection.isDisabled() || rule.getDisabled().isTrue());
            }

            if (!rule.getRequired().isConforming()) {
                required = rule.getRequired().isTrue();
            }
        }

        public void revertState() throws UnifyException {
            FormSection formSection = formSections.get(sectionName);
            widget.setVisible(formFieldDef.isVisible());
            widget.setEditable(isContainerEditable() && formSection.isEditable() && formFieldDef.isEditable());
            widget.setDisabled(formSection.isDisabled() || formFieldDef.isDisabled()
                    || formFieldDef.getFieldName().equals(oldMiniForm.getCtx().getFixedReference()));
            required = formFieldDef.isRequired();
        }
    }
}
