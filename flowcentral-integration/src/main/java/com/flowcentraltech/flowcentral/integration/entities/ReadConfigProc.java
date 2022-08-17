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
package com.flowcentraltech.flowcentral.integration.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Read configuration processing entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_READCONFIGPROC", uniqueConstraints = { @UniqueConstraint({ "readConfigId" }) })
public class ReadConfigProc extends BaseEntity {

    @ForeignKey(ReadConfig.class)
    private Long readConfigId;

    @Column
    private Date lastProcessTime;

    @Column
    private Boolean inProcessFlag;

    @Column
    private long successCounter;

    @Column
    private long failureCounter;

    @ListOnly(key = "readConfigId", property = "name")
    private String readConfigName;

    @ListOnly(key = "readConfigId", property = "endpointReader")
    private String endpointReader;

    @ListOnly(key = "readConfigId", property = "eventProcessor")
    private String eventProcessor;

    @ListOnly(key = "readConfigId", property = "processorRule")
    private String processorRule;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getReadConfigId() {
        return readConfigId;
    }

    public void setReadConfigId(Long readConfigId) {
        this.readConfigId = readConfigId;
    }

    public Date getLastProcessTime() {
        return lastProcessTime;
    }

    public void setLastProcessTime(Date lastProcessTime) {
        this.lastProcessTime = lastProcessTime;
    }

    public Boolean getInProcessFlag() {
        return inProcessFlag;
    }

    public void setInProcessFlag(Boolean inProcessFlag) {
        this.inProcessFlag = inProcessFlag;
    }

    public long getSuccessCounter() {
        return successCounter;
    }

    public void setSuccessCounter(long successCounter) {
        this.successCounter = successCounter;
    }

    public long getFailureCounter() {
        return failureCounter;
    }

    public void setFailureCounter(long failureCounter) {
        this.failureCounter = failureCounter;
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

}
