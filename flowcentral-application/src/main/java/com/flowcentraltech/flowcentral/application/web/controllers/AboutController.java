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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModulePathConstants;
import com.flowcentraltech.flowcentral.application.web.data.About;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPopupPageController;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * About controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(ApplicationModulePathConstants.ABOUT)
public class AboutController extends AbstractFlowCentralPopupPageController<About> {

    private static final int WINDOW_WIDTH_IN_PIXELS = 400;
    
    public AboutController() {
        super("$m{aboutpanel.title}", "fc-aboutpanel", WINDOW_WIDTH_IN_PIXELS);
    }

    @Override
    protected About getPopupBean() throws UnifyException {
        final String applicationCode =  getApplicationCode();
        final String applicationName =  getApplicationName();
        final String applicationVersion = getDeploymentVersion();
        return new About(applicationCode, applicationName, applicationVersion);
    }

}
