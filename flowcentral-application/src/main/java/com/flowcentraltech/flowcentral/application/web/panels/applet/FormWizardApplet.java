/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.web.panels.FormWizard;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * For wizard applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormWizardApplet extends AbstractApplet {

    private FormWizard formWizard;
    
    public FormWizardApplet(Page page, AppletUtilities au, List<String> pathVariables) throws UnifyException {
        super(page, au, pathVariables.get(APPLET_NAME_INDEX));
        
        final AppletDef appletDef = getRootAppletDef();
        final String formName = appletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM);
        final FormDef formDef = au.getFormDef(formName);
        final Entity inst = au.application().getEntityClassDef(appletDef.getEntity()).newInst();
        this.formWizard = au.constructFormWizard(this, formDef, inst);
    }

    public FormWizard getFormWizard() {
        return formWizard;
    }

}
