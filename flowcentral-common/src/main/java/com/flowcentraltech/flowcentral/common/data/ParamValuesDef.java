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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application parameter values definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ParamValuesDef {

    private List<ParamValueDef> paramValueList;

    private Map<String, Object> values;

    private ParamValuesDef(List<ParamValueDef> paramValueList) {
        this.paramValueList = paramValueList;
    }

    public List<ParamValueDef> getParamValueList() {
        return paramValueList;
    }

    public Map<String, Object> getValueMap() throws UnifyException {
        if (values == null) {
            values = new HashMap<String, Object>();
            for (ParamValueDef paramValueDef : paramValueList) {
                values.put(paramValueDef.getParamName(), paramValueDef.getConvertedParamVal());
            }
        }

        return values;
    }

    public Object getParamValue(String name) throws UnifyException {
        return getValueMap().get(name);
    }
    
    public boolean isBlank() {
        return paramValueList == null || paramValueList.isEmpty();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<ParamValueDef> paramValueList;

        public Builder() {
            paramValueList = new ArrayList<ParamValueDef>();
        }

        public Builder addParamValueDef(ParamConfig paramConfig, String paramVal) {
            paramValueList.add(new ParamValueDef(paramConfig, paramVal));
            return this;
        }

        public ParamValuesDef build() throws UnifyException {
            return new ParamValuesDef(DataUtils.unmodifiableList(paramValueList));
        }
    }
}
