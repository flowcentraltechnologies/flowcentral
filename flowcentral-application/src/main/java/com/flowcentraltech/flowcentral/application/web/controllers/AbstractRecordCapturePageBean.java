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
package com.flowcentraltech.flowcentral.application.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AbstractRecordCapture;
import com.flowcentraltech.flowcentral.application.data.RecordCaptureTableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.RecordCaptureTable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Abstract record capture page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractRecordCapturePageBean<T extends AbstractRecordCapture> extends AbstractPageBean {

    private RecordCaptureTable<T> table;
    
    public AbstractRecordCapturePageBean(RecordCaptureTableDef tableDef) {
        this.table = new RecordCaptureTable<T>(tableDef);
    }

    public RecordCaptureTable<T> getTable() {
        return table;
    }

    public boolean isEditable() {
        return table.isEditable();
    }

    public void setEditable(boolean editable) {
        table.setEditable(editable);
    }

    public void setRecords(List<T> records) {
        table.setRecords(records);
    }

    public List<T> getRecords() {
        return table.getRecords();
    }
    
    public void clear() throws UnifyException {
        table.clear();
    }
}
