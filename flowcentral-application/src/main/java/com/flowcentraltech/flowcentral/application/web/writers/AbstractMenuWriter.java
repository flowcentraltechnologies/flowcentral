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
package com.flowcentraltech.flowcentral.application.web.writers;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractMenuWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.writer.AbstractPanelWriter;

/**
 * Convenient abstract base class for menu writers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractMenuWriter extends AbstractPanelWriter {

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
        writer.writeResolvedParam("pMenuIds", (String) work.get(AbstractMenuWidget.WORK_MENUIDS));
        writer.writeResolvedParam("pMenuItems", (String) work.get(AbstractMenuWidget.WORK_MENUITEMS));
        writer.endFunction();
    }

    protected void writeMenuAppletDef(ResponseWriter writer, StringBuilder misb, AppletDef appletDef,
            boolean appendISym, boolean multiPage) throws UnifyException {
        writer.write("<div id=\"item_").write(appletDef.getViewId()).write("\">");
        writeLabelWithIcon(writer, appletDef, false);
        writer.write("</div>");
        writeAppletDefJs(writer, misb, appletDef, appendISym, multiPage, false);
    }

    protected void writeSubMenuAppletDef(ResponseWriter writer, StringBuilder misb, AppletDef appletDef,
            boolean appendISym, boolean multiPage) throws UnifyException {
        writer.write("<li id=\"item_").write(appletDef.getViewId()).write("\">");
        writeLabelWithIcon(writer, appletDef, false);
        writer.write("</li>");
        writeAppletDefJs(writer, misb, appletDef, appendISym, multiPage, false);
    }

    protected void writeOpenDraftSubMenuAppletDef(ResponseWriter writer, StringBuilder misb, AppletDef appletDef,
            boolean appendISym, boolean multiPage) throws UnifyException {
        if (appletDef.isWithOpenDraftPath()) {
            writer.write("<li id=\"item_").write(appletDef.getDraftViewId()).write("\">");
            writeLabelWithIcon(writer, appletDef, true);
            writer.write("</li>");
            writeAppletDefJs(writer, misb, appletDef, appendISym, multiPage, true);
        }
    }

    private void writeLabelWithIcon(ResponseWriter writer, AppletDef appletDef, boolean draft) throws UnifyException {
        writer.write("<span class=\"icon\">");
        final String icon = appletDef.isWithIcon() ? appletDef.getIcon() : "window-maximize";
        writer.write(resolveSymbolHtmlHexCode(icon));
        writer.write("</span>");
        writer.write("<span class=\"acl\">")
                .writeWithHtmlEscape(appletDef.getLabel())
                .write("</span>");
    }

    private void writeAppletDefJs(ResponseWriter writer, StringBuilder misb, AppletDef appletDef, boolean appendISym,
            boolean multiPage, boolean isUpdateDraft) throws UnifyException {
        if (appendISym) {
            misb.append(",");
        }

        misb.append("{\"id\":\"item_").append(isUpdateDraft ? appletDef.getDraftViewId() : appletDef.getViewId())
                .append('"');
        misb.append(",\"path\":\"");
        if (multiPage && appletDef.getPropValue(boolean.class, AppletPropertyConstants.PAGE_MULTIPLE)) {
            writer.writeContextURL(misb, ApplicationPageUtils.constructMultiPageAppletOpenPagePath(
                    isUpdateDraft ? appletDef.getOpenDraftPath() : appletDef.getOpenPath()));
        } else {
            writer.writeContextURL(misb, isUpdateDraft ? appletDef.getOpenDraftPath() : appletDef.getOpenPath());
        }

        misb.append('"');
        if (appletDef.isOpenWindow()) {
            misb.append(",\"isOpenWin\":").append(appletDef.isOpenWindow());
            misb.append(",\"winName\":\"").append(appletDef.getName()).append('"');
        }
        misb.append('}');
    }
}
