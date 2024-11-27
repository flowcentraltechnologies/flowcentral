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
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Entity composition widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitycomposition")
public class EntityCompositionWidget extends AbstractValueListWidget<EntityCompositionEntry> {

    private EntityComposition entityComposition;
    
    private String oldCompositionJson;
    
    private Control entityNameCtrl;

    private Control entityTableCtrl;

    private Control fieldTypeCtrl;

    private Control dataTypeCtrl;

    private Control fieldNameCtrl;

    private Control columnCtrl;

    private Control addEntityCtrl;

    private Control addFieldCtrl;

    private Control delEntityCtrl;

    private Control delFieldCtrl;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        entityNameCtrl = (Control) addInternalChildWidget("!ui-name case:camel binding:entityName");
        entityTableCtrl = (Control) addInternalChildWidget("!ui-name case:upper underscore:true binding:table");
        fieldTypeCtrl = (Control) addInternalChildWidget(
                "!ui-select blankOption:$s{} list:dynamicentityfieldtypelist binding:fieldType");
        dataTypeCtrl = (Control) addInternalChildWidget(
                "!ui-select blankOption:$s{} list:datatypelist binding:dataType");
        fieldNameCtrl = (Control) addInternalChildWidget("!ui-name case:camel binding:name");
        columnCtrl = (Control) addInternalChildWidget("!ui-name underscore:true binding:column");
        addFieldCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{plus} hint:$m{button.addfield.hint} debounce:false");
        delFieldCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
        addEntityCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{plus-square} hint:$m{button.addentity.hint} debounce:false");
        delEntityCtrl = (Control) addInternalChildWidget(
                "!ui-button alwaysValueIndex:true styleClass:$e{abutton} symbol:$s{cross} hint:$m{button.delete.hint} debounce:false");
    }

    @Action
    public void normalize() throws UnifyException {
        EntityComposition entityComposition = getEntityComposition();
        if (entityComposition != null) {
            entityComposition.normalize();
        }
    }

    @Action
    public void addEntity() throws UnifyException {
        getEntityComposition().addEntity(getRequestTarget(int.class));
    }

    @Action
    public void delEntity() throws UnifyException {
        getEntityComposition().deleteEntity(getRequestTarget(int.class));
    }

    @Action
    public void addField() throws UnifyException {
        getEntityComposition().addField(getRequestTarget(int.class));
    }

    @Action
    public void delField() throws UnifyException {
        getEntityComposition().deleteField(getRequestTarget(int.class));
    }

    public Control getEntityNameCtrl() {
        return entityNameCtrl;
    }

    public Control getEntityTableCtrl() {
        return entityTableCtrl;
    }

    public Control getFieldTypeCtrl() {
        return fieldTypeCtrl;
    }

    public Control getDataTypeCtrl() {
        return dataTypeCtrl;
    }

    public Control getFieldNameCtrl() {
        return fieldNameCtrl;
    }

    public Control getColumnCtrl() {
        return columnCtrl;
    }

    public Control getAddEntityCtrl() {
        return addEntityCtrl;
    }

    public Control getAddFieldCtrl() {
        return addFieldCtrl;
    }

    public Control getDelEntityCtrl() {
        return delEntityCtrl;
    }

    public Control getDelFieldCtrl() {
        return delFieldCtrl;
    }

    public EntityComposition getEntityComposition() throws UnifyException {
        final String compositionJson = getValue(String.class);
        if (!DataUtils.equals(oldCompositionJson, compositionJson)) {
            entityComposition = DataUtils.fromJsonString(EntityComposition.class, compositionJson);
            oldCompositionJson = compositionJson;
            setPageAttribute("composition", entityComposition);
        }
        
        return entityComposition;
    }

    @Override
    protected List<EntityCompositionEntry> getItemList() throws UnifyException {
        EntityComposition entityComposition = getEntityComposition();
        if (entityComposition != null) {
            return entityComposition.getEntries();
        }

        return Collections.emptyList();
    }

    @Override
    protected ValueStore newValue(EntityCompositionEntry entry, int index) throws UnifyException {
        return createValueStore(entry, index);
    }
}
