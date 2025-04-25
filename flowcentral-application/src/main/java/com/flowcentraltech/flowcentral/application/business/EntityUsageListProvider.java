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
package com.flowcentraltech.flowcentral.application.business;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Entity usage list provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("entity-usagelistprovider")
public class EntityUsageListProvider extends AbstractUsageListProvider {

    @Override
    public List<Usage> findUsages(ValueStoreReader instReader, UsageType usageType) throws UnifyException {
        final String entity = ApplicationNameUtils.getApplicationEntityLongName(
                instReader.read(String.class, "applicationName"), instReader.read(String.class, "name"));
        logDebug("Finding usages of entity [{0}]...", entity);
        List<Usage> usageList = new ArrayList<Usage>();
        for (UsageProvider provider : getProviders()) {
            List<Usage> _usageList = provider.findEntityUsages(entity, usageType);
            usageList.addAll(_usageList);
        }

        return usageList;
    }

    @Override
    public long countUsages(ValueStoreReader instReader, UsageType usageType) throws UnifyException {
        final String entity = ApplicationNameUtils.getApplicationEntityLongName(
                instReader.read(String.class, "applicationName"), instReader.read(String.class, "name"));
        logDebug("Count usages of entity [{0}]...", entity);
        long usages = 0;
        for (UsageProvider provider : getProviders()) {
            usages += provider.countEntityUsages(entity, usageType);
        }

        return usages;
    }

}
