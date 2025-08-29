/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.connect.common;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.PropertyUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowcentraltech.flowcentral.connect.common.constants.DataSourceOperation;
import com.flowcentraltech.flowcentral.connect.common.constants.LingualDateType;
import com.flowcentraltech.flowcentral.connect.common.constants.LingualStringType;
import com.flowcentraltech.flowcentral.connect.common.constants.RestrictionType;
import com.flowcentraltech.flowcentral.connect.common.data.BaseRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceParam;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DateRange;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.OrderDef;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.connect.common.data.QueryDef;
import com.flowcentraltech.flowcentral.connect.common.data.QueryLoadingParams;
import com.flowcentraltech.flowcentral.connect.common.data.ResolvedCondition;
import com.flowcentraltech.flowcentral.connect.common.data.UpdateDef;
import com.flowcentraltech.flowcentral.connect.configuration.xml.ApplicationConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.EntitiesConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.EntityConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.EntityFieldConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.util.XmlUtils;
import com.tcdng.unify.common.constants.ConnectEntityBaseType;
import com.tcdng.unify.common.constants.ConnectFieldDataType;
import com.tcdng.unify.common.data.EntityFieldInfo;
import com.tcdng.unify.common.data.EntityInfo;
import com.tcdng.unify.convert.util.ConverterUtils;

