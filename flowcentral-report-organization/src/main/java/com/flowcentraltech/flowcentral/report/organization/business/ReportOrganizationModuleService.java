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
package com.flowcentraltech.flowcentral.report.organization.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.organization.entities.ReportGroup;
import com.tcdng.unify.core.UnifyException;

/**
 * Report organization module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface ReportOrganizationModuleService extends FlowCentralService {

    /**
     * Finds report groups with role option.
     * 
     * @param role
     *             the role
     * @return list of report groups.
     * @throws UnifyException
     *                        if an error occurs
     */
    List<ReportGroup> findReportGroupsByRole(String role) throws UnifyException;
    
    /**
     * Finds report configurations by report group ID.
     * 
     * @param reportGroupId
     *                the report group ID
     * @return list of report configurations
     * @throws UnifyException
     *                        If an error occurs
     */
    List<ReportConfiguration> findReportConfigurationsByGroup(Long reportGroupId) throws UnifyException;
   

}
