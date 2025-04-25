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
package com.flowcentraltech.flowcentral.common.web.writers;

import java.util.List;

import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.web.util.WidgetWriterUtils;
import com.flowcentraltech.flowcentral.common.web.util.WidgetWriterUtils.ColumnRenderInfo;
import com.flowcentraltech.flowcentral.common.web.widgets.Form;
import com.flowcentraltech.flowcentral.common.web.widgets.Form.FormSection;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Container;
import com.tcdng.unify.web.ui.widget.PushType;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractContainerWriter;

/**
 * Form writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(Form.class)
@Component("fc-form-writer")
public class FormWriter extends AbstractContainerWriter {

    @Override
    protected void writeLayoutContent(ResponseWriter writer, Container container) throws UnifyException {
        Form form = (Form) container;
        form.cascadeValueStore();
        String groupId = null;
        if (form.isContainerEditable()) {
            groupId = form.getDataGroupId();
        }

        final String caption = form.getCaption();
        if (!StringUtils.isBlank(caption)) {
            writer.write("<div class=\"fcaption\"><span>").writeWithHtmlEscape(caption).write("</span></div>");
        }

        boolean isFormDisabled = form.isDisabled();
        boolean isFormEditable = form.isEditable();
        for (FormSection formSection : form.getSections()) {
            if (formSection.isVisible()) {
                form.setDisabled(isFormDisabled || formSection.isDisabled());
                form.setEditable(isFormEditable && formSection.isEditable());

                if (formSection.isBinding()) {
                    if (formSection.isBindingValueList()) {
                        for (ValueStore valueStore : formSection.getValueStoreList()) {
                            writeSectionStructureAndContent(writer, form, formSection, valueStore, groupId);
                        }
                    } else {
                        writeSectionStructureAndContent(writer, form, formSection, formSection.getValueStore(), null);
                    }
                } else {
                    writeSectionStructureAndContent(writer, form, formSection, null, null);
                }

                form.setEditable(isFormEditable);
                form.setDisabled(isFormDisabled);
            }
        }

        if (form.isContainerEditable()) {
            writeHiddenPush(writer, form.getDataGroupId(), PushType.GROUP);
        }
    }

    @Override
    protected void writeContainedWidgetsBehavior(ResponseWriter writer, Container container) throws UnifyException {
        Form form = (Form) container;
        for (FormSection formSection : form.getSections()) {
            if (formSection.isVisible()) {
                if (formSection.isBinding()) {
                    if (formSection.isBindingValueList()) {
                        for (ValueStore valueStore : formSection.getValueStoreList()) {
                            writeSectionBehavior(writer, form, formSection, valueStore);
                        }
                    } else {
                        writeSectionBehavior(writer, form, formSection, formSection.getValueStore());
                    }
                } else {
                    writeSectionBehavior(writer, form, formSection, null);
                }
            }
        }
    }

    private void writeSectionBehavior(ResponseWriter writer, Form form, FormSection formSection, ValueStore valueStore)
            throws UnifyException {
        final int columns = form.getColumns();
        for (int i = 0; i < columns; i++) {
            for (Widget widget : formSection.getWidgetList(i)) {
                if (widget.isVisible() || widget.isHidden()) {
                    if (valueStore != null) {
                        widget.setValueStore(valueStore);
                    }

                    writer.writeBehavior(widget);
                }
            }
        }
    }

    private void writeSectionStructureAndContent(ResponseWriter writer, Form form, FormSection formSection,
            ValueStore valueStore, String groupId) throws UnifyException {
        FormValidationErrors errors = form.getFormValidationErrors();
        writer.write("<div class=\"ftable\">");
        writer.write("<div class=\"frow\">");
        final int columns = form.getColumns();
        List<ColumnRenderInfo> columnInfos = WidgetWriterUtils.getColumnRenderInfoList(columns);
        for (int i = 0; i < columns; i++) {
            writer.write("<div class=\"fcol\" ").write(columnInfos.get(i).getColumnStyle()).write(">");
            for (Widget widget : formSection.getWidgetList(i)) {
                writeFieldCell(writer, errors, widget);
            }
            writer.write("</div>");
        }
        writer.write("</div>");
        writer.write("</div>");
    }

    private void writeFieldCell(ResponseWriter writer, FormValidationErrors errors, Widget widget)
            throws UnifyException {
        if (widget.isVisible()) {
            writer.write("<div class=\"ffield\">");
            writer.write("<div class=\"ffieldrow\">");

            writer.write("<div class=\"fpre\">");
            writer.write("<div class=\"flabel\">");
            writer.write("<span>");
            if (widget.isUplAttribute("required") && widget.getUplAttribute(boolean.class, "required")) {
                writer.write("<img class=\"freq\"src=\"");
                if (StringUtils.isBlank(widget.getValue(String.class))) {
                    writer.writeFileImageContextURL("$t{images/red_asterix.png}");
                } else {
                    writer.writeFileImageContextURL("$t{images/gray_asterix.png}");
                }
                writer.write("\"/>");
            }

            if (widget.isLayoutCaption()) {
                writer.writeWithHtmlEscape(widget.getCaption());
            }

            writer.write("</span>");
            writer.write("</div>");
            writer.write("</div>");

            writer.write("<div class=\"fmid\">");
            writer.write("<div class=\"fcon\">");
            writer.write("<div class=\"fcontent\">");
            writer.writeStructureAndContent(widget);
            if (errors != null && errors.isWithFieldError(widget.getBinding())) {
                for (String msg : errors.getFieldError(widget.getBinding())) {
                    writer.write("<div><span class=\"errmsg\">").write(resolveSessionMessage(msg)).write("</span></div>");
                }
            }

            writer.write("</div>");
            writer.write("</div>");
            writer.write("</div>");

            writer.write("<div class=\"fpost").write("\">");
            writer.write("</div>");

            writer.write("</div>");
            writer.write("</div>");
        }
    }
}
