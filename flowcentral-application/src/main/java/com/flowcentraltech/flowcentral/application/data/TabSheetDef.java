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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Tab sheet definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class TabSheetDef {

    private List<TabDef> tabDefList;

    private FormContext formContext;
    
    private long version;

    protected TabSheetDef(FormContext formContext, List<TabDef> tabDefList, long version) {
        this.formContext = formContext;
        this.tabDefList = DataUtils.unmodifiableList(tabDefList);
        this.version = version;
    }

    public FormContext getFormContext() {
        return formContext;
    }

    public boolean isWithFormContext() {
        return formContext != null;
    }

    public List<TabDef> getTabDefList() {
        return tabDefList;
    }

    public TabDef getTabDef(int tabIndex) {
        return tabDefList.get(tabIndex);
    }

    public long getVersion() {
        return version;
    }

    public static Builder newBuilder(FormContext formContext, long version) {
        return new Builder(formContext, version);
    }

    public static class Builder {

        private List<TabDef> tabDefList;

        private FormContext formContext;
        
        private long version;

        public Builder(FormContext formContext, long version) {
            this.version = version;
            this.formContext = formContext;
            tabDefList = new ArrayList<TabDef>();
        }

        public Builder addTabDef(String tabName, String tabLabel, String tabRenderer) {
            return addTabDef(tabName, tabLabel, tabRenderer, RendererType.SIMPLE_WIDGET);
        }

        public Builder addTabDef(String tabName, String tabLabel, String tabRenderer, RendererType rendererType) {
            tabDefList.add(new TabDef(tabName, tabLabel, tabRenderer, rendererType));
            return this;
        }

        public TabSheetDef build() {
            return new TabSheetDef(formContext, tabDefList, version);
        }
    }
}
