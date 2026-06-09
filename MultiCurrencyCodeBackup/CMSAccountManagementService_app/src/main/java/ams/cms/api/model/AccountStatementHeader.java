package ams.cms.api.model;

public class AccountStatementHeader 
{
	private String strEmail;
	private String strAccountType;
	private String strClosingBalance;
	private String strDescription;
	private String strAccountHolderAdress;
	private String strAccountHolderName;
	private String strAccountNumber;
	
	public String getStrAccountHolderName() {
		return strAccountHolderName;
	}
	public void setStrAccountHolderName(String strAccountHolderName) {
		this.strAccountHolderName = strAccountHolderName;
	}
	
	public String getStrAccountNumber() {
		return strAccountNumber;
	}
	public void setStrAccountNumber(String strAccountNumber) {
		this.strAccountNumber = strAccountNumber;
	}
	public String getStrEmail() {
		return strEmail;
	}
	public String getStrAccountHolderAdress() {
		return strAccountHolderAdress;
	}
	public void setStrAccountHolderAdress(String strAccountHolderAdress) {
		this.strAccountHolderAdress = strAccountHolderAdress;
	}
	public void setStrEmail(String strEmail) {
		this.strEmail = strEmail;
	}
	public String getStrAccountType() {
		return strAccountType;
	}
	public void setStrAccountType(String strAccountType) {
		this.strAccountType = strAccountType;
	}
	public String getStrClosingBalance() {
		return strClosingBalance;
	}
	public void setStrClosingBalance(String strClosingBalance) {
		this.strClosingBalance = strClosingBalance;
	}
	public String getStrDescription() {
		return strDescription;
	}
	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}
	


}
