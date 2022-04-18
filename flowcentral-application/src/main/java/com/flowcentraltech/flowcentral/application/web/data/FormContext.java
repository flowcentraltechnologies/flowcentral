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
import java.util.Arrays;
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
import com.flowcentraltech.flowcentral.application.data.FormAnnotationDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormReviewPolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormStatePolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.SetStateDef;
import com.flowcentraltech.flowcentral.application.web.widgets.FormTriggerEvaluator;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.data.AbstractContext;
import com.flowcentraltech.flowcentral.common.data.ErrorContext;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.common.data.FormStateRule;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage;
import com.flowcentraltech.flowcentral.common.data.TargetFormState;
import com.flowcentraltech.flowcentral.common.data.TargetFormTabStates;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
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
public class FormContext extends AbstractContext implements ErrorContext {

    private AppletContext appletContext;

    private EntityDef entityDef;

    private FormDef formDef;

    private EntityFormEventHandlers formEventHandlers;

    private FormTriggerEvaluator triggerEvaluator;

    private ValueStore formValueStore;

    private Object oldInst;

    private Object inst;

    private Map<String, FormTab> formTabs;

    private Map<String, String> invalidFields;

    private List<TargetFormMessage> reviewErrors;

    private Map<String, List<FormMessage>> reviewErrorsByTab;

    private List<FormMessage> validationErrors;

    private List<FormWidgetState> formWidgetStateList;

    private Set<String> visibleAnnotations;

    private String fixedReference;

    private String focusMemoryId;

    private String focusWidgetId;

    private String tabMemoryId;

    private String tabWidgetId;

    private String[] tabWidgetIds;

    private int tabIndexCounter;

    private boolean formFocused;

    private boolean saveAsMode;

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
            this.appletContext.extractReference(entityDef, inst);
        } else {
            this.formTabs = Collections.emptyMap();
            this.formWidgetStateList = Collections.emptyList();
        }

        this.visibleAnnotations = new HashSet<String>();
    }

    public AppletContext getAppletContext() {
        return appletContext;
    }

    public AppletUtilities getAu() {
        return appletContext.getAu();
    }

    public void setTriggerEvaluator(FormTriggerEvaluator triggerEvaluator) {
        this.triggerEvaluator = triggerEvaluator;
    }

    public EnvironmentService getEnvironment() {
        return appletContext.getAu().getEnvironment();
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public FormDef getFormDef() {
        return formDef;
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
        return saveAsMode ? formEventHandlers.getSaveAsSwitchOnChangeHandlers()
                : formEventHandlers.getFormSwitchOnChangeHandlers();
    }

    public EntityFormEventHandlers getFormEventHandlers() {
        return formEventHandlers;
    }

    public void setInst(Object inst) {
        this.inst = inst;
    }

    public Object getInst() {
        return inst;
    }

    public boolean isSaveAsMode() {
        return saveAsMode;
    }

    public void setSaveAsMode(boolean saveAsMode) {
        this.saveAsMode = saveAsMode;
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

    @Override
    public void addValidationError(String message) {
        addValidationError(new FormMessage(MessageType.ERROR, message));
    }

    public void addValidationErrors(List<FormMessage> messages) {
        if (messages != null) {
            for(FormMessage message: messages) {
                addValidationError(message);
            }
        }
    }

    @Override
    public void addValidationError(FormMessage message) {
        if (validationErrors == null) {
            validationErrors = new ArrayList<FormMessage>();
        }

        validationErrors.add(message);
    }

    @Override
    public void addValidationError(String fieldName, String message) {
        if (invalidFields == null) {
            invalidFields = new HashMap<String, String>();
        }

        invalidFields.put(fieldName, message);
    }

    @Override
    public void clearValidationErrors() {
        invalidFields = null;
        validationErrors = null;
    }

    public void addReviewError(FormReviewPolicyDef policyDef) {
        addReviewError(policyDef.getTarget(), policyDef.getMessageType(), policyDef.getMessage());
    }

    public void addReviewError(List<String> target, MessageType messageType, String message) {
        addReviewError(new HashSet<String>(target), messageType, message);
    }

    public void addReviewError(Set<String> target, MessageType messageType, String message) {
        addReviewError(new TargetFormMessage(target, new FormMessage(messageType, message)));
    }

    public void addReviewError(TargetFormMessage message) {
        if (reviewErrors == null) {
            reviewErrors = new ArrayList<TargetFormMessage>();
        }

        reviewErrors.add(message);
    }

    public void clearReviewErrors() {
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

    public boolean isFormFocused() {
        return formFocused;
    }

    public void setFormFocused(boolean formFocused) {
        this.formFocused = formFocused;
    }

    public boolean isReadOnly() throws UnifyException {
        return appletContext.isReadOnly();
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

    @Override
    public boolean isWithFormErrors() {
        return !DataUtils.isBlank(invalidFields) || !DataUtils.isBlank(validationErrors);
    }

    @Override
    public boolean isWithFieldErrors() {
        return !DataUtils.isBlank(invalidFields);
    }

    @Override
    public boolean isWithFieldError(String fieldName) {
        return invalidFields != null && invalidFields.containsKey(fieldName);
    }

    @Override
    public boolean isWithFieldError(Collection<String> fieldNames) {
        if (invalidFields != null) {
            for (String fieldName : fieldNames) {
                if (invalidFields.containsKey(fieldName)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getFieldError(String fieldName) {
        return invalidFields.get(fieldName);
    }

    @Override
    public boolean isWithValidationErrors() {
        return !DataUtils.isBlank(validationErrors);
    }

    @Override
    public List<FormMessage> getValidationErrors() {
        return validationErrors;
    }

    public void evaluateTabStates() throws UnifyException {
        revertTabStates();
        ValueStore formValueStore = getFormValueStore();
        final Date now = appletContext.getAu().getNow();
        if (formDef.isWithConsolidatedFormState()) {
            ConsolidatedFormStatePolicy policy = getAu().getComponent(ConsolidatedFormStatePolicy.class,
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
                                appletContext.getSpecialParamProvider(), now)
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
            editable = formTabDef.isEditable() | saveAsMode;
            disabled = formTabDef.isDisabled();
        }
    }

    @Override
    public String toString() {
        return "FormContext [appletContext=" + appletContext + ", entityDef=" + entityDef + ", formDef=" + formDef
                + ", formEventHandlers=" + formEventHandlers + ", formValueStore="
                + formValueStore + ", oldInst=" + oldInst + ", inst=" + inst + ", formTabs=" + formTabs
                + ", invalidFields=" + invalidFields + ", reviewErrors=" + reviewErrors + ", reviewErrorsByTab="
                + reviewErrorsByTab + ", validationErrors=" + validationErrors + ", formWidgetStateList="
                + formWidgetStateList + ", visibleAnnotations=" + visibleAnnotations + ", fixedReference="
                + fixedReference + ", focusMemoryId=" + focusMemoryId + ", focusWidgetId=" + focusWidgetId
                + ", tabMemoryId=" + tabMemoryId + ", tabWidgetId=" + tabWidgetId + ", tabWidgetIds="
                + Arrays.toString(tabWidgetIds) + ", tabIndexCounter=" + tabIndexCounter + ", formFocused="
                + formFocused + ", saveAsMode=" + saveAsMode + "]";
    }
}
