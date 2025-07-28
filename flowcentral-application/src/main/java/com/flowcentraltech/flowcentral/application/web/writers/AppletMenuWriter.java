/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.ApplicationAppletDefProvider;
import com.flowcentraltech.flowcentral.application.constants.AppletSessionAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.ApplicationMenuDef;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractMenuWidget;
import com.flowcentraltech.flowcentral.application.web.widgets.AppletMenuWidget;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.system.constants.SystemColorType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;

/**
 * Applet menu writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(AppletMenuWidget.class)
@Component("fc-appletmenu-writer")
public class AppletMenuWriter extends AbstractMenuWriter {

    private static final String WORKFLOW_DRAFT_APPLICATION = "workflowDraft";

    private List<ApplicationAppletDefProvider> applicationAppletDefProviderList;

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        applicationAppletDefProviderList = getComponents(ApplicationAppletDefProvider.class);
    }

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final AppletMenuWidget appletMenuWidget = (AppletMenuWidget) widget;
        final boolean searchable = appletMenuWidget.isSearchable();
        final boolean horizontal = appletMenuWidget.isHorizontal();
        removeSessionAttribute(AppletSessionAttributeConstants.MENU_OPEN_TAB_INFO);
        appletMenuWidget.setCollapsedInitial(true);

        if (horizontal) {
            writer.write("<div");
            writeTagAttributes(writer, appletMenuWidget);
            writer.write(">");

            // Body
            writer.write("<div class=\"hmbody\">");
            if (searchable) {
                // Search
                writer.write("<div class=\"hmsearch\">");
                writeFontIcon(writer, "mfil", "filter");
                writer.writeStructureAndContent(appletMenuWidget.getSearchCtrl());
                writeFontIcon(writer, appletMenuWidget.getClearId(), "mban", "ban");
                writer.write("</div>");
            }

            writer.write("<div class=\"hmnav\" id=\"").write(appletMenuWidget.getNavLeftId()).write("\">");;
            writeFontIcon(writer, "chevron-left");
            writer.write("</div>");

            writer.write("<div class=\"hmsection\"");
            String menuSectionId = appletMenuWidget.getMenuSectionId();
            writeTagId(writer, menuSectionId);
            writer.write(">");
            doWriteSectionStructureAndContent(writer, appletMenuWidget, menuSectionId);
            writer.write("</div>");

            writer.write("<div class=\"hmnav\" id=\"").write(appletMenuWidget.getNavRightId()).write("\">");
            writeFontIcon(writer, "chevron-right");
            writer.write("</div>");

            writer.write("</div>");
            // End body

            writer.write("</div>");
        } else {
            writer.write("<div");
            writeTagAttributes(writer, appletMenuWidget);
            writer.write(">");
            // Header
            writer.write("<div class=\"mheader\">");
            writer.write("<div style=\"display:inline-block;float:left;\"><span>");
            writer.writeWithHtmlEscape(getSessionMessage("appletmenu.application"));
            writer.write("</span></div>");
            writer.write("<div style=\"display:inline-block;float:right;\">");
            writeFontIcon(writer, "exp_" + appletMenuWidget.getId(), "icon", "plus-square");
            writeFontIcon(writer, "col_" + appletMenuWidget.getId(), "icon", "minus-square");
            writer.write("</div></div>");
            // End header

            // pop-up menu
            writer.write("<div id=\"mpop_").write(appletMenuWidget.getId())
                    .write("\" class=\"mpop\" style=\"display:none;\">");
            writer.write("</div>");

            // Body
            writer.write("<div class=\"mbody\">");
            if (searchable) {
                // Search
                writer.write("<div class=\"msearch\">");
                writeFontIcon(writer, "mfil", "filter");
                writer.writeStructureAndContent(appletMenuWidget.getSearchCtrl());
                writeFontIcon(writer, appletMenuWidget.getClearId(), "mban", "ban");
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
            final String studioApplicationName = au.system().getSysParameterValue(String.class,
                    ApplicationModuleSysParamConstants.STUDIO_APPLICATION_NAME);
            final boolean indicateSectorLabels = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SECTOR_LABEL_INDICATION_ON_MENU);
            final boolean indicateSectorColors = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SECTOR_COLOR_INDICATION_ON_MENU);
            final boolean studioMenuEnabled = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.STUDIO_MENU_ENABLED);
            final boolean sectionWithItemsOnly = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SHOW_MENU_SECTIONS_ITEMS_ONLY);
            final boolean enterprise = au.collaborationProvider() != null;
            final boolean multiPage = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ENABLE_VIEW_ENTITY_IN_SEPARATE_TAB);
            final boolean openInWindow = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ENABLE_OPEN_TAB_IN_BROWSER);
            final WriteParam wparam = new WriteParam(multiPage, openInWindow);

            final StringBuilder msb = new StringBuilder();
            final StringBuilder misb = new StringBuilder();
            msb.append('[');
            misb.append('[');
            final String submenuStyle = appletMenuWidget.isCollapsedInitial() ? "none" : "block";
            boolean appendSym = false;
            final String searchInput = appletMenuWidget.isSearchable() ? appletMenuWidget.getSearchInput() : null;
            List<ApplicationMenuDef> applicationDefList = au.application().getApplicationMenuDefs(searchInput);
            List<AppletDef> draftAppletList = new ArrayList<AppletDef>();
            for (ApplicationMenuDef applicationMenuDef : applicationDefList) {
                for (AppletDef appletDef : applicationMenuDef.getAppletDefList()) {
                    if (appletDef.isWithOpenDraftPath()) {
                        draftAppletList.add(appletDef);
                    }
                }
            }

            final boolean horizontal = appletMenuWidget.isHorizontal();
            if (horizontal) {
                writer.write("<div class=\"hmslide\" style=\"left:0px;\" id=\"").write(appletMenuWidget.getSlideId()).write("\">");
            }

            for (ApplicationMenuDef applicationMenuDef : applicationDefList) {
                final String appPrivilegeCode = applicationMenuDef.getPrivilege();
                if (appPrivilegeManager.isRoleWithPrivilege(roleCode, appPrivilegeCode) && (wkspPrivilegeManager == null
                        || wkspPrivilegeManager.isWorkspaceWithPrivilege(workspaceCode, appPrivilegeCode))) {
                    final boolean isWorkflowDraft = WORKFLOW_DRAFT_APPLICATION.equals(applicationMenuDef.getName());
                    if (sectionWithItemsOnly && ((isWorkflowDraft && DataUtils.isBlank(draftAppletList))
                            || (!isWorkflowDraft && DataUtils.isBlank(applicationMenuDef.getAppletDefList())))) {
                        continue;
                    }

                    if (!studioMenuEnabled && applicationMenuDef.getName().equals(studioApplicationName)) {
                        continue;
                    }

                    final Long applicationId = applicationMenuDef.getId();
                    boolean isWithSubMenus = false;
                    writer.useSecondary();
                    try {
                        final String color = (indicateSectorLabels || indicateSectorColors)
                                && applicationMenuDef.isWithSectionColor() ? applicationMenuDef.getSectionColor()
                                        : SystemColorType.GRAY.code();

                        writer.write("<div id=\"menu_").write(applicationId)
                                .write(horizontal ? "\" class=\"hmenu\"" : "\" class=\"menu\"");
                        if (indicateSectorColors) {
                            writer.write(" style=\"background-color:");
                            writer.write(color);
                            writer.write(";\"");
                        }
                        writer.write(">");

                        if (indicateSectorLabels) {
                            writer.write("<span class=\"ind\" style=\"background-color:");
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
                        writer.writeWithHtmlEscape(resolveSessionMessage(applicationMenuDef.getLabel()));
                        writer.write("</span></div>");
                        writer.write("<div id=\"submenu_").write(applicationId)
                                .write(horizontal ? "\" class=\"hsubmenu submenu\" style=\"display:"
                                        : "\" class=\"submenu\" style=\"display:")
                                .write(submenuStyle).write("\">");
                        writer.write("<ul>");

                        if (isWorkflowDraft) {
                            for (AppletDef appletDef : draftAppletList) {
                                final String appletPrivilegeCode = appletDef.getPrivilege();
                                if (appPrivilegeManager.isRoleWithPrivilege(roleCode, appletPrivilegeCode)
                                        && (wkspPrivilegeManager == null || wkspPrivilegeManager
                                                .isWorkspaceWithPrivilege(workspaceCode, appletPrivilegeCode))) {
                                    writeOpenDraftSubMenuAppletDef(writer, misb, appletDef, wparam);
                                    isWithSubMenus = true;
                                    wparam.setAppendISym(true);
                                }
                            }
                        } else {
                            for (AppletDef appletDef : applicationMenuDef.getAppletDefList()) {
                                if (!enterprise && STANDARD_EXCLUSION.contains(appletDef.getName())) {
                                    continue;
                                }

                                final String appletPrivilegeCode = appletDef.getPrivilege();
                                if (appPrivilegeManager.isRoleWithPrivilege(roleCode, appletPrivilegeCode)
                                        && (wkspPrivilegeManager == null || wkspPrivilegeManager
                                                .isWorkspaceWithPrivilege(workspaceCode, appletPrivilegeCode))) {
                                    writeSubMenuAppletDef(writer, misb, appletDef, wparam);
                                    isWithSubMenus = true;
                                    wparam.setAppendISym(true);
                                }
                            }

                            for (ApplicationAppletDefProvider appletDefProvider : applicationAppletDefProviderList) {
                                for (AppletDef appletDef : appletDefProvider
                                        .getAppletDefsByRole(applicationMenuDef.getName(), roleCode, searchInput)) {
                                    if (appletDef.isMenuAccess()) {
                                        writeSubMenuAppletDef(writer, misb, appletDef, wparam);
                                        isWithSubMenus = true;
                                        wparam.setAppendISym(true);
                                    }
                                }
                            }
                        }

                        writer.write("</ul></div>");
                    } finally {
                        if (horizontal || isWithSubMenus) {
                            if (appendSym) {
                                msb.append(',');
                            } else {
                                appendSym = true;
                            }

                            msb.append('"').append(applicationId).append('"');
                            writer.discardMergeSecondary();
                        } else {
                            writer.discardSecondary();
                        }
                    }
                }
            }

            if (horizontal) {
                writer.write("</div>");
            }

            msb.append(']');
            misb.append(']');

            WriteWork work = appletMenuWidget.getWriteWork();
            work.set(AppletMenuWidget.WORK_MENUIDS, msb.toString());
            work.set(AppletMenuWidget.WORK_MENUITEMS, misb.toString());
        }
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        final AppletMenuWidget appletMenuWidget = (AppletMenuWidget) widget;
        final boolean searchable = appletMenuWidget.isSearchable();
        if (searchable) {
            writer.beginFunction("fux.rigMenuSearch");
            writer.writeParam("pId", appletMenuWidget.getId());
            writer.writeParam("pInpId", appletMenuWidget.getSearchCtrl().getId());
            writer.writeParam("pClrId", appletMenuWidget.getClearId());
            writer.writeParam("pHoriz", appletMenuWidget.isHorizontal());
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
            writer.writeParam("pReg", true);
            writer.writeParam("pHoriz", appletMenuWidget.isHorizontal());
            writer.writeParam("pSecId", appletMenuWidget.getMenuSectionId());
            writer.writeParam("pSldId", appletMenuWidget.getSlideId());
            writer.writeParam("pLeftId", appletMenuWidget.getNavLeftId());
            writer.writeParam("pRightId", appletMenuWidget.getNavRightId());
            writer.writeResolvedParam("pMenuIds", (String) work.get(AbstractMenuWidget.WORK_MENUIDS));
            writer.writeResolvedParam("pMenuItems", (String) work.get(AbstractMenuWidget.WORK_MENUITEMS));
            writer.endFunction();
        }
    }

}
