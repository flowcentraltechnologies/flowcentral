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

package com.flowcentraltech.flowcentral.workflow.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow role provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface WorkflowRoleProvider extends FlowCentralComponent {

    /**
     * Gets participating role.
     * 
     * @param workflow
     *                 the workflow long name
     * @param step
     *                 the workflow step name
     * @return list of participating role codes
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> getParticipatingRoles(String workflow, String step) throws UnifyException;
    
    /**
     * Keeps already assigned role.
     * 
     * @param applicationName
     *                        the application name
     * @param workflowName
     *                        the workflow name
     * @param stepList
     *                        the step list
     * @throws UnifyException
     *                        if an error occurs
     */
    void keepAlreadyAssignedRoles(String applicationName, String workflowName, List<WfStep> stepList)
            throws UnifyException;
    
    /**
     * Gets user full name.
     * 
     * @param userLoginId
     *                    the user login ID
     * @return the full name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getUserFullName(String userLoginId) throws UnifyException;
}
