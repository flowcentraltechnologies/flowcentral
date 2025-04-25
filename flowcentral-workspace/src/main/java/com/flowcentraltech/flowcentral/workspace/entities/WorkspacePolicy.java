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
package com.flowcentraltech.flowcentral.workspace.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityPolicy;
import com.flowcentraltech.flowcentral.common.util.WorkspaceNamingUtils;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Workspace entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("workspace-entitypolicy")
public class WorkspacePolicy extends BaseStatusEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        Workspace workspace = (Workspace) record;
        Long id = (Long) super.preCreate(workspace, now);
        if (!DefaultApplicationConstants.ROOT_WORKSPACE_CODE.equals(workspace.getCode())) {
            workspace.setCode(WorkspaceNamingUtils.getWorkspaceCodeFromId(id));
        }

        return id;
    }

}
