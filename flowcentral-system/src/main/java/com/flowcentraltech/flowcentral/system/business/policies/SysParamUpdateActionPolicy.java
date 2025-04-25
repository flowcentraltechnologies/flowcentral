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

package com.flowcentraltech.flowcentral.system.business.policies;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractAppletActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.entities.SystemParameter;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.SessionAttributeProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * System parameter update action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "system.sysParam" })
@Component(name = "sysparam-updateactionpolicy", description = "$m{system.entityactionpolicy.sysparamupdate}")
public class SysParamUpdateActionPolicy extends AbstractAppletActionPolicy {

    private static final Set<String> resetParams = Collections.unmodifiableSet(new HashSet<String>(
            Arrays.asList(SystemModuleSysParamConstants.SYSTEM_GLOBAL_ACCOUNTING_INPUT_ENABLED)));

    @Configurable
    private SessionAttributeProvider sessionAttributeProvider;

    @Override
    public boolean checkAppliesTo(Entity inst) throws UnifyException {
        return true;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        SystemParameter systemParameter = (SystemParameter) ctx.getInst();
        EntityActionResult result = new EntityActionResult(ctx);
        // TODO This system parameter code should fetched
        final String code = systemParameter.getCode();
        boolean refreshMenu = "APP-0005".equals(code);
        result.setRefreshMenu(refreshMenu);
        
        if (resetParams.contains(code))  {
            sessionAttributeProvider.reset();
        }           
        return result;
    }

}
