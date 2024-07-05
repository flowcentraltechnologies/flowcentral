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
package com.flowcentraltech.flowcentral.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.data.ParamValueDef;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.entities.ParamValues;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.common.input.BigDecimalArrayInput;
import com.flowcentraltech.flowcentral.common.input.BigDecimalInput;
import com.flowcentraltech.flowcentral.common.input.BooleanArrayInput;
import com.flowcentraltech.flowcentral.common.input.BooleanInput;
import com.flowcentraltech.flowcentral.common.input.CharacterArrayInput;
import com.flowcentraltech.flowcentral.common.input.CharacterInput;
import com.flowcentraltech.flowcentral.common.input.DateArrayInput;
import com.flowcentraltech.flowcentral.common.input.DateInput;
import com.flowcentraltech.flowcentral.common.input.DoubleArrayInput;
import com.flowcentraltech.flowcentral.common.input.DoubleInput;
import com.flowcentraltech.flowcentral.common.input.FloatArrayInput;
import com.flowcentraltech.flowcentral.common.input.FloatInput;
import com.flowcentraltech.flowcentral.common.input.IntegerArrayInput;
import com.flowcentraltech.flowcentral.common.input.IntegerInput;
import com.flowcentraltech.flowcentral.common.input.LongArrayInput;
import com.flowcentraltech.flowcentral.common.input.LongInput;
import com.flowcentraltech.flowcentral.common.input.ShortArrayInput;
import com.flowcentraltech.flowcentral.common.input.ShortInput;
import com.flowcentraltech.flowcentral.common.input.StringArrayInput;
import com.flowcentraltech.flowcentral.common.input.StringInput;
import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Common input utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class CommonInputUtils {

    private static final Class<?>[] NEW_INPUT_PARAMS = new Class<?>[] { String.class, String.class };

    private static final Map<InputType, Class<? extends AbstractInput<?>>> classToInputMap;

    private static final Map<Class<?>, InputType> classToInputTypeMap;

    static {
        Map<InputType, Class<? extends AbstractInput<?>>> map = new EnumMap<InputType, Class<? extends AbstractInput<?>>>(
                InputType.class);
        map.put(InputType.BOOLEAN, BooleanInput.class);
        map.put(InputType.CHAR, CharacterInput.class);
        map.put(InputType.SHORT, ShortInput.class);
        map.put(InputType.INTEGER, IntegerInput.class);
        map.put(InputType.LONG, LongInput.class);
        map.put(InputType.MAPPED, LongInput.class);
        map.put(InputType.FLOAT, FloatInput.class);
        map.put(InputType.DOUBLE, DoubleInput.class);
        map.put(InputType.DECIMAL, BigDecimalInput.class);
        map.put(InputType.DATE, DateInput.class);
        map.put(InputType.DATETIME, DateInput.class);
        map.put(InputType.STRING, StringInput.class);
        map.put(InputType.BOOLEAN_ARRAY, BooleanArrayInput.class);
        map.put(InputType.CHAR_ARRAY, CharacterArrayInput.class);
        map.put(InputType.SHORT_ARRAY, ShortArrayInput.class);
        map.put(InputType.INTEGER_ARRAY, IntegerArrayInput.class);
        map.put(InputType.LONG_ARRAY, LongArrayInput.class);
        map.put(InputType.FLOAT_ARRAY, FloatArrayInput.class);
        map.put(InputType.DOUBLE_ARRAY, DoubleArrayInput.class);
        map.put(InputType.DECIMAL_ARRAY, BigDecimalArrayInput.class);
        map.put(InputType.DATE_ARRAY, DateArrayInput.class);
        map.put(InputType.DATETIME_ARRAY, DateArrayInput.class);
        map.put(InputType.STRING_ARRAY, StringArrayInput.class);
        classToInputMap = Collections.unmodifiableMap(map);

        Map<Class<?>, InputType> map2 = new HashMap<Class<?>, InputType>();
        map2.put(boolean.class, InputType.BOOLEAN);
        map2.put(Boolean.class, InputType.BOOLEAN);
        map2.put(char.class, InputType.CHAR);
        map2.put(Character.class, InputType.CHAR);
        map2.put(short.class, InputType.SHORT);
        map2.put(Short.class, InputType.SHORT);
        map2.put(int.class, InputType.INTEGER);
        map2.put(Integer.class, InputType.INTEGER);
        map2.put(long.class, InputType.LONG);
        map2.put(Long.class, InputType.LONG);
        map2.put(float.class, InputType.FLOAT);
        map2.put(Float.class, InputType.FLOAT);
        map2.put(double.class, InputType.DOUBLE);
        map2.put(Double.class, InputType.DOUBLE);
        map2.put(BigDecimal.class, InputType.DECIMAL);
        map2.put(Date.class, InputType.DATE);
        map2.put(String.class, InputType.STRING);
        map2.put(boolean[].class, InputType.BOOLEAN_ARRAY);
        map2.put(Boolean[].class, InputType.BOOLEAN_ARRAY);
        map2.put(char[].class, InputType.CHAR_ARRAY);
        map2.put(Character[].class, InputType.CHAR_ARRAY);
        map2.put(short[].class, InputType.SHORT_ARRAY);
        map2.put(Short[].class, InputType.SHORT_ARRAY);
        map2.put(int[].class, InputType.INTEGER_ARRAY);
        map2.put(Integer[].class, InputType.INTEGER_ARRAY);
        map2.put(long[].class, InputType.LONG_ARRAY);
        map2.put(Long[].class, InputType.LONG_ARRAY);
        map2.put(float[].class, InputType.FLOAT_ARRAY);
        map2.put(Float[].class, InputType.FLOAT_ARRAY);
        map2.put(double[].class, InputType.DOUBLE_ARRAY);
        map2.put(Double[].class, InputType.DOUBLE_ARRAY);
        map2.put(BigDecimal[].class, InputType.DECIMAL_ARRAY);
        map2.put(Date[].class, InputType.DATE_ARRAY);
        map2.put(String[].class, InputType.STRING_ARRAY);
        classToInputTypeMap = Collections.unmodifiableMap(map2);
    }

    private CommonInputUtils() {

    }
    
    public static String buildCollectionString(List<String> val) {
        if (val != null) {
            return CommonInputUtils.buildCollectionString(DataUtils.toArray(String.class, val));
        }
        
        return null;
    }
    
    public static String buildCollectionString(String[] val) {
        if (val != null) {
            StringBuilder sb = new StringBuilder();
            boolean appendSym = false;
            for (String valin : val) {
                if (appendSym) {
                    sb.append('|');
                } else {
                    appendSym = true;
                }

                sb.append(valin);
            }
            
            return sb.toString();
        }
        return null;
    }

    public static String[] breakdownCollectionString(String val) {
        return val != null ? val.split("\\|") : null;
    }

    public static Class<? extends AbstractInput<?>> getInputClass(InputType type) {
        return classToInputMap.get(type);
    }

    public static AbstractInput<?> newInput(ParamConfig paramConfig) throws UnifyException {
        if (paramConfig.isWithEditor()) {
            Class<? extends AbstractInput<?>> inputClass = classToInputMap
                    .get(classToInputTypeMap.get(paramConfig.getType()));
            return (AbstractInput<?>) ReflectUtils.newInstance(inputClass, NEW_INPUT_PARAMS,
                    paramConfig.getEditor() + " style:$s{width:100%;}", "!ui-label");
        }

        return null;
    }

    public static ParamValuesDef getParamValuesDef(List<ParamConfig> paramConfigList) throws UnifyException {
        return CommonInputUtils.getParamValuesDef(paramConfigList, null);
    }

    public static ParamValuesDef getParamValuesDef(List<ParamConfig> paramConfigList, ParamValues paramValues)
            throws UnifyException {
        ParamValuesDef.Builder pvdb = ParamValuesDef.newBuilder();
        Map<String, String> valMap = Collections.emptyMap();
        if (paramValues != null) {
            valMap = new HashMap<String, String>();
            try(BufferedReader reader = new BufferedReader(new StringReader(paramValues.getDefinition()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    String paramName = p[0];
                    String paramVal = p.length > 1 ? p[1] : null;
                    valMap.put(paramName, paramVal);
                }
            } catch (IOException e) {
                throw new UnifyOperationException(e);
            }
        }

        for (ParamConfig pc : paramConfigList) {
            pvdb.addParamValueDef(pc, valMap.get(pc.getParamName()));
        }

        return pvdb.build();
    }

    public static String getParamValuesDefinition(ParamValuesDef paramValuesDef) throws UnifyException {
        StringWriter sw = new StringWriter();
         try(BufferedWriter bw = new BufferedWriter(sw)) {
            for (ParamValueDef paramValueDef : paramValuesDef.getParamValueList()) {
                bw.write(paramValueDef.getParamConfig().getParamName());
                bw.write(']');
                if (paramValueDef.getParamVal() != null) {
                    bw.write(paramValueDef.getParamVal());
                    bw.write(']');
                }

                bw.newLine();
            }
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }
        return sw.toString();
    }
}
