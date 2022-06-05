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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.Collection;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;

/**
 * Convenient abstract base class for single form beans.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSingleFormBean implements SingleFormBean {

    private AppletUtilities au;

    @Override
    public final void init(AppletUtilities au) throws UnifyException {
        this.au = au;
        doInit();
    }

    @Override
    public final void load(Entity inst) throws UnifyException {
        loadFromEntity(new BeanValueStore(inst));
    }

    @Override
    public final void save(Entity inst) throws UnifyException {
        saveToEntity(new BeanValueStore(inst));
    }

    protected AppletUtilities getAu() {
        return au;
    }

    protected void copyFrom(ValueStore entityValueStore) throws UnifyException {
        new BeanValueStore(this).copy(entityValueStore);
    }

    protected void copyFromWithExclusions(ValueStore entityValueStore, String... fieldNames) throws UnifyException {
        new BeanValueStore(this).copyWithExclusions(entityValueStore, fieldNames);
    }

    protected void copyFromWithInclusions(ValueStore entityValueStore, String... fieldNames) throws UnifyException {
        new BeanValueStore(this).copyWithInclusions(entityValueStore, fieldNames);
    }

    protected void copyTo(ValueStore entityValueStore) throws UnifyException {
        entityValueStore.copy(new BeanValueStore(this));
    }

    protected void copyToWithExclusions(ValueStore entityValueStore, String... fieldNames) throws UnifyException {
        entityValueStore.copyWithExclusions(new BeanValueStore(this), fieldNames);
    }

    protected void copyToWithInclusions(ValueStore entityValueStore, String... fieldNames) throws UnifyException {
        entityValueStore.copyWithInclusions(new BeanValueStore(this), fieldNames);
    }

    protected abstract void doInit() throws UnifyException;

    protected abstract void loadFromEntity(ValueStore entityValueStore) throws UnifyException;

    protected abstract void saveToEntity(ValueStore entityValueStore) throws UnifyException;
}
