/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for entity action policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityActionPolicy extends AbstractUnifyComponent implements EntityActionPolicy {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private EnvironmentService environment;

    public final void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public final void setEnvironment(EnvironmentService environment) {
        this.environment = environment;
    }

    @Override
    public final void executePreAction(EntityActionContext ctx) throws UnifyException {
        if (checkAppliesTo(ctx.getInst())) {
            doExecutePreAction(ctx);
        }
    }

    @Override
    public final EntityActionResult executePostAction(EntityActionContext ctx) throws UnifyException {
        EntityActionResult entityActionResult = null;
        if (checkAppliesTo(ctx.getInst())) {
            entityActionResult = doExecutePostAction(ctx);
        }

        if (entityActionResult == null) {
            return new EntityActionResult(ctx);
        }

        return entityActionResult;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected final EnvironmentService environment() {
        return environment;
    }

    protected void registerPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode,
            String privilegeDesc) throws UnifyException {
        if (!applicationPrivilegeManager.isRegisteredPrivilege(privilegeCategoryCode, privilegeCode)) {
            applicationPrivilegeManager.registerPrivilege(applicationId, privilegeCategoryCode, privilegeCode,
                    privilegeDesc);
        }
    }

    protected void unregisterPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode)
            throws UnifyException {
        applicationPrivilegeManager.unregisterPrivilege(applicationId, privilegeCategoryCode, privilegeCode);
    }

    protected boolean assignPrivilegeToRole(String roleCode, String privilegeCode)
            throws UnifyException {
        return applicationPrivilegeManager.assignPrivilegeToRole(roleCode, privilegeCode);
    }

    protected abstract void doExecutePreAction(EntityActionContext ctx) throws UnifyException;

    protected abstract EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException;

}
