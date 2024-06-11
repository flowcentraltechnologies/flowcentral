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

package com.flowcentraltech.flowcentral.studio.web.controllers;

import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotTaskConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Take snapshot page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/studio/takesnapshot")
@UplBinding("web/studio/upl/takesnapshotpage.upl")
public class StudioTakeSnapshotPageController extends AbstractStudioPageController<StudioTakeSnapshotPageBean> {

    public StudioTakeSnapshotPageController() {
        super(StudioTakeSnapshotPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String takeSnapshot() throws UnifyException {
        StudioTakeSnapshotPageBean pageBean = getPageBean();
        TaskSetup taskSetup = TaskSetup.newBuilder(
                StudioSnapshotTaskConstants.STUDIO_TAKE_SNAPSHOT_TASK_NAME)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_TYPE, StudioSnapshotType.MANUAL_SYSTEM)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_NAME, pageBean.getSnapshotTitle())
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_MESSAGE, pageBean.getMessage())
                .logMessages()
                .build();
        return launchTaskWithMonitorBox(taskSetup, "Take Studio SnapshotDetails", "/studio/snapshots/openPage", null);
    }

}
