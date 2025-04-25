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
package com.flowcentraltech.flowcentral.messaging.data;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Message header.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class MessageHeader {

    private String config;
    
    private String destination;
    
    private String handler;

    public MessageHeader(String config, String destination, String handler) {
        this.config = config;
        this.destination = destination;
        this.handler = handler;
    }

    public MessageHeader(String config, String destination) {
        this.config = config;
        this.destination = destination;
        this.handler = null;
    }

    public String getConfig() {
        return config;
    }

    public String getDestination() {
        return destination;
    }

    public String getHandler() {
        return handler;
    }

    public boolean isWithHandler() {
        return !StringUtils.isBlank(handler);
    }
}
