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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;

/**
 * Convenient abstract base class for entry table policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntryTablePolicy extends AbstractUnifyComponent implements EntryTablePolicy {

    @Configurable
    private EnvironmentService environmentService;

    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Override
    public Number getParentColumnSummaryValue(ValueStoreReader parentReader, String fieldName) throws UnifyException {
        return null;
    }

    @Override
    public Number getTableColumnSummaryValue(ValueStoreReader parentReader, String fieldName, ValueStore itemValueStore)
            throws UnifyException {
        return (Number) itemValueStore.retrieve(fieldName);
    }

    @Override
    public void applyFixedAction(ValueStoreReader parentReader, ValueStore valueStore, int index,
            FixedRowActionType fixedActionType) throws UnifyException {
        if (!fixedActionType.fixed()) {
            doApplyFixedAction(parentReader, valueStore, index, fixedActionType);
            if (fixedActionType.updateLean()) {
                environmentService.updateLean((Entity) valueStore.getValueObject()); 
            } else if (fixedActionType.delete()){
                environmentService.delete((Entity) valueStore.getValueObject()); 
            }
        }
    }

    @Override
    public List<TableSummaryLine> getTableSummaryLines(ValueStore tableValueStore) {
        return Collections.emptyList();
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected EnvironmentService environment() {
        return environmentService;
    }

    protected abstract void doApplyFixedAction(ValueStoreReader parentReader, ValueStore valueStore, int index,
            FixedRowActionType fixedActionType) throws UnifyException;
}
