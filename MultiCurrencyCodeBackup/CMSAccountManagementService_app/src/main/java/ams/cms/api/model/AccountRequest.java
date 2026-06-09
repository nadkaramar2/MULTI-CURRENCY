package ams.cms.api.model;

import java.io.Serializable;

public class AccountRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	private String strAccountNo;
	private String strAccountType;
	public String getStrAccountNo() {
		return strAccountNo;
	}
	public void setStrAccountNo(String strAccountNo) {
		this.strAccountNo = strAccountNo;
	}
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
}
