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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Record capture table definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class RecordCaptureTableDef {

    private Class<? extends AbstractRecordCapture> recordClass;

    private List<RecordCaptureColumnDef> columnDefs;

    private List<String> captureFields;

    private boolean serialNo;

    private RecordCaptureTableDef(boolean serialNo, Class<? extends AbstractRecordCapture> recordClass,
            List<RecordCaptureColumnDef> columnDefs, List<String> captureFields) {
        this.serialNo = serialNo;
        this.recordClass = recordClass;
        this.columnDefs = columnDefs;
        this.captureFields = captureFields;
    }

    public boolean isSerialNo() {
        return serialNo;
    }

    public Class<? extends AbstractRecordCapture> getRecordClass() {
        return recordClass;
    }

    public List<RecordCaptureColumnDef> getColumnDefs() {
        return columnDefs;
    }

    public RecordCaptureColumnDef getRecordCaptureColumnDef(int index) {
        return columnDefs.get(index);
    }

    public List<String> getCaptureFields() {
        return captureFields;
    }

    public static Builder newBuilder(Class<? extends AbstractRecordCapture> recordClass) {
        return new Builder(recordClass);
    }

    public static class Builder {

        private String recordNoCaption;

        private String titleCaption;

        private boolean serialNo;

        private Class<? extends AbstractRecordCapture> recordClass;

        private List<RecordCaptureColumnDef> columnDefs;

        private List<String> captureFields;

        public Builder(Class<? extends AbstractRecordCapture> recordClass) {
            this.recordClass = recordClass;
            this.columnDefs = new ArrayList<RecordCaptureColumnDef>();
            this.captureFields = new ArrayList<String>();
        }

        public Builder recordNoCaption(String recordNoCaption) {
            this.recordNoCaption = recordNoCaption;
            return this;
        }

        public Builder titleCaption(String titleCaption) {
            this.titleCaption = titleCaption;
            return this;
        }

        public Builder serialNo(boolean serialNo) {
            this.serialNo = serialNo;
            return this;
        }

        public Builder addColumnDef(String fieldName, String caption, String cellEditor, String cellRenderer)
                throws UnifyException {
            if ("recordNo".equals(fieldName) || "title".equals(fieldName)) {
                throw new IllegalArgumentException("Supplied field name is reserved.");
            }

            if (!ReflectUtils.isGettableField(recordClass, fieldName)
                    || !ReflectUtils.isSettableField(recordClass, fieldName)) {
                throw new IllegalArgumentException(
                        "Record class associated with this builder has no getter and setter for supplied field.");
            }

            captureFields.add(fieldName);
            columnDefs.add(new RecordCaptureColumnDef(fieldName, caption, cellEditor + " binding:" + fieldName,
                    cellRenderer + " binding:" + fieldName));
            return this;
        }

        public RecordCaptureTableDef build() throws UnifyException {
            if (StringUtils.isBlank(recordNoCaption) && StringUtils.isBlank(titleCaption)) {
                throw new IllegalArgumentException(
                        "Title caption or record number caption has not been set for this builder.");
            }

            if (StringUtils.isBlank(titleCaption)) {
                columnDefs.add(0, new RecordCaptureColumnDef("title", titleCaption, "!ui-label", "!ui-label"));
            }

            if (StringUtils.isBlank(recordNoCaption)) {
                columnDefs.add(0, new RecordCaptureColumnDef("recordNo", recordNoCaption, "!ui-label", "!ui-label"));
            }

            return new RecordCaptureTableDef(serialNo, recordClass, DataUtils.unmodifiableList(columnDefs),
                    DataUtils.unmodifiableList(captureFields));
        }
    }

}
