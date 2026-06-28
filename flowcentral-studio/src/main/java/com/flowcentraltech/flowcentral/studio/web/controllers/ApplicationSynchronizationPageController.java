/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.ApplicationSynchronizationProvider;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPageController;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Application synchronization page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("/studio/applicationsynchronization")
@UplBinding("web/studio/upl/applicationsynchronizationpage.upl")
public class ApplicationSynchronizationPageController
        extends AbstractFlowCentralPageController<ApplicationSynchronizationPageBean> {

    @Configurable
    private ApplicationSynchronizationProvider provider;
    
    public ApplicationSynchronizationPageController() {
        super(ApplicationSynchronizationPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String start() throws UnifyException {
        logDebug("Starting application synhronization...");
        if (provider != null) {
            provider.synchronizeApplication();
            hintUser("$m{studio.applicationsynchronization.success.hint}");
        } else {
            hintUser(MODE.WARNING, "$m{studio.applicationsynchronization.nocomponent.hint}");
        }
        
        return  noResult();
    }

}
