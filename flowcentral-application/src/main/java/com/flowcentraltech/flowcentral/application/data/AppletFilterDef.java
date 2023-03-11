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

import com.flowcentraltech.flowcentral.configuration.constants.ChildListActionType;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Applet filter definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletFilterDef implements Listable {

    private FilterDef filterDef;

    private String preferredForm;

    private String preferredChildListApplet;

    private ChildListActionType childListActionType;

    public AppletFilterDef(FilterDef filterDef, String preferredForm, String preferredChildListApplet,
            ChildListActionType childListActionType) {
        this.filterDef = filterDef;
        this.preferredForm = preferredForm;
        this.preferredChildListApplet = preferredChildListApplet;
        this.childListActionType = childListActionType;
   }

    @Override
    public String getListDescription() {
        return filterDef.getListDescription();
    }

    @Override
    public String getListKey() {
        return filterDef.getListKey();
    }

    public FilterDef getFilterDef() {
        return filterDef;
    }

    public String getName() {
        return filterDef.getName();
    }

    public String getPreferredForm() {
        return preferredForm;
    }

    public String getPreferredChildListApplet() {
        return preferredChildListApplet;
    }

    public ChildListActionType getChildListActionType() {
        return childListActionType;
    }

    public boolean isShowPopupChildListAction() {
        return childListActionType != null && childListActionType.isShowPopup();
    }

    public boolean isShowMultiSelectChildListAction() {
        return ChildListActionType.SHOW_MULTISELECT.equals(childListActionType);
    }

    public boolean isShowTreeMultiSelectChildListAction() {
        return ChildListActionType.SHOW_TREEMULTISELECT.equals(childListActionType);
    }

    public boolean isHideAddWidgetChildListAction() {
        return ChildListActionType.HIDE_ADDWIDGET.equals(childListActionType);
    }

    public boolean isWithPreferredForm() {
        return !StringUtils.isBlank(preferredForm);
    }

    public boolean isWithPreferredChildListApplet() {
        return !StringUtils.isBlank(preferredChildListApplet);
    }
}
