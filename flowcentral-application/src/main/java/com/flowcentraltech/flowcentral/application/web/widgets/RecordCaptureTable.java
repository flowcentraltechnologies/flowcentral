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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AbstractRecordCapture;
import com.flowcentraltech.flowcentral.application.data.RecordCaptureTableDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Record capture table.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class RecordCaptureTable<T extends AbstractRecordCapture> {
    
    private RecordCaptureTableDef tableDef;
    
    private List<T> records;
    
    private boolean editable;

    public RecordCaptureTable(RecordCaptureTableDef tableDef) {
        this.tableDef = tableDef;
        this.records = Collections.emptyList();
    }

    public RecordCaptureTableDef getTableDef() {
        return tableDef;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public List<T> getRecords() {
        return records;
    }
    
    public void clear() throws UnifyException {
        if (!DataUtils.isBlank(records)) {
            for (ValueStore valueStore: new BeanValueListStore(records)) {
                for(String fieldName: tableDef.getCaptureFields()) {
                    valueStore.store(fieldName, null);
                }
            }
        }
    }

}
