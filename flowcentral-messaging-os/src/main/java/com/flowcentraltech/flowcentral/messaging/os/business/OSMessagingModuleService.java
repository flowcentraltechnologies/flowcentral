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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingMode;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingReq;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.flowcentraltech.flowcentral.messaging.os.data.OSInfo;
import com.flowcentraltech.flowcentral.messaging.os.data.OSMessagingHeader;
import com.flowcentraltech.flowcentral.messaging.os.data.OSResponse;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingPeerEndpoint;
import com.flowcentraltech.flowcentral.messaging.os.entities.OSMessagingPeerEndpointQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.util.ContentDisposition;

/**
 * OS messaging module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface OSMessagingModuleService extends FlowCentralService {

    /**
     * Gets container OS information.
     * 
     * @return the OS information.
     * @throws UnifyException
     *                        if an error occurs
     */
    OSInfo getOSInfo() throws UnifyException;
    
    /**
     * Gets a peer end-point short name.
     * 
     * @param appId
     *              the end-point app ID
     * @return optional short name
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> getPeerEndpointShortName(String appId) throws UnifyException;
    
    /**
     * Gets a peer end-point URL.
     * 
     * @param appId
     *              the end-point app ID
     * @return optional endpoint URL
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> getPeerEndpointURL(String appId) throws UnifyException;
    
    /**
     * Finds OS messaging endpoints.
     * 
     * @param query
     *              the endpoint query
     * @return the list of messaging endpoints
     * @throws UnifyException
     *                        if an error occurs
     */
    List<OSMessagingPeerEndpoint> findOSMessagingEndpoints(OSMessagingPeerEndpointQuery query) throws UnifyException;

    /**
     * Gets OS messaging header based on supplied authorization.
     * 
     * @param authorization
     *                      the authorization
     * @return the OS messaging header
     * @throws UnifyException
     *                        if an error occurs
     */
    OSMessagingHeader getOSMessagingHeader(String authorization) throws UnifyException;

    /**
     * Sends a synchronous message to delegate function.
     * 
     * @param header
     *                    origin header information
     * @param function
     *                    the delegate function
     * @param correlationId
     *                    the correlation ID
     * @param userloginId
     *                    the user login ID
     * @param requestJson
     *                    the request message
     * @return response message
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> sendSynchronousMessageToDelegate(OSMessagingHeader header, String function, String correlationId,
            String userloginId, String requestJson) throws UnifyException;

    /**
     * Sends a synchronous message to delegate service.
     * 
     * @param header
     *                    origin header information
     * @param service
     *                    the delegate service
     * @param correlationId
     *                    the correlation ID
     * @param userloginId
     *                    the user login ID
     * @param requestJson
     *                    the request message
     * @return response message
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> sendSynchronousMessageToService(OSMessagingHeader header, String service, String correlationId,
            String userloginId, String requestJson) throws UnifyException;

    /**
     * Sends a asynchronous message to delegate function.
     * 
     * @param header
     *                    origin header information
     * @param function
     *                    the delegate function
     * @param correlationId
     *                    the correlation ID
     * @param userloginId
     *                    the user login ID
     * @param requestJson
     *                    the request message
     * @return response message
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> sendAsynchronousMessageToDelegate(OSMessagingHeader header, String function, String correlationId,
            String userloginId, String requestJson) throws UnifyException;

    /**
     * Sends a asynchronous message to delegate service.
     * 
     * @param header
     *                    origin header information
     * @param service
     *                    the delegate service
     * @param correlationId
     *                    the correlation ID
     * @param userloginId
     *                    the user login ID
     * @param requestJson
     *                    the request message
     * @return response message
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> sendAsynchronousMessageToService(OSMessagingHeader header, String service, String correlationId,
            String userloginId, String requestJson) throws UnifyException;

    /**
     * Sends an upload message to delegate function.
     * 
     * @param header
     *                    origin header information
     * @param function
     *                    the delegate function
     * @param correlationId the correlation ID
     * @param userloginId
     *                    the user login ID
     * @param fileSignature the file Signature
     * @param disposition the content disposition
     * @param in
     *                    the input stream
     * @return response message
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> sendUploadMessageToDelegate(OSMessagingHeader header, String function, String correlationId,
            String userloginId, String fileSignature, ContentDisposition disposition, InputStream in) throws UnifyException;

    /**
     * Sends an upload message to delegate service.
     * 
     * @param header
     *                    origin header information
     * @param service
     *                    the delegate service
     * @param correlationId the correlation ID
     * @param userloginId
     *                    the user login ID
     * @param fileSignature the file Signature
     * @param disposition    the content disposition
     * @param in
     *                    the input stream
     * @return response message
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> sendUploadMessageToService(OSMessagingHeader header, String service, String correlationId,
            String userloginId, String fileSignature, ContentDisposition disposition, InputStream in) throws UnifyException;

    /**
     * Sends a download message to delegate function.
     * 
     * @param header
     *                    origin header information
     * @param function
     *                    the delegate function
     * @param correlationId the correlation ID
     * @param userloginId
     *                    the user login ID
     * @param fileSignature the file Signature
     * @param out
     *                    the output stream
     * @return response message
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> sendDownloadMessageToDelegate(OSMessagingHeader header, String function, String correlationId,
            String userloginId, String fileSignature, OutputStream out) throws UnifyException;

    /**
     * Sends a download message to delegate service.
     * 
     * @param header
     *                    origin header information
     * @param service
     *                    the delegate service
     * @param correlationId the correlation ID
     * @param userloginId
     *                    the user login ID
     * @param fileSignature the file Signature
     * @param disposition    the content disposition
     * @param out
     *                    the output stream
     * @return response message
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<String> sendDownloadMessageToService(OSMessagingHeader header, String service, String correlationId,
            String userloginId, String fileSignature, OutputStream out) throws UnifyException;

    /**
     * Sends synchronous message.
     * 
     * @param respClass
     *                  the response class
     * @param request
     *                  the message
     * @return the response object
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseOSMessagingResp, U extends BaseOSMessagingReq> T sendSynchronousMessage(Class<T> respClass,
            U request) throws UnifyException;

    /**
     * Sends asynchronous message.
     * 
     * @param request
     *                the message
     * @return the correlation ID
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseOSMessagingReq> String sendAsynchronousMessage(T request) throws UnifyException;

    /**
     * Sends asynchronous message with a delay.
     * 
     * @param request
     *                    the message
     * @param delayInSecs
     *                    the delay in seconds.
     * @return the correlation ID
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseOSMessagingReq> String sendAsynchronousMessage(T request, long delayInSecs)
            throws UnifyException;
    
    /**
     * Get asynchronous acknowledgement.
     * 
     * @param correlationdId
     *                       the message correlation ID
     * @return the response object
     * @throws UnifyException
     *                        if an error occurs
     */
    OSResponse getAsynchronousAck(String correlationdId) throws UnifyException;
    
    /**
     * Logs message processing.
     * 
     * @param mode
     *                     the messaging mode
     * @param correlationdId the correlation 
     * @param source
     *                     the source
     * @param processor
     *                     the processor
     * @param summary
     *                     the summary
     * @param responseCode
     *                     the response code
     * @param responseMsg
     *                     the response message
     * @throws UnifyException
     *                        if an error occurs
     */
    void logProcessing(OSMessagingMode mode, String correlationdId, String source, String processor, String summary,
            String responseCode, String responseMsg) throws UnifyException;

}
