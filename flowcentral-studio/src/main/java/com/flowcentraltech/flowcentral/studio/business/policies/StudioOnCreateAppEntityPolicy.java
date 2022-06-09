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

package com.flowcentraltech.flowcentral.studio.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.report.business.ReportModuleService;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinition;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.flowcentraltech.flowcentral.report.util.ReportEntityUtils;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio on create application entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiooncreateappentity-policy")
public class StudioOnCreateAppEntityPolicy extends StudioOnCreateComponentPolicy {

    @Configurable
    private ReportModuleService reportModuleService;

    @Configurable
    private MessageResolver messageResolver;

    public void setReportModuleService(ReportModuleService reportModuleService) {
        this.reportModuleService = reportModuleService;
    }

    public void setMessageResolver(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    @Override
    protected void doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        super.doExecutePreAction(ctx);
        AppEntity appEntity = (AppEntity) ctx.getInst();
        if (DataUtils.isBlank(appEntity.getFieldList())) {
            appEntity.setFieldList(getAms().getEntityBaseTypeFieldList(appEntity.getBaseType(), ConfigType.CUSTOM));
        } else {
            for (AppEntityField appEntityField : appEntity.getFieldList()) {
                if (appEntityField.getType().isStatic()) {
                    appEntityField.setType(EntityFieldType.CUSTOM);
                }
            }
        }
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = super.doExecutePostAction(ctx);
        final String applicationName = (String) getSessionAttribute(
                StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
        final AppEntity appEntity = (AppEntity) ctx.getInst();
        final Long applicationId = appEntity.getApplicationId(); //(Long) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        final String entity = ApplicationNameUtils.ensureLongNameReference(applicationName, appEntity.getName());
        final String nameDesc = appEntity.getDescription();

        // Create application reference
        AppRef appRef = new AppRef();
        appRef.setApplicationId(applicationId);
        appRef.setName(appEntity.getName() + "Ref");
        appRef.setDescription(resolveSessionMessage("$m{studio.appref.reference.template}", nameDesc));
        appRef.setEntity(entity);
        if (((EntityDef) ctx.getEntityDef()).isWithDescriptionField()) {
            appRef.setSearchField("description");
        }

        getAms().createAppRef(appRef);

        // Create reportable if necessary
        if (appEntity.getReportable()) {
            String description = resolveApplicationMessage("$m{report.managedreport.description}",
                    nameDesc);
            ReportableDefinition reportableDefinition = new ReportableDefinition();
            reportableDefinition.setApplicationId(applicationId);
            reportableDefinition.setEntity(entity);
            reportableDefinition.setTitle(description);
            reportableDefinition.setName(appEntity.getName());
            reportableDefinition.setDescription(description);
            List<ReportableField> baseReportableFieldList = ReportEntityUtils
                    .getEntityBaseTypeReportableFieldList(messageResolver, appEntity.getBaseType());
            reportableDefinition.setFieldList(baseReportableFieldList);
            reportModuleService.createReportableDefinition(reportableDefinition);

            final String privilegeCode = PrivilegeNameUtils.getReportablePrivilegeName(
                    ApplicationNameUtils.ensureLongNameReference(applicationName, appEntity.getName()));
            registerPrivilege(applicationId, ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE,
                    privilegeCode, description);

            UserToken userToken = getUserToken();
            if (!userToken.isReservedUser() && getSystemModuleService().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ASSIGN_APPLICATIONENTITY_ONCREATE)) {
                assignPrivilegeToRole(userToken.getRoleCode(), privilegeCode);
            }
        }

        return result;
    }

}
