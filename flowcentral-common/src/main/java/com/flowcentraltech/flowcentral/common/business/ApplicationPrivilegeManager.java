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

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Application privilege manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ApplicationPrivilegeManager extends FlowCentralComponent {

    /**
     * Registers a privilege.
     * 
     * @param applicationId
     *                              the application ID
     * @param privilegeCategoryCode
     *                              the privilege category code
     * @param privilegeCode
     *                              the privilege code
     * @param privilegeDesc
     *                              the privilege description
     * @throws UnifyException
     *                        if privilege category code is unknown. If an error
     *                        occurs
     */
    void registerPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode, String privilegeDesc)
            throws UnifyException;

    /**
     * Unregisters a privilege.
     * 
     * @param applicationId
     *                              the application ID
     * @param privilegeCategoryCode
     *                              the privilege category code
     * @param privilegeCode
     *                              the privilege code
     * @return true if privilege was existing otherwise false
     * @throws UnifyException
     *                        if privilege category code is unknown. If an error
     *                        occurs
     */
    boolean unregisterPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode)
            throws UnifyException;

    /**
     * Unregisters all privileges associated with an application.
     * 
     * @param applicationId
     *                      the application ID
     * @throws UnifyException
     *                        If an error occurs
     */
    void unregisterApplicationPrivileges(Long applicationId) throws UnifyException;

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
    
    /**
     * Finds role privileges.
     * 
     * @param privilegeCategoryCode
     *                              the privilege category code
     * @param roleCode
     *                              the role code
     * @return list of privilege codes
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> findRolePrivileges(String privilegeCategoryCode, String roleCode) throws UnifyException;

    /**
     * Assigns privilege to a role.
     * 
     * @param roleCode
     *                      the role code
     * @param privilegeCode
     *                      the privilege code
     * @return false if privilege already assigned otherwise true
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean assignPrivilegeToRole(String roleCode, String privilegeCode) throws UnifyException;

    /**
     * Checks if supplied string is known registered privilege code.
     * 
     * @param privilegeCategoryCode
     *                      the privilege category code
     * @param privilegeCode
     *                      the privilege code
     * @return true if supplied string is a privilege otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isRegisteredPrivilege(String privilegeCategoryCode, String privilegeCode) throws UnifyException;

    /**
     * Checks if a role has a privilege.
     * 
     * @param roleCode
     *                      the role code
     * @param privilegeCode
     *                      the privilege code
     * @return true if supplied role has privilege otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isRoleWithPrivilege(String roleCode, String privilegeCode) throws UnifyException;
}
