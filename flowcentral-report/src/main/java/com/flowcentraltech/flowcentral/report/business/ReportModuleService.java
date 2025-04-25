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
package com.flowcentraltech.flowcentral.report.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.common.business.ReportProvider;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.entities.ReportConfigurationQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportGroup;
import com.flowcentraltech.flowcentral.report.entities.ReportParameter;
import com.flowcentraltech.flowcentral.report.entities.ReportParameterQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinition;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinitionQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.flowcentraltech.flowcentral.report.entities.ReportableFieldQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * Report module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ReportModuleService extends FlowCentralService, ReportProvider {

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
    
    /**
     * Finds report configurations.
     * 
     * @param query
     *                the query
     * @return list of report configurations
     * @throws UnifyException
     *                        if report configuration with ID is not found. If an error occurs
     */
    List<ReportConfiguration> findReportConfigurations(ReportConfigurationQuery query) throws UnifyException;

    /**
     * Finds report configuration by ID.
     * 
     * @param reportConfigurationId
     *                the reportConfiguration ID
     * @return the report configuration
     * @throws UnifyException
     *                        if report configuration with ID is not found. If an error occurs
     */
    ReportConfiguration findReportConfiguration(Long reportConfigurationId) throws UnifyException;

    /**
     * Finds report configuration ID list for application.
     * 
     * @param applicationName
     *                        the application name
     * @return list of application report configuration IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> findCustomReportConfigurationIdList(String applicationName) throws UnifyException;

    /**
     * Creates a reportable definition.
     * 
     * @param reportableDefinition
     *                             the reportable definition
     * @return the created definition ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createReportableDefinition(ReportableDefinition reportableDefinition) throws UnifyException;

    /**
     * Updates a reportable definition.
     * 
     * @param reportableDefinition
     *                update cout
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateReportableDefinition(ReportableDefinition reportableDefinition) throws UnifyException;

    /**
     * Finds reportable definitions by criteria.
     * 
     * @param query
     *              the search query
     * @return list of reportable definitions
     * @throws UnifyException
     *                        if an error occurs
     */
    List<ReportableDefinition> findReportDefinitions(ReportableDefinitionQuery query) throws UnifyException;

    /**
     * Finds report definition by criteria.
     * 
     * @param query
     *              the search query
     * @return the report definition otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    ReportableDefinition findReportDefinition(ReportableDefinitionQuery query) throws UnifyException;

    /**
     * Counts reportable definitions by criteria.
     * 
     * @param query
     *              the search query
     * @return count of reportable definitions
     * @throws UnifyException
     *                        if an error occurs
     */
    int countReportDefinitions(ReportableDefinitionQuery query) throws UnifyException;

    /**
     * Gets the reportable definition ID for reportable.
     * 
     * @param reportableName
     *                       the reportable long name
     * @return the reportable definition ID if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Long getReportableDefinitionId(String reportableName) throws UnifyException;

    /**
     * Gets the entity for reportable.
     * 
     * @param reportableName
     *                       the reportable long name
     * @return the entity name if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    String getReportableEntity(String reportableName) throws UnifyException;

    /**
     * Gets the reportable definition ID for report configuration.
     * 
     * @param reportConfigurationId
     *                              the report configuration ID
     * @return the reportable definition ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Long getReportConfigReportableDefinitionId(Long reportConfigurationId) throws UnifyException;

    /**
     * Create reportable field.
     * 
     * @param reportableField
     *                        the reportable field
     * @return the created record ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createReportableField(ReportableField reportableField) throws UnifyException;

    /**
     * Finds reportable field by criteria.
     * 
     * @param query
     *              the search query
     * @return the reportable field if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    ReportableField findReportableField(ReportableFieldQuery query) throws UnifyException;

    /**
     * Finds reportable fields by criteria.
     * 
     * @param query
     *              the search query
     * @return list of reportable fields
     * @throws UnifyException
     *                        if an error occurs
     */
    List<ReportableField> findReportableFields(ReportableFieldQuery query) throws UnifyException;

    /**
     * Updates a reportable field.
     * 
     * @param reportableField
     *                        updates a reportable field
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateReportableField(ReportableField reportableField) throws UnifyException;

    /**
     * Deletes reportable field by criteria.
     * 
     * @param query
     *              the search query
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int deleteReportableField(ReportableFieldQuery query) throws UnifyException;

    /**
     * Finds report parameters by criteria.
     * 
     * @param query
     *              the search query
     * @return list of report parameters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<ReportParameter> findReportParameters(ReportParameterQuery query) throws UnifyException;
}
