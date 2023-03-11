/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.security.business.SecurityModuleService;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleAuditConstants;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Change password controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/security/changepassword")
@UplBinding("web/security/upl/changepassword.upl")
@ResultMappings({
    @ResultMapping(name = "refresh", response = { "!refreshpanelresponse panels:$l{changePasswordPanel}" })})
public class ChangePasswordController extends AbstractSecurityPageController<ChangePasswordPageBean> {

    @Configurable
    private SecurityModuleService securityModuleService;

    public ChangePasswordController() {
        super(ChangePasswordPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public void setSecurityModuleService(SecurityModuleService securityModuleService) {
        this.securityModuleService = securityModuleService;
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        setPageTitle(resolveSessionMessage("$m{security.changepassword}"));
        ChangePasswordPageBean pageBean = getPageBean();
        pageBean.setAltSubCaption(resolveSessionMessage("$m{security.mypassword}"));
    }

    @Action
    public String changePassword() throws UnifyException {
        ChangePasswordPageBean pageBean = getPageBean();
        if (StringUtils.isBlank(pageBean.getOldPassword()) || StringUtils.isBlank(pageBean.getNewPassword())
                || StringUtils.isBlank(pageBean.getConfirmPassword())) {
            hintUser(MODE.ERROR, "$m{security.application.allpasswordfields.required}");
        } else if (pageBean.getOldPassword().equals(pageBean.getNewPassword())) {
            hintUser(MODE.ERROR, "$m{security.application.newandoldpassword.same}");
        } else if (!pageBean.getConfirmPassword().equals(pageBean.getNewPassword())) {
            hintUser(MODE.ERROR, "$m{security.application.newandconfirmpassword.notsame}");
        } else {
            try {
                securityModuleService.changeUserPassword(pageBean.getOldPassword(), pageBean.getNewPassword());
                logUserEvent(SecurityModuleAuditConstants.CHANGE_PASSWORD);
                pageBean.setOldPassword(null);
                pageBean.setNewPassword(null);
                pageBean.setConfirmPassword(null);
                hintUser("$m{security.changepassword.hint.success}");
            } catch (UnifyException e) {
                UnifyError err = e.getUnifyError();
                hintUser(MODE.ERROR, getSessionMessage(err.getErrorCode(), err.getErrorParams()));
            }
        }

        return "refresh";
    }
}
