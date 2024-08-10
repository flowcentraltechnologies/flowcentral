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

import com.flowcentraltech.flowcentral.application.data.Diff;
import com.flowcentraltech.flowcentral.application.data.DiffEntity;
import com.flowcentraltech.flowcentral.application.data.DiffEntityField;
import com.flowcentraltech.flowcentral.application.web.widgets.FormDiffWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Form diff writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(FormDiffWidget.class)
@Component("formdiff-writer")
public class FormDiffWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FormDiffWidget frmDiffWidget = (FormDiffWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, frmDiffWidget);
        writer.write(">");

        final Diff diff = frmDiffWidget.getDiff();
        if (diff != null) {
            writeDiff(writer, diff, 0);
        }

        writer.write("</div>");
    }

    private void writeDiff(ResponseWriter writer, Diff diff, int depth) throws UnifyException {
        writer.write("<div class=\"diff\">");
        writer.write("<div class=\"diffrow\">");

        writer.write("<div class=\"diffcell\">");
        writeDiff(writer, diff.getLeft(), depth);
        writer.write("</div>");

        writer.write("<div class=\"diffcell\">");
        writeDiff(writer, diff.getRight(), depth);
        writer.write("</div>");

        writer.write("</div>");
        writer.write("</div>");

        ++depth;
        for (Diff cdiff : diff.getChildren()) {
            writeDiff(writer, cdiff, depth);
        }
    }

    private void writeDiff(ResponseWriter writer, DiffEntity diffEntity, int depth) throws UnifyException {
        writer.write("<div class=\"diff\">");
        for (DiffEntityField field : diffEntity.getFields()) {
            writer.write("<div class=\"diffrow\">");

            writer.write("<div class=\"difflbl\">");
            writer.writeWithHtmlEscape(field.getLabel());
            writer.write(":");
            writer.write("</div>");

            writer.write("<div class=\"diffval");
            if (!field.getChangeType().isNone()) {
                writer.write(" ");
                writer.write(field.getChangeType().shade());
            }
            writer.write("\">");

            if (field.isWithValue()) {
                writer.writeWithHtmlEscape(field.getValue());
            }

            writer.write("</div>");

            writer.write("</div>");
        }

        writer.write("</div>");
    }

}
