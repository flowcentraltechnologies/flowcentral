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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;
import com.tcdng.unify.web.ui.widget.panel.SwitchPanel;

/**
 * Convenient abstract base panel for applet panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractAppletPanel extends SwitchPanel {

    @Configurable
    private TaskLauncher taskLauncher;

    public void setTaskLauncher(TaskLauncher taskLauncher) {
        this.taskLauncher = taskLauncher;
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

    protected void showReportOptionsBox(ReportOptions reportOptions) throws UnifyException {
        setSessionAttribute(FlowCentralSessionAttributeConstants.REPORTOPTIONS, reportOptions);
        setCommandResultMapping("showapplicationreportoptions");
    }

    protected void addPanelToPushComponents(String panelName, boolean editable) throws UnifyException {
        if (editable && getApplet().isSaveHeaderFormOnTabAction()) {
            Panel formPanel = getWidgetByShortName(Panel.class, panelName);
            getRequestContextUtil().addListItem("mainform.push.components", formPanel.getId());
        }
    }

    protected AbstractApplet getApplet() throws UnifyException {
        return getValue(AbstractApplet.class);
    }
}
