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
package com.flowcentraltech.flowcentral.common.constants;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;

/**
 * Formatter options.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormatterOptions {

    public static final FormatterOptions DEFAULT = new FormatterOptions("!integerformat useGrouping:true",
            "!decimalformat precision:20 scale:2 useGrouping:true", "!fixeddatetimeformat pattern:$s{yyyy-MM-dd}",
            "!fixeddatetimeformat pattern:$s{yyyy-MM-dd HH:mm:ss}");

    private final String integerFormatter;

    private final String decimalFormatter;

    private final String dateFormatter;

    private final String timestampFormatter;

    public FormatterOptions(String integerFormatter, String decimalFormatter, String dateFormatter,
            String timestampFormatter) {
        this.integerFormatter = integerFormatter;
        this.decimalFormatter = decimalFormatter;
        this.dateFormatter = dateFormatter;
        this.timestampFormatter = timestampFormatter;
    }

    public String getIntegerFormatter() {
        return integerFormatter;
    }

    public String getDecimalFormatter() {
        return decimalFormatter;
    }

    public String getDateFormatter() {
        return dateFormatter;
    }

    public String getTimestampFormatter() {
        return timestampFormatter;
    }

    public String getFormatter(EntityFieldDataType dataType) {
        if (dataType.isDecimal()) {
            return decimalFormatter;
        } else if (dataType.isInteger()) {
            return integerFormatter;
        } else if (dataType.isTimestamp()) {
            return timestampFormatter;
        } else if (dataType.isDate()) {
            return dateFormatter;
        }

        return null;
    }
}