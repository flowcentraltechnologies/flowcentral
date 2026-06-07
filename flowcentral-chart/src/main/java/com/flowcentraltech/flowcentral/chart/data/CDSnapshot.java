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
import java.util.List;

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
    
    private List<CDSnapshotSeries> series;

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

    public List<CDSnapshotSeries> getSeries() {
        return series;
    }

    public void setSeries(List<CDSnapshotSeries> series) {
        this.series = series;
    }

}
