/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.studio.business.tasks;

import com.flowcentraltech.flowcentral.studio.business.data.SnapshotResultDetails;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Schedulable;
import com.tcdng.unify.core.task.TaskInput;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Take snapshot studio task.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = "takesnapshot-studiotask", description = "Take Snapshot Studio Task")
@Schedulable(parameters = {
        @Parameter(name = "snapshotName", description = "Snapshot Name", editor = "!ui-text", type = String.class,
                mandatory = true),
        @Parameter(name = "message", description = "Message", editor = "!ui-textarea", type = String.class) })
public class TakeSnapshotStudioTask extends AbstractStudioSchedulableTask {

    @Override
    public void doExecute(TaskMonitor taskMonitor, TaskInput input) throws UnifyException {
        final String snapshotName = input.getParam(String.class, "snapshotName");
        final String message = input.getParam(String.class, "message");
        SnapshotResultDetails resultDetails = new SnapshotResultDetails();
        studio().takeStudioSnapshotTask(taskMonitor, StudioSnapshotType.AUTOMATIC_SYSTEM, snapshotName, message,
                resultDetails);
        // TODO Push to remote repository
    }

}
