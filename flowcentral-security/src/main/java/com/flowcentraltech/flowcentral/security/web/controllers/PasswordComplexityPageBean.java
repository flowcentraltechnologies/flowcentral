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
import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Password complexity page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PasswordComplexityPageBean extends AbstractPageBean {

    private PasswordComplexitySettings settings;

    public PasswordComplexitySettings getSettings() {
        return settings;
    }

    public void setSettings(PasswordComplexitySettings settings) {
        this.settings = settings;
    }
    
}
