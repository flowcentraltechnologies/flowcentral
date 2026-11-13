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
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.JobFetchInfo;

/**
 * Send work item critical job handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component("sendworkitemcritical-jobhandler")
public class SendWorkItemCriticalJobHandler extends AbstractWorkflowJobHandler {

    @Override
    public void performWork(Long workItemId) throws UnifyException {
        logInfo("Sending critical alert for work item [{0}]...", workItemId);
        final Long wfItemEventId = environment().value(Long.class, "wfItemEventId",
                new WfItemAlertQuery().id(workItemId));
        final WfItem wfItem = environment().list(new WfItemQuery().wfItemEventId(wfItemEventId));
        if (wfItem != null) {
            workflow().sendWorkItemAlert(wfItem, getNow(), WorkflowAlertType.CRITICAL_NOTIFICATION);
        }
    }

    @Override
    public void notifyFail(Long workItemId) throws UnifyException {
        
    }

    @Override
    protected JobFetchInfo getJobFetchInfo() throws UnifyException {
        final int batchSize = system().getSysParameterValue(int.class,
                WorkflowModuleSysParamConstants.WF_WORKITEM_ALERT_BATCH_SIZE);
        return JobFetchInfo.newBuilder()
                    .title("Send critical alert")
                    .claimQuery(new WfItemAlertQuery().reminderDue())
                    .claimUpdate(new Update().set("criticalSent", true))
                    .fetchBatchSize(batchSize)
                    .jobMaxAttempts(1)
                    .build();
    }

}
