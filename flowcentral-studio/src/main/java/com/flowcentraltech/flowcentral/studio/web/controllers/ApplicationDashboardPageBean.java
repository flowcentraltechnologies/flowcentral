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
package com.flowcentraltech.flowcentral.studio.web.controllers;

import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.studio.web.data.CreateAppForm;
import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Studio application dashboard page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationDashboardPageBean extends AbstractPageBean {

    private EntitySearch switchApplicationSearch;

    private CreateAppForm createAppForm;

    public EntitySearch getSwitchApplicationSearch() {
        return switchApplicationSearch;
    }

    public void setSwitchApplicationSearch(EntitySearch switchApplicationSearch) {
        this.switchApplicationSearch = switchApplicationSearch;
    }

    public CreateAppForm getCreateAppForm() {
        return createAppForm;
    }

    public void setCreateAppForm(CreateAppForm createAppForm) {
        this.createAppForm = createAppForm;
    }
}
