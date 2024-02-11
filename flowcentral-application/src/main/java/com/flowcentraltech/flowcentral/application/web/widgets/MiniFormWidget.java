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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormSectionDef;
import com.flowcentraltech.flowcentral.application.data.FormStatePolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormWidgetRulesPolicyDef;
import com.flowcentraltech.flowcentral.application.data.SetStateDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext.FormTab;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.data.FormStateRule;
import com.flowcentraltech.flowcentral.common.data.TargetFormState;
import com.flowcentraltech.flowcentral.common.data.TargetFormWidgetStates;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralMultiControl;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
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
public class MiniFormWidget extends AbstractFlowCentralMultiControl implements FormTriggerEvaluator {

    @Configurable
    private AppletUtilities appletUtilities;

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
                FormDef formDef = miniForm.getCtx().getFormDef();
                Map<String, Map<String, Widget>> altWidgetsByFields = Collections.emptyMap();
                if (formDef.isWithFormWidgetRulesPolicy()) {
                    altWidgetsByFields = new HashMap<String, Map<String, Widget>>();
                    for (FormWidgetRulesPolicyDef formWidgetRulesPolicyDef : formDef
                            .getFormWidgetRulesPolicyDefList()) {
                        String ruleName = formWidgetRulesPolicyDef.getName();
                        for (Map.Entry<String, String> ruleEditor : formWidgetRulesPolicyDef.getRuleEditors()
                                .entrySet()) {
                            String fieldName = ruleEditor.getKey();
                            Map<String, Widget> altWidgets = altWidgetsByFields.get(fieldName);
                            if (altWidgets == null) {
                                altWidgets = new LinkedHashMap<String, Widget>();
                                altWidgetsByFields.put(fieldName, altWidgets);
                            }

                            Widget widget = addExternalChildWidget(ruleEditor.getValue());
                            altWidgets.put(ruleName, widget);
                        }
                    }
                }

                for (FormSectionDef formSectionDef : miniForm.getFormTabDef().getFormSectionDefList()) {
                    int columns = formSectionDef.getColumns().columns();
                    List<FormWidget>[] formWidgetLists = new List[columns];
                    for (int i = 0; i < columns; i++) {
                        formWidgetLists[i] = new ArrayList<FormWidget>();
                    }

                    for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                        Map<String, Widget> altWidgets = altWidgetsByFields.get(formFieldDef.getFieldName());
                        FormWidget formWidget = new FormWidget(formSectionDef.getName(), formFieldDef,
                                addExternalChildWidget(formFieldDef.getRenderer()), altWidgets);
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

                formSections = DataUtils.unmodifiableMap(formSections);

                FormContext ctx = miniForm.getCtx();
                ctx.addFormWidgetStateList(formWidgets.values());
                if (miniForm.isMainForm()) {
                    ctx.setMainFormSections(formSections);
                }
            }

