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

package com.flowcentraltech.flowcentral.application.web.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormWidget.FormSection;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.ReviewResult;
import com.flowcentraltech.flowcentral.common.data.AbstractContext;
import com.flowcentraltech.flowcentral.common.data.EntityAudit;
import com.flowcentraltech.flowcentral.common.data.EntityAuditInfo;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.flowcentraltech.flowcentral.common.data.FormStateRule;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage.Target;
import com.flowcentraltech.flowcentral.common.data.TargetFormState;
import com.flowcentraltech.flowcentral.common.data.TargetFormTabStates;
import com.flowcentraltech.flowcentral.common.data.ValidationErrors;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
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
 * @since 4.1
 */
public class FormContext extends AbstractContext implements ValidationErrors {

    public enum Mode {
        NORMAL,
        SAVE_AS,
        CRUD,
        QUICK_EDIT,
        PREVIEW_FORM
    }

    private AppletContext appletContext;

    private EntityDef parentEntityDef;

    private Entity parentInst;

    private EntityDef entityDef;

    private FormDef formDef;

    private EntityFormEventHandlers formEventHandlers;

    private FormTriggerEvaluator triggerEvaluator;

    private List<EventHandler> quickEditFormEventHandlers;

    private ValueStore formValueStore;

    private Object oldInst;

    private Object inst;

    private Map<String, FormTab> formTabs;

    private List<TargetFormMessage> reviewErrors;

    private Map<String, FormSection> mainFormSections;

    private Map<String, List<FormMessage>> reviewErrorsByTab;

    private List<FormWidgetState> formWidgetStateList;

    private FormValidationErrors formValidationErrors;

    private Set<String> visibleAnnotations;

    private EntityAudit entityAudit;

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

    private boolean updateEnabled;

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

