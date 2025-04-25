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
package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.core.UnifyException;

/**
 * Assignment page definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AssignmentPageDef extends BaseApplicationEntityDef {

    private String label;

    private String entity;

    private String updatePolicy;

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

    public AssignmentPageDef(String longName, String description, Long id, long version, String label, String entity,
            String updatePolicy, String baseField, String assignField, String filterCaption1, String filterCaption2,
            String filterList1, String filterList2, String assignCaption, String unassignCaption, String assignList,
            String unassignList, String ruleDescField) throws UnifyException {
        super(ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
        this.label = label;
        this.entity = entity;
        this.updatePolicy = updatePolicy;
        this.baseField = baseField;
        this.assignField = assignField;
        this.filterCaption1 = filterCaption1;
        this.filterCaption2 = filterCaption2;
        this.filterList1 = filterList1;
        this.filterList2 = filterList2;
        this.assignCaption = assignCaption;
        this.unassignCaption = unassignCaption;
        this.assignList = assignList;
        this.unassignList = unassignList;
        this.ruleDescField = ruleDescField;
    }

    public String getLabel() {
        return label;
    }

    public String getEntity() {
        return entity;
    }

    public String getUpdatePolicy() {
        return updatePolicy;
    }

    public String getBaseField() {
        return baseField;
    }

    public String getAssignField() {
        return assignField;
    }

    public String getFilterCaption1() {
        return filterCaption1;
    }

    public boolean isWithFilterCaption1() {
        return filterCaption1 != null;
    }

    public String getFilterCaption2() {
        return filterCaption2;
    }

    public boolean isWithFilterCaption2() {
        return filterCaption2 != null;
    }

    public String getFilterList1() {
        return filterList1;
    }

    public boolean isWithFilterList1() {
        return filterList1 != null;
    }

    public String getFilterList2() {
        return filterList2;
    }

    public boolean isWithFilterList2() {
        return filterList2 != null;
    }

    public String getAssignCaption() {
        return assignCaption;
    }

    public String getUnassignCaption() {
        return unassignCaption;
    }

    public String getAssignList() {
        return assignList;
    }

    public String getUnassignList() {
        return unassignList;
    }

    public String getRuleDescField() {
        return ruleDescField;
    }

    public boolean isWithRuleDesc() {
        return ruleDescField != null;
    }
}
