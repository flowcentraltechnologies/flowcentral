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
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractMenuWidget;
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

    private static final List<String> snapshotAppletList = Collections
            .unmodifiableList(Arrays.asList("studio.takeSnapshot", "studio.uploadSnapshot", "studio.snapshots"));

    private static final List<StudioAppComponentType> utilMenuCategoryList = Collections.unmodifiableList(
            Arrays.asList(StudioAppComponentType.CODEGENERATION, StudioAppComponentType.SYNCHRONIZATION,
                    StudioAppComponentType.SNAPSHOT, StudioAppComponentType.APPLICATION));

    private static final List<StudioAppComponentType> collabUtilMenuCategoryList = Collections
            .unmodifiableList(Arrays.asList(StudioAppComponentType.COLLABORATION, StudioAppComponentType.CODEGENERATION,
                    StudioAppComponentType.SYNCHRONIZATION, StudioAppComponentType.SNAPSHOT,
                    StudioAppComponentType.APPLICATION));

    private static final List<StudioAppComponentType> menuCategoryList = Collections
            .unmodifiableList(Arrays.asList(StudioAppComponentType.ENUMERATION, StudioAppComponentType.WIDGET,
                    StudioAppComponentType.ENTITY, StudioAppComponentType.APPLET, StudioAppComponentType.REFERENCE,
                    StudioAppComponentType.CHART_DATASOURCE, StudioAppComponentType.CHART,
                    StudioAppComponentType.DASHBOARD, StudioAppComponentType.NOTIFICATION_TEMPLATE,
                    StudioAppComponentType.NOTIFICATION_LARGETEXT, StudioAppComponentType.REPORT_CONFIGURATION,
                    StudioAppComponentType.TABLE, StudioAppComponentType.FORM, StudioAppComponentType.WORKFLOW));

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final StudioMenuWidget studioMenuWidget = (StudioMenuWidget) widget;
        final boolean searchable = studioMenuWidget.isSearchable();

        final String applicationName = (String) getSessionAttribute(
                StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
        final boolean application = !StringUtils.isBlank(applicationName);

        writer.write("<div");
        writeTagAttributesWithTrailingExtraStyleClass(writer, studioMenuWidget, "g_fsm");
        writer.write("><div class=\"mheader\">");
        writer.write("<span>");
        writer.writeWithHtmlEscape(application ? getSessionMessage("studio.menu.application.components")
                : getSessionMessage("studio.menu.application.utilities"));
        writer.write("</span></div><div class=\"mbody\">");
        if (searchable) {
            // Search
            writer.write("<div class=\"msearch\"><span class=\"mfil g_fsm\">");
            writer.write(resolveSymbolHtmlHexCode("filter"));
            writer.write("</span>");
            writer.writeStructureAndContent(studioMenuWidget.getSearchCtrl());
            writer.write("<span class=\"mban g_fsm\" id=\"").write(studioMenuWidget.getClearId()).write("\">");
            writer.write(resolveSymbolHtmlHexCode("ban"));
            writer.write("</span>");
            writer.write("</div>");
        }

        final boolean isCollaborationEnabled = appletUtilities.collaborationProvider() != null;
        final List<StudioAppComponentType> selMenuCategoryList = application ? menuCategoryList
                : (isCollaborationEnabled ? collabUtilMenuCategoryList : utilMenuCategoryList);
        StudioAppComponentType currCategory = studioMenuWidget.getCurrentSel();
        if (currCategory == null) {
            currCategory = application ? StudioAppComponentType.ENUMERATION
                    : (isCollaborationEnabled ? StudioAppComponentType.COLLABORATION
                            : (codeGenerationProvider != null ? StudioAppComponentType.CODEGENERATION
                                    : StudioAppComponentType.APPLICATION));
            studioMenuWidget.setCurrentSel(currCategory);
        }

        writer.write("<div");
        if (searchable) {
            writer.write(" class=\"mtop\"");
        }

        writer.write(" style=\"display:table;width:100%;height:100%;\"><div style=\"display:table-row;\">");
        // Category
        writer.write("<div class=\"menucatbar\" style=\"display:table-cell;vertical-align:top;\">");
        JsonWriter cjw = new JsonWriter();
        cjw.beginArray();
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
            writer.write("<span class=\"symcat\">").write(resolveSymbolHtmlHexCode(category.icon())).write("</span>");
            writer.write("<span class=\"labelcat\">").writeWithHtmlEscape(resolveSessionMessage(category.caption2()))
                    .write("</span>");
            writer.write("</div>");
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

        // Body
        writer.write("<div class=\"mbody\">");
        writer.write("<div");
        String menuSectionId = studioMenuWidget.getMenuSectionId();
        writeTagId(writer, menuSectionId);
        writer.write(">");
        doWriteSectionStructureAndContent(writer, studioMenuWidget, menuSectionId);
        writer.write("</div>");
        writer.write("</div>");
        // End body

        writer.write("</div>");
        writer.write("</div>");

        writer.write("</div></div>");

        WriteWork work = studioMenuWidget.getWriteWork();
        work.set(StudioMenuWidget.WORK_MENU_CATEGORIES, cjw);

        writer.writeStructureAndContent(studioMenuWidget.getCurrentSelCtrl());
        writer.write("</div></div>");
    }

    @Override
    protected void doWriteSectionStructureAndContent(ResponseWriter writer, Widget widget, String sectionId)
            throws UnifyException {
        final StudioMenuWidget studioMenuWidget = (StudioMenuWidget) widget;
        if (studioMenuWidget.getMenuSectionId().equals(sectionId)) {
            final String applicationName = (String) getSessionAttribute(
                    StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
            writer.write("<ul>");
            JsonWriter mjw = new JsonWriter();
            mjw.beginArray();
            final StudioAppComponentType currCategory = studioMenuWidget.getCurrentSel();
            final boolean isApplications = StudioAppComponentType.APPLICATION.equals(currCategory);
            final boolean isCollaboration = StudioAppComponentType.COLLABORATION.equals(currCategory);
            final boolean isCodeGeneration = codeGenerationProvider != null
                    && StudioAppComponentType.CODEGENERATION.equals(currCategory);
            final boolean isSynchronization = StudioAppComponentType.SYNCHRONIZATION.equals(currCategory);
            final boolean isSnapshot = StudioAppComponentType.SNAPSHOT.equals(currCategory);

            final String searchInput = studioMenuWidget.isSearchable() ? studioMenuWidget.getSearchInput() : null;
            final List<AppletDef> appletDefList = isApplications
                    ? getApplicationAppletDefs(applicationName, searchInput)
                    : (isCollaboration ? getCollaborationAppletDefs(applicationName, searchInput)
                            : (isCodeGeneration ? getCodeGenerationAppletDefs(applicationName, searchInput)
                                    : (isSynchronization ? getSychronizationAppletDefs(applicationName, searchInput)
                                            : (isSnapshot ? getSnapshotAppletDefs(applicationName, searchInput)
                                                    : studioModuleService.findAppletDefs(applicationName, currCategory,
                                                            searchInput)))));

            for (AppletDef appletDef : appletDefList) {
                if (isApplications || isCollaboration || isCodeGeneration || isSynchronization || isSnapshot
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
            mjw.endArray();
            writer.write("</ul>");

            WriteWork work = studioMenuWidget.getWriteWork();
            work.set(StudioMenuWidget.WORK_MENUITEMS, mjw);
        }
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        StudioMenuWidget studioMenuWidget = (StudioMenuWidget) widget;
        WriteWork work = studioMenuWidget.getWriteWork();
        writer.beginFunction("fuxstudio.rigStudioMenu");
        writer.writeParam("pId", studioMenuWidget.getId());
        writer.writeParam("pContId", studioMenuWidget.getContainerId());
        writer.writeParam("pCategoryId", studioMenuWidget.getCategoryId());
        final boolean searchable = studioMenuWidget.isSearchable();
        if (searchable) {
            writer.writeParam("pSearch", true);
            writer.writeParam("pInpId", studioMenuWidget.getSearchCtrl().getId());
            writer.writeParam("pClrId", studioMenuWidget.getClearId());
        }

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
        writer.writeParam("pMenuItems", (JsonWriter) work.get(StudioMenuWidget.WORK_MENUITEMS));
        writer.endFunction();
        getRequestContextUtil().setDefaultFocusOnWidgetId(studioMenuWidget.getSearchCtrl().getId());
    }

    @Override
    protected void doWriteSectionBehavior(ResponseWriter writer, Widget widget, String sectionId)
            throws UnifyException {
        StudioMenuWidget studioMenuWidget = (StudioMenuWidget) widget;
        if (studioMenuWidget.getMenuSectionId().equals(sectionId)) {
            WriteWork work = studioMenuWidget.getWriteWork();
            writer.beginFunction("fux.rigMenuSectionResult");
            writer.writeParam("pId", studioMenuWidget.getId());
            writer.writeParam("pContId", studioMenuWidget.getContainerId());
            writer.writeCommandURLParam("pCmdURL");
            writer.writeParam("pCollInit", studioMenuWidget.isCollapsedInitial());
            writer.writeResolvedParam("pMenuItems",
                    ((JsonWriter) work.get(AbstractMenuWidget.WORK_MENUITEMS)).toString());
            writer.endFunction();
        }
    }

    private List<AppletDef> getApplicationAppletDefs(String applicationName, String filter) throws UnifyException {
        return getRoleAppletDefs(applicationName, applicationAppletList, filter);
    }

    private List<AppletDef> getCollaborationAppletDefs(String applicationName, String filter) throws UnifyException {
        return getRoleAppletDefs(applicationName, appletUtilities.collaborationProvider().getCollaborationApplets(),
                filter);
    }

    private List<AppletDef> getCodeGenerationAppletDefs(String applicationName, String filter) throws UnifyException {
        return getRoleAppletDefs(applicationName, codeGenerationProvider.getCodeGenerationApplets(), filter);
    }

    private List<AppletDef> getSychronizationAppletDefs(String applicationName, String filter) throws UnifyException {
        return getRoleAppletDefs(applicationName, synchronizationAppletList, filter);
    }

    private List<AppletDef> getSnapshotAppletDefs(String applicationName, String filter) throws UnifyException {
        return getRoleAppletDefs(applicationName, snapshotAppletList, filter);
    }

    private List<AppletDef> getRoleAppletDefs(String applicationName, List<String> applets, String filter)
            throws UnifyException {
        final String roleCode = getUserToken().getRoleCode();
        if (filter != null) {
            filter = filter.toLowerCase();
        }

        List<AppletDef> appletDefList = new ArrayList<AppletDef>();
        for (String appletName : applets) {
            AppletDef _appletDef = appletUtilities.application()
                    .getAppletDef(ApplicationNameUtils.addVestigialNamePart(appletName, applicationName));
            if (appletUtilities.applicationPrivilegeManager().isRoleWithPrivilege(roleCode,
                    _appletDef.getPrivilege())) {
                if (filter == null || _appletDef.getLowerCaseLabel().contains(filter)) {
                    appletDefList.add(_appletDef);
                }
            }
        }
        return appletDefList;
    }
}
