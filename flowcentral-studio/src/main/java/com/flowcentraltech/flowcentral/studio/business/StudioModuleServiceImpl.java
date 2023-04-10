/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.StandardAppletDef;
import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.util.ApplicationCollaborationUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.StudioProvider;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppletPropertyConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioModuleNameConstants;
import com.flowcentraltech.flowcentral.studio.util.StudioNameUtils;
import com.flowcentraltech.flowcentral.studio.util.StudioNameUtils.StudioAppletNameParts;
import com.flowcentraltech.flowcentral.studio.web.util.StudioWidgetWriterUtils;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.util.DataUtils;
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
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    private final FactoryMap<String, AppletDef> appletDefMap;

    public StudioModuleServiceImpl() {
        this.appletDefMap = new FactoryMap<String, AppletDef>()
            {
                @Override
                protected AppletDef create(String appletName, Object... arg1) throws Exception {
                    AppletDef appletDef = null;
                    StudioAppletNameParts np = StudioNameUtils.getStudioAppletNameParts(appletName);
                    final StudioAppComponentType type = np.getType();
                    final boolean descriptiveButtons = systemModuleService.getSysParameterValue(boolean.class,
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
                            final String entity = applicationModuleService.getFormDef(form).getEntityDef()
                                    .getLongName();
                            final String assignDescField = null;
                            final String pseudoDeleteField = null;
                            StandardAppletDef.Builder adb = StandardAppletDef.newBuilder(type.appletType(), entity, label, type.icon(),
                                    assignDescField, pseudoDeleteField, 0, true, false, descriptiveButtons, appletName,
                                    description, np.getInstId(), 0L);
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

                            adb.openPath(
                                    ApplicationPageUtils.constructAppletOpenPagePath(type.appletPath(), appletName));
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

    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public final void setAppletUtilities(AppletUtilities appletUtilities) {
        this.appletUtilities = appletUtilities;
    }

    @Override
    public boolean isInstallDefaultDeveloperRoles() throws UnifyException {
        return getContainerSetting(boolean.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_INSTALL_DEVELOPER_ROLES, true);
    }

    @Override
    public AppletDef getAppletDef(String appletName) throws UnifyException {
        return appletDefMap.get(appletName);
    }

    @Override
    public List<AppletDef> findAppletDefs(String applicationName, StudioAppComponentType type) throws UnifyException {
        List<AppletDef> appletDefList = null;
        List<Long> instIdList = applicationModuleService.findAppComponentIdList(applicationName, type.componentType());
        if (!instIdList.isEmpty()) {
            appletDefList = new ArrayList<AppletDef>();
            for (Long instId : instIdList) {
                appletDefList.add(appletDefMap.get(StudioNameUtils.getStudioAppletName(applicationName, type, instId)));
            }

            DataUtils.sortAscending(appletDefList, AppletDef.class, "label");
        }

        if (type.isSupportsNew() && !StringUtils.isBlank(applicationName)
                && applicationModuleService.isApplicationDevelopable(applicationName)) {
            if (appletDefList == null) {
                appletDefList = new ArrayList<AppletDef>();
            }

            appletDefList.add(0, appletDefMap.get(StudioNameUtils.getStudioAppletName(applicationName, type, 0L)));
        }

        return appletDefList != null ? appletDefList : Collections.emptyList();
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {
        installStudioFeatures(moduleInstall);
    }

    private void installStudioFeatures(final ModuleInstall moduleInstall) throws UnifyException {
        if (StudioModuleNameConstants.STUDIO_MODULE_NAME.equals(moduleInstall.getModuleConfig().getName())) {

        }
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        StudioWidgetWriterUtils.registerJSAliases();
    }

}
