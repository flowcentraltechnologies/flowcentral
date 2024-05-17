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

import com.flowcentraltech.flowcentral.application.web.widgets.CollapsibleInfo;
import com.flowcentraltech.flowcentral.application.web.widgets.CollapsibleTableWidget;
import com.flowcentraltech.flowcentral.application.web.widgets.CollapsibleInfo.Column;
import com.flowcentraltech.flowcentral.application.web.widgets.CollapsibleInfo.Row;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Collapsible table widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(CollapsibleTableWidget.class)
@Component("fc-collapsibletable-writer")
public class CollapsibleTableWidgetWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        CollapsibleTableWidget collapsibleTableWidget = (CollapsibleTableWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, collapsibleTableWidget);
        writer.write(">");

        final String id = collapsibleTableWidget.getId();
        // Action
        writer.write("<div class=\"actb\" style=\"width:100%;\">");
        writer.write("<span id=\"");
        writer.write(id);
        writer.write("_exp\" class=\"act\">");
        writer.write(resolveSessionMessage("$m{button.expandall}"));
        writer.write("</span>");
        writer.write("<span id=\"");
        writer.write(id);
        writer.write("_col\" class=\"act\">");
        writer.write(resolveSessionMessage("$m{button.collapseall}"));
        writer.write("</span>");
        writer.write("</div>");
        
        CollapsibleInfo info = collapsibleTableWidget.getCollapsibleInfo();
        if (info != null) {            
            // Body
            writer.write("<div style=\"width:100%;\">");

            final Formats.Instance formatsInstance = Formats.DEFAULT.createInstance();
            final List<Column> columns = info.getColumns();

            final int numberOfLevels = info.getNumberOfLevels();
            final int numberOfColumns = info.getNumberOfColumns();

            writer.write("<table class=\"bdy\" style=\"table-layout:fixed;width:100%;\">");
            writer.write("<colgroup>");
            // Controls
            for (int i = 0; i < numberOfLevels; i++) {
                writer.write("<col class=\"ctrl\"/>");
            }

            // Columns
            for (int i = 0; i < numberOfColumns; i++) {
                writer.write("<col style=\"width:");
                writer.write(columns.get(i).getWidthInPercent()).write("%;\"/>");
            }

            writer.write("</colgroup>");

            int i = 0;
            int currVisibleDepth = 0;
            for (Row row : info.getRows()) {
                final String cid = id + "_cl" + i;
                final boolean expandable = row.isExpandable();
                if (row.isRise(currVisibleDepth)) {
                    currVisibleDepth = row.getDepth();
                }

                if (row.isVisible(currVisibleDepth)) {
                    final boolean expanded = row.isExpanded();
                    writer.write("<tr class=\"");
                    writer.write(expandable && currVisibleDepth == 0 ? "odd" : "even");
                    writer.write("\">");
                    // Controls
                    for (int j = 0; j <= currVisibleDepth; j++) {
                        if (j == 0) {
                            writer.write("<td class=\"ctrlr\">");
                        } else {
                            writer.write("<td class=\"ctrlb\">");
                        }

                        if (expandable && (currVisibleDepth == j)) {
                            writer.write("<span id=\"").write(cid).write("\" class=\"icon g_fsm\">");
                            if (expanded) {
                                writer.write(resolveSymbolHtmlHexCode("caret-down"));
                            } else {
                                writer.write(resolveSymbolHtmlHexCode("caret-right"));
                            }

                            writer.write("</span>");
                        }

                        writer.write("</td>");
                    }

                    final int colspan = numberOfLevels - currVisibleDepth;
                    // Columns
                    for (int j = 0; j < numberOfColumns; j++) {
                        Column column = columns.get(j);
                        String val = formatsInstance
                                .format(DataUtils.getNestedBeanProperty(row.getData(), column.getFieldName()));
                        writer.write("<td class=\"dat ");
                        writer.write(column.getAlign().styleClass());
                        writer.write("\"");
                        if (j == 0) {
                            writer.write(" colspan=\"").write(colspan).write("\"");
                        }
                        writer.write(">");
                        if (val != null) {
                            writer.writeWithHtmlEscape(val);
                        }
                        
                        writer.write("</td>");
                    }

                    writer.write("</tr>");

                    if (expandable && expanded) {
                        currVisibleDepth++;
                    }
                }

                i++;
            }

            writer.write("</table>");
            
            writer.write("</div>");
        }

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        CollapsibleTableWidget collapsibleTableWidget = (CollapsibleTableWidget) widget;
        CollapsibleInfo info = collapsibleTableWidget.getCollapsibleInfo();
        if (info != null) {
            writer.beginFunction("fux.rigCollapsibleTable");
            writer.writeParam("pId", collapsibleTableWidget.getId());
            writer.writeCommandURLParam("pCmdURL");
            writer.writeParam("pContId", collapsibleTableWidget.getContainerId());
            writer.writeParam("pRows", info.getNumberOfRows());
            writer.endFunction();
        }
    }

}
