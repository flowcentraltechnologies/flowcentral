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

package com.flowcentraltech.flowcentral.application.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.web.widgets.ParamValueEntries;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.task.TaskManager;

/**
 * Task execution applet.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TaskExecutionApplet extends AbstractApplet {

    private final String taskTitle;

    private final ParamValueEntries paramValueEntries;

    public TaskExecutionApplet(AppletUtilities au, TaskManager tm, String appletName) throws UnifyException {
        super(au, appletName);
        List<ParamConfig> paramConfigList = tm
                .getTaskParameters(getRootAppletProp(String.class, AppletPropertyConstants.TASKEXECUTION_TASKNAME));
        ParamValuesDef paramValuesDef = CommonInputUtils.getParamValuesDef(paramConfigList);
        paramValueEntries = new ParamValueEntries(paramValuesDef);
        taskTitle = getRootAppletDef().getLabel();
        setAltSubCaption(taskTitle);
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public ParamValueEntries getParamValueEntries() {
        return paramValueEntries;
    }

}
