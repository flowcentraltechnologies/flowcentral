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

import com.flowcentraltech.flowcentral.application.web.widgets.AssignmentPageWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Assignment page widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(AssignmentPageWidget.class)
@Component("fc-assignmentpage-writer")
public class AssignmentPageWidgetWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        AssignmentPageWidget assignmentPageWidget = (AssignmentPageWidget) widget;
        assignmentPageWidget.getAssignmentBoxCtrl().setEditable( assignmentPageWidget.isContainerEditable());
        writer.write("<div");
        writeTagAttributes(writer, assignmentPageWidget);
        writer.write(">");
        writer.writeStructureAndContent(assignmentPageWidget.getAssignmentBoxCtrl());
        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        AssignmentPageWidget assignmentPageWidget = (AssignmentPageWidget) widget;
        Control control = assignmentPageWidget.getAssignmentBoxCtrl();
        control.setEditable(assignmentPageWidget.isContainerEditable());
        writer.writeBehavior(control);
        addPageAlias(assignmentPageWidget.getId(), control);
    }

}
