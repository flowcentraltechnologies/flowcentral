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
package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.flowcentraltech.flowcentral.studio.web.panels.DashboardEditorPage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Studio application dashboard applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-studiodashboardappletpanel")
@UplBinding("web/studio/upl/studiodashboardappletpanel.upl")
public class StudioDashboardAppletPanel extends StudioAppComponentAppletPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        AbstractEntityFormApplet applet = getEntityFormApplet();
        final AbstractEntityFormApplet.ViewMode viewMode = applet.getMode();

        switch (viewMode) {
            case ENTITY_CRUD_PAGE:
            case ENTRY_TABLE_PAGE:
            case ASSIGNMENT_PAGE:
            case PROPERTYLIST_PAGE:
            case LISTING_FORM:
            case MAINTAIN_FORM_SCROLL:
            case MAINTAIN_PRIMARY_FORM_NO_SCROLL:
            case MAINTAIN_CHILDLIST_FORM_NO_SCROLL:
            case MAINTAIN_RELATEDLIST_FORM_NO_SCROLL:
            case MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL:
            case MAINTAIN_FORM:
            case MAINTAIN_CHILDLIST_FORM:
            case MAINTAIN_RELATEDLIST_FORM:
            case MAINTAIN_HEADLESSLIST_FORM:
            case NEW_FORM:
            case NEW_PRIMARY_FORM:
            case NEW_CHILD_FORM:
            case NEW_CHILDLIST_FORM:
            case NEW_RELATEDLIST_FORM:
            case NEW_HEADLESSLIST_FORM:
            case SEARCH:
            case HEADLESS_TAB:
                break;
            case CUSTOM_PAGE:
                setWidgetVisible("saveDesignCloseBtn", !applet.appletCtx().isReadOnly());
                switchContent("dashboardEditorPagePanel");
                break;
            default:
                break;
        }
    }

    @Action
    public void saveDesignAndClose() throws UnifyException {
        StudioDashboardApplet applet = getValue(StudioDashboardApplet.class);
        DashboardEditorPage dashboardEditorPage = applet.getDashboardEditorPage();
        dashboardEditorPage.commitDesign();
        applet.navBackToPrevious();
        hintUser("$m{studiodashboardapplet.dashboardeditor.success.hint}", dashboardEditorPage.getSubTitle());
    }

}
