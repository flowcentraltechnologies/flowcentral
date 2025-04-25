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

package com.flowcentraltech.flowcentral.studio.web.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetEventHandler;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.flowcentraltech.flowcentral.report.entities.ReportConfiguration;
import com.flowcentraltech.flowcentral.studio.web.widgets.ReportEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.ReportEditor.ReportColumn;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.constant.VAlignType;
import com.tcdng.unify.core.database.Query;

/**
 * Report editor page.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ReportEditorPage extends AbstractStudioEditorPage implements TabSheetEventHandler {

    private static final int DESIGN_INDEX = 0;

    private static final int PREVIEW_INDEX = 1;

    private final EntityDef entityDef;

    private final Object baseId;

    private TabSheet tabSheet;

    private ReportEditor reportEditor;

    private ReportPreview reportPreview;

    public ReportEditorPage(AppletUtilities au, EntityDef entityDef, Object baseId, BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.entityDef = entityDef;
        this.baseId = baseId;
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public ReportEditor getReportEditor() {
        return reportEditor;
    }

    public ReportPreview getReportPreview() {
        return reportPreview;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public Object getBaseId() {
        return baseId;
    }

    @Override
    public void onChoose(TabSheetItem tabSheetItem) throws UnifyException {
        switch (tabSheetItem.getIndex()) {
            case DESIGN_INDEX:
                break;
            case PREVIEW_INDEX:
                reportPreview.reload();
                break;
            default:
        }
    }

    public void commitDesign() throws UnifyException {
        ReportConfiguration reportConfiguration = getAu().environment().find(ReportConfiguration.class, baseId);
        List<com.flowcentraltech.flowcentral.report.entities.ReportColumn> columnList = Collections.emptyList();
        if (reportEditor.getDesign() != null && reportEditor.getDesign().getColumns() != null) {
            columnList = new ArrayList<com.flowcentraltech.flowcentral.report.entities.ReportColumn>();
            for (ReportColumn editReportColumn : reportEditor.getDesign().getColumns()) {
                com.flowcentraltech.flowcentral.report.entities.ReportColumn reportColumn =
                        new com.flowcentraltech.flowcentral.report.entities.ReportColumn();
                reportColumn.setFieldName(editReportColumn.getFldNm());
                reportColumn.setRenderWidget(editReportColumn.getWidget());
                reportColumn.setColumnOrder(OrderType.fromCode(editReportColumn.getOrder()));
                reportColumn.setHorizAlignType(HAlignType.fromCode(editReportColumn.getHorizAlign()));
                reportColumn.setVertAlignType(VAlignType.fromCode(editReportColumn.getVertAlign()));
                reportColumn.setDescription(editReportColumn.getDescription());
                reportColumn.setFormatter(editReportColumn.getFormatter());
                reportColumn.setWidth(editReportColumn.getWidth());
                reportColumn.setBold(editReportColumn.isBold());
                reportColumn.setGroup(editReportColumn.isGroup());
                reportColumn.setGroupOnNewPage(editReportColumn.isGroupOnNewPage());
                reportColumn.setSum(editReportColumn.isSum());
                columnList.add(reportColumn);
            }
        }

        reportConfiguration.setColumnList(columnList);
        getAu().environment().updateByIdVersion(reportConfiguration);
    }

    public void newEditor() throws UnifyException {
        ReportEditor.Builder teb = ReportEditor.newBuilder(entityDef);
        for (com.flowcentraltech.flowcentral.report.entities.ReportColumn reportColumn : getAu().environment()
                .findAll(Query.of(com.flowcentraltech.flowcentral.report.entities.ReportColumn.class)
                        .addEquals("reportConfigurationId", baseId).addOrder("id"))) {
            teb.addColumn(reportColumn.getFieldName(), reportColumn.getRenderWidget(),
                    reportColumn.getColumnOrder() != null ? reportColumn.getColumnOrder().code() : null,
                    reportColumn.getHorizAlignType() != null ? reportColumn.getHorizAlignType().code() : null,
                    reportColumn.getVertAlignType() != null ? reportColumn.getVertAlignType().code() : null,
                    reportColumn.getDescription(), reportColumn.getFormatter(),
                    reportColumn.getWidth(), reportColumn.isBold(), reportColumn.isGroup(),
                    reportColumn.isGroupOnNewPage(), reportColumn.isSum());
        }

        TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
        tsdb.addTabDef("editor", getAu().resolveSessionMessage("$m{studio.reportconfiguration.form.design}"), "!fc-reporteditor",
                RendererType.SIMPLE_WIDGET);
        tsdb.addTabDef("preview", getAu().resolveSessionMessage("$m{studio.reportconfiguration.form.preview}"),
                "fc-reportpreviewpanel", RendererType.STANDALONE_PANEL);
        reportEditor = teb.build();
        reportPreview = new ReportPreview(getAu(), reportEditor);
        final String appletName = null;
        tabSheet = new TabSheet(tsdb.build(),
                Arrays.asList(new TabSheetItem("reportEditor", appletName, reportEditor, DESIGN_INDEX, true),
                        new TabSheetItem("reportPreview", appletName, reportPreview, PREVIEW_INDEX, true)));
        tabSheet.setEventHandler(this);
    }
}
