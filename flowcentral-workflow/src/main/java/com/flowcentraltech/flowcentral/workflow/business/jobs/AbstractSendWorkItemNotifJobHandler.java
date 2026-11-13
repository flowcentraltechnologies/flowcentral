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
package com.flowcentraltech.flowcentral.workflow.business.jobs;

import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleSysParamConstants;
import com.flowcentraltech.flowcentral.workflow.entities.WfItem;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemAlertQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.JobFetchInfo;

/**
 * Abstract base class for sending work item notification job handlers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractSendWorkItemNotifJobHandler extends AbstractWorkflowJobHandler {

    private final String name;
    
    private final WorkflowAlertType type;
        
    public AbstractSendWorkItemNotifJobHandler(String name, WorkflowAlertType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public final void performWork(Long workItemId) throws UnifyException {
        logInfo("Sending {0} alert for work item [{1}]...", name, workItemId);
        final Long wfItemEventId = environment().value(Long.class, "wfItemEventId",
                new WfItemAlertQuery().id(workItemId));
        final WfItem wfItem = environment().list(new WfItemQuery().wfItemEventId(wfItemEventId));
        if (wfItem != null) {
            workflow().sendWorkItemAlert(wfItem, getNow(), type);
        }
    }

    @Override
    public final void notifyFail(Long workItemId) throws UnifyException {
        
    }

    @Override
    protected final JobFetchInfo getJobFetchInfo() throws UnifyException {
        final int batchSize = system().getSysParameterValue(int.class,
                WorkflowModuleSysParamConstants.WF_WORKITEM_ALERT_BATCH_SIZE);
        JobFetchInfo.Builder jb = JobFetchInfo.newBuilder();
        populateJobFetchInfo(jb);
        return jb.fetchBatchSize(batchSize)
                    .jobMaxAttempts(1)
                    .build();
    }

    protected abstract void populateJobFetchInfo(JobFetchInfo.Builder jb) throws UnifyException;
    
}
