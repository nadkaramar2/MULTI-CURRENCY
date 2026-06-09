package ams.cms.model;

public class PriorityWalletResponse {
	
	private String accountType;
	private String walletAccountNumber;
	private int priority;
	
	
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getWalletAccountNumber() {
		return walletAccountNumber;
	}
	public void setWalletAccountNumber(String walletAccountNumber) {
		this.walletAccountNumber = walletAccountNumber;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	

}
