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
import com.flowcentraltech.flowcentral.common.data.SecuredLinkContentInfo;
import com.flowcentraltech.flowcentral.common.data.SecuredLinkInfo;
import com.tcdng.unify.core.UnifyException;

/**
 * Secured link manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface SecuredLinkManager extends FlowCentralComponent {

    /**
     * Creates a new a secured link.
     * 
     * @param title
     *                            the title
     * @param contentPath
     *                            the content path
     * @param expirationInMinutes
     *                            expiration in minutes
     * @return the secured link information
     * @throws UnifyException
     *                        if an error occurs
     */
    SecuredLinkInfo getNewSecuredLink(String title, String contentPath, int expirationInMinutes) throws UnifyException;

    /**
     * Creates a new secured link.
     * 
     * @param title
     *                            the title
     * @param contentPath
     *                            the content path
     * @param assignedLoginId
     *                            the assigned login ID
     * @param expirationInMinutes
     *                            expiration in minutes
     * @return the secured link information
     * @throws UnifyException
     *                        if an error occurs
     */
    SecuredLinkInfo getNewSecuredLink(String title, String contentPath, String assignedLoginId, int expirationInMinutes)
            throws UnifyException;

    /**
     * Creates a new secured link.
     * 
     * @param title
     *                            the title
     * @param contentPath
     *                            the content path
     * @param assignedLoginId
     *                            the assigned login ID
     * @param assignedRole
     *                            the assigned role
     * @param expirationInMinutes
     *                            expiration in minutes
     * @return the secured link information
     * @throws UnifyException
     *                        if an error occurs
     */
    SecuredLinkInfo getNewSecuredLink(String title, String contentPath, String assignedLoginId, String assignedRole,
            int expirationInMinutes) throws UnifyException;

    /**
     * Gets secured link.
     * 
     * @param linkId
     *               the link ID
     * @return the secured link information
     * @throws UnifyException
     *                        if an error occurs
     */
    SecuredLinkContentInfo getSecuredLink(Long linkId) throws UnifyException;
}
