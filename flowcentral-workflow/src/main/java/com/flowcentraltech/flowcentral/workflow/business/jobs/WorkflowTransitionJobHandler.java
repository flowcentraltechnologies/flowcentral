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

import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleSysParamConstants;
import com.flowcentraltech.flowcentral.workflow.entities.WfTransitionQueueQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.JobFetchInfo;

/**
 * Workflow transition job handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component("workflowtransition-jobhandler")
public class WorkflowTransitionJobHandler extends AbstractWorkflowJobHandler {

    @Override
    public final void performWork(String payloadId) throws UnifyException {
        workflow().performWfTransition(payloadId);
    }

    @Override
    public final void notifyFail(String payloadId) throws UnifyException {
        
    }

    @Override
    protected final JobFetchInfo getJobFetchInfo() throws UnifyException {
        final int batchSize = system().getSysParameterValue(int.class,
                WorkflowModuleSysParamConstants.WFTRANSITION_PROCESSING_BATCH_SIZE);
        return JobFetchInfo.newBuilder()
                .claimQuery(new WfTransitionQueueQuery().unprocessed())
                .claimUpdate(new Update().setNow("processingDt"))
                .fetchBatchSize(batchSize)
                .jobMaxAttempts(1)
                .build();
    }
    
}
