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
package com.flowcentraltech.flowcentral.security.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntity;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.data.Describable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity for storing user group member information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_USERGROUPMEMBER", uniqueConstraints = { @UniqueConstraint({ "userGroupId", "userId" }) })
public class UserGroupMember extends BaseAuditTenantEntity implements Describable {

    @ForeignKey(UserGroup.class)
    private Long userGroupId;

    @ForeignKey(User.class)
    private Long userId;

    @ListOnly(key = "userGroupId", property = "name")
    private String userGroupName;

    @ListOnly(key = "userGroupId", property = "description")
    private String userGroupDesc;

    @ListOnly(key = "userGroupId", property = "email")
    private String userGroupEmail;

    @ListOnly(key = "userId", property = "loginId")
    private String userLoginId;

    @ListOnly(key = "userId", property = "fullName")
    private String userFullName;

    @ListOnly(key = "userId", property = "email")
    private String userEmail;

    @ListOnly(key = "userId", property = "branchId")
    private Long userBranchId;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(userGroupDesc, " - ", userFullName);
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getUserGroupDesc() {
        return userGroupDesc;
    }

    public void setUserGroupDesc(String userGroupDesc) {
        this.userGroupDesc = userGroupDesc;
    }

    public String getUserGroupEmail() {
        return userGroupEmail;
    }

    public void setUserGroupEmail(String userGroupEmail) {
        this.userGroupEmail = userGroupEmail;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getUserBranchId() {
        return userBranchId;
    }

    public void setUserBranchId(Long userBranchId) {
        this.userBranchId = userBranchId;
    }

}
