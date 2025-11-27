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

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingMode;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * OS messaging processing log.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "FC_OSMESSAGINGPROCLOG", indexes = {
        @Index({ "source" }), @Index({ "processor" }), @Index({ "correlationId" })})
public class OSMessagingProcessingLog extends BaseEntity {

    @ForeignKey
    private OSMessagingMode mode;

    @Column(name = "CORRELATION_ID", length = 32, nullable = true)
    private String correlationId;

    @Column(length = 32)
    private String source;
    
    @Column(length = 64)
    private String processor;

    @Column(length = 1024)
    private String summary;

    @Column(length = 10)
    private String responseCode;

    @Column(length = 1024)
    private String responseMsg;

    @Column(type = ColumnType.TIMESTAMP)
    private Date createDate;
    
    @ListOnly(key = "mode", property = "description")
    private String modeDesc;

    public OSMessagingProcessingLog(OSMessagingMode mode, String correlationId, String source, String processor, String summary,
            String responseCode, String responseMsg, Date createDate) {
        this.mode = mode;
        this.correlationId = correlationId;
        this.source = source;
        this.processor = processor;
        this.summary = summary;
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.createDate = createDate;
    }

    public OSMessagingProcessingLog() {
        
    }
    
    @Override
    public String getDescription() {
        return summary;
    }

    public OSMessagingMode getMode() {
        return mode;
    }

    public void setMode(OSMessagingMode mode) {
        this.mode = mode;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModeDesc() {
        return modeDesc;
    }

    public void setModeDesc(String modeDesc) {
        this.modeDesc = modeDesc;
    }

}
