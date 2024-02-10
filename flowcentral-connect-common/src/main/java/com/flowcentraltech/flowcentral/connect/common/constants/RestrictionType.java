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
package com.flowcentraltech.flowcentral.connect.common.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Restriction type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum RestrictionType {

    EQUALS("EQ", false, false, false),
    IEQUALS("IEQ", false, false, false),
    EQUALS_LINGUAL("EQG", false, false, true),
    NOT_EQUALS("NEQ", false, false, false),
    NOT_EQUALS_LINGUAL("NEL", false, false, true),
    INOT_EQUALS("INQ", false, false, false),
    LESS_THAN("LT", false, false, false),
    LESS_THAN_LINGUAL("LTL", false, false, true),
    LESS_OR_EQUAL("LTE", false, false, false),
    LESS_OR_EQUAL_LINGUAL("LTL", false, false, true),
    GREATER_THAN("GT", false, false, false),
    GREATER_THAN_LINGUAL("GTL", false, false, true),
    GREATER_OR_EQUAL("GTE", false, false, false),
    GREATER_OR_EQUAL_LINGUAL("GTL", false, false, true),
    BETWEEN("BT", false, false, false),
    NOT_BETWEEN("NBT", false, false, false),
    AMONGST("IN", false, false, false),
    NOT_AMONGST("NIN", false, false, false),
    LIKE("LK", false, false, false),
    ILIKE("ILK", false, false, false),
    NOT_LIKE("NLK", false, false, false),
    BEGINS_WITH("BW", false, false, false),
    IBEGINS_WITH("IBW", false, false, false),
    NOT_BEGIN_WITH("NBW", false, false, false),
    ENDS_WITH("EW", false, false, false),
    IENDS_WITH("IEW", false, false, false),
    NOT_END_WITH("NEW", false, false, false),
    IS_NULL("NL", false, false, false),
    IS_NOT_NULL("NNL", false, false, false),

    EQUALS_FIELD("EQF", true, false, false),
    NOT_EQUALS_FIELD("NEQF", true, false, false),
    LESS_THAN_FIELD("LTF", true, false, false),
    LESS_OR_EQUAL_FIELD("LTEF", true, false, false),
    GREATER_THAN_FIELD("GTF", true, false, false),
    GREATER_OR_EQUAL_FIELD("GTEF", true, false, false),
    BETWEEN_FIELD("BTF", true, false, false),
    NOT_BETWEEN_FIELD("NBTF", true, false, false),
    LIKE_FIELD("LKF", true, false, false),
    ILIKE_FIELD("ILKF", true, false, false),
    NOT_LIKE_FIELD("NLKF", true, false, false),
    BEGINS_WITH_FIELD("BWF", true, false, false),
    IBEGINS_WITH_FIELD("IBWF", true, false, false),
    NOT_BEGIN_WITH_FIELD("NBWF", true, false, false),
    ENDS_WITH_FIELD("EWF", true, false, false),
    IENDS_WITH_FIELD("IEWF", true, false, false),
    NOT_END_WITH_FIELD("NEWF", true, false, false),

    EQUALS_PARAM("EQP", false, true, false),
    NOT_EQUALS_PARAM("NEQP", false, true, false),
    LESS_THAN_PARAM("LTP", false, true, false),
    LESS_OR_EQUAL_PARAM("LTEP", false, true, false),
    GREATER_THAN_PARAM("GTP", false, true, false),
    GREATER_OR_EQUAL_PARAM("GTEP", false, true, false),
    BETWEEN_PARAM("BTP", false, true, false),
    NOT_BETWEEN_PARAM("NBTP", false, true, false),
    LIKE_PARAM("LKP", false, true, false),
    ILIKE_PARAM("ILKP", false, true, false),
    NOT_LIKE_PARAM("NLKP", false, true, false),
    BEGINS_WITH_PARAM("BWP", false, true, false),
    IBEGINS_WITH_PARAM("IBWP", false, true, false),
    NOT_BEGIN_WITH_PARAM("NBWP", false, true, false),
    ENDS_WITH_PARAM("EWP", false, true, false),
    IENDS_WITH_PARAM("IEWP", false, true, false),
    NOT_END_WITH_PARAM("NEWP", false, true, false),

    EQUALS_COLLECTION("EQC", false, false, false),
    NOT_EQUALS_COLLECTION("NEQC", false, false, false),
    LESS_THAN_COLLECTION("LTC", false, false, false),
    LESS_OR_EQUAL_COLLECTION("LTEC", false, false, false),
    GREATER_THAN_COLLECTION("GTC", false, false, false),
    GREATER_OR_EQUAL_COLLECTION("GTEC", false, false, false),

    AND("AND", false, false, false),
    OR("OR", false, false, false);

    private final String code;

    private final boolean fieldVal;

    private final boolean parameterVal;

    private final boolean lingual;

    private static final Map<String, RestrictionType> typesByCode;
    static {
        Map<String, RestrictionType> map = new HashMap<String, RestrictionType>();
        for (RestrictionType type: RestrictionType.values()) {
            map.put(type.code, type);
        }
        
        typesByCode = Collections.unmodifiableMap(map);
    }
    
    private RestrictionType(String code, boolean fieldVal, boolean parameterVal, boolean lingual) {
        this.code = code;
        this.fieldVal = fieldVal;
        this.parameterVal = parameterVal;
        this.lingual = lingual;
    }

    public String code() {
        return code;
    }

    public boolean isFieldVal() {
        return fieldVal;
    }

    public boolean isParameterVal() {
        return parameterVal;
    }

    public boolean isLingual() {
        return lingual;
    }

    public boolean isAmongst() {
        return AMONGST.equals(this) || NOT_AMONGST.equals(this);
    }

    public boolean isCompound() {
        return AND.equals(this) || OR.equals(this);
    }

    public boolean isRange() {
        return BETWEEN.equals(this) || NOT_BETWEEN.equals(this);
    }

    public boolean isZeroParams() {
        return IS_NULL.equals(this) || IS_NOT_NULL.equals(this);
    }

    public static RestrictionType fromCode(String code) {
        return code != null? typesByCode.get(code) : null;
    }

    public static RestrictionType fromName(String name) {
        return name != null? RestrictionType.valueOf(name.toUpperCase()) : null;
    }
}
