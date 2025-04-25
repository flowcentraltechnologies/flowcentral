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

package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Convenient abstract base class for single form beans.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractSingleFormBean implements SingleFormBean {

    private AppletUtilities au;

    private ValueStore beanValueStore;

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
    public final void unload(Entity inst) throws UnifyException {
        saveToEntity(new BeanValueStore(inst));
    }

    protected AppletUtilities au() {
        return au;
    }

    protected void copyFrom(ValueStore entityValueStore) throws UnifyException {
        getValueStore().copy(entityValueStore);
    }

    protected void copyTo(ValueStore entityValueStore) throws UnifyException {
        entityValueStore.copyWithExclusions(getValueStore(), ApplicationEntityUtils.RESERVED_BASE_FIELDS);
    }

    protected ValueStore getValueStore() {
        if (beanValueStore == null) {
            synchronized (this) {
                if (beanValueStore == null) {
                    beanValueStore = new BeanValueStore(this);
                }
            }
        }

        return beanValueStore;
    }

    protected abstract void doInit() throws UnifyException;

    protected abstract void loadFromEntity(ValueStore entityValueStore) throws UnifyException;

    protected abstract void saveToEntity(ValueStore entityValueStore) throws UnifyException;
}
