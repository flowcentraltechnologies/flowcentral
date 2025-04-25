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

package com.flowcentraltech.flowcentral.common.data;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Abstract base class for target form states.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractTargetFormStates {

    private List<TargetFormValue> targetValueList;

    public AbstractTargetFormStates() {
        this.targetValueList = new ArrayList<TargetFormValue>();
    }

    public void setFieldValue(String fieldName, Object val) {
        targetValueList.add(new TargetFormValue(fieldName, val));
    }
    
    public boolean isWithValueList() {
        return !targetValueList.isEmpty();
    }
    
    public void applyValues(ValueStore valueStore) throws UnifyException {
        for (TargetFormValue targetFormValue: targetValueList) {
            valueStore.store(targetFormValue.getFieldName(), targetFormValue.getValue());
        }
    }
}
