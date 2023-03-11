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

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractAppletActionPolicy;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for studio entity action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractStudioAppletActionPolicy extends AbstractAppletActionPolicy {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private SystemModuleService systemModuleService;

    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    protected ApplicationModuleService application() {
        return applicationModuleService;
    }

    protected SystemModuleService system() {
        return systemModuleService;
    }

    protected void registerPrivilege(String applicationName, Long applicationId, StudioAppComponentType type,
            BaseApplicationEntity appEntity) throws UnifyException {
        List<String> privilegeCodeList = new ArrayList<String>();
        String privilegeCode = null;
        boolean assignToRole = false;
        switch (type) {
            case WIDGET:
                break;
            case APPLET:
                privilegeCode = PrivilegeNameUtils.getAppletPrivilegeName(
                        ApplicationNameUtils.getApplicationEntityLongName(applicationName, appEntity.getName()));
                registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                        privilegeCode, appEntity.getDescription());
                privilegeCodeList.add(privilegeCode);
                assignToRole = true;
                break;
            case CHART:
                break;
            case DASHBOARD:
                privilegeCode = PrivilegeNameUtils.getDashboardPrivilegeName(
                        ApplicationNameUtils.getApplicationEntityLongName(applicationName, appEntity.getName()));
                registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_DASHBOARD_CATEGORY_CODE,
                        privilegeCode, appEntity.getDescription());
                privilegeCodeList.add(privilegeCode);
                assignToRole = true;
                break;
            case ENTITY:
                final String entityLongName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                        appEntity.getName());

                privilegeCode = PrivilegeNameUtils.getAddPrivilegeName(entityLongName);
                registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        privilegeCode,
                        getApplicationMessage("application.entity.privilege.add", appEntity.getDescription()));
                privilegeCodeList.add(privilegeCode);

                privilegeCode = PrivilegeNameUtils.getEditPrivilegeName(entityLongName);
                registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        privilegeCode,
                        getApplicationMessage("application.entity.privilege.edit", appEntity.getDescription()));
                privilegeCodeList.add(privilegeCode);

                privilegeCode = PrivilegeNameUtils.getDeletePrivilegeName(entityLongName);
                registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        privilegeCode,
                        getApplicationMessage("application.entity.privilege.delete", appEntity.getDescription()));
                privilegeCodeList.add(privilegeCode);

                if (((AppEntity) appEntity).getBaseType().isWorkEntityType()) {
                    privilegeCode = PrivilegeNameUtils.getAttachPrivilegeName(entityLongName);
                    registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                            privilegeCode,
                            getApplicationMessage("application.entity.privilege.attach", appEntity.getDescription()));
                    privilegeCodeList.add(privilegeCode);
                }

                assignToRole = true;
                break;
            case FORM:
                break;
            case NOTIFICATION_TEMPLATE:
                break;
            case REFERENCE:
                break;
            case REPORT_CONFIGURATION:
                privilegeCode = PrivilegeNameUtils.getReportConfigPrivilegeName(
                        ApplicationNameUtils.ensureLongNameReference(applicationName, appEntity.getName()));
                registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_REPORTCONFIG_CATEGORY_CODE,
                        privilegeCode, appEntity.getDescription());
                break;
            case TABLE:
                break;
            case WORKFLOW:
                break;
            default:
                break;

        }

        if (assignToRole) {
            UserToken userToken = getUserToken();
            if (!userToken.isReservedUser() && systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ASSIGN_APPLICATIONENTITY_ONCREATE)) {
                for (String assgnPrivilegeCode : privilegeCodeList) {
                    assignPrivilegeToRole(userToken.getRoleCode(), assgnPrivilegeCode);
                }
            }
        }
    }

}
