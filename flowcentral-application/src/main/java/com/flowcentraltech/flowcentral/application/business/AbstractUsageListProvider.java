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
package com.flowcentraltech.flowcentral.application.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;

/**
 * Convenient abstract base class for usage list providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractUsageListProvider extends AbstractUnifyComponent implements UsageListProvider {

    private List<UsageProvider> providers;

    @Override
    protected void onInitialize() throws UnifyException {
        providers = new ArrayList<UsageProvider>();
        for (UnifyComponentConfig config : getComponentConfigs(UsageProvider.class)) {
            UsageProvider provider = (UsageProvider) getComponent(config.getName());
            providers.add(provider);
        }
        
        providers = Collections.unmodifiableList(providers);
    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected List<UsageProvider> getProviders() {
        return providers == null ? Collections.emptyList() : providers;
    }

}
