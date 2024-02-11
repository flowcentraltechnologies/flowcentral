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
package com.flowcentraltech.flowcentral.common.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.data.SearchInput;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity configuration utility methods.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class EntityConfigurationUtils {

    private EntityConfigurationUtils() {

    }

    public static List<UnifyComponentConfig> filterConfigListByEntity(List<UnifyComponentConfig> configList,
            String entityName) throws UnifyException {
        return EntityConfigurationUtils.filterConfigListByEntity(configList, entityName, false);
    }

    public static List<UnifyComponentConfig> filterConfigListByEntityAcceptNonReferenced(
            List<UnifyComponentConfig> configList, String entityName) throws UnifyException {
        return EntityConfigurationUtils.filterConfigListByEntity(configList, entityName, true);
    }

    public static List<? extends Listable> getConfigListableByEntity(List<UnifyComponentConfig> configList,
            String entityName, MessageResolver messageResolver) throws UnifyException {
        return EntityConfigurationUtils.getConfigListableByEntity(configList, entityName, messageResolver, null);
    }

    public static List<? extends Listable> getConfigListableByEntity(List<UnifyComponentConfig> configList,
            String entityName, MessageResolver messageResolver, SearchInput search) throws UnifyException {
        return EntityConfigurationUtils.getConfigListableByEntity(configList, entityName, messageResolver, search,
                false);
    }

    public static List<? extends Listable> getConfigListableByEntityAcceptNonReferenced(
            List<UnifyComponentConfig> configList, String entityName, MessageResolver messageResolver)
            throws UnifyException {
        return EntityConfigurationUtils.getConfigListableByEntityAcceptNonReferenced(configList, entityName,
                messageResolver, null);
    }

    public static List<? extends Listable> getConfigListableByEntityAcceptNonReferenced(
            List<UnifyComponentConfig> configList, String entityName, MessageResolver messageResolver,
            SearchInput search) throws UnifyException {
        return EntityConfigurationUtils.getConfigListableByEntity(configList, entityName, messageResolver, search,
                true);
    }

    public static List<? extends Listable> getConfigListable(List<UnifyComponentConfig> configList,
            MessageResolver messageResolver) throws UnifyException {
        return EntityConfigurationUtils.getConfigListable(configList, messageResolver, null);
    }

    public static List<? extends Listable> getConfigListable(List<UnifyComponentConfig> configList,
            MessageResolver messageResolver, SearchInput search) throws UnifyException {
        if (!DataUtils.isBlank(configList)) {
            List<Listable> list = new ArrayList<Listable>();
            final String input = search != null && !StringUtils.isBlank(search.getInput())
                    ? search.getInput().toLowerCase()
                    : null;
            int limit = search != null ? search.getLimit() : 0;
            for (UnifyComponentConfig unifyComponentConfig : configList) {
                String description = unifyComponentConfig.getDescription() != null
                        ? messageResolver.resolveSessionMessage(unifyComponentConfig.getDescription())
                        : unifyComponentConfig.getName();
                if (input != null && !description.toLowerCase().contains(input)) {
                    continue;
                }

                list.add(new ListData(unifyComponentConfig.getName(), description));

                if (limit > 0 && (--limit == 0)) {
                    break;
                }
            }

            DataUtils.sortAscending(list, Listable.class, "listDescription");
            return list;
        }

        return Collections.emptyList();
    }

    private static List<? extends Listable> getConfigListableByEntity(List<UnifyComponentConfig> configList,
            String entityName, MessageResolver messageResolver, SearchInput search, boolean acceptNonReferenced)
            throws UnifyException {
        List<UnifyComponentConfig> filteredList = EntityConfigurationUtils.filterConfigListByEntity(configList,
                entityName, acceptNonReferenced);
        return EntityConfigurationUtils.getConfigListable(filteredList, messageResolver, search);
    }

    private static List<UnifyComponentConfig> filterConfigListByEntity(List<UnifyComponentConfig> configList,
            String entityName, boolean acceptNonReferenced) throws UnifyException {
        if (!DataUtils.isBlank(configList)) {
            if (!StringUtils.isBlank(entityName)) {
                List<UnifyComponentConfig> list = new ArrayList<UnifyComponentConfig>();
                for (UnifyComponentConfig unifyComponentConfig : configList) {
                    EntityReferences era = unifyComponentConfig.getType().getAnnotation(EntityReferences.class);
                    if (era == null) {
                        if (acceptNonReferenced) {
                            list.add(unifyComponentConfig);
                        }
                    } else {
                        for (String entity : era.value()) {
                            if (entityName.equals(entity) || "*".equals(entity)) {
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

}
