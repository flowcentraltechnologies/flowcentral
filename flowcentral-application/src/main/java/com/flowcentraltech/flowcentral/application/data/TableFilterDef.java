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
package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.core.data.Listable;

/**
 * Table filter definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableFilterDef implements Listable {

    private FilterDef filterDef;

    private String rowColor;

    public TableFilterDef(FilterDef filterDef, String rowColor) {
        this.filterDef = filterDef;
        this.rowColor = rowColor;
    }

    @Override
    public String getListDescription() {
        return filterDef.getListDescription();
    }

    @Override
    public String getListKey() {
        return filterDef.getListKey();
    }

    public FilterDef getFilterDef() {
        return filterDef;
    }

    public String getName() {
        return filterDef.getName();
    }

    public String getRowColor() {
        return rowColor;
    }

    public boolean isRowColor() {
        return rowColor != null;
    }
}
