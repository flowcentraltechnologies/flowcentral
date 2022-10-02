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
package com.flowcentraltech.flowcentral.common.data;

/**
 * Table column summary value.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableColumnSummaryVal {

    private Number val;
    
    private boolean replace;

    public TableColumnSummaryVal(Number val, boolean replace) {
        this.val = val;
        this.replace = replace;
    }

    public TableColumnSummaryVal(Number val) {
        this.val = val;
    }

    public Number getVal() {
        return val;
    }

    public boolean isReplace() {
        return replace;
    }
}
