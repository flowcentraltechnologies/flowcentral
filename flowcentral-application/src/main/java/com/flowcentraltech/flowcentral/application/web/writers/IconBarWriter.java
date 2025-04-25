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

import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.IconBar;
import com.flowcentraltech.flowcentral.application.web.widgets.IconBarWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Icon bar writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(IconBarWidget.class)
@Component("iconbar-writer")
public class IconBarWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        IconBarWidget iconBarWidget = (IconBarWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, iconBarWidget);
        writer.write(">");
        writer.write("<div style=\"display:table;\">");
        IconBar iconBar = iconBarWidget.getIconBar();
        if (iconBar != null) {
            final boolean vertical = iconBarWidget.isVertical();
            final boolean serialNo = iconBarWidget.isSerialNo();
            final List<IconBar.Item> items = iconBar.getItems();
            final int len = items.size();
            final int selected = iconBar.getSelected();
            if (iconBarWidget.isVertical()) {
                for (int i = 0; i < len; i++) {
                    writer.write("<div style=\"display:table-row;\">");
                    writeItem(writer, items.get(i), i, serialNo, vertical, i == selected);
                    writer.write("</div>");
                }
            } else {
                writer.write("<div style=\"display:table-row;\">");
                for (int i = 0; i < len; i++) {
                    writeItem(writer, items.get(i), i, serialNo, vertical, i == selected);
                }
                writer.write("</div>");
            }
        }
        writer.write("</div>");
        writer.write("</div>");
    }

    private void writeItem(ResponseWriter writer, IconBar.Item item, int index, boolean serialNo, boolean vertical,
            boolean active) throws UnifyException {
        writer.write("<div class=\"");
        writer.write(active ? "ibact" : "ibict");
        writer.write(" g_fsm\" style=\"display:table-cell;\">");
        writer.write("<div class=\"ibitem");
        if (active) {
            writer.write(vertical ? " ibvert" : " ibhoriz");
        }
        writer.write("\">");
        writer.write("<span class=\"ibiconcat\">").write(resolveSymbolHtmlHexCode(item.getIcon())).write("</span>");
        final String label = serialNo
                ? resolveSessionMessage("$m{iconbar.serialno.label}", index + 1, resolveSessionMessage(item.getLabel()))
                : resolveSessionMessage(item.getLabel());
        writer.write("<span class=\"iblabelcat\">").writeWithHtmlEscape(label).write("</span>");
        writer.write("</div>");
        writer.write("</div>");
    }
}
