/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.chart.data;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;

/**
 * Chart series object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartSeries {

    private EntityFieldDataType type;

    private String name;

    private String label;

    private String field;

    private Object[] vals;

    private int grouping; // 0 - No grouping, 1 - Text and 2 - Datetime

    private boolean time;

    public ChartSeries(EntityFieldDataType type, String name, String label, String field, int grouping, Object[] vals,
            boolean time) {
        this.type = type;
        this.name = name;
        this.label = label;
        this.field = field;
        this.grouping = grouping;
        this.vals = vals;
        this.time = time;
    }

    public EntityFieldDataType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getField() {
        return field;
    }

    public int getGrouping() {
        return grouping;
    }

    public int getDataDepth() {
        return vals.length;
    }
    
    public Object getVal(int index) {
        return vals[index];
    }

    public boolean isTime() {
        return time;
    }

}
