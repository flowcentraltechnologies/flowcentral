/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.AppletEditorPage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application applet applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioAppAppletApplet extends AbstractStudioAppComponentApplet {

    private AppletEditorPage appletEditorPage;

    public StudioAppAppletApplet(Page page, StudioModuleService sms, AppletUtilities au, List<String> pathVariables,
            String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
        createDesign();
    }

    public AppletEditorPage getAppletEditorPage() {
        return appletEditorPage;
    }

    public void createDesign() throws UnifyException {
        AppApplet appApplet = (AppApplet) form.getFormBean();
        Long appletId = appApplet.getId();
        if (appletId != null) {
            if (appApplet.getType().isEntityList()) {
                String subTitle = appApplet.getDescription();
                appletEditorPage = constructNewAppletEditorPage(appApplet.getEntity(), appletId, subTitle);
                appletEditorPage.newEditor();
            }
        }
    }

    private AppletEditorPage constructNewAppletEditorPage(String entityName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        final AppletDef appletDef = au().getAppletDef((Long) id);
        final String tableName = appletDef.getPropValue(String.class, AppletPropertyConstants.SEARCH_TABLE);
        final String formName = appletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM);
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{appleteditor.appletdesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new AppletEditorPage(au(), tableName, formName, id, breadCrumbs);
    }

}
