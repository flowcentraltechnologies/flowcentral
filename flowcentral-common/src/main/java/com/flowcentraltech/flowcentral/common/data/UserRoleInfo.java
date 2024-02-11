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

package com.flowcentraltech.flowcentral.common.data;

/**
 * User role information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UserRoleInfo {

    private String roleCode;

    private String roleDesc;

    private String groupName;

    private String groupDesc;

    private String departmentCode;

    public UserRoleInfo(String roleCode, String roleDesc, String groupName, String groupDesc, String departmentCode) {
        this.roleCode = roleCode;
        this.roleDesc = roleDesc;
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.departmentCode = departmentCode;
    }

    public UserRoleInfo(String roleCode, String roleDesc, String departmentCode) {
        this.roleCode = roleCode;
        this.roleDesc = roleDesc;
        this.departmentCode = departmentCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }
}
