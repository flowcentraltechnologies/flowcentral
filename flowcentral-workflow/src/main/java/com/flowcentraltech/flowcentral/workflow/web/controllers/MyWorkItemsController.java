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
package com.flowcentraltech.flowcentral.workflow.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.data.WorkItemStep;
import com.flowcentraltech.flowcentral.workflow.data.WorkItemsSlate;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
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
import com.tcdng.unify.web.ui.AbstractPageController;

/**
 * Workflow my work items controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/workflow/myworkitems")
@UplBinding("web/workflow/upl/myworkitems.upl")
@ResultMappings({ @ResultMapping(name = "refreshSlate",
        response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{myWorkItemsSlatePanel}" }) })
public class MyWorkItemsController extends AbstractPageController<MyWorkItemsPageBean> {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private WorkflowModuleService workflowModuleService;

    public MyWorkItemsController() {
        super(MyWorkItemsPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public void setWorkflowModuleService(WorkflowModuleService workflowModuleService) {
        this.workflowModuleService = workflowModuleService;
    }

    @Action
    public String refreshSlate() throws UnifyException {
        return "refreshSlate";
    }

    @Action
    public String loadWorkItemsSlate() throws UnifyException {
        MyWorkItemsPageBean pageBean = getPageBean();
        pageBean.setWorkItemsSlate(null);
        if (!StringUtils.isBlank(pageBean.getSelStepName())) {
            WorkItemsSlate workItemsSlate = null;
            // TODO
            pageBean.setWorkItemsSlate(workItemsSlate);
        }

        return refreshSlate();
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        setPageTitle(resolveSessionMessage("$m{workflowmyworkitems.workitems}"));
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        getPageRequestContextUtil().considerDefaultFocusOnWidget();
        MyWorkItemsPageBean pageBean = getPageBean();
        if (StringUtils.isBlank(pageBean.getSelStepName())) {
            UserToken userToken = getUserToken();
            List<WorkItemStep> stepList = workflowModuleService.findWorkItemStepsByRole(userToken.getRoleCode());
            pageBean.setSelStepName(!stepList.isEmpty() ? stepList.get(0).getLongName() : null);
        }

        loadWorkItemsSlate();
    }

}
