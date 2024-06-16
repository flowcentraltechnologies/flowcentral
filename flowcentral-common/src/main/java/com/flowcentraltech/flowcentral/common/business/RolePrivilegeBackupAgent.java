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

package com.flowcentraltech.flowcentral.common.business;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Role privilege backup agent.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface RolePrivilegeBackupAgent extends FlowCentralComponent {

    /**
     * Unregisters all privileges associated with an application.
     * 
     * @param applicationId
     *                      the application ID
     * @throws UnifyException
     *                        If an error occurs
     */
    void unregisterApplicationRolePrivileges(Long applicationId) throws UnifyException;

    /**
     * Backs up application role privileges (Not expected to be persistent).
     * 
     * @param applicationId
     *                      the application ID
     * @throws UnifyException
     *                        if an error occurs
     */
    void backupApplicationRolePrivileges(Long applicationId) throws UnifyException;

    /**
     * Restores application role privileges from prior backup which is cleared on completion.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void restoreApplicationRolePrivileges() throws UnifyException;

}
