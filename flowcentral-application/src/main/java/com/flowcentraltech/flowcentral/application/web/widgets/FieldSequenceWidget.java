/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
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
 * Field sequence widget.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("fc-fieldsequence")
public class FieldSequenceWidget extends AbstractValueListWidget<FieldSequenceEntry> {

    private Control fieldSelectCtrl;

    private Control paramCtrl;

    private Control moveUpCtrl;

    private Control moveDownCtrl;

    private Control deleteCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        fieldSelectCtrl = (Control) addInternalChildWidget(
                "!ui-select blankOption:$s{} list:entityfielddeflist listParams:$l{entityDef} binding:fieldName");
        paramCtrl = (Control) addInternalChildWidget(
                "!ui-select blankOption:$s{} list:entityfielddefformatterlist listParams:$l{entityDef fieldName} binding:param");
        moveUpCtrl = (Control) addInternalChildWidget(
                "!ui-button styleClass:$e{abutton} symbol:$s{arrow-up} hint:$m{button.moveup.hint} debounce:false");
        moveDownCtrl = (Control) addInternalChildWidget(
                "!ui-button styleClass:$e{abutton} symbol:$s{arrow-down} hint:$m{button.movedown.hint} debounce:false");
        deleteCtrl = (Control) addInternalChildWidget(
                "!ui-button styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
    }

    @Action
    public void normalize() throws UnifyException {
        FieldSequence fieldSequence = getFieldSequence();
        if (fieldSequence != null) {
            fieldSequence.normalize();
        }
    }

    @Action
    public void moveUp() throws UnifyException {
        getFieldSequence().moveUpEntry(getRequestTarget(int.class));
        invalidateValueList();
    }

    @Action
    public void moveDown() throws UnifyException {
        getFieldSequence().moveDownEntry(getRequestTarget(int.class));
        invalidateValueList();
    }

    @Action
    public void delete() throws UnifyException {
        getFieldSequence().removeEntry(getRequestTarget(int.class));
    }

    public Control getFieldSelectCtrl() {
        return fieldSelectCtrl;
    }

    public Control getParamCtrl() {
        return paramCtrl;
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

    public FieldSequence getFieldSequence() throws UnifyException {
        return getValue(FieldSequence.class);
    }

    @Override
    protected List<FieldSequenceEntry> getItemList() throws UnifyException {
        FieldSequence fieldSequence = getFieldSequence();
        if (fieldSequence != null) {
            return fieldSequence.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(FieldSequenceEntry setValueEntry, int index) throws UnifyException {
        return createValueStore(setValueEntry, index);
    }

}
