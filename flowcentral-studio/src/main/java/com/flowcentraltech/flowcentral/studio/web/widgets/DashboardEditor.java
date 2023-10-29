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
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.tcdng.unify.core.UnifyException;

/**
 * Dashboard editor object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardEditor {

    private final AppletUtilities au;

    private Design design;

    private DashboardSection editSection;

    private DashboardTile editTile;

    private String editorBodyPanelName;

    private String editSectionPanelName;

    private String editTilePanelName;

    private String dialogTitle;

    private int originSectionIndex;

    private boolean readOnly;

    private DashboardEditor(AppletUtilities au, Design design) {
        this.au = au;
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

    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public DashboardSection getEditSection() {
        return editSection;
    }

    public DashboardTile getEditTile() {
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
        editSection = new DashboardSection();
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
        DashboardSection dashboardSection = design.getDashboardSection(originSectionIndex);
        dashboardSection.addDashboardTile(editTile, originSectionIndex);
        if (editTile.getType() == null) {
            editTile.setType(DashboardTileType.SIMPLE.code());
        }

        return editorBodyPanelName;
    }

    public String prepareTileCreate(String chart, int sectionIndex, int column) throws UnifyException {
        dialogTitle = au.resolveSessionMessage("$m{dashboardeditor.dashboardtileeditorpanel.caption.add}");
        this.originSectionIndex = sectionIndex;
        editTile = new DashboardTile();
        editTile.setMode("CREATE");
        editTile.setType(DashboardTileType.SIMPLE.code());
        editTile.setChart(chart);
        editTile.setName(null);
        editTile.setDescription(null);
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

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<DashboardSection> sections;

        private DashboardSection currentSection;

        public Builder() {
            this.sections = new ArrayList<DashboardSection>();
        }

        public Builder addSection(DashboardColumnsType columns) {
            currentSection = new DashboardSection(columns.code());
            sections.add(currentSection);
            return this;
        }

        public Builder addTile(DashboardTileType type, String name, String description, String chart) {
            currentSection.getTiles().add(new DashboardTile(name, description, type.code(), chart));
            return this;
        }

        public DashboardEditor build(AppletUtilities au) {
            return new DashboardEditor(au, new Design(sections));
        }
    }

    public static class Design {

        private List<DashboardSection> sections;

        public Design(List<DashboardSection> sections) {
            this.sections = sections;
        }

        public Design() {

        }

        public List<DashboardSection> getSections() {
            return sections;
        }

        public void setSections(List<DashboardSection> sections) {
            this.sections = sections;
        }

        public void addDashboardSection(int originIndex, DashboardSection section) {
            sections.add(originIndex + 1, section);
        }

        public DashboardSection getDashboardSection(int index) {
            return sections.get(index);
        }

        public DashboardSection removeDashboardSection(int index) {
            return sections.remove(index);
        }

        public DashboardTile getDashboardTile(int sectionIndex, int tileIndex) {
            DashboardSection section = sections.get(sectionIndex);
            return section.getDashboardTile(tileIndex);
        }

        public void removeDashboardTile(int sectionIndex, int tileIndex) {
            DashboardSection section = sections.get(sectionIndex);
            section.removeDashboardTile(tileIndex);
        }
    }

    public static class DashboardSection {

        private String mode;

        private String columns;

        private List<DashboardTile> tiles;

        public DashboardSection(String columns) {
            this.tiles = new ArrayList<DashboardTile>();
            this.columns = columns;
        }

        public DashboardSection() {
            this.tiles = new ArrayList<DashboardTile>();
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

        public List<DashboardTile> getTiles() {
            return tiles;
        }

        public void setTiles(List<DashboardTile> tiles) {
            this.tiles = tiles;
        }

        public void addDashboardTile(DashboardTile editTile, int index) {
            tiles.add(index, editTile);
        }

        public DashboardTile getDashboardTile(int index) {
            return tiles.get(index);
        }

        public DashboardTile removeDashboardTile(int index) {
            return tiles.remove(index);
        }
    }

    public static class DashboardTile {

        private String mode;

        private String name;

        private String description;

        private String type;

        private String chart;

        public DashboardTile(String name, String description, String type, String chart) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.chart = chart;
        }

        public DashboardTile() {

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

    }
}
