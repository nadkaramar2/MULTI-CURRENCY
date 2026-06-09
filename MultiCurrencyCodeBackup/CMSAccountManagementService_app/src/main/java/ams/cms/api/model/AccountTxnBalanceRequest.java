package ams.cms.api.model;

import java.io.Serializable;

public class AccountTxnBalanceRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String strAccountTranType;
	private String strAccountNumber;
	private String strFromDate;
	private String strToDate;
	
	public String getStrAccountTranType() {
		return strAccountTranType;
	}
	public void setStrAccountTranType(String strAccountTranType) {
		this.strAccountTranType = strAccountTranType;
	}
	public String getStrAccountNumber() {
		return strAccountNumber;
	}
	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}
	public String getStrFromDate() {
		return strFromDate;
	}
	public void setStrFromDate(String strFromDate) {
		this.strFromDate = strFromDate;
	}
	public String getStrToDate() {
		return strToDate;
	}
	public void setStrToDate(String strToDate) {
		this.strToDate = strToDate;
	}

}
