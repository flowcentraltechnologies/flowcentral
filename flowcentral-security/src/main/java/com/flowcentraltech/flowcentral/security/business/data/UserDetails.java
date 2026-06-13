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
package com.flowcentraltech.flowcentral.security.business.data;

import java.util.List;


/**
 * User details.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class UserDetails {

	private String fullName;
	
	private String email;
	
	private String mobileNo;
	
    private Boolean superBranchUser;
    
    private Boolean superDepartmentUser;
    
	private List<String> userRoleCode;
	

	public UserDetails(String fullName, String email, String mobileNo, Boolean superBranchUser,
            Boolean superDepartmentUser, List<String> userRoleCode) {
        this.fullName = fullName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.superBranchUser = superBranchUser;
        this.superDepartmentUser = superDepartmentUser;
        this.userRoleCode = userRoleCode;
    }

    public UserDetails() {
		
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMobileNo() {
		return mobileNo;
	}
	
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Boolean getSuperBranchUser() {
        return superBranchUser;
    }

    public void setSuperBranchUser(Boolean superBranchUser) {
        this.superBranchUser = superBranchUser;
    }

    public Boolean getSuperDepartmentUser() {
        return superDepartmentUser;
    }

    public void setSuperDepartmentUser(Boolean superDepartmentUser) {
        this.superDepartmentUser = superDepartmentUser;
    }

    public List<String> getUserRoleCode() {
		return userRoleCode;
	}

	public void setUserRoleCode(List<String> userRoleCode) {
		this.userRoleCode = userRoleCode;
	}

	
	
}
