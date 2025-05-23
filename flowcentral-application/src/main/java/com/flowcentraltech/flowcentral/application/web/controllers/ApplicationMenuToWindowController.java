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

import java.util.Map;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletSessionAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModulePathConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.RequestOpenTabInfo;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageController;

/**
 * Application menu-to-window controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(ApplicationModulePathConstants.APPLICATION_MENU_TO_WINDOW)
@UplBinding("web/application/upl/applicationmenutowindow.upl")
@ResultMappings({ @ResultMapping(name = ApplicationResultMappingConstants.OPEN_IN_NEW_BROWSER_WINDOW,
        response = { "!openinbrowserwindowresponse" }) })
public class ApplicationMenuToWindowController extends AbstractPageController<ApplicationMenuToWindowPageBean> {

    public ApplicationMenuToWindowController() {
        super(ApplicationMenuToWindowPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @SuppressWarnings("unchecked")
    @Action
    public String openInBrowserWindow() throws UnifyException {
        final String pathVariable = ApplicationPageUtils.mergePathVariables(getPathVariables());
        RequestOpenTabInfo requestOpenTabInfo = ((Map<String, RequestOpenTabInfo>) getSessionAttribute(
                AppletSessionAttributeConstants.MENU_OPEN_TAB_INFO)).get(pathVariable);
        setRequestAttribute(AppletRequestAttributeConstants.OPEN_TAB_INFO, requestOpenTabInfo);
        return ApplicationResultMappingConstants.OPEN_IN_NEW_BROWSER_WINDOW;
    }

}
