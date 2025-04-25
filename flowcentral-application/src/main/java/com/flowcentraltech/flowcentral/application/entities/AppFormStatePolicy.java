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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.configuration.constants.FormStatePolicyType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Application form state policy entity;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_FORMSTATEPOLICY",
    uniqueConstraints = { @UniqueConstraint({ "appFormId", "name" }) })
public class AppFormStatePolicy extends BaseConfigNamedEntity {

    @ForeignKey(AppForm.class)
    private Long appFormId;

    @ForeignKey(name = "POLICY_TYPE")
    private FormStatePolicyType type;
    
    @Column(name = "VALUE_GENERATOR", length = 128, nullable = true)
    private String valueGenerator;
    
    @Column(name = "POLICY_TRIGGER", length = 256, nullable = true)
    private String trigger;

    @Column(length = 64, nullable = true)
    private Integer executionIndex;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;
    
    @Child(category = "formstatepolicy")
    private AppFilter onCondition;

    @Child(category = "formstatepolicy")
    private AppSetValues setValues;

    @ChildList
    private List<AppFormSetState> setStateList;

    public Long getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(Long appFormId) {
        this.appFormId = appFormId;
    }

    public FormStatePolicyType getType() {
        return type;
    }

    public void setType(FormStatePolicyType type) {
        this.type = type;
    }

    public String getValueGenerator() {
        return valueGenerator;
    }

    public void setValueGenerator(String valueGenerator) {
        this.valueGenerator = valueGenerator;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public AppFilter getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(AppFilter onCondition) {
        this.onCondition = onCondition;
    }

    public AppSetValues getSetValues() {
        return setValues;
    }

    public void setSetValues(AppSetValues setValues) {
        this.setValues = setValues;
    }

    public List<AppFormSetState> getSetStateList() {
        return setStateList;
    }

    public void setSetStateList(List<AppFormSetState> setStateList) {
        this.setStateList = setStateList;
    }

    public Integer getExecutionIndex() {
        return executionIndex;
    }

    public void setExecutionIndex(Integer executionIndex) {
        this.executionIndex = executionIndex;
    }

}
