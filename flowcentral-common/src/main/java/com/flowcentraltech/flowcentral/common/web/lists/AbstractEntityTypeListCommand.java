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
package com.flowcentraltech.flowcentral.common.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.list.ListParam;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

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

    @Override
    final protected List<UnifyComponentConfig> filterList(List<UnifyComponentConfig> baseConfigList, U param)
            throws UnifyException {
        if (!DataUtils.isBlank(baseConfigList)) {
            final String entityName = getEntityName(param);
            if (!StringUtils.isBlank(entityName)) {
                List<UnifyComponentConfig> list = new ArrayList<UnifyComponentConfig>();
                final boolean accept = acceptNonReferenced();
                for (UnifyComponentConfig unifyComponentConfig : baseConfigList) {
                    EntityReferences era = unifyComponentConfig.getType().getAnnotation(EntityReferences.class);
                    if (era == null) {
                        if (accept) {
                            list.add(unifyComponentConfig);
                        }
                    } else {
                        for (String entity : era.value()) {
                            if (entityName.equals(entity) || "*".equals(entity)) { // TODO Implement partial wildcard
                                list.add(unifyComponentConfig);
                                break;
                            }
                        }
                    }
                }
                return list;
            }
        }

        return Collections.emptyList();
    }

    protected boolean acceptNonReferenced() {
        return true;
    }

    protected abstract String getEntityName(U param) throws UnifyException;
}
