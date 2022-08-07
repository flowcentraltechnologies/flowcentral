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

package com.flowcentraltech.flowcentral.common.business.policies;

/**
 * Fixed row action type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum FixedRowActionType {
    FIXED(0, "$m{table.row.fixed}"),
    EXCLUDE(1, "$m{table.row.exclude}"),
    INCLUDE(2, "$m{table.row.include}"),
    DELETE(3, "$m{table.row.delete}");
    
    private final int index;
    
    private final String label;
    
    private FixedRowActionType(int index, String label) {
        this.index = index;
        this.label = label;
    }
    
    public int index() {
        return index;
    }
    
    public String label() {
        return label;
    }
}
