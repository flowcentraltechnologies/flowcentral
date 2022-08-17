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
package com.flowcentraltech.flowcentral.workspace.entities;

import java.util.Collection;
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Workspace privilege query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkspacePrivilegeQuery extends BaseAuditEntityQuery<WorkspacePrivilege> {

    public WorkspacePrivilegeQuery() {
        super(WorkspacePrivilege.class);
    }

    public WorkspacePrivilegeQuery workspaceId(Long workspaceId) {
        return (WorkspacePrivilegeQuery) addEquals("workspaceId", workspaceId);
    }

    public WorkspacePrivilegeQuery workspaceIdIn(List<Long> workspaceIdList) {
        return (WorkspacePrivilegeQuery) addAmongst("workspaceId", workspaceIdList);
    }

    public WorkspacePrivilegeQuery workspaceCode(String workspaceCode) {
        return (WorkspacePrivilegeQuery) addEquals("workspaceCode", workspaceCode);
    }

    public WorkspacePrivilegeQuery privilegeCatCode(String privilegeCatCode) {
        return (WorkspacePrivilegeQuery) addEquals("privilegeCatCode", privilegeCatCode);
    }

    public WorkspacePrivilegeQuery privilegeId(Long privilegeId) {
        return (WorkspacePrivilegeQuery) addEquals("privilegeId", privilegeId);
    }

    public WorkspacePrivilegeQuery privilegeIdIn(Collection<Long> privilegeId) {
        return (WorkspacePrivilegeQuery) addAmongst("privilegeId", privilegeId);
    }

    public WorkspacePrivilegeQuery privilegeIdNotIn(Collection<Long> privilegeId) {
        return (WorkspacePrivilegeQuery) addNotAmongst("privilegeId", privilegeId);
    }

    @Override
    public WorkspacePrivilegeQuery addSelect(String field) {
        return (WorkspacePrivilegeQuery) super.addSelect(field);
    }

    @Override
    public WorkspacePrivilegeQuery addOrder(String field) {
        return (WorkspacePrivilegeQuery) super.addOrder(field);
    }
}
