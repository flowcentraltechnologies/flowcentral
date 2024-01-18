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
package com.flowcentraltech.flowcentral.messaging.readers;

import com.flowcentraltech.flowcentral.messaging.data.Message;
import com.flowcentraltech.flowcentral.messaging.data.MessageHeader;
import com.tcdng.unify.core.UnifyException;

/**
 * Messaging JSON consumer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractMessagingJsonConsumer<T> extends AbstractMessagingConsumer {

    private final Class<T> messageType;
    
    protected AbstractMessagingJsonConsumer(Class<T> messageType) {
        this.messageType = messageType;
    }

    @Override
    public void consume(Message message) throws UnifyException {
        MessageHeader header = message.getHeader();
        if (header.isWithHandler()) {
            String handlerName = resolveHandler(header.getHandler());
            MessagingHandler handler = getComponent(MessagingHandler.class, handlerName);
            Object msg = fromJson(handler.getMessageType(), message.getBody());
            handler.process(msg);
            return;
        }

        T msg = fromJson(messageType, message.getBody());
        process(msg);
    }

    protected abstract String resolveHandler(String handler) throws UnifyException;

    protected abstract void process(T msg) throws UnifyException;
}
