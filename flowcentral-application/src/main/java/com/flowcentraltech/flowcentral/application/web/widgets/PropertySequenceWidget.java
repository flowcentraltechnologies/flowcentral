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

/**
 * Property sequence widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-propertysequence")
public class PropertySequenceWidget extends AbstractValueListWidget<PropertySequenceEntry> {

    private Control propertySelectCtrl;

    private Control labelCtrl;

    private Control moveUpCtrl;

    private Control moveDownCtrl;

    private Control deleteCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        propertySelectCtrl = (Control) addInternalChildWidget(
                "!ui-select blankOption:$s{} list:entitydefpropertylist listParams:$l{entityDef type} binding:property");
        labelCtrl = (Control) addInternalChildWidget(
                "!ui-text binding:label");
        moveUpCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{arrow-up} hint:$m{button.moveup.hint} debounce:false");
        moveDownCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{arrow-down} hint:$m{button.movedown.hint} debounce:false");
        deleteCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
    }

    @Action
    public void normalize() throws UnifyException {
        PropertySequence propertySequence = getPropertySequence();
        if (propertySequence != null) {
            propertySequence.normalize();
        }
    }

    @Action
    public void moveUp() throws UnifyException {
        getPropertySequence().moveUpEntry(getRequestTarget(int.class));
        invalidateValueList();
    }

    @Action
    public void moveDown() throws UnifyException {
        getPropertySequence().moveDownEntry(getRequestTarget(int.class));
        invalidateValueList();
    }

    @Action
    public void delete() throws UnifyException {
        getPropertySequence().removeEntry(getRequestTarget(int.class));
    }

    public Control getPropertySelectCtrl() {
        return propertySelectCtrl;
    }

    public Control getLabelCtrl() {
        return labelCtrl;
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

    public PropertySequence getPropertySequence() throws UnifyException {
        return getValue(PropertySequence.class);
    }

    @Override
    protected List<PropertySequenceEntry> getItemList() throws UnifyException {
        PropertySequence propertySequence = getPropertySequence();
        if (propertySequence != null) {
            return propertySequence.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(PropertySequenceEntry propertySequenceEntry, int index) throws UnifyException {
        return createValueStore(propertySequenceEntry, index);
    }

}
