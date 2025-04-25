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
package com.flowcentraltech.flowcentral.common.web.lists;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.web.util.EntityConfigurationUtils;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.list.ListParam;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for entity type list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityTypeListCommand<T extends UnifyComponent, U extends ListParam>
        extends AbstractFlowCentralTypeListCommand<T, U> {

    public AbstractEntityTypeListCommand(Class<T> typeClazz, Class<U> paramClazz) {
        super(typeClazz, paramClazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<? extends Listable> filterList(List<UnifyComponentConfig> baseConfigList, U param)
            throws UnifyException {
        final String entityName = getEntityName(param);
        final boolean accept = acceptNonReferenced();
        if (!DataUtils.isBlank(baseConfigList)) {
            List<UnifyComponentConfig> _baseConfigList = new ArrayList<UnifyComponentConfig>();
            for (UnifyComponentConfig config : baseConfigList) {
                if (accept((Class<T>) config.getType())) {
                    _baseConfigList.add(config);
                }
            }

            baseConfigList = _baseConfigList;
        }

        return accept
                ? EntityConfigurationUtils.getConfigListableByEntityAcceptNonReferenced(baseConfigList, entityName,
                        getMessageResolver())
                : EntityConfigurationUtils.getConfigListableByEntity(baseConfigList, entityName, getMessageResolver());
    }

    protected boolean accept(Class<T> type) {
        return true;
    }

    protected boolean acceptNonReferenced() {
        return true;
    }

    protected abstract String getEntityName(U param) throws UnifyException;
}
