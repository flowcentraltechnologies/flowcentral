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
package com.flowcentraltech.flowcentral.studio.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AbstractEntitySchemaManager;
import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.data.EntityFieldSchema;
import com.flowcentraltech.flowcentral.application.data.EntitySchema;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityQuery;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppRefQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.report.business.ReportModuleService;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinition;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinitionQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.flowcentraltech.flowcentral.report.util.ReportEntityUtils;
import com.flowcentraltech.flowcentral.report.util.ReportEntityUtils.ReportFormatOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

@Component("studio-entityschemamanger")
public class StudioEntitySchemaManagerImpl extends AbstractEntitySchemaManager {

    private static final String ENTITY_SCHEMA_OPERATION = "app:entityschemaoperation";

    @Configurable
    private AppletUtilities au;

    @Configurable
    private ReportModuleService reportModuleService;

    @Configurable
    private MessageResolver messageResolver;

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    public final void setReportModuleService(ReportModuleService reportModuleService) {
        this.reportModuleService = reportModuleService;
    }

    public final void setMessageResolver(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    @Override
    @Synchronized(ENTITY_SCHEMA_OPERATION)
    public boolean createEntitySchema(EntitySchema entitySchema) throws UnifyException {
        logDebug("Creating entity schema. Entity [{0}]...", entitySchema.getEntity());
        ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(entitySchema.getEntity());
        AppEntity appEntity = au.environment().findLean(new AppEntityQuery().delegate(entitySchema.getDelegate())
                .applicationName(np.getApplicationName()).name(np.getEntityName()));
        if (appEntity == null) {
            // Create entity
            final Long applicationId = au.application().getApplicationId(np.getApplicationName());
            appEntity = new AppEntity();
            appEntity.setApplicationId(applicationId);
            appEntity.setConfigType(ConfigType.CUSTOM);
            appEntity.setBaseType(entitySchema.getBaseType());
            appEntity.setName(np.getEntityName());
            appEntity.setDescription(entitySchema.getDescription());
            appEntity.setLabel(entitySchema.getDescription());
            appEntity.setTableName(entitySchema.getTableName());
            final String entityClass = ApplicationCodeGenUtils.generateCustomEntityClassName(ConfigType.CUSTOM,
                    np.getApplicationName(), np.getEntityName());
            appEntity.setEntityClass(entityClass);
            appEntity.setDataSourceName(entitySchema.getDataSourceAlias());
            appEntity.setDelegate(entitySchema.getDelegate());
            appEntity.setActionPolicy(entitySchema.isActionPolicy());
            appEntity.setAuditable(true);
            appEntity.setReportable(true);

            List<AppEntityField> fieldList = new ArrayList<AppEntityField>();
            for (EntityFieldSchema entityFieldSchema : entitySchema.getFields()) {
                AppEntityField appEntityField = newAppEntityField(null, entityFieldSchema);
                fieldList.add(appEntityField);
            }
            appEntity.setFieldList(fieldList);

            au.environment().create(appEntity);

            // Create default components
            createDefaultComponents(entitySchema.getEntity(), appEntity);

            logDebug("Entity schema for [{0}] successfully created.", entitySchema.getEntity());
            return true;
        } else {
            logDebug("Entity schema for [{0}] already exists.", entitySchema.getEntity());
        }

        return false;
    }

    @Override
    @Synchronized(ENTITY_SCHEMA_OPERATION)
    public boolean updateEntitySchema(EntitySchema entitySchema) throws UnifyException {
        logDebug("Updating entity schema. Entity [{0}]...", entitySchema.getEntity());
        ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(entitySchema.getEntity());
        AppEntity appEntity = au.environment().findLean(new AppEntityQuery().delegate(entitySchema.getDelegate())
                .applicationName(np.getApplicationName()).name(np.getEntityName()));
        if (appEntity != null) {
            final Long appEntityId = appEntity.getId();

            // Update entity fields
            Set<String> existing = au.environment().valueSet(String.class, "name",
                    new AppEntityFieldQuery().appEntityId(appEntityId));
            for (EntityFieldSchema entityFieldSchema : entitySchema.getFields()) {
                if (existing.remove(entityFieldSchema.getName())) {
                    logDebug("Updating field [{0}]...", entityFieldSchema.getName());
                    Update update = new Update();
                    if (entityFieldSchema.getLength() > 0 && entityFieldSchema.getDataType().isString()) {
                        update.add("maxLen", entityFieldSchema.getLength());
                    }

                    if (entityFieldSchema.getPrecision() > 0) {
                        update.add("precision", entityFieldSchema.getPrecision());
                    }

                    if (entityFieldSchema.getScale() > 0) {
                        update.add("scale", entityFieldSchema.getScale());
                    }

                    if (!StringUtils.isBlank(entityFieldSchema.getColumn())) {
                        update.add("columnName", entityFieldSchema.getColumn());
                    }

                    update.add("nullable", entityFieldSchema.isNullable());
                    au.environment().updateAll(
                            new AppEntityFieldQuery().appEntityId(appEntityId).name(entityFieldSchema.getName()),
                            update);
                } else {
                    AppEntityField appEntityField = newAppEntityField(appEntityId, entityFieldSchema);
                    au.environment().create(appEntityField);
                }
            }

            // Update default components
            updateDefaultComponents(ENTITY_SCHEMA_OPERATION, appEntity);

            // Update entity
            if (!StringUtils.isBlank(entitySchema.getTableName())) {
                appEntity.setTableName(entitySchema.getTableName());
            }

            if (!StringUtils.isBlank(entitySchema.getDataSourceAlias())) {
                appEntity.setDataSourceName(entitySchema.getDataSourceAlias());
            }

            appEntity.setActionPolicy(entitySchema.isActionPolicy());

            au.environment().updateLeanByIdVersion(appEntity);
            logDebug("Entity schema for [{0}] successfully updated.", entitySchema.getEntity());
            return true;
        } else {
            logDebug("Entity schema for [{0}] does not exists.", entitySchema.getEntity());
        }

        return false;
    }

    @Override
    public void createDefaultComponents(String entity, AppEntity appEntity) throws UnifyException {
        final ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(entity);
        final Long applicationId = appEntity.getApplicationId();
        final String nameDesc = appEntity.getDescription();

        if (au.application().countAppRefs(
                (AppRefQuery) new AppRefQuery().applicationId(applicationId).name(appEntity.getName() + "Ref")) == 0) {
            // Create application reference
            AppRef appRef = new AppRef();
            appRef.setApplicationId(applicationId);
            appRef.setName(appEntity.getName() + "Ref");
            appRef.setDescription(resolveSessionMessage("$m{application.appref.reference.template}", nameDesc));
            appRef.setEntity(entity);
            au.application().createAppRef(appRef);
        }

        // Create reportable if necessary
        if (appEntity.isReportable()) {
            if (reportModuleService.countReportDefinitions((ReportableDefinitionQuery) new ReportableDefinitionQuery()
                    .applicationId(applicationId).name(appEntity.getName())) == 0) {
                String description = resolveApplicationMessage("$m{report.managedreport.description}", nameDesc);
                ReportableDefinition reportableDefinition = new ReportableDefinition();
                reportableDefinition.setApplicationId(applicationId);
                reportableDefinition.setEntity(entity);
                reportableDefinition.setTitle(description);
                reportableDefinition.setName(appEntity.getName());
                reportableDefinition.setDescription(description);
                List<ReportableField> reportableFieldList = ReportEntityUtils.getEntityBaseTypeReportableFieldList(
                        messageResolver, appEntity.getBaseType(), ReportFormatOptions.DEFAULT);
                reportableFieldList.addAll(ReportEntityUtils.getReportableFieldList(messageResolver,
                        appEntity.getFieldList(), ReportFormatOptions.DEFAULT));
                reportableDefinition.setFieldList(reportableFieldList);
                reportModuleService.createReportableDefinition(reportableDefinition);

                final String privilegeCode = PrivilegeNameUtils.getReportablePrivilegeName(
                        ApplicationNameUtils.ensureLongNameReference(np.getApplicationName(), appEntity.getName()));
                if (!au.applicationPrivilegeManager().isRegisteredPrivilege(
                        ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE, privilegeCode)) {
                    au.applicationPrivilegeManager().registerPrivilege(applicationId,
                            ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE, privilegeCode,
                            description);
                }

                UserToken userToken = getUserToken();
                if (!userToken.isReservedUser() && au.system().getSysParameterValue(boolean.class,
                        ApplicationModuleSysParamConstants.ASSIGN_APPLICATIONENTITY_ONCREATE)) {
                    au.applicationPrivilegeManager().assignPrivilegeToRole(userToken.getRoleCode(), privilegeCode);
                }
            }
        }
    }

    @Override
    public void updateDefaultComponents(String entity, AppEntity appEntity) throws UnifyException {
        final ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(entity);
        final Long applicationId = appEntity.getApplicationId();
        final String nameDesc = appEntity.getDescription();

        // Create reportable if necessary
        if (appEntity.isReportable()) {
            ReportableDefinition reportableDefinition = reportModuleService
                    .findReportDefinition((ReportableDefinitionQuery) new ReportableDefinitionQuery()
                            .applicationId(applicationId).name(appEntity.getName()));
            if (reportableDefinition == null) {
                String description = resolveApplicationMessage("$m{report.managedreport.description}", nameDesc);
                reportableDefinition = new ReportableDefinition();
                reportableDefinition.setApplicationId(applicationId);
                reportableDefinition.setEntity(entity);
                reportableDefinition.setTitle(description);
                reportableDefinition.setName(appEntity.getName());
                reportableDefinition.setDescription(description);
                List<ReportableField> reportableFieldList = ReportEntityUtils.getEntityBaseTypeReportableFieldList(
                        messageResolver, appEntity.getBaseType(), ReportFormatOptions.DEFAULT);
                reportableFieldList.addAll(ReportEntityUtils.getReportableFieldList(messageResolver,
                        appEntity.getFieldList(), ReportFormatOptions.DEFAULT));
                reportableDefinition.setFieldList(reportableFieldList);
                reportModuleService.createReportableDefinition(reportableDefinition);

                final String privilegeCode = PrivilegeNameUtils.getReportablePrivilegeName(
                        ApplicationNameUtils.ensureLongNameReference(np.getApplicationName(), appEntity.getName()));
                if (!au.applicationPrivilegeManager().isRegisteredPrivilege(
                        ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE, privilegeCode)) {
                    au.applicationPrivilegeManager().registerPrivilege(applicationId,
                            ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE, privilegeCode,
                            description);
                }

                UserToken userToken = getUserToken();
                if (!userToken.isReservedUser() && au.system().getSysParameterValue(boolean.class,
                        ApplicationModuleSysParamConstants.ASSIGN_APPLICATIONENTITY_ONCREATE)) {
                    au.applicationPrivilegeManager().assignPrivilegeToRole(userToken.getRoleCode(), privilegeCode);
                }
            } else {
                final List<AppEntityField> appEntityFieldList = au.environment()
                        .findAll(new AppEntityFieldQuery().appEntityId(appEntity.getId()));
                List<ReportableField> reportableFieldList = ReportEntityUtils.getEntityBaseTypeReportableFieldList(
                        messageResolver, appEntity.getBaseType(), ReportFormatOptions.DEFAULT);
                reportableFieldList.addAll(ReportEntityUtils.getReportableFieldList(messageResolver, appEntityFieldList,
                        ReportFormatOptions.DEFAULT));
                reportableDefinition.setFieldList(reportableFieldList);
                reportModuleService.updateReportableDefinition(reportableDefinition);
            }
        }
    }

    private AppEntityField newAppEntityField(Long appEntityId, EntityFieldSchema entityFieldSchema) {
        AppEntityField appEntityField = new AppEntityField();
        appEntityField.setAppEntityId(appEntityId);
        appEntityField.setConfigType(ConfigType.CUSTOM);
        appEntityField
                .setType(ApplicationEntityUtils.isReservedFieldName(entityFieldSchema.getName()) ? EntityFieldType.BASE
                        : EntityFieldType.CUSTOM);
        appEntityField.setDataType(entityFieldSchema.getDataType());
        appEntityField.setName(entityFieldSchema.getName());
        appEntityField.setColumnName(
                !StringUtils.isBlank(entityFieldSchema.getColumn()) ? entityFieldSchema.getColumn() : null);
        appEntityField.setReferences(
                entityFieldSchema.getReferences() != null ? entityFieldSchema.getReferences() + "Ref" : null);
        appEntityField
                .setLabel(!StringUtils.isBlank(entityFieldSchema.getDescription()) ? entityFieldSchema.getDescription()
                        : NameUtils.describeName(entityFieldSchema.getName()));
        appEntityField
                .setInputWidget(InputWidgetUtils.getDefaultSyncEntityFieldWidget(entityFieldSchema.getDataType()));
        appEntityField.setMaxLen(entityFieldSchema.getLength() > 0 && entityFieldSchema.getDataType().isString()
                ? entityFieldSchema.getLength()
                : null);
        appEntityField.setPrecision(entityFieldSchema.getPrecision() > 0 ? entityFieldSchema.getPrecision() : null);
        appEntityField.setScale(entityFieldSchema.getScale() > 0 ? entityFieldSchema.getScale() : null);
        appEntityField.setAuditable(true);
        appEntityField.setReportable(true);
        appEntityField.setNullable(entityFieldSchema.isNullable());
        return appEntityField;
    }

}
