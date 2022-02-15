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
package com.flowcentraltech.flowcentral.chart.business;

import java.util.List;

import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartSnapshotDef;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.chart.entities.ChartQuery;
import com.flowcentraltech.flowcentral.chart.entities.ChartSnapshot;
import com.flowcentraltech.flowcentral.chart.entities.ChartSnapshotQuery;
import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.tcdng.unify.core.UnifyException;

/**
 * Chart business service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ChartModuleService extends FlowCentralService {

    /**
     * Finds charts by criteria.
     * 
     * @param query
     *              the query object
     * @return list of charts
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Chart> findCharts(ChartQuery query) throws UnifyException;

    /**
     * Finds chart by ID.
     * 
     * @param chartId
     *                the chart ID
     * @return the chart
     * @throws UnifyException
     *                        if chart with ID is not found. If an error occurs
     */
    Chart findChart(Long chartId) throws UnifyException;

    /**
     * Finds chart ID list for application.
     * 
     * @param applicationName
     *                        the application name
     * @return list of application chart IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> findChartIdList(String applicationName) throws UnifyException;
    
    /**
     * Finds snapshots by criteria.
     * 
     * @param query
     *              the query object
     * @return list of chart snapshots
     * @throws UnifyException
     *                        if an error occurs
     */
    List<ChartSnapshot> findChartSnapshots(ChartSnapshotQuery query) throws UnifyException;

    /**
     * Gets a chart definition.
     * 
     * @param chartName
     *                  the chart long name
     * @return the chart definition
     * @throws UnifyException
     *                        if an error occurs
     */
    ChartDef getChartDef(String chartName) throws UnifyException;

    /**
     * Gets a chart snapshot definition.
     * 
     * @param snapshotName
     *                     the snapshot name
     * @return the chart snapshot
     * @throws UnifyException
     *                        if an error occurs
     */
    ChartSnapshotDef getChartSnapshotDef(String snapshotName) throws UnifyException;

    /**
     * Checks if chart snapshot exists.
     * 
     * @param snapshotName
     *                     the snapshot name
     * @return true if snapshot with name exists otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isChartSnapshotExist(String snapshotName) throws UnifyException;

    /**
     * Saves a chart snapshot. Updates existing record or creates a new one if
     * necessary.
     * 
     * @param chartSnapshot
     *                      the snapshot to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveChartSnapshot(ChartSnapshot chartSnapshot) throws UnifyException;
}
