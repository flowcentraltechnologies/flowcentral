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
import com.flowcentraltech.flowcentral.application.data.RecordCaptureColumnDef;
import com.flowcentraltech.flowcentral.application.data.RecordCaptureTableDef;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralValueListMultiControl;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Record capture table widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-recordcapturetable")
public class RecordCaptureTableWidget
        extends AbstractFlowCentralValueListMultiControl<ValueStore, AbstractRecordCapture> {

    private RecordCaptureTable<? extends AbstractRecordCapture> oldTable;

    public String getRowId() throws UnifyException {
        return getPrefixedId("row_");
    }

    @Override
    public void populate(DataTransferBlock transferBlock) throws UnifyException {
        if (transferBlock != null) {
            DataTransferBlock childBlock = transferBlock.getChildBlock();
            ChildWidgetInfo childWidgetInfo = getChildWidgetInfo(childBlock.getId());
            Control control = (Control) childWidgetInfo.getWidget();
            control.setValueStore(getValueList().get(childBlock.getItemIndex()));
            control.populate(childBlock);
        }
    }

    @SuppressWarnings("unchecked")
    public RecordCaptureTable<? extends AbstractRecordCapture> getTable() throws UnifyException {
        RecordCaptureTable<? extends AbstractRecordCapture> table = (RecordCaptureTable<? extends AbstractRecordCapture>) getValue(
                RecordCaptureTable.class);
        if (table != oldTable) {
            RecordCaptureTableDef oldTableDef = null;
            if (oldTable != null) {
                oldTableDef = oldTable != null ? oldTable.getTableDef() : null;
            }

            RecordCaptureTableDef tableDef = table != null ? table.getTableDef() : null;
            if (oldTableDef != tableDef) {
                removeAllExternalChildWidgets();
                if (table != null) {
                    for (RecordCaptureColumnDef tableColumnDef : tableDef.getColumnDefs()) {
                        addExternalChildWidget(tableColumnDef.getEditor());
                    }
                }
            }
            
            oldTable = table;
       }

        return table;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected List<AbstractRecordCapture> getItemList() throws UnifyException {
        RecordCaptureTable table = getTable();
        if (table != null) {
            return table.getRecords();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(AbstractRecordCapture item, int index) throws UnifyException {
        return createValueStore(item, index);
    }

    @Override
    protected void onCreateValueList(List<ValueStore> valueList) throws UnifyException {

    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {

    }
}
