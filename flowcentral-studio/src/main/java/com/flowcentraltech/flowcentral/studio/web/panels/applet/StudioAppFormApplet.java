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
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.FormEditorPage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application form applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioAppFormApplet extends AbstractStudioAppComponentApplet {

    public StudioAppFormApplet(Page page, StudioModuleService sms, AppletUtilities au, List<String> pathVariables,
            String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
        createDesign();
    }
    
    public static class Design {

        private FormEditorPage formEditorPage;

        public Design(FormEditorPage formEditorPage) {
            this.formEditorPage = formEditorPage;
        }

        public FormEditorPage getFormEditorPage() {
            return formEditorPage;
        }
        
    }
    
    public void commitDesign() throws UnifyException {
        if (form.getDesign() != null) {
            ((Design) form.getDesign()).getFormEditorPage().commitDesign();
        }
    }

    public void createDesign() throws UnifyException {
        AppForm appForm = (AppForm) form.getFormBean();
        Long formId = appForm.getId();
        if (formId != null) {
            String subTitle = appForm.getDescription();
            FormEditorPage formEditorPage = constructNewFormEditorPage(
                    ApplicationNameUtils.getApplicationEntityLongName(getApplicationName(), appForm.getName()), formId,
                    subTitle);
            formEditorPage.newEditor();
            form.setDesign(new Design(formEditorPage));
        } else {
            form.setDesign(null);
        }
    }

    private FormEditorPage constructNewFormEditorPage(String formName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        FormDef formDef = getFormDef(formName);
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{formeditor.formdesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new FormEditorPage(au(), formDef, id, breadCrumbs);
    }

}
