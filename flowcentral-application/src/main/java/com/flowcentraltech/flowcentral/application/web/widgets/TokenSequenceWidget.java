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
 * Token sequence widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-tokensequence")
public class TokenSequenceWidget extends AbstractValueListWidget<TokenSequenceEntry> {

    private Control tokenSelectCtrl;

    private Control textCtrl;

    private Control fieldSelectCtrl;

    private Control generatorSelectCtrl;

    private Control paramCtrl;

    private Control moveUpCtrl;

    private Control moveDownCtrl;

    private Control deleteCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        tokenSelectCtrl = (Control) addInternalChildWidget(
                "!ui-select styleClass:$e{fc-tiny} blankOption:$s{} list:stringtokentypelist binding:tokenType");
        textCtrl = (Control) addInternalChildWidget("!ui-textarea styleClass:$e{fc-tiny} rows:2 binding:param");
        fieldSelectCtrl = (Control) addInternalChildWidget(
                "!ui-select styleClass:$e{fc-tiny} blankOption:$s{} list:entityfielddeflist listParams:$l{entityDef} binding:fieldName");
        paramCtrl = (Control) addInternalChildWidget(
                "!ui-select styleClass:$e{fc-tiny} blankOption:$s{} list:entityfielddefformatterlist listParams:$l{entityDef fieldName} binding:param");
        generatorSelectCtrl = (Control) addInternalChildWidget(
                "!ui-select styleClass:$e{fc-tiny} blankOption:$s{} list:paramgeneratorlist listParams:$s{entityDef} binding:param");
        moveUpCtrl = (Control) addInternalChildWidget(
                "!ui-button styleClass:$e{abutton} symbol:$s{arrow-up} hint:$m{button.moveup.hint} debounce:false");
        moveDownCtrl = (Control) addInternalChildWidget(
                "!ui-button styleClass:$e{abutton} symbol:$s{arrow-down} hint:$m{button.movedown.hint} debounce:false");
        deleteCtrl = (Control) addInternalChildWidget(
                "!ui-button styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
    }

    @Action
    public void normalize() throws UnifyException {
        TokenSequence tokenSequence = getTokenSequence();
        if (tokenSequence != null) {
            tokenSequence.normalize();
        }
    }

    @Action
    public void moveUp() throws UnifyException {
        getTokenSequence().moveUpEntry(getRequestTarget(int.class));
        invalidateValueList();
    }

    @Action
    public void moveDown() throws UnifyException {
        getTokenSequence().moveDownEntry(getRequestTarget(int.class));
        invalidateValueList();
    }

    @Action
    public void delete() throws UnifyException {
        getTokenSequence().removeEntry(getRequestTarget(int.class));
    }

    public Control getTokenSelectCtrl() {
        return tokenSelectCtrl;
    }

    public Control getTextCtrl() {
        return textCtrl;
    }

    public Control getFieldSelectCtrl() {
        return fieldSelectCtrl;
    }

    public Control getParamCtrl() {
        return paramCtrl;
    }

    public Control getGeneratorSelectCtrl() {
        return generatorSelectCtrl;
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

    public TokenSequence getTokenSequence() throws UnifyException {
        return getValue(TokenSequence.class);
    }

    public String getPreview() throws UnifyException {
        TokenSequence tokenSequence = getTokenSequence();
        return tokenSequence != null ? tokenSequence.getPreview() : null;
    }
    
    @Override
    protected List<TokenSequenceEntry> getItemList() throws UnifyException {
        TokenSequence tokenSequence = getTokenSequence();
        if (tokenSequence != null) {
            return tokenSequence.getEntryList();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(TokenSequenceEntry setValueEntry, int index) throws UnifyException {
        return createValueStore(setValueEntry, index);
    }

}
