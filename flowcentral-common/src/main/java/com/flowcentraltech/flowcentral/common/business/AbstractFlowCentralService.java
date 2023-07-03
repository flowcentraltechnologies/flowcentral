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
package com.flowcentraltech.flowcentral.common.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralEditionConstants;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.business.AbstractBusinessService;

/**
 * Base class for flowCentral service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractFlowCentralService extends AbstractBusinessService implements FlowCentralService {

    @Configurable
    private EnvironmentService environmentService;

    public void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Override
    public final void installFeatures(List<ModuleInstall> moduleInstallList) throws UnifyException {
        for (ModuleInstall moduleInstall : moduleInstallList) {
            doInstallModuleFeatures(moduleInstall);
        }
    }

    protected final boolean isEnterprise() throws UnifyException {
        return FlowCentralEditionConstants.ENTERPRISE.equalsIgnoreCase(getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_INSTALLATION_TYPE));
    }

    protected final EnvironmentService environment() {
        return environmentService;
    }

    protected void executeEntityPreActionPolicy(EntityActionContext ctx) throws UnifyException {
        if (ctx.isWithActionPolicy()) {
            ((EntityActionPolicy) getComponent(ctx.getActionPolicyName())).executePreAction(ctx);
        }
    }

    protected EntityActionResult executeEntityPostActionPolicy(EntityActionContext ctx) throws UnifyException {
        if (ctx.isWithSweepingCommitPolicy()) {
            ctx.getSweepingCommitPolicy().bumpAllParentVersions(db(), ctx.getActionType());
        }

        if (ctx.isWithActionPolicy()) {
            return ((EntityActionPolicy) getComponent(ctx.getActionPolicyName())).executePostAction(ctx);
        }

        return new EntityActionResult(ctx);
    }

    protected abstract void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException;

}
