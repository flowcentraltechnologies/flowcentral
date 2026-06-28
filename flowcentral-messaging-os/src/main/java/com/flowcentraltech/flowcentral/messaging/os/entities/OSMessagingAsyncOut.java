/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.messaging.os.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;

/**
 * OS messaging async out.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_OSMESSAGINGASYNCOUT", indexes = { @Index({ "target" }), @Index({ "correlationId" }) })
public class OSMessagingAsyncOut extends BaseAuditEntity {

    @Column(name = "TARGET", length = 32)
    private String target;

    @Column(name = "CORRELATION_ID", length = 32)
    private String correlationId;

    @Column(name = "PROCESSOR", length = 64)
    private String processor;

    @Column(name = "DELEGATE_FUNC", length = 64, nullable = true)
    private String function;

    @Column(name = "DELEGATE_SERVICE", length = 64, nullable = true)
    private String service;

    @Column(name = "USER_LOGIN_ID", length = 64, nullable = true)
    private String userLoginId;

    @Column(name = "ASYNC_MESSAGE", type = ColumnType.CLOB)
    private String message;

    @Column(name = "SENT_ON", type = ColumnType.TIMESTAMP, nullable = true)
    private Date sentOn;

    @Column(name = "ERROR_CD", length = 10, nullable = true)
    private String errorCode;
    
    @Column(name = "ERROR_MSG", length = 1024, nullable = true)
    private String errorMessage;
    
    @Override
    public String getDescription() {
        return target + " " + processor + (correlationId != null ? " " + correlationId : "");
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentOn() {
        return sentOn;
    }

    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