        this.visibleAnnotations = new LinkedHashSet<String>();
        this.formValidationErrors = new FormValidationErrors();
        this.mode = Mode.NORMAL;
    }

    public AppletContext getAppletContext() {
        return appletContext;
    }

    public AppletUtilities au() {
        return appletContext.au();
    }

    public EntityAudit getEntityAudit() throws UnifyException {
        if (isSupportAudit()) {
            if (entityAudit == null) {
                EntityAuditInfo entityAuditInfo = au().getEntityAuditInfo(entityDef.getLongName());
                entityAudit = new EntityAudit(entityAuditInfo, (Entity) inst);
            } else {
                entityAudit.setEntity((Entity) inst);
            }
        }

        return entityAudit;
    }

    public boolean isSupportAudit() {
        return appletContext.isAuditingEnabled() && entityDef.isAuditable();
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

    public void setMainFormSections(Map<String, FormSection> mainFormSections) {
        this.mainFormSections = mainFormSections;
    }

    public List<EventHandler> getFormSwitchOnChangeHandlers() {
        if (formEventHandlers != null) {
            return isCrudMode() ? formEventHandlers.getCrudSwitchOnChangeHandlers()
                    : (isSaveAsMode() ? formEventHandlers.getSaveAsSwitchOnChangeHandlers()
                            : (isQuickEditMode() ? quickEditFormEventHandlers
                                    : formEventHandlers.getFormSwitchOnChangeHandlers()));
        }

        return Collections.emptyList();
    }

    public EntityFormEventHandlers getFormEventHandlers() {
        return formEventHandlers;
    }

    public void setInst(Object inst) throws UnifyException {
        appletContext.extractReference(entityDef, inst);
        this.inst = inst;
        altFormTitle = (formDef != null && isWithInst() && formDef.isWithTitleFormat())
                ? appletContext.specialParamProvider()
                        .getStringGenerator(getFormValueStore().getReader(), getFormValueStore().getReader(),
                                formDef.getTitleFormat())
                        .generate()
                : (inst instanceof Entity && ((Entity) inst).getId() == null
                        ? au().resolveSessionMessage("$m{form.newentity}",
                                au().resolveSessionMessage(entityDef.getLabel()))
                        : au().resolveSessionMessage("$m{form.maintainentity}",
                                au().resolveSessionMessage(entityDef.getLabel())));
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

    public boolean isQuickEditMode() {
        return Mode.QUICK_EDIT.equals(mode);
    }

    public boolean isPreviewFormMode() {
        return Mode.PREVIEW_FORM.equals(mode);
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

    public void setPreviewFormMode() {
        this.mode = Mode.PREVIEW_FORM;
    }

    public void setQuickEditMode(List<EventHandler> quickEditFormEventHandlers) {
        this.quickEditFormEventHandlers = quickEditFormEventHandlers;
        this.mode = Mode.QUICK_EDIT;
    }

    public ValueStore getFormValueStore() {
        if (inst != oldInst) {
            formValueStore = ValueStoreUtils.getValueStore(inst);
            oldInst = inst;
        }

        return formValueStore;
    }

    public String getEntityLongName() {
        return entityDef.getLongName();
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

    @Override
    public void addValidationError(String message) {
        formValidationErrors.addValidationError(message);
    }

    public void addValidationErrorMessages(List<String> messages) {
        if (messages != null && !messages.isEmpty()) {
            for (String _message : messages) {
                formValidationErrors.addValidationError(_message);
            }
        }
    }

    public void addValidationErrors(List<FormMessage> messages) {
        if (messages != null) {
            for (FormMessage message : messages) {
                addValidationError(message);
            }
        }
    }

    @Override
    public void addValidationError(FormMessage message) {
        formValidationErrors.addValidationError(message);
    }

    @Override
    public void addValidationError(Target target, String message) {
        formValidationErrors.addValidationError(target, message);
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

    public List<String> getFieldError(String fieldName) {
        return formValidationErrors.getFieldError(fieldName);
    }

    public boolean isWithSectionErrors() {
        return formValidationErrors.isWithSectionErrors();
    }

    public boolean isWithSectionError(String sectionName) {
        return formValidationErrors.isWithSectionError(sectionName);
    }

    public boolean isWithSectionError(Collection<String> sectionNames) {
        return formValidationErrors.isWithSectionError(sectionNames);
    }

    public List<String> getSectionError(String sectionName) {
        return formValidationErrors.getSectionError(sectionName);
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

    public boolean isWithInst() {
        return inst != null;
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
            FormMessage formMessage = message.getFormMessage();
            rrb.addRequired(formMessage.getMessage());

            String mainTabName = formDef.getFormTabDef(0).getName();
            if (message.isFieldTarget(mainTabName)) {
                addValidationError(formMessage);
            }
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

    public boolean isUpdateEnabled() {
        return updateEnabled;
    }

    public void setUpdateEnabled(boolean updateEnabled) {
        this.updateEnabled = updateEnabled;
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
                if (targetMessage.isFieldTarget(tabName)) {
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
        if (isWithInst()) {
            final ValueStore formValueStore = getFormValueStore();
            final ValueStoreReader formValueStoreReader = formValueStore.getReader();
            final Date now = appletContext.au().getNow();
            if (formDef.isWithConsolidatedFormState()) {
                ConsolidatedFormStatePolicy policy = au().getComponent(ConsolidatedFormStatePolicy.class,
                        formDef.getConsolidatedFormState());
                String trigger = triggerEvaluator != null ? triggerEvaluator.evaluateTrigger() : null;
                TargetFormTabStates states = policy.evaluateTabStates(formValueStoreReader, trigger);
                if (states.isWithValueList()) {
                    states.applyValues(formValueStore);
                }

                for (TargetFormState state : states.getTargetStateList()) {
                    for (String target : state.getTarget()) {
                        FormTab tb = formTabs.get(target);
                        if (tb != null) {
                            tb.applyStatePolicy(state);
                        }
                    }
                }
            }

            for (FormAnnotationDef formAnnotationDef : formDef.getDirectFormAnnotationDefList()) {
                ObjectFilter objectFilter = formAnnotationDef.isWithCondition()
                        ? formAnnotationDef.getOnCondition().getObjectFilter(entityDef, formValueStoreReader, now)
                        : null;
                if (objectFilter == null || objectFilter.matchReader(formValueStoreReader)) {
                    visibleAnnotations.add(formAnnotationDef.getName());
                }
            }

            for (FormStatePolicyDef formStatePolicyDef : formDef.getOnSwitchFormStatePolicyDefList()) {
                if (formStatePolicyDef.isTriggered("")) {
                    ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                            ? formStatePolicyDef.getOnCondition().getObjectFilter(entityDef, formValueStoreReader, now)
                            : null;
                    if (objectFilter == null || objectFilter.matchReader(formValueStoreReader)) {
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
    }

    public void revertTabStates() throws UnifyException {
        for (FormTab formTab : formTabs.values()) {
            formTab.revertState();
        }

        visibleAnnotations.clear();
        formFocused = false;
    }

    public boolean isVisibleMainSection(String sectionName) {
        if (isQuickEditMode()) {
            return true;
        }

        if (mainFormSections != null) {
            FormSection formSection = mainFormSections.get(sectionName);
            return formSection != null && formSection.isVisible();
        }

        return false;
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

        String getSectionName();

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
