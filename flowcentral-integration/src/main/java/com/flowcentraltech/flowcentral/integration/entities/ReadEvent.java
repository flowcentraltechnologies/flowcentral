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
package com.flowcentraltech.flowcentral.integration.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.integration.endpoint.reader.EndpointReadEventStatus;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Read event entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_READEVENT")
public class ReadEvent extends BaseAuditEntity {

    @ForeignKey(ReadConfigProc.class)
    private Long readConfigProcId;

    @ForeignKey(name = "REC_ST")
    private EndpointReadEventStatus status;

    @Column(length = 64)
    private String node;

    @ListOnly(key = "readConfigProcId", property = "readConfigId")
    private Long readConfigId;

    @ListOnly(key = "readConfigProcId", property = "readConfigName")
    private String readConfigName;

    @ListOnly(key = "readConfigProcId", property = "endpointReader")
    private String endpointReader;

    @ListOnly(key = "readConfigProcId", property = "eventProcessor")
    private String eventProcessor;

    @ListOnly(key = "readConfigProcId", property = "processorRule")
    private String processorRule;

    @ListOnly(key = "readConfigProcId", property = "inProcessFlag")
    private Boolean inProcessFlag;

    @ChildList
    private List<ReadEventMessage> messageList;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getReadConfigProcId() {
        return readConfigProcId;
    }

    public void setReadConfigProcId(Long readConfigProcId) {
        this.readConfigProcId = readConfigProcId;
    }

    public EndpointReadEventStatus getStatus() {
        return status;
    }

    public void setStatus(EndpointReadEventStatus status) {
        this.status = status;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Long getReadConfigId() {
        return readConfigId;
    }

    public void setReadConfigId(Long readConfigId) {
        this.readConfigId = readConfigId;
    }

    public String getReadConfigName() {
        return readConfigName;
    }

    public void setReadConfigName(String readConfigName) {
        this.readConfigName = readConfigName;
    }

    public String getEndpointReader() {
        return endpointReader;
    }

    public void setEndpointReader(String endpointReader) {
        this.endpointReader = endpointReader;
    }

    public String getEventProcessor() {
        return eventProcessor;
    }

    public void setEventProcessor(String eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    public String getProcessorRule() {
        return processorRule;
    }

    public void setProcessorRule(String processorRule) {
        this.processorRule = processorRule;
    }

    public Boolean getInProcessFlag() {
        return inProcessFlag;
    }

    public void setInProcessFlag(Boolean inProcessFlag) {
        this.inProcessFlag = inProcessFlag;
    }

    public List<ReadEventMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<ReadEventMessage> messageList) {
        this.messageList = messageList;
    }

}
