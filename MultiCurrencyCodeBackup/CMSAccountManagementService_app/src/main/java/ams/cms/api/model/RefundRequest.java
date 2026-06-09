package ams.cms.api.model;


public class RefundRequest {

	private String strMerchantCustId;
	private String strMerchantAccountNo;
	private String strMerchantAccountType;
	private String strDateOfTransaction;
	private String strTransactionId;
	
	public String getStrMerchantCustId() {
		return strMerchantCustId;
	}
	public void setStrMerchantCustId(String strMerchantCustId) {
		this.strMerchantCustId = strMerchantCustId;
	}
	public String getStrMerchantAccountNo() {
		return strMerchantAccountNo;
	}
	public void setStrMerchantAccountNo(String strMerchantAccountNo) {
		this.strMerchantAccountNo = strMerchantAccountNo;
	}
	public String getStrMerchantAccountType() {
		return strMerchantAccountType;
	}
	public void setStrMerchantAccountType(String strMerchantAccountType) {
		this.strMerchantAccountType = strMerchantAccountType;
	}
	public String getStrDateOfTransaction() {
		return strDateOfTransaction;
	}
	public void setStrDateOfTransaction(String strDateOfTransaction) {
		this.strDateOfTransaction = strDateOfTransaction;
	}
	public String getStrTransactionId() {
		return strTransactionId;
	}
	public void setStrTransactionId(String strTransactionId) {
		this.strTransactionId = strTransactionId;
	}
}
