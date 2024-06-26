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

import java.text.SimpleDateFormat;

import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotTaskConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Take snapshot page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/studio/takesnapshot")
@UplBinding("web/studio/upl/takesnapshotpage.upl")
@ResultMappings({ @ResultMapping(name = "refresh", response = { "!refreshpanelresponse panels:$l{formPanel}" }) })
public class StudioTakeSnapshotPageController extends AbstractStudioPageController<StudioTakeSnapshotPageBean> {

    public StudioTakeSnapshotPageController() {
        super(StudioTakeSnapshotPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String takeSnapshot() throws UnifyException {
        StudioTakeSnapshotPageBean pageBean = getPageBean();
        if (StringUtils.isBlank(pageBean.getSnapshotTitle())) {
            hintUser(MODE.ERROR, "$m{studio.takesnapshotpage.titlerequired}");
            return "refresh";
        }

        if (StringUtils.isBlank(pageBean.getMessage())) {
            hintUser(MODE.ERROR, "$m{studio.takesnapshotpage.messagerequired}");
            return "refresh";
        }

        TaskSetup taskSetup = TaskSetup.newBuilder(StudioSnapshotTaskConstants.STUDIO_TAKE_SNAPSHOT_TASK_NAME)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_TYPE, StudioSnapshotType.MANUAL_SYSTEM)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_NAME, pageBean.getSnapshotTitle())
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_MESSAGE, pageBean.getMessage()).logMessages()
                .build();
        return launchTaskWithMonitorBox(taskSetup, "Take Studio Snapshot", "/studio/snapshots/openPage", null);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        StudioTakeSnapshotPageBean pageBean = getPageBean();
        final String snapshotTitle = "SNAPSHOT_SYS_"
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(studio().getNow());
        pageBean.setSnapshotTitle(snapshotTitle);
    }

}
