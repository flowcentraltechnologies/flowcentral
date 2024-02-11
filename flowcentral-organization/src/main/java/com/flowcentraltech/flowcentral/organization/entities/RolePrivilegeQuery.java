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
package com.flowcentraltech.flowcentral.organization.entities;

import java.util.Collection;
import java.util.List;

import com.flowcentraltech.flowcentral.common.constants.WfItemVersionType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntityQuery;

/**
 * Role privilege query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RolePrivilegeQuery extends BaseAuditTenantEntityQuery<RolePrivilege> {

    public RolePrivilegeQuery() {
        super(RolePrivilege.class);
    }

    public RolePrivilegeQuery roleId(Long roleId) {
        return (RolePrivilegeQuery) addEquals("roleId", roleId);
    }

    public RolePrivilegeQuery roleIdIn(List<Long> roleIdList) {
        return (RolePrivilegeQuery) addAmongst("roleId", roleIdList);
    }

    public RolePrivilegeQuery roleCode(String roleCode) {
        return (RolePrivilegeQuery) addEquals("roleCode", roleCode);
    }

    public RolePrivilegeQuery roleWfItemVersionType(WfItemVersionType roleWfItemVersionType) {
        return (RolePrivilegeQuery) addEquals("roleWfItemVersionType", roleWfItemVersionType);
    }

    public RolePrivilegeQuery privilegeCatCode(String privilegeCatCode) {
        return (RolePrivilegeQuery) addEquals("privilegeCatCode", privilegeCatCode);
    }

    public RolePrivilegeQuery privilegeId(Long privilegeId) {
        return (RolePrivilegeQuery) addEquals("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery privilegeIdIn(Collection<Long> privilegeId) {
        return (RolePrivilegeQuery) addAmongst("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery privilegeIdNotIn(Collection<Long> privilegeId) {
        return (RolePrivilegeQuery) addNotAmongst("privilegeId", privilegeId);
    }

    public RolePrivilegeQuery moduleCode(String moduleCode) {
        return (RolePrivilegeQuery) addEquals("moduleCode", moduleCode);
    }

    @Override
    public RolePrivilegeQuery addSelect(String field) {
        return (RolePrivilegeQuery) super.addSelect(field);
    }

    @Override
    public RolePrivilegeQuery addOrder(String field) {
        return (RolePrivilegeQuery) super.addOrder(field);
    }
}
