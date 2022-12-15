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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Search condition type enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_SEARCHCONDTYPE")
@StaticList(name = "searchconditiontypelist", description = "$m{staticlist.searchconditiontypelist}")
public enum SearchConditionType implements EnumConst {

    EQUALS(
            FilterConditionType.EQUALS.code(),
            true,
            true,
            true),
    LESS_THAN(
            FilterConditionType.LESS_THAN.code(),
            false,
            true,
            false),
    LESS_OR_EQUAL(
            FilterConditionType.LESS_OR_EQUAL.code(),
            false,
            true,
            false),
    GREATER_THAN(
            FilterConditionType.GREATER_THAN.code(),
            false,
            true,
            false),
    GREATER_OR_EQUAL(
            FilterConditionType.GREATER_OR_EQUAL.code(),
            false,
            true,
            false),
    BEGINS_WITH(
            FilterConditionType.BEGINS_WITH.code(),
            false,
            false,
            true),
    ENDS_WITH(
            FilterConditionType.ENDS_WITH.code(),
            false,
            false,
            true),
    LIKE(
            FilterConditionType.LIKE.code(),
            false,
            false,
            true),
    ILIKE(
            FilterConditionType.ILIKE.code(),
            false,
            false,
            true),
    NOT_EQUALS(
            FilterConditionType.NOT_EQUALS.code(),
            true,
            true,
            true),
    NOT_LIKE(
            FilterConditionType.NOT_LIKE.code(),
            false,
            false,
            true),
    NOT_BEGIN_WITH(
            FilterConditionType.NOT_BEGIN_WITH.code(),
            false,
            false,
            true),
    NOT_END_WITH(
            FilterConditionType.NOT_END_WITH.code(),
            false,
            false,
            true),
    IS_NULL(
            FilterConditionType.IS_NULL.code(),
            true,
            true,
            true),
    IS_NOT_NULL(
            FilterConditionType.IS_NOT_NULL.code(),
            true,
            true,
            true);

    private final String code;

    private final boolean supportsBoolean;

    private final boolean supportsNumber;

    private final boolean supportsString;

    private SearchConditionType(String code, boolean supportsBoolean, boolean supportsNumber, boolean supportsString) {
        this.code = code;
        this.supportsBoolean = supportsBoolean;
        this.supportsNumber = supportsNumber;
        this.supportsString = supportsString;
    }

    @Override
    public String code() {
        return this.code;
    }

    public FilterConditionType filterType() {
        return FilterConditionType.fromCode(code);
    }

    @Override
    public String defaultCode() {
        return EQUALS.code;
    }

    public boolean supportsBoolean() {
        return supportsBoolean;
    }

    public boolean supportsNumber() {
        return supportsNumber;
    }

    public boolean supportsString() {
        return supportsString;
    }

    public static SearchConditionType fromCode(String code) {
        return EnumUtils.fromCode(SearchConditionType.class, code);
    }

    public static SearchConditionType fromName(String name) {
        return EnumUtils.fromName(SearchConditionType.class, name);
    }

}
