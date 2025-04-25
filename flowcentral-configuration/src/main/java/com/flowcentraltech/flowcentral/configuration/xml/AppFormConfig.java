/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.FormType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormTypeXmlAdapter;

/**
 * Form configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class AppFormConfig extends BaseNameConfig {

    @JacksonXmlProperty
    private FormFiltersConfig filters;
    
    @JacksonXmlProperty
    private FormAnnotationsConfig annotations;

    @JacksonXmlProperty
    private FormActionsConfig actions;

    @JacksonXmlProperty
    private FormTabsConfig tabs;

    @JacksonXmlProperty
    private RelatedListsConfig relatedLists;

    @JacksonXmlProperty
    private FormStatePoliciesConfig formStatePolicies;

    @JacksonXmlProperty
    private FormWidgetRulesPoliciesConfig formWidgetRulesPolicies;

    @JacksonXmlProperty
    private FieldValidationPoliciesConfig fieldValidationPolicies;

    @JacksonXmlProperty
    private FormValidationPoliciesConfig formValidationPolicies;

    @JacksonXmlProperty
    private FormReviewPoliciesConfig formReviewPolicies;

    @JsonSerialize(using = FormTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = FormTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private FormType type;
    
    @JacksonXmlProperty(isAttribute = true)
    private String entity;
    
    @JacksonXmlProperty(isAttribute = true)
    private String helpSheet;

    @JacksonXmlProperty(isAttribute = true)
    private String titleFormat;

    @JacksonXmlProperty(isAttribute = true)
    private String listingGenerator;

    @JacksonXmlProperty(isAttribute = true)
    private String consolidatedValidation;

    @JacksonXmlProperty(isAttribute = true)
    private String consolidatedReview;

    @JacksonXmlProperty(isAttribute = true)
    private String consolidatedState;

    public AppFormConfig() {
        this.type = FormType.INPUT;
    }

    public FormFiltersConfig getFilters() {
        return filters;
    }

    public void setFilters(FormFiltersConfig filters) {
        this.filters = filters;
    }

    public FormAnnotationsConfig getAnnotations() {
        return annotations;
    }

    public void setAnnotations(FormAnnotationsConfig annotations) {
        this.annotations = annotations;
    }

    public FormActionsConfig getActions() {
        return actions;
    }

    public void setActions(FormActionsConfig actions) {
        this.actions = actions;
    }

    public FormTabsConfig getTabs() {
        return tabs;
    }

    public void setTabs(FormTabsConfig tabs) {
        this.tabs = tabs;
    }

    public RelatedListsConfig getRelatedLists() {
        return relatedLists;
    }

    public void setRelatedLists(RelatedListsConfig relatedLists) {
        this.relatedLists = relatedLists;
    }

    public FormStatePoliciesConfig getFormStatePolicies() {
        return formStatePolicies;
    }

    public void setFormStatePolicies(FormStatePoliciesConfig formStatePolicies) {
        this.formStatePolicies = formStatePolicies;
    }

    public FormWidgetRulesPoliciesConfig getFormWidgetRulesPolicies() {
        return formWidgetRulesPolicies;
    }

    public void setFormWidgetRulesPolicies(FormWidgetRulesPoliciesConfig formWidgetRulesPolicies) {
        this.formWidgetRulesPolicies = formWidgetRulesPolicies;
    }

    public FieldValidationPoliciesConfig getFieldValidationPolicies() {
        return fieldValidationPolicies;
    }

    public void setFieldValidationPolicies(FieldValidationPoliciesConfig fieldValidationPolicies) {
        this.fieldValidationPolicies = fieldValidationPolicies;
    }

    public FormValidationPoliciesConfig getFormValidationPolicies() {
        return formValidationPolicies;
    }

    public void setFormValidationPolicies(FormValidationPoliciesConfig formValidationPolicies) {
        this.formValidationPolicies = formValidationPolicies;
    }

    public FormReviewPoliciesConfig getFormReviewPolicies() {
        return formReviewPolicies;
    }

    public void setFormReviewPolicies(FormReviewPoliciesConfig formReviewPolicies) {
        this.formReviewPolicies = formReviewPolicies;
    }

    public FormType getType() {
        return type;
    }

    public void setType(FormType type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getHelpSheet() {
        return helpSheet;
    }

    public void setHelpSheet(String helpSheet) {
        this.helpSheet = helpSheet;
    }

    public String getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(String titleFormat) {
        this.titleFormat = titleFormat;
    }

    public String getListingGenerator() {
        return listingGenerator;
    }

    public void setListingGenerator(String listingGenerator) {
        this.listingGenerator = listingGenerator;
    }

    public String getConsolidatedValidation() {
        return consolidatedValidation;
    }

    public void setConsolidatedValidation(String consolidatedValidation) {
        this.consolidatedValidation = consolidatedValidation;
    }

    public String getConsolidatedReview() {
        return consolidatedReview;
    }

    public void setConsolidatedReview(String consolidatedReview) {
        this.consolidatedReview = consolidatedReview;
    }

    public String getConsolidatedState() {
        return consolidatedState;
    }

    public void setConsolidatedState(String consolidatedState) {
        this.consolidatedState = consolidatedState;
    }

}
