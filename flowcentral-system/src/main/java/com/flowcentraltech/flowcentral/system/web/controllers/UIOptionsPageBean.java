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

package com.flowcentraltech.flowcentral.system.web.controllers;

import java.util.Arrays;

import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * User interface options page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UIOptionsPageBean extends AbstractPageBean {

    private String loginPageTitle;

    private String loginPageSubtitle;

    private byte[] loginHeaderImage;

    private byte[] loginBackImage;
    
    public String getLoginPageTitle() {
        return loginPageTitle;
    }

    public void setLoginPageTitle(String loginPageTitle) {
        this.loginPageTitle = loginPageTitle;
    }

    public String getLoginPageSubtitle() {
        return loginPageSubtitle;
    }

    public void setLoginPageSubtitle(String loginPageSubtitle) {
        this.loginPageSubtitle = loginPageSubtitle;
    }

    public byte[] getLoginHeaderImage() {
        return loginHeaderImage;
    }

    public void setLoginHeaderImage(byte[] loginHeaderImage) {
        this.loginHeaderImage = loginHeaderImage;
    }

    public byte[] getLoginBackImage() {
        return loginBackImage;
    }

    public void setLoginBackImage(byte[] loginBackImage) {
        this.loginBackImage = loginBackImage;
    }

    @Override
    public String toString() {
        return "UIOptionsPageBean [loginPageTitle=" + loginPageTitle + ", loginPageSubtitle=" + loginPageSubtitle
                + ", loginHeaderImage=" + Arrays.toString(loginHeaderImage) + ", loginBackImage="
                + Arrays.toString(loginBackImage) + "]";
    }
    
}
