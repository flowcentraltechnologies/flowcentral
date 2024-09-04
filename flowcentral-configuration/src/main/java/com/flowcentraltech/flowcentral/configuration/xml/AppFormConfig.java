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
package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.FormType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.FormTypeXmlAdapter;

/**
 * Form configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class AppFormConfig extends BaseNameConfig {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "filter")
    private List<FormFilterConfig> filterList;
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "annotation")
    private List<FormAnnotationConfig> annotationList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "action")
    private List<FormActionConfig> actionList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "tab")
    private List<FormTabConfig> tabList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "relatedList")
    private List<RelatedListConfig> relatedList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "formStatePolicy")
    private List<FormStatePolicyConfig> formStatePolicyList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "formWidgetRulesPolicy")
    private List<FormWidgetRulesPolicyConfig> widgetRulesPolicyList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "fieldValidationPolicy")
    private List<FieldValidationPolicyConfig> fieldValidationPolicyList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "formValidationPolicy")
    private List<FormValidationPolicyConfig> formValidationPolicyList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "formReviewPolicy")
    private List<FormReviewPolicyConfig> formReviewPolicyList;

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

    public List<FormFilterConfig> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<FormFilterConfig> filterList) {
        this.filterList = filterList;
    }
    
    public List<FormAnnotationConfig> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<FormAnnotationConfig> annotationList) {
        this.annotationList = annotationList;
    }

    public List<FormActionConfig> getActionList() {
        return actionList;
    }

    public void setActionList(List<FormActionConfig> actionList) {
        this.actionList = actionList;
    }

    public List<FormTabConfig> getTabList() {
        return tabList;
    }

    public void setTabList(List<FormTabConfig> tabList) {
        this.tabList = tabList;
    }

    public List<RelatedListConfig> getRelatedList() {
        return relatedList;
    }

    public void setRelatedList(List<RelatedListConfig> relatedList) {
        this.relatedList = relatedList;
    }

    public List<FormStatePolicyConfig> getFormStatePolicyList() {
        return formStatePolicyList;
    }

    public void setFormStatePolicyList(List<FormStatePolicyConfig> formStatePolicyList) {
        this.formStatePolicyList = formStatePolicyList;
    }

    public List<FormWidgetRulesPolicyConfig> getWidgetRulesPolicyList() {
        return widgetRulesPolicyList;
    }

    public void setWidgetRulesPolicyList(List<FormWidgetRulesPolicyConfig> widgetRulesPolicyList) {
        this.widgetRulesPolicyList = widgetRulesPolicyList;
    }

    public List<FieldValidationPolicyConfig> getFieldValidationPolicyList() {
        return fieldValidationPolicyList;
    }

    public void setFieldValidationPolicyList(List<FieldValidationPolicyConfig> fieldValidationPolicyList) {
        this.fieldValidationPolicyList = fieldValidationPolicyList;
    }

    public List<FormValidationPolicyConfig> getFormValidationPolicyList() {
        return formValidationPolicyList;
    }

    public void setFormValidationPolicyList(List<FormValidationPolicyConfig> formValidationPolicyList) {
        this.formValidationPolicyList = formValidationPolicyList;
    }

    public List<FormReviewPolicyConfig> getFormReviewPolicyList() {
        return formReviewPolicyList;
    }

    public void setFormReviewPolicyList(List<FormReviewPolicyConfig> formReviewPolicyList) {
        this.formReviewPolicyList = formReviewPolicyList;
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
