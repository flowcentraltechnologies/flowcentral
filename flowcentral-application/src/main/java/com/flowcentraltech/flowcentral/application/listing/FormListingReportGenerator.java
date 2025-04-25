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

package com.flowcentraltech.flowcentral.application.listing;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.report.Report;

/**
 * Form listing report generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface FormListingReportGenerator extends FlowCentralComponent {

    /**
     * Generates form HTML report into builder.
     * 
     * @param rb
     *                       the report builder
     * @param reader
     *                       the form bean value store reader
     * @param listingOptions
     *                       form listing options
     * @return report
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateHtmlReport(Report.Builder rb, ValueStoreReader reader, FormListingOptions listingOptions)
            throws UnifyException;

    /**
     * Generates form HTML report.
     * 
     * @param reader
     *                       the form bean value store reader
     * @param listingOptions
     *                       form listing options
     * @return report
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateHtmlReport(ValueStoreReader reader, FormListingOptions listingOptions) throws UnifyException;

    /**
     * Generates form Excel report.
     * 
     * @param reader
     *                       the form bean value store reader
     * @param listingOptions
     *                       form listing options
     * @return report
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateExcelReport(ValueStoreReader reader, FormListingOptions listingOptions) throws UnifyException;
}
