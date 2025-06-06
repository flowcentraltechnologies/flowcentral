/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleAuditConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModulePathConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.common.business.LicenseProvider;
import com.flowcentraltech.flowcentral.common.business.LoginUserPhotoGenerator;
import com.flowcentraltech.flowcentral.common.business.UserLoginActivityProvider;
import com.flowcentraltech.flowcentral.common.business.WorkspacePrivilegeManager;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.UserRoleInfo;
import com.flowcentraltech.flowcentral.common.data.UserRoleOptions;
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
import com.tcdng.unify.web.ui.widget.ContentPanel;
import com.tcdng.unify.web.ui.widget.control.Table;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Application controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(ApplicationModulePathConstants.APPLICATION)
@UplBinding("web/application/upl/application.upl")
@ResultMappings({
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_USERROLE_OPTIONS,
                response = { "!showpopupresponse popup:$s{userRoleOptionsPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_USER_DETAILS,
                response = { "!showpopupresponse popup:$s{userDetailsPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_ENTITY_SELECT,
                response = { "!showpopupresponse popup:$s{entitySelectPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_ENTITY_MULTISELECT,
                response = { "!showpopupresponse popup:$s{entityMultiselectPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_ENTITY_TREEMULTISELECT,
                response = { "!showpopupresponse popup:$s{entityTreeMultiselectPopup}" }),
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
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_HELP_FORM,
            response = { "!showpopupresponse popup:$s{helpFormPopup}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.FORWARD_TO_HOME,
                response = { "!forwardresponse path:$x{application.web.home}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.REFRESH_WORKSPACE,
                response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{menuPanel content}" }),
        @ResultMapping(name = ApplicationResultMappingConstants.REFRESH_CONTENT,
                response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{content}" }) })
public class ApplicationController extends AbstractApplicationForwarderController<ApplicationPageBean> {
 
    @Configurable
    private WorkspacePrivilegeManager wkspPrivilegeManager;

    @Configurable
    private UserLoginActivityProvider userLoginActivityProvider;

    @Configurable
    private LicenseProvider licenseProvider;

    public ApplicationController() {
        super(ApplicationPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Action
    @Override
    public String content() throws UnifyException {
        setReloadOnSwitch();
        return ApplicationResultMappingConstants.REFRESH_CONTENT;
    }

    @Action
    public String changeWorkspace() throws UnifyException {
        ContentPanel contentPanel = getPageWidgetByShortName(ContentPanel.class, "content");
        contentPanel.clearPages();
        setSessionWorkspace();
        return ApplicationResultMappingConstants.REFRESH_WORKSPACE;
    }

    @Action
    public String resolveUserNotification() throws UnifyException {
        return "resolveusernotification";
    }

    @Action
    public String dismissUserNotification() throws UnifyException {
        return hidePopup();
    }

    @Action
    public String dismissUserNotifications() throws UnifyException {
        return cancelUserNotifications();
    }

    @Action
    public String cancelUserNotifications() throws UnifyException {
        return hidePopup();
    }

    @Action
    public String logOut() throws UnifyException {
        logUserEvent(ApplicationModuleAuditConstants.LOGOUT);
        userLoginActivityProvider.logoutUser(true);
        return ApplicationResultMappingConstants.FORWARD_TO_HOME;
    }

    @Action
    public String showUserNotifications() throws UnifyException {
        return noResult();
    }

    @Action
    public String refreshContent() throws UnifyException {
        setReloadOnSwitch();
        return ApplicationResultMappingConstants.REFRESH_CONTENT;
    }

    @Action
    public String prepareUserRoleOptions() throws UnifyException {
        UserToken userToken = getUserToken();
        return showPopup(new Popup(ApplicationResultMappingConstants.SHOW_USERROLE_OPTIONS,
                new UserRoleOptions(userLoginActivityProvider.getAvailableUserRolesActiveNow(userToken.getUserLoginId(),
                        userToken.getRoleCode()))));
    }

    @Action
    public String showUserDetails() throws UnifyException {
        return ApplicationResultMappingConstants.SHOW_USER_DETAILS;
    }

    @Action
    public String switchUserRole() throws UnifyException {
        Popup popup = getCurrentPopup();
        UserRoleOptions userRoleOptions = (UserRoleOptions) popup.getBackingBean();
        UserRoleInfo userRoleInfo = userRoleOptions.getUserRoleList().get(
                getPageWidgetByShortName(Table.class, "userRoleOptionsPopup.roleTablePanel.contentTbl").getViewIndex());
        ContentPanel contentPanel = getPageWidgetByShortName(ContentPanel.class, "content");
        contentPanel.clearPages();
        removeCurrentPopup();
        return forwardToApplication(userRoleInfo);
    }

    @Override
    protected void onInitPage() throws UnifyException {
        super.onInitPage();
        ApplicationPageBean pageBean = getPageBean();
        LoginUserPhotoGenerator userPhotoGenerator = (LoginUserPhotoGenerator) getComponent(
                ApplicationModuleNameConstants.LOGIN_USER_PHOTO_GENERATOR);
        pageBean.setUserPhotoGenerator(userPhotoGenerator);

        final boolean enableStickyPaths = system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_STICKY_WORK_ITEMS_PAGE);
        final boolean enableMultipleTabs = system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_MULTIPLE_TABS);
        final boolean indicateHighLatency = system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_HIGH_LATENCY_INDICATION);
        final boolean showWorkItemsOnLogin = system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SHOW_WORK_ITEMS_ON_LOGIN);
        final boolean clientUpdateSync =  system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.GLOBAL_CLIENT_UPDATE_SYNCHRONIZATION);
        final String contentStyleClass = enableMultipleTabs ? "fc-content-tabbed" : "fc-content";
        pageBean.setEnableStickyPaths(enableStickyPaths);
        pageBean.setEnableMultipleTabs(enableMultipleTabs);
        pageBean.setIndicateHighLatency(indicateHighLatency);
        pageBean.setContentStyleClass(contentStyleClass);
        pageBean.setClientPushSync(clientUpdateSync);

        UserToken userToken = getUserToken();
        final int workitemCategoryParticipationCount = userToken != null? application()
                .getWorkitemCategoryParticipationCount(userToken.getRoleCode()) : 0;
        String[] applicationPaths = showWorkItemsOnLogin && workitemCategoryParticipationCount > 0
                ? new String[] { "/application/dashboard/openPage", "/workflow/myworkitems/openPage" }
                : new String[] { "/application/dashboard/openPage" };
        pageBean.setApplicationPaths(applicationPaths);
        setPageWidgetVisible("businessUnitLabel", isTenancyEnabled());
    }

    @Override
    protected void onIndexPage() throws UnifyException {
        super.onIndexPage();
        ApplicationPageBean pageBean = getPageBean();

        final boolean isWorkspaceEnabled = wkspPrivilegeManager != null;
        setPageWidgetVisible("wkspSelPanel", isWorkspaceEnabled);
        if (isWorkspaceEnabled) {
            List<String> workspaceCodes = wkspPrivilegeManager.findRoleWorkspaceCodes(getUserToken().getRoleCode());
            String workspaceCode = workspaceCodes.size() > 0 ? workspaceCodes.get(0) : null;
            getPageBean().setWorkspaceCode(workspaceCode);
            setSessionWorkspace();
        }

        String headerTitle = system().getSysParameterValue(String.class,
                ApplicationModuleSysParamConstants.APPLICATION_HEADER_TITLE);
        if (StringUtils.isBlank(headerTitle)) {
            headerTitle = getContainerSetting(String.class, "flowcentral.application.headertitle", null);
        }
        pageBean.setHeaderTitle(headerTitle);
        setPageWidgetVisible("topTitle", !StringUtils.isBlank(headerTitle));
    }

    private void setSessionWorkspace() throws UnifyException {
        setSessionAttribute(FlowCentralSessionAttributeConstants.WORKSPACE_CODE, getPageBean().getWorkspaceCode());
    }
}
