/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.UserRoleInfo;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractForwarderController;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractForwarderPageBean;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Configurable;
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
    private SystemModuleService systemModuleService;

    public AbstractApplicationForwarderController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    protected final String forwardToApplication(UserRoleInfo userRoleInfo) throws UnifyException {
        UserToken userToken = getUserToken();
        if (userRoleInfo != null) {
            userToken.setRoleCode(userRoleInfo.getRoleCode());
            userToken.setDepartmentCode(userRoleInfo.getDepartmentCode());
            setSessionAttribute(FlowCentralSessionAttributeConstants.ROLEDESCRIPTION, userRoleInfo.getRoleDesc());
        } else {
            setSessionAttribute(FlowCentralSessionAttributeConstants.ROLEDESCRIPTION, getUserToken().getUserName());
        }

        boolean inStudioWindow = LAUNCH_STUDIO_WINDOW
                .equals(getRequestAttribute(PageRequestParameterConstants.WINDOW_NAME)); // TODO Check if role has
                                                                                         // developer privilege
        String applicationPath = inStudioWindow
                ? systemModuleService.getSysParameterValue(String.class,
                        ApplicationModuleSysParamConstants.STUDIO_APPLICATION)
                : systemModuleService.getSysParameterValue(String.class,
                        ApplicationModuleSysParamConstants.DEFAULT_APPLICATION);
        return forwardToPath(applicationPath);
    }

    protected SystemModuleService system() {
        return systemModuleService;
    }

}
