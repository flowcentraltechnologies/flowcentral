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
package com.flowcentraltech.flowcentral.messaging.os.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingEndpoint;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingEndpointQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * OS messaging module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface OSMessagingModuleService extends FlowCentralService {

    /**
     * Finds messaging endpoints.
     * 
     * @param query
     *              the search criteria
     * @return list of endpoints
     * @throws UnifyException
     *                        if an error occurs
     */
    List<OSMessagingEndpoint> findMessagingEndpoints(OSMessagingEndpointQuery query) throws UnifyException;
    
    /**
     * Sends synchronous message.
     * 
     * @param respClass
     *                     the response class
     * @param request
     *                     the message
     * @param endpointName
     *                     the messaging end-point.
     * @return the response object
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseOSMessagingResp, U extends BaseOSMessagingReq> T sendSynchronousMessage(Class<T> respClass,
            U request, String endpointName) throws UnifyException;

    /**
     * Sends asynchronous message.
     * 
     * @param request
     *                     the message
     * @param endpointName
     *                     the messaging end-point.
     * @return the response object
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseOSMessagingReq> void sendAsynchronousMessage(T request, String endpointName) throws UnifyException;

    /**
     * Sends asynchronous message with a delay.
     * 
     * @param request
     *                     the message
     * @param endpointName
     *                     the messaging end-point.
     * @param delayInSecs
     *                     the delay in seconds.
     * @return the response object
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseOSMessagingReq> void sendAsynchronousMessage(T request, String endpointName, long delayInSecs)
            throws UnifyException;
}
