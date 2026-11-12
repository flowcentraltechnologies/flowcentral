/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import java.util.Collection;
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Workspace privilege query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WorkspacePrivilegeQuery extends BaseAuditEntityQuery<WorkspacePrivilege> {

    public WorkspacePrivilegeQuery() {
        super(WorkspacePrivilege.class);
    }

    public WorkspacePrivilegeQuery workspaceId(Long workspaceId) {
        return (WorkspacePrivilegeQuery) equals("workspaceId", workspaceId);
    }

    public WorkspacePrivilegeQuery workspaceIdIn(List<Long> workspaceIdList) {
        return (WorkspacePrivilegeQuery) amongst("workspaceId", workspaceIdList);
    }

    public WorkspacePrivilegeQuery workspaceCode(String workspaceCode) {
        return (WorkspacePrivilegeQuery) equals("workspaceCode", workspaceCode);
    }

    public WorkspacePrivilegeQuery privilegeCatCode(String privilegeCatCode) {
        return (WorkspacePrivilegeQuery) equals("privilegeCatCode", privilegeCatCode);
    }

    public WorkspacePrivilegeQuery privilegeId(Long privilegeId) {
        return (WorkspacePrivilegeQuery) equals("privilegeId", privilegeId);
    }

    public WorkspacePrivilegeQuery privilegeIdIn(Collection<Long> privilegeId) {
        return (WorkspacePrivilegeQuery) amongst("privilegeId", privilegeId);
    }

    public WorkspacePrivilegeQuery privilegeIdNotIn(Collection<Long> privilegeId) {
        return (WorkspacePrivilegeQuery) notamongst("privilegeId", privilegeId);
    }

    @Override
    public WorkspacePrivilegeQuery select(String field) {
        return (WorkspacePrivilegeQuery) super.select(field);
    }

    @Override
    public WorkspacePrivilegeQuery order(String field) {
        return (WorkspacePrivilegeQuery) super.order(field);
    }
}
