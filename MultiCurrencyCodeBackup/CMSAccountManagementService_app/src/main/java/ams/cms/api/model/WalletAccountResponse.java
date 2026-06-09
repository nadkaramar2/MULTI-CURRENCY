package ams.cms.api.model;

public class WalletAccountResponse {
	private String walletAccountNumber;
	private String balance;
	private int priority;
	private String currencyCode;
	public String getWalletAccountNumber() {
		return walletAccountNumber;
	}
	public void setWalletAccountNumber(String walletAccountNumber) {
		this.walletAccountNumber = walletAccountNumber;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	

}
