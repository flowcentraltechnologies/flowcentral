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

package com.flowcentraltech.flowcentral.dashboard.web.widgets;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardTileDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Dashboard slate widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-dashboardslate")
public class DashboardSlateWidget extends AbstractMultiControl {

    private DashboardSlate oldSlate;

    private List<List<Widget>> tileList;

    public DashboardSlateWidget() {
        tileList = new ArrayList<List<Widget>>();
    }

    public DashboardSlate getDashboardSlate() throws UnifyException {
        DashboardSlate slate = getValue(DashboardSlate.class);
        if (slate != oldSlate) {
            DashboardDef oldDashboardDef = oldSlate != null ? oldSlate.getDashboardDef() : null;
            DashboardDef dashboardDef = slate != null ? slate.getDashboardDef() : null;
            if (dashboardDef != oldDashboardDef) {
                removeAllExternalChildWidgets();
                tileList.clear();

                if (dashboardDef != null) {
                    final int sections = dashboardDef.getSections();
                    for (int i = 0; i < sections; i++) {
                        List<Widget> widgetList = new ArrayList<Widget>();
                        for (DashboardTileDef dashboardTileDef : dashboardDef.getTileList(i)) {
                            String renderer = null;
                            switch (dashboardTileDef.getType()) {
                                case SPARKLINE:
                                    renderer = "!fc-chart sparkLine:true binding:chart";
                                    break;
                                case SIMPLE:
                                default:
                                    renderer = "!fc-chart sparkLine:false binding:chart";
                                    break;
                            }

                            Widget widget = addExternalChildWidget(renderer);
                            widget.setValueStore(createValueStore(dashboardTileDef));
                            widgetList.add(widget);
                        }

                        tileList.add(widgetList);
                    }

                }
            }

            oldSlate = slate;
        }

        return slate;
    }

    public List<List<Widget>> getTileList() throws UnifyException {
        getDashboardSlate();
        return tileList;
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {

    }

}
