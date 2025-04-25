/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ParamConfig;

/**
 * Parameter value entry.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ParamValueEntry {

    private ParamConfig paramConfig;

    private AbstractInput<?> paramInput;

    public ParamValueEntry(ParamConfig paramConfig) {
        this.paramConfig = paramConfig;
    }

    public ParamConfig getParamConfig() {
        return paramConfig;
    }

    public AbstractInput<?> getParamInput() {
        return paramInput;
    }

    public boolean isWithParamInput() {
        return paramInput != null;
    }

    public void normalize() throws UnifyException {
        paramInput = paramInput != null ? paramInput : CommonInputUtils.newInput(paramConfig);
    }

}
