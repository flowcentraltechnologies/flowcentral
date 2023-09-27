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

package com.flowcentraltech.flowcentral.common.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.data.CollaborationLockInfo;
import com.tcdng.unify.core.UnifyException;

/**
 * Collaboration provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface CollaborationProvider extends FlowCentralComponent {

    /**
     * Gets collaboration applets.
     * 
     * @return the collaboration applets
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> getCollaborationApplets() throws UnifyException;

    /**
     * Checks if resource is frozen.
     * 
     * @param type
     *                 the collaboration type
     * @param resourceName
     *                 the resource's long name
     * @return true if frozen otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isFrozen(CollaborationType type, String resourceName) throws UnifyException;
    
    /**
     * Gets lock information.
     * 
     * @param type
     *                 the collaboration type
     * @param resourceName
     *                 the resource's long name
     * @return the lock information if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    CollaborationLockInfo getLockInfo(CollaborationType type, String resourceName) throws UnifyException;

    /**
     * Checks if resource is locked.
     * 
     * @param type
     *                 the collaboration type
     * @param resourceName
     *                 the resource's long name
     * @return true if locked otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isLocked(CollaborationType type, String resourceName) throws UnifyException;

    /**
     * Checks if resource is locked by user.
     * 
     * @param type
     *                 the collaboration type
     * @param resourceName
     *                 the resource's long name
     * @param userLoginId
     *                    the user login ID
     * @return true if locked by user otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isLockedBy(CollaborationType type, String resourceName, String userLoginId) throws UnifyException;

    /**
     * Locks a resource by user.
     * 
     * @param type
     *                    the collaboration type
     * @param resourceName
     *                    the resource's long name
     * @param userLoginId
     *                    the user login ID
     * @return true is successfully locked or user already owns lock otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean lock(CollaborationType type, String resourceName, String userLoginId) throws UnifyException;

    /**
     * Unlocks a resource by user.
     * 
     * @param type
     *                    the collaboration type
     * @param resourceName
     *                    the resource's long name
     * @param userLoginId
     *                    the user login ID
     * @return true is successfully unlocked by user otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean unlock(CollaborationType type, String resourceName, String userLoginId) throws UnifyException;

    /**
     * Unlocks a resource.
     * 
     * @param type
     *                 the collaboration type
     * @param resourceName
     *                 the resource's long name
     * @return true is successfully unlocked otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean unlock(CollaborationType type, String resourceName) throws UnifyException;

    /**
     * Grab lock a resource by user.
     * 
     * @param type
     *                    the collaboration type
     * @param resourceName
     *                    the resource's long name
     * @param userLoginId
     *                    the user login ID
     * @return true is successfully lock was successfully grabbed. 
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean grabLock(CollaborationType type, String resourceName, String userLoginId) throws UnifyException;
}
