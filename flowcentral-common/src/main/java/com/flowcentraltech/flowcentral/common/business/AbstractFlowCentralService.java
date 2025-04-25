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
package com.flowcentraltech.flowcentral.common.business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralEditionConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.VersionedEntityDef;
import com.flowcentraltech.flowcentral.configuration.data.Messages;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.system.HeartbeatManager;
import com.tcdng.unify.core.util.PostResp;

/**
 * Base class for flowCentral service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractFlowCentralService extends AbstractBusinessService implements FlowCentralService {

    @Configurable
    private EnvironmentService environmentService;

    @Configurable
    private HeartbeatManager heartbeatManager;

    private static final String SYSTEM_RESTORE_LOCK = "app:system-restore-lock";

    private static final int KEEP_CLUSTER_SAFE_IN_MINUTES = 2;

    @Override
    public final void installFeatures(List<ModuleInstall> moduleInstallList) throws UnifyException {
        for (ModuleInstall moduleInstall : moduleInstallList) {
            doInstallModuleFeatures(moduleInstall);
        }
    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {
        
    }

    protected final String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }
    
    protected final boolean isEnterprise() throws UnifyException {
        return FlowCentralEditionConstants.ENTERPRISE.equalsIgnoreCase(getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_INSTALLATION_TYPE));
    }

    protected final EnvironmentService environment() {
        return environmentService;
    }
    
    @Override
    protected String resolveApplicationMessage(String message, Object... params) throws UnifyException {
        Messages messages = getSessionAttribute(Messages.class,
                FlowCentralSessionAttributeConstants.ALTERNATIVE_RESOURCES_BUNDLE);
        String msg = messages != null ? messages.resolveMessage(message, params) : null;
        return msg == null ? super.resolveApplicationMessage(message, params) : msg;
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
    
    protected <T> T extractResult(PostResp<T> resp) throws UnifyException {
        if (resp.isError()) {
            throwOperationErrorException(new Exception(resp.getError()));
        }

        return resp.getResult();
    }

    protected void keepThreadAndClusterSafe(String expiryFieldName, Query<? extends Entity> query)
            throws UnifyException {
        heartbeatManager.startHeartbeat(query, expiryFieldName, KEEP_CLUSTER_SAFE_IN_MINUTES);
    }

    protected final boolean enterSystemRestoreMode() throws UnifyException {
        return tryGrabLock(SYSTEM_RESTORE_LOCK);
    }

    protected final void exitSystemRestoreMode() throws UnifyException {
        releaseLock(SYSTEM_RESTORE_LOCK);
    }
    
    protected final boolean isStale(Query<? extends Entity> query, VersionedEntityDef baseDef) throws UnifyException {
        final Optional<Long> versionOpt = environment().valueOptional(long.class, "versionNo",
                query.addEquals("id", baseDef.getId()));
        return !versionOpt.isPresent() || versionOpt.get() > baseDef.getVersion();
    }
    
    protected abstract void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException;

}
