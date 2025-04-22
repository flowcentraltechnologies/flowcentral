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

package com.flowcentraltech.flowcentral.studio.business.policies;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Studio on delete application dashboard policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({ "application.dashboard" })
@Component("studioondeletedashboard-policy")
public class StudioOnDeleteDashboardPolicy extends AbstractStudioAppletActionPolicy {

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        final Dashboard dashboard = (Dashboard) ctx.getInst();
        final String dashboardName = ApplicationNameUtils.getApplicationEntityLongName(dashboard.getApplicationName(),
                dashboard.getName());
        unregisterPrivilege(dashboard.getApplicationId(),
                ApplicationPrivilegeConstants.APPLICATION_DASHBOARD_CATEGORY_CODE,
                PrivilegeNameUtils.getAppletPrivilegeName(dashboardName));
        return new EntityActionResult(ctx);
    }

    @Override
    public boolean checkAppliesTo(Entity inst) throws UnifyException {
        return true;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

}
