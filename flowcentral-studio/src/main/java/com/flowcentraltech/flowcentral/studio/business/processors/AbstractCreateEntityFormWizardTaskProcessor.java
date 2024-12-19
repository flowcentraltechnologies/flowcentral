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
package com.flowcentraltech.flowcentral.studio.business.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.EntitySchemaManager;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.entities.AppAPI;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUpload;
import com.flowcentraltech.flowcentral.application.entities.AppFieldSequence;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityComposition;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityCompositionEntry;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractFormWizardTaskProcessor;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.APIType;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceEntryConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.batch.ConstraintAction;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for create entity form wizard policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractCreateEntityFormWizardTaskProcessor extends AbstractFormWizardTaskProcessor {

    @Configurable
    private AppletUtilities au;

    @Configurable
    private EntitySchemaManager entitySchemaManager;

    @Override
    public void process(TaskMonitor taskMonitor, ValueStore instValueStore) throws UnifyException {
        logDebug(taskMonitor, "Processing form wizard create JSON entity item...");
        final String refinedStructure = instValueStore.retrieve(String.class, "refinedStructure");
        final String delegate = instValueStore.retrieve(String.class, "delegate");
        EntityComposition entityComposition = DataUtils.fromJsonString(EntityComposition.class, refinedStructure);

        // Save source
        logDebug(taskMonitor, "Saving source...");
        au.environment().create((Entity) instValueStore.getValueObject());

        // Create entities
        logDebug(taskMonitor, "Creating one or more entities...");
        List<String> entityNames = new ArrayList<String>();
        final Long applicationId = instValueStore.retrieve(Long.class, "applicationId");
        final ApplicationDef applicationDef = au.application().getApplicationDef(applicationId);
        final String applicationName = applicationDef.getName();
        final EntityBaseType baseType = instValueStore.retrieve(EntityBaseType.class, "baseType");
        final String dateFormatter = instValueStore.retrieve(String.class, "dateFormatter");
        final String dateTimeFormatter = instValueStore.retrieve(String.class, "dateTimeFormatter");
        final boolean generateImport = instValueStore.retrieve(boolean.class, "generateImport");
        final List<String> importList = new ArrayList<String>();
        AppEntity appEntity = null;
        for (EntityCompositionEntry entry : entityComposition.getEntries()) {
            if (entry.getFieldType() == null) {
                if (appEntity != null) {
                    final String entity = createEntity(taskMonitor, applicationName, appEntity,
                            generateImport && entityNames.size() == 0 ? importList : Collections.emptyList());
                    entityNames.add(entity);
                }

                appEntity = new AppEntity();
                // Add base fields
                final EntityBaseType _baseType = entityNames.isEmpty() ? baseType : EntityBaseType.BASE_AUDIT_ENTITY;
                List<AppEntityField> baseFieldList = au.application().getEntityBaseTypeFieldList(_baseType,
                        ConfigType.CUSTOM);
                appEntity.setFieldList(new ArrayList<AppEntityField>(baseFieldList));

                // Create entity
                appEntity.setApplicationId(applicationId);
                appEntity.setConfigType(ConfigType.CUSTOM);
                appEntity.setBaseType(_baseType);
                appEntity.setName(entry.getEntityName());
                appEntity.setDescription(NameUtils.describeName(entry.getEntityName()));
                appEntity.setLabel(appEntity.getDescription());
                appEntity.setTableName(entry.getTable());
                appEntity.setDateFormatter(dateFormatter);
                appEntity.setDateTimeFormatter(dateTimeFormatter);
                final String entityClass = ApplicationCodeGenUtils.generateCustomEntityClassName(ConfigType.STATIC,
                        applicationName, entry.getEntityName());
                appEntity.setEntityClass(entityClass);
                appEntity.setDelegate(delegate);
                appEntity.setActionPolicy(false);
                appEntity.setAuditable(true);
                appEntity.setReportable(true);
                appEntity.setSchemaUpdateRequired(true);
            } else {
                AppEntityField appEntityField = newAppEntityField(applicationName, entry);
                appEntity.getFieldList().add(appEntityField);
                if (generateImport && entityNames.size() == 0) {
                    importList.add(appEntityField.getName());
                }
            }
        }

        final String entity = createEntity(taskMonitor, applicationName, appEntity,
                generateImport && entityNames.size() == 0 ? importList : Collections.emptyList());
        entityNames.add(entity);

        // Create CRUD applets
        final boolean generateApplets = instValueStore.retrieve(boolean.class, "generateApplet");
        if (generateApplets) {
            String rootAppletName = null;
            // Do reverse loop to create child applets first
            logDebug(taskMonitor, "Creating one or more applets...");
            for (int i = entityNames.size() - 1; i >= 0; i--) {
                final String _entity = entityNames.get(i);
                ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(_entity);
                final String appletName = "manage" + StringUtils.capitalizeFirstLetter(parts.getEntityName());
                if (i == 0) {
                    rootAppletName = appletName;
                }

                logDebug(taskMonitor, "Creating new entity list applet...");
                AppApplet appApplet = new AppApplet();
                appApplet.setApplicationId(applicationId);
                appApplet.setName(appletName);
                appApplet.setDescription(NameUtils.describeName(appletName));
                appApplet.setConfigType(ConfigType.CUSTOM);
                appApplet.setEntity(_entity);
                appApplet.setLabel(StringUtils.capitalizeFirstLetter(parts.getEntityName()));
                appApplet.setMenuAccess(i == 0);
                appApplet.setType(AppletType.MANAGE_ENTITYLIST);

                entitySchemaManager.createDefaultAppletComponents(applicationName, appApplet, i > 0);
                au.environment().create(appApplet);
                logDebug(taskMonitor, "Applet [{0}] successfully created.", appletName);
            }

            // Create REST API
            final boolean generateRest = instValueStore.retrieve(boolean.class, "generateRest");
            if (generateRest) {
                // Generate for root entity only
                logDebug(taskMonitor, "Creating REST API...");
                final String _entity = entityNames.get(0);
                ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(_entity);
                final String apiName = StringUtils.decapitalize(parts.getEntityName()) + "RestAPI";
                final String apiDesc =  NameUtils.describeName(parts.getEntityName()) + " Rest API";
                logDebug(taskMonitor, "Creating entity REST API...");
                AppAPI appAPI = new AppAPI();
                appAPI.setApplicationId(applicationId);
                appAPI.setType(APIType.REST_CRUD);
                appAPI.setName(apiName);
                appAPI.setDescription(apiDesc);
                appAPI.setConfigType(ConfigType.CUSTOM);
                appAPI.setEntity(_entity);
                appAPI.setApplet(ApplicationNameUtils.ensureLongNameReference(applicationName, rootAppletName));
                appAPI.setSupportCreate(Boolean.TRUE);
                appAPI.setSupportDelete(Boolean.TRUE);
                appAPI.setSupportRead(Boolean.TRUE);
                appAPI.setSupportUpdate(Boolean.TRUE);

                au.environment().create(appAPI);
                logDebug(taskMonitor, "REST API [{0}] successfully created.", apiName);
            }

            // Load Source
            final boolean loadSourceJSON = instValueStore.retrieve(boolean.class, "loadSourceJSON");
            if (loadSourceJSON) {
                logDebug(taskMonitor, "Loading source...");
                final String sourceJson = instValueStore.retrieve(String.class, "sourceJson");
                final String _entity = entityNames.get(0);
                loadSource(taskMonitor, sourceJson, _entity);
            }

        }
    }

    protected final AppletUtilities au() {
        return au;
    }

    protected abstract void loadSource(TaskMonitor taskMonitor, String source, String entity) throws UnifyException;

    private AppEntityField newAppEntityField(final String applicationName, final EntityCompositionEntry entry) {
        AppEntityField appEntityField = new AppEntityField();
        appEntityField.setConfigType(ConfigType.CUSTOM);
        appEntityField.setType(EntityFieldType.CUSTOM);

        EntityFieldDataType dataType = null;
        String references = !StringUtils.isBlank(entry.getReferences())
                ? ApplicationNameUtils.ensureLongNameReference(applicationName, entry.getReferences()) + "Ref"
                : null;
        if (entry.getFieldType().isTableColumn()) {
            DataType _dataType = entry.getDataType();
            dataType = EntityFieldDataType.fromName(_dataType.name());
            appEntityField.setAuditable(true);
            appEntityField.setReportable(true);
        } else if (entry.getFieldType().isForeignKey()) {
            dataType = EntityFieldDataType.REF;
        } else if (entry.getFieldType().isChild()) {
            dataType = EntityFieldDataType.CHILD;
        } else if (entry.getFieldType().isChildList()) {
            dataType = EntityFieldDataType.CHILD_LIST;
        }

        appEntityField.setDataType(dataType);
        appEntityField.setName(entry.getName());
        appEntityField.setColumnName(entry.getColumn());
        appEntityField.setReferences(references);
        appEntityField.setLabel(NameUtils.describeName(entry.getName()));
        appEntityField.setInputWidget(InputWidgetUtils.getDefaultSyncEntityFieldWidget(dataType));
        appEntityField.setJsonName(entry.getJsonName());
        appEntityField.setJsonFormatter(entry.getFormatter());
        if (dataType.isDecimal()) {
            appEntityField.setPrecision(20);
            appEntityField.setScale(2);
        }

        return appEntityField;
    }

    private String createEntity(TaskMonitor taskMonitor, String applicationName, AppEntity appEntity,
            List<String> importList) throws UnifyException {
        if (appEntity != null) {
            if (!importList.isEmpty()) {
                FieldSequenceConfig fieldSequenceConfig = new FieldSequenceConfig();
                List<FieldSequenceEntryConfig> entryList = new ArrayList<FieldSequenceEntryConfig>();
                for (String fieldName: importList) {
                    FieldSequenceEntryConfig fieldSequenceEntryConfig = new FieldSequenceEntryConfig();
                    fieldSequenceEntryConfig.setFieldName(fieldName);
                    fieldSequenceEntryConfig.setFormatter(null); // TODO
                    entryList.add(fieldSequenceEntryConfig);
                }
                
                fieldSequenceConfig.setEntryList(entryList);

                AppFieldSequence fieldSequence = new AppFieldSequence(
                        InputWidgetUtils.getFieldSequenceDefinition(fieldSequenceConfig));
                
                final String uploadName = StringUtils.decapitalize(appEntity.getName()) + "Upload";
                final String uploadDesc =  NameUtils.describeName(appEntity.getName()) + " Upload";
                AppEntityUpload appEntityUpload = new AppEntityUpload();
                appEntityUpload.setName(uploadName);
                appEntityUpload.setDescription(uploadDesc);
                appEntityUpload.setConstraintAction(ConstraintAction.SKIP);
                appEntityUpload.setFieldSequence(fieldSequence);
                appEntity.setUploadList(Arrays.asList(appEntityUpload));
            }

            // Save
            logDebug(taskMonitor, "Creating new entity...");
            au.environment().create(appEntity);

            final String entity = ApplicationNameUtils.ensureLongNameReference(applicationName, appEntity.getName());
            entitySchemaManager.createDefaultComponents(entity, appEntity);
            logDebug(taskMonitor, "Entity [{0}] successfully created.", entity);
            return entity;
        }

        return null;
    }
}
