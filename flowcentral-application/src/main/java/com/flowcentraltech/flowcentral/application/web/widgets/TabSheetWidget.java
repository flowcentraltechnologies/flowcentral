/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.TabDef;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.AbstractValueListMultiControl;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Tab sheet widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-tabsheet")
public class TabSheetWidget extends AbstractValueListMultiControl<ValueStore, TabSheetItem> {

    private TabSheet oldTabSheet;

    private Widget[] tabWidgets;

    @Action
    public void choose() throws UnifyException {
        TabSheet tabSheet = getTabSheet();
        if (tabSheet != null) {
            tabSheet.setCurrentTabIndex(getRequestTarget(int.class));
            if (tabSheet.isWithEventHandler() && tabSheet.isWithCurrentItem()) {
                tabSheet.getEventHandler().onChoose(tabSheet.getCurrentItem());
            }
        }
    }

    public TabSheet getTabSheet() throws UnifyException {
        TabSheet tabSheet = getValue(TabSheet.class);
        if (tabSheet != oldTabSheet) {
            TabSheetDef oldTabSheetDef = null;
            if (oldTabSheet != null) {
                oldTabSheetDef = oldTabSheet != null ? oldTabSheet.getTabSheetDef() : null;
            }

            TabSheetDef tabSheetDef = null;
            if (tabSheet != null) {
                tabSheetDef = tabSheet != null ? tabSheet.getTabSheetDef() : null;
            }

            if (oldTabSheetDef != tabSheetDef) {
                removeAllExternalChildWidgets();
                tabWidgets = null;
                if (tabSheet != null) {
                    List<TabDef> tabDefList = tabSheet.getTabDefList();
                    tabWidgets = new Widget[tabDefList.size()];
                    for (int i = 0; i < tabWidgets.length; i++) {
                        TabDef tabDef = tabDefList.get(i);
                        if (RendererType.STANDALONE_PANEL.equals(tabDef.getRendererType())) {
                            tabWidgets[i] = addExternalChildStandalonePanel(tabDef.getTabRenderer(), getId() + i);
                        } else {
                            tabWidgets[i] = addExternalChildWidget(tabDef.getTabRenderer());
                        }
                    }
                }
            }

            oldTabSheet = tabSheet;
        }

        return tabSheet;
    }

    public Widget getCurrentTabWidget() throws UnifyException {
        TabSheet tabSheet = getTabSheet();
        if (tabSheet != null) {
            return tabWidgets[tabSheet.getCurrentTabIndex()];
        }

        return null;
    }

    public void setAppAppletFilterId(Long id) {
        
    }
    
    @Override
    protected void doOnPageConstruct() throws UnifyException {

    }

    @Override
    protected ValueStore newValue(TabSheetItem object, int index) throws UnifyException {
        return createValueStore(object.getValObject(), index);
    }

    @Override
    protected void onCreateValueList(List<ValueStore> arg0) throws UnifyException {

    }

    @Override
    protected List<TabSheetItem> getItemList() throws UnifyException {
        TabSheet tabSheet = getTabSheet();
        if (tabSheet != null) {
            return tabSheet.getTabSheetItemList();
        }

        return Collections.emptyList();
    }

}
