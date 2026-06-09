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

/**
 * Chart category object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartCategory {
    
    private String name;

    private String label;

    private Object val;

    public ChartCategory(String name, String label, Object val) {
        this.name = name;
        this.label = label;
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public Object getVal() {
        return val;
    }

}
