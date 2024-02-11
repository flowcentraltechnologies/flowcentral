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

import com.flowcentraltech.flowcentral.application.data.FormAnnotationDef;
import com.flowcentraltech.flowcentral.application.web.widgets.FormAnnotationWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Form annotation writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(FormAnnotationWidget.class)
@Component("formannotation-writer")
public class FormAnnotationWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FormAnnotationWidget frmAnnotationWidget = (FormAnnotationWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, frmAnnotationWidget);
        writer.write(">");
        List<FormAnnotationDef> formAnnotationDefList = frmAnnotationWidget.getFormAnnotationDef();
        if (!DataUtils.isBlank(formAnnotationDefList)) {
            for (FormAnnotationDef frmAnnotationDef : formAnnotationDefList) {
                writer.write("<span class=\"");
                writer.write(frmAnnotationDef.getType().styleClass());
                writer.write("\">");
                if (frmAnnotationDef.isHtml()) {
                    writer.write(frmAnnotationDef.getMessage());
                } else {
                    writer.writeWithHtmlEscape(frmAnnotationDef.getMessage());
                }
                writer.write("</span>");
            }
        }
        writer.write("</div>");
    }

}
