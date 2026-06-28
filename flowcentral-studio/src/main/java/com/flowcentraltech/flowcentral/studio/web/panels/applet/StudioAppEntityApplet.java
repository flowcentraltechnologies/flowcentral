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
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.EntityEditorPage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application entity applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioAppEntityApplet extends AbstractStudioAppComponentApplet {

    private EntityEditorPage entityEditorPage;

    public StudioAppEntityApplet(Page page, StudioModuleService sms, AppletUtilities au, List<String> pathVariables,
            String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
        createDesign();
    }

    public EntityEditorPage getEntityEditorPage() {
        return entityEditorPage;
    }

    public void createDesign() throws UnifyException {
        AppEntity appEntity = (AppEntity) form.getFormBean();
        Long entityId = appEntity.getId();
        if (entityId != null) {
            String subTitle = appEntity.getDescription();
            entityEditorPage = constructNewEntityEditorPage(
                    ApplicationNameUtils.getApplicationEntityLongName(getApplicationName(), appEntity.getName()), entityId,
                    subTitle);
            entityEditorPage.newEditor();
        }
    }

    private EntityEditorPage constructNewEntityEditorPage(String entityName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{entityeditor.entitydesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new EntityEditorPage(au(), entityName, id, breadCrumbs);
    }

}
