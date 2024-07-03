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

import java.util.Optional;

import com.flowcentraltech.flowcentral.application.constants.AppletSessionAttributeConstants;
import com.flowcentraltech.flowcentral.application.data.SessionOpenTabInfo;
import com.flowcentraltech.flowcentral.common.business.SecuredLinkManager;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.SecuredLinkContentInfo;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractForwarderController;
import com.flowcentraltech.flowcentral.security.business.SecurityModuleService;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleNameConstants;
import com.flowcentraltech.flowcentral.security.entities.UserRole;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Secured link access controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(SecurityModuleNameConstants.SECURED_LINK_ACCESS_CONTROLLER)
@UplBinding("web/security/upl/securedlinkaccess.upl")
@ResultMappings({
        @ResultMapping(type = MimeType.TEXT_HTML, name = "forwardtourl",
                response = { "!externalforwardresponse pathBinding:$s{targetPath}" }),
        @ResultMapping(type = MimeType.TEXT_HTML, name = "plainmessage",
                response = { "!plainhtmlresponse htmlBinding:$s{message}" }) })
public class SecuredLinkAccessController extends AbstractForwarderController<SecuredLinkAccessPageBean> {

    @Configurable
    private SecuredLinkManager securedLinkManager;

    @Configurable
    private SecurityModuleService securityModuleService;

    public SecuredLinkAccessController() {
        super(SecuredLinkAccessPageBean.class, Secured.FALSE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        final String lid = getHttpRequestParameter("lid");
        logDebug("Accessing secured item with id [{0}]...", lid);
        if (!StringUtils.isBlank(lid)) {
            final SecuredLinkContentInfo securedLinkContentInfo = securedLinkManager.getSecuredLink(lid);
            if (!securedLinkContentInfo.isPresent()) {
                showPlainMessage(resolveSessionMessage("$m{securedlinkaccess.unresolvable}"));
                return;
            }

            if (securedLinkContentInfo.isExpired()) {
                showPlainMessage(resolveSessionMessage("$m{securedlinkaccess.expired}"));
                return;
            }

            String url = securedLinkContentInfo.getLoginUrl();
            if (isUserLoggedIn()) {
                UserToken userToken = getUserToken();
                if (securedLinkContentInfo.isWithAssignedLoginId()
                        && !securedLinkContentInfo.getAssignedLoginId().equals(userToken.getUserLoginId())) {
                    showPlainMessage(resolveSessionMessage("$m{securedlinkaccess.assignedtoother}"));
                    return;
                }

                if (securedLinkContentInfo.isWithAssignedRole()) {
                    Optional<UserRole> optional = securityModuleService.findUserRole(
                            securedLinkContentInfo.getAssignedLoginId(), securedLinkContentInfo.getAssignedRole());
                    if (!optional.isPresent()) {
                        showPlainMessage(resolveSessionMessage("$m{securedlinkaccess.norequiredrole}"));
                        return;
                    }
                }

                url = securedLinkContentInfo.getDocUrl();
                setSessionAttribute(AppletSessionAttributeConstants.OPEN_TAB_INFO,
                        new SessionOpenTabInfo(securedLinkContentInfo.getTitle(), securedLinkContentInfo.getDocUrl(),
                                securedLinkContentInfo.getContentPath()));
            } else {
                setSessionAttribute(FlowCentralSessionAttributeConstants.SECURED_LINK_ACCESS, securedLinkContentInfo);
            }

            replacePage(url);
            logDebug("Forwarding secured access to URL [{0}]...", url);
            return;
        }

        showPlainMessage(resolveSessionMessage("$m{securedlinkaccess.itemnotfound}"));
    }

    private void showPlainMessage(String msg) throws UnifyException {
        SecuredLinkAccessPageBean pageBean = getPageBean();
        pageBean.setMessage(resolveSessionMessage(msg));
        setResultMapping("plainmessage");
    }

    private void replacePage(String targetPath) throws UnifyException {
        SecuredLinkAccessPageBean pageBean = getPageBean();
        pageBean.setTargetPath(targetPath);
        setResultMapping("forwardtourl");
    }

}
