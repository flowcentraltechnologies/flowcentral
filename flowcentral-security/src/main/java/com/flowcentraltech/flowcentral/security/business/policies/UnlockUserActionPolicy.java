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

package com.flowcentraltech.flowcentral.security.business.policies;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractFormActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.security.business.SecurityModuleService;
import com.flowcentraltech.flowcentral.security.entities.User;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Unlock user action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "security.user" })
@Component(name = "unlockuser-actionpolicy", description = "$m{security.entityactionpolicy.unlockuser}")
public class UnlockUserActionPolicy extends AbstractFormActionPolicy {

    @Configurable
    private SecurityModuleService securityModuleService;

    @Override
    public boolean checkAppliesTo(ValueStoreReader reader) throws UnifyException {
        return true;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        User user = (User) ctx.getInst();
        logDebug("Executing unlock user pre-action policy for user [{0}] ...", user.getFullName());
        securityModuleService.unlockUser(user.getId());
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        User user = (User) ctx.getInst();
        logDebug("Executing unlock user post-action policy for user [{0}] ...", user.getFullName());
        EntityActionResult entityActionResult = new EntityActionResult(ctx);
        entityActionResult.setSuccessHint("$m{security.userresetunlock.unlock.success.hint}");
        return entityActionResult;
    }

}
