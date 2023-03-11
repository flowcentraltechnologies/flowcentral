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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.widget.AbstractValueListMultiControl;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Convenient abstract base class for value list widgets.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractValueListWidget<T> extends AbstractValueListMultiControl<ValueStore, T> {

    @Override
    public void populate(DataTransferBlock transferBlock) throws UnifyException {
        if (transferBlock != null) {
            DataTransferBlock ctrlBlock = transferBlock.getChildBlock();
            Control control = (Control) getChildWidgetInfo(ctrlBlock.getId()).getWidget();
            if (ctrlBlock.getChildBlock() == null) {
                control.setValueStore(getValueList().get(ctrlBlock.getItemIndex()));
            } else {
                control.setValueStore(getValueList().get(ctrlBlock.getChildBlock().getItemIndex()));
            }

            control.populate(ctrlBlock);
        }
    }

    @Override
    protected void onCreateValueList(List<ValueStore> valueStoreList) throws UnifyException {

    }

}
