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

package com.flowcentraltech.flowcentral.common.constants;

/**
 * Sequence part type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum SequencePartType {

    TEXT(
            "",
            "",
            false,
            false),
    VALUESTORE_GENERATOR(
            "{v:",
            "",
            false,
            false),
    SYSTEM_PARAMETER(
            "{p:",
            "",
            false,
            false),
    SEQUENCE_NUMBER(
            "{n:",
            "",
            false,
            false),
    SEQUENCE_NUMBER_BY_DATE(
            "{N:",
            "",
            false,
            false),
    LONG_YEAR(
            "{yyyy}",
            "____",
            true,
            false),
    SHORT_YEAR(
            "{yy}",
            "__",
            true,
            false),
    MONTH(
            "{mm}",
            "_",
            true,
            false),
    MONTH_FIXED(
            "{MM}",
            "_",
            true,
            false),
    DAY_OF_MONTH(
            "{dd}",
            "_",
            true,
            true),
    DAY_OF_YEAR(
            "{ddd}",
            "_",
            true,
            true),
    DAY_OF_MONTH_FIXED(
            "{DD}",
            "_",
            true,
            true),
    DAY_OF_YEAR_FIXED(
            "{DDD}",
            "_",
            true,
            true);

    private final String track;

    private final String skeleton;

    private final boolean datePart;

    private final boolean dayPart;

    private SequencePartType(String track, String skeleton, boolean datePart, boolean dayPart) {
        this.track = track;
        this.skeleton = skeleton;
        this.datePart = datePart;
        this.dayPart = dayPart;
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
        return datePart;
    }

    public boolean isDayPart() {
        return dayPart;
    }

    public boolean isFixed() {
        return MONTH_FIXED.equals(this) || DAY_OF_MONTH_FIXED.equals(this) || DAY_OF_YEAR_FIXED.equals(this);
    }

}
