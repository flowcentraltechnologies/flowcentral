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

package com.flowcentraltech.flowcentral.audit.policies;

import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationFormActionPolicy;
import com.flowcentraltech.flowcentral.audit.constants.AuditModuleSessionAttributes;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Form action policy for launching audit trail applet.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({ "audit.entityAuditKeys" })
@Component(name = "launchaudittrail-actionpolicy", description = "Launch audit trail Action Policy")
public class LaunchAuditTrailFormActionPolicy extends AbstractApplicationFormActionPolicy {

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult result = new EntityActionResult(ctx);
        final ValueStoreReader reader = new BeanValueStore(ctx.getInst()).getReader();
        setSessionAttribute(AuditModuleSessionAttributes.AUDIT_BASE_AUDIT_NO, reader.read(String.class, "auditNo"));
        // TODO Return page open
        //result.setDisplayListingReport(true);
        return result;
    }

}
