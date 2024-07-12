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
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleAuditConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.entities.ApplicationQuery;
import com.flowcentraltech.flowcentral.common.business.LoginUserPhotoGenerator;
import com.flowcentraltech.flowcentral.common.business.UserLoginActivityProvider;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPageController;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.flowcentraltech.flowcentral.studio.web.widgets.StudioMenuWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.ContentPanel;

/**
 * Application studio controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/applicationstudio")
@UplBinding("web/studio/upl/applicationstudio.upl")
@ResultMappings({
        @ResultMapping(name = "showuserdetails", response = { "!showpopupresponse popup:$s{userDetailsPopup}" }),
        @ResultMapping(name = "forwardtohome", response = { "!forwardresponse path:$x{application.web.home}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_TEXT_TEMPLATE_EDITOR,
                response = { "!showpopupresponse popup:$s{textTemplatePopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_QUICK_FORM_EDIT,
                response = { "!showpopupresponse popup:$s{quickFormEditPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_QUICK_TABLE_EDIT,
                response = { "!showpopupresponse popup:$s{quickTableEditPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_QUICK_TABLE_ORDER,
                response = { "!showpopupresponse popup:$s{quickTableOrderPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_PREVIEW_FORM,
                response = { "!showpopupresponse popup:$s{previewFormPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.REFRESH_CONTENT,
                response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{content}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.REFRESH_ALL, response = { "!hidepopupresponse",
                "!refreshpanelresponse panels:$l{topBanner menuColPanel content}" }) })
public class ApplicationStudioController extends AbstractFlowCentralPageController<ApplicationStudioPageBean> {

    @Configurable
    private LoginUserPhotoGenerator userPhotoGenerator;

    @Configurable
    private UserLoginActivityProvider userLoginActivityProvider;

    @Configurable
    private AppletUtilities appletUtilities;

    public ApplicationStudioController() {
        super(ApplicationStudioPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    @Override
    public String content() throws UnifyException {
        appletUtilities.setReloadOnSwitch();
        return ApplicationResultMappingConstants.REFRESH_CONTENT;
    }

    @Action
    public String showUserDetails() throws UnifyException {
        return "showuserdetails";
    }

    @Action
    public String showUtilities() throws UnifyException {
        removeSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        removeSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
        removeSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_DESC);
        removeSessionAttribute(StudioSessionAttributeConstants.CLEAR_PAGES);
        closeAllPages();
        clearCategorySelect();
        return ApplicationResultMappingConstants.REFRESH_ALL;
    }

    @Action
    public String logOut() throws UnifyException {
        logUserEvent(ApplicationModuleAuditConstants.LOGOUT);
        userLoginActivityProvider.logoutUser(true);
        return "forwardtohome";
    }

    @Action
    public String onDeleteApplication() throws UnifyException {
        Long applicationId = (Long) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        if (environment().countAll(new ApplicationQuery().id(applicationId)) == 0) {
            return showUtilities();
        }

        return ApplicationResultMappingConstants.REFRESH_CONTENT;
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        ApplicationStudioPageBean pageBean = getPageBean();
        pageBean.setUserPhotoGenerator(userPhotoGenerator);

        setPageWidgetVisible("businessUnitLabel", isTenancyEnabled());
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        super.onIndexPage();

        Long applicationId = (Long) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        if (!QueryUtils.isValidLongCriteria(applicationId)
                || Boolean.TRUE.equals(removeSessionAttribute(StudioSessionAttributeConstants.CLEAR_PAGES))) {
            ContentPanel contentPanel = getPageWidgetByShortName(ContentPanel.class, "content");
            contentPanel.clearPages();
            clearCategorySelect();
        }
    }

    private void clearCategorySelect() throws UnifyException {
        getPageWidgetByShortName(StudioMenuWidget.class, "studioMenuPanel").setCurrentSel(null);
    }
}
