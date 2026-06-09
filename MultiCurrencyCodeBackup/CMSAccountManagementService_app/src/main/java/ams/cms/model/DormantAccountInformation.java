package ams.cms.model;

public class DormantAccountInformation 
{
	private String accountType;
	private String description;
	private String creationDate;
	private String lastTxnDate;
	private String closingBalance;
	private String dormantMarkedDate;
	private String status;
	private String accountHolderName;
	private String accountNumber;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountHolderName() {
		return accountHolderName;
	}
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getLastTxnDate() {
		return lastTxnDate;
	}
	public void setLastTxnDate(String lastTxnDate) {
		this.lastTxnDate = lastTxnDate;
	}
	public String getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(String closingBalance) {
		this.closingBalance = closingBalance;
	}
	public String getDormantMarkedDate() {
		return dormantMarkedDate;
	}
	public void setDormantMarkedDate(String dormantMarkedDate) {
		this.dormantMarkedDate = dormantMarkedDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
