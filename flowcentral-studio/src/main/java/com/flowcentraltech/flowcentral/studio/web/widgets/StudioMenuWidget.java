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
package com.flowcentraltech.flowcentral.studio.web.widgets;

import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.ui.widget.AbstractPanel;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Studio menu widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-studiomenu")
public class StudioMenuWidget extends AbstractPanel {

    public static final String WORK_MENU_CATEGORIES = "menuCats";

    public static final String WORK_MENU_ITEMS = "menuItems";

    private Control currentSelCtrl;

    private StudioAppComponentType currentSel;

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        currentSelCtrl = (Control) addInternalWidget("!ui-hidden binding:currentSel");
    }

    public StudioAppComponentType getCurrentSel() {
        return currentSel;
    }

    public void setCurrentSel(StudioAppComponentType currentSel) {
        this.currentSel = currentSel;
    }

    public Control getCurrentSelCtrl() {
        return currentSelCtrl;
    }

    public String getCategoryId() throws UnifyException {
        return getPrefixedId("cat_");
    }
}
