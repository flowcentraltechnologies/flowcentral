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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.data.UniqueHistory;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.upl.UplElementReferences;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.widget.AbstractValueListMultiControl;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Page;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.panel.StandalonePanel;

/**
 * Convenient abstract base class for table widgets.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplAttributes({ @UplAttribute(name = "contentDependentList", type = UplElementReferences.class),
        @UplAttribute(name = "multiSelDependentList", type = UplElementReferences.class),
        @UplAttribute(name = "multiSelect", type = boolean.class) })
public abstract class AbstractTableWidget<T extends AbstractTable<V, U>, U, V>
        extends AbstractValueListMultiControl<ValueStore, U> implements TableSelect<U> {

    private Class<T> tableClass;

    private Class<U> itemClass;

    private T oldTable;

    private Control selectCtrl;

    private Control sortColumnCtrl;

    private Integer[] selected;

    private Sort sort;

    private int sortColumnIndex;

    private List<String> contentDependentList;

    private List<String> multiSelDependentList;

    private UniqueHistory<Sort> sortHistory;

    private Set<Widget> inputs;

    public AbstractTableWidget(Class<T> tableClass, Class<U> itemClass) {
        this.tableClass = tableClass;
        this.itemClass = itemClass;
        this.sortColumnIndex = -1;
    }

    @Override
    public void addPageAliases() throws UnifyException {
        if (selectCtrl != null) {
            addPageAlias(selectCtrl);
        }
    }

    @Override
    public void populate(DataTransferBlock transferBlock) throws UnifyException {
        if (transferBlock != null) {
            DataTransferBlock childBlock = transferBlock.getChildBlock();
            ChildWidgetInfo childWidgetInfo = getChildWidgetInfo(childBlock.getId());
            Control control = (Control) childWidgetInfo.getWidget();
            if (control == selectCtrl) {
                selectCtrl.populate(childBlock);
            } else if (control == sortColumnCtrl) {
                sortColumnCtrl.populate(childBlock);
            } else {
                control.setValueStore(getValueList().get(childBlock.getItemIndex()));
                control.populate(childBlock);
            }
        }
    }

    @Action
    public void sortColumn() throws UnifyException {
        if (oldTable != null) {
            TableDef tableDef = oldTable.getTableDef();
            if (sort == null || sort.getColumnIndex() != sortColumnIndex) {
                if (tableDef.isUseSortHistory()) {
                    if (sort != null) {
                        if (sortHistory == null) {
                            sortHistory = new UniqueHistory<Sort>(tableDef.getSortHistory());
                        }
                        sortHistory.add(sort);
                    }
                }
                sort = new Sort(sortColumnIndex);
            }

            sort.flip();

            Order order = new Order().add(tableDef.getColumnDef(sort.getColumnIndex()).getFieldName(),
                    sort.getSortType());
            if (sortHistory != null) {
                int index = sortHistory.size();
                while (--index >= 0) {
                    Sort oldSort = sortHistory.get(index);
                    order.add(tableDef.getColumnDef(oldSort.getColumnIndex()).getFieldName(), oldSort.getSortType());
                }
            }

            oldTable.setOrder(order);
            oldTable.firstPage();
        }
    }

    public List<String> getContentDependentList() throws UnifyException {
        if (contentDependentList == null) {
            contentDependentList = getPageNames(getUplAttribute(UplElementReferences.class, "contentDependentList"));
        }
        return contentDependentList;
    }

    public List<String> getMultiSelDependentList() throws UnifyException {
        if (multiSelDependentList == null) {
            multiSelDependentList = getPageNames(getUplAttribute(UplElementReferences.class, "multiSelDependentList"));
        }
        return multiSelDependentList;
    }

    public boolean isMultiSelect() throws UnifyException {
        return getUplAttribute(boolean.class, "multiSelect");
    }

    public String getSelectAllId() throws UnifyException {
        return getPrefixedId("sela_");
    }

    public String getColumnHeaderId() throws UnifyException {
        return getPrefixedId("th_");
    }

    public String getRowId() throws UnifyException {
        return getPrefixedId("row_");
    }

    public T getTable() throws UnifyException {
        T table = getValue(tableClass);
        if (table != oldTable) {
            TableDef oldTableDef = null;
            if (oldTable != null) {
                oldTableDef = oldTable != null ? oldTable.getTableDef() : null;
            }

            TableDef tableDef = table != null ? table.getTableDef() : null;
            if (oldTableDef != tableDef) {
                removeAllExternalChildWidgets();
                if (table != null) {
                    StandalonePanel standalonePanel = getStandalonePanel();
                    while (standalonePanel != null) {
                        if (standalonePanel instanceof Page) {
                            break;
                        }

                        standalonePanel = standalonePanel.getStandalonePanel();
                    }

                    final boolean entryMode = table.isEntryMode();
                    for (TableColumnDef tableColumnDef : table.getColumnDefList()) {
                        final boolean cellEditor = tableColumnDef.isWithCellEditor();
                        final String columnWidgetUpl = entryMode && cellEditor ? tableColumnDef.getCellEditor()
                                : tableColumnDef.getCellRenderer();
                        Widget widget = addExternalChildWidget(columnWidgetUpl);
                        if (cellEditor) {
                            if (inputs == null) {
                                inputs = new HashSet<Widget>();
                            }
                            
                            inputs.add(widget);
                        }

                        EventHandler[] handlers = widget.getUplAttribute(EventHandler[].class, "eventHandler");
                        if (handlers != null) {
                            for (EventHandler handler : handlers) {
                                handler.setPageAction(null);
                            }
                        }

                        standalonePanel.resolvePageActions(handlers);
                    }
                }
            }

            if (table != null) {
                table.setTableSelect(this);
            }
            
            oldTable = table;
            sortHistory = null;
        }

        return table;
    }

    public Control getSelectCtrl() {
        return selectCtrl;
    }

    public Control getSortColumnCtrl() {
        return sortColumnCtrl;
    }

    public Integer[] getSelected() {
        return selected;
    }

    public void setSelected(Integer[] selected) throws UnifyException {
        this.selected = selected;
        Set<Integer> set = selected == null || selected.length == 0 ? Collections.emptySet()
                : new HashSet<Integer>(Arrays.asList(selected));
        getTable().setSelected(set);
    }

    public OrderType getSortType() {
        return sort != null ? sort.getSortType() : OrderType.ASCENDING;
    }

    public int getSortColumnIndex() {
        return sortColumnIndex;
    }

    public void setSortColumnIndex(int sortColumnIndex) {
        this.sortColumnIndex = sortColumnIndex;
    }

    public boolean isInputWidget(Widget widget) {
        return inputs != null && inputs.contains(widget);
    }
    
    @Override
    public List<U> getSelectedItems() throws UnifyException {
        if (selected != null) {
            List<U> list = new ArrayList<U>();
            for (int i = 0; i < selected.length; i++) {
                list.add(getItem(selected[i]));
            }

            return list;
        }

        return Collections.emptyList();
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        if (isMultiSelect()) {
            selectCtrl = (Control) addInternalChildWidget("!ui-hidden binding:selected");
        }

        sortColumnCtrl = (Control) addInternalChildWidget("!ui-hidden binding:sortColumnIndex");
    }

    @Override
    protected ValueStore newValue(U object, int index) throws UnifyException {
        return createValueStore(object, index);
    }

    @Override
    protected void onCreateValueList(List<ValueStore> valueStoreList) throws UnifyException {

    }

    @Override
    protected final List<U> getItemList() throws UnifyException {
        T table = getTable();
        if (table != null) {
            return table.getDispItemList();
        }

        return Collections.emptyList();
    }

    protected Class<U> getItemClass() {
        return itemClass;
    }

    private class Sort {

        private OrderType sortType;

        private int columnIndex;

        public Sort(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public OrderType getSortType() {
            return sortType;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        public void flip() {
            sortType = (!OrderType.ASCENDING.equals(sortType)) ? OrderType.ASCENDING : OrderType.DESCENDING;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }

            if (getClass() != o.getClass()) {
                return false;
            }

            return this.columnIndex == ((Sort) o).columnIndex;
        }
    }
}
