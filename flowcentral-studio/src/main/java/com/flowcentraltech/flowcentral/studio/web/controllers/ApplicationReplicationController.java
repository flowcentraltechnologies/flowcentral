/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPageController;
import com.flowcentraltech.flowcentral.studio.web.panels.ApplicationReplication;
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
 * Application studio controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("/studio/applicationreplication")
@UplBinding("web/studio/upl/applicationreplicationpage.upl")
@ResultMappings({
    @ResultMapping(name = "refresh",
        response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{replicationPanel}" }) })
public class ApplicationReplicationController
        extends AbstractFlowCentralPageController<ApplicationReplicationPageBean> {

    public ApplicationReplicationController() {
        super(ApplicationReplicationPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String clear() throws UnifyException {
        ApplicationReplicationPageBean pageBean = getPageBean();
        pageBean.getReplication().clear();
        return "refresh";
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        ApplicationReplicationPageBean pageBean = getPageBean();
        pageBean.setReplication(new ApplicationReplication());
    }

}
