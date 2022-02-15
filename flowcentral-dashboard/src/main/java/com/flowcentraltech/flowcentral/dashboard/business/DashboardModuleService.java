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
package com.flowcentraltech.flowcentral.dashboard.business;

import java.util.List;

import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.tcdng.unify.core.UnifyException;

/**
 * Dashboard business service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface DashboardModuleService extends FlowCentralService {

    /**
     * Finds dashboard by ID.
     * 
     * @param dashboardId
     *                the dashboard ID
     * @return the dashboard
     * @throws UnifyException
     *                        if dashboard with ID is not found. If an error occurs
     */
    Dashboard findDashboard(Long dashboardId) throws UnifyException;

    /**
     * Finds dashboard ID list for application.
     * 
     * @param applicationName
     *                        the application name
     * @return list of application dashboard IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> findDashboardIdList(String applicationName) throws UnifyException;

    /**
     * Gets application dashboard definition.
     * 
     * @param dashboardName
     *                      the dashboard long name
     * @return the dashboard definition
     * @throws UnifyException
     *                        if an error occurs
     */
    DashboardDef getDashboardDef(String dashboardName) throws UnifyException;

}
