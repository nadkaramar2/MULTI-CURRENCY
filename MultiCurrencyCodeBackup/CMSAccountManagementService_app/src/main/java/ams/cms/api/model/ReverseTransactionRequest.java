package ams.cms.api.model;

public class ReverseTransactionRequest {

	private String strTransactionType;
	private String strMerchantCustId;
	private String strMerchantAccountType;
	private String strMerchantAccountNumber;
	private String strMerchantPin;
	private Double strTransactionAmount;
	private String strCustAccountType;
	private String strCustAccountNumber;
	private String strOriginalTransactionId;
	public String getStrTransactionType() {
		return strTransactionType;
	}
	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}
	public String getStrMerchantCustId() {
		return strMerchantCustId;
	}
	public void setStrMerchantCustId(String strMerchantCustId) {
		this.strMerchantCustId = strMerchantCustId;
	}
	public String getStrMerchantAccountType() {
		return strMerchantAccountType;
	}
	public void setStrMerchantAccountType(String strMerchantAccountType) {
		this.strMerchantAccountType = strMerchantAccountType;
	}
	public String getStrMerchantPin() {
		return strMerchantPin;
	}
	public void setStrMerchantPin(String strMerchantPin) {
		this.strMerchantPin = strMerchantPin;
	}
	public Double getStrTransactionAmount() {
		return strTransactionAmount;
	}
	public void setStrTransactionAmount(Double strTransactionAmount) {
		this.strTransactionAmount = strTransactionAmount;
	}
	public String getStrCustAccountType() {
		return strCustAccountType;
	}
	public void setStrCustAccountType(String strCustAccountType) {
		this.strCustAccountType = strCustAccountType;
	}
	public String getStrCustAccountNumber() {
		return strCustAccountNumber;
	}
	public void setStrCustAccountNumber(String strCustAccountNumber) {
		this.strCustAccountNumber = strCustAccountNumber;
	}
	public String getStrOriginalTransactionId() {
		return strOriginalTransactionId;
	}
	public void setStrOriginalTransactionId(String strOriginalTransactionId) {
		this.strOriginalTransactionId = strOriginalTransactionId;
	}
	public String getStrMerchantAccountNumber() {
		return strMerchantAccountNumber;
	}
	public void setStrMerchantAccountNumber(String strMerchantAccountNumber) {
		this.strMerchantAccountNumber = strMerchantAccountNumber;
	}
}
