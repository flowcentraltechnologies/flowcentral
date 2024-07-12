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
package com.flowcentraltech.flowcentral.studio.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.SnapshotDetails;
import com.flowcentraltech.flowcentral.application.data.StandardAppletDef;
import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.util.ApplicationCollaborationUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.codegeneration.business.CodeGenerationModuleService;
import com.flowcentraltech.flowcentral.codegeneration.constants.CodeGenerationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.codegeneration.data.Snapshot;
import com.flowcentraltech.flowcentral.codegeneration.data.SnapshotMeta;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.StudioProvider;
import com.flowcentraltech.flowcentral.common.business.SynchronizableEnvironmentDelegate;
import com.flowcentraltech.flowcentral.common.business.SystemRestoreService;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationRestore;
import com.flowcentraltech.flowcentral.configuration.data.Messages;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.configuration.data.ModuleRestore;
import com.flowcentraltech.flowcentral.configuration.data.NotifLargeTextRestore;
import com.flowcentraltech.flowcentral.configuration.data.NotifTemplateRestore;
import com.flowcentraltech.flowcentral.configuration.data.ReportRestore;
import com.flowcentraltech.flowcentral.configuration.data.SystemRestore;
import com.flowcentraltech.flowcentral.configuration.data.WorkflowRestore;
import com.flowcentraltech.flowcentral.configuration.data.WorkflowWizardRestore;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifLargeTextConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppWorkflowConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppWorkflowWizardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ModuleAppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ModuleConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifLargeTextConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SnapshotConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfWizardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.studio.business.data.DelegateSynchronizationItem;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppletPropertyConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioDelegateSynchronizationTaskConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioFeatureConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioModuleNameConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioModuleSysParamConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotTaskConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotType;
import com.flowcentraltech.flowcentral.studio.entities.StudioSnapshot;
import com.flowcentraltech.flowcentral.studio.entities.StudioSnapshotDetails;
import com.flowcentraltech.flowcentral.studio.entities.StudioSnapshotDetailsQuery;
import com.flowcentraltech.flowcentral.studio.entities.StudioSnapshotQuery;
import com.flowcentraltech.flowcentral.studio.util.StudioNameUtils;
import com.flowcentraltech.flowcentral.studio.util.StudioNameUtils.StudioAppletNameParts;
import com.flowcentraltech.flowcentral.studio.web.util.StudioWidgetWriterUtils;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of studio module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(StudioModuleNameConstants.STUDIO_MODULE_SERVICE)
public class StudioModuleServiceImpl extends AbstractFlowCentralService implements StudioModuleService, StudioProvider {

    static {
        ApplicationCollaborationUtils.registerCollaborationType(Chart.class, CollaborationType.CHART);
        ApplicationCollaborationUtils.registerCollaborationType(Dashboard.class, CollaborationType.DASHBOARD);
        ApplicationCollaborationUtils.registerCollaborationType(NotificationTemplate.class,
                CollaborationType.NOTIFICATION_TEMPLATE);
        ApplicationCollaborationUtils.registerCollaborationType(ReportConfiguration.class,
                CollaborationType.REPORT_CONFIGURATION);
        ApplicationCollaborationUtils.registerCollaborationType(Workflow.class, CollaborationType.WORKFLOW);
    }

    private final AppFilter entityFormUpdateCondition = new AppFilter("IN]0]configType]M|C|Z]\r\n");

    private final AppFilter entityFormDeleteCondition = new AppFilter("EQ]0]configType]C]\r\n");

    @Configurable
    private AppletUtilities appletUtilities;

    @Configurable
    private CodeGenerationModuleService codeGenerationModuleService;

    @Configurable
    private SystemRestoreService systemRestoreService;

    private final FactoryMap<String, AppletDef> appletDefMap;

