/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.codegeneration.web.controllers;

import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Snapshot settings page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("/codegeneration/snapshotsettings")
@UplBinding("web/codegeneration/upl/snapshotsettingspage.upl")
@ResultMappings({
    @ResultMapping(name = "refresh", response = { "!refreshpanelresponse panels:$l{formPanel}" }) })
public class SnapshotSettingsPageController
        extends AbstractCodeGenerationPageController<SnapshotSettingsPageBean> {

    @Configurable
    private SystemModuleService systemModuleService;

    public SnapshotSettingsPageController() {
        super(SnapshotSettingsPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String save() throws UnifyException {
        SnapshotSettingsPageBean pageBean = getPageBean();
        systemModuleService.setSysParameterValue(CodeGenerationModuleSysParamConstants.SNAPSHOT_TARGET_REPOSITORY,
                pageBean.getRepository());
        systemModuleService.setSysParameterValue(CodeGenerationModuleSysParamConstants.SNAPSHOT_SRC_PATH,
                pageBean.getSnapshotSrc());
        systemModuleService.setSysParameterValue(CodeGenerationModuleSysParamConstants.SNAPSHOT_TARGET_BRANCH,
                pageBean.getBranch());
        systemModuleService.setSysParameterValue(CodeGenerationModuleSysParamConstants.ENABLE_SNAPSHOT_TO_REPOSITORY,
                pageBean.isGenerateToRepo());
        loadSettings();
        return "refresh";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        loadSettings();
    }

    private void loadSettings() throws UnifyException {
        SnapshotSettingsPageBean pageBean = getPageBean();
        final String repository = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.SNAPSHOT_TARGET_REPOSITORY);
        final String snapshotSrc = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.SNAPSHOT_SRC_PATH);
        final String branch = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.SNAPSHOT_TARGET_BRANCH);
        final boolean generateToRepo = systemModuleService.getSysParameterValue(boolean.class,
                CodeGenerationModuleSysParamConstants.ENABLE_SNAPSHOT_TO_REPOSITORY);

        pageBean.setRepository(repository);
        pageBean.setSnapshotSrc(snapshotSrc);
        pageBean.setBranch(branch);
        pageBean.setGenerateToRepo(generateToRepo);
    }
}
