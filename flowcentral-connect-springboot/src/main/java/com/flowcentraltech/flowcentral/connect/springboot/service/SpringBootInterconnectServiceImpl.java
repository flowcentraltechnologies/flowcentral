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
package com.flowcentraltech.flowcentral.connect.springboot.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.flowcentraltech.flowcentral.connect.common.EntityInstFinder;
import com.flowcentraltech.flowcentral.connect.common.constants.RestrictionType;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.EntityDTO;
import com.flowcentraltech.flowcentral.connect.common.data.EntityFieldInfo;
import com.flowcentraltech.flowcentral.connect.common.data.EntityInfo;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingDTO;
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
import com.flowcentraltech.flowcentral.connect.configuration.constants.ConnectFieldDataType;
import com.flowcentraltech.flowcentral.connect.springboot.SpringBootInterconnect;
import com.tcdng.unify.convert.util.ConverterUtils;

/**
 * Implementation of flow central spring boot interconnect service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class SpringBootInterconnectServiceImpl implements SpringBootInterconnectService, EntityInstFinder {

    private final Logger LOGGER = Logger.getLogger(SpringBootInterconnectServiceImpl.class.getName());

    private final SpringBootInterconnect interconnect;

    private final Environment env;

    private final ApplicationContext context;

    private Map<String, PlatformInfo> platforms;

    private PlatformInfo defaultPlatform;

    private boolean logging;

    @Autowired
    public SpringBootInterconnectServiceImpl(SpringBootInterconnect interconnect, Environment env,
            ApplicationContext context) {
        this.interconnect = interconnect;
        this.platforms = new HashMap<String, PlatformInfo>();
        this.env = env;
        this.context = context;
    }

    @PostConstruct
    public void init() throws Exception {
        String interconectConfigFile = env.getProperty("flowcentral.interconnect.configfile");
        logging = env.getProperty("flowcentral.interconnect.logging.enabled", boolean.class, false);
        logInfo("Initializing spring boot interconnect [{0}]...", interconectConfigFile);
        interconnect.init(interconectConfigFile, this);
    }

    @Override
    public String getRedirect() {
        return interconnect.getRedirect();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findById(EntityInfo entityInfo, Object id) throws Exception {
        logInfo("Finding entity [{0}] by ID [{1}]...", new Object[] { entityInfo.getName(), id });
        PlatformInfo platform = getPlatform(entityInfo);
        T result = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = platform.emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            result = em.find((Class<T>) entityInfo.getImplClass(), id);
            tx.commit();
            em.close();
        } catch (Exception e) {
            logSevere("Datasource request processing failure.", e);
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return result;
    }

    @Override
    public DetectEntityResponse detectEntity(DetectEntityRequest req) throws Exception {
        logInfo("Detect entity  [{0}]...", interconnect.prettyJSON(req));
        final boolean present = interconnect.isPresent(req.getEntity());
        return new DetectEntityResponse(present);
    }

    @Override
    public GetEntityResponse getEntity(GetEntityRequest req) throws Exception {
        logInfo("Get entity  [{0}]...", interconnect.prettyJSON(req));
        if (interconnect.isPresent(req.getEntity())) {
            EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
            return new GetEntityResponse(new EntityDTO(entityInfo));
        }

        return new GetEntityResponse();
    }

    @Override
    public EntityListingResponse listEntities(EntityListingRequest req) throws Exception {
        logInfo("List entities [{0}]...", interconnect.prettyJSON(req));
        List<EntityListingDTO> listings = new ArrayList<EntityListingDTO>();
        for (String entity : interconnect.getAllEntityNames()) {
            listings.add(new EntityListingDTO(entity));
        }

        return new EntityListingResponse(Collections.emptyList(), listings);
    }

    @Override
    public JsonProcedureResponse executeProcedureRequest(ProcedureRequest req) throws Exception {
        if (context.containsBean(req.getOperation())) {
            logInfo("Execute procedure request [{0}]...", interconnect.prettyJSON(req));
            Object reqBean = req.isUseRawPayload() ? req.getPayload() : interconnect.getBeanFromJsonPayload(req);
            SpringBootInterconnectProcedure procedure = context.getBean(req.getOperation(),
                    SpringBootInterconnectProcedure.class);
            procedure.execute(reqBean, req.isReadOnly());
            Object[] result = req.isReadOnly() ? null : new Object[] { reqBean };
            return interconnect.createProcedureResponse(result, req);
        }

        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public JsonDataSourceResponse processDataSourceRequest(DataSourceRequest req) throws Exception {
        if (interconnect.isPresent(req.getEntity())) {
            logInfo("Processing datasource request [{0}]...", interconnect.prettyJSON(req));
            final EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
            PlatformInfo platform = getPlatform(entityInfo);
            String errorCode = null;
            String errorMsg = null;

            EntityManager em = null;
            EntityTransaction tx = null;
            Object[] result = null;
            SpringBootInterconnectEntityDataSourceHandler handler = null;
            try {
                em = platform.emf.createEntityManager();
                tx = em.getTransaction();
                tx.begin();

                if (entityInfo.isWithHandler() && (handler = context.getBean(entityInfo.getHandler(),
                        SpringBootInterconnectEntityDataSourceHandler.class)).supports(req)) {
                    result = handler.process(entityInfo.getImplClass(), req);
                } else {
                    EntityActionPolicy entityActionPolicy = entityInfo.isWithActionPolicy()
                            ? context.getBean(entityInfo.getActionPolicy(), EntityActionPolicy.class)
                            : null;
                    switch (req.getOperation()) {
                        case COUNT_ALL: {
                            CriteriaQuery<Long> cq = createLongQuery(entityInfo.getImplClass(), em, req);
                            Long count = em.createQuery(cq).getSingleResult();
                            result = new Object[] { count };
                        }
                            break;
                        case VALIDATE: {
                            Object reqBean = interconnect.getBeanFromJsonPayload(req);
                            if (entityActionPolicy != null) {
                                result = entityActionPolicy.validate(req.getEvalMode(), reqBean);
                            }

                            break;
                        }
                        case CREATE: {
                            Object reqBean = interconnect.getBeanFromJsonPayload(req);
                            if (entityActionPolicy != null) {
                                entityActionPolicy.executePreCreateAction(reqBean);
                            }

                            // em.merge(reqBean);
                            em.persist(reqBean);
                            em.flush();
                            if (entityActionPolicy != null) {
                                entityActionPolicy.executePostCreateAction(reqBean);
                            }

                            Object id = PropertyUtils.getProperty(reqBean, entityInfo.getIdFieldName());
                            result = new Object[] { id };
                        }
                            break;
                        case DELETE: {
                            Object reqBean = interconnect.getBeanFromJsonPayload(req);
                            if (reqBean == null) {
                                CriteriaQuery<?> cq = createQuery(entityInfo.getImplClass(), em, req);
                                TypedQuery<?> query = em.createQuery(cq);
                                List<?> results = query.getResultList();
                                reqBean = results != null && results.size() == 1 ? results.get(0) : null;
                            }

                            if (reqBean == null) {
                                errorMsg = "Could not find entity to delete.";
                            } else {
                                if (req.version() && entityInfo.isWithVersionNo()) {
                                    PropertyUtils.setProperty(reqBean, entityInfo.getVersionNoFieldName(),
                                            req.getVersionNo());
                                }

                                if (entityActionPolicy != null) {
                                    entityActionPolicy.executePreDeleteAction(reqBean);
                                }

                                em.remove(reqBean);

                                if (entityActionPolicy != null) {
                                    entityActionPolicy.executePreDeleteAction(reqBean);
                                }

                            }
                        }
                            break;
                        case DELETE_ALL: {
                            CriteriaDelete<?> cd = createDeleteQuery(entityInfo.getImplClass(), em, req);
                            int count = em.createQuery(cd).executeUpdate();
                            result = new Object[] { count };
                        }
                            break;
                        case FIND:
                        case FIND_ALL:
                        case FIND_LEAN:
                        case LIST:
                        case LIST_ALL:
                        case LIST_LEAN: {
                            CriteriaQuery<?> cq = createQuery(entityInfo.getImplClass(), em, req);
                            TypedQuery<?> query = em.createQuery(cq);
                            if (req.getOffset() >= 0) {
                                query.setFirstResult(req.getOffset());
                            }

                            if (req.getLimit() > 0) {
                                query.setMaxResults(req.getLimit());
                            }

                            List<?> results = query.getResultList();
                            if (!req.getOperation().isMultipleResult()) {
                                if (results.size() > 1) {
                                    throw new RuntimeException(
                                            "Mutiple records found on single item operation on entity ["
                                                    + req.getEntity() + "].");
                                }
                            }

                            result = results.toArray(new Object[results.size()]);
                        }
                            break;
                        case UPDATE:
                        case UPDATE_EDITABLE_CHILD:
                        case UPDATE_LEAN: {
                            Object reqBean = interconnect.getBeanFromJsonPayload(req);
                            if (reqBean != null) {
                                Object id = PropertyUtils.getProperty(reqBean, entityInfo.getIdFieldName());
                                Object saveBean = em.find(entityInfo.getImplClass(), id);
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

                                if (entityActionPolicy != null) {
                                    entityActionPolicy.executePreUpdateAction(saveBean);
                                }

                                em.merge(saveBean);

                                if (entityActionPolicy != null) {
                                    entityActionPolicy.executePostUpdateAction(saveBean);
                                }

                                result = new Object[] { 1L };
                            } else if (req.getId() != null && req.getUpdate() != null) {
                                Object saveBean = em.find(entityInfo.getImplClass(), req.getId());
                                UpdateDef updateDef = interconnect.getUpdates(req);
                                for (String fieldName : updateDef.getFieldNames()) {
                                    PropertyUtils.setProperty(saveBean, fieldName, updateDef.getUpdate(fieldName));
                                }

                                if (entityActionPolicy != null) {
                                    entityActionPolicy.executePreUpdateAction(saveBean);
                                }

                                em.merge(saveBean);

                                if (entityActionPolicy != null) {
                                    entityActionPolicy.executePostUpdateAction(saveBean);
                                }

                                result = new Object[] { 1L };
                            }
                        }
                            break;
                        case UPDATE_ALL:
                            break;
                        case VALUE:
                        case VALUE_LIST:
                        case VALUE_SET: {
                            CriteriaQuery<Tuple> cq = createTupleQuery(entityInfo.getImplClass(), em, req);
                            cq.distinct(req.getOperation().isDistinct());
                            List<Tuple> tupleResult = em.createQuery(cq).getResultList();
                            if (!req.getOperation().isMultipleResult()) {
                                if (tupleResult.size() > 1) {
                                    throw new RuntimeException(
                                            "Mutiple records found on single item operation on entity ["
                                                    + req.getEntity() + "].");
                                }
                            }

                            result = new Object[tupleResult.size()];
                            for (int i = 0; i < result.length; i++) {
                                result[i] = tupleResult.get(i).get(0);
                            }
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
                if (tx != null) {
                    tx.rollback();
                    tx = null;
                }
            } finally {
                if (tx != null) {
                    tx.commit();
                }

                if (em != null) {
                    em.close();
                }
            }

            result = null;
            JsonDataSourceResponse resp = interconnect.createDataSourceResponse(result, req, errorCode, errorMsg);
            logInfo("Returning response [{0}]...", interconnect.prettyJSON(resp));
            return resp;
        }

        return null;
    }

    private <T> CriteriaQuery<T> createQuery(Class<T> entityClass, EntityManager em, DataSourceRequest req)
            throws Exception {
        EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        Predicate restrictions = createRestriction(cb, root, entityInfo, req);
        if (restrictions != null) {
            cq.where(restrictions);
        }

        List<OrderDef> orderDefList = interconnect.getOrderDef(entityInfo, req.getOrder());
        if (!orderDefList.isEmpty()) {
            List<Order> orderList = new ArrayList<Order>();
            for (OrderDef orderDef : orderDefList) {
                if (orderDef.isAscending()) {
                    orderList.add(cb.asc(root.get(orderDef.getFieldName())));
                } else {
                    orderList.add(cb.desc(root.get(orderDef.getFieldName())));
                }
            }

            cq.orderBy(orderList);
        }

        return cq;
    }

    private <T> CriteriaQuery<Tuple> createTupleQuery(Class<T> entityClass, EntityManager em, DataSourceRequest req)
            throws Exception {
        EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<T> root = cq.from(entityClass);
        cq.multiselect(root.get(req.getFieldName()));
        Predicate restrictions = createRestriction(cb, root, entityInfo, req);
        if (restrictions != null) {
            cq.where(restrictions);
        }

        return cq;
    }

    private <T> CriteriaQuery<Long> createLongQuery(Class<T> entityClass, EntityManager em, DataSourceRequest req)
            throws Exception {
        EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        Predicate restrictions = createRestriction(cb, root, entityInfo, req);
        if (restrictions != null) {
            cq.where(restrictions);
        }

        return cq;
    }

    private <T> CriteriaDelete<T> createDeleteQuery(Class<T> entityClass, EntityManager em, DataSourceRequest req)
            throws Exception {
        EntityInfo entityInfo = interconnect.getEntityInfo(req.getEntity());
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<T> cq = cb.createCriteriaDelete(entityClass);
        Root<T> root = cq.from(entityClass);
        Predicate restrictions = createRestriction(cb, root, entityInfo, req);
        if (restrictions != null) {
            cq.where(restrictions);
        }

        return cq;
    }

    private Predicate createRestriction(CriteriaBuilder cb, Root<?> root, EntityInfo entityInfo, DataSourceRequest req)
            throws Exception {
        Predicate predicate = null;
        if (req.byIdOnly()) {
            predicate = cb.equal(root.get(entityInfo.getIdFieldName()), req.getId());
        } else if (req.byIdVersion()) {
            predicate = cb.and(cb.equal(root.get(entityInfo.getIdFieldName()), req.getId()),
                    cb.equal(root.get(entityInfo.getVersionNoFieldName()), req.getVersionNo()));
        } else if (req.byQuery()) {
            QueryDef queryDef = interconnect.getQueryDef(req.getQuery());
            final int len = queryDef.size();
            if (len > 0) {
                FilterRestrictionDef fo = queryDef.getFilterRestrictionDef(0);
                if (len == 1 && !fo.getType().isCompound()) {
                    predicate = getSimplePredicate(cb, entityInfo, fo, new Index(root), getNow());
                } else {
                    predicate = addCompoundPredicate(cb, entityInfo, queryDef, fo, new Index(root).set(1), getNow());
                }
            }
        }

        if (predicate == null && !req.isIgnoreEmptyCriteria()) {
            throw new RuntimeException("Operation on entity [" + req.getEntity() + "] must have a clause.");
        }

        return predicate;
    }

    private Date getNow() {
        // TODO
        return new Date();
    }

    private Predicate addCompoundPredicate(CriteriaBuilder cb, EntityInfo entityInfo, QueryDef queryDef,
            FilterRestrictionDef fo, Index index, Date now) throws Exception {
        List<Predicate> predicateList = new ArrayList<Predicate>();
        final int len = queryDef.size();
        final int depth = fo.getDepth();
        int i = index.get();
        for (; i < len; i++) {
            FilterRestrictionDef sfo = queryDef.getFilterRestrictionDef(i);
            if (sfo.getDepth() > depth) {
                Predicate predicate = null;
                if (sfo.getType().isCompound()) {
                    predicate = addCompoundPredicate(cb, entityInfo, queryDef, sfo, index.set(i + 1), now);
                    i = index.get() - 1;
                } else {
                    predicate = getSimplePredicate(cb, entityInfo, sfo, index, now);
                }

                predicateList.add(predicate);
            } else {
                break;
            }
        }

        index.set(i);

        Predicate[] restrictions = predicateList.toArray(new Predicate[predicateList.size()]);
        return RestrictionType.AND.equals(fo.getType()) ? cb.and(restrictions) : cb.or(restrictions);
    }

    @SuppressWarnings("unchecked")
    private Predicate getSimplePredicate(CriteriaBuilder cb, EntityInfo entityInfo, FilterRestrictionDef fo,
            Index index, Date now) throws Exception {
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
                return index.getRoot().get(fieldName).in((Collection<?>) paramA);
            case AND:
                break;
            case BEGINS_WITH:
                return cb.like(index.getRoot().get(fieldName), paramA + "%");
            case BEGINS_WITH_FIELD:
                break;
            case BEGINS_WITH_PARAM:
                break;
            case BETWEEN:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return cb.between(index.getRoot().get(fieldName), (Date) paramA, (Date) paramB);
                }

                if (_entityFieldInfo.isInteger()) {
                    return cb.between(index.getRoot().get(fieldName), ConverterUtils.convert(Long.class, paramA),
                            ConverterUtils.convert(Long.class, paramB));
                }

                if (_entityFieldInfo.isDouble()) {
                    return cb.between(index.getRoot().get(fieldName), ConverterUtils.convert(Double.class, paramA),
                            ConverterUtils.convert(Double.class, paramB));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return cb.between(index.getRoot().get(fieldName), (BigDecimal) paramA, (BigDecimal) paramB);
                }
                break;
            case BETWEEN_FIELD:
                break;
            case BETWEEN_PARAM:
                break;
            case ENDS_WITH:
                return cb.like(index.getRoot().get(fieldName), "%" + paramA);
            case ENDS_WITH_FIELD:
                break;
            case ENDS_WITH_PARAM:
                break;
            case EQUALS:
                return cb.equal(index.getRoot().get(fieldName), paramA);
            case EQUALS_COLLECTION:
                break;
            case EQUALS_FIELD:
                break;
            case EQUALS_LINGUAL:
                return cb.equal(index.getRoot().get(fieldName), paramA);
            case EQUALS_PARAM:
                break;
            case GREATER_OR_EQUAL:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return cb.greaterThanOrEqualTo(index.getRoot().get(fieldName), (Date) paramA);
                }

                if (_entityFieldInfo.isInteger()) {
                    return cb.greaterThanOrEqualTo(index.getRoot().get(fieldName),
                            ConverterUtils.convert(Long.class, paramA));
                }

                if (_entityFieldInfo.isDouble()) {
                    return cb.greaterThanOrEqualTo(index.getRoot().get(fieldName),
                            ConverterUtils.convert(Double.class, paramA));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return cb.greaterThanOrEqualTo(index.getRoot().get(fieldName), (BigDecimal) paramA);
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
                    return cb.greaterThan(index.getRoot().get(fieldName), (Date) paramA);
                }

                if (_entityFieldInfo.isInteger()) {
                    return cb.greaterThan(index.getRoot().get(fieldName), ConverterUtils.convert(Long.class, paramA));
                }

                if (_entityFieldInfo.isDouble()) {
                    return cb.greaterThan(index.getRoot().get(fieldName), ConverterUtils.convert(Double.class, paramA));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return cb.greaterThan(index.getRoot().get(fieldName), (BigDecimal) paramA);
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
                return cb.like(lowerCriterion(cb, index, paramA, fieldName), paramA + "%");
            case IBEGINS_WITH_FIELD:
                break;
            case IBEGINS_WITH_PARAM:
                break;
            case IENDS_WITH:
                paramA = paramToLowerCase(paramA);
                return cb.like(lowerCriterion(cb, index, paramA, fieldName), "%" + paramA);
            case IENDS_WITH_FIELD:
                break;
            case IENDS_WITH_PARAM:
                break;
            case IEQUALS:
                paramA = paramToLowerCase(paramA);
                return cb.equal(lowerCriterion(cb, index, paramA, fieldName), paramA);
            case ILIKE:
                paramA = paramToLowerCase(paramA);
                return cb.like(lowerCriterion(cb, index, paramA, fieldName), "%" + paramA + "%");
            case ILIKE_FIELD:
                break;
            case ILIKE_PARAM:
                break;
            case INOT_EQUALS:
                paramA = paramToLowerCase(paramA);
                return cb.equal(lowerCriterion(cb, index, paramA, fieldName), paramA).not();
            case IS_NOT_NULL:
                return cb.isNotNull(index.getRoot().get(fieldName));
            case IS_NULL:
                return cb.isNull(index.getRoot().get(fieldName));
            case LESS_OR_EQUAL:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return cb.lessThanOrEqualTo(index.getRoot().get(fieldName), (Date) paramA);
                }

                if (_entityFieldInfo.isInteger()) {
                    return cb.lessThanOrEqualTo(index.getRoot().get(fieldName),
                            ConverterUtils.convert(Long.class, paramA));
                }

                if (_entityFieldInfo.isDouble()) {
                    return cb.lessThanOrEqualTo(index.getRoot().get(fieldName),
                            ConverterUtils.convert(Double.class, paramA));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return cb.lessThanOrEqualTo(index.getRoot().get(fieldName), (BigDecimal) paramA);
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
                    return cb.lessThan(index.getRoot().get(fieldName), (Date) paramA);
                }

                if (_entityFieldInfo.isInteger()) {
                    return cb.lessThan(index.getRoot().get(fieldName), ConverterUtils.convert(Long.class, paramA));
                }

                if (_entityFieldInfo.isDouble()) {
                    return cb.lessThan(index.getRoot().get(fieldName), ConverterUtils.convert(Double.class, paramA));
                }

                if (_entityFieldInfo.isDecimal()) {
                    return cb.lessThan(index.getRoot().get(fieldName), (BigDecimal) paramA);
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
                return cb.like(index.getRoot().get(fieldName), "%" + paramA + "%");
            case LIKE_FIELD:
                break;
            case LIKE_PARAM:
                break;
            case NOT_AMONGST:
                return index.getRoot().get(fieldName).in((Collection<?>) paramA).not();
            case NOT_BEGIN_WITH:
                return cb.like(index.getRoot().get(fieldName), paramA + "%").not();
            case NOT_BEGIN_WITH_FIELD:
                break;
            case NOT_BEGIN_WITH_PARAM:
                break;
            case NOT_BETWEEN:
                if (_entityFieldInfo.isDate() || _entityFieldInfo.isTimestamp()) {
                    return cb.between(index.getRoot().get(fieldName), (Date) paramA, (Date) paramB).not();
                }

                if (_entityFieldInfo.isInteger()) {
                    return cb.between(index.getRoot().get(fieldName), ConverterUtils.convert(Long.class, paramA),
                            ConverterUtils.convert(Long.class, paramB)).not();
                }

                if (_entityFieldInfo.isDouble()) {
                    return cb.between(index.getRoot().get(fieldName), ConverterUtils.convert(Double.class, paramA),
                            ConverterUtils.convert(Double.class, paramB)).not();
                }

                if (_entityFieldInfo.isDecimal()) {
                    return cb.between(index.getRoot().get(fieldName), (BigDecimal) paramA, (BigDecimal) paramB).not();
                }
                break;
            case NOT_BETWEEN_FIELD:
                break;
            case NOT_BETWEEN_PARAM:
                break;
            case NOT_END_WITH:
                return cb.like(index.getRoot().get(fieldName), "%" + paramA).not();
            case NOT_END_WITH_FIELD:
                break;
            case NOT_END_WITH_PARAM:
                break;
            case NOT_EQUALS:
                return cb.equal(index.getRoot().get(fieldName), paramA).not();
            case NOT_EQUALS_COLLECTION:
                break;
            case NOT_EQUALS_FIELD:
                break;
            case NOT_EQUALS_LINGUAL:
                break;
            case NOT_EQUALS_PARAM:
                break;
            case NOT_LIKE:
                return cb.like(index.getRoot().get(fieldName), "%" + paramA + "%").not();
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

        private final Root<?> root;

        private int i;

        public Index(Root<?> root) {
            this.root = root;
        }

        public Root<?> getRoot() {
            return root;
        }

        public int get() {
            return i;
        }

        public Index set(int i) {
            this.i = i;
            return this;
        }
    }

    private void logInfo(String message, Object... params) {
        if (logging) {
            LOGGER.log(Level.INFO, message, params);
        }
    }

    private void logSevere(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message, e);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object paramToLowerCase(Object param) {
        return param != null && param.getClass().isEnum()
                ? Enum.valueOf((Class<? extends Enum>) param.getClass(), param.toString())
                : ((String) param).toLowerCase();
    }

    private Expression<String> lowerCriterion(CriteriaBuilder cb, Index index, Object param, String fieldName) {
        return param != null && param.getClass().isEnum() ? index.getRoot().get(fieldName)
                : cb.lower(index.getRoot().get(fieldName));
    }

    private PlatformInfo getPlatform(EntityInfo entityInfo) {
        if (entityInfo.getEntityManagerFactory() != null) {
            PlatformInfo platform = platforms.get(entityInfo.getEntityManagerFactory());
            if (platform == null) {
                synchronized (this) {
                    if (platform == null) {
                        EntityManagerFactory emf = context.getBean(entityInfo.getEntityManagerFactory(),
                                EntityManagerFactory.class);
                        platform = new PlatformInfo(emf);
                        platforms.put(entityInfo.getEntityManagerFactory(), platform);
                    }
                }
            }

            return platform;
        }

        if (defaultPlatform == null) {
            synchronized (this) {
                if (defaultPlatform == null) {
                    EntityManagerFactory emf = context.getBean(EntityManagerFactory.class);
                    defaultPlatform = new PlatformInfo(emf);
                }
            }
        }

        return defaultPlatform;
    }

    private class PlatformInfo {

        private final EntityManagerFactory emf;

        private PlatformInfo(EntityManagerFactory emf) {
            this.emf = emf;
        }

    }
}
