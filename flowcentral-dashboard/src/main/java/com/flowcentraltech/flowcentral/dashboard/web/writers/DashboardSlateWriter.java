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

package com.flowcentraltech.flowcentral.dashboard.web.writers;

import java.util.List;

import com.flowcentraltech.flowcentral.dashboard.web.widgets.DashboardSlateWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
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

        int sections = dashboardSlateWidget.getTileList().size();
        for (int i = 0; i < sections; i++) {
            writer.write("<div class=\"section\"><div class=\"sectionrow\">");
            List<Widget> list = dashboardSlateWidget.getTileList().get(i);
            int tiles = list.size();
            int tileWidth = tiles > 0 ? 100 / tiles : 100;
            for (int j = 0; j < tiles; j++) {
                writer.write("<div class=\"sectioncell\" style=\"width:").write(tileWidth)
                        .write("%;\"><div class=\"tile\">");
                writer.writeStructureAndContent(list.get(j));
                writer.write("</div></div>");
            }

            writer.write("</div></div>");
        }

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        DashboardSlateWidget dashboardSlateWidget = (DashboardSlateWidget) widget;
        for (List<Widget> list : dashboardSlateWidget.getTileList()) {
            for (Widget tileWidget : list) {
                writer.writeBehavior(tileWidget);
            }
        }
    }
}
