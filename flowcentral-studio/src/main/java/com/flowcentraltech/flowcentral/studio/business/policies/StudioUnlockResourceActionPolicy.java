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
import com.flowcentraltech.flowcentral.common.business.policies.AbstractCollaborationFormActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Studio unlock resource action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = "unlockresource-actionpolicy", description = "$m{studio.entityactionpolicy.unlockresource}")
public class StudioUnlockResourceActionPolicy extends AbstractCollaborationFormActionPolicy {

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
                    return !getCollaborationProvider().isFrozen(type, resourceName) && getCollaborationProvider()
                            .isLockedBy(type, resourceName, getUserToken().getUserLoginId());
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
        getCollaborationProvider().unlock(type, resourceName, getUserToken().getUserLoginId());
        return null;
    }

}
