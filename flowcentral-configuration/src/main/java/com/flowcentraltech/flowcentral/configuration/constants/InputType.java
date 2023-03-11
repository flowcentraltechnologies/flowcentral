/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Input type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_INPUTTYPE")
@StaticList(name = "inputtypelist", description = "$m{staticlist.inputtypelist}")
public enum InputType implements EnumConst {

    CHAR(
            "CH",
            false,
            DataType.CHAR),
    CHAR_ARRAY(
            "CHA",
            true,
            DataType.CHAR),
    BOOLEAN(
            "BL",
            false,
            DataType.BOOLEAN),
    BOOLEAN_ARRAY(
            "BLA",
            true,
            DataType.BOOLEAN),
    SHORT(
            "SH",
            false,
            DataType.SHORT,
            DataType.INTEGER,
            DataType.LONG),
    SHORT_ARRAY(
            "SHA",
            true,
            DataType.SHORT,
            DataType.INTEGER,
            DataType.LONG),
    INTEGER(
            "IN",
            false,
            DataType.INTEGER,
            DataType.LONG),
    INTEGER_ARRAY(
            "INA",
            true,
            DataType.INTEGER,
            DataType.LONG),
    LONG(
            "LN",
            false,
            DataType.LONG),
    LONG_ARRAY(
            "LNA",
            true,
            DataType.LONG),
    FLOAT(
            "FL",
            false,
            DataType.FLOAT,
            DataType.DOUBLE,
            DataType.DECIMAL),
    FLOAT_ARRAY(
            "FLA",
            true,
            DataType.FLOAT,
            DataType.DOUBLE,
            DataType.DECIMAL),
    DOUBLE(
            "DB",
            false,
            DataType.DOUBLE,
            DataType.DECIMAL),
    DOUBLE_ARRAY(
            "DBA",
            true,
            DataType.DOUBLE,
            DataType.DECIMAL),
    DECIMAL(
            "DC",
            false,
            DataType.DECIMAL),
    DECIMAL_ARRAY(
            "DCA",
            true,
            DataType.DECIMAL),
    DATE(
            "DT",
            false,
            DataType.DATE),
    DATE_ARRAY(
            "DTA",
            true,
            DataType.DATE),
    DATETIME(
            "TS",
            false,
            DataType.TIMESTAMP,
            DataType.TIMESTAMP_UTC),
    DATETIME_ARRAY(
            "TSA",
            true,
            DataType.TIMESTAMP,
            DataType.TIMESTAMP_UTC),
    STRING(
            "ST",
            false,
            DataType.STRING,
            DataType.CLOB),
    STRING_ARRAY(
            "STA",
            true,
            DataType.STRING,
            DataType.CLOB),
    FILE(
            "FIL",
            false,
            DataType.BLOB);

    private final String code;

    private final Set<DataType> compatibleTypes;

    private final boolean array;

    private InputType(String code, boolean array, DataType... compatibleTypes) {
        this.code = code;
        this.array = array;
        if (compatibleTypes == null || compatibleTypes.length == 0) {
            this.compatibleTypes = Collections.emptySet();
        } else {
            this.compatibleTypes = new HashSet<DataType>(Arrays.asList(compatibleTypes));
        }
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return STRING.code;
    }

    public boolean array() {
        return array;
    }

    public boolean isCompatible(DataType dataType) {
        if (DataType.BLOB.equals(dataType) || DataType.CLOB.equals(dataType)) {
            return false;
        }

        return compatibleTypes.contains(dataType);
    }

    public static InputType fromCode(String code) {
        return EnumUtils.fromCode(InputType.class, code);
    }

    public static InputType fromName(String name) {
        return EnumUtils.fromName(InputType.class, name);
    }
}
