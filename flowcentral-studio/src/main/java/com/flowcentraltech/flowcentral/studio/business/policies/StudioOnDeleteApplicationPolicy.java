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

package com.flowcentraltech.flowcentral.studio.business.policies;

import com.flowcentraltech.flowcentral.application.constants.ApplicationDeletionTaskConstants;
import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.task.TaskSetup;

/**
 * Studio on delete application policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({ "application.application" })
@Component("studioondeleteapplication-policy")
public class StudioOnDeleteApplicationPolicy extends AbstractStudioAppletActionPolicy {

    @Override
    public boolean checkAppliesTo(Entity inst) throws UnifyException {
        return true;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        Application application = (Application) ctx.getInst();
        TaskSetup resultTaskSetup = TaskSetup.newBuilder()
                .addTask(ApplicationDeletionTaskConstants.APPLICATION_DELETION_TASK_NAME)
                .setParam(ApplicationDeletionTaskConstants.APPLICATION_NAME, application.getName())
                .logMessages()
                .build();
        EntityActionResult entityActionResult = new EntityActionResult(ctx, resultTaskSetup,
                "Application Deletion Task");
        entityActionResult.setTaskSuccessPath("/applicationstudio/onDeleteApplication");
        return entityActionResult;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        return new EntityActionResult(ctx);
    }

}
