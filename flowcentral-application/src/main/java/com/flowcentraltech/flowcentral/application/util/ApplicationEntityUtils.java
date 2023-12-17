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
package com.flowcentraltech.flowcentral.application.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntityInstNameParts;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseNamedEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusTenantEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusWorkEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusWorkTenantEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseTenantEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseVersionEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseVersionTenantEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseWorkEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseWorkTenantEntity;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Amongst;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application entity utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationEntityUtils {

    public static final Set<String> RESERVED_BASE_FIELDS = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList("id", "versionNo", "createDt", "createdBy", "updateDt", "updatedBy",
                    "originWorkRecId", "originalCopyId", "wfItemVersionType", "inWorkflow", "workBranchCode",
                    "workDepartmentCode", "processingStatus", "devVersionType", "devMergeVersionNo", "classified")));

    public static final List<EntityBaseType> BASE_WORK_TYPES = Collections
            .unmodifiableList(Arrays.asList(EntityBaseType.BASE_WORK_ENTITY, EntityBaseType.BASE_STATUS_WORK_ENTITY,
                    EntityBaseType.BASE_WORK_TENANT_ENTITY, EntityBaseType.BASE_STATUS_WORK_TENANT_ENTITY));

    public static final Restriction WORK_ENTITY_RESTRICTION = new Amongst("type",
            ApplicationEntityUtils.BASE_WORK_TYPES);

    private static final Map<String, String> baseFieldColumns;

    private static final Set<String> nonReportables = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList("id", "versionNo", "tenantId", "applicationId", "originWorkRecId")));

    private static final Set<String> nullables = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("originWorkRecId", "workBranchCode",
                    "workDepartmentCode", "processingStatus", "devMergeVersionNo")));

    private static final Set<String> maintainLinks = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("name", "description")));

    private static final Map<EntityBaseType, Class<? extends BaseEntity>> entityClassMap;

    static {
        Map<EntityBaseType, Class<? extends BaseEntity>> map = new EnumMap<EntityBaseType, Class<? extends BaseEntity>>(
                EntityBaseType.class);
        map.put(EntityBaseType.BASE_APPLICATION_ENTITY, BaseApplicationEntity.class);
        map.put(EntityBaseType.BASE_AUDIT_ENTITY, BaseAuditEntity.class);
        map.put(EntityBaseType.BASE_ENTITY, BaseEntity.class);
        map.put(EntityBaseType.BASE_VERSION_ENTITY, BaseVersionEntity.class);
        map.put(EntityBaseType.BASE_NAMED_ENTITY, BaseNamedEntity.class);
        map.put(EntityBaseType.BASE_STATUS_ENTITY, BaseStatusEntity.class);
        map.put(EntityBaseType.BASE_STATUS_WORK_ENTITY, BaseStatusWorkEntity.class);
        map.put(EntityBaseType.BASE_WORK_ENTITY, BaseWorkEntity.class);
        map.put(EntityBaseType.BASE_CONFIG_ENTITY, BaseConfigEntity.class);
        map.put(EntityBaseType.BASE_CONFIG_NAMED_ENTITY, BaseConfigNamedEntity.class);
        map.put(EntityBaseType.BASE_AUDIT_TENANT_ENTITY, BaseAuditTenantEntity.class);
        map.put(EntityBaseType.BASE_TENANT_ENTITY, BaseTenantEntity.class);
        map.put(EntityBaseType.BASE_VERSION_TENANT_ENTITY, BaseVersionTenantEntity.class);
        map.put(EntityBaseType.BASE_STATUS_TENANT_ENTITY, BaseStatusTenantEntity.class);
        map.put(EntityBaseType.BASE_STATUS_WORK_TENANT_ENTITY, BaseStatusWorkTenantEntity.class);
        map.put(EntityBaseType.BASE_WORK_TENANT_ENTITY, BaseWorkTenantEntity.class);
        entityClassMap = Collections.unmodifiableMap(map);

        Map<String, String> _map = new HashMap<String, String>();
        _map.put("versionNo", "VERSION_NO");
        _map.put("createdBy", "CREATED_BY");
        _map.put("updatedBy", "UPDATE_BY");
        _map.put("createDt", "CREATE_DT");
        _map.put("updateDt", "UPDATE_DT");
        _map.put("tenantId", "TENANT_ID");
        baseFieldColumns = Collections.unmodifiableMap(_map);
    }

    private ApplicationEntityUtils() {

    }

    public static boolean isReservedFieldName(String fieldName) {
        return RESERVED_BASE_FIELDS.contains(fieldName);
    }

    public static String getBaseFieldColumnName(String fieldName) {
        return baseFieldColumns.get(fieldName);
    }

    public static String getEntityInstName(String entityName, Long id) {
        return StringUtils.concatenateUsingSeparator(':', entityName, id);
    }

    public static EntityInstNameParts getEntityInstNameParts(String entityInstName) {
        String[] po = StringUtils.charSplit(entityInstName, ':');
        return new EntityInstNameParts(po[0], Long.valueOf(po[1]));
    }

    public static Class<? extends BaseEntity> getBaseEntityClass(EntityBaseType type) {
        return entityClassMap.get(type);
    }

    public static EntityBaseType getEntityBaseType(Class<? extends BaseEntity> entityClass) {
        // This order is important!
        if (BaseApplicationEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_APPLICATION_ENTITY;
        }

        if (BaseConfigNamedEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_CONFIG_NAMED_ENTITY;
        }

        if (BaseNamedEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_NAMED_ENTITY;
        }

        if (BaseStatusWorkTenantEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_STATUS_WORK_TENANT_ENTITY;
        }

        if (BaseStatusWorkEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_STATUS_WORK_ENTITY;
        }

        if (BaseConfigEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_CONFIG_ENTITY;
        }

        if (BaseWorkTenantEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_WORK_TENANT_ENTITY;
        }

        if (BaseWorkEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_WORK_ENTITY;
        }

        if (BaseStatusTenantEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_STATUS_TENANT_ENTITY;
        }

        if (BaseStatusEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_STATUS_ENTITY;
        }

        if (BaseAuditTenantEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_AUDIT_TENANT_ENTITY;
        }

        if (BaseAuditEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_AUDIT_ENTITY;
        }

        if (BaseVersionTenantEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_VERSION_TENANT_ENTITY;
        }

        if (BaseVersionEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_VERSION_ENTITY;
        }

        if (BaseTenantEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_TENANT_ENTITY;
        }

        return EntityBaseType.BASE_ENTITY;
    }

    private static Map<EntityBaseType, List<EntityBaseType>> typeInheritMap;
    static {
        Map<EntityBaseType, List<EntityBaseType>> map = new EnumMap<EntityBaseType, List<EntityBaseType>>(
                EntityBaseType.class);
        map.put(EntityBaseType.BASE_ENTITY, Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY)));
        map.put(EntityBaseType.BASE_TENANT_ENTITY, Collections
                .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_TENANT_ENTITY)));

        map.put(EntityBaseType.BASE_VERSION_ENTITY, Collections
                .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_VERSION_ENTITY)));
        map.put(EntityBaseType.BASE_VERSION_TENANT_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                        EntityBaseType.BASE_VERSION_ENTITY, EntityBaseType.BASE_VERSION_TENANT_ENTITY)));

        map.put(EntityBaseType.BASE_AUDIT_ENTITY, Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                EntityBaseType.BASE_VERSION_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY)));
        map.put(EntityBaseType.BASE_AUDIT_TENANT_ENTITY,
                Collections
                        .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_VERSION_ENTITY,
                                EntityBaseType.BASE_AUDIT_ENTITY, EntityBaseType.BASE_AUDIT_TENANT_ENTITY)));

        map.put(EntityBaseType.BASE_STATUS_ENTITY,
                Collections
                        .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_VERSION_ENTITY,
                                EntityBaseType.BASE_AUDIT_ENTITY, EntityBaseType.BASE_STATUS_ENTITY)));
        map.put(EntityBaseType.BASE_STATUS_TENANT_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                        EntityBaseType.BASE_VERSION_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY,
                        EntityBaseType.BASE_STATUS_ENTITY, EntityBaseType.BASE_STATUS_TENANT_ENTITY)));

        map.put(EntityBaseType.BASE_WORK_ENTITY,
                Collections
                        .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_VERSION_ENTITY,
                                EntityBaseType.BASE_AUDIT_ENTITY, EntityBaseType.BASE_WORK_ENTITY)));
        map.put(EntityBaseType.BASE_WORK_TENANT_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                        EntityBaseType.BASE_VERSION_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY,
                        EntityBaseType.BASE_WORK_ENTITY, EntityBaseType.BASE_WORK_TENANT_ENTITY)));

        map.put(EntityBaseType.BASE_STATUS_WORK_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                        EntityBaseType.BASE_VERSION_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY,
                        EntityBaseType.BASE_WORK_ENTITY, EntityBaseType.BASE_STATUS_WORK_ENTITY)));
        map.put(EntityBaseType.BASE_STATUS_WORK_TENANT_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                        EntityBaseType.BASE_VERSION_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY,
                        EntityBaseType.BASE_WORK_ENTITY, EntityBaseType.BASE_STATUS_WORK_ENTITY,
                        EntityBaseType.BASE_STATUS_WORK_TENANT_ENTITY)));

        map.put(EntityBaseType.BASE_NAMED_ENTITY,
                Collections
                        .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_VERSION_ENTITY,
                                EntityBaseType.BASE_AUDIT_ENTITY, EntityBaseType.BASE_NAMED_ENTITY)));
        map.put(EntityBaseType.BASE_APPLICATION_ENTITY,
                Collections
                        .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_VERSION_ENTITY,
                                EntityBaseType.BASE_NAMED_ENTITY, EntityBaseType.BASE_CONFIG_NAMED_ENTITY,
                                EntityBaseType.BASE_AUDIT_ENTITY, EntityBaseType.BASE_APPLICATION_ENTITY)));
        map.put(EntityBaseType.BASE_CONFIG_ENTITY,
                Collections
                        .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_VERSION_ENTITY,
                                EntityBaseType.BASE_AUDIT_ENTITY, EntityBaseType.BASE_CONFIG_ENTITY)));
        map.put(EntityBaseType.BASE_CONFIG_NAMED_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                        EntityBaseType.BASE_VERSION_ENTITY, EntityBaseType.BASE_NAMED_ENTITY,
                        EntityBaseType.BASE_CONFIG_NAMED_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY)));
        typeInheritMap = Collections.unmodifiableMap(map);
    }

    public static List<AppEntityField> getEntityBaseTypeFieldList(MessageResolver msgResolver, EntityBaseType type,
            ConfigType configType) throws UnifyException {
        List<AppEntityField> resultList = new ArrayList<AppEntityField>();
        for (EntityBaseType subType : typeInheritMap.get(type)) {
            ApplicationEntityUtils.populateEntityBaseTypeFieldList(resultList, msgResolver, subType, configType);
        }

        return resultList;
    }

    public static EntityFieldDef createEntityFieldDef(AppletUtilities au, String entityLongName,
            AppEntityField appEntityField) throws UnifyException {
        final WidgetTypeDef textWidgetTypeDef = au.getWidgetTypeDef("application.text");
        WidgetTypeDef inputWidgetTypeDef = null;
        if (!StringUtils.isBlank(appEntityField.getInputWidget())) {
            inputWidgetTypeDef = au.getWidgetTypeDef(appEntityField.getInputWidget());
        }

        WidgetTypeDef lingualWidgetTypeDef = null;
        EntityFieldDataType type = appEntityField.getDataType();
        if (!StringUtils.isBlank(appEntityField.getLingualWidget())) {
            lingualWidgetTypeDef = au.getWidgetTypeDef(appEntityField.getLingualWidget());
        } else {
            if (EntityFieldDataType.STRING.equals(type)) {
                lingualWidgetTypeDef = au.getWidgetTypeDef("application.lingualstringtypelist");
            } else if (type.isDate() || type.isTimestamp()) {
                lingualWidgetTypeDef = au.getWidgetTypeDef("application.lingualdatetypelist");
            }
        }

        String references = appEntityField.getReferences();
        RefDef refDef = null;
        if (type.isEntityRef()
                || (!appEntityField.getDataType().isEnumDataType() && !StringUtils.isBlank(references))) {
            refDef = au.getRefDef(references);
        }

        return new EntityFieldDef(textWidgetTypeDef, inputWidgetTypeDef, lingualWidgetTypeDef, refDef,
                appEntityField.getDataType(), appEntityField.getType(), appEntityField.getTextCase(), entityLongName,
                appEntityField.getName(), appEntityField.getMapped(), appEntityField.getLabel(),
                appEntityField.getColumnName(), references, appEntityField.getCategory(),
                appEntityField.getSuggestionType(), appEntityField.getInputLabel(), appEntityField.getInputListKey(),
                appEntityField.getLingualListKey(), appEntityField.getAutoFormat(), appEntityField.getDefaultVal(),
                appEntityField.getKey(), appEntityField.getProperty(),
                DataUtils.convert(int.class, appEntityField.getRows()),
                DataUtils.convert(int.class, appEntityField.getColumns()),
                DataUtils.convert(int.class, appEntityField.getMinLen()),
                DataUtils.convert(int.class, appEntityField.getMaxLen()),
                DataUtils.convert(int.class, appEntityField.getPrecision()),
                DataUtils.convert(int.class, appEntityField.getScale()), appEntityField.isAllowNegative(),
                !appEntityField.isReadOnly(), appEntityField.isNullable(), appEntityField.isAuditable(),
                appEntityField.isReportable(), appEntityField.isMaintainLink(), appEntityField.isBasicSearch(),
                appEntityField.isDescriptive());
    }

    public static void addChangeLogFormElements(List<AppFormElement> elementList) {
        // Section
        AppFormElement appFormElement = new AppFormElement();
        appFormElement.setType(FormElementType.SECTION);
        appFormElement.setElementName("changeLog");
        appFormElement.setSectionColumns(FormColumnsType.TYPE_2);
        appFormElement.setVisible(true);
        appFormElement.setDisabled(true);
        appFormElement.setConfigType(ConfigType.STATIC_INSTALL);
        elementList.add(appFormElement);

        // Change log fields
        appFormElement = new AppFormElement();
        appFormElement.setType(FormElementType.FIELD);
        appFormElement.setElementName("createdBy");
        appFormElement.setInputWidget("application.name");
        appFormElement.setFieldColumn(0);
        appFormElement.setVisible(true);
        appFormElement.setConfigType(ConfigType.STATIC_INSTALL);
        elementList.add(appFormElement);

        appFormElement = new AppFormElement();
        appFormElement.setType(FormElementType.FIELD);
        appFormElement.setElementName("updatedBy");
        appFormElement.setInputWidget("application.name");
        appFormElement.setFieldColumn(0);
        appFormElement.setVisible(true);
        appFormElement.setConfigType(ConfigType.STATIC_INSTALL);
        elementList.add(appFormElement);

        appFormElement = new AppFormElement();
        appFormElement.setType(FormElementType.FIELD);
        appFormElement.setElementName("createDt");
        appFormElement.setInputWidget("application.datetimetext");
        appFormElement.setFieldColumn(1);
        appFormElement.setVisible(true);
        appFormElement.setConfigType(ConfigType.STATIC_INSTALL);
        elementList.add(appFormElement);

        appFormElement = new AppFormElement();
        appFormElement.setType(FormElementType.FIELD);
        appFormElement.setElementName("updateDt");
        appFormElement.setInputWidget("application.datetimetext");
        appFormElement.setFieldColumn(1);
        appFormElement.setVisible(true);
        appFormElement.setConfigType(ConfigType.STATIC_INSTALL);
        elementList.add(appFormElement);
    }

    private static void populateEntityBaseTypeFieldList(List<AppEntityField> list, MessageResolver msgResolver,
            EntityBaseType type, ConfigType configType) throws UnifyException {
        switch (type) {
            case BASE_APPLICATION_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.REF, "applicationId",
                        msgResolver.resolveApplicationMessage("$m{baseapplicationentity.field.label.applicationid}"),
                        "application.applicationRef", null, null, null,
                        msgResolver.resolveApplicationMessage("$m{baseapplicationentity.field.label.application}"),
                        "application.entitylist", null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY,
                        "applicationName",
                        msgResolver.resolveApplicationMessage("$m{baseapplicationentity.field.label.applicationname}"),
                        null, "applicationId", "name", null, null, null, null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY,
                        "applicationDesc",
                        msgResolver.resolveApplicationMessage("$m{baseapplicationentity.field.label.applicationdesc}"),
                        null, "applicationId", "description", null, null, null, null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.ENUM, "devVersionType",
                        msgResolver.resolveApplicationMessage("$m{baseapplicationentity.field.label.devversiontype}"),
                        "developmentversiontypelist", null, null, null, null, "application.enumlist", null, null,
                        configType, false));
                list.add(
                        ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "devMergeVersionNo",
                                msgResolver.resolveApplicationMessage(
                                        "$m{baseapplicationentity.field.label.devmergeversionno}"),
                                null, null, null, null, null, "application.text", null, 32, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.BOOLEAN, "classified",
                        msgResolver.resolveApplicationMessage("$m{baseapplicationentity.field.label.classified}"), null,
                        null, null, null, null, "application.checkbox", null, null, configType, false));
                break;
            case BASE_AUDIT_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.TIMESTAMP_UTC, "createDt",
                        msgResolver.resolveApplicationMessage("$m{baseauditentity.field.label.createdt}"), null, null,
                        null, null, null, "application.datetime", null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "createdBy",
                        msgResolver.resolveApplicationMessage("$m{baseauditentity.field.label.createdby}"), null, null,
                        null, null, null, "application.name", null, 64, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.TIMESTAMP_UTC, "updateDt",
                        msgResolver.resolveApplicationMessage("$m{baseauditentity.field.label.updatedt}"), null, null,
                        null, null, null, "application.datetime", null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "updatedBy",
                        msgResolver.resolveApplicationMessage("$m{baseauditentity.field.label.updatedby}"), null, null,
                        null, null, null, "application.name", null, 64, configType, false));
                break;
            case BASE_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LONG, "id",
                        msgResolver.resolveApplicationMessage("$m{baseentity.field.label.id}"), null, null, null, null,
                        null, null, null, null, configType, false));
                break;
            case BASE_VERSION_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LONG, "versionNo",
                        msgResolver.resolveApplicationMessage("$m{baseentity.field.label.versionno}"), null, null, null,
                        null, null, null, null, null, configType, false));
                break;
            case BASE_NAMED_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "name",
                        msgResolver.resolveApplicationMessage("$m{basesystementity.field.label.name}"), null, null,
                        null, null, null, "application.name", null, 64, configType, true));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "description",
                        msgResolver.resolveApplicationMessage("$m{basesystementity.field.label.description}"), null,
                        null, null, null, null, "application.text", null, 128, configType, true));
                break;
            case BASE_STATUS_ENTITY:
            case BASE_STATUS_WORK_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.ENUM_REF, "status",
                        msgResolver.resolveApplicationMessage("$m{basestatusworkentity.field.label.status}"),
                        "statuslist", null, null, null, null, "application.enumlist", null, null, configType, true));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY, "statusDesc",
                        msgResolver.resolveApplicationMessage("$m{basestatusworkentity.field.label.statusdesc}"), null,
                        "status", "description", null, null, null, null, null, configType, false));
                break;
            case BASE_CONFIG_ENTITY:
            case BASE_CONFIG_NAMED_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.ENUM_REF, "configType",
                        msgResolver.resolveApplicationMessage("$m{baseconfigentity.field.label.configtype}"),
                        "configtypelist", null, null, null, null, "application.enumlist", null, null, configType, false));
                list.add(
                        ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY, "configTypeDesc",
                                msgResolver
                                        .resolveApplicationMessage("$m{baseconfigentity.field.label.configtypedesc}"),
                                null, "configType", "description", null, null, null, null, null, configType, false));
                break;
            case BASE_WORK_ENTITY:
                list.add(ApplicationEntityUtils
                        .createBaseAppEntityField(EntityFieldDataType.ENUM_REF, "processingStatus",
                                msgResolver.resolveApplicationMessage(
                                        "$m{baseworkentity.field.label.processingstatus}"),
                                "processingstatuslist", null, null, null, null, "application.enumlist", null, null,
                                configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY,
                        "processingStatusDesc",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.processingstatusdesc}"),
                        null, "processingStatus", "description", null, null, null, null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "workBranchCode",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.workbranchcode}"), null,
                        null, null, null, null, null, null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING,
                        "workDepartmentCode",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.workdepartmentcode}"),
                        null, null, null, null, null, null, null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.BOOLEAN, "inWorkflow",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.inworkflow}"), null, null,
                        null, null, null, "application.checkbox", null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LONG, "originalCopyId",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.originalcopyid}"), null,
                        null, null, null, null, "application.integer", null, null, configType, false));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.ENUM, "wfItemVersionType",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.wfversiontype}"),
                        "wfitemversiontypelist", null, null, null, null, "application.enumlist", null, null,
                        configType, true));
                break;
            default:
                break;
        }

        if (type.isTenantType()) {
            list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.TENANT_ID, "tenantId",
                    msgResolver.resolveApplicationMessage("$m{baseentity.field.label.tenantid}"), null, null, null,
                    null, null, "application.integer", null, null, configType, false));
        }
    }

    private static AppEntityField createBaseAppEntityField(EntityFieldDataType type, String name, String label,
            String references, String key, String property, String category, String inputLabel, String inputWidget,
            String inputListKey, Integer length, ConfigType configType,  boolean auditable) {
        boolean nullable = nullables.contains(name);
        boolean reportable = !nonReportables.contains(name);
        boolean maintainLink = maintainLinks.contains(name);
        boolean allowNegative = false;
        boolean readOnly = false;
        String suggestionType = null;
        AppEntityField field = new AppEntityField(type, name, label, references, key, property, category, inputLabel,
                inputWidget, suggestionType, inputListKey, length, allowNegative, readOnly, nullable, auditable,
                reportable, maintainLink);
        if (type.isDate() || type.isTimestamp()) {
            field.setLingualWidget("application.lingualdatetypelist");
        }

        field.setType(EntityFieldType.BASE);
        field.setConfigType(configType);
        return field;
    }

}
