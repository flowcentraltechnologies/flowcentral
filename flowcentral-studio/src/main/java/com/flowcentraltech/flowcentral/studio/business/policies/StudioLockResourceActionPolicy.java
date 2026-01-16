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

package com.flowcentraltech.flowcentral.studio.business.policies;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationCollaborationUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.RequestUserPhotoGenerator;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractCollaborationFormActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.CollaborationLockInfo;
import com.flowcentraltech.flowcentral.common.data.CollaborationLockedResourceInfo;
import com.flowcentraltech.flowcentral.organization.entities.MappedBranch;
import com.flowcentraltech.flowcentral.security.business.SecurityModuleService;
import com.flowcentraltech.flowcentral.security.entities.User;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Studio lock resource action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = "lockresource-actionpolicy", description = "$m{studio.entityactionpolicy.lockresource}")
public class StudioLockResourceActionPolicy extends AbstractCollaborationFormActionPolicy {

    @Configurable
    private SecurityModuleService securityModuleService;

    @Configurable
    private RequestUserPhotoGenerator requestUserPhotoGenerator;

    @Override
    public boolean checkAppliesTo(ValueStoreReader reader) throws UnifyException {
        final BaseApplicationEntity _appInst = (BaseApplicationEntity) reader.getValueObject();
        if (isCollaboration()) {
            final ConfigType configType = _appInst.getConfigType();
            if (!configType.isStatic()) {
                final CollaborationType type = ApplicationCollaborationUtils.getCollaborationType(_appInst.getClass());
                if (type != null) {
                    final String resourceName = ApplicationNameUtils
                            .getApplicationEntityLongName(_appInst.getApplicationName(), _appInst.getName());
                    return !getCollaborationProvider().isFrozen(type, resourceName)
                            && !getCollaborationProvider().isLockedBy(type, resourceName, getUserLoginId());
                }
            }
        }

        return false;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        final BaseApplicationEntity _appInst = (BaseApplicationEntity) ctx.getInst();
        final CollaborationType type = ApplicationCollaborationUtils.getCollaborationType(_appInst.getClass());
        final String resourceName = ApplicationNameUtils.getApplicationEntityLongName(_appInst.getApplicationName(),
                _appInst.getName());
        if (!getCollaborationProvider().lock(type, resourceName, getUserLoginId())) {
            CollaborationLockInfo collaborationLockInfo = getCollaborationProvider().getLockInfo(type, resourceName);
            if (collaborationLockInfo != null) {
                User user = securityModuleService.findUser(collaborationLockInfo.getLockedBy());
                final MappedBranch userBranch = user.getBranchId() != null
                        ? environment().find(MappedBranch.class, user.getBranchId())
                        : null;
                String branchDesc = userBranch != null ? userBranch.getDescription() : null;
                branchDesc = branchDesc == null ? getSessionMessage("application.no.branch") : branchDesc;
                CollaborationLockedResourceInfo collaborationLockedResourceInfo = new CollaborationLockedResourceInfo(
                        requestUserPhotoGenerator, type, resourceName, collaborationLockInfo.getLockedBy(),
                        user.getFullName(), branchDesc, collaborationLockInfo.getLockDate());
                setSessionAttribute(FlowCentralSessionAttributeConstants.LOCKED_RESOURCEOPTIONS,
                        collaborationLockedResourceInfo);
                return new EntityActionResult(ctx, "/application/commonutilities/showLockedResource");
            }
        }

        return null;
    }

}
