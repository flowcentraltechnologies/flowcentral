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
package com.flowcentraltech.flowcentral.common.business;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.entities.EntityWrapper;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.business.BusinessService;
import com.tcdng.unify.core.criterion.AggregateFunction;
import com.tcdng.unify.core.criterion.GroupingFunction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Aggregation;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.GroupingAggregation;
import com.tcdng.unify.core.database.Query;

/**
 * Environment service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface EnvironmentService extends BusinessService {

    /**
     * Gets the environment database.
     * 
     * @return the environment database
     * @throws UnifyException
     *                        if an error occurs
     */
    Database getDatabase() throws UnifyException;

    /**
     * Gets the environment database.
     * 
     * @param datasource the data source
     * @return the environment database
     * @throws UnifyException
     *                        if an error occurs
     */
    Database getDatabase(String datasource) throws UnifyException;

    /**
     * Clears roll back on current transactions in database session.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void clearRollbackTransactions() throws UnifyException;

    /**
     * Sets save point for transaction session.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void setSavePoint() throws UnifyException;

    /**
     * Clears current save point for transaction.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void clearSavePoint() throws UnifyException;

    /**
     * Roll back transaction to last save point.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void rollbackToSavePoint() throws UnifyException;

    /**
     * Performs validation on an entity record
     * 
     * @param inst
     *             the record
     * @param mode
     *             the evaluation mode
     * @return list of validation messages if any
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> validate(Entity inst, EvaluationMode mode) throws UnifyException;
    
    /**
     * Performs policy action only on supplied entity.
     * 
     * @param ctx
     *            the entity action context
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult performEntityAction(EntityActionContext ctx) throws UnifyException;

    /**
     * Performs policy action only on supplied entity list.
     * 
     * @param ctx
     *            the entity list action context
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityListActionResult performEntityAction(EntityListActionContext ctx) throws UnifyException;

    /**
     * Creates a record in database.
     * 
     * @param inst
     *             the record to persist
     * @return the record ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Object create(Entity inst) throws UnifyException;

    /**
     * Creates a record in database.
     * 
     * @param wrapperInst
     *             the wrapped record to persist
     * @return the record ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Object create(EntityWrapper wrapperInst) throws UnifyException;

    /**
     * Creates a record in database and applies a policy if supplied.
     * 
     * @param ctx
     *            the entity action context
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult create(EntityActionContext ctx) throws UnifyException;

    /**
     * Finds a persistent record by ID. Child and child list properties are
     * populated.
     * 
     * @param clazz
     *              the record type
     * @param id
     *              the record ID
     * @return the record found otherwise null
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> T find(Class<T> clazz, Object id) throws UnifyException;

    /**
     * Finds a persistent record by query. Child and child list properties are
     * populated.
     * 
     * @param query
     *              the record query
     * @return the record found otherwise null
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> T find(Query<T> query) throws UnifyException;

    /**
     * Finds a lean persistent record by ID.
     * 
     * @param clazz
     *              the record type
     * @param id
     *              the record ID
     * @return the record found otherwise null
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> T findLean(Class<T> clazz, Object id) throws UnifyException;

    /**
     * Finds a lean persistent record by query.
     * 
     * @param query
     *              the record query
     * @return the record found otherwise null
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> T findLean(Query<T> query) throws UnifyException;

    /**
     * Finds all records by criteria returning resulting record in a map. The keys
     * of the map are values of fields specified by the key property of the
     * criteria.
     * 
     * @param keyClass
     *                 the map key class
     * @param keyName
     *                 the key field
     * @param query
     *                 the query object.
     * @return the resulting map.
     * @throws UnifyException
     *                        if criteria key property is not set. If an error
     *                        occurs
     */
    <T, U extends Entity> Map<T, U> findAllMap(Class<T> keyClass, String keyName, Query<U> query) throws UnifyException;

    /**
     * Returns a map of lists by key. List-only properties of returned objects are
     * not populated. Child and child list properties are not populated.
     * 
     * @param keyClass
     *                 the map key class
     * @param keyName
     *                 the key name
     * @param query
     *                 the search query
     * @return map of lists
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U extends Entity> Map<T, List<U>> findAllListMap(Class<T> keyClass, String keyName, Query<U> query)
            throws UnifyException;

    /**
     * Finds all records with fields that match supplied query.
     * 
     * @param query
     *              the query to match
     * @return list of records found
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> List<T> findAll(Query<T> query) throws UnifyException;

    /**
     * Finds all records and their child record with fields that match supplied
     * query.
     * 
     * @param query
     *              the query to match
     * @return list of records found
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> List<T> findAllWithChildren(Query<T> query) throws UnifyException;

    /**
     * Finds child records into supplied record.
     * 
     * @param record
     *               the record to find children into
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> void findChildren(T record) throws UnifyException;

    /**
     * Finds editable child records into supplied record.
     * 
     * @param record
     *               the record to find children into
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> void findEditableChildren(T record) throws UnifyException;

    /**
     * Finds read-only child records into supplied record.
     * 
     * @param record
     *               the record to find children into
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> void findReadOnlyChildren(T record) throws UnifyException;

    /**
     * Lists a persistent record by ID. Child and child list properties are
     * populated.
     * 
     * @param clazz
     *              the record type
     * @param id
     *              the record ID
     * @return the record found otherwise null
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> T list(Class<T> clazz, Object id) throws UnifyException;

    /**
     * Lists a persistent record by query. Child and child list properties are
     * populated.
     * 
     * @param query
     *              the record query
     * @return the record found otherwise null
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> T list(Query<T> query) throws UnifyException;

    /**
     * Lists a lean persistent record by ID.
     * 
     * @param clazz
     *              the record type
     * @param id
     *              the record ID
     * @return the record found otherwise null
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> T listLean(Class<T> clazz, Object id) throws UnifyException;

    /**
     * Lists a lean persistent record by query.
     * 
     * @param query
     *              the record query
     * @return the record found otherwise null
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> T listLean(Query<T> query) throws UnifyException;

    /**
     * Lists all records by criteria returning resulting record in a map. The keys
     * of the map are values of fields specified by the key property of the
     * criteria.
     * 
     * @param keyClass
     *                 the map key class
     * @param keyName
     *                 the key field
     * @param query
     *                 the query object.
     * @return the resulting map.
     * @throws UnifyException
     *                        if criteria key property is not set. If an error
     *                        occurs
     */
    <T, U extends Entity> Map<T, U> listAllMap(Class<T> keyClass, String keyName, Query<U> query) throws UnifyException;

    /**
     * Lists all records with fields that match supplied query.
     * 
     * @param query
     *              the query to match
     * @return list of records found
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> List<T> listAll(Query<T> query) throws UnifyException;

    /**
     * Lists all records and their child record with fields that match supplied
     * query.
     * 
     * @param query
     *              the query to match
     * @return list of records found
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> List<T> listAllWithChildren(Query<T> query) throws UnifyException;

    /**
     * Lists child records into supplied record.
     * 
     * @param record
     *               the record to list children into
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> void listChildren(T record) throws UnifyException;

    /**
     * Lists editable child records into supplied record.
     * 
     * @param record
     *               the record to list children into
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> void listEditableChildren(T record) throws UnifyException;


    /**
     * Lists read-only child records into supplied record.
     * 
     * @param record
     *               the record to list children into
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> void listReadOnlyChildren(T record) throws UnifyException;

    /**
     * Lists the value of a record's property.
     * 
     * @param valueClazz
     *                    the value type
     * @param recordClazz
     *                    the record type
     * @param id
     *                    the record ID
     * @param property
     *                    the property
     * @return the selected value
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U extends Entity> T listValue(Class<T> valueClazz, Class<U> recordClazz, Object id, String property)
            throws UnifyException;

    /**
     * Updates a record in database.
     * 
     * @param inst
     *             the record to update
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateById(Entity inst) throws UnifyException;

    /**
     * Updates a record in database.
     * 
     * @param inst
     *             the record to update
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateByIdVersion(Entity inst) throws UnifyException;

    /**
     * Updates a record in database.
     * 
     * @param wrappedInst
     *             the wrapped record to update
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateByIdVersion(EntityWrapper wrappedInst) throws UnifyException;

    /**
     * Updates a record by ID and version number. Only editable children are updated.
     * 
     * @param record
     *            the record to modify
     * @return the number of record updated. Always 1.
     * @throws UnifyException
     *             if an error occurs during modify
     */
    int updateByIdVersionEditableChildren(Entity record) throws UnifyException;

    /**
     * Updates a record in database using by ID and version and applies a policy if
     * supplied.
     * 
     * @param ctx
     *            the entity action context
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult updateByIdVersion(EntityActionContext ctx) throws UnifyException;

    /**
     * Updates a record in database using lean rule.
     * 
     * @param inst
     *             the record to update
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateLean(Entity inst) throws UnifyException;

    /**
     * Updates record in database by ID. Child records, if any, are not updated.
     * 
     * @param record
     *               the record to update
     * @return the number of record updated. Always 1.
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    int updateLeanById(Entity record) throws UnifyException;

    /**
     * Updates record in database by ID and version number. Child records, if any,
     * are not updated.
     * 
     * @param record
     *               the record to update
     * @return the number of record updated.
     * @throws UnifyException
     *                        If an error occurs
     */
    int updateLeanByIdVersion(Entity record) throws UnifyException;

    /**
     * Updates a record in database using lean rule.
     * 
     * @param wrappedInst
     *             the wrapped record to update
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateLean(EntityWrapper wrappedInst) throws UnifyException;

    /**
     * Updates record in database by ID. Child records, if any, are not updated.
     * 
     * @param wrappedInst
     *               the wrapped record to update
     * @return the number of record updated. Always 1.
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    int updateLeanById(EntityWrapper wrappedInst) throws UnifyException;

    /**
     * Updates record in database by ID and version number. Child records, if any,
     * are not updated.
     * 
     * @param wrappedInst
     *               the wrapped instance to update
     * @return the number of record updated.
     * @throws UnifyException
     *                        If an error occurs
     */
    int updateLeanByIdVersion(EntityWrapper wrappedInst) throws UnifyException;

    /**
     * Updates a record in database using lean rule and applies a policy if
     * supplied.
     * 
     * @param ctx
     *            the entity action context
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult updateLean(EntityActionContext ctx) throws UnifyException;

    /**
     * Updates record by ID.
     * 
     * @param clazz
     *               the record type
     * @param id
     *               the record ID
     * @param update
     *               update object
     * @return number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateById(Class<? extends Entity> clazz, Object id, Update update) throws UnifyException;

    /**
     * Updates all records with fields that match criteria using supplied update
     * information.
     * 
     * @param query
     *               the query object
     * @param update
     *               the update information object
     * @return the number of record updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateAll(Query<? extends Entity> query, Update update) throws UnifyException;

    /**
     * Adds supplied value to field for all records that match criteria.
     * 
     * @param fieldClass
     *                   the value type
     * @param fieldName
     *                   the field name
     * @param val
     *                   the value to add
     * @param query
     *                   the criteria
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Number, U extends Entity> int add(Class<T> fieldClass, String fieldName, T val, Query<U> query)
            throws UnifyException;

    /**
     * Subtracts supplied value to field for all records that match criteria.
     * 
     * @param fieldClass
     *                   the value type
     * @param fieldName
     *                   the field name
     * @param val
     *                   the value to add
     * @param query
     *                   the criteria
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Number, U extends Entity> int subtract(Class<T> fieldClass, String fieldName, T val, Query<U> query)
            throws UnifyException;

    /**
     * Multiplies supplied value to field for all records that match criteria.
     * 
     * @param fieldClass
     *                   the value type
     * @param fieldName
     *                   the field name
     * @param val
     *                   the value to add
     * @param query
     *                   the criteria
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Number, U extends Entity> int multiply(Class<T> fieldClass, String fieldName, T val, Query<U> query)
            throws UnifyException;

    /**
     * Divides supplied value to field for all records that match criteria.
     * 
     * @param fieldClass
     *                   the value type
     * @param fieldName
     *                   the field name
     * @param val
     *                   the value to add
     * @param query
     *                   the criteria
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Number, U extends Entity> int divide(Class<T> fieldClass, String fieldName, T val, Query<U> query)
            throws UnifyException;

    /**
     * Gets minimum value of selected field for record fetched by query.
     * 
     * @param fieldClass
     *            the value class
     * @param fieldName
     *            the value field name
     * @param query
     *            the query
     * @return the minimum value
     * @throws UnifyException
     *             if no field or multiple fields are selected in criteria. If
     *             multiple or no record match criteria. If an error occurs
     */
    <T extends Number, U extends Entity> T min(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException;

    /**
     * Gets maximum value of selected field for record fetched by query.
     * 
     * @param fieldClass
     *            the value class
     * @param fieldName
     *            the value field name
     * @param query
     *            the query
     * @return the maximum value
     * @throws UnifyException
     *             if no field or multiple fields are selected in criteria. If
     *             multiple or no record match criteria. If an error occurs
     */
    <T extends Number, U extends Entity> T max(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException;

    /**
     * Deletes a record
     * 
     * @param inst
     *             the record to delete
     * @throws UnifyException
     *                        if an error occurs
     */
    int delete(Entity inst) throws UnifyException;

    /**
     * Deletes a record and applies a policy if supplied.
     * 
     * @param ctx
     *            the entity action context
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult delete(EntityActionContext ctx) throws UnifyException;

    /**
     * Deletes a persistent record by ID.
     * 
     * @param clazz
     *              the record type
     * @param id
     *              the record ID
     * @return the record found
     * @throws UnifyException
     *                        if record with ID is not found. If an error occurs
     */
    <T extends Entity> int delete(Class<T> clazz, Object id) throws UnifyException;

    /**
     * Deletes all records with fields that match criteria.
     * 
     * @param query
     *              the query object
     * @return the number of record deleted
     * @throws UnifyException
     *                        if an error occurs
     */
    int deleteAll(Query<? extends Entity> query) throws UnifyException;

    /**
     * Deletes a record by ID.
     * 
     * @param record
     *               the record to delete
     * @return the number of record deleted. Always 1.
     * @throws UnifyException
     *                        if an error occurs during modify
     */
    int deleteById(Entity record) throws UnifyException;

    /**
     * Deletes a record by ID and version number.
     * 
     * @param record
     *               the record to delete
     * @return the number of record deleted. Always 1.
     * @throws UnifyException
     *                        if an error occurs during modify
     */
    int deleteByIdVersion(Entity record) throws UnifyException;

    /**
     * Deletes a record by ID.
     * 
     * @param wrappedInst
     *               the wrapped record to delete
     * @return the number of record deleted. Always 1.
     * @throws UnifyException
     *                        if an error occurs during modify
     */
    int deleteById(EntityWrapper wrappedInst) throws UnifyException;

    /**
     * Deletes a record by ID and version number.
     * 
     * @param wrappedInst
     *               the wrapped record to delete
     * @return the number of record deleted. Always 1.
     * @throws UnifyException
     *                        if an error occurs during modify
     */
    int deleteByIdVersion(EntityWrapper wrappedInst) throws UnifyException;

    /**
     * Executes an aggregate function that match specified query.
     * 
     * @param aggregateFunction the aggregate function
     * @param query             the query to use
     * @return the aggregation
     * @throws UnifyException If aggregate function field is unknown for entity. If
     *                        aggregate function field is not numeric. If an error
     *                        occurs
     */
    Aggregation aggregate(AggregateFunction aggregateFunction, Query<? extends Entity> query) throws UnifyException;

    /**
     * Executes a list of aggregate functions that match specified query.
     * 
     * @param aggregateFunction the aggregate function
     * @param query             the query to use
     * @return the aggregation
     * @throws UnifyException If aggregate function field is unknown for entity. If
     *                        aggregate function field is not numeric. If an error
     *                        occurs
     */
    List<Aggregation> aggregate(List<AggregateFunction> aggregateFunction, Query<? extends Entity> query)
            throws UnifyException;

    /**
     * Executes an group aggregate function that match specified query.
     * 
     * @param aggregateFunction the aggregate function
     * @param query             the query to use
     * @param groupingFunction  the grouping function
     * @return the aggregation
     * @throws UnifyException If aggregate function field is unknown for entity. If
     *                        aggregate function field is not numeric. If an error
     *                        occurs
     */
    List<GroupingAggregation> aggregate(AggregateFunction aggregateFunction, Query<? extends Entity> query,
            GroupingFunction groupingFunction) throws UnifyException;

    /**
     * Executes a list of group aggregate functions that match specified query.
     * 
     * @param aggregateFunction the aggregate function
     * @param query             the query to use
     * @param groupingFunction  the grouping function
     * @return the aggregation
     * @throws UnifyException If aggregate function field is unknown for entity. If
     *                        aggregate function field is not numeric. If an error
     *                        occurs
     */
    List<GroupingAggregation> aggregate(List<AggregateFunction> aggregateFunction, Query<? extends Entity> query,
            GroupingFunction groupingFunction) throws UnifyException;

    /**
     * Executes an group aggregate function that match specified query.
     * 
     * @param aggregateFunction the aggregate function
     * @param query             the query to use
     * @param groupingFunction  the grouping function
     * @return the aggregation
     * @throws UnifyException If aggregate function field is unknown for entity. If
     *                        aggregate function field is not numeric. If an error
     *                        occurs
     */
    List<GroupingAggregation> aggregate(AggregateFunction aggregateFunction, Query<? extends Entity> query,
            List<GroupingFunction> groupingFunction) throws UnifyException;

    /**
     * Executes a list of group aggregate functions that match specified query.
     * 
     * @param aggregateFunction the aggregate function
     * @param query             the query to use
     * @param groupingFunction  the grouping function
     * @return the aggregation
     * @throws UnifyException If aggregate function field is unknown for entity. If
     *                        aggregate function field is not numeric. If an error
     *                        occurs
     */
    List<GroupingAggregation> aggregate(List<AggregateFunction> aggregateFunction, Query<? extends Entity> query,
            List<GroupingFunction> groupingFunction) throws UnifyException;

    /**
     * Populates list-only properties of a record
     * 
     * @param record
     *               the record to populate
     * @throws UnifyException
     *                        if an error occurs
     */
    void populateListOnly(Entity record) throws UnifyException;

    /**
     * Counts records by query.
     * 
     * @param query
     *              the search query
     * @return the number of record
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> int countAll(Query<T> query) throws UnifyException;

    /**
     * Finds constraining record that may prevent supplied record from being
     * successfully created.
     * 
     * @param record
     * @return constraining record if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends Entity> T findConstraint(T record) throws UnifyException;

    /**
     * Gets the assigned list for particular field for specified entity using a base
     * field.
     * 
     * @param entityClass
     *                    the entity class
     * @param valueClass
     *                    the value class
     * @param baseField
     *                    the base field
     * @param baseId
     *                    the base id
     * @param valueField
     *                    the value field
     * @return the value list
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U extends Entity> List<T> getAssignedList(Class<U> entityClass, Class<T> valueClass, String baseField,
            Object baseId, String valueField) throws UnifyException;

    /**
     * Gets the assigned set for particular field for specified entity using a base
     * field.
     * 
     * @param entityClass
     *                    the entity class
     * @param valueClass
     *                    the value class
     * @param baseField
     *                    the base field
     * @param baseId
     *                    the base id
     * @param valueField
     *                    the value field
     * @return the value list
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U extends Entity> Set<T> getAssignedSet(Class<U> entityClass, Class<T> valueClass, String baseField,
            Object baseId, String valueField) throws UnifyException;

    /**
     * Updates the assignment list for particular field for specified entity using
     * supplied base field and list.
     * 
     * @param sweepingCommitPolicy
     *                               sweeping commit policy
     * @param assignmentCommitPolicy
     *                               assignment commit policy
     * @param entityClass
     *                               the entity class
     * @param baseField
     *                               the base field
     * @param baseId
     *                               the base Id
     * @param valueField
     *                               the value field
     * @param valueList
     *                               the value list to set
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U extends Entity> int updateAssignedList(SweepingCommitPolicy sweepingCommitPolicy,
            String assignmentCommitPolicy, Class<U> entityClass, String baseField, Object baseId, String valueField,
            List<T> valueList) throws UnifyException;

    /**
     * Updates the assignment list for particular field for specified entity using
     * supplied base field and list.
     * 
     * @param sweepingCommitPolicy
     *                               sweeping commit policy
     * @param assignmentCommitPolicy
     *                               assignment commit policy
     * @param entityClass
     *                               the entity class
     * @param baseField
     *                               the base field
     * @param baseId
     *                               the base Id
     * @param instList
     *                               the inst list to set
     * @param fixedAssignment
     *                               indicates the assignment is fixed
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U extends Entity> int updateAssignedList(SweepingCommitPolicy sweepingCommitPolicy,
            String assignmentCommitPolicy, Class<U> entityClass, String baseField, Object baseId, List<T> instList,
            boolean fixedAssignment) throws UnifyException;

    /**
     * Updates the entry list for particular field for specified entity using
     * supplied base field and list.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param entryUpdatePolicy
     *                             entry update policy
     * @param entityClass
     *                             the entity class
     * @param baseField
     *                             the base field
     * @param baseId
     *                             the base Id
     * @param instList
     *                             the inst list to set
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U extends Entity> int updateEntryList(SweepingCommitPolicy sweepingCommitPolicy, String entryUpdatePolicy,
            Class<U> entityClass, String baseField, Object baseId, List<T> instList) throws UnifyException;

    /**
     * Gets an entity value.
     * 
     * @param valueType
     *                       the value type
     * @param valueFieldName
     *                       the value field name
     * @param query
     *                       the query
     * @return the value
     * @throws UnifyException
     *                        if an error occurs
     */
    <T> T value(Class<T> valueType, String valueFieldName, Query<? extends Entity> query) throws UnifyException;

    /**
     * Gets an entity value (optional).
     * 
     * @param valueType
     *                       the value type
     * @param valueFieldName
     *                       the value field name
     * @param query
     *                       the query
     * @return the value
     * @throws UnifyException
     *                        if an error occurs
     */
    <T> Optional<T> valueOptional(Class<T> valueType, String valueFieldName, Query<? extends Entity> query)
            throws UnifyException;

    /**
     * Gets an entity value list.
     * 
     * @param valueType
     *                       the value type
     * @param valueFieldName
     *                       the value field name
     * @param query
     *                       the query
     * @return the value
     * @throws UnifyException
     *                        if an error occurs
     */
    <T> List<T> valueList(Class<T> valueType, String valueFieldName, Query<? extends Entity> query)
            throws UnifyException;

    /**
     * Gets a set of values of a particular field for all record that match supplied
     * criteria. The field, which can be a list-only field, must be selected in the
     * criteria object.
     * 
     * @param fieldClass
     *                   the field type
     * @param fieldName
     *                   the field name
     * @param query
     *                   the query with field selected
     * @throws UnifyException
     *                        if no field or multiple fields are selected in
     *                        criteria. If an error occurs
     */
    <T, U extends Entity> Set<T> valueSet(Class<T> fieldClass, String fieldName, Query<U> query) throws UnifyException;

    /**
     * Gets a key value map
     * 
     * @param keyClass
     *                   the key type
     * @param keyName
     *                   the key field name
     * @param valueClass
     *                   the value type
     * @param valueName
     *                   the value field name
     * @param query
     *                   the query
     * @return a map of key/values
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U, V extends Entity> Map<T, U> valueMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException;

    /**
     * Gets a key value list map
     * 
     * @param keyClass
     *                   the key type
     * @param keyName
     *                   the key field name
     * @param valueClass
     *                   the value type
     * @param valueName
     *                   the value field name
     * @param query
     *                   the query
     * @return a map of key/ list values
     * @throws UnifyException
     *                        if an error occurs
     */
    <T, U, V extends Entity> Map<T, List<U>> valueListMap(Class<T> keyClass, String keyName, Class<U> valueClass,
            String valueName, Query<V> query) throws UnifyException;

    /**
     * Retrieves first record matched by criteria. List-only properties of returned object are not
     * populated. Child and child list properties are populated.
     * 
     * @param query
     *            the query
     * @return the record found otherwise null
     * @throws UnifyException
     *             if multiple records are found. If an error occurs
     */
    <T extends Entity> T findFirst(Query<T> query) throws UnifyException;

    /**
     * Retrieves last record matched by criteria. List-only properties of returned object are not
     * populated. Child and child list properties are populated.
     * 
     * @param query
     *            the query
     * @return the record found otherwise null
     * @throws UnifyException
     *             if multiple records are found. If an error occurs
     */
    <T extends Entity> T findLast(Query<T> query) throws UnifyException;

    /**
     * Retrieves first record matched by criteria. List-only properties of returned object are not
     * populated. Child and child list properties are not populated.
     * 
     * @param query
     *            the query
     * @return the record found otherwise null
     * @throws UnifyException
     *             if multiple records are found. If an error occurs
     */
    <T extends Entity> T findLeanFirst(Query<T> query) throws UnifyException;

    /**
     * Retrieves last record matched by criteria. List-only properties of returned object are not
     * populated. Child and child list properties are not populated.
     * 
     * @param query
     *            the query
     * @return the record found otherwise null
     * @throws UnifyException
     *             if multiple records are found. If an error occurs
     */
    <T extends Entity> T findLeanLast(Query<T> query) throws UnifyException;

    /**
     * Retrieves first record matched by criteria from associated view. List-only properties of
     * returned object are populated. Child and child list properties are populated.
     * 
     * @param query
     *            the query
     * @return the record found otherwise null
     * @throws UnifyException
     *             if multiple records are found. If an error occurs
     */
    <T extends Entity> T listFirst(Query<T> query) throws UnifyException;

    /**
     * Retrieves last record matched by criteria from associated view. List-only properties of
     * returned object are populated. Child and child list properties are populated.
     * 
     * @param query
     *            the query
     * @return the record found otherwise null
     * @throws UnifyException
     *             if multiple records are found. If an error occurs
     */
    <T extends Entity> T listLast(Query<T> query) throws UnifyException;

    /**
     * Retrieves first record matched by criteria from associated view. List-only properties of
     * returned object are populated. Child and child list properties are not populated.
     * 
     * @param query
     *            the query
     * @return the record found otherwise null
     * @throws UnifyException
     *             if multiple records are found. If an error occurs
     */
    <T extends Entity> T listLeanFirst(Query<T> query) throws UnifyException;

    /**
     * Retrieves last record matched by criteria from associated view. List-only properties of
     * returned object are populated. Child and child list properties are not populated.
     * 
     * @param query
     *            the query
     * @return the record found otherwise null
     * @throws UnifyException
     *             if multiple records are found. If an error occurs
     */
    <T extends Entity> T listLeanLast(Query<T> query) throws UnifyException;

    /**
     * Gets the datasource that the entity belongs to.
     * 
     * @param entityLongName
     *                       the entity long name
     * @return the datasource name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getEntityDataSourceName(String entityLongName) throws UnifyException;

    /**
     * Gets the datasource that the entity class belongs to.
     * 
     * @param entityClass
     *                    the entity class
     * @return the datasource name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getEntityDataSourceName(Class<? extends Entity> entityClass) throws UnifyException;

    /**
     * Commits transactions.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void commitTransactions() throws UnifyException;
}
