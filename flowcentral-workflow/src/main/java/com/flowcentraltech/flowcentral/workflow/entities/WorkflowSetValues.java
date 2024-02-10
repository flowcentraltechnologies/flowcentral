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
package com.flowcentraltech.flowcentral.workflow.entities;

import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowSetValuesType;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow set values entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_WORKFLOWSETVALUES", uniqueConstraints = { @UniqueConstraint({ "workflowId", "name" }),
        @UniqueConstraint({ "workflowId", "description" }) })
public class WorkflowSetValues extends BaseConfigNamedEntity {

    @ForeignKey(name = "WORKFLOWSETVALUES_TY", nullable = true)
    private WorkflowSetValuesType type;
    
    @ForeignKey(Workflow.class)
    private Long workflowId;

    @Column(length = 128, nullable = true)
    private String valueGenerator;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;
    
    @Child(category = "workflow")
    private AppFilter onCondition;

    @Child(category = "workflow")
    private AppSetValues setValues;

    @Override
    public String getListKey() {
        return getName();
    }

    public WorkflowSetValuesType getType() {
        return type;
    }

    public void setType(WorkflowSetValuesType type) {
        this.type = type;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public String getValueGenerator() {
        return valueGenerator;
    }

    public void setValueGenerator(String valueGenerator) {
        this.valueGenerator = valueGenerator;
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

}
