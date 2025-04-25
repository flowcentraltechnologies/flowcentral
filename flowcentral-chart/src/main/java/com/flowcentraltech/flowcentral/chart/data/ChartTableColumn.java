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
package com.flowcentraltech.flowcentral.chart.data;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;

/**
 * Chart table column.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ChartTableColumn {

    private EntityFieldDataType type;
    
    private String fieldName;

    private String label;

    private boolean category;
    
    public ChartTableColumn(EntityFieldDataType type, String fieldName, String label, boolean category) {
        this.type = type;
        this.fieldName = fieldName;
        this.label = label;
        this.category = category;
    }

    public EntityFieldDataType getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getLabel() {
        return label;
    }

    public boolean isCategory() {
        return category;
    }

    public boolean isSeries() {
        return !category;
    }

}
