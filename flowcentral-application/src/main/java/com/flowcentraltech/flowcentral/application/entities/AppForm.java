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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.FormType;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Application form entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_FORM")
public class AppForm extends BaseApplicationEntity {

    @ForeignKey(name = "form_type")
    private FormType type;
    
    @Column(length = 128)
    private String entity;

    @Column(length = 64, nullable = true)
    private String label;

    @Column(length = 64, nullable = true)
    private String helpSheet;

    @Column(length = 64, nullable = true)
    private String consolidatedValidation;

    @Column(length = 64, nullable = true)
    private String consolidatedReview;

    @Column(length = 64, nullable = true)
    private String consolidatedState;

    @Column(length = 64, nullable = true)
    private String listingGenerator;

    @Column(length = 1024, nullable = true)
    private String titleFormat;

    @ListOnly(name = "form_type_desc", key = "type", property="description")
    private String typeDesc;

    @ChildList
    private List<AppFormFilter> filterList;
    
    @ChildList
    private List<AppFormAnnotation> annotationList;

    @ChildList
    private List<AppFormAction> actionList;

    @ChildList
    private List<AppFormElement> elementList;

    @ChildList
    private List<AppFormRelatedList> relatedList;

    @ChildList
    private List<AppFormStatePolicy> fieldStateList;

    @ChildList
    private List<AppFormWidgetRulesPolicy> widgetRulesList;
    
    @ChildList
    private List<AppFormFieldValidationPolicy> fieldValidationList;

    @ChildList
    private List<AppFormValidationPolicy> formValidationList;

    @ChildList
    private List<AppFormReviewPolicy> formReviewList;

    public FormType getType() {
        return type;
    }

    public void setType(FormType type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getListingGenerator() {
        return listingGenerator;
    }

    public void setListingGenerator(String listingGenerator) {
        this.listingGenerator = listingGenerator;
    }

    public String getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(String titleFormat) {
        this.titleFormat = titleFormat;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHelpSheet() {
        return helpSheet;
    }

    public void setHelpSheet(String helpSheet) {
        this.helpSheet = helpSheet;
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

    public List<AppFormFilter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<AppFormFilter> filterList) {
        this.filterList = filterList;
    }

    public List<AppFormAnnotation> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<AppFormAnnotation> annotationList) {
        this.annotationList = annotationList;
    }

    public List<AppFormAction> getActionList() {
        return actionList;
    }

    public void setActionList(List<AppFormAction> actionList) {
        this.actionList = actionList;
    }

    public List<AppFormElement> getElementList() {
        return elementList;
    }

    public void setElementList(List<AppFormElement> elementList) {
        this.elementList = elementList;
    }

    public List<AppFormRelatedList> getRelatedList() {
        return relatedList;
    }

    public void setRelatedList(List<AppFormRelatedList> relatedList) {
        this.relatedList = relatedList;
    }

    public List<AppFormStatePolicy> getFieldStateList() {
        return fieldStateList;
    }

    public void setFieldStateList(List<AppFormStatePolicy> fieldStateList) {
        this.fieldStateList = fieldStateList;
    }

    public List<AppFormWidgetRulesPolicy> getWidgetRulesList() {
        return widgetRulesList;
    }

    public void setWidgetRulesList(List<AppFormWidgetRulesPolicy> widgetRulesList) {
        this.widgetRulesList = widgetRulesList;
    }

    public List<AppFormFieldValidationPolicy> getFieldValidationList() {
        return fieldValidationList;
    }

    public void setFieldValidationList(List<AppFormFieldValidationPolicy> fieldValidationList) {
        this.fieldValidationList = fieldValidationList;
    }

    public List<AppFormValidationPolicy> getFormValidationList() {
        return formValidationList;
    }

    public void setFormValidationList(List<AppFormValidationPolicy> formValidationList) {
        this.formValidationList = formValidationList;
    }

    public List<AppFormReviewPolicy> getFormReviewList() {
        return formReviewList;
    }

    public void setFormReviewList(List<AppFormReviewPolicy> formReviewList) {
        this.formReviewList = formReviewList;
    }

}
