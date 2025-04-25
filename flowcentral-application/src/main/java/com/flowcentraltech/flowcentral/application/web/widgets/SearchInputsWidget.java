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
 * Search inputs widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-searchinputs")
public class SearchInputsWidget extends AbstractValueListWidget<SearchInputEntry> {

    private Control labelCtrl;

    private Control fieldSelectCtrl;

    private Control widgetCtrl;

    private Control conditionTypeCtrl;

    private Control moveUpCtrl;

    private Control moveDownCtrl;

    private Control deleteCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        labelCtrl = (Control) addInternalChildWidget("!ui-alphanumeric maxLen:64 space:true special:true binding:label");
        fieldSelectCtrl = (Control) addInternalChildWidget(
                "!ui-select style:$s{width:100%;} blankOption:$s{} list:searchinputfieldlist listParams:$l{entityDef} binding:fieldName");
        widgetCtrl = (Control) addInternalChildWidget(
                "!ui-select style:$s{width:100%;} blankOption:$s{} list:searchinputfieldwidgetlist listParams:$l{entityDef fieldName} binding:widget");
        conditionTypeCtrl = (Control) addInternalChildWidget(
                "!ui-select style:$s{width:100%;} blankOption:$s{} list:searchinputconditionlist listParams:$l{entityDef fieldName} binding:condition");
        moveUpCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{arrow-up} hint:$m{button.moveup.hint} debounce:false");
        moveDownCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{arrow-down} hint:$m{button.movedown.hint} debounce:false");
        deleteCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
    }

    @Action
    public void normalize() throws UnifyException {
        SearchInputs searchInputs = getSearchInputs();
        if (searchInputs != null) {
            searchInputs.normalize();
        }
    }

    @Action
    public void moveUp() throws UnifyException {
        getSearchInputs().moveUpEntry(getRequestTarget(int.class));
        invalidateValueList();
    }

    @Action
    public void moveDown() throws UnifyException {
        getSearchInputs().moveDownEntry(getRequestTarget(int.class));
        invalidateValueList();
    }

    @Action
    public void delete() throws UnifyException {
        getSearchInputs().removeEntry(getRequestTarget(int.class));
    }

    public Control getLabelCtrl() {
        return labelCtrl;
    }

    public Control getWidgetCtrl() {
        return widgetCtrl;
    }

    public Control getConditionTypeCtrl() {
        return conditionTypeCtrl;
    }

    public Control getFieldSelectCtrl() {
        return fieldSelectCtrl;
    }

    public Control getMoveUpCtrl() {
        return moveUpCtrl;
    }

    public Control getMoveDownCtrl() {
        return moveDownCtrl;
    }

    public Control getDeleteCtrl() {
        return deleteCtrl;
    }

    public SearchInputs getSearchInputs() throws UnifyException {
        return getValue(SearchInputs.class);
    }

    @Override
    protected List<SearchInputEntry> getItemList() throws UnifyException {
        SearchInputs searchInputs = getSearchInputs();
        if (searchInputs != null) {
            return searchInputs.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(SearchInputEntry searchInputEntry, int index) throws UnifyException {
        return createValueStore(searchInputEntry, index);
    }

}
