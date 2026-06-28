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
 * Chart datasource snapshot category object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class CDSnapshotCategory {
    
    private String nm;

    private String lbl;
    
    private CDSnapshotSeries[] series;

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getLbl() {
        return lbl;
    }

    public void setLbl(String lbl) {
        this.lbl = lbl;
    }

    public CDSnapshotSeries[] getSeries() {
        return series;
    }

    public void setSeries(CDSnapshotSeries[] series) {
        this.series = series;
    }

}
