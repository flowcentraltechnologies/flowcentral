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

package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.FormEditorPage;
import com.tcdng.unify.core.UnifyException;

/**
 * Studio application form applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StudioAppFormApplet extends StudioAppComponentApplet {

    private FormEditorPage formEditorPage;

    public StudioAppFormApplet(StudioModuleService sms, AppletUtilities au, String pathVariable, String applicationName,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(sms, au, pathVariable, applicationName, appletWidgetReferences, formEventHandlers);
    }

    public FormEditorPage getFormEditorPage() {
        return formEditorPage;
    }

    public void designChildItem(int childTabIndex) throws UnifyException {
        currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
        AppForm appForm = (AppForm) form.getFormBean();
        Object id = appForm.getId();
        String subTitle = appForm.getDescription();
        saveCurrentForm(currFormTabDef);
        formEditorPage = constructNewFormEditorPage(
                ApplicationNameUtils.getApplicationEntityLongName(getApplicationName(), appForm.getName()), id,
                subTitle);
        formEditorPage.newEditor();
        viewMode = ViewMode.CUSTOM_PAGE;
    }

    private FormEditorPage constructNewFormEditorPage(String formName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        FormDef formDef = getFormDef(formName);
        breadCrumbs.setLastCrumbTitle(au.resolveSessionMessage("$m{formeditor.formdesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new FormEditorPage(au, formDef, id, breadCrumbs);
    }

}
