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

import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditorWidget;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditor.DesignWfEditType;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditor.DesignWfStepType;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditorWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.util.json.JsonWriter;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Workflow editor widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(WorkflowEditorWidget.class)
@Component("fc-workfloweditor-writer")
public class WorkflowEditorWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final WorkflowEditorWidget workflowEditorWidget = (WorkflowEditorWidget) widget;
        final WorkflowEditor workflowEditor = workflowEditorWidget.getWorkflowEditor();
        final JsonWriter jsonWriter = new JsonWriter();
        jsonWriter.beginObject();
        writer.write("<div");
        writeTagAttributes(writer, workflowEditorWidget);
        writer.write(">");
        jsonWriter.write("editable", !workflowEditor.isReadOnly());
        jsonWriter.beginArray("editTypes");
        for (DesignWfEditType designWfEditType : workflowEditor.getEditTypeList()) {
            jsonWriter.writeObject(designWfEditType);
        }
        jsonWriter.endArray();

        jsonWriter.beginArray("stepTypes");
        for (DesignWfStepType designWfStepType : workflowEditor.getStepTypeList()) {
            jsonWriter.writeObject(designWfStepType);
        }
        jsonWriter.endArray();

        // Body
        writer.write(
                "<div style=\"display:table;table-layout: fixed;width:100%;height:100%;\"><div style=\"display:table-row;\">");
        // Design
        writer.write("<div style=\"display:table-cell;vertical-align:top;\">");
        writer.write("<div class=\"design\" style=\"display:block;overflow:scroll;\">");
        writer.write("<div id=\"").write(workflowEditorWidget.getDesignBaseId()).write("\" class=\"designbase\">");
        writer.write("<canvas id=\"").write(workflowEditorWidget.getDesignCanvasId())
                .write("\" class=\"canvas\"></canvas>");
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</div>");
        jsonWriter.writeObject("design", workflowEditor.getDesign());
        // End design

        // Tools
        writer.write("<div class=\"tools\" style=\"display:table-cell;vertical-align:top;width:")
                .write(workflowEditorWidget.getChoiceWidth()).write(";\">");
        writer.write("<div class=\"bdy\" id=\"").write(workflowEditorWidget.getToolBaseId()).write("\">");
        writer.write("<div class=\"hdr\">").write(getSessionMessage("workfloweditor.components")).write("</div>");
        jsonWriter.beginArray("tools");
        int i = 0;
        for (WorkflowStepType workflowStepType : WorkflowStepType.asList()) {
            if (workflowStepType.isOptional()) {
                final String label = getListItemByKey(LocaleType.SESSION, "wfsteptypelist", workflowStepType.code())
                        .getListDescription();
                writer.write("<div class=\"item\" id=\"").write(workflowEditorWidget.getChoiceId()).write(i)
                        .write("\">");
                writeFontIcon(writer, "symcat", "workflowStepType.icon()");
                writer.write("<span class=\"labelcat\">").writeWithHtmlEscape(label).write("</span>");
                writer.write("</div>");

                jsonWriter.beginObject();
                jsonWriter.write("typeNm", workflowStepType.toString().toLowerCase());
                jsonWriter.endObject();

                i++;
            }
        }
        jsonWriter.endArray();
        writer.write("</div></div>");
        // End tools

        writer.write("</div></div>");
        // End body

        // State transfer control
        writer.writeStructureAndContent(workflowEditorWidget.getValueCtrl());
        writer.writeStructureAndContent(workflowEditorWidget.getEditStepCtrl());
        writer.writeStructureAndContent(workflowEditorWidget.getEditTypeCtrl());
        writer.writeStructureAndContent(workflowEditorWidget.getCreateTypeCtrl());
        writer.writeStructureAndContent(workflowEditorWidget.getCreateXCtrl());
        writer.writeStructureAndContent(workflowEditorWidget.getCreateYCtrl());

        writer.write("</div>");
        jsonWriter.endObject();

        WriteWork work = workflowEditorWidget.getWriteWork();
        work.set(FormEditorWidget.WORK_CONTENT, jsonWriter.toString());
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);

        final WorkflowEditorWidget workflowEditorWidget = (WorkflowEditorWidget) widget;
        writer.writeBehavior(workflowEditorWidget.getValueCtrl());

        writer.beginFunction("fuxstudio.rigWorkflowEditor");
        writer.writeParam("pId", workflowEditorWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", workflowEditorWidget.getContainerId());
        writer.writeParam("pDsnBaseId", workflowEditorWidget.getDesignBaseId());
        writer.writeParam("pDsnCanvasId", workflowEditorWidget.getDesignCanvasId());
        writer.writeParam("pToolBaseId", workflowEditorWidget.getToolBaseId());
        writer.writeParam("pChoiceId", workflowEditorWidget.getChoiceId());
        writer.writeParam("pStateId", workflowEditorWidget.getValueCtrl().getId());
        writer.writeParam("pEditStepId", workflowEditorWidget.getEditStepCtrl().getId());
        writer.writeParam("pEditTypeId", workflowEditorWidget.getEditTypeCtrl().getId());
        writer.writeParam("pNewTypeId", workflowEditorWidget.getCreateTypeCtrl().getId());
        writer.writeParam("pNewXId", workflowEditorWidget.getCreateXCtrl().getId());
        writer.writeParam("pNewYId", workflowEditorWidget.getCreateYCtrl().getId());
        writer.writeResolvedParam("pContent",
                (String) workflowEditorWidget.getWriteWork().get(FormEditorWidget.WORK_CONTENT));
        writer.endFunction();
    }
}
