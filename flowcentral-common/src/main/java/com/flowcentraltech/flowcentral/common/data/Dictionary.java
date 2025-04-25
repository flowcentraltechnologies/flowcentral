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

import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Dictionary class.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class Dictionary {

    private Map<String, Object> valueMap;

    public Dictionary() {
        valueMap = new HashMap<String, Object>();
    }

    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValue(String key, Object val) {
        valueMap.put(key, val);
    }

    public Object replaceValue(String key, Object val) {
        Object oldVal = valueMap.get(key);
        valueMap.put(key, val);
        return oldVal;
    }

    public Object getValue(String key) {
        return valueMap.get(key);
    }

    public <T> T getValue(Class<T> targetClazz, String key) throws UnifyException {
        return DataUtils.convert(targetClazz, valueMap.get(key));
    }

    public <T> T getValue(Class<T> targetClazz, String key, Formatter<?> formatter) throws UnifyException {
        return DataUtils.convert(targetClazz, valueMap.get(key), formatter);
    }

    public void clear() {
        valueMap.clear();
    }

    public int size() {
        return valueMap.size();
    }

    public boolean isKey(String key) {
        return valueMap.containsKey(key);
    }

    public boolean isEmpty() {
        return valueMap.isEmpty();
    }
}
