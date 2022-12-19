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
package com.flowcentraltech.flowcentral.security.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusTenantEntity;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Entity for storing user group information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_USERGROUP",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class UserGroup extends BaseStatusTenantEntity {

    @Column(name = "USERGROUP_NM", length = 64)
    private String name;

    @Column(name = "USERGROUP_DESC", length = 96)
    private String description;

    @Column(length = 64, nullable = true)
    private String email;

    @ChildList
    private List<UserGroupRole> userGroupRoleList;

    @ChildList
    private List<UserGroupMember> userGroupMemberList;

    @Override
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserGroupRole> getUserGroupRoleList() {
        return userGroupRoleList;
    }

    public void setUserGroupRoleList(List<UserGroupRole> userGroupRoleList) {
        this.userGroupRoleList = userGroupRoleList;
    }

    public List<UserGroupMember> getUserGroupMemberList() {
        return userGroupMemberList;
    }

    public void setUserGroupMemberList(List<UserGroupMember> userGroupMemberList) {
        this.userGroupMemberList = userGroupMemberList;
    }

}
