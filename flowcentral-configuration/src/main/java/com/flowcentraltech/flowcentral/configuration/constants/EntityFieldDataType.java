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
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Entity field data type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_ENTITYFIELDDATATYPE")
@StaticList(name = "entityfielddatatypelist", description = "$m{staticlist.entityfielddatatypelist}")
public enum EntityFieldDataType implements EnumConst {

    CHAR(
            "CH",
            DataType.CHAR,
            false,
            true,
            true,
            true,
            true),
    BOOLEAN(
            "BL",
            DataType.BOOLEAN,
            false,
            true,
            true,
            true,
            true),
    SHORT(
            "SH",
            DataType.SHORT,
            true,
            true,
            true,
            true,
            true),
    INTEGER(
            "IN",
            DataType.INTEGER,
            true,
            true,
            true,
            true,
            true),
    LONG(
            "LN",
            DataType.LONG,
            true,
            true,
            true,
            true,
            true),
    TENANT_ID(
            "TD",
            DataType.LONG,
            true,
            false,
            false,
            true,
            true),
    MAPPED(
            "MD",
            DataType.LONG,
            true,
            false,
            false,
            true,
            false),
    FLOAT(
            "FL",
            DataType.FLOAT,
            true,
            true,
            true,
            true,
            true),
    DOUBLE(
            "DB",
            DataType.DOUBLE,
            true,
            true,
            true,
            true,
            true),
    DECIMAL(
            "DC",
            DataType.DECIMAL,
            true,
            true,
            true,
            true,
            true),
    DATE(
            "DT",
            DataType.DATE,
            false,
            true,
            true,
            true,
            true),
    TIMESTAMP_UTC(
            "TU",
            DataType.TIMESTAMP_UTC,
            false,
            true,
            true,
            true,
            true),
    TIMESTAMP(
            "TS",
            DataType.TIMESTAMP,
            false,
            true,
            true,
            true,
            true),
    CLOB(
            "CT",
            DataType.CLOB,
            false,
            false,
            true,
            true,
            true),
    BLOB(
            "BT",
            DataType.BLOB,
            false,
            false,
            true,
            false,
            false),
    STRING(
            "ST",
            DataType.STRING,
            false,
            true,
            true,
            true,
            true),
    ENUM(
            "EN",
            DataType.STRING,
            false,
            true,
            true,
            true,
            true),
    ENUM_REF(
            "ER",
            DataType.STRING,
            false,
            true,
            true,
            true,
            true),
    REF(
            "RF",
            DataType.LONG,
            false,
            true,
            true,
            true,
            true),
    REF_UNLINKABLE(
            "UR",
            DataType.LONG,
            false,
            false,
            true,
            true,
            true),
    REF_FILEUPLOAD(
            "CU",
            null,
            false,
            false,
            true,
            true,
            false),
    FOSTER_PARENT_ID(
            "FI",
            DataType.LONG,
            false,
            false,
            false,
            false,
            false),
    FOSTER_PARENT_TYPE(
            "FT",
            DataType.STRING,
            false,
            false,
            false,
            false,
            false),
    CATEGORY_COLUMN(
            "CC",
            DataType.STRING,
            false,
            false,
            false,
            false,
            false),
    SCRATCH(
            "SC",
            DataType.STRING,
            false,
            false,
            false,
            false,
            true),
    LIST_ONLY(
            "LO",
            null,
            false,
            true,
            true,
            true,
            false),
    CHILD(
            "CD",
            null,
            false,
            false,
            false,
            false,
            false),
    CHILD_LIST(
            "CL",
            null,
            false,
            false,
            false,
            false,
            false);

    private final String code;

    private final DataType dataType;

    private final boolean number;

    private final boolean tableView;

    private final boolean formView;

    private final boolean supportFilter;

    private final boolean supportSetValue;

    private EntityFieldDataType(String code, DataType dataType, boolean number, boolean tableView, boolean formView,
            boolean supportFilter, boolean supportSetValue) {
        this.code = code;
        this.dataType = dataType;
        this.number = number;
        this.tableView = tableView;
        this.formView = formView;
        this.supportFilter = supportFilter;
        this.supportSetValue = supportSetValue;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return STRING.code;
    }

    public DataType dataType() {
        return dataType;
    }

    public boolean isTableViewable() {
        return tableView;
    }

    public boolean isFormViewable() {
        return formView;
    }

    public boolean isSupportFilter() {
        return supportFilter;
    }

    public boolean isSupportSetValue() {
        return supportSetValue;
    }

    public boolean isSupportLingual() {
        return STRING.equals(this) || DATE.equals(this) || TIMESTAMP.equals(this) || TIMESTAMP_UTC.equals(this);
    }

    public boolean isEnumDataType() {
        return EntityFieldDataType.ENUM.equals(this) || EntityFieldDataType.ENUM_REF.equals(this);
    }

    public boolean isRefDataType() {
        return EntityFieldDataType.REF.equals(this) || EntityFieldDataType.REF_UNLINKABLE.equals(this)
                || EntityFieldDataType.ENUM_REF.equals(this) || EntityFieldDataType.CHILD.equals(this)
                || EntityFieldDataType.CHILD_LIST.equals(this) || EntityFieldDataType.REF_FILEUPLOAD.equals(this);
    }

    public boolean isPrimitive() {
        return dataType != null;
    }

    public boolean isDate() {
        return DATE.equals(this);
    }

    public boolean isTimestamp() {
        return TIMESTAMP.equals(this) || TIMESTAMP_UTC.equals(this);
    }

    public boolean isBoolean() {
        return BOOLEAN.equals(this);
    }

    public boolean isNumber() {
        return number;
    }

    public boolean isDecimal() {
        return DECIMAL.equals(this) || DOUBLE.equals(this) || FLOAT.equals(this);
    }

    public boolean isString() {
        return STRING.equals(this);
    }

    public boolean isTenantId() {
        return TENANT_ID.equals(this);
    }

    public boolean isMapped() {
        return MAPPED.equals(this);
    }

    public boolean isScratch() {
        return SCRATCH.equals(this);
    }

    public boolean isEntityRef() {
        return EntityFieldDataType.REF.equals(this) || EntityFieldDataType.REF_UNLINKABLE.equals(this)
                || EntityFieldDataType.REF_FILEUPLOAD.equals(this) || EntityFieldDataType.CHILD.equals(this)
                || EntityFieldDataType.CHILD_LIST.equals(this);
    }

    public boolean isForeignKey() {
        return EntityFieldDataType.REF.equals(this) || EntityFieldDataType.REF_UNLINKABLE.equals(this)
                || EntityFieldDataType.ENUM_REF.equals(this);
    }

    public boolean isListOnly() {
        return EntityFieldDataType.LIST_ONLY.equals(this);
    }

    public boolean isChild() {
        return EntityFieldDataType.CHILD.equals(this);
    }

    public boolean isChildList() {
        return EntityFieldDataType.CHILD_LIST.equals(this);
    }

    public boolean isRefFileUpload() {
        return EntityFieldDataType.REF_FILEUPLOAD.equals(this);
    }

    public static EntityFieldDataType fromCode(String code) {
        return EnumUtils.fromCode(EntityFieldDataType.class, code);
    }

    public static EntityFieldDataType fromName(String name) {
        return EnumUtils.fromName(EntityFieldDataType.class, name);
    }

}
