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
package com.flowcentraltech.flowcentral.connect.common.data;

import java.util.List;

import com.flowcentraltech.flowcentral.connect.common.constants.DataSourceOperation;
import com.flowcentraltech.flowcentral.connect.configuration.constants.EvaluationMode;

/**
 * Data source request.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DataSourceRequest extends BaseRequest {

    private DataSourceOperation operation;

    private EvaluationMode evalMode;
    
    private String fieldName;
    
    private String update;
    
    private String query;
    
    private String order;
    
    private Long versionNo;
    
    private boolean ignoreEmptyCriteria;
    
    private int offset;
    
    private int limit;
    
    private List<DataSourceParam> params;
    
    public DataSourceRequest(DataSourceOperation operation, Long id, Long versionNo) {
        this.operation = operation;
        this.versionNo = versionNo;
        setId(id);
    }
    
    public DataSourceRequest(DataSourceOperation operation, EvaluationMode evalMode, Long id, Long versionNo) {
        this.operation = operation;
        this.evalMode = evalMode;
        this.versionNo = versionNo;
        setId(id);
    }

    public DataSourceRequest(DataSourceOperation operation, EvaluationMode evalMode) {
        this.operation = operation;
        this.evalMode = evalMode;
    }

    public DataSourceRequest(DataSourceOperation operation) {
        this.operation = operation;
    }

    public DataSourceRequest() {
        this.limit = -1;
    }
    
    public DataSourceOperation getOperation() {
        return operation;
    }

    public void setOperation(DataSourceOperation operation) {
        this.operation = operation;
    }

    public EvaluationMode getEvalMode() {
        return evalMode;
    }

    public void setEvalMode(EvaluationMode evalMode) {
        this.evalMode = evalMode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public boolean isIgnoreEmptyCriteria() {
        return ignoreEmptyCriteria;
    }

    public void setIgnoreEmptyCriteria(boolean ignoreEmptyCriteria) {
        this.ignoreEmptyCriteria = ignoreEmptyCriteria;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public boolean clause() {
        return getId() != null || versionNo != null || query != null;
    }
    
    public boolean byIdOnly() {
        return getId() != null && versionNo == null;
    }
    
    public boolean byIdVersion() {
        return getId() != null && versionNo != null;
    }
    
    public boolean byQuery() {
        return query != null;
    }
    
    public boolean version() {
        return versionNo != null;
    }
    
    public boolean value() {
        return fieldName != null;
    }
    
    public boolean count() {
        return DataSourceOperation.COUNT_ALL.equals(operation);
    }
    
    public boolean delete() {
        return DataSourceOperation.DELETE_ALL.equals(operation);
    }

    public List<DataSourceParam> getParams() {
        return params;
    }

    public void setParams(List<DataSourceParam> params) {
        this.params = params;
    }
}
