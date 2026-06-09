package ams.cms.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkTransferFromAccounts implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int strId;
	private String strFromAccountNo;
	private String strFromAccountType;
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
