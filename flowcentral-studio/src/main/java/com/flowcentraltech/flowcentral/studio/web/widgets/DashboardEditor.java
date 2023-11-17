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

package com.flowcentraltech.flowcentral.studio.web.widgets;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.tcdng.unify.core.UnifyException;

/**
 * Dashboard editor object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardEditor {

    private final AppletUtilities au;

    private final ChartModuleService cms;

    private DashboardDef dashboardDef;

    private Design design;

    private DDashboardSection editSection;

    private DDashboardTile editTile;

    private String editorBodyPanelName;

    private String editSectionPanelName;

    private String editTilePanelName;

    private String dialogTitle;

    private int originSectionIndex;

    private boolean readOnly;

    private DashboardEditor(AppletUtilities au, ChartModuleService cms, DashboardDef dashboardDef, Design design) {
        this.au = au;
        this.cms = cms;
        this.dashboardDef = dashboardDef;
        this.design = design;
    }

    public void init(String editorBodyPanelName, String editSectionPanelName, String editTilePanelName) {
        this.editorBodyPanelName = editorBodyPanelName;
        this.editSectionPanelName = editSectionPanelName;
        this.editTilePanelName = editTilePanelName;
    }

    public String getEditorBodyPanelName() {
        return editorBodyPanelName;
    }

    public String getEditSectionPanelName() {
        return editSectionPanelName;
    }

    public String getEditTilePanelName() {
        return editTilePanelName;
    }

    public boolean isInitialized() {
        return editorBodyPanelName != null;
    }

    public DashboardDef getDashboardDef() {
        return dashboardDef;
    }

    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public DDashboardSection getEditSection() {
        return editSection;
    }

    public DDashboardTile getEditTile() {
        return editTile;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    // Sections
    public void performSectionAdd() throws UnifyException {
        design.addDashboardSection(originSectionIndex, editSection);
    }

    public String prepareSectionCreate(int sectionIndex) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{dashboardeditor.dashboardsectioneditorpanel.caption.add}");
        this.originSectionIndex = sectionIndex;
        editSection = new DDashboardSection();
        editSection.setMode("CREATE");
        return editSectionPanelName;
    }

    public String prepareSectionEdit(int sectionIndex) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{dashboardeditor.dashboardsectioneditorpanel.caption.edit}");
        editSection = design.getDashboardSection(sectionIndex);
        editSection.setMode("UPDATE");
        return editSectionPanelName;
    }

    public String performSectionDel(int sectionIndex) throws UnifyException {
        design.removeDashboardSection(sectionIndex);
        return editorBodyPanelName;
    }

    // Fields
    public String performTileAdd() throws UnifyException {
        DDashboardSection dDashboardSection = design.getDashboardSection(originSectionIndex);
        dDashboardSection.addDashboardTile(editTile);
        if (editTile.getType() == null) {
            editTile.setType(DashboardTileType.SIMPLE.code());
        }

        return editorBodyPanelName;
    }

    public String prepareTileCreate(String chart, int sectionIndex, int tileIndex) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{dashboardeditor.dashboardtileeditorpanel.caption.add}");
        this.originSectionIndex = sectionIndex;
        ChartDef chartDef = cms.getChartDef(chart);
        editTile = new DDashboardTile();
        editTile.setIndex(tileIndex);
        editTile.setMode("CREATE");
        editTile.setType(DashboardTileType.SIMPLE.code());
        editTile.setChart(chart);
        editTile.setName(chart);
        editTile.setDescription(chartDef.getDescription());
        return editTilePanelName;
    }

    public String prepareTileEdit(int sectionIndex, int tileIndex) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{dashboardeditor.dashboardtileeditorpanel.caption.edit}");
        editTile = design.getDashboardTile(sectionIndex, tileIndex);
        editTile.setMode("UPDATE");
        return editTilePanelName;
    }

    public String performTileDel(int sectionIndex, int tileIndex) throws UnifyException {
        design.removeDashboardTile(sectionIndex, tileIndex);
        return editorBodyPanelName;
    }

    public static Builder newBuilder(ChartModuleService cms, DashboardDef dashboardDef) {
        return new Builder(cms, dashboardDef);
    }

    public static class Builder {

        private final ChartModuleService cms;

        private final DashboardDef dashboardDef;

        private final List<DDashboardSection> sections;

        private int sectionIndex;

        public Builder(ChartModuleService cms, DashboardDef dashboardDef) {
            this.cms = cms;
            this.dashboardDef = dashboardDef;
            this.sections = new ArrayList<DDashboardSection>();
            this.sectionIndex = 0;
        }

        public Builder addSection(DashboardColumnsType columns) {
            sections.add(new DDashboardSection(columns.code(), sectionIndex++));
            return this;
        }

        public Builder addTile(DashboardTileType type, String name, String description, String chart, int sectionIndex,
                int tileIndex) {
            if (sectionIndex >= sections.size()) {
                throw new IllegalArgumentException("Invalid section index [" + sectionIndex + "]");
            }

            DDashboardSection _section = sections.get(sectionIndex);
            if (_section.getDashboardTile(tileIndex) != null) {
                throw new IllegalArgumentException("Tile already placed at index [" + tileIndex + "]");
            }

            _section.addDashboardTile(new DDashboardTile(name, description, type.code(), chart, tileIndex));
            return this;
        }

        public DashboardEditor build(AppletUtilities au) {
            return new DashboardEditor(au, cms, dashboardDef, new Design(sections));
        }
    }

    public static class Design {

        private List<DDashboardSection> sections;

        public Design(List<DDashboardSection> sections) {
            this.sections = sections;
        }

        public Design() {

        }

        public List<DDashboardSection> getSections() {
            return sections;
        }

        public void setSections(List<DDashboardSection> sections) {
            this.sections = sections;
        }

        public void addDashboardSection(int originIndex, DDashboardSection section) {
            sections.add(originIndex + 1, section);
        }

        public DDashboardSection getDashboardSection(int index) {
            return sections.get(index);
        }

        public DDashboardSection removeDashboardSection(int index) {
            return sections.remove(index);
        }

        public DDashboardTile getDashboardTile(int sectionIndex, int tileIndex) {
            DDashboardSection section = sections.get(sectionIndex);
            return section.getDashboardTile(tileIndex);
        }

        public void removeDashboardTile(int sectionIndex, int tileIndex) {
            DDashboardSection section = sections.get(sectionIndex);
            section.removeDashboardTile(tileIndex);
        }
    }

    public static class DDashboardSection {

        private String mode;

        private String columns;

        private int index;

        private List<DDashboardTile> tiles;

        public DDashboardSection(String columns, int index) {
            this.tiles = new ArrayList<DDashboardTile>();
            this.columns = columns;
            this.index = index;
        }

        public DDashboardSection() {
            this.tiles = new ArrayList<DDashboardTile>();
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getColumns() {
            return columns;
        }

        public void setColumns(String columns) {
            this.columns = columns;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public List<DDashboardTile> getTiles() {
            return tiles;
        }

        public void setTiles(List<DDashboardTile> tiles) {
            this.tiles = tiles;
        }

        public void addDashboardTile(DDashboardTile editTile) {
            tiles.add(editTile);
        }

        public DDashboardTile getDashboardTile(int tileIndex) {
            for (DDashboardTile tile : tiles) {
                if (tile.getIndex() == tileIndex) {
                    return tile;
                }
            }

            return null;
        }

        public DDashboardTile removeDashboardTile(int tileIndex) {
            DDashboardTile tile = getDashboardTile(tileIndex);
            if (tile != null) {
                tiles.remove(tile);
            }

            return tile;
        }
    }

    public static class DDashboardTile {

        private String mode;

        private String name;

        private String description;

        private String type;

        private String chart;

        private int index;

        public DDashboardTile(String name, String description, String type, String chart, int index) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.chart = chart;
            this.index = index;
        }

        public DDashboardTile() {

        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getChart() {
            return chart;
        }

        public void setChart(String chart) {
            this.chart = chart;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

    }
}
