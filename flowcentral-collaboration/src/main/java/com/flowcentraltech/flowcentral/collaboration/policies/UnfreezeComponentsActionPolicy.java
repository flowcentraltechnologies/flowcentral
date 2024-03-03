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
package com.flowcentraltech.flowcentral.collaboration.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.collaboration.business.CollaborationModuleService;
import com.flowcentraltech.flowcentral.collaboration.entities.FreezeUnfreeze;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractEntityListActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Unfreeze components action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("unfreezecomponents-actionpolicy")
public class UnfreezeComponentsActionPolicy extends AbstractEntityListActionPolicy {

    @Configurable
    private CollaborationModuleService collaborationModuleService;

    @SuppressWarnings("unchecked")
    @Override
    public EntityListActionResult executeAction(EntityListActionContext ctx, String rule) throws UnifyException {
        List<FreezeUnfreeze> unfreezeList = (List<FreezeUnfreeze>) ctx.getInstList();
        if (!DataUtils.isBlank(unfreezeList)) {
            collaborationModuleService.unfreezeComponents(unfreezeList);
        }

        return null;
    }

}
