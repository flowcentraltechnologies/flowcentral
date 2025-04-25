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

package com.flowcentraltech.flowcentral.studio.web.controllers;

import java.util.Date;

import com.flowcentraltech.flowcentral.application.data.SnapshotDetails;
import com.flowcentraltech.flowcentral.application.data.Snapshots;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotTaskConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.core.data.DownloadFile;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Snapshots page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("/studio/snapshots")
@UplBinding("web/studio/upl/snapshotspage.upl")
@ResultMappings({ @ResultMapping(name = "refresh",
        response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{snapshotsPanel}" }) })
public class StudioSnapshotsPageController extends AbstractStudioPageController<StudioSnapshotsPageBean> {

    public StudioSnapshotsPageController() {
        super(StudioSnapshotsPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String search() throws UnifyException {
        StudioSnapshotsPageBean pageBean = getPageBean();
        Snapshots.Builder sb = Snapshots.newBuilder();
        for (SnapshotDetails details : studio().findSnapshotDetails(pageBean.getFromDate(), pageBean.getToDate())) {
            sb.addDetails(details);
        }

        pageBean.setSnapshots(sb.build());
        return "refresh";
    }

    @Action
    public String restore() throws UnifyException {
        final int targetIndex = getRequestTarget(int.class);
        StudioSnapshotsPageBean pageBean = getPageBean();
        Snapshots snapshots = pageBean.getSnapshots();
        SnapshotDetails snapshotDetails = snapshots.getDetails(targetIndex);
        TaskSetup taskSetup = TaskSetup.newBuilder(StudioSnapshotTaskConstants.STUDIO_RESTORE_SNAPSHOT_TASK_NAME)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_DETAILS_ID,
                        snapshotDetails.getSnapshotDetailsId())
                .logMessages().build();
        return launchTaskWithMonitorBox(taskSetup, "Studio Restore from Snapshot");
    }

    @Action
    public String download() throws UnifyException {
        final int targetIndex = getRequestTarget(int.class);
        StudioSnapshotsPageBean pageBean = getPageBean();
        Snapshots snapshots = pageBean.getSnapshots();
        SnapshotDetails snapshotDetails = snapshots.getDetails(targetIndex);
        byte[] snapshot = studio().getSnapshot(snapshotDetails.getSnapshotDetailsId());
        DownloadFile downloadFile = new DownloadFile(MimeType.APPLICATION_OCTETSTREAM, snapshotDetails.getFilename(),
                snapshot);
        return fileDownloadResult(downloadFile, true);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        StudioSnapshotsPageBean pageBean = getPageBean();
        if (pageBean.getFromDate() == null) {
            final Date today = studio().getToday();
            pageBean.setFromDate(today);
            pageBean.setToDate(today);
        }

        search();
    }

}
