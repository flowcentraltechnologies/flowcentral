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
package com.flowcentraltech.flowcentral.organization.business;

import java.util.Collections;

import com.flowcentraltech.flowcentral.application.business.AbstractMappedEntityProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Default mapped role workflow step provider
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("default-mappedrolewfstepprovider")
public class DefaultMappedRoleWfStepProvider
        extends AbstractMappedEntityProvider<DefaultMappedRoleWfStepProviderContext> {

    public DefaultMappedRoleWfStepProvider() {
        super(DefaultMappedRoleWfStepProviderContext.class, "organization.mappedRoleWfStep", "workflow.wfStepRole",
                Collections.emptyMap());
    }

    @Override
    protected void doMappedCopyFromDestToSrc(DefaultMappedRoleWfStepProviderContext context, ValueStore srcValueStore,
            ValueStore destValueStore) throws UnifyException {
        srcValueStore.copy(destValueStore);
    }

    @Override
    protected void doMappedCopyFromSrcToDest(DefaultMappedRoleWfStepProviderContext context, ValueStore destValueStore,
            ValueStore srcValueStore) throws UnifyException {
        destValueStore.copy(srcValueStore);
    }

}
