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

package com.flowcentraltech.flowcentral.security.web.controllers;

import com.flowcentraltech.flowcentral.common.business.SecuredLinkManager;
import com.flowcentraltech.flowcentral.common.data.SecuredLinkContentInfo;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageController;

/**
 * Secured link access controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(SecurityModuleNameConstants.SECURED_LINK_ACCESS_CONTROLLER)
@UplBinding("web/security/upl/securedlinkaccess.upl")
public class SecuredLinkAccessController extends AbstractPageController<SecuredLinkAccessPageBean> {

    @Configurable
    private SecuredLinkManager securedLinkManager;

    public SecuredLinkAccessController() {
        super(SecuredLinkAccessPageBean.class, Secured.FALSE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        SecuredLinkAccessPageBean pageBean = getPageBean();
        final String lid = getHttpRequestParameter("lid");
        final SecuredLinkContentInfo securedLinkContentInfo = securedLinkManager.getSecuredLink(lid);
        if (!securedLinkContentInfo.isPresent()) {
            pageBean.setMessage(resolveSessionMessage("$m{securedlinkaccess.unresolvable}"));
        } else if (securedLinkContentInfo.isExpired()) {
            pageBean.setMessage(resolveSessionMessage("$m{securedlinkaccess.expired}"));
        } else {
            UserToken userToken = getUserToken();
            if (!isUserLoggedIn()) {
                
            }
            
            // TODO
            pageBean.setMessage("TODO");
        }
    }

}
