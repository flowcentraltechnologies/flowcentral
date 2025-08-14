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
package com.flowcentraltech.flowcentral.messaging.os.data;

/**
 * Abstract base class for OS messaging requests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1 
 */
public abstract class BaseOSMessagingReq {   
    
    private final String processor;

    private String source;

    protected BaseOSMessagingReq(String processor) {
        this.processor = processor;
    }

    public final String getProcessor() {
        return processor;
    }

    public final String getSource() {
        return source;
    }

    public final void setSource(String source) {
        this.source = source;
    }
      
}
