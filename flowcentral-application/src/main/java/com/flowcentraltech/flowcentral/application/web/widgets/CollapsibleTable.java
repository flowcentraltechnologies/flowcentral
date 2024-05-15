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
import java.util.List;

import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Collapsible table.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CollapsibleTable {

    private List<Column> columns;

    private List<Row> rows;

    private int numberOfLevels;

    private int levelWidth;

    private int fieldWidth;
    
    private CollapsibleTable(List<Column> columns, List<Row> rows, int numberOfLevels) {
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

    public int getNumberOfLevels() {
        return numberOfLevels;
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

        public Builder addColumn(String fieldName, HAlignType align) {
            columns.add(new Column(fieldName, align));
            return this;
        }

        public Builder addColumn(String fieldName) {
            columns.add(new Column(fieldName, HAlignType.LEFT));
            return this;
        }

        public Builder addRow(String label, Object data) {
            rows.add(new Row(data, label, currentDepth));
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

        public CollapsibleTable build() {
            return new CollapsibleTable(DataUtils.unmodifiableList(columns), DataUtils.unmodifiableList(rows),
                    currentDepth + 1);
        }
    }

    public static class Column {

        private final String fieldName;

        private final HAlignType align;

        public Column(String fieldName, HAlignType align) {
            this.fieldName = fieldName;
            this.align = align;
        }

        public String getFieldName() {
            return fieldName;
        }

        public HAlignType getAlign() {
            return align;
        }
    }

    public static class Row {

        private final Object data;

        private final String label;

        private final int depth;

        private boolean expanded;

        public Row(Object data, String label, int depth) {
            this.data = data;
            this.label = label;
            this.depth = depth;
        }

        public Object getData() {
            return data;
        }

        public int getDepth() {
            return depth;
        }

        public String getLabel() {
            return label;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }
    }
}
