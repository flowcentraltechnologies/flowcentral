/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workflow.organization.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.tcdng.unify.common.constants.WfItemVersionType;

/**
 * Workflow role query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WfStepRoleQuery extends BaseEntityQuery<WfStepRole> {

    public WfStepRoleQuery() {
        super(WfStepRole.class);
    }

    public WfStepRoleQuery roleCode(String roleCode) {
        return (WfStepRoleQuery) equals("roleCode", roleCode);
    }

    public WfStepRoleQuery wfStepName(String wfStepName) {
        return (WfStepRoleQuery) equals("wfStepName", wfStepName);
    }
    
    public WfStepRoleQuery workflowName(String workflowName) {
        return (WfStepRoleQuery) equals("workflowName", workflowName);
    }
    
    public WfStepRoleQuery workflowRunnable(boolean workflowRunnable) {
        return (WfStepRoleQuery) equals("workflowRunnable", workflowRunnable);
    }
    
    public WfStepRoleQuery wfStepId(Long wfStepId) {
        return (WfStepRoleQuery) equals("wfStepId", wfStepId);
    }
    
    public WfStepRoleQuery roleId(Long roleId) {
        return (WfStepRoleQuery) equals("roleId", roleId);
    }
    
    public WfStepRoleQuery applicationId(Long applicationId) {
        return (WfStepRoleQuery) equals("applicationId", applicationId);
    }
    
    public WfStepRoleQuery applicationName(String applicationName) {
        return (WfStepRoleQuery) equals("applicationName", applicationName);
    }
    
    public WfStepRoleQuery wfStepType(WorkflowStepType wfStepType) {
        return (WfStepRoleQuery) equals("wfStepType", wfStepType);
    }
    
    public WfStepRoleQuery wfStepTypeIn(List<WorkflowStepType> wfStepTypes) {
        return (WfStepRoleQuery) amongst("wfStepType", wfStepTypes);
    }
    
    public WfStepRoleQuery workflowLoadingTable(String workflowLoadingTable) {
        return (WfStepRoleQuery) equals("workflowLoadingTable", workflowLoadingTable);
    }
    
    public WfStepRoleQuery isWithLoadingTable() {
        return (WfStepRoleQuery) notnull("workflowLoadingTable");
    }
    
    public WfStepRoleQuery isOriginal() {
        return (WfStepRoleQuery) equals("wfItemVersionType", WfItemVersionType.ORIGINAL);
    }

    public WfStepRoleQuery configType(ConfigType configType) {
        return (WfStepRoleQuery) equals("configType", configType);
    }

    public WfStepRoleQuery isCustom() {
        return (WfStepRoleQuery) equals("configType", ConfigType.CUSTOM);
    }
}
