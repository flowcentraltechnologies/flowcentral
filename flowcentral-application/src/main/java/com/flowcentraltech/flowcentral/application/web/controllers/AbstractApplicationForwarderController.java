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

package com.flowcentraltech.flowcentral.application.web.controllers;

import java.util.Collections;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.AppletPageAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletSessionAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModulePathConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.SessionOpenTabInfo;
import com.flowcentraltech.flowcentral.common.business.SecuredLinkManager;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.SecuredLinkType;
import com.flowcentraltech.flowcentral.common.data.SecuredLinkContentInfo;
import com.flowcentraltech.flowcentral.common.data.UserRoleInfo;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractForwarderController;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractForwarderPageBean;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.constant.PageRequestParameterConstants;

/**
 * Convenient abstract base class for application forwarder controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractApplicationForwarderController<T extends AbstractForwarderPageBean>
        extends AbstractForwarderController<T> {

    private static final String LAUNCH_STUDIO_WINDOW = "launchStudio";

    @Configurable
    private AppletUtilities appletUtilities;

    public AbstractApplicationForwarderController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }
 
    protected final String forwardToApplication(UserRoleInfo userRoleInfo) throws UnifyException {
        UserToken userToken = getUserToken();
        if (userRoleInfo != null) {
            userToken.setRoleCode(userRoleInfo.getRoleCode());
            userToken.setDepartmentCode(userRoleInfo.getDepartmentCode());
            setSessionAttribute(FlowCentralSessionAttributeConstants.DEPARTMENTCODE, userRoleInfo.getDepartmentCode());
            setSessionAttribute(FlowCentralSessionAttributeConstants.ROLECODE, userRoleInfo.getRoleCode());
            setSessionAttribute(FlowCentralSessionAttributeConstants.ROLEDESCRIPTION, userRoleInfo.getRoleDesc());
            setSessionAttribute(FlowCentralSessionAttributeConstants.BRANCH_SCOPING,
                    userRoleInfo.getBranchScopingList());
        } else {
            setSessionAttribute(FlowCentralSessionAttributeConstants.ROLEDESCRIPTION, getUserToken().getUserName());
            setSessionAttribute(FlowCentralSessionAttributeConstants.BRANCH_SCOPING, Collections.emptyList());
        }

        boolean inStudioWindow = LAUNCH_STUDIO_WINDOW
                .equals(getRequestAttribute(PageRequestParameterConstants.WINDOW_NAME)); // TODO Check if role has
                                                                                         // developer privilege
        final SecuredLinkContentInfo securedLinkContentInfo = (SecuredLinkContentInfo) getRequestAttribute(
                FlowCentralSessionAttributeConstants.SECURED_LINK_ACCESS);

        if (securedLinkContentInfo != null) {
            setSessionAttribute(AppletSessionAttributeConstants.OPEN_TAB_INFO,
                    new SessionOpenTabInfo(securedLinkContentInfo.getTitle(), securedLinkContentInfo.getDocUrl(),
                            securedLinkContentInfo.getContentPath()));
        }

        final String applicationPath = inStudioWindow
                ? appletUtilities.system().getSysParameterValue(String.class,
                        ApplicationModuleSysParamConstants.STUDIO_APPLICATION)
                : (securedLinkContentInfo != null && securedLinkContentInfo.isRealContentPath()
                        ? ApplicationModulePathConstants.APPLICATION_BROWSER_WINDOW
                        : appletUtilities.system().getSysParameterValue(String.class,
                                ApplicationModuleSysParamConstants.DEFAULT_APPLICATION));
        return forwardToPath(applicationPath);
    }

    protected SystemModuleService system() {
        return appletUtilities.system();
    }

    protected ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final void setReloadOnSwitch() throws UnifyException {
        appletUtilities.setReloadOnSwitch();
    }

    protected final boolean clearReloadOnSwitch() throws UnifyException {
        return appletUtilities.clearReloadOnSwitch();
    }

    protected final boolean isReloadOnSwitch() throws UnifyException {
        return appletUtilities.isReloadOnSwitch();
    }

    protected final void captureSecuredLink(SecuredLinkType type) throws UnifyException {
        final String forward = getSessionContext().getExternalForward();
        if (!StringUtils.isBlank(forward)) {
            SecuredLinkManager slm = appletUtilities.getComponent(SecuredLinkManager.class);
            SecuredLinkContentInfo securedLinkContentInfo = slm.getSecuredLink(forward);
            if (type.equals(securedLinkContentInfo.getType())) {
                getSessionContext().removeExternalForward();
                getPage().setAttribute(AppletPageAttributeConstants.SECURED_LINK_ACCESSKEY, forward);
            }
        }
    }

    protected final void invalidateSecuredLink(SecuredLinkType type) throws UnifyException {
        final String accessKey = getPage().getAttribute(String.class,
                AppletPageAttributeConstants.SECURED_LINK_ACCESSKEY);
        if (!StringUtils.isBlank(accessKey)) {
            SecuredLinkManager slm = appletUtilities.getComponent(SecuredLinkManager.class);
            slm.invalidateSecuredLinkByAccessKey(type, accessKey);
            getPage().removeAttribute(String.class, AppletPageAttributeConstants.SECURED_LINK_ACCESSKEY);
        }
    }

}
