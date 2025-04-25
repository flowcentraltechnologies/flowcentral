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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.web.widgets.ParamValueEntries;
import com.flowcentraltech.flowcentral.application.web.widgets.ParamValueEntry;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;

/**
 * Task execution applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-taskexecutionappletpanel")
@UplBinding("web/application/upl/taskexecutionappletpanel.upl")
public class TaskExecutionAppletPanel extends AbstractAppletPanel {

    @Action
    public void executeTask() throws UnifyException {
        TaskExecutionApplet applet = getTaskExecutionApplet();
        TaskSetup.Builder tsb = TaskSetup
                .newBuilder(applet.getRootAppletProp(String.class, AppletPropertyConstants.TASKEXECUTION_TASKNAME));
        ParamValueEntries paramValueEntries = applet.getParamValueEntries();
        for (ParamValueEntry entry : paramValueEntries.getEntryList()) {
            tsb.setParam(entry.getParamConfig().getParamName(), entry.getParamInput().getValue());
        }

        tsb.logMessages();
        launchTaskWithMonitorBox(tsb.build(), applet.getTaskTitle());
    }

    @Override
    protected void onReviewErrors(EntityActionResult entityActionResult) throws UnifyException {

    }

    protected TaskExecutionApplet getTaskExecutionApplet() throws UnifyException {
        return getValue(TaskExecutionApplet.class);
    }
}
