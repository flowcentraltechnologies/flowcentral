/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditorWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.json.JsonWriter;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.WriteWork;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Dashboard editor widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(DashboardEditorWidget.class)
@Component("fc-dashboardeditor-writer")
public class DashboardEditorWriter extends AbstractControlWriter {

    private static final String[] SECTION_PROPERTY_KEYS = { "dashboardeditor.section.caption.prefix",
            "dashboardeditor.section.columns" };

    @Configurable
    private ChartModuleService chartModuleService;

    public final void setChartModuleService(ChartModuleService chartModuleService) {
        this.chartModuleService = chartModuleService;
    }

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        final DashboardEditorWidget dashboardEditorWidget = (DashboardEditorWidget) widget;
        final DashboardEditor dashboardEditor = dashboardEditorWidget.getDashboardEditor();
        final JsonWriter jsonWriter = new JsonWriter();
        jsonWriter.beginObject();
        writer.write("<div");
        writeTagAttributes(writer, dashboardEditorWidget);
        writer.write(">");

        jsonWriter.write("downarrow", resolveSymbolHtmlHexCode("arrow-down"));
        jsonWriter.write("plus", resolveSymbolHtmlHexCode("plus"));
        jsonWriter.write("cog", resolveSymbolHtmlHexCode("cog"));
        jsonWriter.write("cross", resolveSymbolHtmlHexCode("cross"));
        jsonWriter.write("addsec", getSessionMessage("dashboardeditor.addsection"));
        jsonWriter.write("placesec", getSessionMessage("dashboardeditor.placeheresec"));
        jsonWriter.write("addtile", getSessionMessage("dashboardeditor.adddashboardtile"));
        jsonWriter.write("sectiontitle", getSessionMessage("dashboardeditor.section"));
        jsonWriter.write("editable", !dashboardEditor.isReadOnly());

        // Column property labels
        String[] labels = getSessionMessages(SECTION_PROPERTY_KEYS);
        jsonWriter.write("sectionLabels", labels);

        // Body
        writer.write(
                "<div style=\"display:table;table-layout: fixed;width:100%;height:100%;\"><div style=\"display:table-row;\">");
        // Charts
        writer.write("<div class=\"charts\" style=\"display:table-cell;vertical-align:top;width:")
                .write(dashboardEditorWidget.getChoiceWidth()).write(";\">");
        writer.write("<div class=\"bdy\" id=\"").write(dashboardEditorWidget.getChartBaseId()).write("\">");
        writer.write("<div class=\"hdr\">").write(getSessionMessage("dashboardeditor.availablecharts")).write("</div>");
        jsonWriter.beginArray("charts");
        int i = 0;
        final String applicationName = (String) getSessionAttribute(
                StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
        for (ChartDef chartDef : chartModuleService.findChartDefs(applicationName)) {
            writer.write("<div class=\"tle\" id=\"").write(dashboardEditorWidget.getChoiceId()).write(i)
                    .write("\"><span>");
            writer.writeWithHtmlEscape(chartDef.getDescription());
            writer.write("</span></div>");

            jsonWriter.beginObject();
            jsonWriter.write("tleLabel", chartDef.getDescription());
            jsonWriter.write("tleNm", chartDef.getName());
            jsonWriter.endObject();

            i++;
        }
        jsonWriter.endArray();
        writer.write("</div></div>");
        // End charts

        // Design
        writer.write("<div class=\"design\" style=\"display:table-cell;vertical-align:top;\">");
        writer.write("<div id=\"").write(dashboardEditorWidget.getDesignBaseId()).write("\" class=\"designbase\">");
        writer.write("</div></div>");
        jsonWriter.writeObject("design", dashboardEditor.getDesign());
        // End design

        writer.write("</div></div>");
        // End body

        // State transfer control
        writer.writeStructureAndContent(dashboardEditorWidget.getValueCtrl());
        writer.writeStructureAndContent(dashboardEditorWidget.getEditSectionCtrl());
        writer.writeStructureAndContent(dashboardEditorWidget.getEditTileCtrl());
        writer.writeStructureAndContent(dashboardEditorWidget.getEditTileIndexCtrl());
        writer.writeStructureAndContent(dashboardEditorWidget.getEditColCtrl());
        writer.writeStructureAndContent(dashboardEditorWidget.getEditModeCtrl());

        writer.write("</div>");
        jsonWriter.endObject();

        WriteWork work = dashboardEditorWidget.getWriteWork();
        work.set(DashboardEditorWidget.WORK_CONTENT, jsonWriter.toString());
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers); 

        final DashboardEditorWidget dashboardEditorWidget = (DashboardEditorWidget) widget;
        writer.writeBehavior(dashboardEditorWidget.getValueCtrl());

        writer.beginFunction("fuxstudio.rigDashboardEditor");
        writer.writeParam("pId", dashboardEditorWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", dashboardEditorWidget.getContainerId());
        writer.writeParam("pDsnBaseId", dashboardEditorWidget.getDesignBaseId());
        writer.writeParam("pChartBaseId", dashboardEditorWidget.getChartBaseId());
        writer.writeParam("pChoiceId", dashboardEditorWidget.getChoiceId());
        writer.writeParam("pStateId", dashboardEditorWidget.getValueCtrl().getId());
        writer.writeParam("pEditSecId", dashboardEditorWidget.getEditSectionCtrl().getId());
        writer.writeParam("pEditTileId", dashboardEditorWidget.getEditTileCtrl().getId());
        writer.writeParam("pEditTileIndexId", dashboardEditorWidget.getEditTileIndexCtrl().getId());
        writer.writeParam("pEditColId", dashboardEditorWidget.getEditColCtrl().getId());
        writer.writeParam("pEditModeId", dashboardEditorWidget.getEditModeCtrl().getId());
        writer.writeResolvedParam("pContent",
                (String) dashboardEditorWidget.getWriteWork().get(DashboardEditorWidget.WORK_CONTENT));
        writer.endFunction();
    }
}
