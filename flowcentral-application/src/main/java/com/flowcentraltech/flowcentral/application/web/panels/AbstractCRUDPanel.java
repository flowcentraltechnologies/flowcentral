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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.AbstractPanel;

/**
 * Convenient abstract base class for CRUD panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/crudpanel.upl")
public class AbstractCRUDPanel<T extends AbstractCRUD<?>> extends AbstractPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        T crud = getCrud();
        crud.evaluateTabStates();
        if (crud.isEditable()) {
            final boolean editable = isContainerEditable();        
            final boolean displayItems = crud.isWithDisplayItems();        
            final boolean create = crud.isCreate();

            setVisible("crudFormPanel", editable || displayItems);
            setEditable("crudFormPanel", editable && crud.isAllowMaintain());
            
            setVisible("crudActionPanel", editable);
            setVisible("crudAddBtn", create);
            setVisible("crudUpdateBtn", !create && crud.isAllowUpdate());
            setVisible("crudDeleteBtn", !create && crud.isAllowDelete());
            setVisible("crudCancelBtn", !create);
        } else {
            setVisible("crudActionPanel", false);
        }
    }
    
    @Action
    public void addItem() throws UnifyException {
        getCrud().save();
    }
    
    @Action
    public void updateItem() throws UnifyException {
        getCrud().save();
    }
    
    @Action
    public void deleteItem() throws UnifyException {
        getCrud().delete();
    }
    
    @Action
    public void cancelItem() throws UnifyException {
        T crud = getCrud();
        crud.enterCreate();
    }
    
    @SuppressWarnings("unchecked")
    private T getCrud() throws UnifyException {
        return (T) getValue(AbstractCRUD.class);
    }

}
