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
package com.flowcentraltech.flowcentral.workflow.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractEntityListActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for workflow entity list action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractWfEntityListActionPolicy extends AbstractEntityListActionPolicy {

    @Configurable
    private EnvironmentService environmentService;

    @Configurable
    private WorkflowModuleService workflowModuleService;

    @Override
    public EntityListActionResult executeAction(EntityListActionContext ctx, String rule) throws UnifyException {
        Object result = doExecuteAction(ctx, ctx.getInstList(), rule);
        ctx.setResult(result);
        return new EntityListActionResult(ctx);
    }

    protected EnvironmentService environment() {
        return environmentService;
    }

    protected WorkflowModuleService workflow() {
        return workflowModuleService;
    }

    protected abstract Object doExecuteAction(EntityListActionContext ctx, List<? extends Entity> items, String rule)
            throws UnifyException;
}
