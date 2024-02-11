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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Entity tree table data object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityTreeTable {

    private List<EntityTreeLevel> levels;

    private List<EntityTreeItem> items;

    private List<Integer> itemLevelChain;

    private String title;

    private String tableClass;

    private boolean centerAlignHeaders;

    private boolean multiSelect;

    private boolean multiColumn;

    private boolean showLabel;

    private EntityTreeTable(List<EntityTreeLevel> levels, List<EntityTreeItem> items, String title,
            boolean centerAlignHeaders, boolean multiSelect, boolean multiColumn, boolean showLabel) {
        this.levels = levels;
        this.items = items;
        this.title = title;
        this.centerAlignHeaders = centerAlignHeaders;
        this.multiSelect = multiSelect;
        this.multiColumn = multiColumn;
        this.showLabel = showLabel;
    }

    public List<EntityTreeLevel> getLevels() {
        return levels;
    }

    public EntityTreeLevel getLevel(int index) {
        return levels.get(index);
    }

    public int getLevelCount() {
        return levels.size();
    }

    public EntityTreeItem getEntityTreeItem(int index) {
        return items.get(index);
    }

    public List<EntityTreeItem> getItems() {
        return items;
    }

    public String getTableClass() {
        return tableClass;
    }

    public void setTableClass(String tableClass) {
        this.tableClass = tableClass;
    }

    public Integer[] getItemLevelChain() {
        if (itemLevelChain == null) {
            itemLevelChain = new ArrayList<Integer>();
            for (EntityTreeItem item : items) {
                itemLevelChain.add(item.getLevel());
            }

            itemLevelChain = Collections.unmodifiableList(itemLevelChain);
        }

        return DataUtils.toArray(Integer.class, itemLevelChain);
    }

    public int getItemCount() {
        return items.size();
    }

    public void unselectAll() {
        final int len = items.size();
        for (int i = 0; i < len; i++) {
            items.get(i).setSelected(false);
        }
    }

    public void selectAll() {
        final int len = items.size();
        for (int i = 0; i < len; i++) {
            items.get(i).setSelected(true);
        }
    }

    public void select(int index) {
        if (index >= 0 && index < items.size()) {
            items.get(index).setSelected(true);
        }
    }

    public String getTitle() {
        return title;
    }

    public boolean isCenterAlignHeaders() {
        return centerAlignHeaders;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public boolean isMultiColumn() {
        return multiColumn;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<EntityTreeLevel> levels;

        private List<EntityTreeItem> items;

        private Stack<EntityTreeItem> parentItems;

        private String title;

        private boolean centerAlignHeaders;

        private boolean multiSelect;

        private boolean multiColumn;

        private boolean showLabel;

        private int currentLevel;

        public Builder() {
            this.levels = new ArrayList<EntityTreeLevel>();
            this.items = new ArrayList<EntityTreeItem>();
            this.parentItems = new Stack<EntityTreeItem>();
            this.currentLevel = 0;
        }

        public int currentLevel() {
            return currentLevel;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder centerAlignHeaders(boolean centerAlignHeaders) {
            this.centerAlignHeaders = centerAlignHeaders;
            return this;
        }

        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder multiColumn(boolean multiColumn) {
            this.multiColumn = multiColumn;
            return this;
        }

        public Builder showLabel(boolean showLabel) {
            this.showLabel = showLabel;
            return this;
        }

        public Builder addLevel(String label, String lineFormat, boolean multiSelect, TableDef tableDef,
                String... fieldNames) throws UnifyException {
            List<TableColumnDef> columnDefList = new ArrayList<TableColumnDef>();
            for (String fieldName : fieldNames) {
                TableColumnDef tableColumnDef = tableDef.getVisibleColumnDef(fieldName);
                columnDefList.add(tableColumnDef);
            }

            List<StringToken> _lineFormat = !StringUtils.isBlank(lineFormat)
                    ? StringUtils.breakdownParameterizedString(lineFormat)
                    : Collections.emptyList();

            levels.add(new EntityTreeLevel(label, multiSelect, tableDef, DataUtils.unmodifiableList(columnDefList),
                    _lineFormat));
            return this;
        }

        public Builder descend() {
            int nextLevel = currentLevel + 1;
            if (nextLevel >= levels.size()) {
                throw new RuntimeException("Attempted to descend to level " + nextLevel + ". " + levels.size()
                        + " defined for this builder.");
            }

            if (items.isEmpty() || items.get(items.size() - 1).getLevel() != currentLevel) {
                throw new RuntimeException("Can only descend from a parent item at the builder current level");
            }

            parentItems.push(items.get(items.size() - 1));
            currentLevel = nextLevel;
            return this;
        }

        public Builder ascend() {
            int prevLevel = currentLevel - 1;
            if (prevLevel < 0) {
                throw new RuntimeException("Attempted to ascend to level " + prevLevel + ".");
            }

            parentItems.pop();
            currentLevel = prevLevel;
            return this;
        }

        public Builder addItem(Entity _inst) {
            return addItem(_inst, false);
        }

        public Builder addItem(Entity _inst, boolean selected) {
            if (levels.isEmpty()) {
                throw new RuntimeException("You must have at least one item before you can add items");
            }

            items.add(new EntityTreeItem(_inst, currentLevel, selected));
            return this;
        }

        public EntityTreeTable build() {
            if (levels.isEmpty()) {
                throw new RuntimeException("You must have at least one tree level defined.");
            }

            return new EntityTreeTable(DataUtils.unmodifiableList(levels), DataUtils.unmodifiableList(items), title,
                    centerAlignHeaders, multiSelect, multiColumn, showLabel);
        }
    }

    public static class EntityTreeLevel {

        private String label;

        private TableDef tableDef;

        private List<TableColumnInfo> columnDefList;

        private List<StringToken> lineFormat;

        private boolean multiSelect;

        private int totalWidth;

        private EntityTreeLevel(String label, boolean multiSelect, TableDef tableDef,
                List<TableColumnDef> columnDefList, List<StringToken> lineFormat) {
            this.label = label;
            this.tableDef = tableDef;
            this.columnDefList = new ArrayList<TableColumnInfo>();
            for (TableColumnDef tableColumnDef : columnDefList) {
                String columnLabel = tableColumnDef.isWithLabel() ? tableColumnDef.getLabel()
                        : tableDef.getEntityDef().getFieldDef(tableColumnDef.getFieldName()).getFieldLabel();
                this.columnDefList.add(new TableColumnInfo(tableColumnDef, columnLabel));
                this.totalWidth += tableColumnDef.getWidth();
            }

            this.columnDefList = Collections.unmodifiableList(this.columnDefList);
            this.lineFormat = lineFormat;
            this.multiSelect = multiSelect;
        }

        public String getLabel() {
            return label;
        }

        public boolean isMultiSelect() {
            return multiSelect;
        }

        public int getTotalWidth() {
            return totalWidth;
        }

        public TableDef getTableDef() {
            return tableDef;
        }

        public List<TableColumnInfo> getColumnInfoList() {
            return columnDefList;
        }

        public int getColumnCount() {
            return columnDefList.size();
        }

        public List<StringToken> getLineFormat() {
            return lineFormat;
        }
    }

    public static class TableColumnInfo {

        private TableColumnDef tableColumnDef;

        private Widget widget;

        private String label;

        private String style;

        public TableColumnInfo(TableColumnDef _tableColumnDef, String label) {
            this.tableColumnDef = _tableColumnDef;
            this.label = label;
        }

        public Widget getWidget() {
            return widget;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
        }

        public TableColumnDef getTableColumnDef() {
            return tableColumnDef;
        }

        public String getLabel() {
            return label;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }
    }

    public static class EntityTreeItem {

        private ValueStore instValueStore;

        private Entity inst;

        private int level;

        private boolean selected;

        private EntityTreeItem(Entity inst, int level, boolean selected) {
            this.inst = inst;
            this.level = level;
            this.selected = selected;
        }

        public ValueStore getInstValueStore() {
            if (instValueStore == null) {
                synchronized (this) {
                    if (instValueStore == null) {
                        instValueStore = new BeanValueStore(inst);
                    }
                }
            }

            return instValueStore;
        }

        public Entity getInst() {
            return inst;
        }

        public int getLevel() {
            return level;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
