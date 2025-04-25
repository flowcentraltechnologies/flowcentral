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

package com.flowcentraltech.flowcentral.studio.web.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetEventHandler;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.entities.Dashboard;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardSection;
import com.flowcentraltech.flowcentral.dashboard.entities.DashboardTile;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor.DDashboardSection;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor.DDashboardTile;
import com.tcdng.unify.core.UnifyException;

/**
 * Dashboard editor page.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DashboardEditorPage extends AbstractStudioEditorPage implements TabSheetEventHandler {

    private static final int DESIGN_INDEX = 0;

    private static final int PREVIEW_INDEX = 1;
    
    private final ChartModuleService cms;

    private final DashboardDef dashboardDef;

    private final Object baseId;

    private TabSheet tabSheet;

    private DashboardEditor dashboardEditor;

    private DashboardPreview dashboardPreview;

    public DashboardEditorPage(AppletUtilities au, ChartModuleService cms, DashboardDef dashboardDef, Object baseId, BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.cms = cms;
        this.dashboardDef = dashboardDef;
        this.baseId = baseId;
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public DashboardEditor getDashboardEditor() {
        return dashboardEditor;
    }

    public DashboardPreview getDashboardPreview() {
        return dashboardPreview;
    }

    public DashboardDef getDashboardDef() {
        return dashboardDef;
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
                dashboardPreview.reload();
                break;
            default:
        }
    }

    public void commitDesign() throws UnifyException {
        Dashboard dashboard = getAu().environment().find(Dashboard.class, baseId);
        List<DashboardSection> sectionList = Collections.emptyList();
        List<DashboardTile> tileList = Collections.emptyList();
        if (dashboardEditor.getDesign() != null && dashboardEditor.getDesign().getSections() != null) {
            sectionList = new ArrayList<DashboardSection>();
            tileList = new ArrayList<DashboardTile>();
            int sectionIndex = 0;
            for (DDashboardSection dsection : dashboardEditor.getDesign().getSections()) {
                DashboardSection section = new DashboardSection();
                section.setIndex(sectionIndex);
                section.setHeight(dsection.getHeight());
                section.setType(DashboardColumnsType.fromCode(dsection.getColumns()));

                for (DDashboardTile dtile : dsection.getTiles()) {
                    DashboardTile tile = new DashboardTile();
                    tile.setType(DashboardTileType.fromCode(dtile.getType()));
                    tile.setName(dtile.getName());
                    tile.setDescription(dtile.getDescription());
                    tile.setChart(dtile.getChart());
                    tile.setSection(sectionIndex);
                    tile.setIndex(dtile.getIndex());
                    tileList.add(tile);
                }

                sectionList.add(section);
                sectionIndex++;
            }
        }

        dashboard.setSectionList(sectionList);
        dashboard.setTileList(tileList);
        getAu().environment().updateByIdVersion(dashboard);
    }

    public void newEditor() throws UnifyException {
        DashboardEditor.Builder deb = DashboardEditor.newBuilder(cms, dashboardDef);
        Dashboard dashboard = getAu().environment().find(Dashboard.class, baseId);
        for (DashboardSection dashboardSection : dashboard.getSectionList()) {
            deb.addSection(dashboardSection.getType(), dashboardSection.getHeight());
        }

        for (DashboardTile dashboardTile : dashboard.getTileList()) {
            deb.addTile(dashboardTile.getType(), dashboardTile.getName(), dashboardTile.getDescription(),
                    dashboardTile.getChart(), dashboardTile.getSection(), dashboardTile.getIndex());
        }

        dashboardEditor = deb.build(getAu());

        TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
        tsdb.addTabDef("editor", getAu().resolveSessionMessage("$m{studio.dashboard.form.design}"),
                "!fc-dashboardeditor", RendererType.SIMPLE_WIDGET);
        tsdb.addTabDef("preview", getAu().resolveSessionMessage("$m{studio.dashboard.form.preview}"),
                "fc-dashboardpreviewpanel", RendererType.STANDALONE_PANEL);
        dashboardPreview = new DashboardPreview(dashboardEditor);

        final String appletName = null;
        tabSheet = new TabSheet(tsdb.build(),
                Arrays.asList(new TabSheetItem("dashboardEditor", appletName, dashboardEditor, DESIGN_INDEX, true),
                        new TabSheetItem("dashboardPreview", appletName, dashboardPreview, PREVIEW_INDEX, true)));
        tabSheet.setEventHandler(this);
    }
}
