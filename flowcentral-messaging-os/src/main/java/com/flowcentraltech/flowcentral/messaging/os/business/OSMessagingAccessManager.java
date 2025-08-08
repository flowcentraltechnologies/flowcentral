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
package com.flowcentraltech.flowcentral.messaging.os.business;

import java.util.Optional;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingAccess;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingHeader;
import com.tcdng.unify.core.UnifyException;

/**
 * OS messaging access manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface OSMessagingAccessManager extends  FlowCentralService {

    /**
     * Checks if application has messaging access.
     * 
     * @param authorization
     *                      the authorization token
     * @return response the corresponding header
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<OSMessagingHeader> resolveAccess(String authorization) throws UnifyException;

    /**
     * Logs messaging access.
     * 
     * @param access
     *                    the access object
     * @throws UnifyException
     *                        if an error occurs
     */
    void logAccess(OSMessagingAccess access) throws UnifyException;
    
    /**
     * Gets target authorization.
     * 
     * @param target
     *                  the target OS application ID
     * @param processor
     *                  the target processor
     * @return the authorization
     * @throws UnifyException
     *                        if not authorized. if an error occurs
     */
    String getTargetAuthorization(String target, String processor) throws UnifyException;
}
