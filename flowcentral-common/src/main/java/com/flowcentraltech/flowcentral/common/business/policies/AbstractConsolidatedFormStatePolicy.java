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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.data.TargetFormTabStates;
import com.flowcentraltech.flowcentral.common.data.TargetFormWidgetStates;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for consolidated form state policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractConsolidatedFormStatePolicy extends AbstractUnifyComponent
        implements ConsolidatedFormStatePolicy {

    @Configurable
    private EnvironmentService environmentService;

    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Override
    public final TargetFormTabStates evaluateTabStates(ValueStoreReader reader, String trigger)
            throws UnifyException {
        TargetFormTabStates states = new TargetFormTabStates();
        evaluateTabStates(reader, trigger, states);
        return states;
    }

    @Override
    public final TargetFormWidgetStates evaluateWidgetStates(ValueStoreReader reader, String trigger)
            throws UnifyException {
        TargetFormWidgetStates states = new TargetFormWidgetStates();
        evaluateWidgetStates(reader, trigger, states);
        return states;
    }

    protected abstract boolean autoUpdateSupported() throws UnifyException;
    
    protected abstract void evaluateTabStates(ValueStoreReader reader, String trigger,
            TargetFormTabStates states) throws UnifyException;

    protected abstract void evaluateWidgetStates(ValueStoreReader reader, String trigger,
            TargetFormWidgetStates states) throws UnifyException;

    protected final EnvironmentService environment() {
        return environmentService;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}
