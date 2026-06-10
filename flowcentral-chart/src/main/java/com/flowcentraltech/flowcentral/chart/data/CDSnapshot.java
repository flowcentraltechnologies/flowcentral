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

/**
 * Chart datasource snapshot.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class CDSnapshot {

    private String chartDatasourceName;
    
    private String view;
    
    private Date takenOn;
    
    private Date expiresOn;

    private int groupingStart;

    private boolean datetimeGrouping;

    private boolean numericMerged;
    
    private CDSnapshotCategory[] categories;

    private String[] groupingNames;

    public String getChartDatasourceName() {
        return chartDatasourceName;
    }

    public void setChartDatasourceName(String chartDatasourceName) {
        this.chartDatasourceName = chartDatasourceName;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Date getTakenOn() {
        return takenOn;
    }

    public void setTakenOn(Date takenOn) {
        this.takenOn = takenOn;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    public CDSnapshotCategory[] getCategories() {
        return categories;
    }

    public void setCategories(CDSnapshotCategory[] categories) {
        this.categories = categories;
    }

    public String[] getGroupingNames() {
        return groupingNames;
    }

    public void setGroupingNames(String[] groupingNames) {
        this.groupingNames = groupingNames;
    }

    public int getGroupingStart() {
        return groupingStart;
    }

    public void setGroupingStart(int groupingStart) {
        this.groupingStart = groupingStart;
    }

    public boolean isDatetimeGrouping() {
        return datetimeGrouping;
    }

    public void setDatetimeGrouping(boolean datetimeGrouping) {
        this.datetimeGrouping = datetimeGrouping;
    }

    public boolean isNumericMerged() {
        return numericMerged;
    }

    public void setNumericMerged(boolean numericMerged) {
        this.numericMerged = numericMerged;
    }

}
