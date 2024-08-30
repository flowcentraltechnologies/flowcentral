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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageController;

/**
 * Studio application dashboard controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/applicationstudio/dashboard")
@UplBinding("web/studio/upl/studiodashboard.upl")
//@ResultMappings({
//    @ResultMapping(name = "showcreateapplication",
//        response = {"!showpopupresponse popup:$s{createApplicationPopup}" }),
//    @ResultMapping(name = "reloadapplicationstudio", response = { "!forwardresponse path:$s{/applicationstudio}" }),
//    @ResultMapping(name = "cancelnewapplication", response = { "!hidepopupresponse" }),
//    @ResultMapping(name = "switchsearchapplication",
//                response = {
//                        "!refreshpanelresponse panels:$l{createApplicationPopup}" }),
//    @ResultMapping(name = "newcreateapplication", response = {
//                "!refreshpanelresponse panels:$l{switchApplicationPopup.entitySearchActionPanel}" }) })
public class ApplicationDashboardController extends AbstractPageController<ApplicationDashboardPageBean> {

    @Configurable
    private AppletUtilities appletUtils;

    public ApplicationDashboardController() {
        super(ApplicationDashboardPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

//    @Action
//    public String showCreateApplication() throws UnifyException {
//        ApplicationDashboardPageBean pageBean = getPageBean();
//        pageBean.setCreateAppForm(new CreateAppForm());
//        return "showcreateapplication";
//    }
//
//    @Action
//    public String openApplication() throws UnifyException {
//        IndexedTarget target = getRequestTarget(IndexedTarget.class);
//        if (target.isValidIndex()) {
//            Application _inst = (Application) getPageBean().getSwitchApplicationSearch().getEntityTable()
//                    .getDispItemList().get(target.getIndex());
//            setApplicationSessionAttributes(_inst);
//        }
//
//        return "reloadapplicationstudio";
//    }
//
//    @Action
//    public String createApplication() throws UnifyException {
//        ApplicationDashboardPageBean pageBean = getPageBean();
//        CreateAppForm createAppForm = pageBean.getCreateAppForm();
//
//        Module module = null;
//        if (createAppForm.isCreateModule()) {
//            module = new Module();
//            module.setName(createAppForm.getModuleName());
//            module.setDescription(createAppForm.getModuleDesc());
//            module.setLabel(createAppForm.getModuleLabel());
//            module.setShortCode(createAppForm.getModuleShortCode());
//        }
//
//        Application application = new Application();
//        application.setModuleId(createAppForm.getModuleId());
//        application.setName(createAppForm.getApplicationName());
//        application.setDescription(createAppForm.getApplicationDesc());
//        application.setLabel(createAppForm.getApplicationLabel());
//        application.setDevelopable(true);
//        application.setMenuAccess(true);
//        appletUtils.application().createApplication(application, module);
//        setApplicationSessionAttributes(application);
//        return "reloadapplicationstudio";
//    }
//
//    @Action
//    public String prepareNewApplication() throws UnifyException {
//        ApplicationDashboardPageBean pageBean = getPageBean();
//        pageBean.setCreateAppForm(new CreateAppForm());
//        return "switchcreateapplication";
//    }
//
//    @Action
//    public String cancelNewApplication() throws UnifyException {
//        return "cancelnewapplication";
//    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

    }
//
//    private void setApplicationSessionAttributes(Application application) throws UnifyException {
//        setSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID, application.getId());
//        setSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME, application.getName());
//        setSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_DESC, application.getDescription());
//        setSessionAttribute(StudioSessionAttributeConstants.CLEAR_PAGES, Boolean.TRUE);
//    }
}
