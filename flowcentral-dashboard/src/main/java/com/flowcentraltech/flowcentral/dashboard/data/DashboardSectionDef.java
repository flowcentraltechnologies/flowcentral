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

package com.flowcentraltech.flowcentral.dashboard.data;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;

/**
 * Dashboard section definition object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardSectionDef {

    private DashboardColumnsType type;

    private int index;

    private int height;

    private List<DashboardTileDef> tileList;

    public DashboardSectionDef(DashboardColumnsType type, int index, int height, List<DashboardTileDef> tileList) {
        this.type = type;
        this.index = index;
        this.height = height;
        this.tileList = tileList;
    }

    public DashboardColumnsType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public int getHeight() {
        return height;
    }

    public List<DashboardTileDef> getTileList() {
        return tileList;
    }

    public DashboardTileDef getTile(int tileIndex) {
        for (DashboardTileDef dashboardTileDef : tileList) {
            if (dashboardTileDef.getIndex() == tileIndex) {
                return dashboardTileDef;
            }
        }

        return null;
    }
}
