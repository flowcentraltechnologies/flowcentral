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
package com.flowcentraltech.flowcentral.connect.unify.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.flowcentraltech.flowcentral.connect.common.EntityInstFinder;
import com.flowcentraltech.flowcentral.connect.common.constants.RestrictionType;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingRequest;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingResponse;
import com.flowcentraltech.flowcentral.connect.common.data.FilterRestrictionDef;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.OrderDef;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.connect.common.data.QueryDef;
import com.flowcentraltech.flowcentral.connect.common.data.ResolvedCondition;
import com.flowcentraltech.flowcentral.connect.common.data.UpdateDef;
import com.flowcentraltech.flowcentral.connect.unify.UnifyInterconnect;
import com.tcdng.unify.common.constants.ConnectFieldDataType;
import com.tcdng.unify.common.data.EntityDTO;
import com.tcdng.unify.common.data.EntityFieldInfo;
import com.tcdng.unify.common.data.EntityInfo;
import com.tcdng.unify.common.data.EntityListingDTO;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Amongst;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.BeginsWith;
import com.tcdng.unify.core.criterion.Between;
import com.tcdng.unify.core.criterion.EndsWith;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Greater;
import com.tcdng.unify.core.criterion.GreaterOrEqual;
import com.tcdng.unify.core.criterion.IBeginsWith;
import com.tcdng.unify.core.criterion.IEndsWith;
import com.tcdng.unify.core.criterion.IEquals;
import com.tcdng.unify.core.criterion.ILike;
import com.tcdng.unify.core.criterion.INotEquals;
import com.tcdng.unify.core.criterion.IsNotNull;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Less;
import com.tcdng.unify.core.criterion.LessOrEqual;
import com.tcdng.unify.core.criterion.Like;
import com.tcdng.unify.core.criterion.NotAmongst;
import com.tcdng.unify.core.criterion.NotBeginWith;
import com.tcdng.unify.core.criterion.NotBetween;
import com.tcdng.unify.core.criterion.NotEndWith;
import com.tcdng.unify.core.criterion.NotEquals;
import com.tcdng.unify.core.criterion.NotLike;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Query;

