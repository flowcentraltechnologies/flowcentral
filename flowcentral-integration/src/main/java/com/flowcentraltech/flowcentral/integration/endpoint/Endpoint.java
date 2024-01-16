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
package com.flowcentraltech.flowcentral.integration.endpoint;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.tcdng.unify.core.UnifyException;

/**
 * End-point.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface Endpoint extends FlowCentralComponent {

    /**
     * Sets up end-point.
     * 
     * @param endpointDef
     *                    the end-point definition
     * @throws UnifyException
     *                        if end-point is already setup. if an error occurs
     */
    void setup(EndpointDef endpointDef) throws UnifyException;

    /**
     * Sends a message to a destination through endpoint.
     * 
     * @param destination
     *                    the destination
     * @param text
     *                    the message
     * @throws UnifyException
     *                        if an error occurs
     */
    void sendMessage(String destination, String text) throws UnifyException;

    /**
     * Receives a message from a source through endpoint
     * 
     * @param source
     *               the source
     * @return the message if present otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    String receiveMessage(String source) throws UnifyException;

}
