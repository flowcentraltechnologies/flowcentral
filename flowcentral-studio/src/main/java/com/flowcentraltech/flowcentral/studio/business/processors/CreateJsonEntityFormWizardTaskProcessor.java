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
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.EntitySchemaManager;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityComposition;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityCompositionEntry;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractFormWizardTaskProcessor;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Create JSON entity form wizard policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({ "studio.studioJsonEntity" })
@Component("createjsonentity-formwizardprocessor")
public class CreateJsonEntityFormWizardTaskProcessor extends AbstractFormWizardTaskProcessor {

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
        AppEntity appEntity = null;
        for (EntityCompositionEntry entry : entityComposition.getEntries()) {
            if (entry.getFieldType() == null) {
                if (appEntity != null) {
                    final String entity = createEntity(taskMonitor, applicationName, appEntity);
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
                final String entityClass = ApplicationCodeGenUtils.generateCustomEntityClassName(ConfigType.CUSTOM,
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
            }
        }

        final String entity = createEntity(taskMonitor, applicationName, appEntity);
        entityNames.add(entity);

        // Create CRUD applets
        final boolean generateApplets = instValueStore.retrieve(boolean.class, "generateApplet");
        if (generateApplets) {
            // Do reverse loop to create child applets first
            logDebug(taskMonitor, "Creating one or more applets...");
            for (int i = entityNames.size() - 1; i >= 0; i--) {
                final String _entity = entityNames.get(i);
                ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(_entity);
                final String appletName = "manage" + StringUtils.capitalizeFirstLetter(parts.getEntityName());
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

                entitySchemaManager.createDefaultAppletComponents(applicationName, appApplet);
                au.environment().create(appApplet);
                logDebug(taskMonitor, "Applet [{0}] successfully created.", appletName);
            }
        }
    }

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
        return appEntityField;
    }

    private String createEntity(TaskMonitor taskMonitor, String applicationName, AppEntity appEntity) throws UnifyException {
        if (appEntity != null) {
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
