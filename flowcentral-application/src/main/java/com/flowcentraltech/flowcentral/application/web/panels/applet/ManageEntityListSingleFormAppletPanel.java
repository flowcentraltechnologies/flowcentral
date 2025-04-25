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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Manage entity list single form applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-manageentitylistsingleformappletpanel")
@UplBinding("web/application/upl/manageentitylistsingleformappletpanel.upl")
public class ManageEntityListSingleFormAppletPanel extends AbstractEntitySingleFormAppletPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final ManageEntityListSingleFormApplet applet = getManageEntityListApplet();
        applet.ensureRootAppletStruct();
        if (isWidgetVisible("entitySearchPanel.newBtn")) {
            setVisible("entitySearchPanel.newBtn", !applet.isWithBaseFilter());
        }

        final AbstractEntitySingleFormApplet.ViewMode viewMode = applet.getMode();
        switch (viewMode) {
            case MAINTAIN_FORM_SCROLL:
            case MAINTAIN_PRIMARY_FORM_NO_SCROLL:
            case MAINTAIN_FORM:
            case NEW_FORM:
            case NEW_PRIMARY_FORM:
                break;
            case SEARCH:
                switchContent("entitySearchPanel");
            default:
                break;
        }
    }

    @Action
    public void prepareGenerateReport() throws UnifyException {
        final ManageEntityListSingleFormApplet applet = getManageEntityListApplet();
        EntityTable entityTable = applet.getEntitySearch().getEntityTable();
        prepareGenerateReport(entityTable);
     }

    protected ManageEntityListSingleFormApplet getManageEntityListApplet() throws UnifyException {
        return getValue(ManageEntityListSingleFormApplet.class);
    }
}
