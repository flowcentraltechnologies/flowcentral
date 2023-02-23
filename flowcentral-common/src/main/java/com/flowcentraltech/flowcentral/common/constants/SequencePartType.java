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

package com.flowcentraltech.flowcentral.common.constants;

/**
 * Sequence part type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum SequencePartType {

    TEXT(
            "",
            ""),
    VALUESTORE_GENERATOR(
            "{v:",
            ""),
    SYSTEM_PARAMETER(
            "{p:",
            ""),
    SEQUENCE_NUMBER(
            "{n:",
            ""),
    SEQUENCE_NUMBER_BY_DATE(
            "{N:",
            ""),
    LONG_YEAR(
            "{yyyy}",
            "____"),
    SHORT_YEAR(
            "{yy}",
            "__"),
    DAY_OF_MONTH(
            "{dd}",
            "_"),
    DAY_OF_YEAR(
            "{ddd}",
            "_");

    private final String track;

    private final String skeleton;

    private SequencePartType(String track, String skeleton) {
        this.track = track;
        this.skeleton = skeleton;
    }

    public String track() {
        return track;
    }

    public String skeleton() {
        return skeleton;
    }

    public boolean isSequenceNumber() {
        return SEQUENCE_NUMBER.equals(this) || SEQUENCE_NUMBER_BY_DATE.equals(this);
    }

    public boolean isSystemParameter() {
        return SYSTEM_PARAMETER.equals(this);
    }

    public boolean isValueStore() {
        return VALUESTORE_GENERATOR.equals(this);
    }

    public boolean isDatePart() {
        return LONG_YEAR.equals(this) || SHORT_YEAR.equals(this) || DAY_OF_MONTH.equals(this)
                || DAY_OF_YEAR.equals(this);
    }

    public boolean isDayPart() {
        return DAY_OF_MONTH.equals(this) || DAY_OF_YEAR.equals(this);
    }

}
