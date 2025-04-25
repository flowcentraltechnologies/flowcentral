/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

/**
 * Widget rules widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-widgetrules")
public class WidgetRulesWidget extends AbstractValueListWidget<WidgetRuleEntry> {

    private Control fieldSelectCtrl;

    private Control widgetCtrl;

    private Control deleteCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        fieldSelectCtrl = (Control) addInternalChildWidget(
                "!ui-select style:$s{width:100%;} blankOption:$s{} list:entityfielddeflist listParams:$l{entityDef} binding:fieldName");
        widgetCtrl = (Control) addInternalChildWidget(
                "!ui-select style:$s{width:100%;} blankOption:$s{} list:entityfielddefinputwidgetlist listParams:$l{entityDef fieldName} binding:widget");
        deleteCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
    }

    @Action
    public void normalize() throws UnifyException {
        WidgetRules widgetRules = getWidgetRules();
        if (widgetRules != null) {
            widgetRules.normalize();
        }
    }

    @Action
    public void delete() throws UnifyException {
        getWidgetRules().removeEntry(getRequestTarget(int.class));
    }

    public Control getFieldSelectCtrl() {
        return fieldSelectCtrl;
    }

    public Control getWidgetCtrl() {
        return widgetCtrl;
    }

    public Control getDeleteCtrl() {
        return deleteCtrl;
    }

    public WidgetRules getWidgetRules() throws UnifyException {
        return getValue(WidgetRules.class);
    }

    @Override
    protected List<WidgetRuleEntry> getItemList() throws UnifyException {
        WidgetRules widgetRules = getWidgetRules();
        if (widgetRules != null) {
            return widgetRules.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(WidgetRuleEntry widgetRuleEntry, int index) throws UnifyException {
        return createValueStore(widgetRuleEntry, index);
    }

}
