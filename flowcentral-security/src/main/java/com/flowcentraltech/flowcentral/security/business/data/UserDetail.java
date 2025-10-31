package com.flowcentraltech.flowcentral.security.business.data;

public class UserDetail {

	private String fullName;
	private String loginId;
	private String password;
	private String email;
	private String mobileNo;
	
	public UserDetail(String fullName,String loginId, String password, String email, String mobileNo) {
		this.fullName = fullName;
		this.loginId = loginId;
		this.password = password;
		this.email = email;
		this.mobileNo = mobileNo;
	}
	
	public UserDetail() {
		
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	
}
