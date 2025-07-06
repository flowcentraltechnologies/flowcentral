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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Assignment page configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class AppAssignmentPageConfig extends BaseClassifiedConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true)
    private String commitPolicy;

    @JacksonXmlProperty(isAttribute = true)
    private String baseField;

    @JacksonXmlProperty(isAttribute = true)
    private String assignField;

    @JacksonXmlProperty(isAttribute = true)
    private String filterCaption1;

    @JacksonXmlProperty(isAttribute = true)
    private String filterCaption2;

    @JacksonXmlProperty(isAttribute = true)
    private String filterList1;

    @JacksonXmlProperty(isAttribute = true)
    private String filterList2;

    @JacksonXmlProperty(isAttribute = true)
    private String assignCaption;

    @JacksonXmlProperty(isAttribute = true)
    private String unassignCaption;

    @JacksonXmlProperty(isAttribute = true)
    private String assignList;

    @JacksonXmlProperty(isAttribute = true)
    private String unassignList;

    @JacksonXmlProperty(isAttribute = true)
    private String ruleDescField;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCommitPolicy() {
        return commitPolicy;
    }

    public void setCommitPolicy(String commitPolicy) {
        this.commitPolicy = commitPolicy;
    }

    public String getBaseField() {
        return baseField;
    }

    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }

    public String getAssignField() {
        return assignField;
    }

    public void setAssignField(String assignField) {
        this.assignField = assignField;
    }

    public String getFilterCaption1() {
        return filterCaption1;
    }

    public void setFilterCaption1(String filterCaption1) {
        this.filterCaption1 = filterCaption1;
    }

    public String getFilterCaption2() {
        return filterCaption2;
    }

    public void setFilterCaption2(String filterCaption2) {
        this.filterCaption2 = filterCaption2;
    }

    public String getFilterList1() {
        return filterList1;
    }

    public void setFilterList1(String filterList1) {
        this.filterList1 = filterList1;
    }

    public String getFilterList2() {
        return filterList2;
    }

    public void setFilterList2(String filterList2) {
        this.filterList2 = filterList2;
    }

    public String getAssignCaption() {
        return assignCaption;
    }

    public void setAssignCaption(String assignCaption) {
        this.assignCaption = assignCaption;
    }

    public String getUnassignCaption() {
        return unassignCaption;
    }

    public void setUnassignCaption(String unassignCaption) {
        this.unassignCaption = unassignCaption;
    }

    public String getAssignList() {
        return assignList;
    }

    public void setAssignList(String assignList) {
        this.assignList = assignList;
    }

    public String getUnassignList() {
        return unassignList;
    }

    public void setUnassignList(String unassignList) {
        this.unassignList = unassignList;
    }

    public String getRuleDescField() {
        return ruleDescField;
    }

    public void setRuleDescField(String ruleDescField) {
        this.ruleDescField = ruleDescField;
    }

}
