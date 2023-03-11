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

package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application parameter value definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ParamValueDef {

    private ParamConfig paramConfig;

    private String paramVal;

    public ParamValueDef(ParamConfig paramConfig, String paramVal) {
        this.paramConfig = paramConfig;
        this.paramVal = paramVal;
    }

    public ParamConfig getParamConfig() {
        return paramConfig;
    }

    public String getParamName() {
        return paramConfig.getParamName();
    }

    public String getParamVal() {
        return paramVal;
    }

    public Object getConvertedParamVal() throws UnifyException {
        return DataUtils.convert(paramConfig.getType(), paramVal);
    }

    public <T> T getConvertedParamVal(Class<T> clazz) throws UnifyException {
        return DataUtils.convert(clazz, paramVal);
    }

    @Override
    public String toString() {
        return "ParamValueDef [paramConfig=" + paramConfig + ", paramVal=" + paramVal + "]";
    }
}
