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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormScope;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;

/**
 * Header with tabs form.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class HeaderWithTabsForm extends AbstractForm {

    public enum UpdateType {
        SWITCH_ON_CHANGE,
        MAINTAIN_INST,
        NAV_BACK_TO_PREVIOUS,
        RELOAD,
        FORMACTION_ON_INST,
        UPDATE_INST;

        public boolean isMaintain() {
            return MAINTAIN_INST.equals(this);
        }
    }

    private MiniForm headerForm;

    private TabSheet tabSheet;

    private TabSheet relatedListTabSheet;

    private UpdateType updateType;
    
    public HeaderWithTabsForm(FormContext ctx, SectorIcon sectorIcon, BreadCrumbs breadCrumbs) {
        super(ctx, sectorIcon, breadCrumbs);
        headerForm = new MiniForm(MiniFormScope.MAIN_FORM, ctx, ctx.getFormDef().getFormTabDef(0));
    }

    public String getFocusWidgetId() {
        return getCtx().getFocusWidgetId();
    }

    public void setFocusWidgetId(String focusWidgetId) {
        getCtx().setFocusWidgetId(focusWidgetId);
    }

    public String getTabWidgetId() {
        return getCtx().getTabWidgetId();
    }

    public void setTabWidgetId(String tabWidgetId) {
        getCtx().setTabWidgetId(tabWidgetId);
    }

    public MiniForm getHeaderForm() {
        return headerForm;
    }

    public void setTabSheet(TabSheet tabSheet) {
        this.tabSheet = tabSheet;
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public boolean isWithTabSheet() {
        return tabSheet != null;
    }

    public boolean isTabSheetInStateForDisplay() {
        return tabSheet != null && tabSheet.isInStateForDisplay();
    }
    
    public void setRelatedListTabSheet(TabSheet relatedListTabSheet) {
        this.relatedListTabSheet = relatedListTabSheet;
    }

    public TabSheet getRelatedListTabSheet() {
        return relatedListTabSheet;
    }

    public boolean isWithRelatedListTabSheet() {
        return relatedListTabSheet != null;
    }

    public boolean isRelatedListTabSheetInStateForDisplay() {
        return relatedListTabSheet != null && relatedListTabSheet.isInStateForDisplay();
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }
}
