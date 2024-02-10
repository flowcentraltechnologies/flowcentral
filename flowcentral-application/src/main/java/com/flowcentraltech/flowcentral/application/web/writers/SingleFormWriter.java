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

import com.flowcentraltech.flowcentral.application.web.panels.SingleFormPanel;
import com.flowcentraltech.flowcentral.application.web.widgets.SingleFormWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Single for widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(SingleFormWidget.class)
@Component("fc-singleform-writer")
public class SingleFormWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        SingleFormWidget singleFormWidget = (SingleFormWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, singleFormWidget);
        writer.write(">");
        writer.write("<div>");
        writer.write("<div class=\"sfbody\">");
        SingleFormPanel<?> panel = singleFormWidget.getSingleFormPanel();
        writer.writeStructureAndContent(panel);
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</div>");

    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        SingleFormWidget singleFormWidget = (SingleFormWidget) widget;
        SingleFormPanel<?> singleFormPanel = singleFormWidget.getSingleFormPanel();
        writer.writeBehavior(singleFormPanel);
        addPageAlias(singleFormWidget.getId(), singleFormPanel);
    }
}
