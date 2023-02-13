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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.web.panels.applet.TaskExecutionApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskManager;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Task execution applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/taskexecutionapplet")
@UplBinding("web/application/upl/taskexecutionappletpage.upl")
public class TaskExecutionAppletController extends AbstractAppletController<TaskExecutionPageBean> {

    @Configurable
    private TaskManager taskManager;

    public TaskExecutionAppletController() {
        super(TaskExecutionPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        TaskExecutionPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            TaskExecutionApplet applet = new TaskExecutionApplet(au(), taskManager, getPathVariable());
            pageBean.setApplet(applet);
            setPageTitle(applet);
        }
    }

}
