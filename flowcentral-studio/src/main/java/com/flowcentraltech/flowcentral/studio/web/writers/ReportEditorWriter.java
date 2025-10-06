/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.studio.web.writers;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.studio.web.widgets.ReportEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.ReportEditorWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.json.JsonWriter;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Report editor widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(ReportEditorWidget.class)
@Component("fc-reporteditor-writer")
public class ReportEditorWriter extends AbstractControlWriter {

    private static final String[] COLUMN_PROPERTY_KEYS = { "reporteditor.field",
            "reporteditor.order", "reporteditor.widget", "reporteditor.horizalign", "reporteditor.vertalign",
            "reporteditor.description", "reporteditor.formatter", "reporteditor.width",
            "reporteditor.bold", "reporteditor.group", "reporteditor.grouponnewpage", "reporteditor.sum" };

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final ReportEditorWidget reportEditorWidget = (ReportEditorWidget) widget;
        final ReportEditor reportEditor = reportEditorWidget.getReportEditor();
        final JsonWriter jsonWriter = new JsonWriter();
        jsonWriter.beginObject();
        writer.write("<div");
        writeTagAttributes(writer, reportEditorWidget);
        writer.write(">");

        jsonWriter.write("downarrow", resolveSymbolHtmlHexCode("arrow-down"));
        jsonWriter.write("plus", resolveSymbolHtmlHexCode("plus"));
        jsonWriter.write("cog", resolveSymbolHtmlHexCode("cog"));
        jsonWriter.write("cross", resolveSymbolHtmlHexCode("cross"));
        jsonWriter.write("none", getSessionMessage("reporteditor.none"));
        jsonWriter.write("placecolumn", getSessionMessage("reporteditor.placeherecolumn"));
        jsonWriter.write("addcolumn", getSessionMessage("reporteditor.addreportcolumn"));
        jsonWriter.write("editable", !reportEditor.isReadOnly());

        // Column property labels
        String[] labels = getSessionMessages(COLUMN_PROPERTY_KEYS);
        jsonWriter.write("propLabels", labels);

        // Body
        writer.write(
                "<div style=\"display:table;table-layout: fixed;width:100%;height:100%;\"><div style=\"display:table-row;\">");
        // Fields
        writer.write("<div class=\"fields\" style=\"display:table-cell;vertical-align:top;width:")
                .write(reportEditorWidget.getChoiceWidth()).write(";\">");
        writer.write("<div class=\"bdy\" id=\"").write(reportEditorWidget.getFieldBaseId()).write("\">");
        writer.write("<div class=\"hdr\">").write(getSessionMessage("reporteditor.availablefields")).write("</div>");
        jsonWriter.beginArray("fields");
        final EntityDef entityDef = reportEditor.getEntityDef();
        int i = 0;
        for (EntityFieldDef entityFieldDef : entityDef.getSortedFieldDefList()) {
            if (entityFieldDef.isTableViewable() && entityDef.isNotDelegateListOnly(entityFieldDef.getFieldName())) {
                writer.write("<div class=\"fld\" id=\"").write(reportEditorWidget.getChoiceId()).write(i)
                        .write("\"><span>");
                String fieldLabel = null;
                if(entityFieldDef.getFieldLabel().contains("$m{")) {
                	fieldLabel = resolveSessionMessage(entityFieldDef.getFieldLabel());
                }else {
                	fieldLabel = entityFieldDef.getFieldLabel();
                }
                writer.writeWithHtmlEscape(fieldLabel);
                writer.write("</span></div>");

                jsonWriter.beginObject();
                jsonWriter.write("fldLabel", entityFieldDef.getFieldLabel());
                jsonWriter.write("fldNm", entityFieldDef.getFieldName());
                if (entityFieldDef.isWithInputWidget()) {
                    jsonWriter.write("fldWidget", entityFieldDef.getInputWidget());
                }
                jsonWriter.endObject();

                i++;
            }
        }
        jsonWriter.endArray();
        writer.write("</div></div>");
        // End fields

        // Design
        writer.write("<div class=\"design\" style=\"display:table-cell;vertical-align:top;\">");
        writer.write("<div id=\"").write(reportEditorWidget.getDesignBaseId()).write("\" class=\"designbase\">");
        writer.write("</div></div>");
        jsonWriter.writeObject("design", reportEditor.getDesign());
        // End design

        writer.write("</div></div>");
        // End body

        // State transfer control
        writer.writeStructureAndContent(reportEditorWidget.getValueCtrl());

        writer.write("</div>");
        jsonWriter.endObject();

        WriteWork work = reportEditorWidget.getWriteWork();
        work.set(ReportEditorWidget.WORK_CONTENT, jsonWriter.toString());
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);

        final ReportEditorWidget reportEditorWidget = (ReportEditorWidget) widget;
        writer.writeBehavior(reportEditorWidget.getValueCtrl());

        writer.beginFunction("fuxstudio.rigReportEditor");
        writer.writeParam("pId", reportEditorWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", reportEditorWidget.getContainerId());
        writer.writeParam("pFieldBaseId", reportEditorWidget.getFieldBaseId());
        writer.writeParam("pDsnBaseId", reportEditorWidget.getDesignBaseId());
        writer.writeParam("pChoiceId", reportEditorWidget.getChoiceId());
        writer.writeParam("pStateId", reportEditorWidget.getValueCtrl().getId());
        writer.writeParam("pEditColId", reportEditorWidget.getReportEditor().getEditColumnId());
        writer.writeResolvedParam("pContent",
                (String) reportEditorWidget.getWriteWork().get(ReportEditorWidget.WORK_CONTENT));
        writer.endFunction();
    }
}
