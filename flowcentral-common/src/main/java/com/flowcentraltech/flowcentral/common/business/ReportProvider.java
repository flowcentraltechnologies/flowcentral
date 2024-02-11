/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.common.business;

import java.io.OutputStream;
import java.util.List;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.DefaultReportColumn;
import com.flowcentraltech.flowcentral.common.data.ReportListing;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.ReportColumn;

/**
 * Report provider component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ReportProvider extends FlowCentralComponent {

    /**
     * Creates a new report options for dynamic reportable associated with supplied
     * entity name.
     * 
     * @param entityName
     *                                the entity name
     * @param defaultReportColumnList
     *                                the default report column list
     * @return the new report options
     * @throws UnifyException
     *                        if an error occurs
     */
    ReportOptions getReportableEntityDynamicReportOptions(String entityName,
            List<DefaultReportColumn> defaultReportColumnList) throws UnifyException;

    /**
     * Get report options for configured report.
     * 
     * @param reportConfigName
     *                         the report configuration long name
     * @return the report options object
     * @throws UnifyException
     *                        if an error occurs
     */
    ReportOptions getReportOptionsForConfiguration(String reportConfigName) throws UnifyException;

    /**
     * Generates a dynamic report based on supplied report options.
     * 
     * @param reportOptions
     *                      the report options
     * @param outputStream
     *                      the output stream where generated report is written to
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateDynamicReport(ReportOptions reportOptions, OutputStream outputStream) throws UnifyException;

    /**
     * Generates a report.
     * 
     * @param report
     *                      the report to generate
     * @param outputStream
     *                      the output stream where generated report is written to
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateReport(Report report, OutputStream outputStream) throws UnifyException;

    /**
     * Generates a report to a byte array.
     * 
     * @param report
     *                      the report to generate
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] generateReportAsByteArray(Report report) throws UnifyException;

    /**
     * Finds report columns of a reportable.
     * 
     * @param reportableName
     *                       the reportable code
     * @return the report columns
     * @throws UnifyException
     *                        if an error occurs
     */
    ReportColumn[] findReportableColumns(String reportableName) throws UnifyException;

    /**
     * Returns true if entity is reportable by this. provider.
     * 
     * @param entityName
     *                   the entity name
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isReportable(String entityName) throws UnifyException;

    /**
     * Gets configured report listing based on user role.
     * 
     * @param roleCode
     *                 the role code. Assumes super user if role is null.
     * @return the report listing
     * @throws UnifyException
     *                        if an error occurs
     */
    List<ReportListing> getRoleReportListing(String roleCode) throws UnifyException;

    /**
     * Populates extra report options data that depend on input parameters.
     * 
     * @param reportOptions
     *                      the report options to populate
     * @throws UnifyException
     *                        if an error occurs
     */
    void populateExtraConfigurationReportOptions(ReportOptions reportOptions) throws UnifyException;
}
