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
 * Abstract base class for OS messaging responses.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseOSMessagingResp {

    private String originalRequestId;
   
    private String responseCode;

    private String responseMessage;

    public BaseOSMessagingResp(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public BaseOSMessagingResp() {
        this.responseCode = OSMessagingResponseConstants.SUCCESS_CODE;
        this.responseMessage = OSMessagingResponseConstants.SUCCESS_MSG;
    }

    public String getOriginalRequestId() {
        return originalRequestId;
    }

    public void setOriginalRequestId(String originalRequestId) {
        this.originalRequestId = originalRequestId;
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
    
    public boolean isSuccessful() {
        return OSMessagingResponseConstants.SUCCESS_CODE.equals(responseCode);
    }

}
