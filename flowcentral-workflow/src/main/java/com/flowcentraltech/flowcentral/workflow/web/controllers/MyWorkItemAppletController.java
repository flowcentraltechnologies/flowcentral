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
package com.flowcentraltech.flowcentral.workflow.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AbstractLoadingAppletController;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.configuration.constants.FlowCentralAppletPathConstants;
import com.flowcentraltech.flowcentral.workflow.web.panels.applet.MyWorkItemApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Singleton;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * My work item applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Singleton(false)
@Component(FlowCentralAppletPathConstants.MY_WORKITEM)
@UplBinding("web/workflow/upl/myworkitemappletpage.upl")
public class MyWorkItemAppletController
        extends AbstractLoadingAppletController<MyWorkItemApplet, MyWorkItemAppletPageBean> {

    public MyWorkItemAppletController() {
        super(MyWorkItemAppletPageBean.class);
    }

    @Override
    protected MyWorkItemApplet createAbstractLoadingApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        return new MyWorkItemApplet(page, au, pathVariables, appletWidgetReferences,
                formEventHandlers);
    }

}
