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

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Collapsible information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CollapsibleInfo {

    private List<Column> columns;

    private List<Row> rows;

    private int numberOfLevels;

    private CollapsibleInfo(List<Column> columns, List<Row> rows, int numberOfLevels) {
        this.columns = columns;
        this.rows = rows;
        this.numberOfLevels = numberOfLevels;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public int getNumberOfColumns() {
        return columns.size();
    }

    public int getNumberOfRows() {
        return rows.size();
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public void expandAll() {
        for (Row row: rows) {
            if (row.isExpandable()) {
                row.setExpanded(true);
            }
        }
    }

    public void collapseAll() {
        for (Row row: rows) {
            if (row.isExpandable()) {
                row.setExpanded(false);
            }
        }
    }
    
    public boolean toggle(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            Row row = rows.get(rowIndex);
            if (row.isExpandable()) {
                row.setExpanded(!row.isExpanded());
                return true;
            }
        }
        
        return false;
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<Column> columns;

        private List<Row> rows;

        private int maxDepth;

        private int currentDepth;

        public Builder() {
            this.columns = new ArrayList<Column>();
            this.rows = new ArrayList<Row>();
        }

        public Builder addColumn(String fieldName, HAlignType align, int widthInPercent) {
            columns.add(new Column(fieldName, align, widthInPercent));
            return this;
        }

        public Builder addColumn(String fieldName, int widthInPercent) {
            columns.add(new Column(fieldName, HAlignType.LEFT, widthInPercent));
            return this;
        }

        public Builder addRow(boolean expandable, Object data) {
            rows.add(new Row(data, currentDepth, expandable));
            return this;
        }

        public Builder rise() {
            if (currentDepth > 0) {
                currentDepth--;
            }

            return this;
        }

        public Builder sink() {
            currentDepth++;
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }

            return this;
        }

        public CollapsibleInfo build() {
            return new CollapsibleInfo(DataUtils.unmodifiableList(columns), DataUtils.unmodifiableList(rows),
                    maxDepth + 1);
        }
    }

    public static class Column {

        private final String fieldName;

        private final HAlignType align;

        private final int widthInPercent;

        public Column(String fieldName, HAlignType align, int widthInPercent) {
            this.fieldName = fieldName;
            this.align = align;
            this.widthInPercent = widthInPercent;
        }

        public String getFieldName() {
            return fieldName;
        }

        public HAlignType getAlign() {
            return align;
        }

        public int getWidthInPercent() {
            return widthInPercent;
        }
    }

    public static class Row {

        private final Object data;

        private final int depth;

        private final boolean expandable;

        private boolean expanded;

        public Row(Object data, int depth, boolean expandable) {
            this.data = data;
            this.depth = depth;
            this.expandable = expandable;
        }

        public Object getData() {
            return data;
        }

        public int getDepth() {
            return depth;
        }

        public boolean isRise(int depth) {
            return this.depth < depth;
        }

        public boolean isVisible(int depth) {
            return this.depth == depth;
        }

        public boolean isExpandable() {
            return expandable;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }
    }
}
