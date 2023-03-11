/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.EntityFieldTotalSummary;

/**
 * Table total summary.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableTotalSummary {

    private String totalLabelColumn;

    private Map<String, EntityFieldTotalSummary> summaries;

    public TableTotalSummary(String totalLabelColumn,  Map<String, EntityFieldTotalSummary> summaries) {
        this.totalLabelColumn = totalLabelColumn;
        this.summaries = summaries;
    }

    public String getTotalLabelColumn() {
        return totalLabelColumn;
    }

    public Map<String, EntityFieldTotalSummary> getSummaries() {
        return summaries;
    }
    
}
