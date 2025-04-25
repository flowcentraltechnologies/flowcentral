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
package com.flowcentraltech.flowcentral.messaging.os.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;

/**
 * OS messaging async.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_OSMESSAGINGASYNC",
        indexes = { @Index({ "endpoint" }) })
public class OSMessagingAsync extends BaseAuditEntity {

    @Column(name = "ENDPOINT_NM", length = 64)
    private String endpoint;

    @Column(name = "ASYNC_MESSAGE", type = ColumnType.CLOB)
    private String message;

    @Column(name = "NEXT_ATTEMPT_ON")
    private Date nextAttemptOn;

    @Column(name = "PROCESS_BEFORE", nullable = true)
    private Date processBefore;

    @Column(name = "SENT_ON", nullable = true)
    private Date sentOn;

    @Column(name = "RESP_CD", length = 10, nullable = true)
    private String responseCode;

    @Column(name = "RESP_MSG", length=256, nullable = true)
    private String responseMsg;

    @Column(name = "PROCESSING_FG", nullable = true)
    private Boolean processing;
    
    @Override
    public String getDescription() {
        return this.endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getNextAttemptOn() {
        return nextAttemptOn;
    }

    public void setNextAttemptOn(Date nextAttemptOn) {
        this.nextAttemptOn = nextAttemptOn;
    }

    public Date getProcessBefore() {
        return processBefore;
    }

    public void setProcessBefore(Date processBefore) {
        this.processBefore = processBefore;
    }

    public Date getSentOn() {
        return sentOn;
    }

    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Boolean getProcessing() {
        return processing;
    }

    public void setProcessing(Boolean processing) {
        this.processing = processing;
    }

}
