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

package com.flowcentraltech.flowcentral.application.web.responses;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletSessionAttributeConstants;
import com.flowcentraltech.flowcentral.application.data.RequestOpenTabInfo;
import com.flowcentraltech.flowcentral.application.data.SessionOpenTabInfo;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.web.ui.AbstractOpenWindowPageControllerResponse;

/**
 * Manage entity open tab response.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("manageentityopentabresponse")
public class ManageEntityOpenTabResponse extends AbstractOpenWindowPageControllerResponse {

    @Override
    protected WindowResourceInfo prepareWindowResource() throws UnifyException {
        final RequestOpenTabInfo requestOpenTabInfo = (RequestOpenTabInfo) getRequestAttribute(
                AppletRequestAttributeConstants.OPEN_TAB_INFO);
        if (requestOpenTabInfo != null) {
            logDebug("Preparing open tab for path [{0}] ...", requestOpenTabInfo.getContentPath());
            final String docpath = ApplicationPageUtils.constructAppletPath("/application/browserwindow",
                    requestOpenTabInfo.getTabName());
            setSessionAttribute(AppletSessionAttributeConstants.OPEN_TAB_INFO, new SessionOpenTabInfo(
                    requestOpenTabInfo.getTitle(), docpath, requestOpenTabInfo.getContentPath()));
            WindowResourceInfo info = new WindowResourceInfo(requestOpenTabInfo.getContentPath(), docpath,
                    requestOpenTabInfo.getTabName(), MimeType.TEXT_HTML.template(), false);
            info.setOpenInTab(true);
            return info;
        }

        return new WindowResourceInfo();
    }
}
