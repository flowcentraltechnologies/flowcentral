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
package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.MapValues;
import com.tcdng.unify.core.data.MapValuesStore;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Table summary line.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableSummaryLine {

    private String label;

    private MapValues values;

    private ValueStore valueStore;
    
    public TableSummaryLine(String label) {
        this.label = label;
        this.values = new MapValues();
    }

    public TableSummaryLine addSummary(String fieldName, Class<?> type, Object val)
            throws UnifyException {
        values.addValue(fieldName, type, val);
        return this;
    }
    
    public String getLabel() {
        return label;
    }

    public Map<String, Object> values() {
        return values.values();
    }
    
    public ValueStore getValueStore() {
        if (valueStore == null) {
            synchronized(this) {
                if (valueStore == null) {
                    valueStore = new MapValuesStore(values);
                }
            }           
        }
        
        return valueStore;
    }
}
