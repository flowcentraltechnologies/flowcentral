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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;

/**
 * Convenient abstract base class for entity list action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityListActionPolicy extends AbstractFlowCentralComponent implements EntityListActionPolicy {

	@Configurable
	private TaskLauncher taskLauncher;
	
    @Override
    public final EntityListActionResult executeAction(EntityListActionContext ctx) throws UnifyException {
        return executeAction(ctx, null);
    }

    @Override
    public boolean isWidgetDisabled(ValueStoreReader parentReader) throws UnifyException {
        return false;
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

}
