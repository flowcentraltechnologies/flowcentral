/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.os.messaging.data;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Messaging access.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class MessagingAccess {
    
    private String application;
    
    private String authorization;
    
    private String processor;
    
    private String responseCode;
    
    private String responseMessage;
    
    private String responseBody;
    
    private String requestBody;
    
    private Long runtimeInMilliSec;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
    
    public Long getRuntimeInMilliSec() {
        return runtimeInMilliSec;
    }

    public void setRuntimeInMilliSec(Long runtimeInMilliSec) {
        this.runtimeInMilliSec = runtimeInMilliSec;
    }

    public boolean isWithAuthorization() {
        return !StringUtils.isBlank(authorization);
    }
}
