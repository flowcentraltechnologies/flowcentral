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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Date;

import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * Base query object for base audit entity sub-classes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseAuditEntityQuery<T extends BaseAuditEntity> extends BaseVersionEntityQuery<T> {

    public BaseAuditEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseAuditEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public BaseAuditEntityQuery(Class<T> entityClass, CompoundRestriction restrictions, boolean applyAppQueryLimit) {
        super(entityClass, restrictions, applyAppQueryLimit);
    }

    public final BaseAuditEntityQuery<T> createdBy(String createdBy) {
        return (BaseAuditEntityQuery<T>) addEquals("createdBy", createdBy);
    }

    public final BaseAuditEntityQuery<T> createdOn(Date date) {
        return (BaseAuditEntityQuery<T>) addBetween("createDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public final BaseAuditEntityQuery<T> createdBetween(Date fromDate, Date toDate) {
        return (BaseAuditEntityQuery<T>) addBetween("createDt", CalendarUtils.getMidnightDate(fromDate),
                CalendarUtils.getLastSecondDate(toDate));
    }

    public final BaseAuditEntityQuery<T> createdBefore(Date date) {
        return (BaseAuditEntityQuery<T>) addLessThan("createDt", date);
    }

    public final BaseAuditEntityQuery<T> createdOnBefore(Date date) {
        return (BaseAuditEntityQuery<T>) addLessThanEqual("createDt", date);
    }

    public final BaseAuditEntityQuery<T> createdAfter(Date date) {
        return (BaseAuditEntityQuery<T>) addGreaterThan("createDt", date);
    }

    public final BaseAuditEntityQuery<T> createdOnAfter(Date date) {
        return (BaseAuditEntityQuery<T>) addGreaterThanEqual("createDt", date);
    }

    public final BaseAuditEntityQuery<T> updatedBy(String updatedBy) {
        return (BaseAuditEntityQuery<T>) addEquals("updatedBy", updatedBy);
    }

    public final BaseAuditEntityQuery<T> updatedOn(Date date) {
        return (BaseAuditEntityQuery<T>) addBetween("updateDt", CalendarUtils.getMidnightDate(date),
                CalendarUtils.getLastSecondDate(date));
    }

    public final BaseAuditEntityQuery<T> updatedBetween(Date fromDate, Date toDate) {
        return (BaseAuditEntityQuery<T>) addBetween("updateDt", CalendarUtils.getMidnightDate(fromDate),
                CalendarUtils.getLastSecondDate(toDate));
    }

    public final BaseAuditEntityQuery<T> updatedBefore(Date date) {
        return (BaseAuditEntityQuery<T>) addLessThan("updateDt", date);
    }

    public final BaseAuditEntityQuery<T> updatedOnBefore(Date date) {
        return (BaseAuditEntityQuery<T>) addLessThanEqual("updateDt", date);
    }

    public final BaseAuditEntityQuery<T> updatedAfter(Date date) {
        return (BaseAuditEntityQuery<T>) addGreaterThan("updateDt", date);
    }

    public final BaseAuditEntityQuery<T> updatedOnAfter(Date date) {
        return (BaseAuditEntityQuery<T>) addGreaterThanEqual("updateDt", date);
    }
}