/**
 * Flow central interconnect.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractInterconnect {

    private static final Logger LOGGER = Logger.getLogger(AbstractInterconnect.class.getName());

    private static final Map<Class<?>, ConnectFieldDataType> classToConnectDataTypeMap;

    static {
        Map<Class<?>, ConnectFieldDataType> map = new HashMap<Class<?>, ConnectFieldDataType>();
        map.put(boolean.class, ConnectFieldDataType.BOOLEAN);
        map.put(Boolean.class, ConnectFieldDataType.BOOLEAN);
        map.put(byte.class, ConnectFieldDataType.INTEGER);
        map.put(Byte.class, ConnectFieldDataType.INTEGER);
        map.put(byte[].class, ConnectFieldDataType.BLOB);
        map.put(char.class, ConnectFieldDataType.CHAR);
        map.put(Character.class, ConnectFieldDataType.CHAR);
        map.put(short.class, ConnectFieldDataType.SHORT);
        map.put(Short.class, ConnectFieldDataType.SHORT);
        map.put(int.class, ConnectFieldDataType.INTEGER);
        map.put(Integer.class, ConnectFieldDataType.INTEGER);
        map.put(long.class, ConnectFieldDataType.LONG);
        map.put(Long.class, ConnectFieldDataType.LONG);
        map.put(float.class, ConnectFieldDataType.FLOAT);
        map.put(Float.class, ConnectFieldDataType.FLOAT);
        map.put(double.class, ConnectFieldDataType.DOUBLE);
        map.put(Double.class, ConnectFieldDataType.DOUBLE);
        map.put(BigDecimal.class, ConnectFieldDataType.DECIMAL);
        map.put(Date.class, ConnectFieldDataType.DATE);
        map.put(boolean[].class, ConnectFieldDataType.BOOLEAN_ARRAY);
        map.put(Boolean[].class, ConnectFieldDataType.BOOLEAN_ARRAY);
        map.put(short[].class, ConnectFieldDataType.SHORT_ARRAY);
        map.put(Short[].class, ConnectFieldDataType.SHORT_ARRAY);
        map.put(int[].class, ConnectFieldDataType.INTEGER_ARRAY);
        map.put(Integer[].class, ConnectFieldDataType.INTEGER_ARRAY);
        map.put(long[].class, ConnectFieldDataType.LONG_ARRAY);
        map.put(Long[].class, ConnectFieldDataType.LONG_ARRAY);
        map.put(float[].class, ConnectFieldDataType.FLOAT_ARRAY);
        map.put(Float[].class, ConnectFieldDataType.FLOAT_ARRAY);
        map.put(double[].class, ConnectFieldDataType.DOUBLE_ARRAY);
        map.put(Double[].class, ConnectFieldDataType.DOUBLE_ARRAY);
        map.put(BigDecimal[].class, ConnectFieldDataType.DECIMAL_ARRAY);
        map.put(Date[].class, ConnectFieldDataType.DATE_ARRAY);
        map.put(org.joda.time.LocalDate.class, ConnectFieldDataType.DATE);
        map.put(org.joda.time.LocalDateTime.class, ConnectFieldDataType.DATE);
        map.put(java.time.LocalDate.class, ConnectFieldDataType.DATE);
        map.put(java.time.LocalDateTime.class, ConnectFieldDataType.DATE);
        map.put(String.class, ConnectFieldDataType.STRING);
        map.put(String[].class, ConnectFieldDataType.STRING_ARRAY);
        map.put(List.class, ConnectFieldDataType.CHILD_LIST);
        map.put(Set.class, ConnectFieldDataType.CHILD_LIST);
        classToConnectDataTypeMap = Collections.unmodifiableMap(map);
    }

    protected enum RefType {
        PRIMITIVE,
        OBJECT;

        public boolean primitive() {
            return this.equals(PRIMITIVE);
        }

        public boolean object() {
            return this.equals(OBJECT);
        }
    }

    private String redirect;

    private Map<String, EntityInfo> entities;

    private Map<Class<?>, EntityInfo> entitiesByImplClass;

    private final RefType refType;

    private EntityInstFinder entityInstFinder;

    private boolean initialized;

    public AbstractInterconnect(RefType refType) {
        this.refType = refType;
        this.entities = Collections.emptyMap();
    }

    public String prettyJSON(Object src) throws Exception {
        if (src != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(src);
        }

        return null;
    }

    public boolean init(String configurationFile, EntityInstFinder entityInstFinder) throws Exception {
        if (!initialized) {
            synchronized (AbstractInterconnect.class) {
                if (!initialized) {
                    LOGGER.log(Level.INFO, "Initializing flowCentral interconnect using configuration [{0}]...",
                            configurationFile);
                    Map<String, EntityInfo> _entitiesbyclassname = new HashMap<String, EntityInfo>();
                    List<ApplicationConfig> applicationConfigList = XmlUtils.readInterconnectConfig(configurationFile);
                    for (ApplicationConfig applicationConfig : applicationConfigList) {
                        final String appEntityManagerFactory = applicationConfig.getEntityManagerFactory();
                        redirect = redirect == null ? redirect = applicationConfig.getRedirect() : redirect;
                        EntitiesConfig entitiesConfig = applicationConfig.getEntitiesConfig();
                        if (entitiesConfig != null) {
                            List<EntityConfig> entityConfigList = entitiesConfig.getEntityList();
                            if (entityConfigList != null && !entityConfigList.isEmpty()) {
                                final String applicationName = applicationConfig.getName();
                                LOGGER.log(Level.INFO, "Loading interconnect entity information for [{0}]...",
                                        applicationName);

                                for (EntityConfig entityConfig : entityConfigList) {
                                    if (entityConfig.getBase() == null) {
                                        throw new IllegalArgumentException(
                                                "Entity configuration for [" + applicationName + "."
                                                        + entityConfig.getName() + "] requires a base type.");
                                    }

                                    final ConnectEntityBaseType base = entityConfig.getBase();
                                    EntityInfo.Builder eib = EntityInfo.newBuilder(appEntityManagerFactory);
                                    eib.dataSourceAlias(applicationConfig.getDataSourceAlias()).baseType(base)
                                            .name(ensureLongName(applicationName, entityConfig.getName()))
                                            .tableName(entityConfig.getTable())
                                            .description(entityConfig.getDescription())
                                            .implementation(entityConfig.getImplementation())
                                            .idFieldName(entityConfig.getIdFieldName())
                                            .versionNoFieldName(entityConfig.getVersionNoFieldName())
                                            .handler(entityConfig.getHandler())
                                            .actionPolicy(entityConfig.getActionPolicy())
                                            .ignoreOnSync(entityConfig.isIgnoreOnSync());
                                    populateBaseFields(eib, base);
                                    if (entityConfig.getEntityFieldList() != null) {
                                        for (EntityFieldConfig entityFieldConfig : entityConfig.getEntityFieldList()) {
                                            eib.addField(entityFieldConfig.getType(),
                                                    entityFieldConfig.getType().javaClass(),
                                                    entityFieldConfig.getName(), entityFieldConfig.getDescription(),
                                                    entityFieldConfig.getColumn(),
                                                    ensureLongName(applicationName, entityFieldConfig.getReferences()),
                                                    entityFieldConfig.getEnumImplClass(),
                                                    entityFieldConfig.getPrecision(), entityFieldConfig.getScale(),
                                                    entityFieldConfig.getLength(), entityFieldConfig.isNullable());
                                        }
                                    }

                                    EntityInfo entityInfo = eib.build();
                                    _entitiesbyclassname.put(entityConfig.getImplementation(), entityInfo);
                                }

                                LOGGER.log(Level.INFO,
                                        "Loaded [{0}] entites interconnect information for application [{1}].",
                                        new Object[] { entityConfigList.size(), applicationName });
                            }
                        }
                    }

                    entities = detectAndApplyImplicitFields(_entitiesbyclassname);
                    entitiesByImplClass = new HashMap<Class<?>, EntityInfo>();
                    for (Map.Entry<String, EntityInfo> entry: entities.entrySet()) {
                        entitiesByImplClass.put(entry.getValue().getImplClass(), entry.getValue());
                    }
                    
                    this.entityInstFinder = entityInstFinder;
                    initialized = true;
                    LOGGER.log(Level.INFO, "Total of [{0}] entity information loaded.", entities.size());
                    LOGGER.log(Level.INFO, "Interconnect successfully initialized.");
                }
            }

            return true;
        }

        return false;
    }

    private Map<String, EntityInfo> detectAndApplyImplicitFields(Map<String, EntityInfo> _entitiesbyclassname)
            throws Exception {
        Map<String, EntityInfo> entities = new HashMap<String, EntityInfo>();
        for (EntityInfo _entityInfo : _entitiesbyclassname.values()) {
            EntityInfo.Builder eib = EntityInfo.newBuilder(_entityInfo.getEntityManagerFactory());
            final String tableName = _entityInfo.getTableName() != null && !_entityInfo.getTableName().isEmpty()
                    ? _entityInfo.getTableName()
                    : getTableName(_entityInfo.getImplClass());
            eib.dataSourceAlias(_entityInfo.getDataSourceAlias()).baseType(_entityInfo.getBaseType())
                    .name(_entityInfo.getName()).tableName(tableName).description(_entityInfo.getDescription())
                    .implementation(_entityInfo.getImplClass().getName()).idFieldName(_entityInfo.getIdFieldName())
                    .versionNoFieldName(_entityInfo.getVersionNoFieldName()).handler(_entityInfo.getHandler())
                    .actionPolicy(_entityInfo.getActionPolicy());
            for (EntityFieldInfo _entityFieldInfo : _entityInfo.getAllFields()) {
                eib.addField(_entityFieldInfo.getType(), _entityFieldInfo.getFieldClass(), _entityFieldInfo.getName(),
                        _entityFieldInfo.getDescription(), _entityFieldInfo.getColumn(),
                        _entityFieldInfo.getReferences(),
                        _entityFieldInfo.getEnumImplClass() != null ? _entityFieldInfo.getEnumImplClass().getName()
                                : null,
                        _entityFieldInfo.getPrecision(), _entityFieldInfo.getScale(), _entityFieldInfo.getLength(),
                        _entityFieldInfo.isNullable());
            }

            // Implicit fields
            Class<?> clazz = _entityInfo.getImplClass();
            do {
                for (Field field : clazz.getDeclaredFields()) {
                    EntityFieldInfo implicitEntityFieldInfo = createEntityFieldInfo(_entitiesbyclassname, field);
                    if (_entityInfo.isField(field.getName())) {
                        EntityFieldInfo _entityFieldInfo = _entityInfo.getEntityFieldInfo(field.getName());
                        _entityFieldInfo.overrideBlank(implicitEntityFieldInfo);
                    } else {
                        if (implicitEntityFieldInfo != null) {
                            eib.addField(implicitEntityFieldInfo.getType(), implicitEntityFieldInfo.getFieldClass(),
                                    implicitEntityFieldInfo.getName(), implicitEntityFieldInfo.getDescription(),
                                    implicitEntityFieldInfo.getColumn(), implicitEntityFieldInfo.getReferences(),
                                    implicitEntityFieldInfo.getEnumImplClass() != null
                                            ? implicitEntityFieldInfo.getEnumImplClass().getName()
                                            : null,
                                    implicitEntityFieldInfo.getPrecision(), implicitEntityFieldInfo.getScale(),
                                    implicitEntityFieldInfo.getLength(), implicitEntityFieldInfo.isNullable());
                        }
                    }
                }
            } while ((clazz = clazz.getSuperclass()) != null);

            EntityInfo entityInfo = eib.build();
            entities.put(entityInfo.getName(), entityInfo);
        }

        return entities;
    }

    @SuppressWarnings("unchecked")
    protected FieldTypeInfo getFieldTypeInfo(Map<String, EntityInfo> _entitiesbyclassname, Field field)
            throws Exception {
        ConnectFieldDataType type = classToConnectDataTypeMap.get(field.getType());
        if (type == null) {
            if (Enum.class.isAssignableFrom(field.getType())) {
                return new FieldTypeInfo(ConnectFieldDataType.ENUM_REF, (Class<? extends Enum<?>>) field.getType());
            }

            EntityInfo _refEntityInfo = _entitiesbyclassname.get(field.getType().getName());
            if (_refEntityInfo == null) {
                throw new IllegalArgumentException("Can not to refer to an interconnect undefined field type. Class ["
                        + field.getDeclaringClass() + "], fieldType = [" + field.getType().getName() + "], field = ["
                        + field.getName() + "]");
            }

            return new FieldTypeInfo(ConnectFieldDataType.REF, _refEntityInfo.getName());
        } else if (ConnectFieldDataType.CHILD_LIST.equals(type)) {
            if (field.getGenericType() instanceof ParameterizedType) {
                Type[] argTypes = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                if (argTypes.length == 1) {
                    if (argTypes[0] instanceof Class) {
                        EntityInfo _refEntityInfo = _entitiesbyclassname.get(((Class<?>) argTypes[0]).getName());
                        if (_refEntityInfo == null) {
                            throw new IllegalArgumentException("Can not to refer to an interconnect undefined type ["
                                    + ((Class<?>) argTypes[0]).getName() + "], field = [" + field.getName() + "]");
                        }

                        return new FieldTypeInfo(type, _refEntityInfo.getName());
                    }
                }
            }
        }

        return new FieldTypeInfo(type);
    }

    protected abstract String getTableName(Class<?> entityClass) throws Exception;

    protected abstract EntityFieldInfo createEntityFieldInfo(Map<String, EntityInfo> _entitiesbyclassname, Field field)
            throws Exception;

    protected class FieldTypeInfo {

        private final ConnectFieldDataType type;

        private final Class<? extends Enum<?>> enumImplClass;

        private final String references;

        public FieldTypeInfo(ConnectFieldDataType type, Class<? extends Enum<?>> enumImplClass) {
            this.type = type;
            this.enumImplClass = enumImplClass;
            this.references = null;
        }

        public FieldTypeInfo(ConnectFieldDataType type, String references) {
            this.type = type;
            this.enumImplClass = null;
            this.references = references;
        }

        public FieldTypeInfo(ConnectFieldDataType type) {
            this.type = type;
            this.enumImplClass = null;
            this.references = null;
        }

        public ConnectFieldDataType getType() {
            return type;
        }

        public Class<? extends Enum<?>> getEnumImplClass() {
            return enumImplClass;
        }

        public boolean isWithEnumImplClass() {
            return enumImplClass != null;
        }

        public String getReferences() {
            return references;
        }

        public boolean isWithReferences() {
            return references != null;
        }
        
        public boolean isArray() {
            return type.isArray();
        }
    }

    private void populateBaseFields(EntityInfo.Builder eib, ConnectEntityBaseType base) throws Exception {
        switch (base) {
            case BASE_WORK_ENTITY:
                eib.addField(ConnectFieldDataType.STRING, String.class, "workBranchCode", "Work Branch Code",
                        "work_branch_cd");
                eib.addField(ConnectFieldDataType.BOOLEAN, Boolean.class, "inWorkflow", "In Workflow",
                        "in_workflow_fg");
                eib.addField(ConnectFieldDataType.LONG, Long.class, "originalCopyId", "Original Copy ID",
                        "original_copy_id");
                eib.addField(ConnectFieldDataType.STRING, String.class, "wfItemVersionType", "Work Item Version Type",
                        "wf_item_version_type");
            case BASE_AUDIT_ENTITY:
                eib.addField(ConnectFieldDataType.STRING, String.class, "createdBy", "Created By", "created_by");
                eib.addField(ConnectFieldDataType.STRING, String.class, "updatedBy", "Updated By", "updated_by");
                eib.addField(ConnectFieldDataType.TIMESTAMP, Date.class, "createDt", "Created On", "created_on");
                eib.addField(ConnectFieldDataType.TIMESTAMP, Date.class, "updateDt", "Updated On", "updated_on");
            case BASE_VERSION_ENTITY:
                eib.addField(ConnectFieldDataType.LONG, long.class, "versionNo", "Version No.", "version_no");
            case BASE_ENTITY:
                eib.addField(ConnectFieldDataType.LONG, Long.class, "id", "ID", "id");
            default:
                break;
        }
    }

    public String getRedirect() {
        return redirect;
    }

    public String[] breakdownCollectionString(String val) {
        return val != null ? val.split("\\|") : null;
    }

    public ResolvedCondition resolveLingualStringCondition(EntityFieldInfo entityFieldInfo, Date now,
            RestrictionType type, Object paramA, Object paramB) throws Exception {
        if (type.isLingual()) {
            LingualStringType lingualType = LingualStringType.fromCode((String) paramA);
            if (lingualType != null) {
                switch (type) {
                    case EQUALS_LINGUAL:
                        paramA = lingualType.value();
                        type = RestrictionType.EQUALS;
                        break;
                    case NOT_EQUALS_LINGUAL:
                        paramA = lingualType.value();
                        type = RestrictionType.NOT_EQUALS;
                        break;
                    default:
                        break;
                }
            } else {
                type = null;
            }
        }

        return new ResolvedCondition(type, paramA, paramB);
    }

    public ResolvedCondition resolveDateCondition(EntityFieldInfo entityFieldInfo, Date now, RestrictionType type,
            Object paramA, Object paramB) throws Exception {
        if (type.isLingual()) {
            LingualDateType lingualType = LingualDateType.fromCode((String) paramA);
            if (lingualType != null) {
                DateRange range = getDateRangeFromNow(now, lingualType);
                switch (type) {
                    case EQUALS_LINGUAL:
                        paramA = range.getFrom();
                        paramB = range.getTo();
                        type = RestrictionType.BETWEEN;
                        break;
                    case NOT_EQUALS_LINGUAL:
                        paramA = range.getFrom();
                        paramB = range.getTo();
                        type = RestrictionType.NOT_BETWEEN;
                        break;
                    case GREATER_OR_EQUAL_LINGUAL:
                        paramA = getMidnightDate(range.getFrom());
                        type = RestrictionType.GREATER_OR_EQUAL;
                        break;
                    case GREATER_THAN_LINGUAL:
                        paramA = getLastSecondDate(range.getFrom());
                        type = RestrictionType.GREATER_THAN;
                        break;
                    case LESS_OR_EQUAL_LINGUAL:
                        paramA = getLastSecondDate(range.getFrom());
                        type = RestrictionType.LESS_OR_EQUAL;
                        break;
                    case LESS_THAN_LINGUAL:
                        paramA = getMidnightDate(range.getFrom());
                        type = RestrictionType.LESS_THAN;
                        break;
                    default:
                        break;
                }
            } else {
                type = null;
            }
        } else {
            paramA = ConverterUtils.convert(Date.class, paramA);
            paramB = ConverterUtils.convert(Date.class, paramB);
            if (entityFieldInfo.isTimestamp()) {
                switch (type) {
                    case EQUALS:
                        paramA = getMidnightDate((Date) paramA);
                        paramB = getLastSecondDate((Date) paramA);
                        type = RestrictionType.BETWEEN;
                        break;
                    case BETWEEN:
                        paramA = getMidnightDate((Date) paramA);
                        paramB = getLastSecondDate((Date) paramB);
                        break;
                    case GREATER_OR_EQUAL:
                        paramA = getMidnightDate((Date) paramA);
                        break;
                    case GREATER_THAN:
                        paramA = getLastSecondDate((Date) paramA);
                        break;
                    case LESS_OR_EQUAL:
                        paramA = getLastSecondDate((Date) paramA);
                        break;
                    case LESS_THAN:
                        paramA = getMidnightDate((Date) paramA);
                        break;
                    case NOT_EQUALS:
                        paramA = getMidnightDate((Date) paramA);
                        paramB = getLastSecondDate((Date) paramA);
                        type = RestrictionType.NOT_BETWEEN;
                        break;
                    case NOT_BETWEEN:
                        paramA = getMidnightDate((Date) paramA);
                        paramB = getLastSecondDate((Date) paramB);
                        break;
                    default:
                        break;
                }
            }
        }

        return new ResolvedCondition(type, paramA, paramB);
    }

    public DateRange getDateRangeFromNow(Date now, LingualDateType type) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(now);
        cal1.setTime(now);
        switch (type) {
            case LAST_12MONTHS:
                cal1.add(Calendar.MONTH, -12);
                break;
            case LAST_3MONTHS:
                cal1.add(Calendar.MONTH, -3);
                break;
            case LAST_6MONTHS:
                cal1.add(Calendar.MONTH, -6);
                break;
            case LAST_9MONTHS:
                cal1.add(Calendar.MONTH, -9);
                break;
            case LAST_MONTH:
                cal1.add(Calendar.MONTH, -1);
                cal2.add(Calendar.MONTH, -1);
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case LAST_WEEK:
                cal1.add(Calendar.DATE, -7);
                cal2.add(Calendar.DATE, -7);
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                cal2.set(Calendar.DAY_OF_WEEK, 7);
                break;
            case LAST_YEAR:
                cal1.add(Calendar.YEAR, -1);
                cal2.add(Calendar.YEAR, -1);
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                cal2.set(Calendar.DAY_OF_YEAR, cal2.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case NEXT_12MONTHS:
                cal2.add(Calendar.MONTH, 12);
                break;
            case NEXT_3MONTHS:
                cal2.add(Calendar.MONTH, 3);
                break;
            case NEXT_6MONTHS:
                cal2.add(Calendar.MONTH, 6);
                break;
            case NEXT_9MONTHS:
                cal2.add(Calendar.MONTH, 9);
                break;
            case NEXT_MONTH:
                cal1.add(Calendar.MONTH, 1);
                cal2.add(Calendar.MONTH, 1);
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case NEXT_WEEK:
                cal1.add(Calendar.DATE, 7);
                cal2.add(Calendar.DATE, 7);
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                cal2.set(Calendar.DAY_OF_WEEK, 7);
                break;
            case NEXT_YEAR:
                cal1.add(Calendar.YEAR, 1);
                cal2.add(Calendar.YEAR, 1);
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                cal2.set(Calendar.DAY_OF_YEAR, cal2.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case THIS_MONTH:
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case THIS_WEEK:
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                cal2.set(Calendar.DAY_OF_WEEK, 7);
                break;
            case THIS_YEAR:
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                cal2.set(Calendar.DAY_OF_YEAR, cal2.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case TOMORROW:
                cal1.add(Calendar.DATE, 1);
                cal2.add(Calendar.DATE, 1);
                break;
            case YESTERDAY:
                cal1.add(Calendar.DATE, -1);
                cal2.add(Calendar.DATE, -1);
                break;
            case TODAY:
            default:
                break;
        }

        return new DateRange(getMidnightDate(cal1.getTime()), getLastSecondDate(cal2.getTime()));
    }

    public Date getDateFromNow(Date now, LingualDateType type) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);
        switch (type) {
            case LAST_12MONTHS:
                cal1.add(Calendar.MONTH, -12);
                break;
            case LAST_3MONTHS:
                cal1.add(Calendar.MONTH, -3);
                break;
            case LAST_6MONTHS:
                cal1.add(Calendar.MONTH, -6);
                break;
            case LAST_9MONTHS:
                cal1.add(Calendar.MONTH, -9);
                break;
            case LAST_MONTH:
                cal1.add(Calendar.MONTH, -1);
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case LAST_WEEK:
                cal1.add(Calendar.DATE, -7);
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                break;
            case LAST_YEAR:
                cal1.add(Calendar.YEAR, -1);
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                break;
            case NEXT_12MONTHS:
                cal1.add(Calendar.MONTH, 12);
                break;
            case NEXT_3MONTHS:
                cal1.add(Calendar.MONTH, 3);
                break;
            case NEXT_6MONTHS:
                cal1.add(Calendar.MONTH, 6);
                break;
            case NEXT_9MONTHS:
                cal1.add(Calendar.MONTH, 9);
                break;
            case NEXT_MONTH:
                cal1.add(Calendar.MONTH, 1);
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case NEXT_WEEK:
                cal1.add(Calendar.DATE, 7);
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                break;
            case NEXT_YEAR:
                cal1.add(Calendar.YEAR, 1);
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                break;
            case THIS_MONTH:
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case THIS_WEEK:
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                break;
            case THIS_YEAR:
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                break;
            case TOMORROW:
                cal1.add(Calendar.DATE, 1);
                break;
            case YESTERDAY:
                cal1.add(Calendar.DATE, -1);
                break;
            case TODAY:
            default:
                break;
        }

        return getMidnightDate(cal1.getTime());
    }

    public Date getMidnightDate(Date date) {
        return getMidnightDate(date, Locale.getDefault());
    }

    public Date getMidnightDate(Date date, Locale locale) {
        if (date == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance(locale);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date getLastSecondDate(Date date) {
        return getLastSecondDate(date, Locale.getDefault());
    }

    public Date getLastSecondDate(Date date, Locale locale) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance(locale);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public Object resolveSpecialParameter(String param) throws Exception {
        // TODO
        return param;
    }

    public Object getBeanFromJsonPayload(BaseRequest req) throws Exception {
        String[] payload = req.getPayload();
        if (payload != null && payload.length == 1) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
            return getBeanFormJson(objectMapper, payload[0], req.getEntity());
        }

        return null;
    }

    public UpdateDef getUpdates(DataSourceRequest req) throws Exception {
        UpdateDef.Builder fdb = UpdateDef.newBuilder();
        if (req.getUpdate() != null) {
            EntityInfo entityInfo = getEntityInfo(req.getEntity());
            BufferedReader reader = new BufferedReader(new StringReader(req.getUpdate()));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    if (p.length == 2) {
                        final String fieldName = p[0];
                        final Object val = ConverterUtils
                                .convert(entityInfo.getEntityFieldInfo(fieldName).getConvertClass(), p[1]);
                        fdb.update(fieldName, val);
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

        return fdb.build();
    }

    public JsonDataSourceResponse createDataSourceResponse(Object[] result, DataSourceRequest req, String errorCode,
            String errorMsg) throws Exception {
        checkInitialized();
        JsonDataSourceResponse resp = new JsonDataSourceResponse(errorCode, errorMsg);
        String[] payload = toJsonResultStringValues(result, req.getOperation(), req.getEntity());
        resp.setPayload(payload);
        return resp;
    }

    public JsonProcedureResponse createProcedureResponse(Object[] result, ProcedureRequest req) throws Exception {
        checkInitialized();
        JsonProcedureResponse resp = new JsonProcedureResponse();
        String[] payload = req.isUseRawPayload() ? getRawResult(result)
                : toJsonResultStringValues(result, DataSourceOperation.LIST_LEAN, req.getEntity());
        resp.setPayload(payload);
        return resp;
    }

    public QueryDef getQueryDef(String query) throws Exception {
        checkInitialized();
        if (query != null) {
            QueryDef.Builder fdb = QueryDef.newBuilder();
            BufferedReader reader = new BufferedReader(new StringReader(query));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    RestrictionType type = RestrictionType.fromCode(p[0]);
                    String fieldName = p.length > 2 ? p[2] : null;
                    String paramA = p.length > 3 ? p[3] : null;
                    String paramB = p.length > 4 ? p[4] : null;
                    int depth = Integer.parseInt(p[1]);
                    fdb.addRestrictionDef(type, fieldName, paramA, paramB, depth);
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            return fdb.build();
        }

        return null;
    }

    public QueryLoadingParams getQueryLoadingParams(List<DataSourceParam> params) {
        Map<String, String> _params = Collections.emptyMap();
        if (params != null && !params.isEmpty()) {
            _params = new LinkedHashMap<String, String>();
            for (DataSourceParam param : params) {
                _params.put(param.getName(), param.getVal());
            }
        }

        return new QueryLoadingParams(_params);
    }
    
    public List<OrderDef> getOrderDef(EntityInfo entityInfo, String order) throws Exception {
        checkInitialized();
        if (order != null) {
            List<OrderDef> orderDefList = new ArrayList<OrderDef>();
            BufferedReader reader = new BufferedReader(new StringReader(order));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    String fieldName = p[0];
                    if ("id".equals(fieldName) && entityInfo.getIdFieldName() == null) {
                        continue;
                    }

                    boolean ascending = "ASCENDING".equals(p[1]);
                    orderDefList.add(new OrderDef(entityInfo.getLocalFieldName(fieldName), ascending));
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            return orderDefList;
        }

        return Collections.emptyList();
    }

    public List<String> getAllEntityNames() {
        return new ArrayList<String>(entities.keySet());
    }

    public EntityInfo findEntityInfoByClass(Class<?> implClass) throws Exception {
        checkInitialized();
        return entitiesByImplClass.get(implClass);
    }

    public EntityInfo getEntityInfo(String entity) throws Exception {
        checkInitialized();
        EntityInfo entityInfo = entities.get(entity);
        if (entityInfo == null) {
            throw new RuntimeException("No interconnect entity information for [" + entity + "].");
        }

        return entityInfo;
    }

    public boolean isPresent(String entity) throws Exception {
        checkInitialized();
        return entities.containsKey(entity);
    }

    public void copy(List<EntityFieldInfo> fieldInfoList, final Object destBean, final Object srcBean)
            throws Exception {
        checkInitialized();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            Object val = PropertyUtils.getProperty(srcBean, entityFieldInfo.getName());
            PropertyUtils.setProperty(destBean, entityFieldInfo.getName(), val);
        }
    }

    public void copyChild(List<EntityFieldInfo> fieldInfoList, final String parentEntity, final Object destBean,
            final Object srcBean) throws Exception {
        checkInitialized();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            Object childBean = PropertyUtils.getProperty(srcBean, entityFieldInfo.getName());
            setParentBean(destBean, childBean, parentEntity, entityFieldInfo);
            PropertyUtils.setProperty(destBean, entityFieldInfo.getName(), childBean);
        }
    }

    @SuppressWarnings("unchecked")
    public void copyChildList(List<EntityFieldInfo> fieldInfoList, final String parentEntity, final Object destBean,
            final Object srcBean) throws Exception {
        checkInitialized();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            Object srcChildListBean = PropertyUtils.getProperty(srcBean, entityFieldInfo.getName());
            Object destChildListBean = PropertyUtils.getProperty(destBean, entityFieldInfo.getName());
            Collection<Object> destList = (Collection<Object>) destChildListBean;
            if (destList == null) {
                destList = entityFieldInfo.isSet() ? new HashSet<>() : new ArrayList<>();
                PropertyUtils.setProperty(destBean, entityFieldInfo.getName(), destList);
            } else {
                for (Object destChildBean : entityFieldInfo.isSet() ? new HashSet<>(destList)
                        : new ArrayList<>(destList)) {
                    destList.remove(destChildBean);
                }
            }

            if (srcChildListBean != null) {
                Collection<Object> srcList = (Collection<Object>) srcChildListBean;
                for (Object srcChildBean : srcList) {
                    Object destChildBean = copyChild(destBean, parentEntity, entityFieldInfo, srcChildBean);
                    destList.add(destChildBean);
                }
            }
        }
    }

    private Object copyChild(final Object destBean, final String parentEntity, final EntityFieldInfo entityFieldInfo,
            final Object srcChildBean) throws Exception {
        final EntityInfo entityInfo = getEntityInfo(entityFieldInfo.getReferences());
        final Object destChildBean = entityInfo.getImplClass().newInstance();
        copy(entityInfo.getRefFieldList(), destChildBean, srcChildBean);
        copy(entityInfo.getFieldList(), destChildBean, srcChildBean);
        copyChild(entityInfo.getChildFieldList(), entityInfo.getName(), destChildBean, srcChildBean);
        copyChildList(entityInfo.getChildListFieldList(), entityInfo.getName(), destChildBean, srcChildBean);
        setParentBean(destBean, destChildBean, parentEntity, entityFieldInfo);
        return destChildBean;
    }

    private void checkInitialized() throws Exception {
        if (!initialized) {
            throw new RuntimeException("FlowCentral interconnect not initialized.");
        }
    }

    private Object getBeanFormJson(ObjectMapper objectMapper, String json, String entity) throws Exception {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>()
            {
            };

        HashMap<String, Object> map = objectMapper.readValue(json, typeRef);
        return getBeanFromMap(objectMapper, map, entity);
    }

    @SuppressWarnings("unchecked")
    private Object getBeanFromMap(ObjectMapper objectMapper, Map<String, Object> map, String entity) throws Exception {
        EntityInfo entityInfo = getEntityInfo(entity);
        final Object bean = entityInfo.getImplClass().newInstance();
        // References
        if (refType.object()) {
            for (EntityFieldInfo entityFieldInfo : entityInfo.getRefFieldList()) {
                EntityInfo parentEntityInfo = getEntityInfo(entityFieldInfo.getReferences());
                Object id = map.get(entityInfo.getFieldNameFromLocal(entityFieldInfo.getName()));
                id = ConverterUtils.convert(entityFieldInfo.getConvertClass(), id);
                if (id != null) {
                    Object parentBean = entityInstFinder.findById(parentEntityInfo, id);
                    PropertyUtils.setProperty(bean, entityFieldInfo.getName(), parentBean);
                }
            }
        } else {
            for (EntityFieldInfo entityFieldInfo : entityInfo.getRefFieldList()) {
                Object val = map.get(entityInfo.getFieldNameFromLocal(entityFieldInfo.getName()));
                val = ConverterUtils.convert(entityFieldInfo.getConvertClass(), val);
                PropertyUtils.setProperty(bean, entityFieldInfo.getName(), val);
            }
        }

        // Fields
        for (EntityFieldInfo entityFieldInfo : entityInfo.getFieldList()) {
            Object val = map.get(entityInfo.getFieldNameFromLocal(entityFieldInfo.getName()));
            val = entityFieldInfo.isEnum() ? ConverterUtils.convert(entityFieldInfo.getEnumImplClass(), val)
                    : ConverterUtils.convert(entityFieldInfo.getConvertClass(), val);
            PropertyUtils.setProperty(bean, entityFieldInfo.getName(), val);
        }

        // Child
        for (EntityFieldInfo entityFieldInfo : entityInfo.getChildFieldList()) {
            final String childFieldName = entityInfo.getFieldNameFromLocal(entityFieldInfo.getName());
            Object val = map.get(childFieldName);
            if (val != null) {
                Object chbean = null;
                if (val instanceof String) {
                    chbean = getBeanFormJson(objectMapper, (String) val, entityFieldInfo.getReferences());
                } else if (val instanceof Map) {
                    chbean = getBeanFromMap(objectMapper, (Map<String, Object>) val, entityFieldInfo.getReferences());
                }

                if (chbean != null) {
                    setParentBean(bean, chbean, entity, entityFieldInfo);
                }

                PropertyUtils.setProperty(bean, entityFieldInfo.getName(), chbean);
            }
        }

        // Child list
        for (EntityFieldInfo entityFieldInfo : entityInfo.getChildListFieldList()) {
            final String childListFieldName = entityInfo.getFieldNameFromLocal(entityFieldInfo.getName());
            Object val = map.get(childListFieldName);
            if (val != null) {
                Object[] chs = ConverterUtils.convert(Object[].class, val);
                Collection<Object> list = entityFieldInfo.isSet() ? new HashSet<>() : new ArrayList<>();
                for (int i = 0; i < chs.length; i++) {
                    Object ch = chs[i];
                    Object chbean = null;
                    if (ch instanceof String) {
                        chbean = getBeanFormJson(objectMapper, (String) ch, entityFieldInfo.getReferences());
                    } else if (ch instanceof Map) {
                        chbean = getBeanFromMap(objectMapper, (Map<String, Object>) ch,
                                entityFieldInfo.getReferences());
                    }

                    if (chbean != null) {
                        setParentBean(bean, chbean, entity, entityFieldInfo);
                        list.add(chbean);
                    }
                }

                PropertyUtils.setProperty(bean, entityFieldInfo.getName(), list);
            }
        }

        return bean;
    }

    private void setParentBean(Object parentBean, Object bean, String parentEntity, EntityFieldInfo entityFieldInfo)
            throws Exception {
        if (bean != null) {
            EntityInfo entityInfo = getEntityInfo(entityFieldInfo.getReferences());
            EntityFieldInfo _refEntityFieldInfo = entityInfo.findRefToParent(parentEntity);
            if (_refEntityFieldInfo != null) {
                PropertyUtils.setProperty(bean, "id", null);
                PropertyUtils.setProperty(bean, _refEntityFieldInfo.getName(), parentBean);
            }
        }
    }

    private String[] toJsonResultStringValues(Object[] result, DataSourceOperation operation, String entity)
            throws Exception {
        if (result != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
            String[] strResult = new String[result.length];
            for (int i = 0; i < result.length; i++) {
                Object val = result[i];
                if (val != null) {
                    if (operation.entityResult()) {
                        strResult[i] = objectMapper
                                .writeValueAsString(toObjectMap(val, entity, operation.isList(), operation.isLean()));
                    } else {
                        strResult[i] = ConverterUtils.convert(String.class, val);
                    }
                }
            }

            return strResult;
        }

        return null;
    }

    private String[] getRawResult(Object[] result) {
        if (result != null) {
            String[] _result = new String[result.length];
            for (int i = 0; i < _result.length; i++) {
                _result[i] = String.valueOf(result[i]);
            }

            return _result;
        }

        return null;
    }

    private Map<String, Object> toObjectMap(Object bean, String entity, boolean list, boolean lean) throws Exception {
        Map<String, Object> map = new HashMap<>();
        EntityInfo entityInfo = getEntityInfo(entity);

        // References
        if (refType.object()) {
            for (EntityFieldInfo entityFieldInfo : entityInfo.getRefFieldList()) {
                EntityInfo parentEntityInfo = getEntityInfo(entityFieldInfo.getReferences());
                if (parentEntityInfo.getIdFieldName() != null) {
                    Object id = getBeanProperty(bean,
                            entityFieldInfo.getName() + "." + parentEntityInfo.getIdFieldName());
                    map.put(entityFieldInfo.getName(), id);
                }
            }
        } else {
            for (EntityFieldInfo entityFieldInfo : entityInfo.getRefFieldList()) {
                map.put(entityFieldInfo.getName(), PropertyUtils.getProperty(bean, entityFieldInfo.getName()));
            }
        }

        // Fields
        for (EntityFieldInfo entityFieldInfo : entityInfo.getFieldList()) {
            map.put(entityFieldInfo.getName(), PropertyUtils.getProperty(bean, entityFieldInfo.getName()));
        }

        // List-only
        if (list) {
            for (EntityFieldInfo entityFieldInfo : entityInfo.getListOnlyFieldList()) {
                map.put(entityFieldInfo.getName(), getBeanProperty(bean, entityFieldInfo.getReferences()));
            }
        }

        if (!lean) {
            // Child
            for (EntityFieldInfo entityFieldInfo : entityInfo.getChildFieldList()) {
                Object chbean = PropertyUtils.getProperty(bean, entityFieldInfo.getName());
                if (chbean != null) {
                    chbean = toObjectMap(chbean, entityFieldInfo.getReferences(), list, lean);
                }

                map.put(entityFieldInfo.getName(), chbean);
            }

            // Child list
            for (EntityFieldInfo entityFieldInfo : entityInfo.getChildListFieldList()) {
                Collection<?> chlist = (Collection<?>) PropertyUtils.getProperty(bean, entityFieldInfo.getName());
                if (chlist != null) {
                    Collection<Object> rchlist = entityFieldInfo.isSet() ? new HashSet<>() : new ArrayList<>();
                    for (Object chbean : chlist) {
                        chbean = toObjectMap(chbean, entityFieldInfo.getReferences(), list, lean);
                        rchlist.add(chbean);
                    }

                    map.put(entityFieldInfo.getName(), rchlist);
                } else {
                    map.put(entityFieldInfo.getName(), null);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String fieldName = entityInfo.getFieldNameFromLocal(entry.getKey());
            result.put(fieldName, entry.getValue());
        }

        return result;
    }

    private Object getBeanProperty(Object bean, String name) {
        try {
            return PropertyUtils.getNestedProperty(bean, name);
        } catch (Exception e) {
        }

        return null;
    }

    private String ensureLongName(String applicationName, String name) {
        if (name != null && !name.trim().isEmpty() && name.indexOf('.') < 0) {
            return applicationName + "." + name;
        }

        return name;
    }
}
