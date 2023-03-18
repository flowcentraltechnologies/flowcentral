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

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.flowcentraltech.flowcentral.common.entities.ParamValues;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Persistence data object for read configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Policy("readconfig-entitypolicy")
@Table(name = "FC_READCONFIG",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class ReadConfig extends BaseStatusEntity {

    @Column(name = "READCONFIG_NM", length = 32)
    private String name;

    @Column(name = "READCONFIG_DESC", length = 64)
    private String description;

    @Column(length = 64)
    private String endpointReader;

    @Column(length = 64)
    private String eventProcessor;

    @Column(length = 64, nullable = true)
    private String processorRule;

    @Column
    private Integer endpointReaderPeriod;

    @Column
    private Integer endpointMaxLoadingSize;

    @Child(category = "integration-readconfig")
    private ParamValues readerParams;

    @Child
    private ReadConfigProc readConfigProc;

    @Override
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndpointReader() {
        return endpointReader;
    }

    public void setEndpointReader(String endpointReader) {
        this.endpointReader = endpointReader;
    }

    public Integer getEndpointReaderPeriod() {
        return endpointReaderPeriod;
    }

    public void setEndpointReaderPeriod(Integer endpointReaderPeriod) {
        this.endpointReaderPeriod = endpointReaderPeriod;
    }

    public Integer getEndpointMaxLoadingSize() {
        return endpointMaxLoadingSize;
    }

    public void setEndpointMaxLoadingSize(Integer endpointMaxLoadingSize) {
        this.endpointMaxLoadingSize = endpointMaxLoadingSize;
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

    public ParamValues getReaderParams() {
        return readerParams;
    }

    public void setReaderParams(ParamValues readerParams) {
        this.readerParams = readerParams;
    }

    public ReadConfigProc getReadConfigProc() {
        return readConfigProc;
    }

    public void setReadConfigProc(ReadConfigProc readConfigProc) {
        this.readConfigProc = readConfigProc;
    }

}
