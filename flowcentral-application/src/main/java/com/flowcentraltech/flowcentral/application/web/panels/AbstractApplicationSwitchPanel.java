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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralResultMappingConstants;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;
import com.tcdng.unify.web.ui.widget.data.Popup;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;
import com.tcdng.unify.web.ui.widget.panel.SwitchPanel;

/**
 * Convenient base class for application switch panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractApplicationSwitchPanel extends SwitchPanel {

    @Configurable
    private AppletUtilities appletUtilities;

    protected final TaskLauncher taskLauncher() {
        return appletUtilities.taskLauncher();
    }
    
    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final SystemModuleService system() {
        return appletUtilities.system();
    }

    protected final CollaborationProvider collaborationProvider() {
        return appletUtilities.collaborationProvider();
    }   
    
    protected final ApplicationPrivilegeManager applicationPrivilegeManager() {
        return appletUtilities.applicationPrivilegeManager();
    }
    
    protected final AppletUtilities au() {
        return appletUtilities;
    }

    protected void fireEntityActionResultTask(EntityActionResult entityActionResult) throws UnifyException {
        final String successPath = entityActionResult.getTaskSuccessPath() != null
                ? entityActionResult.getTaskSuccessPath()
                : getActionFullPath("/closePage");
        final String failurePath = entityActionResult.getTaskFailurePath() != null
                ? entityActionResult.getTaskFailurePath()
                : getActionFullPath("/content");
        launchTaskWithMonitorBox(entityActionResult.getResultTaskSetup(), entityActionResult.getResultTaskCaption(),
                successPath, failurePath);
    }

    protected void launchTaskWithMonitorBox(TaskSetup taskSetup, String caption) throws UnifyException {
        launchTaskWithMonitorBox(taskSetup, caption, null, null);
    }

    protected void launchTaskWithMonitorBox(TaskSetup taskSetup, String caption, String onSuccessPath,
            String onFailurePath) throws UnifyException {
        TaskMonitor taskMonitor = taskLauncher().launchTask(taskSetup);
        TaskMonitorInfo taskMonitorInfo = new TaskMonitorInfo(taskMonitor, resolveSessionMessage(caption),
                onSuccessPath, onFailurePath);
        setSessionAttribute(UnifyWebSessionAttributeConstants.TASKMONITORINFO, taskMonitorInfo);
        setCommandResultMapping("showapplicationtaskmonitor");
    }

    protected void showReportOptionsBox(ReportOptions reportOptions) throws UnifyException {
        commandShowPopup(new Popup(FlowCentralResultMappingConstants.SHOW_APPLICATION_REPORT_OPTIONS, reportOptions));
    }

}
