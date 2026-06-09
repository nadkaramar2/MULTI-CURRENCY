package ams.cms.model;

public class LoadMoneyRequest {

	private String baseWallet;  
	private String channel;
	private String currencyCode;  
	private Double loadAmount;
	private String participantId;
	private String txnType;
	private String pin;
	private String custId;

	
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getBaseWallet() {
		return baseWallet;
	}
	public void setBaseWallet(String baseWallet) {
		this.baseWallet = baseWallet;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Double getLoadAmount() {
		return loadAmount;
	}
	public void setLoadAmount(Double loadAmount) {
		this.loadAmount = loadAmount;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	    
	
	
	
	
}
