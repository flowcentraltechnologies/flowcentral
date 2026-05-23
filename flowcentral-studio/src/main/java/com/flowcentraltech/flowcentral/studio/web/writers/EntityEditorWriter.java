/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.studio.web.widgets.EntityEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.EntityEditorWidget;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditorWidget;
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
 * Entity editor widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(EntityEditorWidget.class)
@Component("fc-entityeditor-writer")
public class EntityEditorWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final EntityEditorWidget entityEditorWidget = (EntityEditorWidget) widget;
        final EntityEditor entityEditor = entityEditorWidget.getEntityEditor();
        final JsonWriter jsonWriter = new JsonWriter();
        jsonWriter.beginObject();
        writer.write("<div");
        writeTagAttributes(writer, entityEditorWidget);
        writer.write(">");

        if (entityEditor != null) {
            jsonWriter.write("editable", !entityEditor.isReadOnly());

            // Body
            writer.write(
                    "<div style=\"display:table;table-layout: fixed;width:100%;height:100%;\"><div style=\"display:table-row;\">");
            writer.write("<div style=\"display:table-cell;vertical-align:top;position:relative;\">");
            
            // Design
            writer.write("<div class=\"design\">");
            writer.write("<div id=\"").write(entityEditorWidget.getDesignBaseId()).write("\" class=\"designbase\">");
            writer.write("<canvas id=\"").write(entityEditorWidget.getDesignCanvasId())
                    .write("\" class=\"canvas\"></canvas>");
            writer.write("</div>");

            writer.write("</div>");
            // End design
            
            jsonWriter.writeObject("design", entityEditor.getDesign());

            writer.write("</div>");
            writer.write("</div></div>");
            // End body

            // State transfer control
            writer.writeStructureAndContent(entityEditorWidget.getValueCtrl());
        }

        writer.write("</div>");

        jsonWriter.endObject();

        WriteWork work = entityEditorWidget.getWriteWork();
        work.set(FormEditorWidget.WORK_CONTENT, jsonWriter.toString());
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);

        final EntityEditorWidget entityEditorWidget = (EntityEditorWidget) widget;
        final EntityEditor entityEditor = entityEditorWidget.getEntityEditor();
        writer.writeBehavior(entityEditorWidget.getValueCtrl());

        if (entityEditor != null) {
            writer.beginFunction("fuxstudio.rigEntityEditor");
            writer.writeParam("pId", entityEditorWidget.getId());
            writer.writeCommandURLParam("pCmdURL");
            writer.writeParam("pContId", entityEditorWidget.getContainerId());
            writer.writeParam("pDsnBaseId", entityEditorWidget.getDesignBaseId());
            writer.writeParam("pDsnCanvasId", entityEditorWidget.getDesignCanvasId());
            writer.writeParam("pStateId", entityEditorWidget.getValueCtrl().getId());
            writer.writeResolvedParam("pContent",
                    (String) entityEditorWidget.getWriteWork().get(FormEditorWidget.WORK_CONTENT));
            writer.endFunction();
        }
    }
}
