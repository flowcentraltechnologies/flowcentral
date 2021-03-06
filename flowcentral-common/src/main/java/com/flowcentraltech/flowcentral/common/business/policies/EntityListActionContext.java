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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.common.data.AbstractContext;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity list action context object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityListActionContext extends AbstractContext {

    private List<? extends Entity> instList;

    private String policyName;

    private Object result;

    public EntityListActionContext(List<? extends Entity> instList, String policyName) {
        this.instList = instList;
        this.policyName = policyName;
    }

    public List<? extends Entity> getInstList() {
        return instList;
    }

    public String getPolicyName() {
        return policyName;
    }

    public boolean isWithPolicy() {
        return policyName != null;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
