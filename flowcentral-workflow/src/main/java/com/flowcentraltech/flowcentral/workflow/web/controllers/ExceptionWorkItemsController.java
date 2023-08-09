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

import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.WorkflowLoadingTableInfo;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AbstractEntityFormAppletController;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.web.panels.applet.ExceptionWorkItemsApplet;
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

/**
 * Workflow exception work items controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/workflow/exceptionworkitems")
@UplBinding("web/workflow/upl/exceptionworkitems.upl")
@ResultMappings({ @ResultMapping(name = "refreshSlate",
        response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{workItemsSlatePanel}" }) })
public class ExceptionWorkItemsController extends AbstractEntityFormAppletController<ExceptionWorkItemsApplet, ExceptionWorkItemsPageBean> {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    private AppletWidgetReferences appletWidgetReferences;

    private EntityFormEventHandlers formEventHandlers;

    public ExceptionWorkItemsController() {
        super(ExceptionWorkItemsPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public final void setWorkflowModuleService(WorkflowModuleService workflowModuleService) {
        this.workflowModuleService = workflowModuleService;
    }

    @Action
    public String refreshSlate() throws UnifyException {
        return "refreshSlate";
    }

    @Action
    public String loadWorkItemsSlate() throws UnifyException {
        ExceptionWorkItemsPageBean pageBean = getPageBean();
        if (!StringUtils.isBlank(pageBean.getSelLoadingTableName())) {
            final ApplicationEntityNameParts tnp = ApplicationNameUtils
                    .getApplicationEntityNameParts(pageBean.getSelLoadingTableName());
            final String loadinAppletName = ApplicationNameUtils.getWorkflowLoadingAppletName(tnp.getApplicationName(),
                    tnp.getEntityName());
            UserToken userToken = getUserToken();
            ExceptionWorkItemsApplet applet = new ExceptionWorkItemsApplet(workflowModuleService, pageBean.getSelLoadingTableName(),
                    userToken.getRoleCode(), au(), loadinAppletName, appletWidgetReferences, formEventHandlers);
            pageBean.setApplet(applet);
            setPageWidgetVisible("appletPanel", true);
        } else {
            pageBean.setApplet(null);
            setPageWidgetVisible("appletPanel", false);
        }

        return refreshSlate();
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        setPageTitle(resolveSessionMessage("$m{workflowexceptionworkitems.workitems}"));
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        getPageRequestContextUtil().considerDefaultFocusOnWidget();
        ExceptionWorkItemsPageBean pageBean = getPageBean();

        if (StringUtils.isBlank(pageBean.getSelLoadingTableName())) {
            UserToken userToken = getUserToken();
            List<WorkflowLoadingTableInfo> loadingTableInfoList = workflowModuleService
                    .findWorkflowLoadingTableInfoByRole(userToken.getRoleCode());
            pageBean.setSelLoadingTableName(
                    !loadingTableInfoList.isEmpty() ? loadingTableInfoList.get(0).getLongName() : null);
        }

        if (appletWidgetReferences == null) {
            appletWidgetReferences = getAppletWidgetReferences();
            formEventHandlers = getEntityFormEventHandlers();
        }

        loadWorkItemsSlate();
    }

}
