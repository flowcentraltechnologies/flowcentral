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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.constants.DataChangeType;
import com.flowcentraltech.flowcentral.application.data.Diff;
import com.flowcentraltech.flowcentral.application.data.DiffEntity;
import com.flowcentraltech.flowcentral.application.data.DiffEntityField;
import com.flowcentraltech.flowcentral.application.web.widgets.FormDiffWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
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

    private Map<DataChangeType, String> hints;

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
        final String style = depth > 0 ? " style=\"padding-right:" + depth * 10 + "px;\"" : null;
        writer.write("<div class=\"diff\">");
        writer.write("<div class=\"diffrow\">");

        writer.write("<div class=\"diffcell\"");
        if (style != null) {
            writer.write(style);
        }

        writer.write(">");
        writeDiff(writer, diff.getLeft(), depth, false);
        writer.write("</div>");

        writer.write("<div class=\"diffcell\"");
        if (style != null) {
            writer.write(style);
        }

        writer.write(">");
        writeDiff(writer, diff.getRight(), depth, true);
        writer.write("</div>");

        writer.write("</div>");
        writer.write("</div>");

        ++depth;
        for (Diff cdiff : diff.getChildren()) {
            writeDiff(writer, cdiff, depth);
        }
    }

    private void writeDiff(ResponseWriter writer, DiffEntity diffEntity, int depth, boolean original)
            throws UnifyException {
        if (!DataUtils.isBlank(diffEntity.getFields())) {
            if (!StringUtils.isBlank(diffEntity.getLabel())) {
                if (depth == 0) {
                    writer.write("<div class=\"head\">");
                } else {
                    writer.write("<div class=\"headtab\">");
                }

                writer.writeWithHtmlEscape(diffEntity.getLabel());
                writer.write("</div>");
            }

            writer.write("<div class=\"diff");
            if (original) {
                writer.write(" orn");
            }

            writer.write("\">");
            for (DiffEntityField field : diffEntity.getFields()) {
                writer.write("<div class=\"diffrow\">");

                writer.write("<div class=\"difflbl\">");
                writer.writeWithHtmlEscape(field.getLabel());
                writer.write(":");
                writer.write("</div>");

                if (field.isNumber()) {
                    writer.write("<div class=\"diffvaln");
                } else {
                    writer.write("<div class=\"diffval");
                }

                if (!field.getChangeType().isNone()) {
                    writer.write(" ");
                    writer.write(field.getChangeType().shade());
                }

                if (!field.getChangeType().isNone()) {
                    writer.write("\" title=\"").writeWithHtmlEscape(getHint(field.getChangeType()));
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

    private String getHint(DataChangeType changeType) throws UnifyException {
        if (hints == null) {
            synchronized (this) {
                if (hints == null) {
                    hints = new HashMap<DataChangeType, String>();
                    hints.put(DataChangeType.NEW, resolveApplicationMessage(DataChangeType.NEW.hint()));
                    hints.put(DataChangeType.UPDATED, resolveApplicationMessage(DataChangeType.UPDATED.hint()));
                    hints.put(DataChangeType.DELETED, resolveApplicationMessage(DataChangeType.DELETED.hint()));
                    hints.put(DataChangeType.NONE, resolveApplicationMessage(DataChangeType.NONE.hint()));
                    hints = Collections.unmodifiableMap(hints);
                }
            }
        }

        return hints.get(changeType);
    }
}
