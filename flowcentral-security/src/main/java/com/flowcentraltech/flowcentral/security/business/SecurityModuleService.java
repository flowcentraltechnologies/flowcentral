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
package com.flowcentraltech.flowcentral.security.business;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.common.business.UserLoginActivityProvider;
import com.flowcentraltech.flowcentral.common.data.BranchInfo;
import com.flowcentraltech.flowcentral.common.data.UserRoleInfo;
import com.flowcentraltech.flowcentral.security.business.data.PasswordComplexityCheck;
import com.flowcentraltech.flowcentral.security.business.data.PasswordComplexitySettings;
import com.flowcentraltech.flowcentral.security.entities.User;
import com.flowcentraltech.flowcentral.security.entities.UserQuery;
import com.flowcentraltech.flowcentral.security.entities.UserRole;
import com.flowcentraltech.flowcentral.security.entities.UserRoleQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * Security module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface SecurityModuleService extends FlowCentralService, UserLoginActivityProvider {

    /**
     * Gets system password complexity.
     * 
     * @return the password complexity
     * @throws UnifyException
     *                        if an error occurs
     */
    PasswordComplexitySettings getPasswordComplexity() throws UnifyException;
    
    /**
     * Saves system password complexity.
     * 
     * @param settings
     *                 the settings
     * @throws UnifyException
     *                        if an error occurs
     */
    void savePasswordComplexity(PasswordComplexitySettings settings) throws UnifyException;
    
    /**
     * Checks password complexity.
     * 
     * @param password
     *                 the password
     * @return the complexity object
     * @throws UnifyException
     *                        if an error occurs
     */
    PasswordComplexityCheck checkPasswordComplexity(String password) throws UnifyException;
    
    /**
     * Finds users by criteria.
     * 
     * @param query
     *              the search query
     * @return list of users found
     * @throws UnifyException
     *                        if an error occurs
     */
    List<User> findUsers(UserQuery query) throws UnifyException;

    /**
     * Finds user by credentials
     * 
     * @param userName
     *                 the user name
     * @param password
     *                 the user password
     * @return user if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    User findUserByCredentials(String userName, String password) throws UnifyException;
    
    /**
     * Login user to application with login ID and password.
     * 
     * @param loginId
     *                      the login ID
     * @param password
     *                      the password
     * @param loginLocale
     *                      optional login locale
     * @param loginTenantId
     *                      login tenant ID
     * @return the user record
     * @throws UnifyException
     *                        if login ID or password is invalid
     */
    User loginUser(String loginId, String password, Locale loginLocale, Long loginTenantId) throws UnifyException;

    /**
     * Changes a user password for current session user.
     * 
     * @param oldPassword
     *                    the old password
     * @param newPassword
     *                    the new password
     * @throws UnifyException
     *                        if old password is invalid. if password history is
     *                        enabled and new password is stale
     */
    void changeUserPassword(String oldPassword, String newPassword) throws UnifyException;

    /**
     * Resets a user password.
     * 
     * @param userId
     *               the user ID
     * @throws UnifyException
     *                        if an error occurs
     */
    void resetUserPassword(Long userId) throws UnifyException;

    /**
     * Unlocks a user.
     * 
     * @param userId
     *               the user ID
     * @throws UnifyException
     *                        if an error occurs
     */
    void unlockUser(Long userId) throws UnifyException;

    /**
     * Finds user role.
     * 
     * @param userLoginId
     *                    the user login ID
     * @param roleCode
     *                    the role code
     * @return optional user role
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<UserRole> findUserRole(String userLoginId, String roleCode) throws UnifyException;

    /**
     * Finds user roles based on supplied query.
     * 
     * @param query
     *              the search query
     * @return list of user roles
     * @throws UnifyException
     *                        if an error occurs
     */
    List<UserRole> findUserRoles(UserRoleQuery query) throws UnifyException;

    /**
     * Finds consolidated user roles. Combines user roles and all the roles for the
     * groups the user belongs to, if any.
     * 
     * @param userLoginId
     *                    the user login ID
     * @param activeAt
     *                    active time
     * @return the user consolidated role information
     * @throws UnifyException
     *                        if an error occurs
     */
    List<UserRoleInfo> findConsolidatedUserRoles(String userLoginId, Date activeAt) throws UnifyException;

    /**
     * Finds consolidated third-party user roles. Combines user roles and all the
     * roles for the groups the user belongs to, if any.
     * 
     * @param userLoginId
     *                    the user login ID
     * @param activeAt
     *                    active time
     * @return the user consolidated role information
     * @throws UnifyException
     *                        if an error occurs
     */
    List<UserRoleInfo> findConsolidatedThirdpartyUserRoles(String userLoginId, Date activeAt) throws UnifyException;
    
    /**
     * Gets associated branches by hub with branch itself included.
     * 
     * @param branchCode
     *                   the branch code
     * @return the associated branches
     * @throws UnifyException
     *                        if an error occurs
     */
    List<BranchInfo> getAssociatedBranches(String branchCode) throws UnifyException;

    /**
     * Finds a user by login ID.
     * 
     * @param userLoginId
     *                    the user login ID
     * @return the user if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    User findUser(String userLoginId) throws UnifyException;

    /**
     * Gets a user full name by login ID.
     * 
     * @param userLoginId
     *                    the user login ID
     * @return the user full name otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    String getUserFullName(String userLoginId) throws UnifyException;

    /**
     * Finds a user photograph.
     * 
     * @param userLoginId
     *                    the user login ID
     * @return the user photograph if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] findUserPhotograph(String userLoginId) throws UnifyException;
}
