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
package com.flowcentraltech.flowcentral.workflow.web.controllers;

import com.flowcentraltech.flowcentral.application.web.controllers.AbstractEntityFormAppletPageBean;
import com.flowcentraltech.flowcentral.workflow.web.panels.applet.ReviewWizardWorkItemsApplet;
import com.tcdng.unify.core.UnifyException;

/**
 * Review wizard work items applet page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ReviewWizardWorkItemsAppletPageBean extends AbstractEntityFormAppletPageBean<ReviewWizardWorkItemsApplet> {

    public ReviewWizardWorkItemsApplet getApplet() {
        return super.getApplet();
    }

    @Override
    public void setApplet(ReviewWizardWorkItemsApplet applet)  throws UnifyException {
        super.setApplet(applet);
    }
}
