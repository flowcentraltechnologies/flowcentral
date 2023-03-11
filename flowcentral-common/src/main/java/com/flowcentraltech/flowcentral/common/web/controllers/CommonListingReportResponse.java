/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.common.web.controllers;

import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.ReportFormat;
import com.tcdng.unify.web.ui.AbstractOpenWindowPageControllerResponse;

/**
 * Used for preparing the generation of a listing report and the presentation of the
 * report in a window.
 * <p>
 * Expects the report options be stored in the request scope using the key
 * {@link FlowCentralRequestAttributeConstants#REPORT}.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("commonlistingreportresponse")
public class CommonListingReportResponse extends AbstractOpenWindowPageControllerResponse {

    @Override
    protected WindowResourceInfo prepareWindowResource() throws UnifyException {
        Report report = (Report) getRequestAttribute(FlowCentralRequestAttributeConstants.REPORT);
        String resourceName = getTimestampedResourceName(report.getTitle()) + ReportFormat.PDF.fileExt();
        logDebug("Preparing window resource for listing report [{0}]...", resourceName);
        return new WindowResourceInfo(report, "/common/resource/listingreport", resourceName,
                ReportFormat.PDF.mimeType().template(), false);
    }
}
