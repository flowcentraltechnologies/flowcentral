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
package com.flowcentraltech.flowcentral.common.web.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.AbstractListCommand;
import com.tcdng.unify.core.list.ListParam;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for flowCentral type list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractFlowCentralTypeListCommand<T extends UnifyComponent, U extends ListParam>
        extends AbstractListCommand<U> {

    @Configurable
    private MessageResolver messageResolver;

    private Class<T> typeClass;

    private List<UnifyComponentConfig> configList;

    public AbstractFlowCentralTypeListCommand(Class<T> typeClazz, Class<U> paramClazz) {
        super(paramClazz);
        this.typeClass = typeClazz;
    }

    @Override
    public List<? extends Listable> execute(Locale locale, U params) throws UnifyException {
        return filterList(getConfigList(), params);
    }

    protected List<? extends Listable> filterList(List<UnifyComponentConfig> baseConfigList, U params)
            throws UnifyException {
        if (messageResolver != null && !DataUtils.isBlank(baseConfigList)) {
            List<ListData> list = new ArrayList<ListData>();
            for (Listable listable : baseConfigList) {
                list.add(new ListData(listable.getListKey(),
                        messageResolver.resolveSessionMessage(listable.getListDescription())));
            }

            return list;
        }
        
        return baseConfigList;
    }

    protected MessageResolver getMessageResolver() {
        return messageResolver;
    }

    private List<UnifyComponentConfig> getConfigList() throws UnifyException {
        if (configList == null) {
            synchronized (this) {
                if (configList == null) {
                    configList = DataUtils.unmodifiableList(getComponentConfigs(typeClass));
                }
            }
        }

        return configList;
    }
}
