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
package com.flowcentraltech.flowcentral.studio.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AbstractEntitySchemaManager;
import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldSchema;
import com.flowcentraltech.flowcentral.application.data.EntitySchema;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppAppletProp;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPage;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityQuery;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppRefQuery;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppTableColumn;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.data.FormatterOptions;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.constants.FormType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.report.business.ReportModuleService;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinition;
import com.flowcentraltech.flowcentral.report.entities.ReportableDefinitionQuery;
import com.flowcentraltech.flowcentral.report.entities.ReportableField;
import com.flowcentraltech.flowcentral.report.util.ReportEntityUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

@Component("studio-entityschemamanger")
public class StudioEntitySchemaManagerImpl extends AbstractEntitySchemaManager {

    private static final String ENTITY_SCHEMA_OPERATION = "stu::entityschemaoperation";

    private static final int MAX_DEFAULT_TABLE_COLUMNS = 8;

    private final Set<String> skipTableColumn = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("id", "versionNo", "originWorkRecId", "originalCopyId",
                    "inWorkflow", "wfItemVersionType", "workBranchCode", "workDepartmentCode", "processingStatus")));

    private final Set<String> skipFormField = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("id",
            "versionNo", "createDt", "createdBy", "updateDt", "updatedBy", "originWorkRecId", "originalCopyId",
            "inWorkflow", "wfItemVersionType", "workBranchCode", "workDepartmentCode", "processingStatus")));

    @Configurable
    private AppletUtilities au;

    @Configurable
    private ReportModuleService reportModuleService;

    @Configurable
    private MessageResolver messageResolver;

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
            appEntity.setConfigType(entitySchema.isDynamic() ? ConfigType.CUSTOM : ConfigType.STATIC);
            appEntity.setBaseType(entitySchema.getBaseType());
            appEntity.setName(np.getEntityName());
            appEntity.setDescription(entitySchema.getDescription());
            appEntity.setLabel(entitySchema.getDescription());
            appEntity.setTableName(entitySchema.getTableName());
            final String entityClass = entitySchema.isDynamic()
                    ? ApplicationCodeGenUtils.generateCustomEntityClassName(ConfigType.CUSTOM, np.getApplicationName(),
                            np.getEntityName())
                    : entitySchema.getImplClass();
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
                    if (!entityFieldSchema.getDataType().isReportable()) {
                        update.add("reportable", false);
                    }

                    au.environment().updateAll(
                            new AppEntityFieldQuery().appEntityId(appEntityId).name(entityFieldSchema.getName()),
                            update);
                } else {
                    AppEntityField appEntityField = newAppEntityField(appEntityId, entityFieldSchema);
                    au.environment().create(appEntityField);
                }
            }

            // Update default components
            updateDefaultComponents(entitySchema.getEntity(), appEntity);

            // Update entity
            if (!StringUtils.isBlank(entitySchema.getTableName())) {
                appEntity.setTableName(entitySchema.getTableName());
            }

            if (!StringUtils.isBlank(entitySchema.getDataSourceAlias())) {
                appEntity.setDataSourceName(entitySchema.getDataSourceAlias());
            }

            final String entityClass = entitySchema.isDynamic()
                    ? ApplicationCodeGenUtils.generateCustomEntityClassName(ConfigType.CUSTOM, np.getApplicationName(),
                            np.getEntityName())
                    : entitySchema.getImplClass();
            appEntity.setEntityClass(entityClass);
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
                        messageResolver, appEntity.getBaseType(), FormatterOptions.DEFAULT);
                reportableFieldList.addAll(ReportEntityUtils.getReportableFieldList(messageResolver,
                        appEntity.getFieldList(), FormatterOptions.DEFAULT));
                reportableFieldList = ReportEntityUtils.removeDuplicates(reportableFieldList);
                reportableDefinition.setFieldList(reportableFieldList);
                reportModuleService.createReportableDefinition(reportableDefinition);

                final String privilegeCode = PrivilegeNameUtils.getReportablePrivilegeName(
                        ApplicationNameUtils.ensureLongNameReference(np.getApplicationName(), appEntity.getName()));
                if (!au.applicationPrivilegeManager().isRegisteredPrivilege(
                        ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE, privilegeCode)) {
                    au.applicationPrivilegeManager().registerPrivilege(ConfigType.CUSTOM, applicationId,
                            ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE, privilegeCode,
                            description);
                }

                UserToken userToken = getUserToken();
                if (userToken != null && !userToken.isReservedUser() && au.system().getSysParameterValue(boolean.class,
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
            final List<AppEntityField> appEntityFieldList = au.environment()
                    .findAll(new AppEntityFieldQuery().appEntityId(appEntity.getId()));
            if (reportableDefinition == null) {
                String description = resolveApplicationMessage("$m{report.managedreport.description}", nameDesc);
                reportableDefinition = new ReportableDefinition();
                reportableDefinition.setApplicationId(applicationId);
                reportableDefinition.setEntity(entity);
                reportableDefinition.setTitle(description);
                reportableDefinition.setName(appEntity.getName());
                reportableDefinition.setDescription(description);
                List<ReportableField> reportableFieldList = ReportEntityUtils.getEntityBaseTypeReportableFieldList(
                        messageResolver, appEntity.getBaseType(), FormatterOptions.DEFAULT);
                reportableFieldList.addAll(ReportEntityUtils.getReportableFieldList(messageResolver, appEntityFieldList,
                        FormatterOptions.DEFAULT));
                reportableFieldList = ReportEntityUtils.removeDuplicates(reportableFieldList);
                reportableDefinition.setFieldList(reportableFieldList);
                reportModuleService.createReportableDefinition(reportableDefinition);

                final String privilegeCode = PrivilegeNameUtils.getReportablePrivilegeName(
                        ApplicationNameUtils.ensureLongNameReference(np.getApplicationName(), appEntity.getName()));
                if (!au.applicationPrivilegeManager().isRegisteredPrivilege(
                        ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE, privilegeCode)) {
                    au.applicationPrivilegeManager().registerPrivilege(ConfigType.CUSTOM, applicationId,
                            ApplicationPrivilegeConstants.APPLICATION_REPORTABLE_CATEGORY_CODE, privilegeCode,
                            description);
                }

                UserToken userToken = getUserToken();
                if (userToken != null && !userToken.isReservedUser() && au.system().getSysParameterValue(boolean.class,
                        ApplicationModuleSysParamConstants.ASSIGN_APPLICATIONENTITY_ONCREATE)) {
                    au.applicationPrivilegeManager().assignPrivilegeToRole(userToken.getRoleCode(), privilegeCode);
                }
            } else {
                List<ReportableField> reportableFieldList = ReportEntityUtils.getReportableFieldList(messageResolver,
                        appEntityFieldList, FormatterOptions.DEFAULT);
                reportableFieldList = ReportEntityUtils.removeDuplicates(reportableFieldList);
                reportableDefinition.setFieldList(reportableFieldList);
                reportModuleService.updateReportableDefinition(reportableDefinition);
            }
        }
    }

    @Override
    public String createDefaultAppletComponents(String applicationName, AppApplet appApplet, boolean child)
            throws UnifyException {
        if (!appApplet.isIdBlank()) {
            return null;
        }

        final AppletType type = appApplet.getType();
        if (AppletType.DATA_IMPORT.equals(type)) {
            appApplet.setIcon("file-import");
            return null;
        }

        final ApplicationDef applicationDef = au.application().getApplicationDef(applicationName);
        final Long applicationId = applicationDef.getId();
        if (type.isCreate() || type.isEntityList()) {
            final String entity = appApplet.getEntity();
            EntityDef entityDef = au.application().getEntityDef(entity);

            String tableName = null;
            if (type.isEntityList()) {
                List<String> existingTables = au.application().findEntityAppTables(entity);
                if (!DataUtils.isBlank(existingTables)) {
                    // Choose the first existing table
                    tableName = existingTables.get(0);
                } else {
                    // Generate and create table
                    tableName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                            appApplet.getName() + "_Tbl");
                    String tableDesc = appApplet.getLabel() + " Table";
                    ApplicationEntityNameParts tnp = ApplicationNameUtils.getApplicationEntityNameParts(tableName);
                    AppTable appTable = new AppTable();
                    appTable.setApplicationId(applicationId);
                    appTable.setEntity(entity);
                    appTable.setName(tnp.getEntityName());
                    appTable.setDescription(tableDesc);
                    appTable.setLabel(tableDesc);
                    appTable.setSortHistory(-1);
                    appTable.setItemsPerPage(25);
                    appTable.setSortable(true);
                    appTable.setSerialNo(true);
                    appTable.setShowLabelHeader(false);
                    appTable.setHeaderToUpperCase(false);
                    appTable.setHeaderCenterAlign(false);
                    appTable.setBasicSearch(true);
                    appTable.setLimitSelectToColumns(true);
                    List<AppTableColumn> columnList = new ArrayList<AppTableColumn>();
                    for (EntityFieldDef entityFieldDef : entityDef.getFieldDefList()) {
                        if (entityFieldDef.isTableViewable()
                                && !skipTableColumn.contains(entityFieldDef.getFieldName())) {
                            if (child && entityFieldDef.isNonEnumForeignKey()) {
                                continue;
                            }

                            AppTableColumn appTableColumn = new AppTableColumn();
                            appTableColumn.setField(entityFieldDef.getFieldName());
                            appTableColumn.setLabel(null);
                            String widget = InputWidgetUtils.resolveEntityFieldWidget(entityFieldDef);
                            appTableColumn.setRenderWidget(widget);

                            if (columnList.isEmpty() || entityFieldDef.isMaintainLink()) {
                                appTableColumn.setLinkAct("maintainAct");
                            }

                            appTableColumn.setWidthRatio(1);
                            appTableColumn.setSortable(true);
                            columnList.add(appTableColumn);

                            if (columnList.size() >= MAX_DEFAULT_TABLE_COLUMNS) {
                                break;
                            }
                        }
                    }
                    appTable.setColumnList(columnList);
                    au.application().createAppTable(appTable);
                }
            }

            List<AppAppletProp> appletPropList = new ArrayList<AppAppletProp>();
            if (type.isAssignment()) {
                // Generate assignment page
                String assignmentPageName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                        appApplet.getName() + "_Asn");
                String assignmentPageDesc = appApplet.getLabel() + " Assignment";
                ApplicationEntityNameParts anp = ApplicationNameUtils.getApplicationEntityNameParts(assignmentPageName);
                AppAssignmentPage appAssignmentPage = new AppAssignmentPage();
                appAssignmentPage.setApplicationId(applicationId);
                appAssignmentPage.setName(anp.getEntityName());
                appAssignmentPage.setDescription(assignmentPageDesc);
                appAssignmentPage.setLabel(assignmentPageDesc);
                EntityFieldDef entityFieldDef = entityDef.getFieldDef(appApplet.getAssignField());
                RefDef refDef = au.application().getRefDef(entityFieldDef.getRefLongName());
                EntityDef refEntityDef = au.application().getEntityDef(refDef.getEntity());
                appAssignmentPage.setAssignCaption(resolveApplicationMessage("Assigned " + refEntityDef.getLabel()));
                appAssignmentPage.setAssignList("entityinlist");
                appAssignmentPage.setUnassignCaption(resolveApplicationMessage("Available " + refEntityDef.getLabel()));
                appAssignmentPage.setUnassignList("entitynotinlist");
                appAssignmentPage.setEntity(entity);
                appAssignmentPage.setBaseField(appApplet.getBaseField());
                appAssignmentPage.setAssignField(appApplet.getAssignField());
                appAssignmentPage.setRuleDescField(appApplet.getAssignDescField());
                appAssignmentPage.setConfigType(ConfigType.CUSTOM);
                au.application().createAppAssignmentPage(appAssignmentPage);

                // Add applet properties
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE, tableName));
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_EDIT, "true"));
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.ASSIGNMENT_PAGE, anp.getLongName()));
            } else {
                if (type.isSingleForm()) {
                    // TODO
                } else {
                    String formName = null;
                    List<String> existingForms = au.application().findEntityAppForms(entity);
                    if (!DataUtils.isBlank(existingForms)) {
                        // Choose first existing form
                        formName = existingForms.get(0);
                    } else {
                        // Generate and create form
                        formName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                                appApplet.getName() + "_Frm");
                        String formDesc = appApplet.getLabel() + " Form";
                        ApplicationEntityNameParts fnp = ApplicationNameUtils.getApplicationEntityNameParts(formName);
                        AppForm appForm = new AppForm();
                        appForm.setApplicationId(applicationId);
                        appForm.setType(FormType.INPUT);
                        appForm.setEntity(entity);
                        appForm.setName(fnp.getEntityName());
                        appForm.setDescription(formDesc);
                        List<AppFormElement> elementList = new ArrayList<AppFormElement>();
                        // Basic Tab
                        AppFormElement appFormElement = new AppFormElement();
                        appFormElement.setType(FormElementType.TAB); // Basic Tab
                        appFormElement.setElementName("basicDetails");
                        appFormElement.setTabContentType(TabContentType.MINIFORM);
                        appFormElement.setLabel("Basic Details");
                        appFormElement.setVisible(true);
                        appFormElement.setEditable(true);
                        appFormElement.setDisabled(false);
                        elementList.add(appFormElement);

                        // Basic Section
                        appFormElement = new AppFormElement();
                        appFormElement.setType(FormElementType.SECTION);
                        appFormElement.setElementName("basicDetails");
                        appFormElement.setSectionColumns(FormColumnsType.TYPE_2);
                        appFormElement.setLabel(null);
                        appFormElement.setVisible(true);
                        appFormElement.setEditable(true);
                        appFormElement.setDisabled(false);
                        elementList.add(appFormElement);

                        // Form
                        int column = 0;
                        List<EntityFieldDef> childList = null;
                        for (EntityFieldDef entityFieldDef : entityDef.getFieldDefList()) {
                            if (entityFieldDef.isFormViewable() && !entityFieldDef.isListOnly()
                                    && !skipFormField.contains(entityFieldDef.getFieldName())) {
                                if (child && entityFieldDef.isNonEnumForeignKey()) {
                                    continue;
                                }

                                appFormElement = new AppFormElement();
                                appFormElement.setType(FormElementType.FIELD);
                                appFormElement.setElementName(entityFieldDef.getFieldName());
                                appFormElement.setLabel(null);
                                if (!entityFieldDef.isWithInputWidget()) {
                                    String widget = InputWidgetUtils
                                            .getDefaultEntityFieldWidget(entityFieldDef.getDataType());
                                    if (widget != null) {
                                        appFormElement.setInputWidget(widget);
                                    } else {
                                        appFormElement.setInputWidget("application.text");
                                    }
                                }

                                appFormElement.setFieldColumn(column % 2);
                                appFormElement.setRequired(!entityFieldDef.isBoolean() && !entityFieldDef.isNullable());
                                appFormElement.setVisible(true);
                                appFormElement.setEditable(true);
                                appFormElement.setDisabled(false);
                                elementList.add(appFormElement);
                                column++;
                            } else if (entityFieldDef.isChild() || entityFieldDef.isChildList()) {
                                if (childList == null) {
                                    childList = new ArrayList<EntityFieldDef>();
                                }

                                childList.add(entityFieldDef);
                            }
                        }

                        // Child and child list tabs
                        if (childList != null) {
                            for (EntityFieldDef entityFieldDef : childList) {
                                appFormElement = new AppFormElement();
                                appFormElement.setType(FormElementType.TAB);
                                appFormElement.setElementName(entityFieldDef.getFieldName());
                                TabContentType childType = entityFieldDef.isChild() ? TabContentType.CHILD
                                        : TabContentType.CHILD_LIST;
                                appFormElement.setTabContentType(childType);
                                appFormElement.setLabel(entityFieldDef.getFieldLabel());
                                appFormElement.setVisible(true);
                                appFormElement.setEditable(true);
                                appFormElement.setDisabled(false);
                                appFormElement.setTabReference(entityFieldDef.getFieldName());
                                List<AppApplet> appletList = au.application().findManageEntityListApplets(
                                        au.application().getRefDef(entityFieldDef.getRefLongName()).getEntity());
                                if (!DataUtils.isBlank(appletList)) {
                                    AppApplet _childAppApplet = appletList.get(0);
                                    appFormElement.setTabApplet(ApplicationNameUtils.getApplicationEntityLongName(
                                            _childAppApplet.getApplicationName(), _childAppApplet.getName()));
                                    elementList.add(appFormElement);
                                }
                            }
                        }

                        // Change log Tab
                        if (entityDef.getBaseType().isAuditType()) {
                            appFormElement = new AppFormElement();
                            appFormElement.setType(FormElementType.TAB); // Change log
                            appFormElement.setElementName("changeLog");
                            appFormElement.setTabContentType(TabContentType.MINIFORM_CHANGELOG);
                            appFormElement.setLabel(resolveSessionMessage("$m{application.form.audit}"));
                            appFormElement.setVisible(true);
                            appFormElement.setEditable(true);
                            appFormElement.setDisabled(false);
                            elementList.add(appFormElement);
                            ApplicationEntityUtils.addChangeLogFormElements(elementList);
                        }

                        appForm.setElementList(elementList);
                        au.application().createAppForm(appForm);
                    }

                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.CREATE_FORM, formName));
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.MAINTAIN_FORM, formName));
                }

                // Add applet properties
                if (type.isEntityList()) {
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE, tableName));
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_NEW, "true"));
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_QUICKFILTER, "true"));
                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_BASICSEARCHONLY, "true"));
                    if (entityDef.isReportable()) {
                        appletPropList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_REPORT, "true"));
                    }

                    appletPropList.add(new AppAppletProp(AppletPropertyConstants.CREATE_FORM_SAVE_CLOSE, "true"));
                }

                appletPropList.add(new AppAppletProp(AppletPropertyConstants.CREATE_FORM_SAVE, "true"));
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.CREATE_FORM_SAVE_NEXT, "true"));

                appletPropList.add(new AppAppletProp(AppletPropertyConstants.MAINTAIN_FORM_UPDATE, "true"));
                appletPropList.add(new AppAppletProp(AppletPropertyConstants.MAINTAIN_FORM_DELETE, "true"));
            }

            appApplet.setPropList(appletPropList);

            return ApplicationNameUtils.getApplicationEntityLongName(applicationName, appApplet.getName());
        }

        return null;
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
        appEntityField.setAuditable(!ApplicationEntityUtils.isReservedFieldName(entityFieldSchema.getName())
                || ApplicationEntityUtils.isAuditableReservedFieldName(entityFieldSchema.getName()));
        appEntityField.setReportable(entityFieldSchema.getDataType().isReportable());
        appEntityField.setNullable(entityFieldSchema.isNullable());
        return appEntityField;
    }

}
