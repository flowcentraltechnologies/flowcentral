/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workflow.data;

import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.tcdng.unify.common.data.Listable;

/**
 * Workflow filter definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WfFilterDef implements Listable {

    private FilterDef filterDef;

    public WfFilterDef(FilterDef filterDef) {
        this.filterDef = filterDef;
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

    public boolean isWithFilter() {
        return filterDef != null;
    }
}
