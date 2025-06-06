/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

import java.io.OutputStream;

import com.flowcentraltech.flowcentral.common.business.ReportProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageResourceController;

/**
 * Common resource controller for generating a listing report.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("/common/resource/listingreport")
public class CommonListingReportResourceController extends AbstractPageResourceController {

    @Configurable
    private ReportProvider reportProvider;

    public CommonListingReportResourceController() {
        super(Secured.FALSE);
    }

    @Override
    public void prepareExecution() throws UnifyException {
        setContentDisposition(getResourceName());
    }

    @Override
    public String execute(OutputStream outputStream) throws UnifyException {
        Report report = (Report) removeSessionAttribute(getResourceName());
        reportProvider.generateReport(report, outputStream);
        return null;
    }
}
