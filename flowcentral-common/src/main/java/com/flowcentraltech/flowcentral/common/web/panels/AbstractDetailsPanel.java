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

package com.flowcentraltech.flowcentral.common.web.panels;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for details panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDetailsPanel<T> extends AbstractFlowCentralStandalonePanel implements DetailsPanel {

    @Override
    public final void loadDetails(ValueStore valueStore) throws UnifyException {
        T details = getDetails(valueStore);
        super.setValueStore(new BeanValueStore(details));
    }

    @Override
    public final void setValueStore(ValueStore valueStore) throws UnifyException {
        
    }

    /**
     * Get value from parent reader stored as temporary value
     * 
     * @param dataType
     *                     the value data type
     * @param propertyName
     *                     the property name
     * @return the value
     * @throws UnifyException
     *                        if an error occurs
     */
    protected final <U> U getParentValue(Class<U> dataType, String propertyName) throws UnifyException {
        ValueStoreReader parentReader = getValueStore().getTempValue(ValueStoreReader.class, "parentReader");
        return parentReader != null ? parentReader.read(dataType, propertyName) : null;
    }

    protected abstract T getDetails(ValueStore valueStore) throws UnifyException;
}
