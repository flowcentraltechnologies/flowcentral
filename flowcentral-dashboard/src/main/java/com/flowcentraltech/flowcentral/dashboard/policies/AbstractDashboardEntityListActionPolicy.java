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
package com.flowcentraltech.flowcentral.dashboard.policies;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.policies.AbstractEntityListActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.flowcentraltech.flowcentral.dashboard.business.DashboardModuleService;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardQuery;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for entity list action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDashboardEntityListActionPolicy extends AbstractEntityListActionPolicy {

	@Configurable
	private DashboardModuleService dashboardModuleService;

    @Override
	public EntityListActionResult executeAction(EntityListActionContext ctx, String rule) throws UnifyException {
		List<Long> idList = new ArrayList<Long>();
		for (Entity entity : ctx.getInstList()) {
			idList.add((Long) entity.getId());
		}

		if (!idList.isEmpty()) {
			doExecuteAction((DashboardQuery) new DashboardQuery().idIn(idList));
		}

		return null;
	}

	protected DashboardModuleService dashboard() {
		return dashboardModuleService;
	}

	protected abstract void doExecuteAction(DashboardQuery query) throws UnifyException;
}
