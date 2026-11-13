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
import com.flowcentraltech.flowcentral.workflow.entities.WfItemAlertQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.JobFetchInfo.Builder;

/**
 * Send work item critical job handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component("sendworkitemcritical-jobhandler")
public class SendWorkItemCriticalJobHandler extends AbstractSendWorkItemNotifJobHandler {

    public SendWorkItemCriticalJobHandler() {
        super("critical", WorkflowAlertType.CRITICAL_NOTIFICATION);
    }

    @Override
    protected void populateJobFetchInfo(Builder jb) throws UnifyException {
        jb.title("Send critical alert")
            .claimQuery(new WfItemAlertQuery().criticalDue())
            .claimUpdate(new Update().set("criticalSent", true));
    }

}
