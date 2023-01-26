/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.util;

import java.util.Map;

/**
 * Application replication context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationReplicationContext {

    private final Map<String, String> messageSwaps;

    private final Map<String, String> componentSwaps;
    
    public ApplicationReplicationContext(Map<String, String> messageSwaps,
            Map<String, String> componentSwaps) {
        this.messageSwaps = messageSwaps;
        this.componentSwaps = componentSwaps;
    }
    
    public String messageSwap(String message) {
        if (message != null) {
            for (Map.Entry<String, String> entry: messageSwaps.entrySet()) {
                message = message.replaceAll(entry.getKey(), entry.getValue());
            }
            
            return message;
        }
        
        return null;
    }
    
    public String componentSwap(String component) {
        if (component != null) {
            for (Map.Entry<String, String> entry: componentSwaps.entrySet()) {
                if (component.startsWith(entry.getKey())) {
                    return entry.getValue() + component.substring(entry.getKey().length());
                }
            }
        }
        
        return null;
    }
}
