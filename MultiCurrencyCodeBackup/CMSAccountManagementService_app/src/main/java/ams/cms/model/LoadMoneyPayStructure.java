package ams.cms.model;

public class LoadMoneyPayStructure {
	
	private String loadAmount;
	private String totalInrRequired;
	private String transferToCurrency;
	private String feeAmount;
	private String GstAmount;
	private String tcsAmount;
	
	public String getLoadAmount() {
		return loadAmount;
	}
	public void setLoadAmount(String loadAmount) {
		this.loadAmount = loadAmount;
	}
	public String getTotalInrRequired() {
		return totalInrRequired;
	}
	public void setTotalInrRequired(String totalInrRequired) {
		this.totalInrRequired = totalInrRequired;
	}
	public String getTransferToCurrency() {
		return transferToCurrency;
	}
	public void setTransferToCurrency(String transferToCurrency) {
		this.transferToCurrency = transferToCurrency;
	}
	public String getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getGstAmount() {
		return GstAmount;
	}
	public void setGstAmount(String gstAmount) {
		GstAmount = gstAmount;
	}
	public String getTcsAmount() {
		return tcsAmount;
	}
	public void setTcsAmount(String tcsAmount) {
		this.tcsAmount = tcsAmount;
	}
	
	

}
