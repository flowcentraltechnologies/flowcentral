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

import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.control.DynamicField;

/**
 * Parameter values widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-paramvalues")
public class ParamValuesWidget extends AbstractValueListWidget<ParamValueEntry> {

    private DynamicField paramCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        paramCtrl = (DynamicField) addInternalChildWidget(
                "!ui-dynamic binding:paramInput.value descriptorBinding:paramInput.editor");
    }

    public DynamicField getParamCtrl() {
        return paramCtrl;
    }

    public ParamValueEntries getParamValueEntries() throws UnifyException {
        return getValue(ParamValueEntries.class);
    }

    @Override
    protected List<ParamValueEntry> getItemList() throws UnifyException {
        ParamValueEntries paramValueEntries = getParamValueEntries();
        if (paramValueEntries != null) {
            return paramValueEntries.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(ParamValueEntry paramValueEntry, int index) throws UnifyException {
        return createValueStore(paramValueEntry, index);
    }

}
