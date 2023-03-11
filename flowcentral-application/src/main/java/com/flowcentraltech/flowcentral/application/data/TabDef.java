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
package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.configuration.constants.RendererType;

/**
 * Tab definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TabDef {

    private String tabName;

    private String tabLabel;

    private String tabRenderer;

    private RendererType rendererType;

    private boolean errors;

    public TabDef(String tabName, String tabLabel, String tabRenderer, RendererType rendererType) {
        this.tabName = tabName;
        this.tabLabel = tabLabel;
        this.tabRenderer = tabRenderer;
        this.rendererType = rendererType;
    }

    public String getTabName() {
        return tabName;
    }

    public String getTabLabel() {
        return tabLabel;
    }

    public void setTabLabel(String tabLabel) {
        this.tabLabel = tabLabel;
    }

    public String getTabRenderer() {
        return tabRenderer;
    }

    public RendererType getRendererType() {
        return rendererType;
    }

    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "TabDef [tabName=" + tabName + ", tabLabel=" + tabLabel + ", tabRenderer=" + tabRenderer
                + ", rendererType=" + rendererType + ", errors=" + errors + "]";
    }
}
