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
package com.flowcentraltech.flowcentral.common.data;

import java.lang.reflect.Array;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.UnifyComponentContext;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Formatter options.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class FormatterOptions {

    public static final FormatterOptions DEFAULT = new FormatterOptions("!integerformat precision:20 useGrouping:true",
            "!decimalformat precision:20 scale:2 useGrouping:true", "!fixeddatetimeformat pattern:$s{yyyy-MM-dd}",
            "!fixeddatetimeformat pattern:$s{yyyy-MM-dd HH:mm:ss}");

    private String integerFormatter;

    private String decimalFormatter;

    private String dateFormatter;

    private String timestampFormatter;

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

    public String getFormatter(DataType dataType) {
        if (dataType != null) {
            switch(dataType) {
                case BLOB:
                    break;
                case BOOLEAN:
                    break;
                case CHAR:
                    break;
                case CLOB:
                    break;
                case DATE:
                    return dateFormatter;
                case DECIMAL:
                case DOUBLE:
                case FLOAT:
                    return decimalFormatter;
                case INTEGER:
                case LONG:
                case SHORT:
                    return integerFormatter;
                case STRING:
                    break;
                case TIMESTAMP:
                case TIMESTAMP_UTC:
                    return timestampFormatter;
                default:
                    break;
                
            }
        }
        
        return null;
    }
    
    public Instance createInstance(UnifyComponentContext ctx) throws UnifyException {
        return new Instance(ctx.createFormatter(integerFormatter), ctx.createFormatter(decimalFormatter),
                ctx.createFormatter(dateFormatter), ctx.createFormatter(timestampFormatter));
    }

    public static class Instance {

        private Formatter<Object> integerFmt;

        private Formatter<Object> decimalFmt;

        private Formatter<Object> dateFmt;

        private Formatter<Object> timestampFmt;

        private Instance(Formatter<Object> integerFmt, Formatter<Object> decimalFmt, Formatter<Object> dateFmt,
                Formatter<Object> timestampFmt) {
            this.integerFmt = integerFmt;
            this.decimalFmt = decimalFmt;
            this.dateFmt = dateFmt;
            this.timestampFmt = timestampFmt;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public String[] format(EntityFieldDataType entityFieldDataType, Object val) throws UnifyException {            
            if (val != null) {
                Object[] _oval = null;
                if (val instanceof List) {
                    _oval = DataUtils.toArray(Object.class, (List) val);
                } else if (val.getClass().isArray()) {
                    final int len = Array.getLength(val);
                    _oval = new Object[len];
                    for (int i = 0; i < len; i++) {
                        _oval[i] = Array.get(val, i);
                    }
                } else {
                    _oval = new Object[] {val};
                }
                
                String[] result = new String[_oval.length];
                Formatter<Object> fmt = null;
                if (entityFieldDataType.isDecimal()) {
                    fmt = decimalFmt;
                } else if (entityFieldDataType.isInteger()) {
                    fmt = integerFmt;
                } else if (entityFieldDataType.isDate()) {
                    fmt = dateFmt;
                } else if (entityFieldDataType.isTimestamp()) {
                    fmt = timestampFmt;
                }

                if (fmt == null) {
                    for (int i = 0; i < _oval.length; i++) {
                        Object _val = _oval[i];
                        if (_val != null) {
                            result[i] = String.valueOf(_val);
                        }
                    }
                } else {
                    for (int i = 0; i < _oval.length; i++) {
                        Object _val = _oval[i];
                        if (_val != null) {
                            result[i] = fmt.format(_val);
                        }
                    }
                }

                return result;
            }

            return null;
        }
    }
}
