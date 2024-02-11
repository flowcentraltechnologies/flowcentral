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

import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.configuration.constants.ChildListActionType;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Application applet filter entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_APPLETFILTER", uniqueConstraints = { @UniqueConstraint({ "appAppletId", "name" }),
        @UniqueConstraint({ "appAppletId", "description" }) })
public class AppAppletFilter extends BaseConfigNamedEntity {

    @ForeignKey(AppApplet.class)
    private Long appAppletId;

    @ForeignKey
    private OwnershipType ownershipType;

    @ForeignKey(nullable = true)
    private ChildListActionType childListActionType;
    
    @Column(length = 64, nullable = true)
    private String owner;

    @Column
    private boolean quickFilter;

    @Column(name = "PREFFERED_FORM_NM", length = 128, nullable = true)
    private String preferredForm;

    @Column(name = "PREFFERED_CHILDLIST_APP_NM", length = 128, nullable = true)
    private String preferredChildListApplet;

    @ListOnly(key = "ownershipType", property = "description")
    private String ownershipTypeDesc;

    @ListOnly(key = "childListActionType", property = "description")
    private String childListActionTypeDesc;
    
    @Column(length = 64, nullable = true)
    private String filterGenerator;

    @Column(length = 64, nullable = true)
    private String filterGeneratorRule;

    @Child(category = "applet")
    private AppFilter filter;

    public AppAppletFilter(String name, String description, String definition) {
        this.setName(name);
        this.setDescription(description);
        this.filter = new AppFilter(definition);
    }

    public AppAppletFilter() {
        
    }
    
    public Long getAppAppletId() {
        return appAppletId;
    }

    public void setAppAppletId(Long appAppletId) {
        this.appAppletId = appAppletId;
    }

    public OwnershipType getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(OwnershipType ownershipType) {
        this.ownershipType = ownershipType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPreferredForm() {
        return preferredForm;
    }

    public void setPreferredForm(String preferredForm) {
        this.preferredForm = preferredForm;
    }

    public String getPreferredChildListApplet() {
        return preferredChildListApplet;
    }

    public void setPreferredChildListApplet(String preferredChildListApplet) {
        this.preferredChildListApplet = preferredChildListApplet;
    }

    public boolean isQuickFilter() {
        return quickFilter;
    }

    public void setQuickFilter(boolean quickFilter) {
        this.quickFilter = quickFilter;
    }

    public String getOwnershipTypeDesc() {
        return ownershipTypeDesc;
    }

    public void setOwnershipTypeDesc(String ownershipTypeDesc) {
        this.ownershipTypeDesc = ownershipTypeDesc;
    }

    public String getFilterGenerator() {
        return filterGenerator;
    }

    public void setFilterGenerator(String filterGenerator) {
        this.filterGenerator = filterGenerator;
    }

    public String getFilterGeneratorRule() {
        return filterGeneratorRule;
    }

    public void setFilterGeneratorRule(String filterGeneratorRule) {
        this.filterGeneratorRule = filterGeneratorRule;
    }

    public AppFilter getFilter() {
        return filter;
    }

    public void setFilter(AppFilter filter) {
        this.filter = filter;
    }

    public ChildListActionType getChildListActionType() {
        return childListActionType;
    }

    public void setChildListActionType(ChildListActionType childListActionType) {
        this.childListActionType = childListActionType;
    }

    public String getChildListActionTypeDesc() {
        return childListActionTypeDesc;
    }

    public void setChildListActionTypeDesc(String childListActionTypeDesc) {
        this.childListActionTypeDesc = childListActionTypeDesc;
    }

}
