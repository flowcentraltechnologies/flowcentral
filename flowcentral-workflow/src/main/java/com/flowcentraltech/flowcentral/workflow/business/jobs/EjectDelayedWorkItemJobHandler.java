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

import com.flowcentraltech.flowcentral.workflow.entities.WfStepQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.JobFetchInfo;

/**
 * Eject delayed work item job handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component("ejectdelayedworkitem-jobhandler")
public class EjectDelayedWorkItemJobHandler extends AbstractWorkflowStepJobHandler {

    private static final int EJECT_CLAIM_SECONDS = 24 * 60 * 60; // 24 Hours

    private static final int EJECT_CHECK_SECONDS = 25; // 25 seconds

    @Override
    protected void process(Long wfStepId) throws UnifyException {
        workflow().ejectWorkItems(wfStepId);
    }

    @Override
    protected JobFetchInfo getJobFetchInfo() throws UnifyException {
        return JobFetchInfo.newBuilder()
                    .claimQuery(new WfStepQuery().ejectionCheckDue())
                    .claimUpdate(new Update().setNowWithOffset("ejectCheckOn", EJECT_CLAIM_SECONDS))
                    .passUpdate(new Update().setNowWithOffset("ejectCheckOn", EJECT_CHECK_SECONDS))
                    .failUpdate(new Update().setNowWithOffset("ejectCheckOn", EJECT_CHECK_SECONDS))
                    .fetchBatchSize(16)
                    .build();
    }

}
