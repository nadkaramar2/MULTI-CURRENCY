package ams.cms.api.model;

import java.io.Serializable;

//created by ankit
public class AccountLimitRequest implements Serializable{

	private static final long serialVersionUID = 6003229220146638378L;
	private String strAccountType;
	private String strAccountNumber;
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
	public String getStrAccountNumber() {
		return strAccountNumber;
	}
	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}
}
