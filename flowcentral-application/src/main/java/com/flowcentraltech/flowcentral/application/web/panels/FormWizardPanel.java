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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralStandalonePanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Form wizard panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-formwizardpanel")
@UplBinding("web/application/upl/formwizardpanel.upl")
public class FormWizardPanel extends AbstractFlowCentralStandalonePanel {

    @Action
    public void previous() throws UnifyException {
        FormWizard formWizard = getFormWizard();
        formWizard.prevPage();
    }

    @Action
    public void next() throws UnifyException {
        FormWizard formWizard = getFormWizard();
        formWizard.nextPage();
    }
    
    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        
        FormWizard formWizard = getFormWizard();
        setDisabled("prevBtn", formWizard.isFirstPage());
        setDisabled("nextBtn", formWizard.isLastPage());

        setVisible("cancelBtn", !formWizard.isLastPage());
        setVisible("closeBtn", formWizard.isLastPage());
    }
    
    private FormWizard getFormWizard() throws UnifyException {
        return getValue(FormWizard.class);
    }
}
