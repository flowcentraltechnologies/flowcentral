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

package com.flowcentraltech.flowcentral.dashboard.web.writers;

import com.flowcentraltech.flowcentral.dashboard.web.widgets.DashboardSlateWidget;
import com.flowcentraltech.flowcentral.dashboard.web.widgets.DashboardSlateWidget.DashboardSlot;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Dashboard slate writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(DashboardSlateWidget.class)
@Component("fc-dashboardslate-writer")
public class DashboardSlateWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        DashboardSlateWidget dashboardSlateWidget = (DashboardSlateWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, dashboardSlateWidget);
        writer.write(">");

        final int sections = dashboardSlateWidget.getSections();
        for (int sectionIndex = 0; sectionIndex < sections; sectionIndex++) {
            final int preferredSectionHeight = dashboardSlateWidget.getSectionPreferredHeight(sectionIndex);
            writer.write("<div class=\"section\"><div class=\"sectionrow\">");
            for (DashboardSlot slot : dashboardSlateWidget.getSectionSlots(sectionIndex)) {
                writer.write("<div class=\"sectioncell\" style=\"width:").write(slot.getWidth());
                writer.write(";");
                if (preferredSectionHeight > 0) {
                    writer.write("height:").write(preferredSectionHeight).write("px;");
                }
                writer.write("\"><div class=\"tile\">");
                if (!slot.isEmpty()) {
                    writer.writeStructureAndContent(slot.getWidget());
                }
                writer.write("</div></div>");
            }

            writer.write("</div></div>");
        }

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        DashboardSlateWidget dashboardSlateWidget = (DashboardSlateWidget) widget;
        final int sections = dashboardSlateWidget.getSections();
        for (int sectionIndex = 0; sectionIndex < sections; sectionIndex++) {
            for (DashboardSlot slot : dashboardSlateWidget.getSectionSlots(sectionIndex)) {
                if (!slot.isEmpty()) {
                    writer.writeBehavior(slot.getWidget());
                }
            }
        }

    }
}
