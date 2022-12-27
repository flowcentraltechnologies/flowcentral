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
package com.flowcentraltech.flowcentral.organization.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditTenantEntity;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Role privilege entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ROLEPRIVILEGE", uniqueConstraints = { @UniqueConstraint({ "roleId", "privilegeId" }) })
public class RolePrivilege extends BaseAuditTenantEntity {

    @ForeignKey(Role.class)
    private Long roleId;

    @ForeignKey(Privilege.class)
    private Long privilegeId;

    @ListOnly(key = "roleId", property = "code")
    private String roleCode;

    @ListOnly(key = "roleId", property = "description")
    private String roleDesc;

    @ListOnly(key = "privilegeId", property = "code")
    private String privilegeCode;

    @ListOnly(key = "privilegeId", property = "description")
    private String privilegeDesc;

    @ListOnly(key = "privilegeId", property = "privilegeCategoryId")
    private Long privilegeCategoryId;

    @ListOnly(key = "privilegeId", property = "privilegeCatCode")
    private String privilegeCatCode;

    @ListOnly(key = "privilegeId", property = "privilegeCatDesc")
    private String privilegeCatDesc;

    @ListOnly(key = "privilegeId", property = "applicationId")
    private Long applicationId;

    @ListOnly(key = "privilegeId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "privilegeId", property = "applicationDesc")
    private String applicationDesc;

    @Override
    public String getDescription() {
        return StringUtils.concatenate(roleDesc, " - ", privilegeDesc);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getPrivilegeCode() {
        return privilegeCode;
    }

    public void setPrivilegeCode(String privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    public String getPrivilegeDesc() {
        return privilegeDesc;
    }

    public void setPrivilegeDesc(String privilegeDesc) {
        this.privilegeDesc = privilegeDesc;
    }

    public Long getPrivilegeCategoryId() {
        return privilegeCategoryId;
    }

    public void setPrivilegeCategoryId(Long privilegeCategoryId) {
        this.privilegeCategoryId = privilegeCategoryId;
    }

    public String getPrivilegeCatCode() {
        return privilegeCatCode;
    }

    public void setPrivilegeCatCode(String privilegeCatCode) {
        this.privilegeCatCode = privilegeCatCode;
    }

    public String getPrivilegeCatDesc() {
        return privilegeCatDesc;
    }

    public void setPrivilegeCatDesc(String privilegeCatDesc) {
        this.privilegeCatDesc = privilegeCatDesc;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDesc() {
        return applicationDesc;
    }

    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

}
