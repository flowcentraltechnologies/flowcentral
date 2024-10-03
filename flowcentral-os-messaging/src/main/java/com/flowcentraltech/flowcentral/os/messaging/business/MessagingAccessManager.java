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
package com.flowcentraltech.flowcentral.os.messaging.business;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.os.messaging.data.MessagingAccess;
import com.flowcentraltech.flowcentral.os.messaging.data.MessagingError;
import com.tcdng.unify.core.UnifyException;

/**
 * Messaging access manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface MessagingAccessManager extends FlowCentralComponent {

    /**
     * Checks if application has messaging access.
     * 
     * @param authorization
     *                      the authorization token
     * @param application
     *                      the application
     * @param processor
     *                      the processor
     * @return response on error otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    MessagingError checkAccess(String authorization, String application, String processor) throws UnifyException;

    /**
     * Logs messaging access.
     * 
     * @param access
     *                    the access object
     * @throws UnifyException
     *                        if an error occurs
     */
    void logAccess(MessagingAccess access) throws UnifyException;
}
