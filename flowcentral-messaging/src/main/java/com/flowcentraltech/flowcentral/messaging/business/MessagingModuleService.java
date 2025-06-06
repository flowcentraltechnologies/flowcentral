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
package com.flowcentraltech.flowcentral.messaging.business;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.messaging.data.Message;
import com.tcdng.unify.core.UnifyException;

/**
 * Messaging module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface MessagingModuleService extends FlowCentralService {

    /**
     * Sends a message.
     * 
     * @param message
     *                the message to send
     * @throws UnifyException
     *                        if an error occurs
     */
    void sendMessage(Message message) throws UnifyException;

    /**
     * Receives a message.
     * 
     * @param config
     *               the channel configuration
     * @param source
     *               the message source
     * @return message is available otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Message receiveMessage(String config, String source) throws UnifyException;

}
