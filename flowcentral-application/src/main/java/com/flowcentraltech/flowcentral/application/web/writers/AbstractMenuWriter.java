/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletSessionAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModulePathConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.RequestOpenTabInfo;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractMenuWidget;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.LicenseProvider;
import com.flowcentraltech.flowcentral.common.business.WorkspacePrivilegeManager;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.writer.AbstractPanelWriter;

/**
 * Convenient abstract base class for menu writers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractMenuWriter extends AbstractPanelWriter {

    @Configurable
    protected AppletUtilities au;

    @Configurable
    protected ApplicationPrivilegeManager appPrivilegeManager;

    @Configurable
    protected WorkspacePrivilegeManager wkspPrivilegeManager;

    @Configurable
    protected LicenseProvider licenseProvider;

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        AbstractMenuWidget appletMenuWidget = (AbstractMenuWidget) widget;
        WriteWork work = appletMenuWidget.getWriteWork();
        writer.beginFunction("fux.rigMenu");
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

    protected void writeMenuAppletDef(ResponseWriter writer, StringBuilder misb, AppletDef appletDef, WriteParam wparam)
            throws UnifyException {
        writer.write("<div id=\"item_").write(appletDef.getViewId()).write("\">");
        writeLabelWithIcon(writer, appletDef, false);
        writer.write("</div>");
        writeAppletDefJs(writer, misb, appletDef, wparam, false);
    }

    protected void writeSubMenuAppletDef(ResponseWriter writer, StringBuilder misb, AppletDef appletDef,
            WriteParam wparam) throws UnifyException {
        writer.write("<li id=\"item_").write(appletDef.getViewId()).write("\">");
        writeLabelWithIcon(writer, appletDef, false);
        writer.write("</li>");
        writeAppletDefJs(writer, misb, appletDef, wparam, false);
    }

    protected void writeOpenDraftSubMenuAppletDef(ResponseWriter writer, StringBuilder misb, AppletDef appletDef,
            WriteParam wparam) throws UnifyException {
        if (appletDef.isWithOpenDraftPath()) {
            writer.write("<li id=\"item_").write(appletDef.getDraftViewId()).write("\">");
            writeLabelWithIcon(writer, appletDef, true);
            writer.write("</li>");
            writeAppletDefJs(writer, misb, appletDef, wparam, true);
        }
    }

    private void writeLabelWithIcon(ResponseWriter writer, AppletDef appletDef, boolean draft) throws UnifyException {
        writer.write("<span class=\"icon\">");
        final String icon = appletDef.isWithIcon() ? appletDef.getIcon() : "window-maximize";
        writer.write(resolveSymbolHtmlHexCode(icon));
        writer.write("</span>");
        writer.write(appletDef.isWithSubApplets() ? "<span class=\"aclm\">" : "<span class=\"acl\">")
                .writeWithHtmlEscape(draft ? resolveSessionMessage("$m{label.draft.applet}", appletDef.getLabel())
                        : appletDef.getLabel())
                .write("</span>");
        if (appletDef.isWithSubApplets()) {
            writer.write("<span class=\"icon\">");
            writer.write(resolveSymbolHtmlHexCode("angle-right"));
            writer.write("</span>");
        }
    }

    private void writeAppletDefJs(ResponseWriter writer, StringBuilder misb, AppletDef appletDef, WriteParam wparam,
            boolean isUpdateDraft) throws UnifyException {
        if (wparam.isAppendISym()) {
            misb.append(",");
        }

        final String path = resolvePath(appletDef, wparam, isUpdateDraft);
        misb.append("{\"id\":\"item_").append(isUpdateDraft ? appletDef.getDraftViewId() : appletDef.getViewId())
                .append('"');
        misb.append(",\"path\":\"");
        writer.writeContextURL(misb, path);
        misb.append('"');

        if (appletDef.isWithSubApplets()) {
            misb.append(",\"pSubMenuItems\":[");
            final UserToken userToken = getUserToken();
            final String roleCode = userToken.getRoleCode();
            boolean appendSym = false;
            for (String appletName : appletDef.getSubAppletList()) {
                final AppletDef _appletDef = au.getAppletDef(appletName);
                if (appPrivilegeManager.isRoleWithPrivilege(roleCode, _appletDef.getPrivilege())) {
                    if (appendSym) {
                        misb.append(',');
                    } else {
                        appendSym = true;
                    }

                    final String _path = resolvePath(_appletDef, wparam, isUpdateDraft);
                    misb.append("{\"path\":\"");
                    writer.writeContextURL(misb, _path);
                    misb.append('"');

                    misb.append(",\"label\":\"").append(_appletDef.getLabel()).append('"');

                    if (_appletDef.isOpenWindow()) {
                        misb.append(",\"isOpenWin\":").append(_appletDef.isOpenWindow());
                        misb.append(",\"winName\":\"").append(_appletDef.getName()).append('"');
                    }

                    final String icon = _appletDef.isWithIcon() ? _appletDef.getIcon() : "window-maximize";
                    misb.append(",\"icon\":\"").append(resolveSymbolHtmlHexCode(icon)).append('"');
                    misb.append('}');
                }
            }
            misb.append("]");
        }

        if (appletDef.isOpenWindow()) {
            misb.append(",\"isOpenWin\":").append(appletDef.isOpenWindow());
            misb.append(",\"winName\":\"").append(appletDef.getName()).append('"');
        }
        misb.append('}');
    }

    private String resolvePath(AppletDef appletDef, WriteParam wparam, boolean isUpdateDraft) throws UnifyException {
        final boolean multipage = wparam.isMultiPage()
                && appletDef.getPropValue(boolean.class, AppletPropertyConstants.PAGE_MULTIPLE);
        final boolean innewwindow = wparam.isOpenInWindow() && appletDef.isSupportOpenInNewWindow();
        final String cpath = multipage
                ? ApplicationPageUtils.constructMultiPageAppletOpenPagePath(
                        isUpdateDraft ? appletDef.getOpenDraftPath() : appletDef.getOpenPath())
                : (isUpdateDraft ? appletDef.getOpenDraftPath() : appletDef.getOpenPath());
        final String tabName = appletDef.getLongName();
        final String path = innewwindow ? ApplicationPageUtils.constructAppletOpenInBrowserWindowPath(
                ApplicationModulePathConstants.APPLICATION_MENU_TO_WINDOW, tabName) : cpath;
        if (innewwindow) {
            RequestOpenTabInfo requestOpenTabInfo = new RequestOpenTabInfo(appletDef.getLabel(), tabName, cpath,
                    multipage);
            setMenuRequestOpenTabInfo(requestOpenTabInfo);
        }

        return path;
    }

    @SuppressWarnings("unchecked")
    protected void setMenuRequestOpenTabInfo(RequestOpenTabInfo requestOpenTabInfo) throws UnifyException {
        Map<String, RequestOpenTabInfo> map = (Map<String, RequestOpenTabInfo>) getSessionAttribute(
                AppletSessionAttributeConstants.MENU_OPEN_TAB_INFO);
        if (map == null) {
            synchronized (this) {
                map = (Map<String, RequestOpenTabInfo>) getSessionAttribute(
                        AppletSessionAttributeConstants.MENU_OPEN_TAB_INFO);
                if (map == null) {
                    map = new HashMap<String, RequestOpenTabInfo>();
                    setSessionAttribute(AppletSessionAttributeConstants.MENU_OPEN_TAB_INFO, map);
                }
            }
        }

        map.put(requestOpenTabInfo.getTabName(), requestOpenTabInfo);
    }

    protected class WriteParam {

        private final boolean multiPage;

        private final boolean openInWindow;

        private boolean appendISym;

        public WriteParam(boolean multiPage, boolean openInWindow) {
            this.multiPage = multiPage;
            this.openInWindow = openInWindow;
        }

        public boolean isMultiPage() {
            return multiPage;
        }

        public boolean isOpenInWindow() {
            return openInWindow;
        }

        public boolean isAppendISym() {
            return appendISym;
        }

        public void setAppendISym(boolean appendISym) {
            this.appendISym = appendISym;
        }
    }
}
