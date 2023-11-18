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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
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

    private final AppletUtilities au;

    private final DashboardEditor dashboardEditor;

    private Design oldDesign;

    public DashboardPreview(AppletUtilities au, DashboardEditor dashboardEditor) {
        this.au = au;
        this.dashboardEditor = dashboardEditor;
    }

    public void reload() throws UnifyException {
        Design design = dashboardEditor.getDesign();
        if (oldDesign != design) {
            final DashboardDef originDashboardDef = dashboardEditor.getDashboardDef();
            DashboardDef.Builder ddb = DashboardDef.newBuilder(originDashboardDef.getStatus(),
                    originDashboardDef.getLongName(), originDashboardDef.getDescription(), originDashboardDef.getId(),
                    originDashboardDef.getVersion());
            if (design != null && design.getSections() != null) {
                int secIndex = -1;
                for (DDashboardSection section : design.getSections()) {
                    secIndex++;
                    ddb.addSection(DashboardColumnsType.fromName(section.getColumns()), section.getHeight());
                    int tileIndex = -1;
                    for (DDashboardTile tile : section.getTiles()) {
                        tileIndex++;
                        final String tileName = tile.getName();
                        ddb.addTile(DashboardTileType.fromName(tile.getType()), tile.getName(), tile.getDescription(), tile.getChart(), secIndex, tileIndex);
                    }
                }

            }

            final DashboardDef dashboardDef = ddb.build();
            oldDesign = design;
        }
    }
}