/**
 * Implementation of flow central spring boot interconnect service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component("unify-interconnectservice")
public class UnifyInterconnectServiceImpl extends AbstractBusinessService
        implements UnifyInterconnectService, EntityInstFinder {

    private final UnifyInterconnect interconnect;

    public UnifyInterconnectServiceImpl() {
        this.interconnect = new UnifyInterconnect();
    }

    @Override
    public String getRedirect() {
        return interconnect.getRedirect();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findById(EntityInfo entityInfo, Object id) throws Exception {
        logInfo("Finding entity [{0}] by ID [{1}]...", new Object[] { entityInfo.getName(), id });
        return (T) db().list((Class<? extends Entity>) entityInfo.getImplClass(), id);
    }

    @Override
    public DetectEntityResponse detectEntity(DetectEntityRequest req) throws UnifyException {
        try {
            logInfo("Detect entity  [{0}]...", interconnect.prettyJSON(req));
            final boolean present = interconnect.isPresent(req.getEntity());
            return new DetectEntityResponse(present);
        } catch (Exception e) {
            throwOperationErrorException(e);
        }

        return null;
    }

    @Override
    public GetEntityResponse getEntity(GetEntityRequest req) throws UnifyException {
        try {
            logInfo("Get entity  [{0}]...", interconnect.prettyJSON(req));
            if (interconnect.isPresent(req.getEntity())) {
                EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
                return new GetEntityResponse(new EntityDTO(entityInfo));
            }

            return new GetEntityResponse();
        } catch (Exception e) {
            throwOperationErrorException(e);
        }

        return null;
    }

    @Override
    public EntityListingResponse listEntities(EntityListingRequest req) throws UnifyException {
        try {
            logInfo("List entities [{0}]...", interconnect.prettyJSON(req));
            List<EntityListingDTO> listings = new ArrayList<EntityListingDTO>();
            for (String entity : interconnect.getAllEntityNames()) {
                listings.add(new EntityListingDTO(entity));
            }

            return new EntityListingResponse(Collections.emptyList(), listings);
        } catch (Exception e) {
            throwOperationErrorException(e);
        }

        return null;
    }

    @Override
    public JsonProcedureResponse executeProcedureRequest(ProcedureRequest req) throws UnifyException {
        try {
            logInfo("Execute procedure request [{0}]...", interconnect.prettyJSON(req));
            Object reqBean = req.isUseRawPayload() ? req.getPayload() : interconnect.getBeanFromJsonPayload(req);
            UnifyInterconnectProcedure procedure = getComponent(UnifyInterconnectProcedure.class, req.getOperation());
            procedure.execute(reqBean, req.isReadOnly());
            Object[] result = req.isReadOnly() ? null : new Object[] { reqBean };
            return interconnect.createProcedureResponse(result, req);
        } catch (Exception e) {
            throwOperationErrorException(e);
        }

        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public JsonDataSourceResponse processDataSourceRequest(DataSourceRequest req) throws UnifyException {
        try {
            if (interconnect.isPresent(req.getEntity())) {
                logInfo("Processing datasource request [{0}]...", interconnect.prettyJSON(req));
                final EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
                String errorCode = null;
                String errorMsg = null;

                Object[] result = null;
                UnifyInterconnectEntityDataSourceHandler handler = null;
                try {
                    if (entityInfo.isWithHandler()
                            && (handler = getComponent(UnifyInterconnectEntityDataSourceHandler.class,
                                    entityInfo.getHandler())).supports(req)) {
                        result = handler.process(entityInfo.getImplClass(), req);
                    } else {
                        UnifyEntityActionPolicy unifyEntityActionPolicy = entityInfo.isWithActionPolicy()
                                ? getComponent(UnifyEntityActionPolicy.class, entityInfo.getActionPolicy())
                                : null;
                        switch (req.getOperation()) {
                            case COUNT_ALL: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                Long count = (long) db().countAll(query);
                                result = new Object[] { count };
                            }
                                break;
                            case VALIDATE: {
                                Object reqBean = interconnect.getBeanFromJsonPayload(req);
                                if (unifyEntityActionPolicy != null) {
                                    result = unifyEntityActionPolicy.validate(req.getEvalMode(), reqBean);
                                }

                                break;
                            }
                            case CREATE: {
                                Object reqBean = interconnect.getBeanFromJsonPayload(req);
                                if (unifyEntityActionPolicy != null) {
                                    unifyEntityActionPolicy.executePreCreateAction(reqBean);
                                }

                                Object id = db().create((Entity) reqBean);

                                if (unifyEntityActionPolicy != null) {
                                    unifyEntityActionPolicy.executePostCreateAction(reqBean);
                                }

                                result = new Object[] { id };
                            }
                                break;
                            case DELETE: {
                                Object reqBean = interconnect.getBeanFromJsonPayload(req);
                                if (reqBean == null) {
                                    Query<? extends Entity> query = createQuery(
                                            (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                    List<?> results = db().listAll(query);
                                    reqBean = results != null && results.size() == 1 ? results.get(0) : null;
                                }

                                if (reqBean == null) {
                                    errorMsg = "Could not find entity to delete.";
                                } else {
                                    if (req.version() && entityInfo.isWithVersionNo()) {
                                        PropertyUtils.setProperty(reqBean, entityInfo.getVersionNoFieldName(),
                                                req.getVersionNo());
                                    }

                                    if (unifyEntityActionPolicy != null) {
                                        unifyEntityActionPolicy.executePreDeleteAction(reqBean);
                                    }

                                    db().deleteByIdVersion((Entity) reqBean);

                                    if (unifyEntityActionPolicy != null) {
                                        unifyEntityActionPolicy.executePreDeleteAction(reqBean);
                                    }
                                }
                            }
                                break;
                            case DELETE_ALL: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                int count = db().deleteAll(query);
                                result = new Object[] { count };
                            }
                                break;
                            case FIND: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                Object results = db().find(query);
                                result = results != null ? new Object[] { results } : null;
                            }
                                break;
                            case FIND_ALL: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                query.setOffset(req.getOffset());
                                query.setLimit(req.getLimit());

                                List<?> results = db().findAll(query);
                                result = results.toArray(new Object[results.size()]);
                            }
                                break;
                            case FIND_LEAN: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                Object results = db().findLean(query);
                                result = results != null ? new Object[] { results } : null;
                            }
                                break;
                            case LIST: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                Object results = db().list(query);
                                result = results != null ? new Object[] { results } : null;
                            }
                                break;
                            case LIST_ALL: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                query.setOffset(req.getOffset());
                                query.setLimit(req.getLimit());

                                List<?> results = db().listAll(query);
                                result = results.toArray(new Object[results.size()]);
                            }
                                break;
                            case LIST_LEAN: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                Object results = db().listLean(query);
                                result = results != null ? new Object[] { results } : null;
                            }
                                break;
                            case UPDATE:
                            case UPDATE_EDITABLE_CHILD:
                            case UPDATE_LEAN: {
                                Object reqBean = interconnect.getBeanFromJsonPayload(req);
                                if (reqBean != null) {
                                    Object id = PropertyUtils.getProperty(reqBean, entityInfo.getIdFieldName());
                                    Object saveBean = db().find((Class<? extends Entity>) entityInfo.getImplClass(), id);
                                    Object versionNo = req.version()
                                            ? PropertyUtils.getProperty(saveBean, entityInfo.getVersionNoFieldName())
                                            : null;
                                    // References
                                    interconnect.copy(entityInfo.getRefFieldList(), saveBean, reqBean);
                                    // Fields
                                    interconnect.copy(entityInfo.getFieldList(), saveBean, reqBean);

                                    if (!req.getOperation().isLean()) {
                                        // Child
                                        interconnect.copyChild(entityInfo.getChildFieldList(), req.getEntity(), saveBean,
                                                reqBean);
                                        // Child list
                                        interconnect.copyChildList(entityInfo.getChildListFieldList(), req.getEntity(),
                                                saveBean, reqBean);
                                    }

                                    if (req.version()) {
                                        PropertyUtils.setProperty(saveBean, entityInfo.getVersionNoFieldName(), versionNo);
                                    }

                                    if (unifyEntityActionPolicy != null) {
                                        unifyEntityActionPolicy.executePreUpdateAction(saveBean);
                                    }

                                    db().updateById((Entity) saveBean);

                                    if (unifyEntityActionPolicy != null) {
                                        unifyEntityActionPolicy.executePostUpdateAction(saveBean);
                                    }

                                    result = new Object[] { 1L };
                                } else if (req.getId() != null && req.getUpdate() != null) {
                                    Object saveBean = db().find((Class<? extends Entity>) entityInfo.getImplClass(),
                                            req.getId());
                                    UpdateDef updateDef = interconnect.getUpdates(req);
                                    for (String fieldName : updateDef.getFieldNames()) {
                                        PropertyUtils.setProperty(saveBean, fieldName, updateDef.getUpdate(fieldName));
                                    }

                                    if (unifyEntityActionPolicy != null) {
                                        unifyEntityActionPolicy.executePreUpdateAction(saveBean);
                                    }

                                    db().updateById((Entity) saveBean);

                                    if (unifyEntityActionPolicy != null) {
                                        unifyEntityActionPolicy.executePostUpdateAction(saveBean);
                                    }

                                    result = new Object[] { 1L };
                                }
                            }
                                break;
                            case UPDATE_ALL:
                                break;
                            case VALUE: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                query.setDistinct(req.getOperation().isDistinct());
                                EntityFieldInfo entityFieldInfo = entityInfo.getEntityFieldInfo(req.getFieldName());
                                Object results = db().value(entityFieldInfo.getJavaClass(), req.getFieldName(), query);
                                result = results != null ? new Object[] { results } : null;
                            }
                                break;
                            case VALUE_LIST: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                query.setDistinct(req.getOperation().isDistinct());
                                EntityFieldInfo entityFieldInfo = entityInfo.getEntityFieldInfo(req.getFieldName());
                                List<?> results = db().valueList(entityFieldInfo.getJavaClass(), req.getFieldName(), query);
                                result = results.toArray(new Object[results.size()]);
                            }
                                break;
                            case VALUE_SET: {
                                Query<? extends Entity> query = createQuery(
                                        (Class<? extends Entity>) entityInfo.getImplClass(), req);
                                query.setDistinct(req.getOperation().isDistinct());
                                EntityFieldInfo entityFieldInfo = entityInfo.getEntityFieldInfo(req.getFieldName());
                                List<?> results = new ArrayList<>(
                                        db().valueSet(entityFieldInfo.getJavaClass(), req.getFieldName(), query));
                                result = results.toArray(new Object[results.size()]);
                            }
                                break;
                            default:
                                break;

                        }

                    }

                    JsonDataSourceResponse resp = interconnect.createDataSourceResponse(result, req, errorCode, errorMsg);
                    logInfo("Returning response [{0}]...", interconnect.prettyJSON(resp));
                    return resp;
                } catch (Exception e) {
                    logSevere("Datasource request processing failure.", e);
                    errorMsg = e.getMessage();
                    setRollbackTransactions();
                }

                result = null;
                JsonDataSourceResponse resp = interconnect.createDataSourceResponse(result, req, errorCode, errorMsg);
                logInfo("Returning response [{0}]...", interconnect.prettyJSON(resp));
                return resp;
            }
        } catch (UnifyException e) {
            throw e;
        } catch (Exception e) {
             throwOperationErrorException(e);
        }

        return null;
    }

    @Override
    protected void onInitialize() throws UnifyException {
        try {
            String interconnectConfigFile = getContainerSetting(String.class, "flowcentral.interconnect.configfile");
            logInfo("Initializing spring boot interconnect [{0}]...", interconnectConfigFile);
            interconnect.init(interconnectConfigFile, this);
        } catch (UnifyException e) {
            throw e;
        } catch (Exception e) {
            throwOperationErrorException(e);
        }
    }

    private <T extends Entity> Query<T> createQuery(Class<T> entityClass, DataSourceRequest req) throws Exception {
        EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
        Query<T> query = Query.of(entityClass);
        Restriction restriction = createRestriction(entityInfo, req);
        if (restriction != null) {
            query.addRestriction(restriction);
        }

        List<OrderDef> orderDefList = interconnect.getOrderDef(entityInfo, req.getOrder());
        if (!orderDefList.isEmpty()) {
            for (OrderDef orderDef : orderDefList) {
                query.addOrder(orderDef.isAscending() ? OrderType.ASCENDING : OrderType.DESCENDING,
                        orderDef.getFieldName());
            }
        }

        return query;
    }

    private Restriction createRestriction(EntityInfo entityInfo, DataSourceRequest req) throws Exception {
        Restriction restriction = null;
        if (req.byIdOnly()) {
            restriction = new Equals(entityInfo.getIdFieldName(), req.getId());
        } else if (req.byIdVersion()) {
            restriction = new And().add(new Equals(entityInfo.getIdFieldName(), req.getId()))
                    .add(new Equals(entityInfo.getVersionNoFieldName(), req.getVersionNo()));
        } else if (req.byQuery()) {
            QueryDef queryDef = interconnect.getQueryDef(req.getQuery());
            final int len = queryDef.size();
            if (len > 0) {
                FilterRestrictionDef fo = queryDef.getFilterRestrictionDef(0);
                if (len == 1 && !fo.getType().isCompound()) {
                    restriction = getSimpleRestriction(entityInfo, fo, new Index(), getNow());
                } else {
                    restriction = addCompoundRestriction(entityInfo, queryDef, fo, new Index().set(1), getNow());
                }
            }
        }

        if (restriction == null && !req.isIgnoreEmptyCriteria()) {
            throw new RuntimeException("Operation on entity [" + req.getEntity() + "] must have a clause.");
        }

        return restriction;
    }

    private Restriction addCompoundRestriction(EntityInfo entityInfo, QueryDef queryDef, FilterRestrictionDef fo,
            Index index, Date now) throws Exception {
        List<Restriction> restrictionList = new ArrayList<Restriction>();
        final int len = queryDef.size();
        final int depth = fo.getDepth();
        int i = index.get();
        for (; i < len; i++) {
            FilterRestrictionDef sfo = queryDef.getFilterRestrictionDef(i);
            if (sfo.getDepth() > depth) {
                Restriction predicate = null;
                if (sfo.getType().isCompound()) {
                    predicate = addCompoundRestriction(entityInfo, queryDef, sfo, index.set(i + 1), now);
                    i = index.get() - 1;
                } else {
                    predicate = getSimpleRestriction(entityInfo, sfo, index, now);
                }

                restrictionList.add(predicate);
            } else {
                break;
            }
        }

        index.set(i);

        return RestrictionType.AND.equals(fo.getType()) ? new And().addAll(restrictionList)
                : new Or().addAll(restrictionList);
    }

    @SuppressWarnings("unchecked")
    private Restriction getSimpleRestriction(EntityInfo entityInfo, FilterRestrictionDef fo, Index index, Date now)
            throws Exception {
        EntityFieldInfo _entityFieldInfo = entityInfo.getEntityFieldInfo(fo.getFieldName());
        RestrictionType type = fo.getType();
        Object paramA = interconnect.resolveSpecialParameter(fo.getParamA());
        Object paramB = interconnect.resolveSpecialParameter(fo.getParamB());
        if (!type.isFieldVal() && !type.isParameterVal()) {
            if (type.isLingual() && _entityFieldInfo.isString()) {
                ResolvedCondition resolved = interconnect.resolveLingualStringCondition(_entityFieldInfo, now, type,
                        paramA, paramB);
                type = resolved.getType();
                paramA = resolved.getParamA();
                paramB = resolved.getParamB();
            } else if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                ResolvedCondition resolved = interconnect.resolveDateCondition(_entityFieldInfo, now, type, paramA,
                        paramB);
                type = resolved.getType();
                paramA = resolved.getParamA();
                paramB = resolved.getParamB();
            } else {
                ConnectFieldDataType connectFieldDataType = _entityFieldInfo.getType();
                Class<?> javaClass = connectFieldDataType.javaClass();
                if (paramA != null) {
                    if (type.isAmongst()) {
                        paramA = ConverterUtils.convert(List.class, javaClass,
                                Arrays.asList(interconnect.breakdownCollectionString((String) paramA)));
                    } else {
                        paramA = ConverterUtils.convert(javaClass, paramA);
                    }
                }

                if (paramB != null) {
                    paramB = ConverterUtils.convert(javaClass, paramB);
                }
            }
        }

        if (_entityFieldInfo.isEnum()) {
            paramA = paramA instanceof List
                    ? ConverterUtils.convert(List.class, _entityFieldInfo.getEnumImplClass(), paramA)
                    : ConverterUtils.convert(_entityFieldInfo.getEnumImplClass(), paramA);
            paramB = ConverterUtils.convert(_entityFieldInfo.getEnumImplClass(), paramB);
        }

        final String fieldName = entityInfo.getLocalFieldName(fo.getFieldName());
        switch (type) {
            case AMONGST:
                return new Amongst(fieldName, (Collection<?>) paramA);
            case AND:
                break;
            case BEGINS_WITH:
                return new BeginsWith(fieldName, paramA);
            case BEGINS_WITH_FIELD:
                break;
            case BEGINS_WITH_PARAM:
                break;
            case BETWEEN:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return new Between(fieldName, (Date) paramA, (Date) paramB);
                }

                if (_entityFieldInfo.isInteger()) {
                    return new Between(fieldName, ConverterUtils.convert(Long.class, paramA),
                            ConverterUtils.convert(Long.class, paramB));
                }

                if (_entityFieldInfo.isDouble()) {
                    return new Between(fieldName, ConverterUtils.convert(Double.class, paramA),
                            ConverterUtils.convert(Double.class, paramB));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return new Between(fieldName, (BigDecimal) paramA, (BigDecimal) paramB);
                }
                break;
            case BETWEEN_FIELD:
                break;
            case BETWEEN_PARAM:
                break;
            case ENDS_WITH:
                return new EndsWith(fieldName, paramA);
            case ENDS_WITH_FIELD:
                break;
            case ENDS_WITH_PARAM:
                break;
            case EQUALS:
                return new Equals(fieldName, paramA);
            case EQUALS_COLLECTION:
                break;
            case EQUALS_FIELD:
                break;
            case EQUALS_LINGUAL:
                return new Equals(fieldName, paramA);
            case EQUALS_PARAM:
                break;
            case GREATER_OR_EQUAL:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return new GreaterOrEqual(fieldName, (Date) paramA);
                }

                if (_entityFieldInfo.isInteger()) {
                    return new GreaterOrEqual(fieldName, ConverterUtils.convert(Long.class, paramA));
                }

                if (_entityFieldInfo.isDouble()) {
                    return new GreaterOrEqual(fieldName, ConverterUtils.convert(Double.class, paramA));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return new GreaterOrEqual(fieldName, (BigDecimal) paramA);
                }

                break;
            case GREATER_OR_EQUAL_COLLECTION:
                break;
            case GREATER_OR_EQUAL_FIELD:
                break;
            case GREATER_OR_EQUAL_LINGUAL:
                break;
            case GREATER_OR_EQUAL_PARAM:
                break;
            case GREATER_THAN:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return new Greater(fieldName, (Date) paramA);
                }

                if (_entityFieldInfo.isInteger()) {
                    return new Greater(fieldName, ConverterUtils.convert(Long.class, paramA));
                }

                if (_entityFieldInfo.isDouble()) {
                    return new Greater(fieldName, ConverterUtils.convert(Double.class, paramA));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return new Greater(fieldName, (BigDecimal) paramA);
                }

                break;
            case GREATER_THAN_COLLECTION:
                break;
            case GREATER_THAN_FIELD:
                break;
            case GREATER_THAN_LINGUAL:
                break;
            case GREATER_THAN_PARAM:
                break;
            case IBEGINS_WITH:
                paramA = paramToLowerCase(paramA);
                return new IBeginsWith(fieldName, paramA);
            case IBEGINS_WITH_FIELD:
                break;
            case IBEGINS_WITH_PARAM:
                break;
            case IENDS_WITH:
                paramA = paramToLowerCase(paramA);
                return new IEndsWith(fieldName, paramA);
            case IENDS_WITH_FIELD:
                break;
            case IENDS_WITH_PARAM:
                break;
            case IEQUALS:
                paramA = paramToLowerCase(paramA);
                return new IEquals(fieldName, (String) paramA);
            case ILIKE:
                paramA = paramToLowerCase(paramA);
                return new ILike(fieldName, paramA);
            case ILIKE_FIELD:
                break;
            case ILIKE_PARAM:
                break;
            case INOT_EQUALS:
                paramA = paramToLowerCase(paramA);
                return new INotEquals(fieldName, (String) paramA);
            case IS_NOT_NULL:
                return new IsNotNull(fieldName);
            case IS_NULL:
                return new IsNull(fieldName);
            case LESS_OR_EQUAL:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return new LessOrEqual(fieldName, (Date) paramA);
                }

                if (_entityFieldInfo.isInteger()) {
                    return new LessOrEqual(fieldName, ConverterUtils.convert(Long.class, paramA));
                }

                if (_entityFieldInfo.isDouble()) {
                    return new LessOrEqual(fieldName, ConverterUtils.convert(Double.class, paramA));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return new LessOrEqual(fieldName, (BigDecimal) paramA);
                }

                break;
            case LESS_OR_EQUAL_COLLECTION:
                break;
            case LESS_OR_EQUAL_FIELD:
                break;
            case LESS_OR_EQUAL_LINGUAL:
                break;
            case LESS_OR_EQUAL_PARAM:
                break;
            case LESS_THAN:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return new Less(fieldName, (Date) paramA);
                }

                if (_entityFieldInfo.isInteger()) {
                    return new Less(fieldName, ConverterUtils.convert(Long.class, paramA));
                }

                if (_entityFieldInfo.isDouble()) {
                    return new Less(fieldName, ConverterUtils.convert(Double.class, paramA));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return new Less(fieldName, (BigDecimal) paramA);
                }

                break;
            case LESS_THAN_COLLECTION:
                break;
            case LESS_THAN_FIELD:
                break;
            case LESS_THAN_LINGUAL:
                break;
            case LESS_THAN_PARAM:
                break;
            case LIKE:
                return new Like(fieldName, paramA);
            case LIKE_FIELD:
                break;
            case LIKE_PARAM:
                break;
            case NOT_AMONGST:
                return new NotAmongst(fieldName, (Collection<?>) paramA);
            case NOT_BEGIN_WITH:
                return new NotBeginWith(fieldName, paramA);
            case NOT_BEGIN_WITH_FIELD:
                break;
            case NOT_BEGIN_WITH_PARAM:
                break;
            case NOT_BETWEEN:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return new NotBetween(fieldName, (Date) paramA, (Date) paramB);
                }

                if (_entityFieldInfo.isInteger()) {
                    return new NotBetween(fieldName, ConverterUtils.convert(Long.class, paramA),
                            ConverterUtils.convert(Long.class, paramB));
                }

                if (_entityFieldInfo.isDouble()) {
                    return new NotBetween(fieldName, ConverterUtils.convert(Double.class, paramA),
                            ConverterUtils.convert(Double.class, paramB));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return new NotBetween(fieldName, (BigDecimal) paramA, (BigDecimal) paramB);
                }
                break;
            case NOT_BETWEEN_FIELD:
                break;
            case NOT_BETWEEN_PARAM:
                break;
            case NOT_END_WITH:
                return new NotEndWith(fieldName, paramA);
            case NOT_END_WITH_FIELD:
                break;
            case NOT_END_WITH_PARAM:
                break;
            case NOT_EQUALS:
                return new NotEquals(fieldName, paramA);
            case NOT_EQUALS_COLLECTION:
                break;
            case NOT_EQUALS_FIELD:
                break;
            case NOT_EQUALS_LINGUAL:
                break;
            case NOT_EQUALS_PARAM:
                break;
            case NOT_LIKE:
                return new NotLike(fieldName, paramA);
            case NOT_LIKE_FIELD:
                break;
            case NOT_LIKE_PARAM:
                break;
            case OR:
                break;
            default:
                break;

        }

        return null;
    }

    private class Index {

        private int i;

        public int get() {
            return i;
        }

        public Index set(int i) {
            this.i = i;
            return this;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object paramToLowerCase(Object param) {
        return param != null && param.getClass().isEnum()
                ? Enum.valueOf((Class<? extends Enum>) param.getClass(), param.toString())
                : ((String) param).toLowerCase();
    }
}
