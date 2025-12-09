package com.flowcentraltech.flowcentral.security.business.data;

import java.util.List;


public class UserDetail {

	private String fullName;
	private String email;
	private String mobileNo;
	private Boolean supervisor;
	private List<String> userRoleCode;
	
	public UserDetail(String fullName, String email, String mobileNo,Boolean supervisor,
			List<String> userRoleCode) {
		this.fullName = fullName;
		this.email = email;
		this.mobileNo = mobileNo;
		this.supervisor = supervisor;
		this.userRoleCode = userRoleCode;
	}
	
	public UserDetail() {
		
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

	public Boolean getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Boolean supervisor) {
		this.supervisor = supervisor;
	}

	public List<String> getUserRoleCode() {
		return userRoleCode;
	}

	public void setUserRoleCode(List<String> userRoleCode) {
		this.userRoleCode = userRoleCode;
	}

	
	
}
