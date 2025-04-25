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
package com.flowcentraltech.flowcentral.system.business.tasks;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.constants.SystemSchedTaskConstants;
import com.flowcentraltech.flowcentral.system.entities.ScheduledTaskHist;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.task.AbstractTask;
import com.tcdng.unify.core.task.TaskInput;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskStatus;
import com.tcdng.unify.core.util.QueryUtils;

/**
 * Convenient abstract base class for schedulable tasks.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSchedulableTask extends AbstractTask implements SchedulableTask {

    @Configurable
    private EnvironmentService environmentService;
    
    @Override
    public final void execute(TaskMonitor tm, TaskInput input) throws UnifyException {
        final Long scheduledTaskId = input.getParam(Long.class, SystemSchedTaskConstants.SCHEDULEDTASK_ID);
        ScheduledTaskHist scheduledTaskHist = null;
        if (QueryUtils.isValidLongCriteria(scheduledTaskId)) {
            scheduledTaskHist = new ScheduledTaskHist();
            scheduledTaskHist.setScheduledTaskId(scheduledTaskId);
            scheduledTaskHist.setStartedOn(environmentService.getNow());
            scheduledTaskHist.setTaskStatus(TaskStatus.INITIALIZED);
            environmentService.create(scheduledTaskHist);            
        }
        
        doExecute(tm, input);
        
        if (QueryUtils.isValidLongCriteria(scheduledTaskId)) {
            scheduledTaskHist.setFinishedOn(environmentService.getNow());
            if (tm.isExceptions()) {
                scheduledTaskHist.setTaskStatus(TaskStatus.FAILED);
                scheduledTaskHist.setErrorMsg(getExceptionMessage(LocaleType.APPLICATION, tm.getExceptions()[0]));
            } else if (tm.isCancelled()) {
                scheduledTaskHist.setTaskStatus(TaskStatus.CANCELED);
            } else if (tm.isNotPermitted()) {
                scheduledTaskHist.setTaskStatus(TaskStatus.ABORTED);
            } else {
                scheduledTaskHist.setTaskStatus(TaskStatus.SUCCESSFUL);
            }

            environmentService.updateByIdVersion(scheduledTaskHist);           
        }
    }

    protected abstract void doExecute(TaskMonitor tm, TaskInput input) throws UnifyException;
}
