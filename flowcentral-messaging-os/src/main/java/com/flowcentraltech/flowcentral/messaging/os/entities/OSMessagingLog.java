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

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;

/**
 * OS messaging log.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "FC_OSMESSAGINGLOG", indexes = {
        @Index({ "target" }), @Index({ "processor" })})
public class OSMessagingLog extends BaseAuditEntity {

    @Column(length = 32)
    private String target;
    
    @Column(length = 64, nullable = true)
    private String processor;

    @Column(type = ColumnType.CLOB, nullable = true)
    private String requestBody;

    @Column(type = ColumnType.CLOB, nullable = true)
    private String responseBody;

    @Column(length = 10, nullable = true)
    private String responseCode;

    @Column(length = 512, nullable = true)
    private String responseMsg;
    
    @Column(nullable = true)
    private Long runtimeInMilliSec;

    @Override
    public String getDescription() {
        return target;
    }

	public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
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

	public Long getRuntimeInMilliSec() {
		return runtimeInMilliSec;
	}

	public void setRuntimeInMilliSec(Long runtimeInMilliSec) {
		this.runtimeInMilliSec = runtimeInMilliSec;
	}

}
