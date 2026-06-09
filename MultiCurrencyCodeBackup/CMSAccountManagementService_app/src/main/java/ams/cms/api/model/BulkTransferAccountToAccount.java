package ams.cms.api.model;

import java.io.Serializable;

public class BulkTransferAccountToAccount implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int strId;
	private String strFromAccountNo;
	private String strFromAccountType;
	private String strToAccountNo;
	private String strToAccountType;
	private Double strTransactionAmount;
	private String responseMessage;
	
	public int getStrId() {
		return strId;
	}
	public void setStrId(int strId) {
		this.strId = strId;
	}
	public String getStrFromAccountNo() {
		return strFromAccountNo;
	}
	public void setStrFromAccountNo(String strFromAccountNo) {
		this.strFromAccountNo = strFromAccountNo;
	}
	public String getStrFromAccountType() {
		return strFromAccountType;
	}
	public void setStrFromAccountType(String strFromAccountType) {
		this.strFromAccountType = strFromAccountType;
	}
	public String getStrToAccountNo() {
		return strToAccountNo;
	}
	public void setStrToAccountNo(String strToAccountNo) {
		this.strToAccountNo = strToAccountNo;
	}
	public String getStrToAccountType() {
		return strToAccountType;
	}
	public void setStrToAccountType(String strToAccountType) {
		this.strToAccountType = strToAccountType;
	}
	public Double getStrTransactionAmount() {
		return strTransactionAmount;
	}
	public void setStrTransactionAmount(Double strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}	
}
