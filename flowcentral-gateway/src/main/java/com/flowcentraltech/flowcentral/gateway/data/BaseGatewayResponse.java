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
package com.flowcentraltech.flowcentral.gateway.data;

import javax.xml.bind.annotation.XmlElement;

import com.flowcentraltech.flowcentral.gateway.constants.GatewayResponseConstants;

/**
 * Abstract base class for gateway responses.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseGatewayResponse {
    
    private String responseCode;

    private String responseMessage;

    public BaseGatewayResponse(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public BaseGatewayResponse() {
        this.responseCode = GatewayResponseConstants.SUCCESS_CODE;
        this.responseMessage = GatewayResponseConstants.SUCCESS_MSG;
    }

    public String getResponseCode() {
        return responseCode;
    }

    @XmlElement
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    @XmlElement
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
