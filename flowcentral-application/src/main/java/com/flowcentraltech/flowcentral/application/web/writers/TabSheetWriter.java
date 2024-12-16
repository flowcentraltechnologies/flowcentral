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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.TabDef;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetWidget;
import com.flowcentraltech.flowcentral.common.web.util.WidgetWriterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.constant.MessageType;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Tab sheet widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(TabSheetWidget.class)
@Component("fc-tabsheet-writer")
public class TabSheetWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        logDebug("Writing tab sheet structure [{0}]...", widget.getLongName());
        TabSheetWidget tabSheetWidget = (TabSheetWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, tabSheetWidget);
        writer.write(">");
        writer.write("<div>");
        TabSheet tabSheet = tabSheetWidget.getTabSheet();
        if (tabSheet != null && tabSheet.isInStateForDisplay()) {
            final List<TabDef> tabDefList = tabSheet.getTabDefList();
            final int len = tabDefList.size();
            if (tabSheet.isExpanded()) {
                for (int i = 0; i < len; i++) {
                    if (tabSheet.getTabSheetItem(i).isVisible()) {
                        TabDef tabDef = tabDefList.get(i);
                        writer.write("<div class=\"ttabx\">");
                        writer.write("<div class=\"ttabxc\">");
                        writer.write("<span>");
                        writer.writeWithHtmlEscape(tabDef.getTabLabel());
                        writer.write("</span>");
                        writer.write("</div>");

                        writer.write("<div class=\"ttabxb\">");
                        Widget tabWidget = tabSheetWidget.getTabWidget(i);
                        if (tabWidget != null) {
                            tabWidget.setValueStore(tabSheetWidget.getValueList().get(i));
                            writer.writeStructureAndContent(tabWidget);
                        }
                        writer.write("</div>");
                        writer.write("</div>");
                    }
                }
            } else {
                writer.write("<ul class=\"ttab\">");
                final String tabPrefix = tabSheetWidget.getPrefixedId("tab_");
                int currentIndex = tabSheet.getCurrentTabIndex();
                for (int i = 0; i < len; i++) {
                    if (tabSheet.getTabSheetItem(i).isVisible()) {
                        TabDef tabDef = tabDefList.get(i);
                        logDebug("Writing tab structure [{0}] with label [{1}]...", tabDef.getTabName(),
                                tabDef.getTabLabel());
                        MessageType messageType = tabSheet.getReviewMessageType(tabDef.getTabName());
                        writer.write("<li id=\"").write(tabPrefix).write(i).write("\" class=\"");
                        writer.write(WidgetWriterUtils.getTabClass(i, currentIndex));
                        writer.write("\">");
                        if (messageType != null) {
                            writer.write("<img src=\"");
                            writer.writeFileImageContextURL(messageType.image());
                            writer.write("\">");
                            writer.write("<span class=\"msg\">").writeWithHtmlEscape(tabDef.getTabLabel())
                                    .write("</span>");
                        } else if (tabDef.isErrors()) {
                            writer.write("<span class=\"err\">").writeWithHtmlEscape(tabDef.getTabLabel())
                                    .write("</span>");
                        } else {
                            writer.writeWithHtmlEscape(tabDef.getTabLabel());
                        }

                        writer.write("</li>");
                    }
                }
                writer.write("</ul>");
                writer.write("</div><div class=\"tbody\">");
                Widget tabWidget = tabSheetWidget.getCurrentTabWidget();
                if (tabWidget != null) {
                    tabWidget.setValueStore(tabSheetWidget.getValueList().get(tabSheet.getCurrentTabIndex()));
                    writer.writeStructureAndContent(tabWidget);
                }
            }
        }

        writer.write("</div>");
        writer.write("</div>");
        logDebug("Tab sheet structure [{0}] successfully written.", widget.getLongName());
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        logDebug("Writing tab sheet behavior [{0}]...", widget.getLongName());
        super.doWriteBehavior(writer, widget, handlers);
        TabSheetWidget tabSheetWidget = (TabSheetWidget) widget;

        TabSheet tabSheet = tabSheetWidget.getTabSheet();
        if (tabSheet != null && tabSheet.isInStateForDisplay()) {
            if (tabSheet.isExpanded()) {
                final int len = tabSheet.getTabCount();
                for (int i = 0; i < len; i++) {
                    if (tabSheet.getTabSheetItem(i).isVisible()) {
                        Widget tabWidget = tabSheetWidget.getTabWidget(i);
                        if (tabWidget != null) {
                            tabWidget.setValueStore(tabSheetWidget.getValueList().get(i));
                            writer.writeBehavior(tabWidget);
                            addPageAlias(tabSheetWidget.getId(), tabWidget);
                        }
                    }
                }
            } else {
                if (tabSheet.getTabSheetItem(tabSheet.getCurrentTabIndex()).isVisible()) {
                    Widget tabWidget = tabSheetWidget.getCurrentTabWidget();
                    if (tabWidget != null) {
                        tabWidget.setValueStore(tabSheetWidget.getValueList().get(tabSheet.getCurrentTabIndex()));
                        writer.writeBehavior(tabWidget);
                        addPageAlias(tabSheetWidget.getId(), tabWidget);
                    }
                }
            }
            // Append tab sheet rigging
            writer.beginFunction("fux.rigTabSheet");
            writer.writeParam("pId", tabSheetWidget.getId());
            writer.writeParam("pContId", tabSheetWidget.getContainerId());
            writer.writeCommandURLParam("pCmdURL");
            writer.writeParam("pTabId", tabSheetWidget.getPrefixedId("tab_"));
            writer.writeParam("pTabCount", tabSheet.getTabCount());
            writer.writeParam("pCurrSel", tabSheet.getCurrentTabIndex());
            writer.endFunction();
        }
        logDebug("Tab sheet behavior [{0}] successfully written.", widget.getLongName());
    }
}
