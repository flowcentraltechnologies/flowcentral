/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.organization.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Mapped;
import com.tcdng.unify.core.annotation.TableName;

/**
 * Mapped interactive workflow step entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Mapped
@TableName("FC_MAPPEDWFSTEP")
public class MappedWfStep extends BaseConfigNamedEntity {

    @Column
    private Long workflowId;

    @Column
    private WorkflowStepType type;

    @Column(name = "WFSTEP_LABEL", length = 64)
    private String label;

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public WorkflowStepType getType() {
        return type;
    }

    public void setType(WorkflowStepType type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
