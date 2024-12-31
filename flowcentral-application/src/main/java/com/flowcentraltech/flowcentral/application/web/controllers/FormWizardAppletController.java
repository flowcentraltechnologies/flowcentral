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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.web.panels.applet.FormWizardApplet;
import com.flowcentraltech.flowcentral.configuration.constants.FlowCentralAppletPathConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Form wizard applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(FlowCentralAppletPathConstants.FORM_WIZARD)
@UplBinding("web/application/upl/formwizardappletpage.upl")
public class FormWizardAppletController extends AbstractAppletController<FormWizardAppletPageBean> {

    public FormWizardAppletController() {
        super(FormWizardAppletPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        FormWizardAppletPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            FormWizardApplet applet = new FormWizardApplet(getPage(), au(), getPathVariables());
            pageBean.setApplet(applet);
            if (pageBean.getAltCaption() == null) {
                setPageTitle(applet);
            }
            
            applet.setClosePath(FlowCentralAppletPathConstants.FORM_WIZARD + "/closePage");
        }
    }

}
