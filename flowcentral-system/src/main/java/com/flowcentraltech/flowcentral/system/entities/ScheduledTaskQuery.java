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

package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;
import com.tcdng.unify.core.criterion.AndBuilder;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Query;

/**
 * Scheduled task query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ScheduledTaskQuery extends BaseStatusEntityQuery<ScheduledTask> {

    private static final Restriction READY_TO_RUN = new AndBuilder()
                                                                .equals("triggerCheck", false)
                                                                .equals("status", RecordStatus.ACTIVE)
                                                                .lesseq("nextExecutionOn", Query.expOfNow())
                                                                .build();
    public ScheduledTaskQuery() {
        super(ScheduledTask.class);
    }

    public ScheduledTaskQuery descriptionLike(String description) {
        return (ScheduledTaskQuery) like("description", description);
    }

    public ScheduledTaskQuery taskName(String taskName) {
        return (ScheduledTaskQuery) equals("taskName", taskName);
    }

    public ScheduledTaskQuery readyToRun() {
        return (ScheduledTaskQuery) restriction(READY_TO_RUN);
    }
}