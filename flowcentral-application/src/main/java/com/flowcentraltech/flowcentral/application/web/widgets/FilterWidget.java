/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.control.DynamicField;

/**
 * Filter widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-filter")
@UplAttributes({
    @UplAttribute(name = "includeSysParam", type = boolean.class),
    @UplAttribute(name = "includeSysParamBinding", type = String.class)})
public class FilterWidget extends AbstractValueListWidget<FilterCondition> {

    private Control fieldSelectCtrl;

    private DynamicField conditionTypeCtrl;

    private DynamicField paramCtrlA;

    private DynamicField paramCtrlB;

    private Control fieldParamCtrlA;

    private Control fieldParamCtrlB;

    private Control swapCtrl;

    private Control andCtrl;

    private Control orCtrl;

    private Control addCtrl;

    private Control deleteCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        conditionTypeCtrl = (DynamicField) addInternalChildWidget(
                "!ui-dynamic style:$s{width:100%;} binding:type descriptorBinding:typeSelector");
        paramCtrlA = (DynamicField) addInternalChildWidget(
                "!ui-dynamic style:$s{width:100%;} binding:paramInputA.value descriptorBinding:paramInputA.editor");
        paramCtrlB = (DynamicField) addInternalChildWidget(
                "!ui-dynamic style:$s{width:100%;} binding:paramInputB.value descriptorBinding:paramInputB.editor");
        swapCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{swap} hint:$m{button.swap.hint} debounce:false");
        andCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} caption:$m{button.and} hint:$m{button.and.hint} debounce:false");
        orCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} caption:$m{button.or} hint:$m{button.or.hint} debounce:false");
        addCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{plus} hint:$m{button.add.hint} debounce:false");
        deleteCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
    }

    @Action
    public void normalize() throws UnifyException {
        Filter filter = getFilter();
        if (filter != null) {
            filter.normalize();
        }
    }

    @Action
    public void and() throws UnifyException {
        getFilter().addCompoundCondition(getRequestTarget(int.class), CompoundType.AND, Editable.TRUE);
    }

    @Action
    public void or() throws UnifyException {
        getFilter().addCompoundCondition(getRequestTarget(int.class), CompoundType.OR, Editable.TRUE);
    }

    @Action
    public void add() throws UnifyException {
        getFilter().addSimpleCondition(getRequestTarget(int.class), Editable.TRUE);
    }

    @Action
    public void delete() throws UnifyException {
        getFilter().removeCondition(getRequestTarget(int.class));
    }

    @Action
    public void swap() throws UnifyException {
        getFilter().swapLogic(getRequestTarget(int.class));
    }

    public boolean isIncludeSysParam() throws UnifyException {
        return getUplAttribute(boolean.class, "includeSysParam", "includeSysParamBinding");
    }
    
    public Control getFieldSelectCtrl() throws UnifyException {
        if (fieldSelectCtrl == null) {
            fieldSelectCtrl = (Control) addInternalChildWidget(isIncludeSysParam()
                    ? "!ui-select style:$s{width:100%;} blankOption:$s{} list:sysentityfilterfielddeflist listParams:$l{entityDef labelSuggestionDef} binding:fieldName"
                    : "!ui-select style:$s{width:100%;} blankOption:$s{} list:entityfilterfielddeflist listParams:$l{entityDef labelSuggestionDef} binding:fieldName");
        }
        
        return fieldSelectCtrl;
    }

    public DynamicField getConditionTypeCtrl() {
        return conditionTypeCtrl;
    }

    public DynamicField getParamCtrlA() {
        return paramCtrlA;
    }

    public DynamicField getParamCtrlB() {
        return paramCtrlB;
    }

    public Control getFieldParamCtrlA() throws UnifyException {
        ensureFieldParamControls();
        return fieldParamCtrlA;
    }

    public Control getFieldParamCtrlB() throws UnifyException {
        ensureFieldParamControls();
        return fieldParamCtrlB;
    }

    public Control getSwapCtrl() {
        return swapCtrl;
    }

    public Control getAndCtrl() {
        return andCtrl;
    }

    public Control getOrCtrl() {
        return orCtrl;
    }

    public Control getAddCtrl() {
        return addCtrl;
    }

    public Control getDeleteCtrl() {
        return deleteCtrl;
    }

    public Filter getFilter() throws UnifyException {
        return getValue(Filter.class);
    }

    @Override
    protected List<FilterCondition> getItemList() throws UnifyException {
        Filter filter = getFilter();
        if (filter != null) {
            return filter.getConditionList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(FilterCondition filterCondition, int index) throws UnifyException {
        return createValueStore(filterCondition, index);
    }

    private void ensureFieldParamControls() throws UnifyException {
        if (fieldParamCtrlA == null) {
            Filter filter = getFilter();
            if (filter != null) {
                switch (filter.getListType()) {
                    case IMMEDIATE_FIELD:
                        fieldParamCtrlA = (Control) addInternalChildWidget(
                                "!ui-select style:$s{width:100%;} blankOption:$s{} list:entityfielddeflist listParams:$s{entityDef} binding:paramFieldA");
                        fieldParamCtrlB = (Control) addInternalChildWidget(
                                "!ui-select style:$s{width:100%;} blankOption:$s{} list:entityfielddeflist listParams:$s{entityDef} binding:paramFieldB");
                        break;
                    case IMMEDIATE_PARAM:
                        fieldParamCtrlA = (Control) addInternalChildWidget("!ui-select style:$s{width:100%;} blankOption:$s{} list:"
                                + filter.getParamList() + " listParams:$s{ownerInstId} binding:paramFieldA");
                        fieldParamCtrlB = (Control) addInternalChildWidget("!ui-select style:$s{width:100%;} blankOption:$s{} list:"
                                + filter.getParamList() + " listParams:$s{ownerInstId} binding:paramFieldB");
                        break;
                    case IMMEDIATE_ONLY:
                    default:
                        break;

                }
            }
        }
    }
}
