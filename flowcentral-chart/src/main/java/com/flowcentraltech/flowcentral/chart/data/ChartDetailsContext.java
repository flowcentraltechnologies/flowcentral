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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Chart details context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartDetailsContext {

    private final CDSnapshot cdSnapshot;
    
    private final Long chartDatasourceId;

    private final Map<String, CDSnapshotCategory> catmap;
    
    public ChartDetailsContext(CDSnapshot cdSnapshot, Long chartDatasourceId) {
        this.cdSnapshot = cdSnapshot;
        this.chartDatasourceId = chartDatasourceId;
        
        this.catmap = new HashMap<String, CDSnapshotCategory>();
        for (CDSnapshotCategory cat: cdSnapshot.getCategories()) {
            this.catmap.put(cat.getCat(), cat);
        }
    }

    public Long getChartDatasourceId() {
        return chartDatasourceId;
    }

    public String getViewOption() {
        return cdSnapshot.getView();
    }
    
    public Date getSnapshotExpiresOn() {
        return cdSnapshot.getExpiresOn();
    }

    public ChartCategory newChartCategory(String catName) {
        CDSnapshotCategory cat = catmap.get(catName);
        if (cat == null) {
            throw new IllegalArgumentException("Category with name [" + catName + "] is unknown.");
        }
        
        return new ChartCategory(cat.getCat(), cat.getLbl(), cat.getCat());
    }
    
    
}