    public StudioModuleServiceImpl() {
        this.appletDefMap = new FactoryMap<String, AppletDef>()
            {
                @Override
                protected AppletDef create(String appletName, Object... arg1) throws Exception {
                    AppletDef appletDef = null;
                    StudioAppletNameParts np = StudioNameUtils.getStudioAppletNameParts(appletName);
                    final StudioAppComponentType type = np.getType();
                    final boolean descriptiveButtons = appletUtilities.system().getSysParameterValue(boolean.class,
                            SystemModuleSysParamConstants.SYSTEM_DESCRIPTIVE_BUTTONS_ENABLED);
                    switch (type.appletType()) {
                        case MANAGE_ENTITYLIST:
                        case MANAGE_ENTITYLIST_ASSIGN:
                        case MANAGE_ENTITYLIST_SINGLEFORM:
                        case HEADLESS_TABS:
                        case MANAGE_PROPERTYLIST:
                        case CREATE_ENTITY:
                        case CREATE_ENTITY_SINGLEFORM:
                        case LISTING:
                        case TASK_EXECUTION:
                        case DATA_IMPORT:
                        case PATH_WINDOW:
                        case PATH_PAGE:
                        case REVIEW_WORKITEMS:
                        case REVIEW_SINGLEFORMWORKITEMS:
                        case REVIEW_WIZARDWORKITEMS:
                        case FACADE:
                        case FACADE_MULTIPLE:
                            break;
                        case STUDIO_FC_COMPONENT: {
                            String name = null;
                            String description = null;
                            String label = null;

                            if (np.isNullInstId()) {
                                String caption = resolveSessionMessage(type.caption());
                                description = getSessionMessage("studio.menu.description.new", caption);
                                label = getSessionMessage("studio.menu.label.new", caption);
                            } else {
                                name = environment().listValue(String.class, type.componentType(), np.getInstId(),
                                        "name");
                                description = environment().listValue(String.class, type.componentType(),
                                        np.getInstId(), "description");
                                label = getSessionMessage(type.labelKey(), description);
                            }

                            final String form = ApplicationNameUtils.ensureLongNameReference(
                                    StudioModuleNameConstants.STUDIO_APPLICATION_NAME, type.form());
                            final String entity = appletUtilities.application().getFormDef(form).getEntityDef()
                                    .getLongName();
                            final String assignDescField = null;
                            final String pseudoDeleteField = null;
                            StandardAppletDef.Builder adb = StandardAppletDef.newBuilder(type.appletType(), entity,
                                    label, type.icon(), assignDescField, pseudoDeleteField, 0, true, false, false,
                                    descriptiveButtons, appletName, description, np.getInstId(), 0L);
                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM, form);
                            adb.addPropDef(StudioAppletPropertyConstants.ENTITY_FORM, form);
                            adb.addPropDef(StudioAppletPropertyConstants.ENTITY_TYPE, type.code());
                            adb.addPropDef(StudioAppletPropertyConstants.ENTITY_INST_ID,
                                    String.valueOf(np.getInstId()));
                            if (type.isSupportsNew()) {
                                adb.addPropDef(AppletPropertyConstants.CREATE_FORM_SAVE, "true");
                                adb.addPropDef(AppletPropertyConstants.CREATE_FORM_NEW_POLICY, type.createPolicy());
                            }

                            if (type.isSupportsSaveAs()) {
                                adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_SAVEAS, "true");
                                adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_SAVEAS_POLICY,
                                        type.createPolicy());
                            }

                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_UPDATE, "true");
                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY, type.updatePolicy());
                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION,
                                    "studioentity-updatecondition");
                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_DELETE, "true");
                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_DELETE_POLICY, type.deletePolicy());
                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_DELETE_CONDITION,
                                    "studioentity-deletecondition");

                            FilterDef filterDef = InputWidgetUtils.getFilterDef(appletUtilities,
                                    "studioentity-updatecondition", "", null, entityFormUpdateCondition);
                            adb.addFilterDef(new AppletFilterDef(filterDef, null, null, null));
                            filterDef = InputWidgetUtils.getFilterDef(appletUtilities, "studioentity-deletecondition",
                                    "", null, entityFormDeleteCondition);
                            adb.addFilterDef(new AppletFilterDef(filterDef, null, null, null));

                            adb.openPath(ApplicationPageUtils.constructAppletOpenPagePath(type.appletPath(), appletName)
                                    .getOpenPath());
                            adb.originApplicationName(np.getApplicationName());
                            adb.originName(name);
                            appletDef = adb.build();
                        }
                            break;
                        default:
                            break;

                    }

                    return appletDef;
                }

            };
    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {
        logDebug("Clearing definitions cache...");
        appletDefMap.clear();
        logDebug("Definitions cache clearing successfully completed.");
    }

    @Override
    public boolean isInstallDefaultDeveloperRoles() throws UnifyException {
        return getContainerSetting(boolean.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_INSTALL_DEVELOPER_ROLES, true);
    }

    @Override
    public List<SnapshotDetails> findSnapshotDetails(Date fromDate, Date toDate) throws UnifyException {
        final int limit = appletUtilities.system().getSysParameterValue(int.class,
                StudioModuleSysParamConstants.SNAPSHOT_DETAILS_LIMIT);
        List<SnapshotDetails> details = new ArrayList<SnapshotDetails>();
        List<StudioSnapshotDetails> studioSnapshotDetails = environment().listAll(new StudioSnapshotDetailsQuery()
                .createdBetween(fromDate, toDate).addOrder(OrderType.DESCENDING, "createDt").setLimit(limit));
        for (StudioSnapshotDetails _studioSnapshotDetails : studioSnapshotDetails) {
            SnapshotDetails snapshotDetails = new SnapshotDetails(_studioSnapshotDetails.getId(),
                    _studioSnapshotDetails.getSnapshotTypeDesc(), _studioSnapshotDetails.getSnapshotName(),
                    _studioSnapshotDetails.getFileName(), _studioSnapshotDetails.getMessage(),
                    _studioSnapshotDetails.getCreateDt(), _studioSnapshotDetails.getCreatedBy());
            details.add(snapshotDetails);
        }

        return details;
    }

    @Override
    public byte[] getSnapshot(Long snapshotDetailsId) throws UnifyException {
        return environment().value(byte[].class, "snapshot",
                new StudioSnapshotQuery().snapshotDetailsId(snapshotDetailsId));
    }

    @Override
    public AppletDef getAppletDef(String appletName) throws UnifyException {
        return appletDefMap.get(appletName);
    }

    @Override
    public List<AppletDef> findAppletDefs(String applicationName, StudioAppComponentType type, String filter)
            throws UnifyException {
        List<AppletDef> appletDefList = null;
        List<Long> instIdList = type.isShowClassified()
                ? appletUtilities.application().findAppComponentIdList(applicationName, type.componentType(),
                        "description", filter)
                : appletUtilities.application().findNonClassifiedAppComponentIdList(applicationName,
                        type.componentType(), "description", filter);
        if (!instIdList.isEmpty()) {
            appletDefList = new ArrayList<AppletDef>();
            for (Long instId : instIdList) {
                appletDefList.add(appletDefMap.get(StudioNameUtils.getStudioAppletName(applicationName, type, instId)));
            }

            DataUtils.sortAscending(appletDefList, AppletDef.class, "label");
        }

        if (type.isSupportsNew() && !StringUtils.isBlank(applicationName)
                && appletUtilities.application().isApplicationDevelopable(applicationName)) {
            if (appletDefList == null) {
                appletDefList = new ArrayList<AppletDef>();
            }

            appletDefList.add(0, appletDefMap.get(StudioNameUtils.getStudioAppletName(applicationName, type, 0L)));
        }

        return appletDefList != null ? appletDefList : Collections.emptyList();
    }

    @Taskable(name = StudioDelegateSynchronizationTaskConstants.DELEGATE_CREATE_SYNCHRONIZATION_TASK_NAME,
            description = "Delegate Create Synchronization Task",
            parameters = { @Parameter(name = StudioDelegateSynchronizationTaskConstants.DELEGATE_SYNCHRONIZATION_ITEM,
                    description = "Delegate Synchronization Item", type = DelegateSynchronizationItem.class,
                    mandatory = true) },
            limit = TaskExecLimit.ALLOW_SINGLE, schedulable = false)
    public int delegateCreateSynchronizationTask(TaskMonitor taskMonitor, DelegateSynchronizationItem delegateSyncItem)
            throws UnifyException {
        SynchronizableEnvironmentDelegate environmentDelegate = getComponent(SynchronizableEnvironmentDelegate.class,
                delegateSyncItem.getDelegate());
        environmentDelegate.delegateCreateSynchronization(taskMonitor);
        return 0;
    }

    @Taskable(name = StudioDelegateSynchronizationTaskConstants.DELEGATE_UPDATE_SYNCHRONIZATION_TASK_NAME,
            description = "Delegate Update Synchronization Task",
            parameters = { @Parameter(name = StudioDelegateSynchronizationTaskConstants.DELEGATE_SYNCHRONIZATION_ITEM,
                    description = "Delegate Synchronization Item", type = DelegateSynchronizationItem.class,
                    mandatory = true) },
            limit = TaskExecLimit.ALLOW_SINGLE, schedulable = false)
    public int delegateUpdateSynchronizationTask(TaskMonitor taskMonitor, DelegateSynchronizationItem delegateSyncItem)
            throws UnifyException {
        SynchronizableEnvironmentDelegate environmentDelegate = getComponent(SynchronizableEnvironmentDelegate.class,
                delegateSyncItem.getDelegate());
        environmentDelegate.delegateUpdateSynchronization(taskMonitor);
        return 0;
    }

    @Taskable(name = StudioSnapshotTaskConstants.STUDIO_TAKE_SNAPSHOT_TASK_NAME,
            description = "Studio Take Snapshot Task",
            parameters = {
                    @Parameter(name = StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_TYPE, description = "Snapshot Type",
                            type = StudioSnapshotType.class),
                    @Parameter(name = StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_NAME, description = "Snapshot Name",
                            type = String.class, mandatory = true),
                    @Parameter(name = StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_MESSAGE, description = "Message",
                            type = String.class) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    @Override
    public int takeStudioSnapshotTask(TaskMonitor taskMonitor, StudioSnapshotType snapshotType, String snapshotName,
            String message) throws UnifyException {
        logDebug(taskMonitor, "Taking studio snapshot [{0}]...", snapshotName);
        final String basePackage = appletUtilities.getSysParameterValue(String.class,
                CodeGenerationModuleSysParamConstants.DEFAULT_CODEGEN_PACKAGE_BASE);
        Snapshot snapshot = codeGenerationModuleService.generateSnapshot(taskMonitor,
                new SnapshotMeta(snapshotType.toString(), snapshotName, message), basePackage);
        StudioSnapshotDetails studioSnapshotDetails = new StudioSnapshotDetails();
        studioSnapshotDetails.setSnapshotType(snapshotType);
        studioSnapshotDetails.setSnapshotName(snapshotName);
        studioSnapshotDetails.setFileName(snapshot.getFilename());
        studioSnapshotDetails.setMessage(message);
        Long studioSnapshotDetailsId = (Long) environment().create(studioSnapshotDetails);

        StudioSnapshot studioSnapshot = new StudioSnapshot();
        studioSnapshot.setSnapshotDetailsId(studioSnapshotDetailsId);
        studioSnapshot.setSnapshot(snapshot.getData());
        environment().create(studioSnapshot);
        logDebug(taskMonitor, "Snapshot successfully taken.");
        return 0;
    }

    @Taskable(name = StudioSnapshotTaskConstants.STUDIO_UPLOAD_SNAPSHOT_TASK_NAME,
            description = "Studio Upload Snapshot Task",
            parameters = {
                    @Parameter(name = StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_CONFIG,
                            description = "Snapshot Configuration", type = SnapshotConfig.class),
                    @Parameter(name = StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_UPLOAD_FILE,
                            description = "Snapshot File", type = UploadedFile.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_MULTIPLE)
    public int uploadStudioSnapshotTask(TaskMonitor taskMonitor, SnapshotConfig snapshotConfig,
            UploadedFile snapshotUploadFile) throws UnifyException {
        logDebug(taskMonitor, "Uploading studio snapshot [{0}]...", snapshotUploadFile.getFilename());
        logDebug(taskMonitor, "Validating uploaded snapshot file...");
        if (snapshotConfig == null) {
            snapshotConfig = ConfigurationUtils.getSnapshotConfig(snapshotUploadFile.getData());
            if (snapshotConfig == null) {
                throwOperationErrorException(new IllegalArgumentException("Invalid snapshot file."));
            }

            if (!getApplicationCode().equals(snapshotConfig.getApplicationCode())) {
                throwOperationErrorException(new IllegalArgumentException("Snapshot application is unmatching."));
            }
        }

        StudioSnapshotDetails studioSnapshotDetails = new StudioSnapshotDetails();
        studioSnapshotDetails.setSnapshotType(StudioSnapshotType.MANUAL_UPLOAD);
        studioSnapshotDetails.setSnapshotName(StringUtils.isBlank(snapshotConfig.getSnapshotTitle()) ? "Untitled"
                : snapshotConfig.getSnapshotTitle());
        studioSnapshotDetails.setMessage(snapshotConfig.getSnapshotMessage());
        studioSnapshotDetails.setFileName(snapshotUploadFile.getFilename());
        Long studioSnapshotDetailsId = (Long) environment().create(studioSnapshotDetails);

        StudioSnapshot studioSnapshot = new StudioSnapshot();
        studioSnapshot.setSnapshotDetailsId(studioSnapshotDetailsId);
        studioSnapshot.setSnapshot(snapshotUploadFile.getData());
        environment().create(studioSnapshot);
        logDebug(taskMonitor, "Snapshot file [{0}] upload successfully completed.", snapshotUploadFile.getFilename());
        return 0;
    }

    private static final String CONFIG_FOLDER = "config/";

    private static final String XML_SUFFIX = ".xml";

    @Taskable(name = StudioSnapshotTaskConstants.STUDIO_RESTORE_SNAPSHOT_TASK_NAME,
            description = "Studio Restore from Snapshot Task",
            parameters = { @Parameter(name = StudioSnapshotTaskConstants.STUDIO_SNAPSHOT_DETAILS_ID,
                    description = "Snapshot Details ID", type = Long.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_SINGLE)
    public int restoreStudioSnapshotTask(TaskMonitor taskMonitor, Long snapshotDetailsId) throws UnifyException {
        logDebug(taskMonitor, "Restoring studio from snapshot...");
        File tempFile = null;
        FileOutputStream fos = null;
        ZipFile zipFile = null;
        try {
            byte[] snapshot = getSnapshot(snapshotDetailsId);
            tempFile = File.createTempFile("flowsnapshot", ".zip");
            fos = new FileOutputStream(tempFile);
            IOUtils.writeAll(fos, snapshot);
            IOUtils.close(fos);

            zipFile = new ZipFile(tempFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            logDebug(taskMonitor, "Reading studio snapshot...");
            // Get entries mapped by name
            List<String> _moduleXmlFiles = new ArrayList<String>();
            Map<String, ZipEntry> _entries = new LinkedHashMap<String, ZipEntry>();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                final String name = entry.getName();
                if (name.length() > CONFIG_FOLDER.length() && name.startsWith(CONFIG_FOLDER)
                        && name.endsWith(XML_SUFFIX)) {
                    _moduleXmlFiles.add(name);
                }

                _entries.put(name, entry);
            }

            // Load module restores
            List<ModuleRestore> moduleRestoreList = new ArrayList<ModuleRestore>();
            for (String moduleXmlFile : _moduleXmlFiles) {
                ModuleRestore moduleRestore = loadModuleRestoreFromZip(taskMonitor, moduleXmlFile, zipFile, _entries);
                moduleRestoreList.add(moduleRestore);
                logDebug(taskMonitor, "Loaded module [{0}] from snapshot...", moduleXmlFile);
            }

            // Perform system restore
            SystemRestore systemRestore = new SystemRestore(moduleRestoreList);
            systemRestoreService.restoreSystem(taskMonitor, systemRestore);
        } catch (UnifyException e) {
            throw e;
        } catch (Exception e) {
            throwOperationErrorException(e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    logSevere(e);
                }
            }

            if (fos != null) {
                IOUtils.close(fos);
            }

            if (tempFile != null) {
                tempFile.delete();
            }
        }
        return 0;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {
        installStudioFeatures(moduleInstall);
    }

    private void installStudioFeatures(final ModuleInstall moduleInstall) throws UnifyException {
        if (StudioModuleNameConstants.STUDIO_MODULE_NAME.equals(moduleInstall.getModuleConfig().getName())) {
            final Long applicationId = appletUtilities.application().getApplicationId("studio");
            appletUtilities.applicationPrivilegeManager().registerPrivilege(ConfigType.STATIC, applicationId,
                    ApplicationPrivilegeConstants.APPLICATION_FEATURE_CATEGORY_CODE,
                    PrivilegeNameUtils.getFeaturePrivilegeName(StudioFeatureConstants.RESTORE_SNAPSHOT),
                    resolveApplicationMessage("$m{studio.privilege.restoresnapshot}"));
            appletUtilities.applicationPrivilegeManager().registerPrivilege(ConfigType.STATIC, applicationId,
                    ApplicationPrivilegeConstants.APPLICATION_FEATURE_CATEGORY_CODE,
                    PrivilegeNameUtils.getFeaturePrivilegeName(StudioFeatureConstants.DOWNLOAD_SNAPSHOT),
                    resolveApplicationMessage("$m{studio.privilege.downloadsnapshot}"));
        }
    }

    private ModuleRestore loadModuleRestoreFromZip(TaskMonitor tm, String moduleXmlFile, ZipFile zipFile,
            Map<String, ZipEntry> _entries) throws UnifyException {
        ModuleRestore moduleRestore = null;
        InputStream inputStream = null;
        try {
            logDebug(tm, "Loading module feature definitions from [{0}]...", moduleXmlFile);
            inputStream = zipFile.getInputStream(_entries.get(moduleXmlFile));
            ModuleConfig moduleConfig = ConfigurationUtils.readConfig(ModuleConfig.class, inputStream);

            List<ApplicationRestore> applicationList = new ArrayList<ApplicationRestore>();
            if (moduleConfig.getModuleAppsConfig() != null
                    && !DataUtils.isBlank(moduleConfig.getModuleAppsConfig().getModuleAppList())) {
                for (ModuleAppConfig moduleAppConfig : moduleConfig.getModuleAppsConfig().getModuleAppList()) {
                    ApplicationRestore applicationRestore = loadApplicationRestoreFromZip(tm,
                            moduleAppConfig.getConfigFile(), zipFile, _entries);
                    applicationList.add(applicationRestore);
                }
            }

            Messages messages = loadModuleMessagesFromZip(tm, moduleConfig.getName(), zipFile, _entries);
            moduleRestore = new ModuleRestore(tm, moduleConfig, applicationList, messages);
            logDebug(tm, "Loaded module feature definitions from [{0}] successfully.", moduleXmlFile);
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
        }

        return moduleRestore;
    }

    private Messages loadModuleMessagesFromZip(TaskMonitor tm, String moduleName, ZipFile zipFile,
            Map<String, ZipEntry> _entries) throws UnifyException {
        Messages messages = Messages.emptyMessages();
        final String propertiesFile = "resources/extension-" + moduleName.toLowerCase() + "-messages.properties";
        InputStream inputStream = null;
        try {
            logDebug(tm, "Loading module messages from [{0}]...", propertiesFile);
            ZipEntry entry = _entries.get(propertiesFile);
            if (entry != null) {
                inputStream = zipFile.getInputStream(entry);
                Properties properties = new Properties();
                properties.load(inputStream);
                messages = new Messages(properties);
                logDebug(tm, "Loaded module messages from [{0}] successfully.", propertiesFile);
            } else {
                logDebug(tm, "No module messages loaded from [{0}].", propertiesFile);
            }
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
        }

        return messages;
    }

    private ApplicationRestore loadApplicationRestoreFromZip(TaskMonitor tm, String configFile, ZipFile zipFile,
            Map<String, ZipEntry> _entries) throws UnifyException {
        ApplicationRestore applicationRestore = null;
        InputStream inputStream = null;
        try {
            logDebug(tm, "Loading application definition from [{0}]...", configFile);
            inputStream = zipFile.getInputStream(_entries.get(configFile));
            AppConfig appConfig = ConfigurationUtils.readConfig(AppConfig.class, inputStream);

            List<ReportRestore> reportList = new ArrayList<ReportRestore>();
            if (appConfig.getReportsConfig() != null
                    && !DataUtils.isBlank(appConfig.getReportsConfig().getReportList())) {
                for (AppReportConfig appReportConfig : appConfig.getReportsConfig().getReportList()) {
                    ReportRestore reportRestore = loadReportRestoreFromZip(tm, appReportConfig.getConfigFile(), zipFile,
                            _entries);
                    reportList.add(reportRestore);
                }
            }

            List<NotifTemplateRestore> notifTemplateList = new ArrayList<NotifTemplateRestore>();
            if (appConfig.getNotifTemplatesConfig() != null
                    && !DataUtils.isBlank(appConfig.getNotifTemplatesConfig().getNotifTemplateList())) {
                for (AppNotifTemplateConfig appNotifTemplateConfig : appConfig.getNotifTemplatesConfig()
                        .getNotifTemplateList()) {
                    NotifTemplateRestore notifTemplateRestore = loadNotifTemplateRestoreFromZip(tm,
                            appNotifTemplateConfig.getConfigFile(), zipFile, _entries);
                    notifTemplateList.add(notifTemplateRestore);
                }
            }

            List<NotifLargeTextRestore> notifLargeTextList = new ArrayList<NotifLargeTextRestore>();
            if (appConfig.getNotifLargeTextsConfig() != null
                    && !DataUtils.isBlank(appConfig.getNotifLargeTextsConfig().getNotifLargeTextList())) {
                for (AppNotifLargeTextConfig appNotifLargeTextConfig : appConfig.getNotifLargeTextsConfig()
                        .getNotifLargeTextList()) {
                    NotifLargeTextRestore notifLargeTextRestore = loadNotifLargeTextRestoreFromZip(tm,
                            appNotifLargeTextConfig.getConfigFile(), zipFile, _entries);
                    notifLargeTextList.add(notifLargeTextRestore);
                }
            }

            List<WorkflowRestore> workflowList = new ArrayList<WorkflowRestore>();
            if (appConfig.getWorkflowsConfig() != null
                    && !DataUtils.isBlank(appConfig.getWorkflowsConfig().getWorkflowList())) {
                for (AppWorkflowConfig appWorkflowConfig : appConfig.getWorkflowsConfig().getWorkflowList()) {
                    WorkflowRestore workflowRestore = loadWorkflowRestoreFromZip(tm, appWorkflowConfig.getConfigFile(),
                            zipFile, _entries);
                    workflowList.add(workflowRestore);
                }
            }

            List<WorkflowWizardRestore> workflowWizardList = new ArrayList<WorkflowWizardRestore>();
            if (appConfig.getWorkflowWizardsConfig() != null
                    && !DataUtils.isBlank(appConfig.getWorkflowWizardsConfig().getWorkflowWizardList())) {
                for (AppWorkflowWizardConfig appWorkflowWizardConfig : appConfig.getWorkflowWizardsConfig()
                        .getWorkflowWizardList()) {
                    WorkflowWizardRestore workflowWizardRestore = loadWorkflowWizardRestoreFromZip(tm,
                            appWorkflowWizardConfig.getConfigFile(), zipFile, _entries);
                    workflowWizardList.add(workflowWizardRestore);
                }
            }

            applicationRestore = new ApplicationRestore(appConfig, reportList, notifTemplateList, notifLargeTextList,
                    workflowList, workflowWizardList);
            logDebug(tm, "Loaded application definition from [{0}] successfully.", configFile);
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
        }

        return applicationRestore;
    }

    private ReportRestore loadReportRestoreFromZip(TaskMonitor tm, String configFile, ZipFile zipFile,
            Map<String, ZipEntry> _entries) throws UnifyException {
        ReportRestore reportRestore = null;
        InputStream inputStream = null;
        try {
            logDebug(tm, "Loading report definition from [{0}]...", configFile);
            inputStream = zipFile.getInputStream(_entries.get(configFile));
            ReportConfig reportConfig = ConfigurationUtils.readConfig(ReportConfig.class, inputStream);
            reportRestore = new ReportRestore(reportConfig);
            logDebug(tm, "Loaded report definition from [{0}] successfully.", configFile);
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
        }

        return reportRestore;
    }

    private NotifTemplateRestore loadNotifTemplateRestoreFromZip(TaskMonitor tm, String configFile, ZipFile zipFile,
            Map<String, ZipEntry> _entries) throws UnifyException {
        NotifTemplateRestore notifTemplateRestore = null;
        InputStream inputStream = null;
        try {
            logDebug(tm, "Loading notification template definition from [{0}]...", configFile);
            inputStream = zipFile.getInputStream(_entries.get(configFile));
            NotifTemplateConfig notifTemplateConfig = ConfigurationUtils.readConfig(NotifTemplateConfig.class,
                    inputStream);
            notifTemplateRestore = new NotifTemplateRestore(notifTemplateConfig);
            logDebug(tm, "Loaded notification template definition from [{0}] successfully.", configFile);
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
        }

        return notifTemplateRestore;
    }

    private NotifLargeTextRestore loadNotifLargeTextRestoreFromZip(TaskMonitor tm, String configFile, ZipFile zipFile,
            Map<String, ZipEntry> _entries) throws UnifyException {
        NotifLargeTextRestore notifLargeTextRestore = null;
        InputStream inputStream = null;
        try {
            logDebug(tm, "Loading notification large text definition from [{0}]...", configFile);
            inputStream = zipFile.getInputStream(_entries.get(configFile));
            NotifLargeTextConfig notifLargeTextConfig = ConfigurationUtils.readConfig(NotifLargeTextConfig.class,
                    inputStream);
            notifLargeTextRestore = new NotifLargeTextRestore(notifLargeTextConfig);
            logDebug(tm, "Loaded notification large text definition from [{0}] successfully.", configFile);
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
        }

        return notifLargeTextRestore;
    }

    private WorkflowRestore loadWorkflowRestoreFromZip(TaskMonitor tm, String configFile, ZipFile zipFile,
            Map<String, ZipEntry> _entries) throws UnifyException {
        WorkflowRestore workflowRestore = null;
        InputStream inputStream = null;
        try {
            logDebug(tm, "Loading workflow definition from [{0}]...", configFile);
            inputStream = zipFile.getInputStream(_entries.get(configFile));
            WfConfig wfConfig = ConfigurationUtils.readConfig(WfConfig.class, inputStream);
            workflowRestore = new WorkflowRestore(wfConfig);
            logDebug(tm, "Loaded workflow definition from [{0}] successfully.", configFile);
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
        }

        return workflowRestore;
    }

    private WorkflowWizardRestore loadWorkflowWizardRestoreFromZip(TaskMonitor tm, String configFile, ZipFile zipFile,
            Map<String, ZipEntry> _entries) throws UnifyException {
        WorkflowWizardRestore workflowWizardRestore = null;
        InputStream inputStream = null;
        try {
            logDebug(tm, "Loading workflow wizard definition from [{0}]...", configFile);
            inputStream = zipFile.getInputStream(_entries.get(configFile));
            WfWizardConfig wfWizardConfig = ConfigurationUtils.readConfig(WfWizardConfig.class, inputStream);
            workflowWizardRestore = new WorkflowWizardRestore(wfWizardConfig);
            logDebug(tm, "Loaded workflow wizard definition from [{0}] successfully.", configFile);
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            IOUtils.close(inputStream);
        }

        return workflowWizardRestore;
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        StudioWidgetWriterUtils.registerJSAliases();
    }

}
