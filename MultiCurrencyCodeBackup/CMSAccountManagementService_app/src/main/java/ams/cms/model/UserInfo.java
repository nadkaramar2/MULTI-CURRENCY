package ams.cms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String jwtKey;
	private String name;
	private String mobileNo;
	private String emailId;
	private String lastLoginTime;
	private String isTermActive;
	private String isUserAuthorize;
	private String oldPassword;
	private String password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getIsTermActive() {
		return isTermActive;
	}

	public void setIsTermActive(String isTermActive) {
		this.isTermActive = isTermActive;
	}

	public String getJwtKey() {
		return jwtKey;
	}

	public void setJwtKey(String jwtKey) {
		this.jwtKey = jwtKey;
	}

	public String getIsUserAuthorize() {
		return isUserAuthorize;
	}

	public void setIsUserAuthorize(String isUserAuthorize) {
		this.isUserAuthorize = isUserAuthorize;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
