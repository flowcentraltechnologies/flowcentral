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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.ApplicationAppletDefProvider;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.ApplicationMenuDef;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractMenuWidget;
import com.flowcentraltech.flowcentral.application.web.widgets.AppletMenuWidget;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.business.LicenseProvider;
import com.flowcentraltech.flowcentral.common.business.WorkspacePrivilegeManager;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.LicenseFeatureCodeConstants;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.constants.SystemColorType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;

/**
 * Applet menu writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(AppletMenuWidget.class)
@Component("fc-appletmenu-writer")
public class AppletMenuWriter extends AbstractMenuWriter {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private ApplicationPrivilegeManager appPrivilegeManager;

    @Configurable
    private WorkspacePrivilegeManager wkspPrivilegeManager;

    @Configurable
    private LicenseProvider licenseProvider;

    @Configurable
    private CollaborationProvider collaborationProvider;

    private List<ApplicationAppletDefProvider> applicationAppletDefProviderList;

    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public final void setAppPrivilegeManager(ApplicationPrivilegeManager appPrivilegeManager) {
        this.appPrivilegeManager = appPrivilegeManager;
    }

    public final void setWkspPrivilegeManager(WorkspacePrivilegeManager wkspPrivilegeManager) {
        this.wkspPrivilegeManager = wkspPrivilegeManager;
    }

    public final void setLicenseProvider(LicenseProvider licenseProvider) {
        this.licenseProvider = licenseProvider;
    }

    public final void setCollaborationProvider(CollaborationProvider collaborationProvider) {
        this.collaborationProvider = collaborationProvider;
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        applicationAppletDefProviderList = getComponents(ApplicationAppletDefProvider.class);
    }

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final AppletMenuWidget appletMenuWidget = (AppletMenuWidget) widget;
        final boolean searchable = appletMenuWidget.isSearchable();
        appletMenuWidget.setCollapsedInitial(true);
        writer.write("<div");
        writeTagAttributesWithTrailingExtraStyleClass(writer, appletMenuWidget, "g_fsm");
        writer.write(">");
        // Header
        writer.write("<div class=\"mheader\">");
        writer.write("<div style=\"display:inline-block;float:left;\"><span>");
        writer.writeWithHtmlEscape(getSessionMessage("appletmenu.application"));
        writer.write("</span></div>");
        writer.write("<div style=\"display:inline-block;float:right;\">");
        writer.write("<span id=\"exp_").write(appletMenuWidget.getId()).write("\" class=\"icon\">");
        writer.write(resolveSymbolHtmlHexCode("plus-square"));
        writer.write("</span>");
        writer.write("<span id=\"col_").write(appletMenuWidget.getId()).write("\" class=\"icon\">");
        writer.write(resolveSymbolHtmlHexCode("minus-square"));
        writer.write("</span>");
        writer.write("</div></div>");
        // End header

        // Body
        writer.write("<div class=\"mbody\">");
        if (searchable) {
            // Search
            writer.write("<div class=\"msearch\"><span class=\"mfil g_fsm\">");
            writer.write(resolveSymbolHtmlHexCode("filter"));
            writer.write("</span>");
            writer.writeStructureAndContent(appletMenuWidget.getSearchCtrl());
            writer.write("<span class=\"mban g_fsm\" id=\"").write(appletMenuWidget.getClearId()).write("\">");
            writer.write(resolveSymbolHtmlHexCode("ban"));
            writer.write("</span>");
            writer.write("</div>");
        }

        writer.write("<div");
        if (searchable) {
            writer.write(" class=\"msection\"");
        }
        String menuSectionId = appletMenuWidget.getMenuSectionId();
        writeTagId(writer, menuSectionId);
        writer.write(">");
        doWriteSectionStructureAndContent(writer, appletMenuWidget, menuSectionId);
        writer.write("</div>");

        writer.write("</div>");
        // End body

        writer.write("</div>");
    }

    private static final Set<String> STANDARD_EXCLUSION = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("manageLicensing")));

    @Override
    protected void doWriteSectionStructureAndContent(ResponseWriter writer, Widget widget, String sectionId)
            throws UnifyException {
        final AppletMenuWidget appletMenuWidget = (AppletMenuWidget) widget;
        if (appletMenuWidget.getMenuSectionId().equals(sectionId)) {
            final UserToken userToken = getUserToken();
            final String roleCode = userToken.getRoleCode();
            final String workspaceCode = (String) getSessionAttribute(
                    FlowCentralSessionAttributeConstants.WORKSPACE_CODE);
            final String studioApplicationName = systemModuleService.getSysParameterValue(String.class,
                    ApplicationModuleSysParamConstants.STUDIO_APPLICATION_NAME);
            final boolean indicateSectors = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SECTOR_INDICATION_ON_MENU);
            final boolean studioMenuEnabled = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.STUDIO_MENU_ENABLED);
            final boolean sectionWithItemsOnly = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SHOW_MENU_SECTIONS_ITEMS_ONLY);
            final boolean isWorkspaceLicensed = licenseProvider != null
                    && licenseProvider.isLicensed(LicenseFeatureCodeConstants.APPLICATION_WORKSPACES);
            final boolean enterprise = collaborationProvider != null;

            final StringBuilder msb = new StringBuilder();
            final StringBuilder misb = new StringBuilder();
            msb.append('[');
            misb.append('[');
            final String submenuStyle = appletMenuWidget.isCollapsedInitial() ? "none" : "block";
            boolean appendSym = false;
            boolean appendISym = false;
            final String searchInput = appletMenuWidget.isSearchable() ? appletMenuWidget.getSearchInput() : null;
            List<ApplicationMenuDef> applicationDefList = applicationModuleService.getApplicationMenuDefs(searchInput);
            for (ApplicationMenuDef applicationMenuDef : applicationDefList) {
                final String appPrivilegeCode = applicationMenuDef.getPrivilege();
                if (appPrivilegeManager.isRoleWithPrivilege(roleCode, appPrivilegeCode) && (!isWorkspaceLicensed
                        || wkspPrivilegeManager.isWorkspaceWithPrivilege(workspaceCode, appPrivilegeCode))) {
                    if (sectionWithItemsOnly && DataUtils.isBlank(applicationMenuDef.getAppletDefList())) {
                        continue;
                    }

                    if (!studioMenuEnabled && applicationMenuDef.getName().equals(studioApplicationName)) {
                        continue;
                    }

                    final Long applicationId = applicationMenuDef.getId();
                    writer.write("<div id=\"menu_").write(applicationId).write("\" class=\"menu\">");
                    if (indicateSectors) {
                        writer.write("<span class=\"ind\" style=\"background-color:");
                        String color = applicationMenuDef.isWithSectionColor() ? applicationMenuDef.getSectionColor()
                                : SystemColorType.GRAY.code();
                        writer.write(color);
                        writer.write(";\">");
                        if (applicationMenuDef.isWithSectionShortCode()) {
                            writer.writeWithHtmlEscape(applicationMenuDef.getSectionShortCode());
                        } else {
                            writer.writeHtmlFixedSpace();
                        }
                        writer.write("</span>");
                    }

                    writer.write("<span class=\"lab\">");
                    writer.writeWithHtmlEscape(applicationMenuDef.getLabel());
                    writer.write("</span></div>");
                    writer.write("<div id=\"submenu_").write(applicationId)
                            .write("\" class=\"submenu\" style=\"display:").write(submenuStyle).write("\">");
                    writer.write("<ul>");

                    if (appendSym) {
                        msb.append(',');
                    } else {
                        appendSym = true;
                    }
                    msb.append('"').append(applicationId).append('"');

                    for (AppletDef appletDef : applicationMenuDef.getAppletDefList()) {
                        if (!enterprise && STANDARD_EXCLUSION.contains(appletDef.getName())) {
                            continue;
                        }

                        final String appletPrivilegeCode = appletDef.getPrivilege();
                        if (appPrivilegeManager.isRoleWithPrivilege(roleCode, appletPrivilegeCode)
                                && (!isWorkspaceLicensed || wkspPrivilegeManager.isWorkspaceWithPrivilege(workspaceCode,
                                        appletPrivilegeCode))) {
                            writeSubMenuAppletDef(writer, misb, appletDef, appendISym);
                            appendISym = true;
                        }
                    }

                    for (ApplicationAppletDefProvider appletDefProvider : applicationAppletDefProviderList) {
                        for (AppletDef appletDef : appletDefProvider.getAppletDefsByRole(applicationMenuDef.getName(),
                                roleCode, searchInput)) {
                            if (appletDef.isMenuAccess()) {
                                writeSubMenuAppletDef(writer, misb, appletDef, appendISym);
                                appendISym = true;
                            }
                        }
                    }
                    writer.write("</ul></div>");
                }
            }
            msb.append(']');
            misb.append(']');

            WriteWork work = appletMenuWidget.getWriteWork();
            work.set(AppletMenuWidget.WORK_MENUIDS, msb.toString());
            work.set(AppletMenuWidget.WORK_MENUITEMS, misb.toString());
        }
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        final AppletMenuWidget appletMenuWidget = (AppletMenuWidget) widget;
        final boolean searchable = appletMenuWidget.isSearchable();
        if (searchable) {
            writer.beginFunction("fux.rigMenuSearch");
            writer.writeParam("pId", appletMenuWidget.getId());
            writer.writeParam("pInpId", appletMenuWidget.getSearchCtrl().getId());
            writer.writeParam("pClrId", appletMenuWidget.getClearId());
            writer.writeCommandURLParam("pCmdURL");
            writer.endFunction();
            getRequestContextUtil().setDefaultFocusOnWidgetId(appletMenuWidget.getSearchCtrl().getId());
        }
    }

    @Override
    protected void doWriteSectionBehavior(ResponseWriter writer, Widget widget, String sectionId)
            throws UnifyException {
        final AppletMenuWidget appletMenuWidget = (AppletMenuWidget) widget;
        if (appletMenuWidget.getMenuSectionId().equals(sectionId)) {
            WriteWork work = appletMenuWidget.getWriteWork();
            writer.beginFunction("fux.rigMenuSectionResult");
            writer.writeParam("pId", appletMenuWidget.getId());
            writer.writeParam("pContId", appletMenuWidget.getContainerId());
            writer.writeCommandURLParam("pCmdURL");
            writer.writeParam("pCollInit", appletMenuWidget.isCollapsedInitial());
            writer.writeResolvedParam("pMenuIds", (String) work.get(AbstractMenuWidget.WORK_MENUIDS));
            writer.writeResolvedParam("pMenuItems", (String) work.get(AbstractMenuWidget.WORK_MENUITEMS));
            writer.endFunction();
        }
    }

}
