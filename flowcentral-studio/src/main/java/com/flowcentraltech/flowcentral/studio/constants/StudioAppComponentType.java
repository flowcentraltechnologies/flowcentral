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

package com.flowcentraltech.flowcentral.studio.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.chart.entities.Chart;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Studio application component type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum StudioAppComponentType implements EnumConst {

    // Codes MUST always have a length of 4
    APPLICATION(
            "app_",
            null,
            "$m{studio.application.component.type.application}",
            "$m{studio.application.component.type.applications}",
            "studio.menu.label.application",
            "desktop",
            null,
            null,
            null,
            null,
            0,
            AppletType.MANAGE_ENTITYLIST,
            null),
    COLLABORATION(
            "clb_",
            null,
            "$m{studio.application.component.type.collaboration}",
            "$m{studio.application.component.type.collaborations}",
            "studio.menu.label.collaboration",
            "users",
            null,
            null,
            null,
            null,
            0,
            AppletType.MANAGE_ENTITYLIST,
            null),
    CODEGENERATION(
            "cdn_",
            null,
            "$m{studio.application.component.type.codegeneration}",
            "$m{studio.application.component.type.codegenerations}",
            "studio.menu.label.codegeneration",
            "cogs",
            null,
            null,
            null,
            null,
            0,
            AppletType.MANAGE_ENTITYLIST,
            null),
    WIDGET(
            "wdg_",
            "appWidgetTypeForm",
            "$m{studio.application.component.type.widget}",
            "$m{studio.application.component.type.widgets}",
            "studio.menu.label.widgettype",
            "grid",
            "/studioappcomponentapplet",
            "studiooncreatecomponent-policy",
            null,
            null,
            StudioAppComponentFlags.ENTITY_COMPONENT | StudioAppComponentFlags.SUPPORTS_NEW
                    | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            AppWidgetType.class),
    APPLET(
            "apl_",
            "appAppletForm",
            "$m{studio.application.component.type.applet}",
            "$m{studio.application.component.type.applets}",
            "studio.menu.label.applet",
            "window-restore",
            "/studioappcomponentapplet",
            "studiooncreateappapplet-policy",
            null,
            "studioondeleteappapplet-policy",
            StudioAppComponentFlags.ENTITY_COMPONENT | StudioAppComponentFlags.SUPPORTS_NEW
                    | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            AppApplet.class),
    ENTITY(
            "ent_",
            "appEntityForm",
            "$m{studio.application.component.type.entity}",
            "$m{studio.application.component.type.entities}",
            "studio.menu.label.entity",
            "database",
            "/studioappcomponentapplet",
            "studiooncreateappentity-policy",
            "studioonupdateappentity-policy",
            null,
            StudioAppComponentFlags.SUPPORTS_NEW | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            AppEntity.class),
    REFERENCE(
            "ref_",
            "appRefForm",
            "$m{studio.application.component.type.reference}",
            "$m{studio.application.component.type.references}",
            "studio.menu.label.reference",
            "newspaper",
            "/studioappcomponentapplet",
            "studiooncreatecomponent-policy",
            null,
            null,
            StudioAppComponentFlags.ENTITY_COMPONENT | StudioAppComponentFlags.SUPPORTS_NEW
                    | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            AppRef.class),
    CHART(
            "cht_",
            "chartForm",
            "$m{studio.application.component.type.chart}",
            "$m{studio.application.component.type.charts}",
            "studio.menu.label.chart",
            "chart-area",
            "/studioappcomponentapplet",
            "studiooncreatecomponent-policy",
            null,
            null,
            StudioAppComponentFlags.SUPPORTS_NEW | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            Chart.class),
    DASHBOARD(
            "dsh_",
            "dashboardForm",
            "$m{studio.application.component.type.dashboard}",
            "$m{studio.application.component.type.dashboards}",
            "studio.menu.label.dashboard",
            "chart-pie",
            "/studioappcomponentapplet",
            "studiooncreatecomponent-policy",
            null,
            null,
            StudioAppComponentFlags.SUPPORTS_NEW | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            Dashboard.class),
    TABLE(
            "tbl_",
            "appTableForm",
            "$m{studio.application.component.type.table}",
            "$m{studio.application.component.type.tables}",
            "studio.menu.label.table",
            "table",
            "/studioapptableapplet",
            "studiooncreatecomponent-policy",
            null,
            null,
            StudioAppComponentFlags.ENTITY_COMPONENT | StudioAppComponentFlags.SUPPORTS_NEW
                    | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            AppTable.class),
    FORM(
            "frm_",
            "appFormForm",
            "$m{studio.application.component.type.form}",
            "$m{studio.application.component.type.forms}",
            "studio.menu.label.form",
            "file",
            "/studioappformapplet",
            "studiooncreateappform-policy",
            null,
            null,
            StudioAppComponentFlags.ENTITY_COMPONENT | StudioAppComponentFlags.SUPPORTS_NEW
                    | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            AppForm.class),
    NOTIFICATION_TEMPLATE(
            "ntt_",
            "notifTemplateForm",
            "$m{studio.application.component.type.notificationtemplate}",
            "$m{studio.application.component.type.notificationtemplates}",
            "studio.menu.label.notificationtemplate",
            "mail",
            "/studioappcomponentapplet",
            "studiooncreatecomponent-policy",
            null,
            null,
            StudioAppComponentFlags.ENTITY_COMPONENT | StudioAppComponentFlags.SUPPORTS_NEW
                    | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            NotificationTemplate.class),
    REPORT_CONFIGURATION(
            "rpc_",
            "reportConfigForm",
            "$m{studio.application.component.type.reportconfiguration}",
            "$m{studio.application.component.type.reportconfigurations}",
            "studio.menu.label.reportconfiguration",
            "file-invoice",
            "/studioappcomponentapplet",
            "studiooncreatecomponent-policy",
            null,
            null,
            StudioAppComponentFlags.SUPPORTS_NEW | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            ReportConfiguration.class),
    WORKFLOW(
            "wrk_",
            "workflowForm",
            "$m{studio.application.component.type.workflow}",
            "$m{studio.application.component.type.workflows}",
            "studio.menu.label.workflow",
            "project-diagram",
            "/studioworkflowapplet",
            "studiooncreateworkflow-policy",
            "studioonupdateworkflow-policy",
            null,
            StudioAppComponentFlags.ENTITY_COMPONENT | StudioAppComponentFlags.SUPPORTS_NEW
                    | StudioAppComponentFlags.SUPPORTS_SAVEAS,
            AppletType.STUDIO_FC_COMPONENT,
            Workflow.class);

    private final String code;

    private final String form;

    private final String caption;

    private final String caption2;

    private final String icon;

    private final String labelKey;

    private final String appletPath;

    private final String createPolicy;

    private final String updatePolicy;

    private final String deletePolicy;

    private final boolean entityComponent;

    private final boolean supportsNew;

    private final boolean supportsSaveAs;

    private final AppletType appletType;

    private final Class<? extends BaseApplicationEntity> componentType;

    private final static Map<Class<? extends BaseApplicationEntity>, StudioAppComponentType> entityTypeMap;

    static {
        Map<Class<? extends BaseApplicationEntity>, StudioAppComponentType> map = new HashMap<Class<? extends BaseApplicationEntity>, StudioAppComponentType>();
        for (StudioAppComponentType type : StudioAppComponentType.values()) {
            map.put(type.componentType, type);
        }

        entityTypeMap = Collections.unmodifiableMap(map);
    };

    private StudioAppComponentType(String code, String form, String caption, String caption2, String labelKey,
            String icon, String appletPath, String createPolicy, String updatePolicy, String deletePolicy, int flags,
            AppletType appletType, Class<? extends BaseApplicationEntity> componentType) {
        this.code = code;
        this.form = form;
        this.caption = caption;
        this.caption2 = caption2;
        this.labelKey = labelKey;
        this.icon = icon;
        this.appletPath = appletPath;
        this.createPolicy = createPolicy;
        this.updatePolicy = updatePolicy;
        this.deletePolicy = deletePolicy;
        this.entityComponent = (StudioAppComponentFlags.ENTITY_COMPONENT & flags) > 0;
        this.supportsNew = (StudioAppComponentFlags.SUPPORTS_NEW & flags) > 0;
        this.supportsSaveAs = (StudioAppComponentFlags.SUPPORTS_SAVEAS & flags) > 0;
        this.appletType = appletType;
        this.componentType = componentType;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return ENTITY.code;
    }

    public String form() {
        return form;
    }

    public String caption() {
        return caption;
    }

    public String caption2() {
        return caption2;
    }

    public String icon() {
        return icon;
    }

    public String appletPath() {
        return appletPath;
    }

    public String createPolicy() {
        return createPolicy;
    }

    public String updatePolicy() {
        return updatePolicy;
    }

    public String deletePolicy() {
        return deletePolicy;
    }

    public boolean isEntityComponent() {
        return entityComponent;
    }

    public boolean isSupportsNew() {
        return supportsNew;
    }

    public boolean isSupportsSaveAs() {
        return supportsSaveAs;
    }

    public String labelKey() {
        return labelKey;
    }

    public AppletType appletType() {
        return appletType;
    }

    public Class<? extends BaseApplicationEntity> componentType() {
        return componentType;
    }

    public static StudioAppComponentType fromEntityClass(Class<? extends BaseApplicationEntity> clazz) {
        return entityTypeMap.get(clazz);
    }

    public static StudioAppComponentType fromCode(String code) {
        return EnumUtils.fromCode(StudioAppComponentType.class, code);
    }

    public static StudioAppComponentType fromName(String name) {
        return EnumUtils.fromName(StudioAppComponentType.class, name);
    }
}
