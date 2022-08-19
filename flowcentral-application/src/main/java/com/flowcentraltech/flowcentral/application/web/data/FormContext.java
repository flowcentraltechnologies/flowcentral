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

package com.flowcentraltech.flowcentral.application.web.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.application.data.FormAnnotationDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormReviewPolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormStatePolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.SetStateDef;
import com.flowcentraltech.flowcentral.application.web.widgets.FormTriggerEvaluator;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.ReviewResult;
import com.flowcentraltech.flowcentral.common.data.AbstractContext;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.common.data.FormStateRule;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage;
import com.flowcentraltech.flowcentral.common.data.TargetFormState;
import com.flowcentraltech.flowcentral.common.data.TargetFormTabStates;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.ValueStoreUtils;
import com.tcdng.unify.web.ui.constant.MessageType;
import com.tcdng.unify.web.ui.widget.EventHandler;

/**
 * Form context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormContext extends AbstractContext {

    public enum Mode {
        NORMAL,
        SAVE_AS,
        CRUD
    }

    private AppletContext appletContext;

    private EntityDef parentEntityDef;

    private Entity parentInst;

    private EntityDef entityDef;

    private FormDef formDef;

    private EntityFormEventHandlers formEventHandlers;

    private FormTriggerEvaluator triggerEvaluator;

    private ValueStore formValueStore;

    private Object oldInst;

    private Object inst;

    private Map<String, FormTab> formTabs;

    private List<TargetFormMessage> reviewErrors;

    private Map<String, List<FormMessage>> reviewErrorsByTab;

    private List<FormWidgetState> formWidgetStateList;

    private FormValidationErrors formValidationErrors;

    private Set<String> visibleAnnotations;

    private String altFormTitle;

    private String fixedReference;

    private String focusMemoryId;

    private String focusWidgetId;

    private String tabMemoryId;

    private String tabWidgetId;

    private String[] tabWidgetIds;

    private int tabIndexCounter;

    private boolean readOnly;

    private boolean conditionalDisabled;

    private boolean formFocused;

    private Mode mode;

    public FormContext(AppletContext appletContext) throws UnifyException {
        this(appletContext, null, null, null, null);
    }

    public FormContext(AppletContext appletContext, EntityDef entityDef, Object inst) throws UnifyException {
        this(appletContext, null, null, entityDef, inst);
    }

    public FormContext(AppletContext appletContext, FormDef formDef, EntityFormEventHandlers formEventHandlers,
            Object inst) throws UnifyException {
        this(appletContext, formDef, formEventHandlers, formDef.getEntityDef(), inst);
    }

    public FormContext(AppletContext appletContext, FormDef formDef, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        this(appletContext, formDef, formEventHandlers, formDef.getEntityDef(), null);
    }

    private FormContext(AppletContext appletContext, FormDef formDef, EntityFormEventHandlers formEventHandlers,
            EntityDef entityDef, Object inst) throws UnifyException {
        this.appletContext = appletContext;
        this.formEventHandlers = formEventHandlers;
        this.entityDef = entityDef;
        this.inst = inst;
        if (formDef != null) {
            this.formDef = formDef;
            this.formTabs = new HashMap<String, FormTab>();
            for (FormTabDef formTabDef : formDef.getFormTabDefList()) {
                this.formTabs.put(formTabDef.getName(), new FormTab(formTabDef));
            }

            this.formTabs = Collections.unmodifiableMap(formTabs);
            this.formWidgetStateList = new ArrayList<FormWidgetState>();
            setInst(inst);
        } else {
            this.formTabs = Collections.emptyMap();
            this.formWidgetStateList = Collections.emptyList();
        }

        this.visibleAnnotations = new HashSet<String>();
        this.formValidationErrors = new FormValidationErrors();
        this.mode = Mode.NORMAL;
    }

    public AppletContext getAppletContext() {
        return appletContext;
    }

    public AppletUtilities au() {
        return appletContext.au();
    }

    public void setTriggerEvaluator(FormTriggerEvaluator triggerEvaluator) {
        this.triggerEvaluator = triggerEvaluator;
    }

    public EnvironmentService getEnvironment() {
        return appletContext.au().environment();
    }

    public EntityDef getParentEntityDef() {
        return parentEntityDef;
    }

    public void setParentEntityDef(EntityDef parentEntityDef) {
        this.parentEntityDef = parentEntityDef;
    }

    public Entity getParentInst() {
        return parentInst;
    }

    public void setParentInst(Entity parentInst) {
        this.parentInst = parentInst;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public FormDef getFormDef() {
        return formDef;
    }

    public List<FormActionDef> getFormActionDefList() {
        return formDef != null ? formDef.getFormActionDefList() : null;
    }

    public int nextTabIndex() {
        return ++tabIndexCounter;
    }

    public void resetTabIndex() {
        tabIndexCounter = 0;
    }

    public Collection<FormTab> getFormTabs() {
        return formTabs.values();
    }

    public List<FormWidgetState> getFormWidgetStateList() {
        return formWidgetStateList;
    }

    public void addFormWidgetStateList(Collection<? extends FormWidgetState> list) {
        for (FormWidgetState formWidgetState : list) {
            if (!formWidgetStateList.contains(formWidgetState)) {
                formWidgetStateList.add(formWidgetState);
            }
        }
    }

    public void removeFormWidgetStateList(Collection<? extends FormWidgetState> list) {
        formWidgetStateList.removeAll(list);
    }

    public List<EventHandler> getFormSwitchOnChangeHandlers() {
        return isCrudMode() ? formEventHandlers.getCrudSwitchOnChangeHandlers()
                : (isSaveAsMode() ? formEventHandlers.getSaveAsSwitchOnChangeHandlers()
                        : formEventHandlers.getFormSwitchOnChangeHandlers());
    }

    public EntityFormEventHandlers getFormEventHandlers() {
        return formEventHandlers;
    }

    public void setInst(Object inst) throws UnifyException {
        appletContext.extractReference(entityDef, inst);
        this.inst = inst;
        altFormTitle = formDef != null && formDef.isWithTitleFormat() ? appletContext.specialParamProvider()
                .getStringGenerator(null, getFormValueStore(), formDef.getTitleFormat()).generate() : null;
    }

    public Object getInst() {
        return inst;
    }

    public boolean isNormalMode() {
        return Mode.NORMAL.equals(mode);
    }

    public boolean isSaveAsMode() {
        return Mode.SAVE_AS.equals(mode);
    }

    public boolean isCrudMode() {
        return Mode.CRUD.equals(mode);
    }

    public void setNormalMode() {
        this.mode = Mode.NORMAL;
    }

    public void setSaveAsMode() {
        this.mode = Mode.SAVE_AS;
    }

    public void setCrudMode() {
        this.mode = Mode.CRUD;
    }

    public ValueStore getFormValueStore() {
        if (inst != oldInst) {
            formValueStore = ValueStoreUtils.getValueStore(inst);
            oldInst = inst;
        }

        return formValueStore;
    }

    public String getEntityName() {
        return entityDef.getName();
    }

    public String getAltFormTitle() {
        return altFormTitle;
    }

    public boolean isWithAltFormTitle() {
        return !StringUtils.isBlank(altFormTitle);
    }

    public void mergeValidationErrors(List<FormValidationErrors> formValidationErrors) {
        this.formValidationErrors.merge(formValidationErrors);
    }

    public void addValidationError(String message) {
        formValidationErrors.addValidationError(message);
    }

    public void addValidationErrors(List<FormMessage> messages) {
        if (messages != null) {
            for (FormMessage message : messages) {
                addValidationError(message);
            }
        }
    }

    public void addValidationError(FormMessage message) {
        formValidationErrors.addValidationError(message);
    }

    public void addValidationError(String fieldName, String message) {
        formValidationErrors.addValidationError(fieldName, message);
    }

    public void clearValidationErrors() {
        formValidationErrors.clearValidationErrors();
    }

    public boolean isWithFormErrors() {
        return formValidationErrors.isWithFormErrors();
    }

    public boolean isWithFieldErrors() {
        return formValidationErrors.isWithFieldErrors();
    }

    public boolean isWithFieldError(String fieldName) {
        return formValidationErrors.isWithFieldError(fieldName);
    }

    public boolean isWithFieldError(Collection<String> fieldNames) {
        return formValidationErrors.isWithFieldError(fieldNames);
    }

    public String getFieldError(String fieldName) {
        return formValidationErrors.getFieldError(fieldName);
    }

    public boolean isWithValidationErrors() {
        return formValidationErrors.isWithValidationErrors();
    }

    public List<FormMessage> getValidationErrors() {
        return formValidationErrors.getValidationErrors();
    }

    public FormValidationErrors getFormValidationErrors() {
        return formValidationErrors;
    }

    public void addReviewError(ReviewResult.Builder rrb, FormReviewPolicyDef policyDef) {
        addReviewError(rrb, policyDef.getTarget(), policyDef.getMessageType(), policyDef.getMessage(),
                policyDef.isSkippable());
    }

    public void addReviewError(ReviewResult.Builder rrb, List<String> target, MessageType messageType, String message,
            boolean skippable) {
        addReviewError(rrb, new HashSet<String>(target), messageType, message, skippable);
    }

    public void addReviewError(ReviewResult.Builder rrb, Set<String> target, MessageType messageType, String message,
            boolean skippable) {
        addReviewError(rrb, new TargetFormMessage(target, new FormMessage(messageType, message), skippable));
    }

    public void addReviewError(ReviewResult.Builder rrb, TargetFormMessage message) {
        if (reviewErrors == null) {
            reviewErrors = new ArrayList<TargetFormMessage>();
        }

        reviewErrors.add(message);
        if (message.isSkippable()) {
            rrb.addSkippable(message.getFormMessage().getMessage());
        } else {
            rrb.addRequired(message.getFormMessage().getMessage());
        }
    }

    public void clearReviewErrors() {
        appletContext.setOriginalEntityActionResult(null);
        reviewErrors = null;
        reviewErrorsByTab = null;
    }

    public String getFixedReference() {
        return fixedReference;
    }

    public void setFixedReference(String fixedReference) {
        this.fixedReference = fixedReference;
    }

    public String getFocusMemoryId() {
        return focusMemoryId;
    }

    public void setFocusMemoryId(String focusMemoryId) {
        this.focusMemoryId = focusMemoryId;
    }

    public String getFocusWidgetId() {
        return focusWidgetId;
    }

    public void setFocusWidgetId(String focusWidgetId) {
        this.focusWidgetId = focusWidgetId;
    }

    public boolean isWithFocusWidgetId() {
        return !StringUtils.isBlank(focusWidgetId);
    }

    public String getTabMemoryId() {
        return tabMemoryId;
    }

    public void setTabMemoryId(String tabMemoryId) {
        this.tabMemoryId = tabMemoryId;
    }

    public String getTabWidgetId() {
        return tabWidgetId;
    }

    public void setTabWidgetId(String tabWidgetId) {
        this.tabWidgetId = tabWidgetId;
    }

    public boolean isWithTabWidgetId() {
        return !StringUtils.isBlank(tabWidgetId);
    }

    public String[] getTabWidgetIds() {
        return tabWidgetIds;
    }

    public void setTabWidgetIds(String[] tabWidgetIds) {
        this.tabWidgetIds = tabWidgetIds;
    }

    public boolean isConditionalDisabled() {
        return conditionalDisabled;
    }

    public void setConditionalDisabled(boolean conditionalDisabled) {
        this.conditionalDisabled = conditionalDisabled;
    }

    public boolean isFormFocused() {
        return formFocused;
    }

    public void setFormFocused(boolean formFocused) {
        this.formFocused = formFocused;
    }

    public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isReadOnly() throws UnifyException {
        return readOnly || appletContext.isReadOnly();
    }

    public List<FormReviewPolicyDef> getReviewPolicies(FormReviewType type) {
        return formDef != null ? formDef.getFormReviewPolicies(type) : Collections.emptyList();
    }

    public boolean isWithReviewPolicies(FormReviewType type) {
        return formDef.isWithConsolidatedFormReview() || !getReviewPolicies(type).isEmpty();
    }

    public void copyReviewErrors(FormContext ctx) {
        reviewErrors = ctx.reviewErrors;
        reviewErrorsByTab = ctx.reviewErrorsByTab;
    }

    public boolean isWithReviewErrors() {
        return !DataUtils.isBlank(reviewErrors);
    }

    public List<FormMessage> getReviewMessages(String tabName) throws UnifyException {
        if (reviewErrorsByTab == null) {
            reviewErrorsByTab = new HashMap<String, List<FormMessage>>();
        }

        List<FormMessage> messageList = reviewErrorsByTab.get(tabName);
        if (messageList == null && !DataUtils.isBlank(reviewErrors)) {
            messageList = new ArrayList<FormMessage>();
            for (TargetFormMessage targetMessage : reviewErrors) {
                if (targetMessage.isTarget(tabName)) {
                    messageList.add(targetMessage.getFormMessage());
                }
            }
            messageList = DataUtils.unmodifiableList(messageList);
            reviewErrorsByTab.put(tabName, messageList);
        }

        return messageList;
    }

    public void evaluateTabStates() throws UnifyException {
        revertTabStates();
        ValueStore formValueStore = getFormValueStore();
        final Date now = appletContext.au().getNow();
        if (formDef.isWithConsolidatedFormState()) {
            ConsolidatedFormStatePolicy policy = au().getComponent(ConsolidatedFormStatePolicy.class,
                    formDef.getConsolidatedFormState());
            String trigger = triggerEvaluator != null ? triggerEvaluator.evaluateTrigger() : null;
            TargetFormTabStates states = policy.evaluateTabStates(formValueStore.getReader(), trigger);
            for (TargetFormState state : states.getTargetStateList()) {
                for (String target : state.getTarget()) {
                    FormTab tb = formTabs.get(target);
                    if (tb != null) {
                        tb.applyStatePolicy(state);
                    }
                }
            }

            if (states.isWithValueList()) {
                states.applyValues(formValueStore);
            }
        }

        for (FormStatePolicyDef formStatePolicyDef : formDef.getOnSwitchFormStatePolicyDefList()) {
            if (formStatePolicyDef.isTriggered("")) {
                ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                        ? formStatePolicyDef.getOnCondition().getObjectFilter(entityDef,
                                appletContext.specialParamProvider(), now)
                        : null;
                if (objectFilter == null || objectFilter.match(formValueStore)) {
                    for (SetStateDef setStateDef : formStatePolicyDef.getSetStatesDef().getSetStateList()) {
                        if (setStateDef.isTabRule()) {
                            for (String target : setStateDef.getTarget()) {
                                FormTab tb = formTabs.get(target);
                                if (tb != null) {
                                    tb.applyStatePolicy(setStateDef);
                                }
                            }
                        } else if (setStateDef.isAnnotationRule() && setStateDef.getVisible().isTrue()) {
                            visibleAnnotations.addAll(setStateDef.getTarget());
                        }
                    }
                }
            }
        }
    }

    public void revertTabStates() throws UnifyException {
        for (FormTab formTab : formTabs.values()) {
            formTab.revertState();
        }

        visibleAnnotations.clear();
        formFocused = false;
    }

    public Collection<String> getFormTabNames() {
        return formTabs.keySet();
    }

    public FormTab getFormTab(String name) {
        return formTabs.get(name);
    }

    public boolean isTabEditable(String name, boolean ignoreConditionalDisabled) {
        FormTab tab = formTabs.get(name);
        return ignoreConditionalDisabled ? tab == null || (tab.isEditable() && !tab.isDisabled())
                : !conditionalDisabled && (tab == null || (tab.isEditable() && !tab.isDisabled()));
    }

    public boolean isTabDisabled(String name, boolean ignoreConditionalDisabled) {
        FormTab tab = formTabs.get(name);
        return ignoreConditionalDisabled ? tab != null && tab.isDisabled()
                : conditionalDisabled || (tab != null && tab.isDisabled());
    }

    public List<FormAnnotationDef> getFormAnnotationDef() {
        if (!visibleAnnotations.isEmpty()) {
            List<FormAnnotationDef> list = new ArrayList<FormAnnotationDef>();
            for (String annotation : visibleAnnotations) {
                list.add(formDef.getFormAnnotationDef(annotation));
            }

            return list;
        }

        return Collections.emptyList();
    }

    public boolean isWithVisibleAnnotations() {
        return !visibleAnnotations.isEmpty();
    }

    public interface FormWidgetState {

        String getFieldName();

        String getFieldLabel();

        String getWidgetName();

        Integer getMinLen();

        Integer getMaxLen();

        boolean isRequired();

        boolean isVisible() throws UnifyException;

        boolean isEditable() throws UnifyException;

        boolean isDisabled() throws UnifyException;
    }

    public class FormTab {

        private FormTabDef formTabDef;

        private boolean visible;

        private boolean editable;

        private boolean disabled;

        private FormTab(FormTabDef formTabDef) {
            this.formTabDef = formTabDef;
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

        public void applyStatePolicy(FormStateRule rule) {
            if (!rule.getVisible().isConforming()) {
                visible = rule.getVisible().isTrue();
            }

            if (!rule.getDisabled().isConforming()) {
                disabled = rule.getDisabled().isTrue();
            }

            if (!rule.getEditable().isConforming()) {
                editable = rule.getEditable().isTrue();
            }
        }

        public void revertState() {
            visible = formTabDef.isVisible();
            editable = formTabDef.isEditable() | isSaveAsMode();
            disabled = formTabDef.isDisabled();
        }
    }

}
