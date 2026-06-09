package ams.cms.model;

import ams.cms.api.model.AccountMaster;

public class LoadMoneyAccountingRequest {
	
	private LoadMoneyRequest loadMoneyRequest;
	private AccountMaster BaseWalletAccount;
	private MultiCurrencyWalletAccountMaster currencyWalletAccount;
	private MultiCurrencyFeeTypeMaster multiCurrencyFeeTypeMaster;
	private GstTypeMaster gstTypeMaster;
	private CurrencyMaster currencyMaster;
	private  Double tcsAmount;
	private boolean isTcsApplicable;
	private Double feeAmount;
	private Double gstAmount;
	private Double convertedAmount;
	private Double totalAmount;
	private Double conversionRate;
	private String participantId;
	private String txnType;
	
	public double getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(double conversionRate) {
		this.conversionRate = conversionRate;
	}
	public LoadMoneyRequest getLoadMoneyRequest() {
		return loadMoneyRequest;
	}
	public void setLoadMoneyRequest(LoadMoneyRequest loadMoneyRequest) {
		this.loadMoneyRequest = loadMoneyRequest;
	}
	
	public MultiCurrencyWalletAccountMaster getCurrencyWalletAccount() {
		return currencyWalletAccount;
	}
	public void setCurrencyWalletAccount(MultiCurrencyWalletAccountMaster currencyWalletAccount) {
		this.currencyWalletAccount = currencyWalletAccount;
	}
	public double getTcsAmount() {
		return tcsAmount;
	}
	public void setTcsAmount(double tcsAmount) {
		this.tcsAmount = tcsAmount;
	}
	
	
	public AccountMaster getBaseWalletAccount() {
		return BaseWalletAccount;
	}
	public void setBaseWalletAccount(AccountMaster baseWalletAccount) {
		BaseWalletAccount = baseWalletAccount;
	}
	public double getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}
	public double getGstAmount() {
		return gstAmount;
	}
	public void setGstAmount(double gstAmount) {
		this.gstAmount = gstAmount;
	}
	public double getConvertedAmount() {
		return convertedAmount;
	}
	public void setConvertedAmount(double convertedAmount) {
		this.convertedAmount = convertedAmount;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public boolean isTcsApplicable() {
		return isTcsApplicable;
	}
	public void setTcsApplicable(boolean isTcsApplicable) {
		this.isTcsApplicable = isTcsApplicable;
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
	public MultiCurrencyFeeTypeMaster getMultiCurrencyFeeTypeMaster() {
		return multiCurrencyFeeTypeMaster;
	}
	public void setMultiCurrencyFeeTypeMaster(MultiCurrencyFeeTypeMaster multiCurrencyFeeTypeMaster) {
		this.multiCurrencyFeeTypeMaster = multiCurrencyFeeTypeMaster;
	}
	public GstTypeMaster getGstTypeMaster() {
		return gstTypeMaster;
	}
	public void setGstTypeMaster(GstTypeMaster gstTypeMaster) {
		this.gstTypeMaster = gstTypeMaster;
	}
	public CurrencyMaster getCurrencyMaster() {
		return currencyMaster;
	}
	public void setCurrencyMaster(CurrencyMaster currencyMaster) {
		this.currencyMaster = currencyMaster;
	}
	

	
}
