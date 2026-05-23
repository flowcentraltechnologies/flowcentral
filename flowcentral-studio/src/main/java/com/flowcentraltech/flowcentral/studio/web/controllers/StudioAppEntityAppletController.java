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
package com.flowcentraltech.flowcentral.studio.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.applet.StudioAppEntityApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application entity applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("/studioappentityapplet")
@UplBinding("web/studio/upl/studioappentityappletpage.upl")
public class StudioAppEntityAppletController
        extends AbstractStudioAppComponentAppletController<StudioAppEntityApplet, StudioAppEntityAppletPageBean> {

    public StudioAppEntityAppletController() {
        super(StudioAppEntityAppletPageBean.class);
    }

    @Override
    protected StudioAppEntityApplet createApplet(Page page, StudioModuleService studio, AppletUtilities au,
            List<String> pathVariables, String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        return new StudioAppEntityApplet(page, studio, au, pathVariables, applicationName, appletWidgetReferences,
                formEventHandlers);
    }

}
