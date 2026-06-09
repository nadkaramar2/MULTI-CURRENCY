package ams.cms.model;

public class LoadMoneyTransactionResponse {
	
	private String requestAmount;
	private String convertedAmount;
	private String transaferToCurrency;
	private String FeeAmount;
	private String gstAmount;
	private String tcsAmount;
	private String amountLoaded;
	
	
	public String getRequestAmount() {
		return requestAmount;
	}
	public void setRequestAmount(String requestAmount) {
		this.requestAmount = requestAmount;
	}
	public String getConvertedAmount() {
		return convertedAmount;
	}
	public void setConvertedAmount(String convertedAmount) {
		this.convertedAmount = convertedAmount;
	}
	public String getTransaferToCurrency() {
		return transaferToCurrency;
	}
	public void setTransaferToCurrency(String transaferToCurrency) {
		this.transaferToCurrency = transaferToCurrency;
	}
	public String getFeeAmount() {
		return FeeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		FeeAmount = feeAmount;
	}
	public String getGstAmount() {
		return gstAmount;
	}
	public void setGstAmount(String gstAmount) {
		this.gstAmount = gstAmount;
	}
	public String getTcsAmount() {
		return tcsAmount;
	}
	public void setTcsAmount(String tcsAmount) {
		this.tcsAmount = tcsAmount;
	}
	public String getAmountLoaded() {
		return amountLoaded;
	}
	public void setAmountLoaded(String amountLoaded) {
		this.amountLoaded = amountLoaded;
	}
	
	
	
	

}
