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
package com.flowcentraltech.flowcentral.connect.common;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DateRange;
import com.flowcentraltech.flowcentral.connect.common.data.EntityFieldInfo;
import com.flowcentraltech.flowcentral.connect.common.data.EntityInfo;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.OrderDef;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.connect.common.data.QueryDef;
import com.flowcentraltech.flowcentral.connect.common.data.ResolvedCondition;
import com.flowcentraltech.flowcentral.connect.configuration.xml.ApplicationConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.EntitiesConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.EntityConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.EntityFieldConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.util.XmlUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcdng.unify.convert.util.ConverterUtils;

/**
 * Flow central interconnect.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Interconnect {

    private static final Logger LOGGER = Logger.getLogger(Interconnect.class.getName());

    private final Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setDateFormat(DateFormat.LONG)
            .setPrettyPrinting().setVersion(1.0).create();

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

    private final RefType refType;

    private EntityInstFinder entityInstFinder;

    private boolean initialized;

    public Interconnect(RefType refType) {
        this.refType = refType;
        this.entities = Collections.emptyMap();
    }

    public String prettyJSON(Object src) {
        return gson.toJson(src, src.getClass());
    }

    public boolean init(String configurationFile, EntityInstFinder entityInstFinder) throws Exception {
        if (!initialized) {
            synchronized (Interconnect.class) {
                if (!initialized) {
                    LOGGER.log(Level.INFO, "Initializing flowCentral interconnect using configuration [{0}]...",
                            configurationFile);
                    entities = new HashMap<String, EntityInfo>();
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
                                    EntityInfo.Builder eib = EntityInfo.newBuilder(appEntityManagerFactory);
                                    eib.name(ensureLongName(applicationName, entityConfig.getName()))
                                            .description(entityConfig.getDescription())
                                            .implementation(entityConfig.getImplementation())
                                            .idFieldName(entityConfig.getIdFieldName())
                                            .versionNoFieldName(entityConfig.getVersionNoFieldName())
                                            .handler(entityConfig.getHandler())
                                            .actionPolicy(entityConfig.getActionPolicy());
                                    for (EntityFieldConfig entityFieldConfig : entityConfig.getEntityFieldList()) {
                                        eib.addField(entityFieldConfig.getType(), entityFieldConfig.getName(),
                                                ensureLongName(applicationName, entityFieldConfig.getReferences()),
                                                entityFieldConfig.getEnumImplClass());
                                    }

                                    EntityInfo entityInfo = eib.build();
                                    entities.put(entityInfo.getName(), entityInfo);
                                }

                                LOGGER.log(Level.INFO,
                                        "Loaded [{0}] entites interconnect information for application [{1}].",
                                        new Object[] { entityConfigList.size(), applicationName });
                            }
                        }
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

    public EntityInfo getEntityInfo(String entity) throws Exception {
        checkInitialized();
        EntityInfo entityInfo = entities.get(entity);
        if (entityInfo == null) {
            throw new RuntimeException("No interconnect entity information for [" + entity + "].");
        }

        return entityInfo;
    }

    public void copy(List<EntityFieldInfo> fieldInfoList, Object srcBean, Object destBean) throws Exception {
        checkInitialized();
        for (EntityFieldInfo entityFieldInfo : fieldInfoList) {
            Object val = PropertyUtils.getProperty(srcBean, entityFieldInfo.getName());
            PropertyUtils.setProperty(destBean, entityFieldInfo.getName(), val);
        }
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
        Object bean = entityInfo.getImplClass().newInstance();
        // References
        if (refType.object()) {
            for (EntityFieldInfo entityFieldInfo : entityInfo.getRefFieldList()) {
                EntityInfo parentEntityInfo = getEntityInfo(entityFieldInfo.getReferences());
                Object id = map.get(entityInfo.getFieldNameFromLocal(entityFieldInfo.getName()));
                id = ConverterUtils.convert(entityFieldInfo.getJavaClass(), id);
                if (id != null) {
                    Object parentBean = entityInstFinder.findById(parentEntityInfo, id);
                    PropertyUtils.setProperty(bean, entityFieldInfo.getName(), parentBean);
                }
            }
        } else {
            for (EntityFieldInfo entityFieldInfo : entityInfo.getRefFieldList()) {
                Object val = map.get(entityInfo.getFieldNameFromLocal(entityFieldInfo.getName()));
                val = ConverterUtils.convert(entityFieldInfo.getJavaClass(), val);
                PropertyUtils.setProperty(bean, entityFieldInfo.getName(), val);
            }
        }

        // Fields
        for (EntityFieldInfo entityFieldInfo : entityInfo.getFieldList()) {
            Object val = map.get(entityInfo.getFieldNameFromLocal(entityFieldInfo.getName()));
            val = entityFieldInfo.isEnum() ? ConverterUtils.convert(entityFieldInfo.getEnumImplClass(), val)
                    : ConverterUtils.convert(entityFieldInfo.getJavaClass(), val);
            PropertyUtils.setProperty(bean, entityFieldInfo.getName(), val);
        }

        // Child
        for (EntityFieldInfo entityFieldInfo : entityInfo.getChildFieldList()) {
            Object val = map.get(entityInfo.getFieldNameFromLocal(entityFieldInfo.getName()));
            if (val != null) {
                Object chbean = null;
                if (val instanceof String) {
                    chbean = getBeanFormJson(objectMapper, (String) val, entityFieldInfo.getReferences());
                } else if (val instanceof Map) {
                    chbean = getBeanFromMap(objectMapper, (Map<String, Object>) val, entityFieldInfo.getReferences());
                }

                PropertyUtils.setProperty(bean, entityFieldInfo.getName(), chbean);
            }
        }

        // Child list
        for (EntityFieldInfo entityFieldInfo : entityInfo.getChildListFieldList()) {
            Object val = map.get(entityInfo.getFieldNameFromLocal(entityFieldInfo.getName()));
            if (val != null) {
                Object[] chs = ConverterUtils.convert(Object[].class, val);
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < chs.length; i++) {
                    Object ch = chs[i];
                    Object chbean = null;
                    if (ch instanceof String) {
                        chbean = getBeanFormJson(objectMapper, (String) ch, entityFieldInfo.getReferences());
                    } else if (ch instanceof Map) {
                        chbean = getBeanFromMap(objectMapper, (Map<String, Object>) ch,
                                entityFieldInfo.getReferences());
                    }

                    list.add(chbean);
                }

                PropertyUtils.setProperty(bean, entityFieldInfo.getName(), list);
            }
        }

        return bean;
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
                Object id = getBeanProperty(bean, entityFieldInfo.getName() + "." + parentEntityInfo.getIdFieldName());
                map.put(entityFieldInfo.getName(), id);
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
                List<?> chlist = (List<?>) PropertyUtils.getProperty(bean, entityFieldInfo.getName());
                if (chlist != null) {
                    List<Object> rchlist = new ArrayList<>();
                    for (int i = 0; i < chlist.size(); i++) {
                        Object chbean = chlist.get(i);
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
        for (Map.Entry<String, Object> entry: map.entrySet()) {
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
