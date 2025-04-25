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
package com.flowcentraltech.flowcentral.application.listing;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.constant.HAlignType;

/**
 * Listing section header.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ListingSectionHeader {

    private ListingColumn[] columns;

    private ListingCell[] cells;

    private ListingSectionHeader(ListingColumn[] columns, ListingCell[] cells) {
        this.columns = columns;
        this.cells = cells;
    }

    public ListingColumn[] getColumns() {
        return columns;
    }

    public ListingCell[] getCells() {
        return cells;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<ListingColumn> columns;

        private List<ListingCell> cells;

        public Builder() {
            this.columns = new ArrayList<ListingColumn>();
            this.cells = new ArrayList<ListingCell>();
        }

        public Builder addColumn(HAlignType align, int widthPercent, ListingCellType cellType, String content,
                int borders) {
            columns.add(new ListingColumn(align, widthPercent));
            cells.add(new ListingCell(cellType, content, borders));
            return this;
        }

        public ListingSectionHeader build() {
            return new ListingSectionHeader(columns.toArray(new ListingColumn[columns.size()]),
                    cells.toArray(new ListingCell[cells.size()]));
        }
    }
}
