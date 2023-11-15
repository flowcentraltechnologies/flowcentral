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

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardSection;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio on create application dashboard policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiooncreatedashboard-policy")
public class StudioOnCreateDashboardPolicy extends StudioOnCreateComponentPolicy {

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        super.doExecutePreAction(ctx);
        final Dashboard dashboard = (Dashboard) ctx.getInst();
        if (DataUtils.isBlank(dashboard.getSectionList())) {
            List<DashboardSection> sectionList = new ArrayList<DashboardSection>();
            DashboardSection section = new DashboardSection();
            section.setIndex(0);
            section.setType(DashboardColumnsType.TYPE_1);
            sectionList.add(section);
            dashboard.setSectionList(sectionList);
            dashboard.setSections(1);
        }
        
        return null;
    }

}