            oldMiniForm = miniForm;
        }

        Object formBean = miniForm != null ? miniForm.getFormBean() : null;
        if (formBean != oldFormBean) {
            if (miniForm != null) {
                ValueStore formValueStore = miniForm.getCtx().getFormValueStore();
                if (formWidgets != null) {
                    for (FormWidget formWidget : formWidgets.values()) {
                        formWidget.setValueStore(formValueStore);
                    }
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
        return getTrigger();
    }

    public void evaluateWidgetStates() throws UnifyException {
        final MiniForm form = getMiniForm();
        final FormContext ctx = form.getCtx();
        final Date now = appletUtilities.getNow();

        final ValueStore formValueStore = ctx.getFormValueStore();
        final ValueStoreReader formValueStoreReader = formValueStore.getReader();
        for (FormSection formSection : formSections.values()) {
            formSection.revertState();
        }

        Set<String> activatedAltRules = Collections.emptySet();
        List<FormStateRule> fieldRules = new ArrayList<FormStateRule>();
        String trigger = getTrigger();
        if (form.getScope().isMainForm() || ctx.isQuickEditMode()) {
            final FormDef formDef = ctx.getFormDef();
            boolean setValuesExecuted = false;

            if (formDef.isWithFormWidgetRulesPolicy()) {
                activatedAltRules = new HashSet<String>();
                for (FormWidgetRulesPolicyDef formWidgetRulesPolicyDef : formDef.getFormWidgetRulesPolicyDefList()) {
                    if (formWidgetRulesPolicyDef.match(formDef, formValueStore, now)) {
                        activatedAltRules.add(formWidgetRulesPolicyDef.getName());
                    }
                }
            }

            // Set values first
            if (formDef.isWithConsolidatedFormState()) {
                ConsolidatedFormStatePolicy policy = ctx.au().getComponent(ConsolidatedFormStatePolicy.class,
                        formDef.getConsolidatedFormState());
                TargetFormWidgetStates _states = policy.evaluateWidgetStates(formValueStore.getReader(), trigger);
                if (_states.isWithValueList()) {
                    _states.applyValues(formValueStore);
                    setValuesExecuted = true;
                }
            }

            final Map<String, Object> variables = Collections.emptyMap();
            for (FormStatePolicyDef formStatePolicyDef : formDef.getOnSwitchFormStatePolicyDefList()) {
                if (formStatePolicyDef.isTriggered(trigger)) {
                    ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                            ? formStatePolicyDef.getOnCondition().getObjectFilter(formDef.getEntityDef(),
                                    formValueStoreReader, now)
                            : null;
                    if (objectFilter == null || objectFilter.matchReader(formValueStoreReader)) {
                        if (formStatePolicyDef.isSetValues()) {
                            formStatePolicyDef.getSetValuesDef().apply(appletUtilities, formDef.getEntityDef(), now,
                                    formValueStore, variables, trigger);
                            setValuesExecuted = true;
                        }
                    }
                }
            }

            if (setValuesExecuted) {
                ctx.au().populateListOnlyFields(formDef.getEntityDef(), (Entity) formValueStore.getValueObject());
            }

            // Then switch states
            if (formDef.isWithConsolidatedFormState()) {
                ConsolidatedFormStatePolicy policy = ctx.au().getComponent(ConsolidatedFormStatePolicy.class,
                        formDef.getConsolidatedFormState());
                TargetFormWidgetStates _states = policy.evaluateWidgetStates(formValueStoreReader, trigger);
                for (TargetFormState state : _states.getTargetStateList()) {
                    if (state.isSectionRule()) {
                        for (String target : state.getTarget()) {
                            FormSection fs = formSections.get(target);
                            if (fs != null) {
                                fs.applyStatePolicy(state);
                            }
                        }
                    } else if (state.isFieldRule()) {
                        fieldRules.add(state);
                    }
                }

                policy.onFormSwitch(formValueStore, trigger);
            }

            for (FormStatePolicyDef formStatePolicyDef : formDef.getOnSwitchFormStatePolicyDefList()) {
                if (formStatePolicyDef.isTriggered(trigger)) {
                    ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                            ? formStatePolicyDef.getOnCondition().getObjectFilter(formDef.getEntityDef(),
                                    formValueStoreReader, now)
                            : null;
                    if (objectFilter == null || objectFilter.matchReader(formValueStoreReader)) {
                        for (SetStateDef setStateDef : formStatePolicyDef.getSetStatesDef().getSetStateList()) {
                            if (setStateDef.isSectionRule()) {
                                for (String target : setStateDef.getTarget()) {
                                    FormSection fs = formSections.get(target);
                                    if (fs != null) {
                                        fs.applyStatePolicy(setStateDef);
                                    }
                                }
                            } else if (setStateDef.isFieldRule()) {
                                fieldRules.add(setStateDef);
                            }
                        }
                    }
                }
            }
        }

        // Apply widget rules
        for (FormWidget formWidget : formWidgets.values()) {
            formWidget.revertState(activatedAltRules);
        }

        for (FormStateRule rule : fieldRules) {
            for (String target : rule.getTarget()) {
                FormWidget formWidget = formWidgets.get(target);
                if (formWidget != null) {
                    formWidget.applyStatePolicy(rule);
                }
            }
        }

        if (form.isAllocateTabIndex()) {
            allocateTabIndex(ctx);
        }
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {

    }

    private String getTrigger() throws UnifyException {
        getMiniForm();
        String trigger = null;
        String focusWidgetId = getRequestContextUtil().getTriggerWidgetId();
        boolean isResolveFocus = focusWidgetId != null;
        final boolean isFacade = isResolveFocus && focusWidgetId.startsWith("fac");
        for (FormWidget formWidget : formWidgets.values()) {
            if (isResolveFocus) {
                if (isFacade) {
                    if (focusWidgetId.equals(formWidget._widget.getFacadeId())) {
                        trigger = formWidget.getFieldName();
                    }
                } else {
                    if (focusWidgetId.equals(formWidget._widget.getId())) {
                        trigger = formWidget.getFieldName();
                    }
                }

                isResolveFocus = trigger == null;
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
                            formWidget._widget.setTabIndex(ctx.nextTabIndex());
                            if (formWidget.isFocusable() && formWidget.isEditable() && !formWidget.isDisabled()) {
                                final String fId = formWidget._widget.isUseFacadeFocus()
                                        ? formWidget._widget.getFacadeId()
                                        : formWidget._widget.getId();
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

        public String getName() {
            return formSectionDef.getName();
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
                editable = rule.getEditable().isTrue();
            }

            if (!rule.getDisabled().isConforming()) {
                disabled = rule.getDisabled().isTrue();
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
                        if (formWidget._widget.isVisible()) {
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

        private final Map<String, Widget> altWidgets;

        private Widget _widget;

        private String sectionName;

        private boolean required;

        private FormWidget(String sectionName, FormFieldDef formFieldDef, Widget widget,
                Map<String, Widget> altWidgets) {
            this.sectionName = sectionName;
            this.formFieldDef = formFieldDef;
            this.widget = widget;
            this._widget = widget;
            this.altWidgets = DataUtils.unmodifiableMap(altWidgets);
        }

        @Override
        public String getSectionName() {
            return sectionName;
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

        public Widget getResolvedWidget() {
            return _widget;
        }

        @Override
        public boolean isRequired() {
            return required;
        }

        public boolean isFocusable() {
            return _widget instanceof TextField || _widget instanceof TextArea
                    || _widget instanceof CheckBox /* && !(widget instanceof AbstractPopupTextField) */;
        }

        @Override
        public boolean isVisible() throws UnifyException {
            return _widget.isVisible();
        }

        @Override
        public boolean isEditable() throws UnifyException {
            return _widget.isEditable();
        }

        @Override
        public boolean isDisabled() throws UnifyException {
            return _widget.isDisabled();
        }

        public void setValueStore(ValueStore formValueStore) throws UnifyException {
            widget.setValueStore(formValueStore);
            if (!altWidgets.isEmpty()) {
                for (Widget __widget : altWidgets.values()) {
                    __widget.setValueStore(formValueStore);
                }
            }
        }

        public void applyStatePolicy(FormStateRule rule) throws UnifyException {
            FormSection formSection = formSections.get(sectionName);
            if (!rule.getVisible().isConforming()) {
                _widget.setVisible(rule.getVisible().isTrue());
            }

            if (!rule.getEditable().isConforming()) {
                _widget.setEditable(isContainerEditable() && formSection.isEditable() && rule.getEditable().isTrue()
                        && !formFieldDef.isListOnly());
            }

            if (!rule.getDisabled().isConforming()) {
                _widget.setDisabled(formSection.isDisabled() || rule.getDisabled().isTrue());
            }

            if (!rule.getRequired().isConforming()) {
                required = rule.getRequired().isTrue();
            }
        }

        public void revertState(Set<String> activatedAltRules) throws UnifyException {
            FormSection formSection = formSections.get(sectionName);
            _widget = widget;
            if (!activatedAltRules.isEmpty() && !altWidgets.isEmpty()) {
                for (Map.Entry<String, Widget> _entry : altWidgets.entrySet()) {
                    if (activatedAltRules.contains(_entry.getKey())) {
                        _widget = _entry.getValue();
                        break;
                    }
                }
            }

            _widget.setVisible(formFieldDef.isVisible());
            _widget.setEditable(isContainerEditable() && formSection.isEditable() && formFieldDef.isEditable());
            _widget.setDisabled(formSection.isDisabled() || formFieldDef.isDisabled()
                    || formFieldDef.getFieldName().equals(oldMiniForm.getCtx().getFixedReference()));
            required = formFieldDef.isRequired();
        }
    }
}
