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

import com.flowcentraltech.flowcentral.security.business.data.PasswordComplexitySettings;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Password complexity controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/security/passwordcomplexity")
@UplBinding("web/security/upl/passwordcomplexity.upl")
@ResultMappings({
    @ResultMapping(name = "refresh", response = { "!refreshpanelresponse panels:$l{pwdComplexityPanel}" })})
public class PasswordComplexityController extends AbstractSecurityPageController<PasswordComplexityPageBean> {

    public PasswordComplexityController() {
        super(PasswordComplexityPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        setPageTitle(resolveSessionMessage("$m{security.passwordcomplexity}"));
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        PasswordComplexityPageBean pageBean = getPageBean();

        setPageTitle(resolveSessionMessage("$m{security.passwordcomplexity}"));
        PasswordComplexitySettings settings = security().getPasswordComplexity();
        pageBean.setSettings(settings);
    }

    @Action
    public String apply() throws UnifyException {
        PasswordComplexityPageBean pageBean = getPageBean();
        PasswordComplexitySettings settings = pageBean.getSettings();
        security().savePasswordComplexity(settings);
        hintUser("$m{security.passwordcomplexity.applysuccess}");
        return "refresh";
    }

    @Action
    public String clear() throws UnifyException {
        PasswordComplexityPageBean pageBean = getPageBean();
        PasswordComplexitySettings settings = pageBean.getSettings();
        settings.clear();
        return "refresh";
    }
}
