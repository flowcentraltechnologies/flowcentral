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
package com.flowcentraltech.flowcentral.report.web.controllers;

import java.io.OutputStream;

import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.flowcentraltech.flowcentral.report.business.ReportModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageResourceController;

/**
 * Backing resource controller for retrieving a generated configured report
 * report.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(CommonModuleNameConstants.CONFIGURED_REPORT_RESOURCE)
public class ConfiguredReportResourceController extends AbstractPageResourceController {

    @Configurable
    private ReportModuleService reportModuleService;

    public ConfiguredReportResourceController() {
        super(Secured.TRUE);
    }

    public void setReportModuleService(ReportModuleService reportModuleService) {
        this.reportModuleService = reportModuleService;
    }

    @Override
    public void prepareExecution() throws UnifyException {
        setContentDisposition(getResourceName());
    }

    @Override
    public void execute(OutputStream outputStream) throws UnifyException {
        ReportOptions reportOptions = (ReportOptions) removeSessionAttribute(getResourceName());
        reportModuleService.populateExtraConfigurationReportOptions(reportOptions);
        reportModuleService.generateDynamicReport(reportOptions, outputStream);
    }
}
