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

package com.flowcentraltech.flowcentral.studio.business.policies;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.flowcentraltech.flowcentral.studio.util.StudioNameUtils;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Studio on create component policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiooncreatecomponent-policy")
public class StudioOnCreateComponentPolicy extends AbstractStudioAppletActionPolicy {

    @Override
    public boolean checkAppliesTo(Entity inst) throws UnifyException {
        return true;
    }

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        String applicationName = (String) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_NAME);
        Long applicationId = (Long) getSessionAttribute(StudioSessionAttributeConstants.CURRENT_APPLICATION_ID);
        StudioAppComponentType type = StudioAppComponentType
                .fromEntityClass((Class<? extends BaseApplicationEntity>) ctx.getInst().getClass());
        // Register instance as privilege based on type
        registerPrivilege(ConfigType.CUSTOM, applicationName, applicationId, type, (BaseApplicationEntity) ctx.getInst());

        // Set result path
        String appletName = StudioNameUtils.getStudioAppletName(applicationName, type, (Long) ctx.getInst().getId());
        String path = ApplicationPageUtils.constructAppletReplacePagePath(type.appletPath(), appletName);
        EntityActionResult result = new EntityActionResult(ctx, path);
        result.setRefreshMenu(true);
        return result;
    }
}
