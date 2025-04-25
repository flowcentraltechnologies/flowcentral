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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.common.data.TransferToRemote;
import com.flowcentraltech.flowcentral.studio.business.data.SnapshotResultDetails;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotTaskConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotType;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
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
 * @since 4.1
 */
@Component("/studio/takesnapshot")
@UplBinding("web/studio/upl/takesnapshotpage.upl")
@ResultMappings({ @ResultMapping(name = "refresh", response = { "!refreshpanelresponse panels:$l{formPanel}" }) })
public class StudioTakeSnapshotPageController extends AbstractStudioPageController<StudioTakeSnapshotPageBean> {

    @Configurable
    private SystemModuleService systemModuleService;

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

        SnapshotResultDetails resultDetails = new SnapshotResultDetails();
        TaskSetup taskSetup = TaskSetup.newBuilder(StudioSnapshotTaskConstants.STUDIO_TAKE_SNAPSHOT_TASK_NAME)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_TYPE, StudioSnapshotType.MANUAL_SYSTEM)
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_NAME, pageBean.getSnapshotTitle())
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_MESSAGE, pageBean.getMessage())
                .setParam(StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_RESULT_DETAILS, resultDetails)
                .logMessages()
                .build();
        final boolean isToRepository = systemModuleService.getSysParameterValue(boolean.class,
                CodeGenerationModuleSysParamConstants.ENABLE_SNAPSHOT_TO_REPOSITORY);
        if (isToRepository) {
            if (StringUtils
                    .isBlank(systemModuleService.getSysParameterValue(String.class,
                            CodeGenerationModuleSysParamConstants.SNAPSHOT_SRC_PATH))
                    || StringUtils.isBlank(systemModuleService.getSysParameterValue(String.class,
                            CodeGenerationModuleSysParamConstants.SNAPSHOT_TARGET_REPOSITORY))
                    || StringUtils.isBlank(systemModuleService.getSysParameterValue(String.class,
                            CodeGenerationModuleSysParamConstants.SNAPSHOT_TARGET_BRANCH))) {
                hintUser(MODE.ERROR, "$m{codegeneration.repository.error}");
                return noResult();
            }

            pageBean.setResultDetails(resultDetails);
        }
        
        pageBean.setMessage(null);
        return launchTaskWithMonitorBox(taskSetup, "Take Studio Snapshot",
                isToRepository ? "/studio/takesnapshot/pushToRemote"
                : "/studio/snapshots/openPage", null);
    }

    @Action
    public String pushToRemote() throws UnifyException {
        StudioTakeSnapshotPageBean pageBean = getPageBean();
        SnapshotResultDetails resultDetails = pageBean.getResultDetails();
        final String workingPath = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.SNAPSHOT_SRC_PATH);
        final String repositoryName = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.SNAPSHOT_TARGET_REPOSITORY);
        final String branch = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.SNAPSHOT_TARGET_BRANCH);
        TransferToRemote transferToRemote = new TransferToRemote(
                TransferToRemote.TransferType.REPLACE_FILE_IN_DIRECTORY, repositoryName, branch, workingPath,
                resultDetails.getFileName(), resultDetails.getSnapshot());
        pageBean.setResultDetails(null);
        pageBean.setMessage(null);
        return executeRepositoryTransfer(transferToRemote, "Push Snapshot File to Remote", "/studio/snapshots/openPage",
                null);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        
        StudioTakeSnapshotPageBean pageBean = getPageBean();
        final Date now = studio().getNow();
        final String snapshotTitle = "SNAPSHOT_SYS_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(now);
        pageBean.setSnapshotTitle(snapshotTitle);

        if (pageBean.getMessage() == null) {
            final String message = resolveSessionMessage("$m{studio.takesnapshotpage.message.default}",
                    new SimpleDateFormat("EEEE MMMM d, yyyy h:mma").format(now));
            pageBean.setMessage(message);
        }
    }

}
