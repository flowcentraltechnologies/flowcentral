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
package com.flowcentraltech.flowcentral.connect.common.data;

/**
 * Procedure request.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ProcedureRequest extends BaseRequest {

    private String operation;

    private Long versionNo;

    private boolean useRawPayload;

    private boolean readOnly;

    public ProcedureRequest(String operation, Long id, Long versionNo) {
        this.operation = operation;
        this.versionNo = versionNo;
        setId(id);
    }

    public ProcedureRequest(String operation) {
        this.operation = operation;
    }

    public ProcedureRequest() {

    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isUseRawPayload() {
        return useRawPayload;
    }

    public void setUseRawPayload(boolean useRawPayload) {
        this.useRawPayload = useRawPayload;
    }

}
