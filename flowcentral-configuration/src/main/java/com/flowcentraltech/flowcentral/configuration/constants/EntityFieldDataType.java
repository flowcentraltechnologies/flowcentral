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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.ConnectFieldDataType;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.HAlignType;
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
            true,
            true,
            false),
    BOOLEAN(
            "BL",
            DataType.BOOLEAN,
            false,
            true,
            true,
            true,
            true,
            true,
            false),
    BOOLEAN_ARRAY(
            "BLA",
            DataType.BOOLEAN,
            false,
            true,
            true,
            false,
            false,
            true,
            true),
    SHORT(
            "SH",
            DataType.SHORT,
            true,
            true,
            true,
            true,
            true,
            true,
            false),
    SHORT_ARRAY(
            "SHA",
            DataType.SHORT,
            true,
            true,
            true,
            false,
            false,
            true,
            true),
    INTEGER(
            "IN",
            DataType.INTEGER,
            true,
            true,
            true,
            true,
            true,
            true,
            false),
    INTEGER_ARRAY(
            "INA",
            DataType.INTEGER,
            true,
            true,
            true,
            false,
            false,
            true,
            true),
    LONG(
            "LN",
            DataType.LONG,
            true,
            true,
            true,
            true,
            true,
            true,
            false),
    LONG_ARRAY(
            "LNA",
            DataType.LONG,
            true,
            true,
            true,
            false,
            false,
            true,
            true),
    TENANT_ID(
            "TD",
            DataType.LONG,
            true,
            false,
            false,
            true,
            true,
            false,
            false),
    MAPPED(
            "MD",
            DataType.LONG,
            true,
            false,
            false,
            true,
            false,
            false,
            false),
    FLOAT(
            "FL",
            DataType.FLOAT,
            true,
            true,
            true,
            true,
            true,
            true,
            false),
    FLOAT_ARRAY(
            "FLA",
            DataType.FLOAT,
            true,
            true,
            true,
            false,
            false,
            true,
            true),
   DOUBLE(
            "DB",
            DataType.DOUBLE,
            true,
            true,
            true,
            true,
            true,
            true,
            false),
   DOUBLE_ARRAY(
           "DBA",
           DataType.DOUBLE,
           true,
           true,
           true,
           false,
           false,
           true,
           true),
    DECIMAL(
            "DC",
            DataType.DECIMAL,
            true,
            true,
            true,
            true,
            true,
            true,
            false),
    DECIMAL_ARRAY(
            "DCA",
            DataType.DECIMAL,
            true,
            true,
            true,
            false,
            false,
            true,
            true),
    DATE(
            "DT",
            DataType.DATE,
            false,
            true,
            true,
            true,
            true,
            true,
            false),
    DATE_ARRAY(
            "DTA",
            DataType.DATE,
            false,
            true,
            true,
            false,
            false,
            true,
            true),
    TIMESTAMP_UTC(
            "TU",
            DataType.TIMESTAMP_UTC,
            false,
            true,
            true,
            true,
            true,
            true,
            false),
    TIMESTAMP(
            "TS",
            DataType.TIMESTAMP,
            false,
            true,
            true,
            true,
            true,
            true,
            false),
    CLOB(
            "CT",
            DataType.CLOB,
            false,
            false,
            true,
            true,
            true,
            true,
            false),
    BLOB(
            "BT",
            DataType.BLOB,
            false,
            false,
            true,
            false,
            false,
            false,
            false),
    STRING(
            "ST",
            DataType.STRING,
            false,
            true,
            true,
            true,
            true,
            true,
            false),
    STRING_ARRAY(
            "STA",
            DataType.STRING,
            false,
            true,
            true,
            false,
            false,
            true,
            true),
    ENUM(
            "EN",
            DataType.STRING,
            false,
            true,
            true,
            true,
            true,
            true,
            false),
    ENUM_REF(
            "ER",
            DataType.STRING,
            false,
            true,
            true,
            true,
            true,
            true,
            false),
    ENUM_DYN(
            "ED",
            DataType.STRING,
            false,
            true,
            true,
            true,
            true,
            true,
            false),
    REF(
            "RF",
            DataType.LONG,
            false,
            true,
            true,
            true,
            true,
            false,
            false),
    REF_UNLINKABLE(
            "UR",
            DataType.LONG,
            false,
            false,
            true,
            true,
            true,
            false,
            false),
    REF_FILEUPLOAD(
            "CU",
            null,
            false,
            false,
            true,
            true,
            false,
            false,
            false),
    FOSTER_PARENT_ID(
            "FI",
            DataType.LONG,
            false,
            false,
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
            true,
            false,
            false),
    LIST_ONLY(
            "LO",
            null,
            false,
            true,
            true,
            true,
            false,
            true,
            false),
    CHILD(
            "CD",
            null,
            false,
            false,
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

    private final boolean reportable;

    private final boolean array;

    private EntityFieldDataType(String code, DataType dataType, boolean number, boolean tableView, boolean formView,
            boolean supportFilter, boolean supportSetValue, boolean reportable, boolean array) {
        this.code = code;
        this.dataType = dataType;
        this.number = number;
        this.tableView = tableView;
        this.formView = formView;
        this.supportFilter = supportFilter;
        this.supportSetValue = supportSetValue;
        this.reportable = reportable;
        this.array = array;
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

    public HAlignType alignType() {
        return dataType != null ? dataType.alignType() : HAlignType.LEFT;
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

    public boolean isReportable() {
        return reportable;
    }

    public boolean isArray() {
        return array;
    }

    public boolean isSupportLingual() {
        return STRING.equals(this) || DATE.equals(this) || TIMESTAMP.equals(this) || TIMESTAMP_UTC.equals(this);
    }

    public boolean isEnumDataType() {
        return ENUM.equals(this) || ENUM_REF.equals(this);
    }

    public boolean isEnumDynamic() {
        return ENUM_DYN.equals(this);
    }

    public boolean isEnumGroup() {
        return ENUM.equals(this) || ENUM_REF.equals(this) || ENUM_DYN.equals(this);
    }

    public boolean isRefDataType() {
        return REF.equals(this) || REF_UNLINKABLE.equals(this) || ENUM_REF.equals(this) || CHILD.equals(this)
                || CHILD_LIST.equals(this) || REF_FILEUPLOAD.equals(this);
    }

    public boolean isUnlinkable() {
        return REF_UNLINKABLE.equals(this);
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

    public boolean isInteger() {
        return LONG.equals(this) || INTEGER.equals(this) || SHORT.equals(this);
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
        return REF.equals(this) || REF_UNLINKABLE.equals(this) || REF_FILEUPLOAD.equals(this) || CHILD.equals(this)
                || CHILD_LIST.equals(this);
    }

    public boolean isNonEnumForeignKey() {
        return REF.equals(this) || REF_UNLINKABLE.equals(this);
    }

    public boolean isForeignKey() {
        return REF.equals(this) || REF_UNLINKABLE.equals(this) || ENUM_REF.equals(this);
    }

    public boolean isListOnly() {
        return LIST_ONLY.equals(this);
    }

    public boolean isChild() {
        return CHILD.equals(this);
    }

    public boolean isChildList() {
        return CHILD_LIST.equals(this);
    }

    public boolean isRefFileUpload() {
        return REF_FILEUPLOAD.equals(this);
    }

    public static EntityFieldDataType fromCode(String code) {
        return EnumUtils.fromCode(EntityFieldDataType.class, code);
    }

    public static EntityFieldDataType fromName(String name) {
        return EnumUtils.fromName(EntityFieldDataType.class, name);
    }

    public static EntityFieldDataType fromName(DataType dataType, boolean array) {
        EntityFieldDataType fieldDataType = EntityFieldDataType.fromName(dataType.name());
        if (array && fieldDataType != null) {
            fieldDataType = fieldDataType.array();
        }
        
        return fieldDataType;
    }

    private EntityFieldDataType array() {
        switch(this) {
            case BOOLEAN:
            case BOOLEAN_ARRAY:
                return BOOLEAN_ARRAY;
            case CATEGORY_COLUMN:
                break;
            case CHAR:
                break;
            case CHILD:
                break;
            case CHILD_LIST:
                break;
            case CLOB:
                break;
            case DATE:
            case DATE_ARRAY:
                return DATE_ARRAY;
            case DECIMAL:
            case DECIMAL_ARRAY:
                return DECIMAL_ARRAY;
            case DOUBLE:
            case DOUBLE_ARRAY:
                return DOUBLE_ARRAY;
            case ENUM:
                break;
            case ENUM_DYN:
                break;
            case ENUM_REF:
                break;
            case FLOAT:
            case FLOAT_ARRAY:
                return FLOAT_ARRAY;
            case FOSTER_PARENT_ID:
            case FOSTER_PARENT_TYPE:
                break;
            case INTEGER:
            case INTEGER_ARRAY:
                return INTEGER_ARRAY;
            case LIST_ONLY:
                break;
            case LONG:
            case LONG_ARRAY:
                return LONG_ARRAY;
            case MAPPED:
            case REF:
            case REF_FILEUPLOAD:
            case REF_UNLINKABLE:
            case SCRATCH:
            case SHORT:
            case SHORT_ARRAY:
                return SHORT_ARRAY;
            case STRING:
            case STRING_ARRAY:
                return STRING_ARRAY;
            case TENANT_ID:
                break;
            case TIMESTAMP:
                break;
            case TIMESTAMP_UTC:
                break;
            default:
                break;
            
        }
        
        return this;
    }

    public static EntityFieldDataType fromInterconnect(ConnectFieldDataType dataType) {
        return dataType != null ? (dataType.isEnum() ? STRING : fromName(dataType.name())) : null;
    }

}
