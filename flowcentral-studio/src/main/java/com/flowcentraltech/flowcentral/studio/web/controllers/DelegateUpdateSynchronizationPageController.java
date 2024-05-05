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

import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPageController;
import com.flowcentraltech.flowcentral.studio.business.data.DelegateSynchronizationItem;
import com.flowcentraltech.flowcentral.studio.constants.StudioDelegateSynchronizationTaskConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Delegate update synchronization page controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/studio/delegateupdatesynchronization")
@UplBinding("web/studio/upl/delegateupdatesynchronizationpage.upl")
public class DelegateUpdateSynchronizationPageController
        extends AbstractFlowCentralPageController<DelegateUpdateSynchronizationPageBean> {

    public DelegateUpdateSynchronizationPageController() {
        super(DelegateUpdateSynchronizationPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    public String syncDelegate() throws UnifyException {
        DelegateUpdateSynchronizationPageBean pageBean = getPageBean();
        DelegateSynchronizationItem delegateSynchronizationItem = new DelegateSynchronizationItem();
        delegateSynchronizationItem.setDelegate(pageBean.getDelegate());
        TaskSetup taskSetup = TaskSetup
                .newBuilder(StudioDelegateSynchronizationTaskConstants.DELEGATE_UPDATE_SYNCHRONIZATION_TASK_NAME)
                .setParam(StudioDelegateSynchronizationTaskConstants.DELEGATE_SYNCHRONIZATION_ITEM,
                        delegateSynchronizationItem)
                .logMessages().build();
        return launchTaskWithMonitorBox(taskSetup, "Delegate Update Synchronization", null, null);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        DelegateUpdateSynchronizationPageBean pageBean = getPageBean();
        pageBean.setDelegate(null);
    }

}
