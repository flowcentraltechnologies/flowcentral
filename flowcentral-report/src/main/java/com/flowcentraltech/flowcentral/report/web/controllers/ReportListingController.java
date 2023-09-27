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
package com.flowcentraltech.flowcentral.report.web.controllers;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralResultMappingConstants;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.flowcentraltech.flowcentral.report.business.ReportModuleService;
import com.flowcentraltech.flowcentral.report.data.ReportGroupInfo;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.report.entities.ReportGroup;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageController;
import com.tcdng.unify.web.ui.widget.data.LinkGridInfo;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Report listing controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/report/reportlisting")
@UplBinding("web/report/upl/reportlisting.upl")
public class ReportListingController extends AbstractPageController<ReportListingPageBean> {

    @Configurable(CommonModuleNameConstants.CONFIGURED_REPORT_RESOURCE)
    private String reportResourcePath;

    @Configurable
    private ReportModuleService reportModuleService;

    public ReportListingController() {
        super(ReportListingPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public void setReportResourcePath(String reportResourcePath) {
        this.reportResourcePath = reportResourcePath;
    }

    public void setReportModuleService(ReportModuleService reportModuleService) {
        this.reportModuleService = reportModuleService;
    }

    @Action
    public String prepareGenerateReport() throws UnifyException {
        String reportConfigName = getPageRequestContextUtil().getRequestTargetValue(String.class);
        ReportOptions reportOptions = reportModuleService.getReportOptionsForConfiguration(reportConfigName);
        reportOptions.setReportResourcePath(reportResourcePath);
        reportOptions.setUserInputOnly(true);
        return showPopup(new Popup(FlowCentralResultMappingConstants.SHOW_APPLICATION_REPORT_OPTIONS, reportOptions));
    }

    @Override
    protected void onInitPage() throws UnifyException {
        setPageValidationEnabled(true);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        ReportListingPageBean pageBean = getPageBean();
        List<ReportGroupInfo> groupList = new ArrayList<ReportGroupInfo>();
        final UserToken userToken = getUserToken();
        final String roleCode = !userToken.isReservedUser() ? userToken.getRoleCode() : null;
        for (ReportGroup reportGroup : reportModuleService.findReportGroupsByRole(roleCode)) {
            List<ReportConfiguration> configurationList = reportModuleService
                    .findReportConfigurationsByGroup(reportGroup.getId());
            if (!DataUtils.isBlank(configurationList)) {
                LinkGridInfo.Builder lb = LinkGridInfo.newBuilder();
                lb.addCategory(reportGroup.getName(), null, "/report/reportlisting/prepareGenerateReport");
                for (ReportConfiguration reportConfiguration : configurationList) {
                    final String reportConfigName = ApplicationNameUtils.getApplicationEntityLongName(
                            reportConfiguration.getApplicationName(), reportConfiguration.getName());
                    lb.addLink(reportGroup.getName(), reportConfigName, reportConfiguration.getDescription());
                }

                LinkGridInfo linkGridInfo = lb.build();
                groupList.add(new ReportGroupInfo(reportGroup.getLabel(), linkGridInfo));
            }
        }

        pageBean.setGroupList(groupList);
    }

}
