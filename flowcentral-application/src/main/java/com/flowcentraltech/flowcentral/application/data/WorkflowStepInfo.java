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
package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.StringUtils;

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

    private String workItemFilterGenName;

    private String entity;

    private String stepName;

    private String stepDesc;

    private String stepLabel;

    private String branchCode;

    private String departmentCode;

    public WorkflowStepInfo(String workflowLongName, String applicationName, String workflowName,
            String workItemFilterGenName, String entity, String stepName, String stepDesc, String stepLabel,
            String branchCode, String departmentCode) {
        this.workflowLongName = workflowLongName;
        this.applicationName = applicationName;
        this.workflowName = workflowName;
        this.workItemFilterGenName = workItemFilterGenName;
        this.entity = entity;
        this.stepName = stepName;
        this.stepDesc = stepDesc;
        this.stepLabel = stepLabel;
        this.branchCode = branchCode;
        this.departmentCode = departmentCode;
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

    public String getWorkItemFilterGenName() {
        return workItemFilterGenName;
    }

    public boolean isWithWorkItemFilterGen() {
        return !StringUtils.isBlank(workItemFilterGenName);
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

    public String getBranchCode() {
        return branchCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public boolean isWithBranchCode() {
        return !StringUtils.isBlank(branchCode);
    }

    public boolean isWithDepartmentCode() {
        return !StringUtils.isBlank(departmentCode);
    }
}
