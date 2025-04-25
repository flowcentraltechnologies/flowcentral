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

package com.flowcentraltech.flowcentral.dashboard.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Dashboard definition object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DashboardDef extends BaseApplicationEntityDef {

    private RecordStatus status;

    private Map<String, DashboardOptionDef> options;

    private List<DashboardOptionDef> optionList;

    private List<DashboardSectionDef> sectionList;

    private DashboardDef(RecordStatus status, Map<String, DashboardOptionDef> options,
            List<DashboardSectionDef> sectionList, ApplicationEntityNameParts nameParts, String description, Long id,
            long version) {
        super(nameParts, description, id, version);
        this.status = status;
        this.options = options;
        this.sectionList = sectionList;
    }

    public boolean isOption(String name) {
        return !StringUtils.isBlank(name) && options.containsKey(name);
    }

    public DashboardOptionDef getFirstOption() {
        return !options.isEmpty() ? getOptionList().get(0) : null;
    }

    public boolean isWithOptions() {
        return !options.isEmpty();
    }

    public DashboardOptionDef getOption(String name) {
        DashboardOptionDef option = options.get(name);
        if (option == null) {
            throw new IllegalArgumentException("Option with name [" + name + "] is unknown.");
        }

        return option;
    }

    public List<DashboardOptionDef> getOptionList() {
        if (optionList == null) {
            synchronized (this) {
                if (optionList == null) {
                    optionList = new ArrayList<DashboardOptionDef>(options.values());
                }
            }
        }

        return optionList;
    }

    public int getSections() {
        return sectionList.size();
    }

    public DashboardSectionDef getSection(int section) {
        return sectionList.get(section);
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

        private Map<String, DashboardOptionDef> options;

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
            this.options = new LinkedHashMap<String, DashboardOptionDef>();
            this.sections = new ArrayList<Section>();
        }

        public Builder addOption(DashboardOptionDef optionDef) {
            options.put(optionDef.getName(), optionDef);
            return this;
        }

        public Builder addSection(DashboardColumnsType type, Integer height) {
            sections.add(new Section(type, sections.size(), height));
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

            private int sectionIndex;

            private Integer height;

            public Section(DashboardColumnsType type, int sectionIndex, Integer height) {
                this.type = type;
                this.sectionIndex = sectionIndex;
                this.height = height;
                this.tileList = new ArrayList<DashboardTileDef>();
                this.usedIndexes = new HashSet<Integer>();
            }

            public DashboardColumnsType getType() {
                return type;
            }

            public Integer getHeight() {
                return height;
            }

            public void addDashboardTileDef(DashboardTileDef dashboardTileDef) {
                if (tileList.size() >= type.columns()) {
                    throw new RuntimeException("Can not add tile [" + dashboardTileDef.getName() + "] to section ["
                            + sectionIndex + "] because all slots have been utilized.");
                }

                if (usedIndexes.contains(dashboardTileDef.getIndex())) {
                    throw new RuntimeException("Can not add tile [" + dashboardTileDef.getName() + "] to section ["
                            + sectionIndex + "] because index is in use by another tile.");
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
                        DataUtils.convert(int.class, section.getHeight()),
                        DataUtils.unmodifiableList(section.getTileList())));
            }

            return new DashboardDef(status, options, DataUtils.unmodifiableList(sections),
                    ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
        }
    }

}
