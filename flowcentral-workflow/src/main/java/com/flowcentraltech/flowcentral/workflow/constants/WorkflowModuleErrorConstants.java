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
package com.flowcentraltech.flowcentral.workflow.constants;

/**
 * Workflow module error constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface WorkflowModuleErrorConstants {

    /** Can not find workflow {0}. */
    String CANNOT_FIND_RUNNABLE_APPLICATION_WORKFLOW = "WORKFLOW_0001";

    /**
     * Can not submit incompatible instance type {0} to workflow {1} in application
     * {2}.
     */
    String CANNOT_SUBMIT_INST_TO_INCOMPATIBLE_WORKFLOW = "WORKFLOW_0002";

    /**
     * Item with ID {0} for type {1} already in workflow {2} in application {3}.
     */
    String INST_ALREADY_IN_WORKFLOW = "WORKFLOW_0003";

    /**
     * Attempt to submit workflow item to a closed workflow channel {0}.
     */
    String WORKFLOW_CHANNEL_IS_CLOSED = "WORKFLOW_0004";

}
