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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.control.DynamicField;

/**
 * Input array widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-inputarray")
@UplAttributes({ @UplAttribute(name = "columns", type = int.class, defaultVal = "1") })
public class InputArrayWidget extends AbstractValueListWidget<InputArrayEntry> {

    private DynamicField editCtrl;

    private DynamicField viewCtrl;

    private Control selectCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        editCtrl = (DynamicField) addInternalChildWidget(
                "!ui-dynamic binding:valueInput.value descriptorBinding:valueInput.editor");
        viewCtrl = (DynamicField) addInternalChildWidget(
                "!ui-dynamic binding:valueInput.value descriptorBinding:valueInput.renderer");
        selectCtrl = (Control) addInternalChildWidget("!ui-checkbox binding:selected");
    }

    public int getColumns() throws UnifyException {
        InputArrayEntries entries = getInputArrayEntries();
        if (entries != null && entries.getColumns() > 0) {
            return entries.getColumns();
        }
        
        return getUplAttribute(int.class, "columns");
    }

    public DynamicField getEditCtrl() {
        return editCtrl;
    }

    public DynamicField getViewCtrl() {
        return viewCtrl;
    }

    public Control getSelectCtrl() {
        return selectCtrl;
    }

    public InputArrayEntries getInputArrayEntries() throws UnifyException {
        return getValue(InputArrayEntries.class);
    }

    @Override
    protected List<InputArrayEntry> getItemList() throws UnifyException {
        InputArrayEntries inputArrayEntries = getInputArrayEntries();
        if (inputArrayEntries != null) {
            return inputArrayEntries.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(InputArrayEntry inputArrayEntry, int index) throws UnifyException {
        return createValueStore(inputArrayEntry, index);
    }

}
