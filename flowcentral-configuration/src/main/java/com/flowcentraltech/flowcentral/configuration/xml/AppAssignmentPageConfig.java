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

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Assignment page configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppAssignmentPageConfig extends BaseNameConfig {

    private String entity;

    private String commitPolicy;

    private String baseField;

    private String assignField;

    private String filterCaption1;

    private String filterCaption2;

    private String filterList1;

    private String filterList2;

    private String assignCaption;

    private String unassignCaption;

    private String assignList;

    private String unassignList;

    private String ruleDescField;

    public String getEntity() {
        return entity;
    }

    @XmlAttribute(required = true)
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCommitPolicy() {
        return commitPolicy;
    }

    @XmlAttribute
    public void setCommitPolicy(String commitPolicy) {
        this.commitPolicy = commitPolicy;
    }

    public String getBaseField() {
        return baseField;
    }

    @XmlAttribute(required = true)
    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }

    public String getAssignField() {
        return assignField;
    }

    @XmlAttribute(required = true)
    public void setAssignField(String assignField) {
        this.assignField = assignField;
    }

    public String getFilterCaption1() {
        return filterCaption1;
    }

    @XmlAttribute
    public void setFilterCaption1(String filterCaption1) {
        this.filterCaption1 = filterCaption1;
    }

    public String getFilterCaption2() {
        return filterCaption2;
    }

    @XmlAttribute
    public void setFilterCaption2(String filterCaption2) {
        this.filterCaption2 = filterCaption2;
    }

    public String getFilterList1() {
        return filterList1;
    }

    @XmlAttribute
    public void setFilterList1(String filterList1) {
        this.filterList1 = filterList1;
    }

    public String getFilterList2() {
        return filterList2;
    }

    @XmlAttribute
    public void setFilterList2(String filterList2) {
        this.filterList2 = filterList2;
    }

    public String getAssignCaption() {
        return assignCaption;
    }

    @XmlAttribute(required = true)
    public void setAssignCaption(String assignCaption) {
        this.assignCaption = assignCaption;
    }

    public String getUnassignCaption() {
        return unassignCaption;
    }

    @XmlAttribute(required = true)
    public void setUnassignCaption(String unassignCaption) {
        this.unassignCaption = unassignCaption;
    }

    public String getAssignList() {
        return assignList;
    }

    @XmlAttribute(required = true)
    public void setAssignList(String assignList) {
        this.assignList = assignList;
    }

    public String getUnassignList() {
        return unassignList;
    }

    @XmlAttribute(required = true)
    public void setUnassignList(String unassignList) {
        this.unassignList = unassignList;
    }

    public String getRuleDescField() {
        return ruleDescField;
    }

    @XmlAttribute
    public void setRuleDescField(String ruleDescField) {
        this.ruleDescField = ruleDescField;
    }

}
