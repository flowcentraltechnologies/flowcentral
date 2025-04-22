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

package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.data.ValueStoreWriter;

/**
 * Workflow entity instance object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfEntityInst {

    private ValueStore valueStore;

    public WfEntityInst(WorkEntity wfEntityInst) {
        this.valueStore = new BeanValueStore(wfEntityInst);
    }

    public WorkEntity getWfEntityInst() {
        return (WorkEntity) valueStore.getValueObject();
    }

    public ValueStoreReader getReader() {
        return valueStore.getReader();
    }

    public ValueStoreWriter getWriter() {
        return valueStore.getWriter();
    }

    public ValueStore getWfInstValueStore() {
        return valueStore;
    }

}
