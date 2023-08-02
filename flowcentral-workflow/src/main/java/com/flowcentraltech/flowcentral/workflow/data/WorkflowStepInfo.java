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
package com.flowcentraltech.flowcentral.workflow.data;

import com.tcdng.unify.core.data.Listable;

/**
 * Workflow information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkflowStepInfo implements Listable {

    private String workflowLongName;

    private String applicationName;

    private String workflowName;

    private String entity;

    private String stepName;

    private String stepDesc;

    private String stepLabel;

    public WorkflowStepInfo(String workflowLongName, String applicationName, String workflowName, String entity,
            String stepName, String stepDesc, String stepLabel) {
        this.workflowLongName = workflowLongName;
        this.applicationName = applicationName;
        this.workflowName = workflowName;
        this.entity = entity;
        this.stepName = stepName;
        this.stepDesc = stepDesc;
        this.stepLabel = stepLabel;
    }

    @Override
    public String getListKey() {
        return stepName;
    }

    @Override
    public String getListDescription() {
        return stepDesc;
    }

    public String getWorkflowLongName() {
        return workflowLongName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getEntity() {
        return entity;
    }

    public String getStepName() {
        return stepName;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public String getStepLabel() {
        return stepLabel;
    }

}