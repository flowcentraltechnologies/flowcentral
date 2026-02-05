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

package com.flowcentraltech.flowcentral.workflow.business;

import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.messaging.os.data.UserAction;
import com.flowcentraltech.flowcentral.workflow.data.WfItemAccessible;
import com.tcdng.unify.core.UnifyException;

/**
 * Work-item external accessibility provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface WorkItemExternalAccessibilityProvider extends FlowCentralComponent {

    /**
     * Submits work record ID from external system.
     * 
     * @param workRecId
     *                     the work record ID
     * @param workflowName
     *                     the workflow name
     * @param entityName
     *                     the entity name
     * @param requestedBy
     *                     the requester
     * @param requestedOn
     *                     requested on
     * @return true if successfully submitted
     * @throws UnifyException if an error occurs
     */
    boolean submitFromExternal(Long workRecId, String workflowName, String entityName,
            String requestedBy, Date requestedOn) throws UnifyException;

    /**
     * Notify external system for start.
     * 
     * @param item
     *             the workflow item
     * @return true if transferred otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean notifyExternalForStart(WfItemAccessible item) throws UnifyException;

    /**
     * Notify external system for end.
     * 
     * @param item
     *             the workflow item
     * @return true if transferred otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean notifyExternalForEnd(WfItemAccessible item) throws UnifyException;

    /**
     * Transfers workflow item to external system for user action.
     * 
     * @param item
     *             the workflow item
     * @return true if transferred otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean transferToExternalForUserAction(WfItemAccessible item) throws UnifyException;

    /**
     * Releases work record ID from external system.
     * 
     * @param workRecId
     *                     the work record ID
     * @param workflowName
     *                     the workflow name
     * @param stepName
     *                     the step name
     * @param actions
     *                     the action list
     * @return true if successfully
     * @throws UnifyException
     */
    boolean releaseFromExternalWithUserAction(Long workRecId, String workflowName, String stepName,
            List<? extends UserAction> actions) throws UnifyException;
}
