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

package com.flowcentraltech.flowcentral.dashboard.web.widgets;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralMultiControl;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardDef;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardOptionDef;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardSectionDef;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardSlotDef;
import com.flowcentraltech.flowcentral.dashboard.data.DashboardTileDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Dashboard slate widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-dashboardslate")
public class DashboardSlateWidget extends AbstractFlowCentralMultiControl {

    private DashboardSlate oldSlate;

    private List<List<DashboardSlot>> sectionList;

    public DashboardSlateWidget() {
        sectionList = new ArrayList<List<DashboardSlot>>();
    }

    public DashboardSlate getDashboardSlate() throws UnifyException {
        DashboardSlate slate = getValue(DashboardSlate.class);
        if (slate != oldSlate) {
            DashboardDef dashboardDef = slate != null ? slate.getDashboardDef() : null;
            removeAllExternalChildWidgets();
            sectionList.clear();
            if (dashboardDef != null) {
                final DashboardOptionDef optionDef = slate.isWithOption() ? slate.getOption() : null;
                final int sections = dashboardDef.getSections();
                for (int sectionIndex = 0; sectionIndex < sections; sectionIndex++) {
                    final DashboardSectionDef dashboardSectionDef = dashboardDef.getSection(sectionIndex);
                    final DashboardColumnsType type = dashboardSectionDef.getType();
                    final List<DashboardSlot> slotList = new ArrayList<DashboardSlot>();
                    final int preferredHeight = dashboardSectionDef.getHeight();
                    for (int tileIndex = 0; tileIndex < type.columns(); tileIndex++) {
                        Widget widget = null;
                        DashboardTileDef dashboardTileDef = dashboardSectionDef.getTile(tileIndex);
                        if (dashboardTileDef != null) {
                            String renderer = null;
                            switch (dashboardTileDef.getType()) {
                                case SPARKLINE:
                                    renderer = "!fc-chart sparkLine:true binding:chart preferredHeight:"
                                            + preferredHeight;
                                    break;
                                case SIMPLE:
                                default:
                                    renderer = "!fc-chart sparkLine:false binding:chart preferredHeight:"
                                            + preferredHeight;
                                    break;
                            }

                            widget = addExternalChildWidget(renderer);
                            widget.setValueStore(
                                    createValueStore(new DashboardSlotDef(dashboardTileDef, optionDef)));
                        }

                        slotList.add(new DashboardSlot(type.dimension().get(tileIndex), widget));
                    }

                    sectionList.add(slotList);
                }

            }

            oldSlate = slate;
        }

        return slate;
    }

    public List<DashboardSlot> getSectionSlots(int sectionIndex) throws UnifyException {
        getDashboardSlate();
        return sectionList.get(sectionIndex);
    }

    public int getSectionPreferredHeight(int sectionIndex) throws UnifyException {
        getDashboardSlate();
        DashboardDef dashboardDef = oldSlate != null ? oldSlate.getDashboardDef() : null;
        return dashboardDef != null ? dashboardDef.getSection(sectionIndex).getHeight() : 0;
    }

    public int getSections() throws UnifyException {
        getDashboardSlate();
        return sectionList.size();
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {

    }

    public static class DashboardSlot {

        private final String width;

        private final Widget widget;

        public DashboardSlot(String width, Widget widget) {
            this.width = width;
            this.widget = widget;
        }

        public String getWidth() {
            return width;
        }

        public Widget getWidget() {
            return widget;
        }

        public boolean isEmpty() {
            return widget == null;
        }
    }
}
