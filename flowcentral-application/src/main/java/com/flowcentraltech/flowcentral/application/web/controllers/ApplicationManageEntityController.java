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

import com.flowcentraltech.flowcentral.application.constants.AppletSessionAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Singleton;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageController;

/**
 * Application manage entity controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Singleton(false)
@Component("/application/manageentity")
@UplBinding("web/application/upl/applicationmanageentity.upl")
@ResultMappings({
    @ResultMapping(name = ApplicationResultMappingConstants.REFRESH_CONTENT,
            response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{content}" }) })
public class ApplicationManageEntityController extends AbstractPageController<ApplicationManageEntityPageBean> {

    public ApplicationManageEntityController() {
        super(ApplicationManageEntityPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    @Override
    public String content() throws UnifyException {
        return ApplicationResultMappingConstants.REFRESH_CONTENT;
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        ApplicationManageEntityPageBean pageBean = getPageBean();
        if (pageBean.getDocumentPath() == null) {
            final String documentPath = (String) removeSessionAttribute(AppletSessionAttributeConstants.OPEN_DOC_PATH);
            final String contentPath = (String) removeSessionAttribute(AppletSessionAttributeConstants.OPEN_TAB_PATH);
            pageBean.setDocumentPath(documentPath);
            pageBean.setContentPaths(new String[] { contentPath });
        }
    }

}
