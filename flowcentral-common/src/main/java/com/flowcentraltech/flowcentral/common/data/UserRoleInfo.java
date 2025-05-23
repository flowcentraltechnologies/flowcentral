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

package com.flowcentraltech.flowcentral.common.data;

import java.util.List;

/**
 * User role information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class UserRoleInfo {

    private String roleCode;

    private String roleDesc;

    private String groupName;

    private String groupDesc;

    private String departmentCode;

    private List<Long> branchScopingList;

    public UserRoleInfo(String roleCode, String roleDesc, String groupName, String groupDesc, String departmentCode,
            List<Long> branchScopingList) {
        this.roleCode = roleCode;
        this.roleDesc = roleDesc;
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.departmentCode = departmentCode;
        this.branchScopingList = branchScopingList;
    }

    public UserRoleInfo(String roleCode, String roleDesc, String departmentCode, List<Long> branchScopingList) {
        this.roleCode = roleCode;
        this.roleDesc = roleDesc;
        this.departmentCode = departmentCode;
        this.branchScopingList = branchScopingList;
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

    public List<Long> getBranchScopingList() {
        return branchScopingList;
    }

}
