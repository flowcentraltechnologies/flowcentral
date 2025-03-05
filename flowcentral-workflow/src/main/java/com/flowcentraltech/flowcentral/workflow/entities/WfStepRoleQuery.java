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

import java.util.List;

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.tcdng.unify.common.constants.WfItemVersionType;

/**
 * Workflow role query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfStepRoleQuery extends BaseEntityQuery<WfStepRole> {

    public WfStepRoleQuery() {
        super(WfStepRole.class);
    }

    public WfStepRoleQuery roleCode(String roleCode) {
        return (WfStepRoleQuery) addEquals("roleCode", roleCode);
    }

    public WfStepRoleQuery wfStepName(String wfStepName) {
        return (WfStepRoleQuery) addEquals("wfStepName", wfStepName);
    }

    public WfStepRoleQuery workflowName(String workflowName) {
        return (WfStepRoleQuery) addEquals("workflowName", workflowName);
    }
    
    public WfStepRoleQuery wfStepId(Long wfStepId) {
        return (WfStepRoleQuery) addEquals("wfStepId", wfStepId);
    }
    
    public WfStepRoleQuery roleId(Long roleId) {
        return (WfStepRoleQuery) addEquals("roleId", roleId);
    }
    
    public WfStepRoleQuery applicationId(Long applicationId) {
        return (WfStepRoleQuery) addEquals("applicationId", applicationId);
    }
    
    public WfStepRoleQuery applicationName(String applicationName) {
        return (WfStepRoleQuery) addEquals("applicationName", applicationName);
    }
    
    public WfStepRoleQuery wfStepType(WorkflowStepType wfStepType) {
        return (WfStepRoleQuery) addEquals("wfStepType", wfStepType);
    }
    
    public WfStepRoleQuery wfStepTypeIn(List<WorkflowStepType> wfStepTypes) {
        return (WfStepRoleQuery) addAmongst("wfStepType", wfStepTypes);
    }
    
    public WfStepRoleQuery workflowLoadingTable(String workflowLoadingTable) {
        return (WfStepRoleQuery) addEquals("workflowLoadingTable", workflowLoadingTable);
    }
    
    public WfStepRoleQuery isWithLoadingTable() {
        return (WfStepRoleQuery) addIsNotNull("workflowLoadingTable");
    }
    
    public WfStepRoleQuery isOriginal() {
        return (WfStepRoleQuery) addEquals("wfItemVersionType", WfItemVersionType.ORIGINAL);
    }

    public WfStepRoleQuery configType(ConfigType configType) {
        return (WfStepRoleQuery) addEquals("configType", configType);
    }

    public WfStepRoleQuery isCustom() {
        return (WfStepRoleQuery) addEquals("configType", ConfigType.CUSTOM);
    }
}
