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

package com.flowcentraltech.flowcentral.dashboard.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Dashboard definition object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardDef extends BaseApplicationEntityDef {

    private RecordStatus status;

    private List<DashboardSectionDef> sectionList;

    private DashboardDef(RecordStatus status, List<DashboardSectionDef> sectionList,
            ApplicationEntityNameParts nameParts, String description, Long id, long version) {
        super(nameParts, description, id, version);
        this.status = status;
        this.sectionList = sectionList;
    }

    public int getSections() {
        return sectionList.size();
    }

    public List<DashboardTileDef> getTileList(int section) {
        return sectionList.get(section).getTileList();
    }

    public RecordStatus getStatus() {
        return status;
    }

    public boolean isActive() {
        return RecordStatus.ACTIVE.equals(status);
    }

    public static Builder newBuilder(RecordStatus status, String longName, String description, Long id, long version) {
        return new Builder(status, longName, description, id, version);
    }

    public static class Builder {

        private RecordStatus status;

        private List<Section> sections;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(RecordStatus status, String longName, String description, Long id, long version) {
            this.status = status;
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
            this.sections = new ArrayList<Section>();
        }

        public Builder addSection(DashboardColumnsType type) {
            sections.add(new Section(type, sections.size()));
            return this;
        }

        public Builder addTile(DashboardTileType type, String name, String description, String chart, int section,
                int index) {
            if (section < 0 || section >= sections.size()) {
                throw new RuntimeException(
                        "Can not add tile to section [" + section + "] for dashboard with [" + sections + "] sections");
            }

            sections.get(section)
                    .addDashboardTileDef(new DashboardTileDef(type, name, description, chart, section, index));
            return this;
        }

        private class Section {

            private DashboardColumnsType type;

            private List<DashboardTileDef> tileList;

            private Set<Integer> usedIndexes;

            private int sessionIndex;

            public Section(DashboardColumnsType type, int sessionIndex) {
                this.type = type;
                this.sessionIndex = sessionIndex;
                this.tileList = new ArrayList<DashboardTileDef>();
                this.usedIndexes = new HashSet<Integer>();
            }

            public DashboardColumnsType getType() {
                return type;
            }

            public void addDashboardTileDef(DashboardTileDef dashboardTileDef) {
                if (tileList.size() >= type.columns()) {
                    throw new RuntimeException("Can not add tile [" + dashboardTileDef.getName() + "] to section ["
                            + sessionIndex + "] because all slots have been utilized.");
                }

                if (usedIndexes.contains(dashboardTileDef.getIndex())) {
                    throw new RuntimeException("Can not add tile [" + dashboardTileDef.getName() + "] to section ["
                            + sessionIndex + "] because index is in use by another tile.");
                }

                tileList.add(dashboardTileDef);
            }

            public List<DashboardTileDef> getTileList() {
                return tileList;
            }

        }

        public DashboardDef build() throws UnifyException {
            List<DashboardSectionDef> sections = new ArrayList<DashboardSectionDef>();
            int sectionIndex = 0;
            for (Section section : this.sections) {
                DataUtils.sortAscending(section.getTileList(), DashboardTileDef.class, "index");
                sections.add(new DashboardSectionDef(section.getType(), sectionIndex,
                        DataUtils.unmodifiableList(section.getTileList())));
            }

            return new DashboardDef(status, DataUtils.unmodifiableList(sections),
                    ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
        }
    }

}
