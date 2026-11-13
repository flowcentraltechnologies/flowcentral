/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.system.business.jobs;

import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.entities.ScheduledTask;
import com.flowcentraltech.flowcentral.system.entities.ScheduledTaskQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.JobFetchInfo;

/**
 * Trigger scheduled task job handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component("triggerscheduledtask-jobhandler")
public class TriggerScheduledTaskJobHandler extends AbstractSystemJobHandler {

    @Override
    public void performWork(Long scheduledTaskId) throws UnifyException {
        system().triggerScheduledTasks(scheduledTaskId);
    }

    @Override
    public void notifyFail(Long scheduledTaskId) throws UnifyException {
        environment().updateById(ScheduledTask.class, scheduledTaskId, new Update().set("triggerCheck", false));
    }

    @Override
    protected boolean ready() throws UnifyException {
         return super.ready() && system().getSysParameterValue(boolean.class,
                 SystemModuleSysParamConstants.SYSTEM_SCHEDULER_ENABLED);
    }

    @Override
    protected JobFetchInfo getJobFetchInfo() throws UnifyException {
        return JobFetchInfo.newBuilder()
                .title("Trigger scheduled task")
                .claimQuery(new ScheduledTaskQuery().readyToRun())
                .claimUpdate(new Update().set("triggerCheck", true))
                .fetchBatchSize(16)
                .jobMaxAttempts(4)
                .build();
    }

}
