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
package com.flowcentraltech.flowcentral.messaging.readers;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Messaging handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractMessagingHandler<T> extends AbstractFlowCentralComponent implements MessagingHandler {

    private final Class<T> messageType;
    
    protected AbstractMessagingHandler(Class<T> messageType) {
        this.messageType = messageType;
    }

    @Override
    public Class<?> getMessageType() {
        return messageType;
    }   
    
    @SuppressWarnings("unchecked")
    @Override
    public void process(Object msg) throws UnifyException {
        doProcess((T) msg);
    }

    @Override
    protected void onInitialize() throws UnifyException {
        
    }

    @Override
    protected void onTerminate() throws UnifyException {
        
    }

    protected abstract void doProcess(T msg) throws UnifyException;
}
