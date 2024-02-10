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

package com.flowcentraltech.flowcentral.workspace.business.policies;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationAppletActionPolicy;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.workspace.entities.Workspace;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Workspace delete policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("workspace-deletepolicy")
public class WorkspaceDeletePolicy extends AbstractApplicationAppletActionPolicy {

    @Override
    protected EntityActionResult doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        return null;
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        final Workspace workspace = (Workspace) ctx.getInst();
        unregisterPrivilege("workspace", ApplicationPrivilegeConstants.APPLICATION_WORKSPACE_CATEGORY_CODE,
                PrivilegeNameUtils.getWorkspacePrivilegeName(workspace.getCode()));
        return null;
    }

}
