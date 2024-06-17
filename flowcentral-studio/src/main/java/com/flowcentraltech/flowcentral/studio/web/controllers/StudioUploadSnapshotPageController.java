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

import com.flowcentraltech.flowcentral.configuration.xml.SnapshotConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotTaskConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.UploadedFile;
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
 * Upload snapshot page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/studio/uploadsnapshot")
@UplBinding("web/studio/upl/uploadsnapshotpage.upl")
@ResultMappings({
    @ResultMapping(name = "refresh", response = { "!refreshpanelresponse panels:$l{formPanel}" }) })
public class StudioUploadSnapshotPageController extends AbstractStudioPageController<StudioUploadSnapshotPageBean> {

    public StudioUploadSnapshotPageController() {
        super(StudioUploadSnapshotPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String uploadSnapshot() throws UnifyException {
        StudioUploadSnapshotPageBean pageBean = getPageBean();
        if (StringUtils.isBlank(pageBean.getSnapshotTitle())) {
            hintUser(MODE.ERROR, "$m{studio.uploadsnapshotpage.titlerequired}");
            return "refresh";
        }

        if (StringUtils.isBlank(pageBean.getMessage())) {
            hintUser(MODE.ERROR, "$m{studio.uploadsnapshotpage.messagerequired}");
            return "refresh";
        }

        UploadedFile snapshotFile = pageBean.getSnapshotFile();
        if (snapshotFile == null) {
            hintUser(MODE.ERROR, "$m{studio.uploadsnapshotpage.snapshotfilerequired}");
            return "refresh";
        }
        
        SnapshotConfig snapshotConfig = ConfigurationUtils.getSnapshotConfig(snapshotFile.getData());
        if (snapshotConfig == null) {
            hintUser(MODE.ERROR, "$m{studio.uploadsnapshotpage.snapshotfileinvalid}");
            return "refresh";
        }

        if (!getApplicationCode().equals(snapshotConfig.getApplicationCode())) {
            hintUser(MODE.ERROR, "$m{studio.uploadsnapshotpage.snapshotfileunmatching}");
            return "refresh";
        }

        snapshotConfig.setSnapshotTitle(pageBean.getSnapshotTitle());
        snapshotConfig.setSnapshotMessage(pageBean.getMessage());
        TaskSetup taskSetup = TaskSetup.newBuilder(
                StudioSnapshotTaskConstants.STUDIO_UPLOAD_SNAPSHOT_TASK_NAME)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_CONFIG, snapshotConfig)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_UPLOAD_FILE, snapshotFile).logMessages()
                .build();
        return launchTaskWithMonitorBox(taskSetup, "Upload Studio Snapshot", "/studio/snapshots/openPage", null);
    }

    
}
