/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.codegeneration.business;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.application.entities.ApplicationQuery;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleNameConstants;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationTaskConstants;
import com.flowcentraltech.flowcentral.codegeneration.data.CodeGenerationItem;
import com.flowcentraltech.flowcentral.codegeneration.generators.ExtensionModuleStaticFileBuilderContext;
import com.flowcentraltech.flowcentral.codegeneration.generators.ExtensionStaticFileBuilderContext;
import com.flowcentraltech.flowcentral.codegeneration.generators.StaticArtifactGenerator;
import com.flowcentraltech.flowcentral.codegeneration.util.CodeGenerationUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.CodeGenerationProvider;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Implementation of code generation module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(CodeGenerationModuleNameConstants.CODEGENERATION_MODULE_SERVICE)
public class CodeGenerationModuleServiceImpl extends AbstractFlowCentralService
        implements CodeGenerationModuleService, CodeGenerationProvider {

    private static final List<String> codegenerationAppletList = Collections.unmodifiableList(
            Arrays.asList("codegeneration.generateStaticFiles"));

    @Configurable
    private SystemModuleService systemModuleService;
    
    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    @Override
    public List<String> getCodeGenerationApplets() throws UnifyException {
        return codegenerationAppletList;
    }

    private static final List<String> APPLICATION_ARTIFACT_GENERATORS = Collections.unmodifiableList(
            Arrays.asList("charts-xml-generator",
                    "dashboards-xml-generator",
                    "notifications-xml-generator",
                    "reports-xml-generator",
                    "workflows-xml-generator",
                    "application-xml-generator"));

    @Taskable(name = CodeGenerationTaskConstants.GENERATE_EXTENSION_MODULE_FILES_TASK_NAME,
            description = "Generate Extension Module Files Task",
            parameters = { @Parameter(name = CodeGenerationTaskConstants.CODEGENERATION_ITEM,
                    description = "Code Generation Item", type = CodeGenerationItem.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_MULTIPLE, schedulable = false)
    public int generateExtensionModuleFilesTask(TaskMonitor taskMonitor, CodeGenerationItem codeGenerationItem)
            throws UnifyException {
//        final String moduleName = codeGenerationItem.getSourceName();
        Date now = environment().getNow();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        try {
            ExtensionStaticFileBuilderContext mainCtx = new ExtensionStaticFileBuilderContext(codeGenerationItem.getBasePackage());
            List<String> moduleList = systemModuleService.getAllModuleNames();
            for (final String moduleName: moduleList) {
                addTaskMessage(taskMonitor, "Generating code for extension module [{0}]", moduleName);
                final String replacements = systemModuleService.getSysParameterValue(String.class, CodeGenerationModuleSysParamConstants.MESSAGE_REPLACEMENT_LIST);
                Map<String, String> messageReplacements = CodeGenerationUtils.splitMessageReplacements(replacements);
                addTaskMessage(taskMonitor, "Using message replacement list [{0}]...", replacements);
                
                ExtensionModuleStaticFileBuilderContext moduleCtx = new ExtensionModuleStaticFileBuilderContext(
                        mainCtx, moduleName, messageReplacements);

                // Generate applications
                List<Application> applicationList = environment()
                        .listAll(new ApplicationQuery().moduleName(moduleName).configType(ConfigType.CUSTOM)); // TODO Include CUSTOMIZED
                if (!applicationList.isEmpty()) {
                    for (Application application : applicationList) {
                        moduleCtx.nextApplication(application.getName(), application.getDescription());
                        addTaskMessage(taskMonitor, "Generating artifacts for application [{0}]...",
                                application.getDescription());
                        for (String generatorName : APPLICATION_ARTIFACT_GENERATORS) {
                            addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...", generatorName);
                            StaticArtifactGenerator generator = (StaticArtifactGenerator) getComponent(generatorName);
                            generator.generate(moduleCtx, application.getName(), zos);
                        }
                    }

                    // Generate module configuration XML
                    addTaskMessage(taskMonitor, "Generating module configuration for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...", "extension-module-xml-generator");
                    StaticArtifactGenerator generator = (StaticArtifactGenerator) getComponent("extension-module-xml-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate module static settings
                    addTaskMessage(taskMonitor, "Generating static settings for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-static-settings-java-generator");
                    generator = (StaticArtifactGenerator) getComponent("extension-module-static-settings-java-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate entity classes
                    addTaskMessage(taskMonitor, "Generating entity classes for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-entities-java-generator");
                    generator = (StaticArtifactGenerator) getComponent("extension-module-entities-java-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate messages
                    addTaskMessage(taskMonitor, "Generating messages for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...", "extension-module-messages-generator");
                    generator = (StaticArtifactGenerator) getComponent(
                            "extension-module-messages-generator");
                    generator.generate(moduleCtx, moduleName, zos);
                }
            }            

            SimpleDateFormat smf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            final String filenamePrefix = StringUtils.flatten(getApplicationCode().toLowerCase());
            String zipFilename = String.format("%s_extension_%s%s", filenamePrefix, smf.format(now), ".zip");

            IOUtils.close(zos);
            codeGenerationItem.setFilename(zipFilename);
            codeGenerationItem.setData(baos.toByteArray());
        } finally {
            IOUtils.close(zos);
        }

        return 0;
    }

    @Override
    protected void doInstallModuleFeatures(final ModuleInstall moduleInstall) throws UnifyException {

    }

}
