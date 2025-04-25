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

package com.flowcentraltech.flowcentral.codegeneration.web.controllers;

import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationTaskConstants;
import com.flowcentraltech.flowcentral.codegeneration.data.CodeGenerationItem;
import com.flowcentraltech.flowcentral.common.data.TransferToRemote;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.core.data.DownloadFile;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Utilities module static files generation page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("/codegeneration/utilitiesmodulestaticfilesgeneration")
@UplBinding("web/codegeneration/upl/utilitiesmodulestaticfilesgenerationpage.upl")
public class UtilitiesModuleStaticFilesGenerationPageController
        extends AbstractCodeGenerationPageController<UtilitiesModuleStaticFilesGenerationPageBean> {

    @Configurable
    private SystemModuleService systemModuleService;

    public UtilitiesModuleStaticFilesGenerationPageController() {
        super(UtilitiesModuleStaticFilesGenerationPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String generateStaticFiles() throws UnifyException {
        UtilitiesModuleStaticFilesGenerationPageBean pageBean = getPageBean();
        CodeGenerationItem codeGenerationItem = new CodeGenerationItem(pageBean.getBasePackage());
        pageBean.setCodeGenerationItem(codeGenerationItem);
        TaskSetup taskSetup = TaskSetup
                .newBuilder(CodeGenerationTaskConstants.GENERATE_UTILITIES_MODULE_FILES_TASK_NAME)
                .setParam(CodeGenerationTaskConstants.CODEGENERATION_ITEM, pageBean.getCodeGenerationItem())
                .logMessages().build();

        final boolean isToRepository = systemModuleService.getSysParameterValue(boolean.class,
                CodeGenerationModuleSysParamConstants.ENABLE_CODEGEN_TO_REPOSITORY);
        if (isToRepository) {
            if (StringUtils
                    .isBlank(systemModuleService.getSysParameterValue(String.class,
                            CodeGenerationModuleSysParamConstants.UTILITIES_SRC_PATH))
                    || StringUtils.isBlank(systemModuleService.getSysParameterValue(String.class,
                            CodeGenerationModuleSysParamConstants.CODEGEN_TARGET_REPOSITORY))
                    || StringUtils.isBlank(systemModuleService.getSysParameterValue(String.class,
                            CodeGenerationModuleSysParamConstants.CODEGEN_TARGET_BRANCH))) {
                hintUser(MODE.ERROR, "$m{codegeneration.repository.error}");
                return noResult();
            }
        }

        return launchTaskWithMonitorBox(taskSetup, "Generate Static Application Files (Utilities)",
                isToRepository ? "/codegeneration/utilitiesmodulestaticfilesgeneration/pushToRemote"
                        : "/codegeneration/utilitiesmodulestaticfilesgeneration/downloadGeneratedFile",
                null);
    }

    @Action
    public String downloadGeneratedFile() throws UnifyException {
        UtilitiesModuleStaticFilesGenerationPageBean pageBean = getPageBean();
        CodeGenerationItem codeGenerationItem = pageBean.getCodeGenerationItem();
        DownloadFile downloadFile = new DownloadFile(MimeType.APPLICATION_OCTETSTREAM, codeGenerationItem.getFilename(),
                codeGenerationItem.getData());
        pageBean.setCodeGenerationItem(null);
        return fileDownloadResult(downloadFile, true);
    }

    @Action
    public String pushToRemote() throws UnifyException {
        UtilitiesModuleStaticFilesGenerationPageBean pageBean = getPageBean();
        CodeGenerationItem codeGenerationItem = pageBean.getCodeGenerationItem();
        final String workingPath = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.UTILITIES_SRC_PATH);
        final String repositoryName = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.CODEGEN_TARGET_REPOSITORY);
        final String branch = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.CODEGEN_TARGET_BRANCH);
        TransferToRemote transferToRemote = new TransferToRemote(
                TransferToRemote.TransferType.REPLACE_DIRECTORY_WITH_ZIP, repositoryName, branch, workingPath,
                codeGenerationItem.getFilename(), codeGenerationItem.getData());
        pageBean.setCodeGenerationItem(null);
        return executeRepositoryTransfer(transferToRemote, "Push Utility Files to Remote");
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        UtilitiesModuleStaticFilesGenerationPageBean pageBean = getPageBean();
        if (StringUtils.isBlank(pageBean.getBasePackage())) {
            String defaultBasePackage = systemModuleService.getSysParameterValue(String.class,
                    CodeGenerationModuleSysParamConstants.DEFAULT_CODEGEN_PACKAGE_BASE);
            pageBean.setBasePackage(defaultBasePackage);
        }

        pageBean.setCodeGenerationItem(null);
    }

}
