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
package com.flowcentraltech.flowcentral.application.web.writers;

import com.flowcentraltech.flowcentral.application.web.widgets.EntitySearchViewInfo;
import com.flowcentraltech.flowcentral.application.web.widgets.EntitySearchViewWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Entity search view widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(EntitySearchViewWidget.class)
@Component("entitysearchview-writer")
public class EntitySearchViewWriter extends AbstractControlWriter {

    private static final String VIEW_INFO = "viewInfo";

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final EntitySearchViewWidget entitySearchViewWidget = (EntitySearchViewWidget) widget;
        writer.write("<div ");
        writeTagId(writer, entitySearchViewWidget.getBorderId());
        writeTagStyle(writer, entitySearchViewWidget);
        writeTagStyleClass(writer, entitySearchViewWidget);
        writer.write(">");
        writer.write("<div style=\"display:flex;width:100%;\">");

        final EntitySearchViewInfo entitySearchViewInfo = entitySearchViewWidget.getItemEntitySearchViewInfo();
        final WriteWork work = entitySearchViewWidget.getWriteWork();
                
        // Hidden
        writer.write("<input type=\"hidden\"");
        writeTagId(writer, entitySearchViewWidget);
        writeTagName(writer, entitySearchViewWidget);

        if (entitySearchViewInfo.isWithKey()) {
            writer.write(" value=\"").writeWithHtmlEscape(String.valueOf(entitySearchViewInfo.getKey())).write("\"");
        }
        writer.write("/>");
        
        // Facade
        writer.write("<input class=\"tread\" type=\"text\"");
        writeTagId(writer, entitySearchViewWidget.getFacadeId());
        writeTagEditAttributes(writer, entitySearchViewWidget);
        if (entitySearchViewInfo.isWithLabel()) {
            writer.write(" value=\"");
            writer.writeWithHtmlEscape(entitySearchViewInfo.getLabel());
            writer.write("\"");
        }
        
        writer.write(" readonly>");
       
        // Search button
        writer.write("<button tabindex=\"-1\"");
        writeTagId(writer, entitySearchViewWidget.getSearchButtonId());
        writeTagStyleClass(writer, "tpmbutton");
        writer.write(" title=\"").writeWithHtmlEscape(resolveSessionMessage("$m{button.search}"));
        writer.write("\"/>");

        writeFontIcon(writer, "search");
        writer.write("</button>");

        // View button
        writer.write("<button tabindex=\"-1\"");
        writeTagId(writer, entitySearchViewWidget.getViewButtonId());
        writeTagStyleClass(writer, "tpbutton");
        writer.write(" title=\"").writeWithHtmlEscape(resolveSessionMessage("$m{button.view}"));
        writer.write("\"/>");

        writeFontIcon(writer, "file");
        writer.write("</button>");

        writer.write("</div>");
        writer.write("</div>");
        
        work.set(VIEW_INFO, entitySearchViewInfo);
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        final EntitySearchViewWidget entitySearchViewWidget = (EntitySearchViewWidget) widget;
        super.doWriteBehavior(writer, entitySearchViewWidget, handlers);
        final WriteWork work = entitySearchViewWidget.getWriteWork();
        final EntitySearchViewInfo entitySearchViewInfo = work.get(EntitySearchViewInfo.class, VIEW_INFO);

        writer.beginFunction("fux.rigEntitySearchView");
        writer.writeParam("pId", entitySearchViewWidget.getId());
        writer.writeParam("pFld", entitySearchViewWidget.getBinding());
        writer.writeParam("pSchBtnId", entitySearchViewWidget.getSearchButtonId());
        writer.writeParam("pViwBtnId", entitySearchViewWidget.getViewButtonId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", entitySearchViewWidget.getContainerId());
        writer.writeParam("pOn",
                entitySearchViewWidget.isContainerEditable() && !entitySearchViewWidget.isContainerDisabled());
        if (!StringUtils.isBlank(entitySearchViewInfo.getApplet())) {
            writer.writeParam("pApl", entitySearchViewInfo.getApplet());
        }

        writer.writeParam("pItemId",
                entitySearchViewInfo.isWithKey() ? String.valueOf(entitySearchViewInfo.getKey()) : "null");
        writer.endFunction();
    }
}
