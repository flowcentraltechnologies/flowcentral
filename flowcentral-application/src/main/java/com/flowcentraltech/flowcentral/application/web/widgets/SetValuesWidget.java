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
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.control.DynamicField;

/**
 * Set values widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-setvalues")
public class SetValuesWidget extends AbstractValueListWidget<SetValueEntry> {

    private Control fieldSelectCtrl;

    private Control typeSelectCtrl;

    private DynamicField paramCtrl;

    private Control deleteCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        fieldSelectCtrl = (Control) addInternalChildWidget(
                "!ui-select blankOption:$s{} list:entitysetvaluefielddeflist listParams:$l{entityDef} binding:fieldName");
        typeSelectCtrl = (Control) addInternalChildWidget(
                "!ui-select blankOption:$s{} list:entitysetvalueoptionlist listParams:$l{entityDef fieldName} binding:type");
        paramCtrl = (DynamicField) addInternalChildWidget(
                "!ui-dynamic binding:paramInput.value descriptorBinding:paramInput.editor");
        deleteCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
    }

    @Action
    public void normalize() throws UnifyException {
        SetValueEntries setValueEntries = getSetValueEntries();
        if (setValueEntries != null) {
            setValueEntries.normalize();
        }
    }

    @Action
    public void delete() throws UnifyException {
        getSetValueEntries().removeEntry(getRequestTarget(int.class));
    }

    public Control getFieldSelectCtrl() {
        return fieldSelectCtrl;
    }

    public Control getTypeSelectCtrl() {
        return typeSelectCtrl;
    }

    public DynamicField getParamCtrl() {
        return paramCtrl;
    }

    public Control getDeleteCtrl() {
        return deleteCtrl;
    }

    public SetValueEntries getSetValueEntries() throws UnifyException {
        return getValue(SetValueEntries.class);
    }

    @Override
    protected List<SetValueEntry> getItemList() throws UnifyException {
        SetValueEntries setValueEntries = getSetValueEntries();
        if (setValueEntries != null) {
            return setValueEntries.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(SetValueEntry setValueEntry, int index) throws UnifyException {
        return createValueStore(setValueEntry, index);
    }

}
