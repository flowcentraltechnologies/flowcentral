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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTable.EntityTreeItem;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.AbstractValueListMultiControl;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Entity tree table widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitytreetable")
@UplAttributes({@UplAttribute(name = "multiSelect", type = boolean.class),
    @UplAttribute(name = "multiColumn", type = boolean.class),
    @UplAttribute(name = "showLevelLabel", type = boolean.class)})
public class EntityTreeTableWidget extends AbstractValueListMultiControl<ValueStore, EntityTreeItem> {

    private Control selectCtrl;

    private Integer[] selected;

    @Override
    public void addPageAliases() throws UnifyException {
        if (selectCtrl != null) {
            addPageAlias(selectCtrl);
        }
    }

    public Control getSelectCtrl() {
        return selectCtrl;
    }

    public Integer[] getSelected() {
        return selected;
    }

    public void setSelected(Integer[] selected) throws UnifyException {
        this.selected = selected;
        EntityTreeTable table = getEntityTreeTable();
        if (table != null) {
            table.unselectAll();
            if (selected != null) {
                for(Integer i: selected) {
                    table.select(i);
                }
            }
        }
    }

    public boolean isMultiSelect() throws UnifyException {
        return getUplAttribute(boolean.class, "multiSelect");
    }

    public boolean isMultiColumn() throws UnifyException {
        return getUplAttribute(boolean.class, "multiColumn");
    }

    public boolean isShowLevelLabel() throws UnifyException {
        return getUplAttribute(boolean.class, "showLevelLabel");
    }

    public String getRowId() throws UnifyException {
        return getPrefixedId("row_");
    }

    public EntityTreeTable getEntityTreeTable() throws UnifyException {
        return getValue(EntityTreeTable.class);
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        if (isMultiSelect()) {
            selectCtrl = (Control) addInternalChildWidget("!ui-hidden binding:selected");
        }
    }

    @Override
    protected List<EntityTreeItem> getItemList() throws UnifyException {
        EntityTreeTable table = getEntityTreeTable();
        return table.getItems();
    }

    @Override
    protected ValueStore newValue(EntityTreeItem item, int index) throws UnifyException {
        return createValueStore(item, index);
    }

    @Override
    protected void onCreateValueList(List<ValueStore> valueStoreList) throws UnifyException {
        
    }
}
