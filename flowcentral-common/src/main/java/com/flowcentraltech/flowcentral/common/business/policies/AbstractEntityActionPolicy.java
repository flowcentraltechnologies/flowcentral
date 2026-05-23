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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;

/**
 * Convenient abstract base class for entity action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractEntityActionPolicy extends AbstractFlowCentralComponent implements EntityActionPolicy {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private EnvironmentService environment;

	@Configurable
	private TaskLauncher taskLauncher;

    @Override
    public final EntityActionResult executePreAction(EntityActionContext ctx) throws UnifyException {
        if (checkAppliesTo(ctx.getReader())) {
            return doExecutePreAction(ctx);
        }
        
        return null;
    }

    @Override
    public final EntityActionResult executePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult entityActionResult = null;
        if (checkAppliesTo(ctx.getReader())) {
            entityActionResult = doExecutePostAction(ctx);
        }

        if (entityActionResult == null) {
            return new EntityActionResult(ctx);
        }

        return entityActionResult;
    }

    protected void launchTaskWithMonitorBox(TaskSetup taskSetup, String caption) throws UnifyException {
        launchTaskWithMonitorBox(taskSetup, caption, null, null);
    }

    protected void launchTaskWithMonitorBox(TaskSetup taskSetup, String caption, String onSuccessPath,
            String onFailurePath) throws UnifyException {
        TaskMonitor taskMonitor = taskLauncher.launchTask(taskSetup);
        TaskMonitorInfo taskMonitorInfo = new TaskMonitorInfo(taskMonitor, resolveSessionMessage(caption),
                onSuccessPath, onFailurePath);
        setSessionAttribute(UnifyWebSessionAttributeConstants.TASKMONITORINFO, taskMonitorInfo);
        setCommandResultMapping("showapplicationtaskmonitor");
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected final EnvironmentService environment() {
        return environment;
    }

    protected void registerPrivilege(ConfigType configType, Long applicationId, String privilegeCategoryCode,
            String privilegeCode, String privilegeDesc) throws UnifyException {
        if (!applicationPrivilegeManager.isRegisteredPrivilege(privilegeCategoryCode, privilegeCode)) {
            applicationPrivilegeManager.registerPrivilege(configType, applicationId, privilegeCategoryCode,
                    privilegeCode, privilegeDesc);
        }
    }

    protected void unregisterPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode)
            throws UnifyException {
        applicationPrivilegeManager.unregisterPrivilege(applicationId, privilegeCategoryCode, privilegeCode);
    }

    protected boolean assignPrivilegeToRole(String roleCode, String privilegeCode)
            throws UnifyException {
        return applicationPrivilegeManager.assignPrivilegeToRole(roleCode, privilegeCode);
    }

    protected abstract EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException;

    protected abstract EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException;

}
