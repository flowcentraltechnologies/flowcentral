/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.web.widgets.FormMessagesWidget;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Form messages writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(FormMessagesWidget.class)
@Component("formmessages-writer")
public class FormMessagesWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FormMessagesWidget frmMessagesWidget = (FormMessagesWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, frmMessagesWidget);
        writer.write(">");
        List<FormMessage> messagesList = frmMessagesWidget.getMessages();
        if (messagesList != null) {
            writer.write("<ul class=\"bdy\">");
            for (FormMessage message : messagesList) {
                writer.write("<li class=\"msg ").write(message.getType().styleClass()).write("\">")
                        .writeWithHtmlEscape(resolveSessionMessage(message.getMessage())).write("</li>");
            }
            writer.write("</ul>");
        }
        writer.write("</div>");
    }

}
