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

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.FormAnnotationDef;
import com.flowcentraltech.flowcentral.application.web.widgets.FormAnnotationWidget;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
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

    @Configurable
    private SystemModuleService systemModuleService;

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FormAnnotationWidget frmAnnotationWidget = (FormAnnotationWidget) widget;
        writer.write("<div");
        writeTagAttributesWithTrailingExtraStyleClass(writer, frmAnnotationWidget, "g_fsm");
        writer.write(">");

        List<FormAnnotationDef> formAnnotationDefList = frmAnnotationWidget.getFormAnnotationDef();
        if (!DataUtils.isBlank(formAnnotationDefList)) {
            final String id = frmAnnotationWidget.getId();
            int i = 0;
            for (FormAnnotationDef frmAnnotationDef : formAnnotationDefList) {
                final String nid = id + "_an" + i;
                final String cid = id + "_cl" + i;

                writer.write("<div id=\"").write(nid).write("\" class=\"frm ");
                writer.write(frmAnnotationDef.getType().styleClass());
                writer.write("\" style=\"display:table;table-layout:fixed;width:100%;\">");
                writer.write("<div style=\"display:table-row;\">");

                writer.write("<div style=\"display:table-cell;\">");
                writer.write("<span class=\"msg\">");
                if (frmAnnotationDef.isHtml()) {
                    writer.write(frmAnnotationDef.getMessage());
                } else {
                    writer.writeWithHtmlEscape(frmAnnotationDef.getMessage());
                }

                writer.write("</span>");
                writer.write("</div>");

                if (frmAnnotationDef.isClosable()) {
                    writer.write("<div class=\"cls\" style=\"display:table-cell;\">");
                    writer.write("<span id=\"").write(cid).write("\" class=\"icon\">");
                    writer.write(resolveSymbolHtmlHexCode("cross"));
                    writer.write("</span>");
                    writer.write("</div>");
                }

                writer.write("</div>");
                writer.write("</div>");

                i++;
            }
        }

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        FormAnnotationWidget frmAnnotationWidget = (FormAnnotationWidget) widget;
        final int hideMessageInSeconds = systemModuleService.getSysParameterValue(int.class,
                ApplicationModuleSysParamConstants.MESSAGE_VISIBILITY_TIMEOUT);

        writer.beginFunction("fux.rigFormAnnotation");
        writer.writeParam("pId", frmAnnotationWidget.getId());
        writer.writeParam("pHideSec", hideMessageInSeconds);

        List<FormAnnotationDef> formAnnotationDefList = frmAnnotationWidget.getFormAnnotationDef();
        if (!DataUtils.isBlank(formAnnotationDefList)) {
            final int len = formAnnotationDefList.size();
            final AnnotationFlags[] flags = new AnnotationFlags[len];
            for (int i = 0; i < len; i++) {
                FormAnnotationDef frmAnnotationDef = formAnnotationDefList.get(i);
                flags[i] = new AnnotationFlags(frmAnnotationDef.isDisappearing(), frmAnnotationDef.isClosable());
            }

            writer.writeObjectParam("pAnnot", flags);
        }

        writer.endFunction();
    }

    public static class AnnotationFlags {

        private boolean disp;

        private boolean closable;

        public AnnotationFlags(boolean disp, boolean closable) {
            this.disp = disp;
            this.closable = closable;
        }

        public boolean isDisp() {
            return disp;
        }

        public boolean isClosable() {
            return closable;
        }
    }
}
