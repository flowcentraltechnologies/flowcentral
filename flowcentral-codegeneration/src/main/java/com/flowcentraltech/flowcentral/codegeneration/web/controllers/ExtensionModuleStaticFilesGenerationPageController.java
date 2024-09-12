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

package com.flowcentraltech.flowcentral.codegeneration.web.controllers;

import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationTaskConstants;
import com.flowcentraltech.flowcentral.codegeneration.data.CodeGenerationItem;
import com.flowcentraltech.flowcentral.repository.constants.TransferToRemoteTaskConstants;
import com.flowcentraltech.flowcentral.repository.data.TransferToRemote;
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
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Extension module static files generation page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/codegeneration/extensionmodulestaticfilesgeneration")
@UplBinding("web/codegeneration/upl/extensionmodulestaticfilesgenerationpage.upl")
@ResultMappings({
    @ResultMapping(name = "refresh",
            response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{generationBodyPanel}" }) })
public class ExtensionModuleStaticFilesGenerationPageController
        extends AbstractCodeGenerationPageController<ExtensionModuleStaticFilesGenerationPageBean> {

    @Configurable
    private SystemModuleService systemModuleService;

    public ExtensionModuleStaticFilesGenerationPageController() {
        super(ExtensionModuleStaticFilesGenerationPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String generateStaticFiles() throws UnifyException {
        ExtensionModuleStaticFilesGenerationPageBean pageBean = getPageBean();
        CodeGenerationItem codeGenerationItem = new CodeGenerationItem(pageBean.getBasePackage(),
                pageBean.getRemoteRepoName(), pageBean.getRemoteRepoBranch());
        pageBean.setCodeGenerationItem(codeGenerationItem);
        TaskSetup taskSetup = TaskSetup
                .newBuilder(CodeGenerationTaskConstants.GENERATE_EXTENSION_MODULE_FILES_TASK_NAME)
                .setParam(CodeGenerationTaskConstants.CODEGENERATION_ITEM, codeGenerationItem)
                .logMessages().build();
        return launchTaskWithMonitorBox(taskSetup, "Generate Static Application Files (Extension)",
                codeGenerationItem.isWithRemoteRepo()
                        ? "/codegeneration/extensionmodulestaticfilesgeneration/pushToRemote"
                        : "/codegeneration/extensionmodulestaticfilesgeneration/downloadGeneratedFile",
                null);
    }

    @Action
    public String downloadGeneratedFile() throws UnifyException {
        ExtensionModuleStaticFilesGenerationPageBean pageBean = getPageBean();
        CodeGenerationItem codeGenerationItem = pageBean.getCodeGenerationItem();
        DownloadFile downloadFile = new DownloadFile(MimeType.APPLICATION_OCTETSTREAM, codeGenerationItem.getFilename(),
                codeGenerationItem.getData());
        pageBean.setCodeGenerationItem(null);
        return fileDownloadResult(downloadFile, true);
    }

    @Action
    public String pushToRemote() throws UnifyException {
        ExtensionModuleStaticFilesGenerationPageBean pageBean = getPageBean();
        final CodeGenerationItem codeGenerationItem = new CodeGenerationItem(pageBean.getBasePackage(),
                pageBean.getRemoteRepoName(), pageBean.getRemoteRepoBranch());
        final String workingPath = systemModuleService.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.EXTENSIONS_PATH);
        TransferToRemote transferToRemote = new TransferToRemote(codeGenerationItem.getRemoteRepoName(),
                codeGenerationItem.getRemoteRepoBranch(), workingPath, codeGenerationItem.getData());
        TaskSetup taskSetup = TaskSetup.newBuilder(TransferToRemoteTaskConstants.TRANSFER_TO_REMOTE_TASK_NAME)
                .setParam(TransferToRemoteTaskConstants.TRANSFER_ITEM, transferToRemote).logMessages().build();
        pageBean.setCodeGenerationItem(null);
        return launchTaskWithMonitorBox(taskSetup, "Push Extension Files to Remote");
    }

    @Action
    public String repoChange() throws UnifyException {
        return "refresh";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        ExtensionModuleStaticFilesGenerationPageBean pageBean = getPageBean();
        if (StringUtils.isBlank(pageBean.getBasePackage())) {
            String defaultBasePackage = systemModuleService.getSysParameterValue(String.class,
                    CodeGenerationModuleSysParamConstants.DEFAULT_CODEGEN_PACKAGE_BASE);
            pageBean.setBasePackage(defaultBasePackage);
        }

        pageBean.setCodeGenerationItem(null);
    }

}
