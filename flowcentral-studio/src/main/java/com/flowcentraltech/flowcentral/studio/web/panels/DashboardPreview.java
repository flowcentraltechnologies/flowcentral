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

import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.web.widgets.DashboardSlate;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor.DDashboardSection;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor.DDashboardTile;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor.Design;
import com.tcdng.unify.core.UnifyException;

/**
 * Dashboard preview object
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardPreview {

    private final DashboardEditor dashboardEditor;

    private DashboardSlate dashboardSlate;
    
    private Design oldDesign;

    public DashboardPreview(DashboardEditor dashboardEditor) {
        this.dashboardEditor = dashboardEditor;
    }

    public DashboardSlate getDashboardSlate() {
        return dashboardSlate;
    }

    public void reload() throws UnifyException {
        Design design = dashboardEditor.getDesign();
        if (oldDesign != design) {
            final DashboardDef originDashboardDef = dashboardEditor.getDashboardDef();
            DashboardDef.Builder ddb = DashboardDef.newBuilder(originDashboardDef.getStatus(),
                    originDashboardDef.getLongName(), originDashboardDef.getDescription(), originDashboardDef.getId(),
                    originDashboardDef.getVersion());
            if (design != null && design.getSections() != null) {
                for (DDashboardSection section : design.getSections()) {
                    ddb.addSection(DashboardColumnsType.fromCode(section.getColumns()), section.getHeight());
                    for (DDashboardTile tile: section.getTiles()) {
                        ddb.addTile(DashboardTileType.fromCode(tile.getType()), tile.getName(), tile.getDescription(),
                                tile.getChart(), tile.getSectionIndex(), tile.getIndex());
                    }
                }
            }

            final DashboardDef dashboardDef = ddb.build();
            dashboardSlate = new DashboardSlate(dashboardDef);
            oldDesign = design;
        }
    }
}
