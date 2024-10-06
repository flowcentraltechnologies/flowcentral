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
package com.flowcentraltech.flowcentral.codegeneration.business;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.application.entities.ApplicationQuery;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleNameConstants;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationTaskConstants;
import com.flowcentraltech.flowcentral.codegeneration.data.CodeGenerationItem;
import com.flowcentraltech.flowcentral.codegeneration.data.DynamicModuleInfo;
import com.flowcentraltech.flowcentral.codegeneration.data.DynamicModuleInfo.ApplicationInfo;
import com.flowcentraltech.flowcentral.codegeneration.data.Snapshot;
import com.flowcentraltech.flowcentral.codegeneration.data.SnapshotMeta;
import com.flowcentraltech.flowcentral.codegeneration.generators.ExtensionModuleStaticFileBuilderContext;
import com.flowcentraltech.flowcentral.codegeneration.generators.ExtensionStaticFileBuilderContext;
import com.flowcentraltech.flowcentral.codegeneration.generators.StaticArtifactGenerator;
import com.flowcentraltech.flowcentral.codegeneration.generators.StaticModuleArtifactGenerator;
import com.flowcentraltech.flowcentral.codegeneration.util.CodeGenerationUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.CodeGenerationProvider;
import com.flowcentraltech.flowcentral.common.constants.ComponentType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
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

    private static final List<String> codegenerationAppletList = Collections
            .unmodifiableList(Arrays.asList("codegeneration.manageCodeGenerationSettings",
                    "codegeneration.generateStaticFiles", "codegeneration.generateStaticUtilitiesFiles"));

    @SuppressWarnings("serial")
    private static final Map<ComponentType, Class<? extends BaseApplicationEntity>> COMPONENTS = Collections
            .unmodifiableMap(new LinkedHashMap<ComponentType, Class<? extends BaseApplicationEntity>>()
                {
                    {
                        put(ComponentType.WIDGET, AppWidgetType.class);
                        put(ComponentType.ENTITY, AppEntity.class);
                        put(ComponentType.REFERENCE, AppRef.class);
                        put(ComponentType.APPLET, AppApplet.class);
                        put(ComponentType.CHART, Chart.class);
                        put(ComponentType.DASHBOARD, Dashboard.class);
                        put(ComponentType.NOTIFICATION_TEMPLATE, NotificationTemplate.class);
                        put(ComponentType.REPORT_CONFIGURATION, ReportConfiguration.class);
                        put(ComponentType.FORM, AppForm.class);
                        put(ComponentType.TABLE, AppTable.class);
                        put(ComponentType.WORKFLOW, Workflow.class);
                    }
                });

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Override
    public void clearDefinitionsCache() throws UnifyException {

    }

    @Override
    public List<String> getCodeGenerationApplets() throws UnifyException {
        return codegenerationAppletList;
    }

    private static final List<String> APPLICATION_ARTIFACT_GENERATORS = Collections.unmodifiableList(
            Arrays.asList("charts-xml-generator", "dashboards-xml-generator", "notification-templates-xml-generator",
                    "notification-largetexts-xml-generator", "reports-xml-generator", "workflows-xml-generator",
                    "help-sheets-xml-generator", "application-xml-generator"));

    @Taskable(name = CodeGenerationTaskConstants.GENERATE_EXTENSION_MODULE_FILES_TASK_NAME,
            description = "Generate Extension Module Files Task",
            parameters = { @Parameter(name = CodeGenerationTaskConstants.CODEGENERATION_ITEM,
                    description = "Code Generation Item", type = CodeGenerationItem.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_MULTIPLE, schedulable = false)
    public int generateExtensionModuleFilesTask(TaskMonitor taskMonitor, CodeGenerationItem codeGenerationItem)
            throws UnifyException {
        Date now = environment().getNow();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        try {
            ExtensionStaticFileBuilderContext mainCtx = new ExtensionStaticFileBuilderContext(
                    codeGenerationItem.getBasePackage(), false);
            List<String> moduleList = systemModuleService.getAllModuleNames();
            for (final String moduleName : moduleList) {
                addTaskMessage(taskMonitor, "Generating code for extension module [{0}]", moduleName);
                final String replacements = systemModuleService.getSysParameterValue(String.class,
                        CodeGenerationModuleSysParamConstants.MESSAGE_REPLACEMENT_LIST);
                Map<String, String> messageReplacements = CodeGenerationUtils.splitMessageReplacements(replacements);
                addTaskMessage(taskMonitor, "Using message replacement list [{0}]...", replacements);

                ExtensionModuleStaticFileBuilderContext moduleCtx = new ExtensionModuleStaticFileBuilderContext(mainCtx,
                        moduleName, messageReplacements, false);

                // Generate applications
                List<Application> applicationList = environment()
                        .listAll(new ApplicationQuery().moduleName(moduleName).isDevelopable());
                if (!applicationList.isEmpty()) {
                    for (Application application : applicationList) {
                        moduleCtx.nextApplication(application.getName(), application.getDescription(),
                                application.getConfigType().isCustom());
                        addTaskMessage(taskMonitor, "Generating artifacts for application [{0}]...",
                                application.getDescription());
                        for (String generatorName : APPLICATION_ARTIFACT_GENERATORS) {
                            addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...", generatorName);
                            StaticModuleArtifactGenerator generator = (StaticModuleArtifactGenerator) getComponent(
                                    generatorName);
                            generator.generate(moduleCtx, application.getName(), zos);
                        }
                    }

                    // Generate module configuration XML
                    addTaskMessage(taskMonitor, "Generating module configuration for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-xml-generator");
                    StaticModuleArtifactGenerator generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-module-xml-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate module static settings
                    addTaskMessage(taskMonitor, "Generating static settings for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-static-settings-java-generator");
                    generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-module-static-settings-java-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate entity classes
                    addTaskMessage(taskMonitor, "Generating entity classes for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-entities-java-generator");
                    generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-module-entities-java-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate messages
                    addTaskMessage(taskMonitor, "Generating messages for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-messages-generator");
                    generator = (StaticModuleArtifactGenerator) getComponent("extension-module-messages-generator");
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

    @Taskable(name = CodeGenerationTaskConstants.GENERATE_STUDIO_SNAPSHOT_TASK_NAME,
            description = "Generate Studio Snapshot Task",
            parameters = { @Parameter(name = CodeGenerationTaskConstants.CODEGENERATION_ITEM,
                    description = "Code Generation Item", type = CodeGenerationItem.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_MULTIPLE, schedulable = false)
    public int generateStudioSnapshotTask(TaskMonitor taskMonitor, CodeGenerationItem codeGenerationItem)
            throws UnifyException {
        Snapshot snapshot = generateSnapshot(taskMonitor, codeGenerationItem.getSnapshotMeta(),
                codeGenerationItem.getBasePackage());
        codeGenerationItem.setFilename(snapshot.getFilename());
        codeGenerationItem.setData(snapshot.getData());
        return 0;
    }

    @Override
    public Snapshot generateSnapshot(SnapshotMeta meta, String basePackage) throws UnifyException {
        return generateSnapshot(null, meta, basePackage);
    }

    @Override
    public Snapshot generateSnapshot(TaskMonitor taskMonitor, SnapshotMeta meta, String basePackage)
            throws UnifyException {
        Date now = environment().getNow();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        try {
            ExtensionStaticFileBuilderContext mainCtx = new ExtensionStaticFileBuilderContext(meta, basePackage, true);
            List<String> moduleList = systemModuleService.getAllModuleNames();
            for (final String moduleName : moduleList) {
                addTaskMessage(taskMonitor, "Generating code for extension module [{0}]", moduleName);
                final String replacements = systemModuleService.getSysParameterValue(String.class,
                        CodeGenerationModuleSysParamConstants.MESSAGE_REPLACEMENT_LIST);
                Map<String, String> messageReplacements = CodeGenerationUtils.splitMessageReplacements(replacements);
                addTaskMessage(taskMonitor, "Using message replacement list [{0}]...", replacements);

                ExtensionModuleStaticFileBuilderContext moduleCtx = new ExtensionModuleStaticFileBuilderContext(mainCtx,
                        moduleName, messageReplacements, true);

                // Generate applications
                List<Application> applicationList = environment()
                        .listAll(new ApplicationQuery().moduleName(moduleName).isDevelopable());
                if (!applicationList.isEmpty()) {
                    for (Application application : applicationList) {
                        moduleCtx.nextApplication(application.getName(), application.getDescription(),
                                application.getConfigType().isCustom());
                        addTaskMessage(taskMonitor, "Generating artifacts for application [{0}]...",
                                application.getDescription());
                        for (String generatorName : APPLICATION_ARTIFACT_GENERATORS) {
                            addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...", generatorName);
                            StaticModuleArtifactGenerator generator = (StaticModuleArtifactGenerator) getComponent(
                                    generatorName);
                            generator.generate(moduleCtx, application.getName(), zos);
                        }
                    }

                    // Generate module configuration XML
                    addTaskMessage(taskMonitor, "Generating module configuration for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-xml-generator");
                    StaticModuleArtifactGenerator generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-module-xml-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate messages
                    addTaskMessage(taskMonitor, "Generating messages for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-messages-generator");
                    generator = (StaticModuleArtifactGenerator) getComponent("extension-module-messages-generator");
                    generator.generate(moduleCtx, moduleName, zos);
                }
            }

            // Generate snapshot meta
            addTaskMessage(taskMonitor, "Generating snapshot meta XML...");
            addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...", "extension-snapshot-xml-generator");
            StaticArtifactGenerator generator = (StaticArtifactGenerator) getComponent(
                    "extension-snapshot-xml-generator");
            generator.generate(mainCtx, zos);

            SimpleDateFormat smf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            final String filenamePrefix = StringUtils.flatten(getApplicationCode().toLowerCase());
            final String fileName = String.format("%s_snapshot_%s", filenamePrefix, smf.format(now));
            String zipFilename = String.format("%s_snapshot_%s%s", filenamePrefix, smf.format(now), ".zip");

            IOUtils.close(zos);
            return new Snapshot(getApplicationName(), getDeploymentVersion(), getAuxiliaryVersion(), fileName,
                    zipFilename, baos.toByteArray());
        } finally {
            IOUtils.close(zos);
        }
    }

    private static final List<String> EXCLUDED_UTILITIES_MODULES = Collections
            .unmodifiableList(Arrays.asList("application", "codegeneration", "collaboration", "dashboard",
                    "integration", "notification", "report", "studio", "workflow", "workspace"));

    @Taskable(name = CodeGenerationTaskConstants.GENERATE_UTILITIES_MODULE_FILES_TASK_NAME,
            description = "Generate Utilities Module Files Task",
            parameters = { @Parameter(name = CodeGenerationTaskConstants.CODEGENERATION_ITEM,
                    description = "Code Generation Item", type = CodeGenerationItem.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_MULTIPLE, schedulable = false)
    public int generateUtilitiesModuleFilesTask(TaskMonitor taskMonitor, CodeGenerationItem codeGenerationItem)
            throws UnifyException {
        Date now = environment().getNow();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        try {
            ExtensionStaticFileBuilderContext mainCtx = new ExtensionStaticFileBuilderContext(
                    codeGenerationItem.getBasePackage(), false);
            List<String> moduleList = systemModuleService.getAllModuleNames();
            moduleList.removeAll(EXCLUDED_UTILITIES_MODULES);
            for (final String moduleName : moduleList) {
                addTaskMessage(taskMonitor, "Generating code for utilities module [{0}]", moduleName);
                ExtensionModuleStaticFileBuilderContext moduleCtx = new ExtensionModuleStaticFileBuilderContext(mainCtx,
                        moduleName, Collections.emptyMap(), false);

                // Generate applications
                List<Application> applicationList = environment()
                        .listAll(new ApplicationQuery().moduleName(moduleName));
                if (!applicationList.isEmpty()) {
                    for (Application application : applicationList) {
                        final String applicationName = application.getName();
                        List<Long> entityIdList = applicationModuleService.findAppComponentIdList(applicationName,
                                AppEntity.class, "label", null);
                        if (!DataUtils.isBlank(entityIdList)) {
                            for (Long entityId : entityIdList) {
                                AppEntity appEntity = applicationModuleService.findAppEntity(entityId);
                                moduleCtx.addEntity(ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                                        appEntity.getName()));
                            }
                        }
                    }

                    DynamicModuleInfo dynamicModuleInfo = getDynamicModuleInfo(moduleName);
                    moduleCtx.setDynamicModuleInfo(dynamicModuleInfo);

                    // Generate component name constants
                    addTaskMessage(taskMonitor, "Generating component name constants classes for module [{0}]...",
                            moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-componentnames-java-generator");
                    StaticModuleArtifactGenerator generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-module-componentnames-java-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate entity wrappers
                    addTaskMessage(taskMonitor, "Generating entity wrapper classes for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-entitywrappers-java-generator");
                    generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-module-entitywrappers-java-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate template wrappers
                    addTaskMessage(taskMonitor, "Generating template wrapper classes for module [{0}]...", moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-templatewrappers-java-generator");
                    generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-module-templatewrappers-java-generator");
                    generator.generate(moduleCtx, moduleName, zos);

                    // Generate large text wrappers
                    addTaskMessage(taskMonitor, "Generating large text wrapper classes for module [{0}]...",
                            moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-module-largetextwrappers-java-generator");
                    generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-module-largetextwrappers-java-generator");
                    generator.generate(moduleCtx, moduleName, zos);
                }
            }

            SimpleDateFormat smf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            final String filenamePrefix = StringUtils.flatten(getApplicationCode().toLowerCase());
            String zipFilename = String.format("%s_utilities_%s%s", filenamePrefix, smf.format(now), ".zip");

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
        System.out.println("@primus: moduleInstall.getModuleConfig().getName() = " + moduleInstall.getModuleConfig().getName());
    }

    private DynamicModuleInfo getDynamicModuleInfo(String moduleName) throws UnifyException {
        List<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();
        for (String applicationName : applicationModuleService.getApplicationNames(moduleName)) {
            Map<ComponentType, List<String>> componentNames = new LinkedHashMap<ComponentType, List<String>>();
            for (Map.Entry<ComponentType, Class<? extends BaseApplicationEntity>> entry : COMPONENTS.entrySet()) {
                List<String> names = applicationModuleService.findAppComponentNames(applicationName, entry.getValue());
                componentNames.put(entry.getKey(), names);
            }

            applications.add(new ApplicationInfo(applicationName, componentNames));
        }

        return new DynamicModuleInfo(moduleName, applications);
    }

}
