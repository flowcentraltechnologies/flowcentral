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
package com.flowcentraltech.flowcentral.application.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.EntityInstNameParts;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseNamedEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusWorkEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseWorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application entity utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationEntityUtils {

    private static final Set<String> nonReportables = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("id", "versionNo", "applicationId", "originWorkRecId")));

    private static final Set<String> nullables = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList("originWorkRecId", "workBranchCode", "workDepartmentCode")));

    private static final Set<String> maintainLinks = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("name", "description")));

    private static final Map<EntityBaseType, Class<? extends BaseEntity>> entityClassMap;

    static {
        Map<EntityBaseType, Class<? extends BaseEntity>> map = new EnumMap<EntityBaseType, Class<? extends BaseEntity>>(
                EntityBaseType.class);
        map.put(EntityBaseType.BASE_APPLICATION_ENTITY, BaseApplicationEntity.class);
        map.put(EntityBaseType.BASE_AUDIT_ENTITY, BaseAuditEntity.class);
        map.put(EntityBaseType.BASE_ENTITY, BaseEntity.class);
        map.put(EntityBaseType.BASE_NAMED_ENTITY, BaseNamedEntity.class);
        map.put(EntityBaseType.BASE_STATUS_ENTITY, BaseStatusEntity.class);
        map.put(EntityBaseType.BASE_STATUS_WORK_ENTITY, BaseStatusWorkEntity.class);
        map.put(EntityBaseType.BASE_WORK_ENTITY, BaseWorkEntity.class);
        map.put(EntityBaseType.BASE_CONFIG_ENTITY, BaseConfigEntity.class);
        map.put(EntityBaseType.BASE_CONFIG_NAMED_ENTITY, BaseConfigNamedEntity.class);
        entityClassMap = Collections.unmodifiableMap(map);
    }

    private ApplicationEntityUtils() {

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
        if (BaseApplicationEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_APPLICATION_ENTITY;
        }

        if (BaseConfigNamedEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_CONFIG_NAMED_ENTITY;
        }

        if (BaseNamedEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_NAMED_ENTITY;
        }

        if (BaseStatusWorkEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_STATUS_WORK_ENTITY;
        }

        if (BaseConfigEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_CONFIG_ENTITY;
        }

        if (BaseWorkEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_WORK_ENTITY;
        }

        if (BaseStatusEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_STATUS_ENTITY;
        }

        if (BaseAuditEntity.class.isAssignableFrom(entityClass)) {
            return EntityBaseType.BASE_AUDIT_ENTITY;
        }

        return EntityBaseType.BASE_ENTITY;
    }

    private static Map<EntityBaseType, List<EntityBaseType>> typeInheritMap;
    static {
        Map<EntityBaseType, List<EntityBaseType>> map = new EnumMap<EntityBaseType, List<EntityBaseType>>(
                EntityBaseType.class);
        map.put(EntityBaseType.BASE_ENTITY, Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY)));
        map.put(EntityBaseType.BASE_AUDIT_ENTITY, Collections
                .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY)));
        map.put(EntityBaseType.BASE_STATUS_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY,
                        EntityBaseType.BASE_STATUS_ENTITY)));
        map.put(EntityBaseType.BASE_WORK_ENTITY, Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                EntityBaseType.BASE_AUDIT_ENTITY, EntityBaseType.BASE_WORK_ENTITY)));
        map.put(EntityBaseType.BASE_STATUS_WORK_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY,
                        EntityBaseType.BASE_WORK_ENTITY, EntityBaseType.BASE_STATUS_WORK_ENTITY)));
        map.put(EntityBaseType.BASE_NAMED_ENTITY, Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY,
                EntityBaseType.BASE_AUDIT_ENTITY, EntityBaseType.BASE_NAMED_ENTITY)));
        map.put(EntityBaseType.BASE_APPLICATION_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_NAMED_ENTITY,
                        EntityBaseType.BASE_CONFIG_NAMED_ENTITY, EntityBaseType.BASE_AUDIT_ENTITY,
                        EntityBaseType.BASE_APPLICATION_ENTITY)));
        map.put(EntityBaseType.BASE_CONFIG_ENTITY, Collections
                .unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_CONFIG_ENTITY)));
        map.put(EntityBaseType.BASE_CONFIG_NAMED_ENTITY,
                Collections.unmodifiableList(Arrays.asList(EntityBaseType.BASE_ENTITY, EntityBaseType.BASE_NAMED_ENTITY,
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
                        "application.entitylist", null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY, "applicationName",
                        msgResolver.resolveApplicationMessage("$m{baseapplicationentity.field.label.applicationname}"),
                        null, "applicationId", "name", null, null, null, null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY, "applicationDesc",
                        msgResolver.resolveApplicationMessage("$m{baseapplicationentity.field.label.applicationdesc}"),
                        null, "applicationId", "description", null, null, null, null, null, configType));
                break;
            case BASE_AUDIT_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.TIMESTAMP_UTC, "createDt",
                        msgResolver.resolveApplicationMessage("$m{baseauditentity.field.label.createdt}"), null, null,
                        null, null, null, "application.datetime", null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "createdBy",
                        msgResolver.resolveApplicationMessage("$m{baseauditentity.field.label.createdby}"), null, null,
                        null, null, null, "application.name", null, 64, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.TIMESTAMP_UTC, "updateDt",
                        msgResolver.resolveApplicationMessage("$m{baseauditentity.field.label.updatedt}"), null, null,
                        null, null, null, "application.datetime", null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "updatedBy",
                        msgResolver.resolveApplicationMessage("$m{baseauditentity.field.label.updatedby}"), null, null,
                        null, null, null, "application.name", null, 64, configType));
                break;
            case BASE_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LONG, "id",
                        msgResolver.resolveApplicationMessage("$m{baseentity.field.label.id}"), null, null, null, null,
                        null, null, null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LONG, "versionNo",
                        msgResolver.resolveApplicationMessage("$m{baseentity.field.label.versionno}"), null, null, null,
                        null, null, null, null, null, configType));
                break;
            case BASE_NAMED_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "name",
                        msgResolver.resolveApplicationMessage("$m{basesystementity.field.label.name}"), null, null,
                        null, null, null, "application.name", null, 64, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "description",
                        msgResolver.resolveApplicationMessage("$m{basesystementity.field.label.description}"), null,
                        null, null, null, null, "application.text", null, 128, configType));
                break;
            case BASE_STATUS_ENTITY:
            case BASE_STATUS_WORK_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.ENUM_REF, "status",
                        msgResolver.resolveApplicationMessage("$m{basestatusworkentity.field.label.status}"),
                        "statuslist", null, null, null, null, "application.enumlist", null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY, "statusDesc",
                        msgResolver.resolveApplicationMessage("$m{basestatusworkentity.field.label.statusdesc}"), null,
                        "status", "description", null, null, null, null, null, configType));
                break;
            case BASE_CONFIG_ENTITY:
            case BASE_CONFIG_NAMED_ENTITY:
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.ENUM_REF, "configType",
                        msgResolver.resolveApplicationMessage("$m{baseconfigentity.field.label.configtype}"),
                        "configtypelist", null, null, null, null, "application.enumlist", null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.LIST_ONLY, "configTypeDesc",
                        msgResolver.resolveApplicationMessage("$m{baseconfigentity.field.label.configtypedesc}"), null,
                        "configType", "description", null, null, null, null, null, configType));
                break;
            case BASE_WORK_ENTITY: 
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING, "workBranchCode",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.workbranchcode}"), null,
                        null, null, null, null, null, null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.STRING,
                        "workDepartmentCode",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.workdepartmentcode}"),
                        null, null, null, null, null, null, null, null, configType));
                list.add(ApplicationEntityUtils.createBaseAppEntityField(EntityFieldDataType.BOOLEAN, "inWorkflow",
                        msgResolver.resolveApplicationMessage("$m{baseworkentity.field.label.inworkflow}"),
                        null, null, null, null, null, "application.checkbox", null, null, configType));
                break;
            default:
                break;
        }
    }

    public static EntityBaseTypeFieldSet getEntityBaseTypeFieldSet(MessageResolver msgResolver) throws UnifyException {
        Map<EntityBaseType, List<AppEntityField>> baseEntityFieldSet = new EnumMap<EntityBaseType, List<AppEntityField>>(
                EntityBaseType.class);
        for (EntityBaseType type : EntityBaseType.values()) {
            baseEntityFieldSet.put(type,
                    ApplicationEntityUtils.getEntityBaseTypeFieldList(msgResolver, type, ConfigType.STATIC));
        }

        return new EntityBaseTypeFieldSet(baseEntityFieldSet);
    }

    public static class EntityBaseTypeFieldSet {

        Map<EntityBaseType, List<AppEntityField>> baseEntityFieldSet;

        public EntityBaseTypeFieldSet(Map<EntityBaseType, List<AppEntityField>> baseEntityFieldSet) {
            this.baseEntityFieldSet = baseEntityFieldSet;
        }

        public List<AppEntityField> getBaseFieldList(EntityBaseType type) {
            List<AppEntityField> resultList = baseEntityFieldSet.get(type);
            for (AppEntityField appEntityField : resultList) {
                appEntityField.setConfigType(ConfigType.STATIC_INSTALL);
            }

            return resultList;
        }
    }

    private static AppEntityField createBaseAppEntityField(EntityFieldDataType type, String name, String label,
            String references, String key, String property, String category, String inputLabel, String inputWidget,
            String inputListKey, Integer length, ConfigType configType) {
        boolean nullable = nullables.contains(name);
        boolean reportable = !nonReportables.contains(name);
        boolean maintainLink = maintainLinks.contains(name);
        boolean auditable = false;
        String suggestionType = null;
        AppEntityField field = new AppEntityField(type, name, label, references, key, property, category, inputLabel,
                inputWidget, suggestionType, inputListKey, length, nullable, auditable, reportable, maintainLink);
        if (type.isDate() || type.isTimestamp()) {
            field.setLingualWidget("application.lingualdatetypelist");
        }

        field.setType(EntityFieldType.BASE);
        field.setConfigType(configType);
        return field;
    }

}
