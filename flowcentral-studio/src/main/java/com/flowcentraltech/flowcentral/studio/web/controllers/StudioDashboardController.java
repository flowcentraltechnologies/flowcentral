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
package com.flowcentraltech.flowcentral.studio.web.controllers;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.studio.constants.StudioApplicationConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.flowcentraltech.flowcentral.studio.web.data.CreateAppForm;
import com.flowcentraltech.flowcentral.system.entities.Module;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.data.IndexedTarget;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.AbstractPageController;

/**
 * Studio dashboard controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/applicationstudio/dashboard")
@UplBinding("web/studio/upl/studiodashboard.upl")
@ResultMappings({ @ResultMapping(name = "showswitchapplication",
        response = { "!switchpanelresponse panels:$l{switchApplicationPopup.searchBaseBodyPanel.entitySearchPanel}",
                "!showpopupresponse popup:$s{switchApplicationPopup}" }),
        @ResultMapping(name = "reloadapplicationstudio", response = { "!forwardresponse path:$s{/applicationstudio}" }),
        @ResultMapping(name = "cancelswitchapplication", response = { "!hidepopupresponse" }),
        @ResultMapping(name = "switchsearchapplication",
                response = {
                        "!switchpanelresponse panels:$l{switchApplicationPopup.searchBaseBodyPanel.entitySearchPanel}",
                        "!refreshpanelresponse panels:$l{switchApplicationPopup.entitySearchActionPanel}" }),
        @ResultMapping(name = "switchcreateapplication", response = {
                "!switchpanelresponse panels:$l{switchApplicationPopup.searchBaseBodyPanel.createApplicationBasePanel}",
                "!refreshpanelresponse panels:$l{switchApplicationPopup.entitySearchActionPanel}" }) })
public class StudioDashboardController extends AbstractPageController<StudioDashboardPageBean> {

    @Configurable
    private AppletUtilities appletUtils;

    public StudioDashboardController() {
        super(StudioDashboardPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    public final void setAppletUtils(AppletUtilities appletUtils) {
        this.appletUtils = appletUtils;
    }

    @Action
    public String showSwitchApplication() throws UnifyException {
        StudioDashboardPageBean pageBean = getPageBean();
        pageBean.getSwitchApplicationSearch().applyFilterToSearch();
        return "showswitchapplication";
    }

    @Action
    public String openApplication() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidIndex()) {
            Application _inst = (Application) getPageBean().getSwitchApplicationSearch().getEntityTable()
                    .getDispItemList().get(target.getIndex());
            setApplicationSessionAttributes(_inst);
        }

        return "reloadapplicationstudio";
    }

    @Action
    public String createApplication() throws UnifyException {
        StudioDashboardPageBean pageBean = getPageBean();
        CreateAppForm createAppForm = pageBean.getCreateAppForm();

        Module module = null;
        if (createAppForm.isCreateModule()) {
            module = new Module();
            module.setName(createAppForm.getModuleName());
            module.setDescription(createAppForm.getModuleDesc());
            module.setLabel(createAppForm.getModuleLabel());
            module.setShortCode(createAppForm.getModuleShortCode());
        }

        Application application = new Application();
        application.setModuleId(createAppForm.getModuleId());
        application.setName(createAppForm.getApplicationName());
        application.setDescription(createAppForm.getApplicationDesc());
        application.setLabel(createAppForm.getApplicationLabel());
        application.setDevelopable(true);
        application.setMenuAccess(true);
        appletUtils.application().createApplication(application, module);
        setApplicationSessionAttributes(application);
        return "reloadapplicationstudio";
    }

    @Action
    public String prepareNewApplication() throws UnifyException {
        StudioDashboardPageBean pageBean = getPageBean();
        pageBean.setCreateAppForm(new CreateAppForm());
        return "switchcreateapplication";
    }

    @Action
    public String cancelSwitchApplication() throws UnifyException {
        if (isPageWidgetVisible("switchApplicationPopup.createAppBtn")) {
            return "switchsearchapplication";
        }

        return "cancelswitchapplication";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        StudioDashboardPageBean pageBean = getPageBean();
        if (pageBean.getSwitchApplicationSearch() == null) {
            TableDef _tableDef = appletUtils.getTableDef(StudioApplicationConstants.APPLICATION_SEARCH_TABLE);

            EntitySearch _switchApplicationSearch = new EntitySearch(
                    new FormContext(new AppletContext(null, appletUtils)), null, null, null, _tableDef, null, null,
                    null, null, 4, EntitySearch.ENABLE_ALL & ~(EntitySearch.SHOW_EDIT_BUTTON), false, false, false);
            _switchApplicationSearch
                    .setPaginationLabel(appletUtils.resolveSessionMessage("$m{entitysearch.display.label}"));
            _switchApplicationSearch.setEntitySubTitle("Application");
            _switchApplicationSearch.setBaseRestriction(new Equals("developable", Boolean.TRUE),
                    appletUtils.specialParamProvider());
            pageBean.setSwitchApplicationSearch(_switchApplicationSearch);
        }

        pageBean.getSwitchApplicationSearch().applyFilterToSearch();

        Long applicationId = (Long) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        if (!QueryUtils.isValidLongCriteria(applicationId)) {
            getPageRequestContextUtil().setRequestPopupName("switchApplicationPopup");
        }
    }

    private void setApplicationSessionAttributes(Application application) throws UnifyException {
        setSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID, application.getId());
        setSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME, application.getName());
        setSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_DESC, application.getDescription());
        setSessionAttribute(StudioSessionAttributeConstants.CLEAR_PAGES, Boolean.TRUE);
    }
}
