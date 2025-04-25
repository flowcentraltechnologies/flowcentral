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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.SynchronizableEnvironmentDelegate;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralTypeListCommand;
import com.flowcentraltech.flowcentral.common.web.util.EntityConfigurationUtils;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * Studio synchronizable environment delegate list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studiosyncenvironmentdelegatelist")
public class StudioSyncEnvironmentDelegateListCommand
        extends AbstractFlowCentralTypeListCommand<SynchronizableEnvironmentDelegate, ZeroParams> {

    public StudioSyncEnvironmentDelegateListCommand() {
        super(SynchronizableEnvironmentDelegate.class, ZeroParams.class);
    }

    @Override
    protected List<? extends Listable> filterList(List<UnifyComponentConfig> baseConfigList, ZeroParams params)
            throws UnifyException {
        return EntityConfigurationUtils.getConfigListable(baseConfigList, getMessageResolver());
    }

}
