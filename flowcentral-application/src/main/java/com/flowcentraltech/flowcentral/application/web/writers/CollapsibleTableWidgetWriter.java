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

import com.flowcentraltech.flowcentral.application.web.widgets.CollapsibleTable;
import com.flowcentraltech.flowcentral.application.web.widgets.CollapsibleTableWidget;
import com.flowcentraltech.flowcentral.application.web.widgets.CollapsibleTable.Column;
import com.flowcentraltech.flowcentral.application.web.widgets.CollapsibleTable.Row;
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
        writeTagAttributesWithTrailingExtraStyleClass(writer, collapsibleTableWidget, "g_fsm");
        writer.write(">");
        // TODO
        CollapsibleTable table = collapsibleTableWidget.getCollapsibleTable();
        if (table != null) {
            final Formats.Instance formatsInstance = Formats.DEFAULT.createInstance();
            final String id = collapsibleTableWidget.getId();
            final int numberOfLevels = table.getNumberOfLevels();
            final int numberOfColumns = table.getNumberOfColumns();
            writer.write("<table style=\"table-layout:fixed;width:100%;\">");
            writer.write("<colgroup>");
            // Controls
            for (int i = 0; i < numberOfLevels; i++) {
                writer.write("<col class=\"ctrl\"/>");
            }

            // Label
            writer.write("<col class=\"lbl\" style=\"width:");
            writer.write(table.getLabelWidth()).write("%;\"/>");

            // Columns
            for (int i = 0; i < numberOfColumns; i++) {
                writer.write("<col class=\"dat\" style=\"width:");
                writer.write(table.getColumnWidth()).write("%;\"/>");
            }

            writer.write("</colgroup>");

            final List<Column> columns = table.getColumns();
            int i = 0;
            int currVisibleDepth = 0;
            for (Row row : table.getRows()) {
                final String cid = id + "_cl" + i;
                final boolean expandable = row.isExpandable();
                if (expandable && !row.isVisible(currVisibleDepth)) {
                    currVisibleDepth = row.getDepth();
                }
                
                if (row.isVisible(currVisibleDepth)) {
                    final boolean expanded = row.isExpanded();
                    writer.write("<tr>");
                    // Controls
                    for (int j = 0; j < numberOfLevels; j++) {
                        writer.write("<td>");
                        if (expandable && (currVisibleDepth == j)) {
                            writer.write("<span id=\"").write(cid).write("\" class=\"icon\">");
                            if (expanded) {
                                writer.write(resolveSymbolHtmlHexCode("caret-down"));
                            } else {
                                writer.write(resolveSymbolHtmlHexCode("caret-right"));
                            }

                            writer.write("</span>");
                        }

                        writer.write("</td>");
                    }

                    // Label
                    writer.write("<td>");
                    writer.writeWithHtmlEscape(row.getLabel());
                    writer.write("</td>");

                    // Columns
                    for (int j = 0; j < numberOfColumns; j++) {
                        Column column = columns.get(j);
                        String val = formatsInstance
                                .format(DataUtils.getNestedBeanProperty(Object.class, column.getFieldName()));                       
                        writer.write("<td class=\"");
                        writer.write(column.getAlign().styleClass());
                        writer.write("\">");
                        writer.write(val);
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
        }

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        // TODO
    }

}
