package ams.cms.api.model;

public class BulkTransferToAccounts {

	private int strId;
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
