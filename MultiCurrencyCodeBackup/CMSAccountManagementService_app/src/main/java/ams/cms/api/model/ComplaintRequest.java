package ams.cms.api.model;

import java.io.Serializable;
//created by ankit
public class ComplaintRequest implements Serializable {

	private static final long serialVersionUID = 8770122426719937820L;

	private String strComplaintType;
	
	private String strComplaintDescription;
	
	/*
	private String strMobileNo;
	*/
	private String strAccountNo;
	private String strAccountType;
	
	public String getStrComplaintType() {
		return strComplaintType;
	}

	public void setStrComplaintType(String strComplaintType) {
		this.strComplaintType = strComplaintType;
	}

	public String getStrComplaintDescription() {
		return strComplaintDescription;
	}

	public void setStrComplaintDescription(String strComplaintDescription) {
		this.strComplaintDescription = strComplaintDescription;
	}
	/*
	public String getStrMobileNo() {
		return strMobileNo;
	}

	public void setStrMobileNo(String strMobileNo) {
		this.strMobileNo = strMobileNo;
	}*/
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

	public ComplaintRequest() {
		super();
	}
	
	
	
}
