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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.FormAnnotationType;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.flowcentraltech.flowcentral.configuration.constants.FormStatePolicyType;
import com.flowcentraltech.flowcentral.configuration.constants.FormType;
import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.constants.UIActionType;
import com.flowcentraltech.flowcentral.configuration.constants.VisibilityType;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Form definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormDef extends BaseApplicationEntityDef {

    private FormType type;

    private EntityDef entityDef;

    private FormTabDef saveAsFormTabDef;

    private List<FormAnnotationDef> formAnnotationDefList;

    private List<FormAnnotationDef> directFormAnnotationDefList;

    private List<FormActionDef> formActionDefList;

    private List<FormTabDef> formTabDefList;

    private List<FormRelatedListDef> formRelatedListDefList;

    private List<FieldValidationPolicyDef> fieldValidationPolicyDefList;

    private List<FormValidationPolicyDef> formValidationPolicyDefList;

    private Map<FormReviewType, List<FormReviewPolicyDef>> formReviewPolicyDefs;

    private List<FormStatePolicyDef> formStatePolicyDefList;

    private List<FormStatePolicyDef> onCreateFormStatePolicyDefList;

    private List<FormStatePolicyDef> onSwitchFormStatePolicyDefList;

    private List<FormStatePolicyDef> onDelayedSetValuesFormStatePolicyDefList;

    private List<FormStatePolicyDef> onFormConstructSetValuesFormStatePolicyDefList;

    private List<FormWidgetRulesPolicyDef> formWidgetRulesPolicyDefList;

    private Map<String, FormStatePolicyDef> onCreateFormStatePolicyDefMap;

    private Map<String, FormFilterDef> filterDefMap;

    private Map<String, FormAnnotationDef> formAnnotationDefMap;

    private Map<String, FormActionDef> formActionDefMap;

    private Map<String, FormRelatedListDef> formRelatedListDefMap;

    private List<StringToken> titleFormat;

    private String consolidatedFormValidation;

    private String consolidatedFormReview;

    private String consolidatedFormState;

    private String listingGenerator;

    private boolean childOrChildListTabs;

    private FormDef(FormType type, EntityDef entityDef, String consolidatedFormValidation,
            String consolidatedFormReview, String consolidatedFormState, String listingGenerator,
            List<StringToken> titleFormat, Map<String, FormFilterDef> filterDefMap,
            Map<String, FormAnnotationDef> formAnnotationDefMap, List<FormActionDef> formActionDefList,
            List<FormTabDef> formTabDefList, List<FormRelatedListDef> formRelatedListDefList,
            List<FormStatePolicyDef> formStatePolicyDefList,
            List<FormWidgetRulesPolicyDef> formWidgetRulesPolicyDefList,
            List<FieldValidationPolicyDef> fieldValidationPolicyDefList,
            List<FormValidationPolicyDef> formValidationPolicyDefList,
            List<FormReviewPolicyDef> formReviewPolicyDefList, ApplicationEntityNameParts nameParts, String description,
            Long id, long version) {
        super(nameParts, description, id, version);
        this.type = type;
        this.entityDef = entityDef;
        this.consolidatedFormValidation = consolidatedFormValidation;
        this.consolidatedFormReview = consolidatedFormReview;
        this.consolidatedFormState = consolidatedFormState;
        this.listingGenerator = listingGenerator;
        this.titleFormat = titleFormat;
        this.filterDefMap = filterDefMap;
        this.formAnnotationDefMap = formAnnotationDefMap;
        this.formActionDefList = formActionDefList;
        this.formTabDefList = formTabDefList;
        this.formRelatedListDefList = formRelatedListDefList;
        this.fieldValidationPolicyDefList = fieldValidationPolicyDefList;
        this.formValidationPolicyDefList = formValidationPolicyDefList;
        this.formWidgetRulesPolicyDefList = formWidgetRulesPolicyDefList;

        // Form review policies
        this.formReviewPolicyDefs = new HashMap<FormReviewType, List<FormReviewPolicyDef>>();
        for (FormReviewPolicyDef formReviewPolicyDef : formReviewPolicyDefList) {
            for (FormReviewType reviewType : FormReviewType.values()) {
                List<FormReviewPolicyDef> list = this.formReviewPolicyDefs.get(reviewType);
                if (list == null) {
                    list = new ArrayList<FormReviewPolicyDef>();
                    this.formReviewPolicyDefs.put(reviewType, list);
                }

                if (formReviewPolicyDef.supports(reviewType)) {
                    list.add(formReviewPolicyDef);
                }
            }
        }

        for (FormReviewType reviewType : FormReviewType.values()) {
            this.formReviewPolicyDefs.put(reviewType,
                    DataUtils.unmodifiableList(this.formReviewPolicyDefs.get(reviewType)));
        }

        // Form state policies
        this.formStatePolicyDefList = formStatePolicyDefList;
        if (!formStatePolicyDefList.isEmpty()) {
            this.onSwitchFormStatePolicyDefList = new ArrayList<FormStatePolicyDef>();
            this.onDelayedSetValuesFormStatePolicyDefList = new ArrayList<FormStatePolicyDef>();
            this.onFormConstructSetValuesFormStatePolicyDefList = new ArrayList<FormStatePolicyDef>();
            this.onCreateFormStatePolicyDefMap = new LinkedHashMap<String, FormStatePolicyDef>();
            for (FormStatePolicyDef formStatePolicyDef : formStatePolicyDefList) {
                switch (formStatePolicyDef.getType()) {
                    case ON_CREATE:
                        this.onCreateFormStatePolicyDefMap.put(formStatePolicyDef.getName(), formStatePolicyDef);
                        break;
                    case ON_DELAYED_SET_VALUES:
                        this.onDelayedSetValuesFormStatePolicyDefList.add(formStatePolicyDef);
                        break;
                    case ON_FORM_CONSTRUCT_SET_VALUES:
                        this.onFormConstructSetValuesFormStatePolicyDefList.add(formStatePolicyDef);
                        break;
                    case ON_SWITCH:
                        this.onSwitchFormStatePolicyDefList.add(formStatePolicyDef);
                        break;
                    default:
                        break;

                }
            }

            this.onSwitchFormStatePolicyDefList = DataUtils.unmodifiableList(this.onSwitchFormStatePolicyDefList);
            this.onDelayedSetValuesFormStatePolicyDefList = DataUtils
                    .unmodifiableList(this.onDelayedSetValuesFormStatePolicyDefList);
            this.onFormConstructSetValuesFormStatePolicyDefList = DataUtils
                    .unmodifiableList(this.onFormConstructSetValuesFormStatePolicyDefList);
        } else {
            this.onSwitchFormStatePolicyDefList = Collections.emptyList();
            this.onDelayedSetValuesFormStatePolicyDefList = Collections.emptyList();
            this.onFormConstructSetValuesFormStatePolicyDefList = Collections.emptyList();
            this.onCreateFormStatePolicyDefMap = Collections.emptyMap();
        }

        if (!formActionDefList.isEmpty()) {
            this.formActionDefMap = new HashMap<String, FormActionDef>();
            for (FormActionDef formActionDef : formActionDefList) {
                this.formActionDefMap.put(formActionDef.getName(), formActionDef);
            }

        } else {
            this.formActionDefMap = Collections.emptyMap();
        }

        if (!formRelatedListDefList.isEmpty()) {
            this.formRelatedListDefMap = new HashMap<String, FormRelatedListDef>();
            for (FormRelatedListDef formRelatedListDef : formRelatedListDefList) {
                this.formRelatedListDefMap.put(formRelatedListDef.getName(), formRelatedListDef);
            }
        } else {
            this.formRelatedListDefMap = Collections.emptyMap();
        }

        for (FormTabDef formTabDef : formTabDefList) {
            if (formTabDef.isChildOrChildList()) {
                childOrChildListTabs = true;
                break;
            }
        }
    }

    public FormType getType() {
        return type;
    }

    public boolean isInputForm() {
        return type.isInputForm();
    }

    public boolean isListingForm() {
        return type.isListingForm();
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public List<StringToken> getTitleFormat() {
        return titleFormat;
    }

    public boolean isWithTitleFormat() {
        return titleFormat != null;
    }

    public boolean isWithConsolidatedFormValidation() {
        return !StringUtils.isBlank(consolidatedFormValidation);
    }

    public String getConsolidatedFormValidation() {
        return consolidatedFormValidation;
    }

    public boolean isWithConsolidatedFormReview() {
        return !StringUtils.isBlank(consolidatedFormReview);
    }

    public String getConsolidatedFormReview() {
        return consolidatedFormReview;
    }

    public boolean isWithConsolidatedFormState() {
        return !StringUtils.isBlank(consolidatedFormState);
    }

    public String getConsolidatedFormState() {
        return consolidatedFormState;
    }

    public String getListingGenerator() {
        return listingGenerator;
    }

    public FormTabDef getSaveAsFormTabDef() {
        if (saveAsFormTabDef == null) {
            FormTabDef primaryFormTabDef = formTabDefList.get(0);
            List<FormSectionDef> formSectionDefList = new ArrayList<FormSectionDef>();
            for (FormSectionDef formSectionDef : primaryFormTabDef.getFormSectionDefList()) {
                List<FormFieldDef> formFieldDefList = new ArrayList<FormFieldDef>();
                for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                    if (formFieldDef.isSaveAs()) {
                        if ("applicationId".equals(formFieldDef.getFieldName())) {
                            formFieldDefList.add(new FormFieldDef(formFieldDef, 0, formFieldDef.isRequired(),
                                    formFieldDef.isVisible(), true, false));
                        } else {
                            formFieldDefList.add(new FormFieldDef(formFieldDef, 0));
                        }
                    }
                }

                if (!formFieldDefList.isEmpty()) {
                    formSectionDefList
                            .add(new FormSectionDef(formSectionDef, formFieldDefList, FormColumnsType.TYPE_1));
                }
            }
            saveAsFormTabDef = new FormTabDef(primaryFormTabDef, formSectionDefList);
        }

        return saveAsFormTabDef;
    }

    public boolean isFilter(String name) {
        return filterDefMap.containsKey(name);
    }

    public FormFilterDef getFilterDef(String name) {
        FormFilterDef filterDef = filterDefMap.get(name);
        if (filterDef == null) {
            throw new RuntimeException(
                    "Filter with name [" + name + "] is unknown for workflow definition [" + getName() + "].");
        }

        return filterDef;
    }

    public List<FormAnnotationDef> getFormAnnotationDefList() {
        if (formAnnotationDefList == null) {
            synchronized (this) {
                if (formAnnotationDefList == null) {
                    formAnnotationDefList = Collections
                            .unmodifiableList(new ArrayList<FormAnnotationDef>(formAnnotationDefMap.values()));
                }
            }
        }

        return formAnnotationDefList;
    }

    public List<FormAnnotationDef> getDirectFormAnnotationDefList() {
        if (directFormAnnotationDefList == null) {
            synchronized (this) {
                if (directFormAnnotationDefList == null) {
                    directFormAnnotationDefList = new ArrayList<FormAnnotationDef>();
                    for (FormAnnotationDef formAnnotationDef : getFormAnnotationDefList()) {
                        if (formAnnotationDef.isDirectPlacement()) {
                            directFormAnnotationDefList.add(formAnnotationDef);
                        }
                    }
                    directFormAnnotationDefList = Collections.unmodifiableList(directFormAnnotationDefList);
                }
            }
        }

        return directFormAnnotationDefList;
    }

    public Map<String, FormAnnotationDef> getFormAnnotationDefs() {
        return formAnnotationDefMap;
    }

    public List<FormActionDef> getFormActionDefList() {
        return formActionDefList;
    }

    public List<FormTabDef> getFormTabDefList() {
        return formTabDefList;
    }

    public List<FormRelatedListDef> getFormRelatedListDefList() {
        return formRelatedListDefList;
    }

    public List<FormStatePolicyDef> getFormStatePolicyDefList() {
        return formStatePolicyDefList;
    }

    public List<FormWidgetRulesPolicyDef> getFormWidgetRulesPolicyDefList() {
        return formWidgetRulesPolicyDefList;
    }

    public boolean isWithFormWidgetRulesPolicy() {
        return !formWidgetRulesPolicyDefList.isEmpty();
    }

    public List<FormReviewPolicyDef> getFormReviewPolicies(FormReviewType type) {
        return formReviewPolicyDefs.get(type);
    }

    public boolean isChildOrChildListTabs() {
        return childOrChildListTabs;
    }

    public List<FormStatePolicyDef> getOnCreateFormStatePolicyDefList() throws UnifyException {
        if (onCreateFormStatePolicyDefList == null) {
            synchronized (this) {
                if (onCreateFormStatePolicyDefList == null) {
                    if (!onCreateFormStatePolicyDefMap.isEmpty()) {
                        onCreateFormStatePolicyDefList = new ArrayList<FormStatePolicyDef>();
                        for (FormStatePolicyDef formStatePolicyDef : onCreateFormStatePolicyDefMap.values()) {
                            onCreateFormStatePolicyDefList.add(formStatePolicyDef);
                        }

                        DataUtils.sortAscending(onCreateFormStatePolicyDefList, FormStatePolicyDef.class,
                                "description");
                        onCreateFormStatePolicyDefList = Collections.unmodifiableList(onCreateFormStatePolicyDefList);
                    } else {
                        onCreateFormStatePolicyDefList = Collections.emptyList();
                    }
                }
            }
        }

        return onCreateFormStatePolicyDefList;
    }

    public List<FormStatePolicyDef> getOnSwitchFormStatePolicyDefList() {
        return onSwitchFormStatePolicyDefList;
    }

    public List<FormStatePolicyDef> getOnDelayedSetValuesFormStatePolicyDefList() {
        return onDelayedSetValuesFormStatePolicyDefList;
    }

    public List<FormStatePolicyDef> getOnFormConstructSetValuesFormStatePolicyDefList() {
        return onFormConstructSetValuesFormStatePolicyDefList;
    }

    public List<FieldValidationPolicyDef> getFieldValidationPolicies() {
        return fieldValidationPolicyDefList;
    }

    public boolean isWithFieldValidationPolicy() {
        return !fieldValidationPolicyDefList.isEmpty();
    }

    public List<FormValidationPolicyDef> getFormValidationPolicies() {
        return formValidationPolicyDefList;
    }

    public boolean isWithFormValidationPolicy() {
        return !formValidationPolicyDefList.isEmpty();
    }

    public FormStatePolicyDef getOnCreateFormStatePolicyDef(String name) {
        FormStatePolicyDef formStatePolicyDef = onCreateFormStatePolicyDefMap.get(name);
        if (formStatePolicyDef == null) {
            throw new RuntimeException("On-create form state policy definition with name [" + name
                    + "] is unknown for form definition [" + getName() + "].");
        }

        return formStatePolicyDef;
    }

    public FormAnnotationDef getFormAnnotationDef(String name) {
        FormAnnotationDef formAnnotationDef = formAnnotationDefMap.get(name);
        if (formAnnotationDef == null) {
            throw new RuntimeException("Form annotation definition with name [" + name
                    + "] is unknown for form definition [" + getName() + "].");
        }

        return formAnnotationDef;
    }

    public FormActionDef getFormActionDef(String name) {
        FormActionDef formActionDef = formActionDefMap.get(name);
        if (formActionDef == null) {
            throw new RuntimeException("Form action definition with name [" + name
                    + "] is unknown for form definition [" + getName() + "].");
        }

        return formActionDef;
    }

    public FormRelatedListDef getFormRelatedListDef(String name) {
        FormRelatedListDef formRelatedListDef = formRelatedListDefMap.get(name);
        if (formRelatedListDef == null) {
            throw new RuntimeException("Related list definition with name [" + name
                    + "] is unknown for form definition [" + getName() + "].");
        }

        return formRelatedListDef;
    }

    public int getTabCount() {
        return formTabDefList.size();
    }

    public FormTabDef getFormTabDef(int tabIndex) {
        return formTabDefList.get(tabIndex);
    }

    public static Builder newBuilder(FormType type, EntityDef entityDef, String consolidatedFormValidation,
            String consolidatedFormReview, String consolidatedFormState, String listingGenerator, String longName,
            String description, Long id, long version) {
        return new Builder(type, entityDef, consolidatedFormValidation, consolidatedFormReview, consolidatedFormState,
                listingGenerator, longName, description, id, version);
    }

    public static class Builder {

        private FormType type;

        private EntityDef entityDef;

        private String consolidatedFormValidation;

        private String consolidatedFormReview;

        private String consolidatedFormState;

        private String listingGenerator;

        private Map<String, FormFilterDef> filterDefMap;

        private Map<String, FormAnnotationDef> formAnnotationDefMap;

        private List<FormActionDef> formActionList;

        private List<TempFormTabDef> formTabDefList;

        private Map<String, FormRelatedListDef> formRelatedListDefList;

        private Map<String, FormStatePolicyDef> fieldStatePolicyDefList;

        private Map<String, FormWidgetRulesPolicyDef> formWidgetRulesPolicyDefList;

        private Map<String, FieldValidationPolicyDef> fieldValidationPolicyDefList;

        private Map<String, FormValidationPolicyDef> formValidationPolicyDefList;

        private Map<String, FormReviewPolicyDef> formReviewPolicyDefList;

        private List<StringToken> titleFormat;

        private Set<String> actionNames;

        private Set<String> tabLabels;

        private Set<String> fieldNames;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(FormType type, EntityDef entityDef, String consolidatedFormValidation,
                String consolidatedFormReview, String consolidatedFormState, String listingGenerator, String longName,
                String description, Long id, long version) {
            this.type = type;
            this.entityDef = entityDef;
            this.consolidatedFormValidation = consolidatedFormValidation;
            this.consolidatedFormReview = consolidatedFormReview;
            this.consolidatedFormState = consolidatedFormState;
            this.filterDefMap = new HashMap<String, FormFilterDef>();
            this.listingGenerator = listingGenerator;
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
            formTabDefList = new ArrayList<TempFormTabDef>();
            fieldNames = new HashSet<String>();
            actionNames = new HashSet<String>();
            tabLabels = new HashSet<String>();
        }

        public Builder titleFormat(List<StringToken> titleFormat) {
            this.titleFormat = titleFormat;
            return this;
        }

        public Builder addFormAnnotation(FormAnnotationType type, VisibilityType visibility, String name,
                String description, String message, boolean html, boolean directPlacement, FilterDef onCondition) {
            if (formAnnotationDefMap == null) {
                formAnnotationDefMap = new LinkedHashMap<String, FormAnnotationDef>();
            }

            if (formAnnotationDefMap.containsKey(name)) {
                throw new RuntimeException(
                        "Annotation with name [" + name + "] already exists on this form[" + longName + "].");
            }

            formAnnotationDefMap.put(name, new FormAnnotationDef(type, visibility, name, description, message, html,
                    directPlacement, onCondition));
            return this;
        }

        public Builder addFormAnnotation(FormAnnotationDef formAnnotationDef) {
            if (formAnnotationDefMap == null) {
                formAnnotationDefMap = new LinkedHashMap<String, FormAnnotationDef>();
            }

            if (formAnnotationDefMap.containsKey(formAnnotationDef.getName())) {
                throw new RuntimeException("Annotation with name [" + formAnnotationDef.getName()
                        + "] already exists on this form[" + longName + "].");
            }

            formAnnotationDefMap.put(formAnnotationDef.getName(), formAnnotationDef);
            return this;
        }

        public Builder addFormAction(UIActionType type, HighlightType highlightType, String name, String description,
                String label, String symbol, String styleClass, String policy, int orderIndex, boolean showOnCreate,
                boolean showOnMaintain, boolean validateForm, FilterDef onCondition) {
            if (actionNames.contains(name)) {
                throw new RuntimeException(
                        "Action with name [" + name + "] already exists on this form[" + longName + "].");
            }

            if (formActionList == null) {
                formActionList = new ArrayList<FormActionDef>();
            }

            formActionList.add(new FormActionDef(type, highlightType, name, description, label, symbol, styleClass,
                    policy, orderIndex, showOnCreate, showOnMaintain, validateForm, onCondition));
            return this;
        }

        public Builder addFormAction(FormActionDef formActionDef) {
            if (actionNames.contains(formActionDef.getName())) {
                throw new RuntimeException("Action with name [" + formActionDef.getName()
                        + "] already exists on this form[" + longName + "].");
            }

            if (formActionList == null) {
                formActionList = new ArrayList<FormActionDef>();
            }

            formActionList.add(formActionDef);
            return this;
        }

        public Builder addFormTab(TabContentType contentType, String name, String tabLabel,
                boolean isIgnoreParentCondition, boolean showSearch, boolean quickEdit, boolean quickOrder,
                boolean visible, boolean editable, boolean disabled) {
            return addFormTab(contentType, null, name, tabLabel, null, null, null, null, null, null, null, null,
                    isIgnoreParentCondition, showSearch, quickEdit, quickOrder, visible, editable, disabled);
        }

        public Builder addFormTab(TabContentType contentType, FilterGroupDef filterGroupDef, String name,
                String tabLabel, String tabApplet, String tabReference, String mappedFieldName, String tabMappedForm,
                String editAction, String editViewOnly, String editAllowAddition, String editFixedRows,
                boolean isIgnoreParentCondition, boolean showSearch, boolean quickEdit, boolean quickOrder,
                boolean visible, boolean editable, boolean disabled) {
            if (tabLabels.contains(name)) {
                throw new RuntimeException("Tab with name [" + name + "] already exists on this form.");
            }

            formTabDefList.add(new TempFormTabDef(contentType, filterGroupDef, name, tabLabel, tabApplet, tabReference,
                    mappedFieldName, tabMappedForm, editAction, editViewOnly, editAllowAddition, editFixedRows,
                    isIgnoreParentCondition, showSearch, quickEdit, quickOrder, visible, editable, disabled));
            tabLabels.add(tabLabel);
            return this;
        }

        public Builder addFormSection(int tabIndex, String name, String sectionLabel, FormColumnsType columns,
                boolean visible, boolean editable, boolean disabled) {
            checkTabIndex(tabIndex);
            formTabDefList.get(tabIndex).addFormSectionDef(sectionLabel, name, columns, visible, editable, disabled);
            return this;
        }

        public Builder addFormField(int tabIndex, int sectionIndex, EntityFieldDef entityFieldDef,
                WidgetTypeDef widgetTypeDef, RefDef inputRefDef, String label, String renderer, int column,
                boolean switchOnChange, boolean saveAs, boolean required, boolean visible, boolean editable,
                boolean disabled) {
            checkTabIndex(tabIndex);

            TempFormTabDef tempFormTabDef = formTabDefList.get(tabIndex);
            if (sectionIndex < 0 || sectionIndex >= tempFormTabDef.getFormSectionDefList().size()) {
                throw new RuntimeException("Tab with index [" + tabIndex + "] and section index [" + sectionIndex
                        + "] does not exist on this form.");
            }

            if (fieldNames.contains(entityFieldDef.getFieldName())) {
                throw new RuntimeException("Field with name [" + entityFieldDef.getFieldName()
                        + "] already exists on this form[" + longName + "].");
            }

            TempFormSectionDef tempFormSectionDef = tempFormTabDef.getFormSectionDefList().get(sectionIndex);
            if (column >= tempFormSectionDef.getColumns().columns()) {
                throw new RuntimeException("Column with index [" + column + "] does not exist in section ["
                        + sectionIndex + "] and tab with index [" + tabIndex + "].");
            }

            tempFormSectionDef.addFormFieldDef(entityFieldDef, widgetTypeDef, inputRefDef, label, renderer, column,
                    switchOnChange, saveAs, required, visible, editable, disabled);
            fieldNames.add(entityFieldDef.getFieldName());
            return this;
        }

        public Builder addRelatedList(FilterGroupDef filterGroupDef, String name, String description, String label,
                String appletName, String editAction) {
            if (formRelatedListDefList == null) {
                formRelatedListDefList = new LinkedHashMap<String, FormRelatedListDef>();
            }

            if (formRelatedListDefList.containsKey(name)) {
                throw new RuntimeException(
                        "Related list with name [" + name + "] already exists on this form[" + longName + "].");
            }

            formRelatedListDefList.put(name,
                    new FormRelatedListDef(filterGroupDef, name, description, label, appletName, editAction));
            return this;
        }

        public Builder addRelatedList(FormRelatedListDef formRelatedListDef) {
            if (formRelatedListDefList == null) {
                formRelatedListDefList = new LinkedHashMap<String, FormRelatedListDef>();
            }

            if (formRelatedListDefList.containsKey(formRelatedListDef.getName())) {
                throw new RuntimeException("Related list with name [" + formRelatedListDef.getName()
                        + "] already exists on this form[" + longName + "].");
            }

            formRelatedListDefList.put(formRelatedListDef.getName(), formRelatedListDef);
            return this;
        }

        public Builder addFormWidgetRulesPolicy(String name, String description, FilterDef onCondition,
                WidgetRulesDef widgetRulesDef, Map<String, String> ruleEditors) {
            if (formWidgetRulesPolicyDefList == null) {
                formWidgetRulesPolicyDefList = new LinkedHashMap<String, FormWidgetRulesPolicyDef>();
            }

            if (formWidgetRulesPolicyDefList.containsKey(name)) {
                throw new RuntimeException(
                        "Widget rules policy with name [" + name + "] already exists on this form[" + longName + "].");
            }

            formWidgetRulesPolicyDefList.put(name,
                    new FormWidgetRulesPolicyDef(name, description, onCondition, widgetRulesDef, ruleEditors));
            return this;
        }

        public Builder addFormStatePolicy(String name, String description, FormStatePolicyType type,
                FilterDef onCondition, SetStatesDef setStatesDef, SetValuesDef setValuesDef, List<String> triggerList) {
            if (fieldStatePolicyDefList == null) {
                fieldStatePolicyDefList = new LinkedHashMap<String, FormStatePolicyDef>();
            }

            if (fieldStatePolicyDefList.containsKey(name)) {
                throw new RuntimeException(
                        "Field state policy with name [" + name + "] already exists on this form[" + longName + "].");
            }

            fieldStatePolicyDefList.put(name, new FormStatePolicyDef(name, description, type, onCondition, setStatesDef,
                    setValuesDef, triggerList));
            return this;
        }

        public Builder addFormStatePolicy(FormStatePolicyDef formStatePolicyDef) {
            if (fieldStatePolicyDefList == null) {
                fieldStatePolicyDefList = new LinkedHashMap<String, FormStatePolicyDef>();
            }

            if (fieldStatePolicyDefList.containsKey(formStatePolicyDef.getName())) {
                throw new RuntimeException("Field state policy with name [" + formStatePolicyDef.getName()
                        + "] already exists on this form[" + longName + "].");
            }

            fieldStatePolicyDefList.put(formStatePolicyDef.getName(), formStatePolicyDef);
            return this;
        }

        public Builder addFieldValidationPolicy(String name, String description, String fieldName, String validator,
                String rule) {
            if (fieldValidationPolicyDefList == null) {
                fieldValidationPolicyDefList = new LinkedHashMap<String, FieldValidationPolicyDef>();
            }

            if (fieldValidationPolicyDefList.containsKey(name)) {
                throw new RuntimeException("Field validation policy with name [" + name
                        + "] already exists on this form[" + longName + "].");
            }

            fieldValidationPolicyDefList.put(name,
                    new FieldValidationPolicyDef(name, description, fieldName, validator, rule));
            return this;
        }

        public Builder addFieldValidationPolicy(FieldValidationPolicyDef fieldValidationPolicyDef) {
            if (fieldValidationPolicyDefList == null) {
                fieldValidationPolicyDefList = new LinkedHashMap<String, FieldValidationPolicyDef>();
            }

            if (fieldValidationPolicyDefList.containsKey(fieldValidationPolicyDef.getName())) {
                throw new RuntimeException("Field validation policy with name [" + fieldValidationPolicyDef.getName()
                        + "] already exists on this form[" + longName + "].");
            }

            fieldValidationPolicyDefList.put(fieldValidationPolicyDef.getName(), fieldValidationPolicyDef);
            return this;
        }

        public Builder addFormValidationPolicy(FilterDef errorCondition, String name, String description,
                String message, String errorMatcher, List<String> targetList) {
            if (formValidationPolicyDefList == null) {
                formValidationPolicyDefList = new LinkedHashMap<String, FormValidationPolicyDef>();
            }

            if (formValidationPolicyDefList.containsKey(name)) {
                throw new RuntimeException("Form validation policy with name [" + name
                        + "] already exists on this form[" + longName + "].");
            }

            formValidationPolicyDefList.put(name,
                    new FormValidationPolicyDef(errorCondition, name, description, message, errorMatcher, targetList));
            return this;
        }

        public Builder addFormValidationPolicy(FormValidationPolicyDef formValidationPolicyDef) {
            if (formValidationPolicyDefList == null) {
                formValidationPolicyDefList = new LinkedHashMap<String, FormValidationPolicyDef>();
            }

            if (formValidationPolicyDefList.containsKey(formValidationPolicyDef.getName())) {
                throw new RuntimeException("Form validation policy with name [" + formValidationPolicyDef.getName()
                        + "] already exists on this form[" + longName + "].");
            }

            formValidationPolicyDefList.put(formValidationPolicyDef.getName(), formValidationPolicyDef);
            return this;
        }

        public Builder addFormReviewPolicy(List<FormReviewType> reviewTypeList, FilterDef errorCondition, String name,
                String description, MessageType messageType, String message, String errorMatcher,
                List<String> targetList, boolean skippable) {
            if (formReviewPolicyDefList == null) {
                formReviewPolicyDefList = new LinkedHashMap<String, FormReviewPolicyDef>();
            }

            if (formReviewPolicyDefList.containsKey(name)) {
                throw new RuntimeException(
                        "Form review policy with name [" + name + "] already exists on this form[" + longName + "].");
            }

            formReviewPolicyDefList.put(name, new FormReviewPolicyDef(reviewTypeList, errorCondition, name, description,
                    messageType, message, errorMatcher, targetList, skippable));
            return this;
        }

        public Builder addFilterDef(FormFilterDef filterDef) {
            if (filterDefMap.containsKey(filterDef.getName())) {
                throw new RuntimeException(
                        "Filter with name [" + filterDef.getName() + "] already exists in this definition.");
            }

            filterDefMap.put(filterDef.getName(), filterDef);
            return this;
        }

        public FormDef build() throws UnifyException {
            List<FormTabDef> formTabDefList = new ArrayList<FormTabDef>();
            for (TempFormTabDef tempFormTabDef : this.formTabDefList) {
                List<FormSectionDef> formSectionDefList = new ArrayList<FormSectionDef>();
                for (TempFormSectionDef tempFormSectionDef : tempFormTabDef.getFormSectionDefList()) {
                    formSectionDefList.add(
                            new FormSectionDef(DataUtils.unmodifiableList(tempFormSectionDef.getFormFieldDefList()),
                                    tempFormSectionDef.getName(), tempFormSectionDef.getSectionLabel(),
                                    tempFormSectionDef.getColumns(), tempFormSectionDef.isVisible(),
                                    tempFormSectionDef.isEditable(), tempFormSectionDef.isDisabled()));
                }

                formTabDefList.add(new FormTabDef(tempFormTabDef.getContentType(), tempFormTabDef.getFilterGroupDef(),
                        tempFormTabDef.getName(), tempFormTabDef.getTabLabel(), tempFormTabDef.getTabApplet(),
                        tempFormTabDef.getTabReference(), tempFormTabDef.getMappedFieldName(),
                        tempFormTabDef.getTabMappedForm(), tempFormTabDef.getEditAction(),
                        tempFormTabDef.getEditViewOnly(), tempFormTabDef.getEditAllowAddition(),
                        tempFormTabDef.getEditFixedRows(), DataUtils.unmodifiableList(formSectionDefList),
                        tempFormTabDef.isIgnoreParentCondition(), tempFormTabDef.isShowSearch(),
                        tempFormTabDef.isQuickEdit(), tempFormTabDef.isQuickOrder(), tempFormTabDef.isVisible(),
                        tempFormTabDef.isEditable(), tempFormTabDef.isDisabled()));
            }

            if (formActionList != null) {
                DataUtils.sortAscending(formActionList, FormActionDef.class, "orderIndex");
            }

            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            return new FormDef(type, entityDef, consolidatedFormValidation, consolidatedFormReview,
                    consolidatedFormState, listingGenerator, titleFormat, DataUtils.unmodifiableMap(filterDefMap),
                    DataUtils.unmodifiableMap(formAnnotationDefMap), DataUtils.unmodifiableList(formActionList),
                    DataUtils.unmodifiableList(formTabDefList),
                    DataUtils.unmodifiableValuesList(formRelatedListDefList),
                    DataUtils.unmodifiableValuesList(fieldStatePolicyDefList),
                    DataUtils.unmodifiableValuesList(formWidgetRulesPolicyDefList),
                    DataUtils.unmodifiableValuesList(fieldValidationPolicyDefList),
                    DataUtils.unmodifiableValuesList(formValidationPolicyDefList),
                    DataUtils.unmodifiableValuesList(formReviewPolicyDefList), nameParts, description, id, version);
        }

        private void checkTabIndex(int tabIndex) {
            if (tabIndex < 0 || tabIndex >= formTabDefList.size()) {
                throw new RuntimeException("Tab with index [" + tabIndex + "] does not exist on this form.");
            }
        }

        private class TempFormTabDef {

            private TabContentType contentType;

            private FilterGroupDef filterGroupDef;

            private String name;

            private String tabLabel;

            private String tabApplet;

            private String tabReference;

            private String mappedFieldName;

            private String tabMappedForm;

            private String editAction;

            private String editViewOnly;

            private String editAllowAddition;

            private String editFixedRows;

            private boolean ignoreParentCondition;

            private boolean showSearch;

            private boolean quickEdit;

            private boolean quickOrder;

            private boolean visible;

            private boolean editable;

            private boolean disabled;

            private List<TempFormSectionDef> formSectionDefList;

            public TempFormTabDef(TabContentType contentType, FilterGroupDef filterGroupDef, String name,
                    String tabLabel, String tabApplet, String tabReference, String mappedFieldName,
                    String tabMappedForm, String editAction, String editViewOnly, String editAllowAddition,
                    String editFixedRows, boolean ignoreParentCondition, boolean showSearch, boolean quickEdit,
                    boolean quickOrder, boolean visible, boolean editable, boolean disabled) {
                this.contentType = contentType;
                this.filterGroupDef = filterGroupDef;
                this.name = name;
                this.tabLabel = tabLabel;
                this.tabApplet = tabApplet;
                this.tabReference = tabReference;
                this.mappedFieldName = mappedFieldName;
                this.tabMappedForm = tabMappedForm;
                this.editAction = editAction;
                this.editViewOnly = editViewOnly;
                this.editAllowAddition = editAllowAddition;
                this.editFixedRows = editFixedRows;
                this.ignoreParentCondition = ignoreParentCondition;
                this.showSearch = showSearch;
                this.quickEdit = quickEdit;
                this.quickOrder = quickOrder;
                this.visible = visible;
                this.editable = editable;
                this.disabled = disabled;
                this.formSectionDefList = new ArrayList<TempFormSectionDef>();
            }

            public TabContentType getContentType() {
                return contentType;
            }

            public final FilterGroupDef getFilterGroupDef() {
                return filterGroupDef;
            }

            public String getName() {
                return name;
            }

            public String getTabLabel() {
                return tabLabel;
            }

            public String getTabApplet() {
                return tabApplet;
            }

            public String getTabReference() {
                return tabReference;
            }

            public String getMappedFieldName() {
                return mappedFieldName;
            }

            public String getTabMappedForm() {
                return tabMappedForm;
            }

            public String getEditAction() {
                return editAction;
            }

            public String getEditViewOnly() {
                return editViewOnly;
            }

            public String getEditAllowAddition() {
                return editAllowAddition;
            }

            public String getEditFixedRows() {
                return editFixedRows;
            }

            public List<TempFormSectionDef> getFormSectionDefList() {
                return formSectionDefList;
            }

            public void addFormSectionDef(String sectionLabel, String name, FormColumnsType columns, boolean visible,
                    boolean editable, boolean disabled) {
                formSectionDefList
                        .add(new TempFormSectionDef(name, sectionLabel, columns, visible, editable, disabled));
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
        }

        private class TempFormSectionDef {

            private List<FormFieldDef> formFieldDefList;

            private String name;

            private String sectionLabel;

            private FormColumnsType columns;

            private boolean visible;

            private boolean editable;

            private boolean disabled;

            public TempFormSectionDef(String name, String sectionLabel, FormColumnsType columns, boolean visible,
                    boolean editable, boolean disabled) {
                this.formFieldDefList = new ArrayList<FormFieldDef>();
                this.name = name;
                this.sectionLabel = sectionLabel;
                this.columns = columns;
                this.visible = visible;
                this.editable = editable;
                this.disabled = disabled;
            }

            public List<FormFieldDef> getFormFieldDefList() {
                return formFieldDefList;
            }

            public void addFormFieldDef(EntityFieldDef entityFieldDef, WidgetTypeDef widgetTypeDef, RefDef inputRefDef,
                    String fieldLabel, String renderer, int column, boolean switchOnChange, boolean saveAs,
                    boolean required, boolean visible, boolean editable, boolean disabled) {
                formFieldDefList.add(new FormFieldDef(entityFieldDef, widgetTypeDef, inputRefDef, fieldLabel, renderer,
                        column, switchOnChange, saveAs, required, visible, editable, disabled));
            }

            public String getName() {
                return name;
            }

            public String getSectionLabel() {
                return sectionLabel;
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

    }
}
