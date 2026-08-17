/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEnumeration;
import com.flowcentraltech.flowcentral.application.entities.AppEnumerationItem;
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
import com.flowcentraltech.flowcentral.codegeneration.data.DynamicModuleInfo.EnumInfo;
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
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Implementation of code generation module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
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

    @Configurable
    private CodeGenerationPlugin codeGenerationPlugin;

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
            limit = TaskExecLimit.ALLOW_SINGLE, schedulable = false)
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

                ExtensionModuleStaticFileBuilderContext moduleCtx = new ExtensionModuleStaticFileBuilderContext(
                        taskMonitor, mainCtx, moduleName, messageReplacements, false);

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
            String zipFilename = String.format("extension_%s_%s%s", filenamePrefix, smf.format(now), ".zip");

            IOUtils.close(zos);

            if (codeGenerationPlugin != null) {
                codeGenerationItem.setFilename(codeGenerationPlugin.getExtensionJarFileName());
                codeGenerationItem.setData(compileAndPackageTask(taskMonitor, baos.toByteArray(), false));
            } else {
                codeGenerationItem.setFilename(zipFilename);
                codeGenerationItem.setData(baos.toByteArray());
            }
        } finally {
            IOUtils.close(zos);
        }

        return 0;
    }

    @Taskable(name = CodeGenerationTaskConstants.GENERATE_STUDIO_SNAPSHOT_TASK_NAME,
            description = "Generate Studio CDSnapshot Task",
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

                ExtensionModuleStaticFileBuilderContext moduleCtx = new ExtensionModuleStaticFileBuilderContext(
                        taskMonitor, mainCtx, moduleName, messageReplacements, true);

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
            final String name = String.format("snapshot_%s_%s", filenamePrefix, smf.format(now));
            IOUtils.close(zos);
            return new Snapshot(getApplicationName(), getDeploymentVersion(), getAuxiliaryVersion(), name,
                    name + ".zip", baos.toByteArray());
        } finally {
            IOUtils.close(zos);
        }
    }

    private static final List<String> EXCLUDED_UTILITIES_MODULES = Collections.unmodifiableList(Arrays.asList(
            "application", "codegeneration", "collaboration", "chart", "dashboard", "organization", "security",
            "osmessaging", "integration", "notification", "report", "system", "studio", "workflow", "workspace"));

    @Taskable(name = CodeGenerationTaskConstants.GENERATE_UTILITIES_MODULE_FILES_TASK_NAME,
            description = "Generate Utilities Module Files Task",
            parameters = { @Parameter(name = CodeGenerationTaskConstants.CODEGENERATION_ITEM,
                    description = "Code Generation Item", type = CodeGenerationItem.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_SINGLE, schedulable = false)
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
            if (codeGenerationPlugin != null) {
                List<String> additional = codeGenerationPlugin.getAdditionalUtilitiesExclusionModules();
                if (!DataUtils.isBlank(additional)) {
                    moduleList.removeAll(additional);
                }
            }

            for (final String moduleName : moduleList) {
                addTaskMessage(taskMonitor, "Generating code for utilities module [{0}]", moduleName);
                ExtensionModuleStaticFileBuilderContext moduleCtx = new ExtensionModuleStaticFileBuilderContext(
                        taskMonitor, mainCtx, moduleName, Collections.emptyMap(), false);

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

                    // Generate enumeration constants
                    addTaskMessage(taskMonitor, "Generating enumeration constants classes for module [{0}]...",
                            moduleName);
                    addTaskMessage(taskMonitor, "Executing artifact generator [{0}]...",
                            "extension-application-enum-java-generator");
                    generator = (StaticModuleArtifactGenerator) getComponent(
                            "extension-application-enum-java-generator");
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

            if (codeGenerationPlugin != null) {
                codeGenerationItem.setFilename(codeGenerationPlugin.getUtilitiesJarFileName());
                codeGenerationItem.setData(compileAndPackageTask(taskMonitor, baos.toByteArray(), false));
            } else {
                codeGenerationItem.setFilename(zipFilename);
                codeGenerationItem.setData(baos.toByteArray());
            }
        } finally {
            IOUtils.close(zos);
        }

        return 0;
    }

    @Override
    protected void doInstallModuleFeatures(final InstallationContext ctx, final ModuleInstall moduleInstall)
            throws UnifyException {
        if (CodeGenerationModuleNameConstants.CODEGENERATION_MODULE_NAME
                .equals(moduleInstall.getModuleConfig().getName())) {
            if (codeGenerationPlugin != null) {
                installWorkDependencies();
            }
        }
    }

    private DynamicModuleInfo getDynamicModuleInfo(String moduleName) throws UnifyException {
        List<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();
        for (String applicationName : applicationModuleService.getApplicationNames(moduleName)) {
            Map<ComponentType, List<String>> componentNames = new LinkedHashMap<ComponentType, List<String>>();
            for (Map.Entry<ComponentType, Class<? extends BaseApplicationEntity>> entry : COMPONENTS.entrySet()) {
                List<String> names = applicationModuleService.findAppComponentNames(applicationName, entry.getValue());
                componentNames.put(entry.getKey(), names);
            }

            List<EnumInfo> enumerations = new ArrayList<EnumInfo>();
            for (Long enumId : applicationModuleService.findCustomAppComponentIdList(applicationName,
                    AppEnumeration.class)) {
                AppEnumeration appEnumeration = applicationModuleService.findAppEnumeration(enumId);
                Map<String, String> options = new LinkedHashMap<String, String>();
                for (AppEnumerationItem item : appEnumeration.getItemList()) {
                    options.put(item.getCode(), item.getLabel());
                }

                enumerations.add(new EnumInfo(appEnumeration.getName(), options));
            }

            applications.add(new ApplicationInfo(applicationName, componentNames, enumerations));
        }

        return new DynamicModuleInfo(moduleName, applications);
    }

    private void installWorkDependencies() throws UnifyException {
        logDebug("Installing code generation work dependencies...");
        try {
            final String workPath = IOUtils.buildFilename(getWorkingPath(), "work");
            final Path workRoot = Path.of(workPath);

            // Extract libraries to work library folder
            logDebug("Extract libraries to work library folder...");
            final Path libPath = Files.createDirectories(workRoot.resolve("lib"));
            CodeSource cs = CodeGenerationModuleServiceImpl.class.getProtectionDomain().getCodeSource();
            if (cs == null) {
                throw new IllegalStateException("No CodeSource - not running from a jar?");
            }

            String jarurl = cs.getLocation().toURI().toString();
            while (jarurl.startsWith("jar:")) {
                jarurl = jarurl.substring(4);
            }
            int index = jarurl.indexOf("!/");
            if (index >= 0) {
                jarurl = jarurl.substring(0, index);
            }

            if (jarurl.startsWith("file:")) {
                jarurl = jarurl.substring(5);
            }

            if (jarurl.matches("^/[A-Za-z]:/.*")) {
                jarurl = jarurl.substring(1);
            }

            final List<String> classpathParts = new ArrayList<>();
            final Path fatJar = Path.of(jarurl);
            try (JarFile jf = new JarFile(fatJar.toFile())) {
                Enumeration<JarEntry> entries = jf.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().startsWith("BOOT-INF/lib/") && entry.getName().endsWith(".jar")) {
                        Path dest = libPath.resolve(Path.of(entry.getName()).getFileName().toString());
                        try (InputStream in = jf.getInputStream(entry)) {
                            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
                        }

                        classpathParts.add(dest.toString());
                    }
                }
            }

            // Save class path information
            logDebug("Saving class path information...");
            final String classPath = String.join(File.pathSeparator, classpathParts);
            final File classPathFile = libPath.resolve("classpath.txt").toFile();
            IOUtils.writeToFile(classPathFile, classPath);
        } catch (UnifyException e) {
            throw e;
        } catch (Exception e) {
            throwOperationErrorException(e);
        }
    }

    private byte[] compileAndPackageTask(TaskMonitor taskMonitor, byte[] srcZip, boolean extension)
            throws UnifyException {
        Path deleteWorkPath = null;
        try {
            final Path workRoot = Path.of(IOUtils.buildFilename(getWorkingPath(), "work"));
            final Path actWorkPath = workRoot.resolve(System.currentTimeMillis() + "-" + ProcessHandle.current().pid());
            deleteWorkPath = actWorkPath;

            final Path libPath = Files.createDirectories(workRoot.resolve("lib"));
            final String classPath = IOUtils.readAllAsString(libPath.resolve("classpath.txt").toFile());

            // Extract source directory to working directory
            Files.createDirectories(actWorkPath);
            final Path sourcePath = actWorkPath.resolve("src/main/java");
            try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(srcZip))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    File target = actWorkPath.resolve(entry.getName()).toFile();
                    if (!target.toPath().normalize().startsWith(actWorkPath.normalize())) {
                        throw new SecurityException("Bad zip entry: " + entry.getName());
                    }

                    if (entry.isDirectory()) {
                        target.mkdirs();
                    } else {
                        target.getParentFile().mkdirs();
                        Files.copy(zis, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    zis.closeEntry();
                }
            }

            final Path classesPath = Files.createDirectories(actWorkPath.resolve("classes"));
            List<Path> sourceFiles;
            try (Stream<Path> walk = Files.walk(sourcePath)) {
                sourceFiles = walk.filter(p -> p.toString().endsWith(".java")).toList();
            }
            if (!sourceFiles.isEmpty()) {
                // Do compilation
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                if (compiler == null) {
                    throw new IllegalStateException("No system Java compiler available - run on a JDK, not a JRE");
                }

                addTaskMessage(taskMonitor, "Performing compilation...");
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
                try (StandardJavaFileManager fm = compiler.getStandardFileManager(diagnostics, null, null)) {
                    fm.setLocation(StandardLocation.CLASS_OUTPUT, List.of(classesPath.toFile()));
                    Iterable<? extends JavaFileObject> units = fm
                            .getJavaFileObjectsFromFiles(sourceFiles.stream().map(Path::toFile).toList());
                    List<String> options = List.of("-classpath", classPath, "-d", classesPath.toString(), "--release",
                            codeGenerationPlugin.getReleaseJavaVersion());
                    boolean ok = compiler.getTask(null, fm, diagnostics, options, null, units).call();
                    for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics()) {
                        addTaskMessage(taskMonitor, d.toString());
                    }

                    if (!ok) {
                        throw new RuntimeException("Compilation failed");
                    }
                }
            }

            // Create POM
            final File pomFile = classesPath.resolve("pom.xml").toFile();
            IOUtils.writeToFile(pomFile,
                    extension ? codeGenerationPlugin.getExtensionJarPOM() : codeGenerationPlugin.getUtilitiesJarPOM());

            // Copy resources
            final Path resourcesPath = actWorkPath.resolve("src/main/resources");
            if (Files.isDirectory(resourcesPath)) {
                try (Stream<Path> walk = Files.walk(resourcesPath)) {
                    for (Path path : (Iterable<Path>) walk::iterator) {
                        Path rel = resourcesPath.relativize(path);
                        Path target = classesPath.resolve(rel);
                        if (Files.isDirectory(path)) {
                            Files.createDirectories(target);
                        } else {
                            Files.createDirectories(target.getParent());
                            Files.copy(path, target, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }

            // Package JAR
            final Path targetPath = Files.createDirectories(actWorkPath.resolve("target"));
            Path outputJar = targetPath.resolve(extension ? codeGenerationPlugin.getExtensionJarFileName()
                    : codeGenerationPlugin.getUtilitiesJarFileName());
            java.util.spi.ToolProvider jarTool = java.util.spi.ToolProvider.findFirst("jar")
                    .orElseThrow(() -> new IllegalStateException("jar tool not found"));
            int code = jarTool.run(System.out, System.err, "--create", "--file", outputJar.toString(), "-C",
                    classesPath.toString(), ".");
            if (code != 0) {
                throw new RuntimeException("jar packaging failed, exit code=" + code);
            }

            addTaskMessage(taskMonitor, "Built: " + outputJar.toAbsolutePath());
            return IOUtils.readAll(outputJar.toFile());
        } catch (UnifyException e) {
            throw e;
        } catch (Exception e) {
            throwOperationErrorException(e);
        } finally {
            if (deleteWorkPath != null) {
                addTaskMessage(taskMonitor, "Performing cleanup...");
                IOUtils.deleteDirectoryAndContents(deleteWorkPath);
            }
        }

        return null;
    }

}
