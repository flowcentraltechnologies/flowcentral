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

import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTable.EntityTreeItem;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTable.EntityTreeLevel;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTable.TableColumnInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.upl.UplElementReferences;
import com.tcdng.unify.web.ui.widget.AbstractValueListMultiControl;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Entity tree table widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitytreetable")
@UplAttributes({ @UplAttribute(name = "multiSelDependentList", type = UplElementReferences.class) })
public class EntityTreeTableWidget extends AbstractValueListMultiControl<ValueStore, EntityTreeItem> {

    private Control selectCtrl;

    private Integer[] selected;

    private List<String> multiSelDependentList;

    private EntityTreeTable oldEntityTreeTable;

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
                for (Integer i : selected) {
                    table.select(i);
                }
            }
        }
    }

    public String getRowId() throws UnifyException {
        return getPrefixedId("row_");
    }

    public String getColumnHeaderId() throws UnifyException {
        return getPrefixedId("th_");
    }

    public String getSelectAllId() throws UnifyException {
        return getPrefixedId("sela_");
    }

    @SuppressWarnings("unused")
    public EntityTreeTable getEntityTreeTable() throws UnifyException {
        EntityTreeTable entityTreeTable = getValue(EntityTreeTable.class);
        if (entityTreeTable != null && entityTreeTable != oldEntityTreeTable) {
            removeAllExternalChildWidgets();
            final boolean entryMode = false; // TODO
            for (EntityTreeLevel level : entityTreeTable.getLevels()) {
                int usedPercent = 0;
                int i = 0;
                final int len = level.getColumnInfoList().size();
                for (TableColumnInfo tableColumnInfo : level.getColumnInfoList()) {
                    TableColumnDef tableColumnDef = tableColumnInfo.getTableColumnDef();
                    final boolean cellEditor = tableColumnDef.isWithCellEditor();
                    final String columnWidgetUpl = entryMode && cellEditor ? tableColumnDef.getCellEditor()
                            : tableColumnDef.getCellRenderer();
                    Widget widget = addExternalChildWidget(columnWidgetUpl);
                    tableColumnInfo.setWidget(widget);
                    
                    int width = (tableColumnDef.getWidth() * 100) / level.getTotalWidth();
                    if (i == (len - 1)) {
                        tableColumnInfo.setStyle("width:" + (100 - usedPercent) + "%;");
                        usedPercent += width;
                    } else {
                        tableColumnInfo.setStyle("width:" + width + "%;");
                        usedPercent += width;
                    }
                }
            }

        }

        oldEntityTreeTable = entityTreeTable;
        return entityTreeTable;
    }

    public List<String> getMultiSelDependentList() throws UnifyException {
        if (multiSelDependentList == null) {
            multiSelDependentList = getPageNames(getUplAttribute(UplElementReferences.class, "multiSelDependentList"));
        }
        return multiSelDependentList;
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        selectCtrl = (Control) addInternalChildWidget("!ui-hidden binding:selected");
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
