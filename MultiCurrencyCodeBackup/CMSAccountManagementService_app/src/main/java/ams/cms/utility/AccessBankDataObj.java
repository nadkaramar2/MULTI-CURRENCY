package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessBankDataObj implements Serializable
{
	private static final long serialVersionUID = 1L;
	private	String accountNo;
	private	String accountName;
	private	String BVN;
	private	String branchCode;
	
	private String availableBalance;
	
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBVN() {
		return BVN;
	}
	public void setBVN(String bVN) {
		BVN = bVN;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	
	public String getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}
	@Override
	public String toString() {
		return "AccessBankDataObj [accountNo=" + accountNo + ", accountName=" + accountName + ", BVN=" + BVN
				+ ", branchCode=" + branchCode + "]";
	}
}
