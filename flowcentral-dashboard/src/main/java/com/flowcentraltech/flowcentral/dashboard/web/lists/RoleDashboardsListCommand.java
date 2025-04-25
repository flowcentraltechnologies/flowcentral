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

package com.flowcentraltech.flowcentral.dashboard.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.LicenseProvider;
import com.flowcentraltech.flowcentral.common.business.WorkspacePrivilegeManager;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Role dashboards list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("roledashboardslist")
public class RoleDashboardsListCommand extends AbstractDashboardListCommand<ZeroParams> {

    @Configurable
    private ApplicationPrivilegeManager appPrivilegeManager;

    @Configurable
    private WorkspacePrivilegeManager wkspPrivilegeManager;

    @Configurable
    private LicenseProvider licenseProvider;

    public RoleDashboardsListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        final String workspaceCode = (String) getSessionAttribute(FlowCentralSessionAttributeConstants.WORKSPACE_CODE);
        List<String> roleDashboardList = appPrivilegeManager.findRolePrivileges(
                ApplicationPrivilegeConstants.APPLICATION_DASHBOARD_CATEGORY_CODE, getUserToken().getRoleCode());
        if (!DataUtils.isBlank(roleDashboardList)) {
            List<Listable> resultList = new ArrayList<Listable>();
            for (String dashboardPrivName : roleDashboardList) {
                if (wkspPrivilegeManager == null
                        || wkspPrivilegeManager.isWorkspaceWithPrivilege(workspaceCode, dashboardPrivName)) {
                    DashboardDef dashboardDef = getDashboardModuleService().getDashboardDef(
                            PrivilegeNameUtils.getPrivilegeNameParts(dashboardPrivName).getEntityName());
                    if (dashboardDef.isActive()) {
                    	resultList.add(dashboardDef);
                    }
                }
            }

            DataUtils.sortAscending(resultList, Listable.class, "listDescription");
            return resultList;
        }

        return Collections.emptyList();
    }

}
