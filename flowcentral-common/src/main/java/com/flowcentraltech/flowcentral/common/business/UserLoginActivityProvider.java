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

package com.flowcentraltech.flowcentral.common.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.UserRoleInfo;
import com.tcdng.unify.core.UnifyException;

/**
 * User login activity provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface UserLoginActivityProvider extends FlowCentralComponent {

    /**
     * Gets available user roles excluding supplied role that are active now.
     * 
     * @param userLoginId
     *                        the user login ID
     * @param excludeRoleCode
     *                        the code of the role to exclude
     * @return the user role list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<UserRoleInfo> getAvailableUserRolesActiveNow(String userLoginId, String excludeRoleCode) throws UnifyException;

    /**
     * Logs out current session user.
     * 
     * @param complete
     *                 indicates if session should be nullified.
     * @throws UnifyException
     *                        if an error occurs
     */
    void logoutUser(boolean complete) throws UnifyException;

}
