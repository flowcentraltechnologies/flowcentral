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
package com.flowcentraltech.flowcentral.integration.business;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.tcdng.unify.core.UnifyException;

/**
 * Integration module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface IntegrationModuleService extends FlowCentralService {

    /**
     * Sends a message to an endpoint destination.
     * 
     * @param endpointConfigName
     *                           the endpoint configuration
     * @param destination
     *                           the destination
     * @param text
     *                           the message
     * @throws UnifyException
     *                        if an error occurs
     */
    void sendMessage(String endpointConfigName, String destination, String text) throws UnifyException;

    /**
     * Receives a message from an endpoint source.
     * 
     * @param endpointConfigName
     *                           the endpoint configuration
     * @param source
     *                           the source
     * @return the message if available otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    String receiveMessage(String endpointConfigName, String source) throws UnifyException;
}
