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

package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.data.SearchInput;
import com.flowcentraltech.flowcentral.common.web.util.EntityConfigurationUtils;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for entity type search widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@UplAttributes({
        @UplAttribute(name = "ref", type = String[].class, defaultVal = "$l{}"),
        @UplAttribute(name = "direct", type = boolean.class, defaultVal = "true"),
        @UplAttribute(name = "listKey", type = String.class),
        @UplAttribute(name = "entityName", type = String.class),
        @UplAttribute(name = "styleClass", type = String.class, defaultVal = "$e{fc-entitysearch}") })
public abstract class AbstractEntityTypeSearchWidget<T extends UnifyComponent> extends EntitySearchWidget {

    @Configurable
    private MessageResolver messageResolver;

    private Class<T> typeClass;

    public AbstractEntityTypeSearchWidget(Class<T> typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    protected Listable doCurrentSelect(Object keyVal) throws UnifyException {
        if (keyVal != null) {
            UnifyComponentConfig config = getComponentConfig((String) keyVal);
            if (config != null) {
                String description = config.getDescription() != null
                        ? messageResolver.resolveSessionMessage(config.getDescription())
                        : config.getName();
                return new ListData(config.getName(), description);
            }
        }

        return null;
    }

    @Override
    protected List<? extends Listable> doSearch(String input, int limit) throws UnifyException {
        List<UnifyComponentConfig> configList = getComponentConfigs(typeClass);
        if (!DataUtils.isBlank(configList)) {
            final String entityName = getUplAttribute(String.class, "entityName");
            return StringUtils.isBlank(entityName)
                    ? EntityConfigurationUtils.getConfigListable(configList, messageResolver,
                            new SearchInput(input, limit))
                    : EntityConfigurationUtils.getConfigListableByEntity(configList, entityName, messageResolver,
                            new SearchInput(input, limit));
        }

        return Collections.emptyList();
    }

}
