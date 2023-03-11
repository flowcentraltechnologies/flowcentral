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
package com.flowcentraltech.flowcentral.dashboard.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameParts;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.web.widgets.DashboardSlate;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageController;

/**
 * Application dashboard controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/application/dashboard")
@UplBinding("web/dashboard/upl/applicationdashboard.upl")
@ResultMappings({ @ResultMapping(name = "refreshSlate",
        response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{dashboardSlatePanel}" }) })
public class ApplicationDashboardController extends AbstractPageController<ApplicationDashboardPageBean> {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private DashboardModuleService dashboardModuleService;

    public ApplicationDashboardController() {
        super(ApplicationDashboardPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public void setDashboardModuleService(DashboardModuleService dashboardModuleService) {
        this.dashboardModuleService = dashboardModuleService;
    }

    @Action
    public String refreshSlate() throws UnifyException {
        return "refreshSlate";
    }

    @Action
    public String loadDashboardSlate() throws UnifyException {
        ApplicationDashboardPageBean pageBean = getPageBean();
        pageBean.setDashboardSlate(null);
        if (!StringUtils.isBlank(pageBean.getSelDashboard())) {
            DashboardDef dashboardDef = dashboardModuleService.getDashboardDef(pageBean.getSelDashboard());
            if (dashboardDef.isActive()) {
                pageBean.setDashboardSlate(new DashboardSlate(dashboardDef));
            } else {
                pageBean.setSelDashboard(null);
            }
        }

        return refreshSlate();
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        setPageTitle(resolveSessionMessage("$m{applicationdashboard.dashboard}"));
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        getPageRequestContextUtil().considerDefaultFocusOnWidget();
        ApplicationDashboardPageBean pageBean = getPageBean();
        if (pageBean.getDashboardSlate() == null || (!StringUtils.isBlank(pageBean.getSelDashboard())
                && !dashboardModuleService.getDashboardDef(pageBean.getSelDashboard()).isActive())) {
            UserToken userToken = getUserToken();
            List<String> roleDashboardList = applicationPrivilegeManager.findRolePrivileges(
                    ApplicationPrivilegeConstants.APPLICATION_DASHBOARD_CATEGORY_CODE, userToken.getRoleCode());
            if (!roleDashboardList.isEmpty()) {
                for (String privilege : roleDashboardList) {
                    PrivilegeNameParts pnp = PrivilegeNameUtils.getPrivilegeNameParts(privilege);
                    DashboardDef dashboardDef = dashboardModuleService.getDashboardDef(pnp.getEntityName());
                    if (dashboardDef.isActive()) {
                        pageBean.setSelDashboard(pnp.getEntityName());
                    }
                }
            }
        }

        loadDashboardSlate();
    }

}
