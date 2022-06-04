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

import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.constant.TriState;

/**
 * Table state override,
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableStateOverride {

    private Map<String, TableColumnStateOverride> columnStateOverrides;

    public TableStateOverride() {
        this.columnStateOverrides = new HashMap<String, TableColumnStateOverride>();
    }
    
    public void setColumnRequired(String fieldName, boolean required) {
        getColumnStateOverride(fieldName).required = TriState.getTriState(required);
    }

    public boolean isColumnRequired(String fieldName, boolean required) {
        TableColumnStateOverride override = columnStateOverrides.get(fieldName);
        return override != null && !override.required.isConforming() ? override.required.isTrue() : required;
    }
    
    public void setColumnEditable(String fieldName, boolean editable) {
        getColumnStateOverride(fieldName).editable = TriState.getTriState(editable);
    }

    public boolean isColumnEditable(String fieldName, boolean editable) {
        TableColumnStateOverride override = columnStateOverrides.get(fieldName);
        return override != null && !override.editable.isConforming() ? override.editable.isTrue() : editable;
    }
    
    public void setColumnDisabled(String fieldName, boolean disabled) {
        getColumnStateOverride(fieldName).disabled = TriState.getTriState(disabled);
    }

    public boolean isColumnDisabled(String fieldName, boolean disabled) {
        TableColumnStateOverride override = columnStateOverrides.get(fieldName);
        return override != null && !override.disabled.isConforming() ? override.disabled.isTrue() : disabled;
    }
   
    public void reset() {
        if (columnStateOverrides != null) {
            for (TableColumnStateOverride override: columnStateOverrides.values()) {
                override.reset();
            }
        }
    }

    private TableColumnStateOverride getColumnStateOverride(String fieldName) {
        if (columnStateOverrides == null) {
            columnStateOverrides = new HashMap<String, TableColumnStateOverride>();
        }

        TableColumnStateOverride override = columnStateOverrides.get(fieldName);
        if (override == null) {
            override = new TableColumnStateOverride();
            columnStateOverrides.put(fieldName, override);
        }
        
        return override;
    }
    
    private class TableColumnStateOverride {

        private TriState required;

        private TriState editable;

        private TriState disabled;

        public TableColumnStateOverride() {
            reset();
        }

        public void reset() {
            required = TriState.CONFORMING;
            editable = TriState.CONFORMING;
            disabled = TriState.CONFORMING;
        }
    }
}
