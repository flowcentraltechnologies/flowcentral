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
package com.flowcentraltech.flowcentral.studio.web.writers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.CodeGenerationProvider;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.flowcentraltech.flowcentral.studio.web.widgets.StudioMenuWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.json.JsonWriter;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.writer.AbstractPanelWriter;

/**
 * Studio menu writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(StudioMenuWidget.class)
@Component("fc-studiomenu-writer")
public class StudioMenuWriter extends AbstractPanelWriter {

    private static final String ORIGINAL_MENU_PATHID = "originalStudio.menu.pathID";

    @Configurable
    private StudioModuleService studioModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    @Configurable
    private CodeGenerationProvider codeGenerationProvider;

    private static final List<String> applicationAppletList = Collections.unmodifiableList(
            Arrays.asList("studio.stuManageModule", "studio.manageApplication", "studio.applicationReplication"));

    private static final List<String> synchronizationAppletList = Collections
            .unmodifiableList(Arrays.asList("studio.delegateCreateSynchronization", "studio.delegateSynchronization"));

    private static final List<StudioAppComponentType> menuCategoryList = Collections.unmodifiableList(
            Arrays.asList(StudioAppComponentType.CODEGENERATION, StudioAppComponentType.SYNCHRONIZATION,
                    StudioAppComponentType.APPLICATION, StudioAppComponentType.WIDGET, StudioAppComponentType.ENTITY,
                    StudioAppComponentType.APPLET, StudioAppComponentType.REFERENCE,
                    StudioAppComponentType.CHART_DATASOURCE, StudioAppComponentType.CHART,
                    StudioAppComponentType.DASHBOARD, StudioAppComponentType.NOTIFICATION_TEMPLATE,
                    StudioAppComponentType.NOTIFICATION_LARGETEXT, StudioAppComponentType.REPORT_CONFIGURATION,
                    StudioAppComponentType.TABLE, StudioAppComponentType.FORM, StudioAppComponentType.WORKFLOW));

    private static final List<StudioAppComponentType> collaborationMenuCategoryList = Collections
            .unmodifiableList(Arrays.asList(StudioAppComponentType.COLLABORATION, StudioAppComponentType.CODEGENERATION,
                    StudioAppComponentType.SYNCHRONIZATION, StudioAppComponentType.APPLICATION,
                    StudioAppComponentType.WIDGET, StudioAppComponentType.ENTITY, StudioAppComponentType.APPLET,
                    StudioAppComponentType.REFERENCE, StudioAppComponentType.CHART_DATASOURCE,
                    StudioAppComponentType.CHART, StudioAppComponentType.DASHBOARD,
                    StudioAppComponentType.NOTIFICATION_TEMPLATE, StudioAppComponentType.NOTIFICATION_LARGETEXT,
                    StudioAppComponentType.REPORT_CONFIGURATION, StudioAppComponentType.TABLE,
                    StudioAppComponentType.FORM, StudioAppComponentType.WORKFLOW));

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        StudioMenuWidget studioMenuWidget = (StudioMenuWidget) widget;
        WriteWork work = studioMenuWidget.getWriteWork();
        writer.beginFunction("fuxstudio.rigStudioMenu");
        writer.writeParam("pId", studioMenuWidget.getId());
        writer.writeParam("pContId", studioMenuWidget.getContainerId());
        writer.writeParam("pCategoryId", studioMenuWidget.getCategoryId());
        // Resolves out of bean context error which usually happens on menu reload
        String originalPathId = (String) getSessionAttribute(ORIGINAL_MENU_PATHID);
        if (!StringUtils.isBlank(originalPathId)) {
            writer.writeCommandURLParam("pCmdURL", originalPathId);
        } else {
            originalPathId = getRequestContextUtil().getResponsePathParts().getControllerPathId();
            setSessionAttribute(ORIGINAL_MENU_PATHID, originalPathId);
            writer.writeCommandURLParam("pCmdURL");
        }

        writer.writeParam("pCurrSelCtrlId", studioMenuWidget.getCurrentSelCtrl().getId());
        writer.writeParam("pMenuCat", (JsonWriter) work.get(StudioMenuWidget.WORK_MENU_CATEGORIES));
        writer.writeParam("pMenuItems", (JsonWriter) work.get(StudioMenuWidget.WORK_MENU_ITEMS));
        writer.endFunction();
    }

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        StudioMenuWidget studioMenuWidget = (StudioMenuWidget) widget;
        writer.write("<div");
        writeTagAttributesWithTrailingExtraStyleClass(writer, studioMenuWidget, "g_fsm");
        writer.write("><div class=\"mheader\">");
        writer.write("<span>");
        writer.writeWithHtmlEscape(getSessionMessage("studio.menu.application.components"));
        writer.write("</span></div><div class=\"mbody\">");

        final boolean isCollaborationEnabled = appletUtilities.collaborationProvider() != null;
        final List<StudioAppComponentType> selMenuCategoryList = isCollaborationEnabled ? collaborationMenuCategoryList
                : menuCategoryList;
        StudioAppComponentType currCategory = studioMenuWidget.getCurrentSel();
        if (currCategory == null) {
            currCategory = isCollaborationEnabled ? StudioAppComponentType.COLLABORATION
                    : (codeGenerationProvider != null ? StudioAppComponentType.CODEGENERATION
                            : StudioAppComponentType.APPLICATION);
            studioMenuWidget.setCurrentSel(currCategory);
        }

        final String applicationName = (String) getSessionAttribute(
                StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
        final boolean application = !StringUtils.isBlank(applicationName);
        final boolean isApplications = StudioAppComponentType.APPLICATION.equals(currCategory);
        final boolean isCollaboration = StudioAppComponentType.COLLABORATION.equals(currCategory);
        final boolean isCodeGeneration = codeGenerationProvider != null
                && StudioAppComponentType.CODEGENERATION.equals(currCategory);
        final boolean isSynchronization = StudioAppComponentType.SYNCHRONIZATION.equals(currCategory);
        final List<AppletDef> appletDefList = isApplications ? getApplicationAppletDefs(applicationName)
                : (isCollaboration ? getCollaborationAppletDefs(applicationName)
                        : (isCodeGeneration ? getCodeGenerationAppletDefs(applicationName)
                                : (isSynchronization ? getSychronizationAppletDefs(applicationName)
                                        : studioModuleService.findAppletDefs(applicationName, currCategory))));

        writer.write("<div style=\"display:table;width:100%;height:100%;\"><div style=\"display:table-row;\">");
        // Category
        writer.write("<div class=\"menucatbar\" style=\"display:table-cell;vertical-align:top;\">");
        JsonWriter cjw = new JsonWriter();
        cjw.beginArray();
        if (application) {
            final String categoryId = studioMenuWidget.getCategoryId();
            final int catLen = selMenuCategoryList.size();
            for (int i = 0; i < catLen; i++) {
                final StudioAppComponentType category = selMenuCategoryList.get(i);
                writer.write("<div class=\"menucat");
                if (category.equals(currCategory)) {
                    writer.write(" activecat");
                } else {
                    writer.write("\" id=\"").write(categoryId).write(i);
                    cjw.beginObject();
                    cjw.write("index", i);
                    cjw.write("type", category.code());
                    cjw.endObject();
                }

                writer.write("\">");
                writer.write("<span class=\"symcat\">").write(resolveSymbolHtmlHexCode(category.icon()))
                        .write("</span>");
                writer.write("<span class=\"labelcat\">")
                        .writeWithHtmlEscape(resolveSessionMessage(category.caption2())).write("</span>");
                writer.write("</div>");
            }
        }
        cjw.endArray();
        writer.write("</div>");

        // Menu items
        writer.write("<div style=\"display:table-cell;vertical-align:top;\">");
        writer.write("<div class=\"menu\">");
        if (application) {
            writer.write("<div class=\"menucatcap\">");
            writer.writeWithHtmlEscape(resolveSessionMessage(currCategory.caption2()));
            writer.write("</div>");
        }
        writer.write("<ul>");
        JsonWriter mjw = new JsonWriter();
        mjw.beginArray();
        if (application) {
            for (AppletDef appletDef : appletDefList) {
                if (isApplications || isCollaboration || isCodeGeneration || isSynchronization
                        || appletDef.isMenuAccess()) {
                    writer.write("<li id=\"item_").write(appletDef.getViewId()).write("\">");
                    writer.write("<span>").writeWithHtmlEscape(appletDef.getLabel()).write("</span>");
                    writer.write("</li>");
                    mjw.beginObject();
                    mjw.write("id", "item_" + appletDef.getViewId());
                    mjw.write("path", getContextURL(appletDef.getOpenPath()));
                    mjw.endObject();
                }
            }
        }
        mjw.endArray();
        writer.write("</ul></div>");
        writer.write("</div>");

        writer.write("</div></div>");

        WriteWork work = studioMenuWidget.getWriteWork();
        work.set(StudioMenuWidget.WORK_MENU_CATEGORIES, cjw);
        work.set(StudioMenuWidget.WORK_MENU_ITEMS, mjw);

        writer.writeStructureAndContent(studioMenuWidget.getCurrentSelCtrl());
        writer.write("</div></div>");
    }

    private List<AppletDef> getApplicationAppletDefs(String applicationName) throws UnifyException {
        return getRoleAppletDefs(applicationName, applicationAppletList);
    }

    private List<AppletDef> getCollaborationAppletDefs(String applicationName) throws UnifyException {
        return getRoleAppletDefs(applicationName, appletUtilities.collaborationProvider().getCollaborationApplets());
    }

    private List<AppletDef> getCodeGenerationAppletDefs(String applicationName) throws UnifyException {
        return getRoleAppletDefs(applicationName, codeGenerationProvider.getCodeGenerationApplets());
    }

    private List<AppletDef> getSychronizationAppletDefs(String applicationName) throws UnifyException {
        return getRoleAppletDefs(applicationName, synchronizationAppletList);
    }

    private List<AppletDef> getRoleAppletDefs(String applicationName, List<String> applets) throws UnifyException {
        final String roleCode = getUserToken().getRoleCode();
        List<AppletDef> appletDefList = new ArrayList<AppletDef>();
        for (String appletName : applets) {
            AppletDef _appletDef = appletUtilities.application()
                    .getAppletDef(ApplicationNameUtils.addVestigialNamePart(appletName, applicationName));
            if (appletUtilities.applicationPrivilegeManager().isRoleWithPrivilege(roleCode,
                    _appletDef.getPrivilege())) {
                appletDefList.add(_appletDef);
            }
        }
        return appletDefList;
    }
}
