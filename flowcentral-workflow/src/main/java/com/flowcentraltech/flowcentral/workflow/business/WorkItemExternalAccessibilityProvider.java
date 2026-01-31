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

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.workflow.data.UserAction;
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
     * Releases workflow item from external system.
     * 
     * @param workItemId
     *                   the work item ID
     * @param stepName
     *                   the step name
     * @param actions
     *                   the action list
     * @return true if successfully
     * @throws UnifyException
     */
    boolean releaseFromExternalWithUserAction(Long workItemId, String stepName, List<? extends UserAction> actions)
            throws UnifyException;
}
